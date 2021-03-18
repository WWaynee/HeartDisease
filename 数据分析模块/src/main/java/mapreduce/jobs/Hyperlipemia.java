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

public class Hyperlipemia extends JobHelper {
  public static int hyperPatient = 0;
  public static int hyperAll = 0;
  public static int patient = 0;

  // 存入数据库的对象，转成JSON后存入
  @Data
  public class DataHub {
    Double data;
    String description;
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

        if (obj.getChol() > 220) {
          hyperAll++;
        }
        if (obj.getTarget() >= 2.0) {
          patient++;
          if (obj.getChol() > 220) {
            hyperPatient++;
          }
        }
      }

    }
  }

  public Hyperlipemia() {
    super("Hyperlipemia", "/data/心脏病诊断数据集.csv", "/test/jabin/Hyperglycemia-" + new Date().getTime(), MyMapper.class);
  }

  public static void Run() throws Exception {
    new Hyperlipemia().run();
  }

  @Override
  public void preCompletion() throws Exception {
  }

  @Override
  public void afterCompletion() throws Exception {
    // 计算结果
    DataHub data1 = new DataHub();
    data1.setDescription("发病的病人中，高血脂的比例");
    data1.setData(Double.valueOf(hyperPatient) / Double.valueOf(patient));
    DataHub data2 = new DataHub();
    data2.setDescription("高血脂的人发病的几率");
    data2.setData(Double.valueOf(hyperPatient) / Double.valueOf(hyperAll));

    // 构建结果
    DataHub[] res = new DataHub[] { data1, data2 };

    // 结果转换为json string
    String key = "Hyperlipemia";
    String value = JSON.toJSONString(res);

    // 保存key-value
    save(key, value);

    // 静态变量归零
    clean();
  }

  public void clean() {
    hyperPatient = 0;
    hyperAll = 0;
    patient = 0;
  }
}