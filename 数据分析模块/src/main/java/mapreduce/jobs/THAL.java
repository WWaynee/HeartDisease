package mapreduce.jobs;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import lombok.Data;
import mapreduce.JobHelper;
import mapreduce.entity.HeartDisease;

public class THAL extends JobHelper {
  public static int[] thalAll = new int[7 + 1];
  public static int[] thalPatient = new int[7 + 1];
  public static int patient = 0;

  // 存入数据库的对象，转成JSON后存入
  @Data
  public class DataHub {
    Double data;
    String description;
  }

  @Data
  public class Result {
    DataHub[] thal3;
    DataHub[] thal6;
    DataHub[] thal7;
  }

  public static class MyMapper extends Mapper<Object, Text, Text, Text> {
    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, Text, Text>.Context context)
        throws IOException, InterruptedException {
      String src = value.toString();
      // 只读取数字
      Pattern pattern = Pattern.compile("-?[0-9]+.*[0-9]*");
      if (pattern.matcher(src.substring(0, 2)).matches()) {
        HeartDisease obj = new HeartDisease(src);
        thalAll[obj.getThal().intValue()]++;
        if (obj.getTarget() >= 2.0) {
          thalPatient[obj.getThal().intValue()]++;
          patient++;
        }
      }

    }
  }

  public THAL() {
    super("THAL", "/data/心脏病诊断数据集.csv", "/test/jabin/Hyperglycemia-" + new Date().getTime(), MyMapper.class);
  }

  public static void Run() throws Exception {
    new THAL().run();
  }

  @Override
  public void preCompletion() throws Exception {
  }

  @Override
  public void afterCompletion() throws Exception {
    // 计算结果
    DataHub thal3_1 = new DataHub();
    thal3_1.setDescription("发病的病人中，THAL值为3患者的比例");
    thal3_1.setData(Double.valueOf(thalPatient[3]) / Double.valueOf(patient));
    DataHub thal3_2 = new DataHub();
    thal3_2.setDescription("THAL值为3的人发病的几率");
    thal3_2.setData(Double.valueOf(thalPatient[3]) / Double.valueOf(thalAll[3]));

    DataHub thal6_1 = new DataHub();
    thal6_1.setDescription("发病的病人中，THAL值为6患者的比例");
    thal6_1.setData(Double.valueOf(thalPatient[6]) / Double.valueOf(patient));
    DataHub thal6_2 = new DataHub();
    thal6_2.setDescription("THAL值为6的人发病的几率");
    thal6_2.setData(Double.valueOf(thalPatient[6]) / Double.valueOf(thalAll[6]));

    DataHub thal7_1 = new DataHub();
    thal7_1.setDescription("发病的病人中，THAL值为7患者的比例");
    thal7_1.setData(Double.valueOf(thalPatient[7]) / Double.valueOf(patient));
    DataHub thal7_2 = new DataHub();
    thal7_2.setDescription("THAL值为7的人发病的几率");
    thal7_2.setData(Double.valueOf(thalPatient[7]) / Double.valueOf(thalAll[7]));

    // 构建结果
    DataHub[] thal3 = new DataHub[] { thal3_1, thal3_2 };
    DataHub[] thal6 = new DataHub[] { thal6_1, thal6_2 };
    DataHub[] thal7 = new DataHub[] { thal7_1, thal7_2 };

    Result res = new Result();
    res.setThal3(thal3);
    res.setThal6(thal6);
    res.setThal7(thal7);

    // 结果转换为json string
    String key = "THAL";
    String value = JSON.toJSONString(res);

    // 保存key-value
    save(key, value);

    // 静态变量归零
    clean();
  }

  public void clean() {
    thalAll = new int[7 + 1];
    thalPatient = new int[7 + 1];
    patient = 0;
  }
}