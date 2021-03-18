package mapreduce.jobs;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import mapreduce.JobHelper;
import mapreduce.entity.HeartDisease;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainBloodVessels extends JobHelper {
  public static int[] vessAll = new int[4];
  public static int[] vessPatient = new int[4];
  private static Integer patient = 0;

  @Data
  public class DataHub {
    Double data;
    String description;
  }

  // 静态内部类MyMapper，主要是提供map函数，每条数据都会调用map函数
  // 可以利用这个做统计
  public static class MyMapper extends Mapper<Object, Text, Text, Text> {
    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, Text, Text>.Context context)
        throws IOException, InterruptedException {
      String src = value.toString();
      // 只读取数字
      Pattern pattern = Pattern.compile("-?[0-9]+.*[0-9]*");
      if (pattern.matcher(src.substring(0, 2)).matches()) {
        HeartDisease obj = new HeartDisease(src);
        vessAll[obj.getCa().intValue()]++;
        if (obj.getTarget() >= 2.0) {
          patient++;
          vessPatient[obj.getCa().intValue()]++;
        }
      }
    }
  }

  // 静态运行方法，内部new一个对象并执行run方法
  public static void Run() throws Exception {
    new MainBloodVessels().run();
  }

  // 构造函数，
  public MainBloodVessels() {
    super("MainBloodVessels", // 任务名，可以与类名保持一致
        "/data/心脏病诊断数据集.csv", // 源数据，hdfs的路径
        "/test/jabin/CountAll-" + new Date().getTime(), // hadoop运行会产生数据，在hadoop的hdfs内保存的路径
        MyMapper.class);// 同上静态内部类
  }

  // 任务提交执行前会触发
  @Override
  public void preCompletion() throws Exception {
  }

  @Override
  public void afterCompletion() throws Exception {
    // 计算结果
    int[] allIndex = new int[] { 0, 1, 2, 3 };
    Map<String, DataHub[]> res = new HashMap<String, DataHub[]>();

    for (int index : allIndex) {
      DataHub vess_0 = new DataHub();
      vess_0.setDescription("发病的病人中，主血管数为" + index + "的比例");
      vess_0.setData(Double.valueOf(vessPatient[index]) / Double.valueOf(patient));
      DataHub vess_1 = new DataHub();
      vess_1.setDescription("主血管数为" + index + "的人，发病的几率");
      vess_1.setData(Double.valueOf(vessPatient[index]) / Double.valueOf(vessAll[index]));
      res.put("vessel" + index, new DataHub[] { vess_0, vess_1 });
    }

    // 结果转换为json string
    String key = "MainBloodVessels";
    String value = JSON.toJSONString(res);

    // 保存key-value
    save(key, value);

    // 静态变量归零
    clean();
  }

  public void clean() {
    vessAll = new int[4];
    vessPatient = new int[4];
    patient = 0;
  }
}
