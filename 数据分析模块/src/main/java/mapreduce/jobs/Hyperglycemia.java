package mapreduce.jobs;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import mapreduce.JobHelper;
import mapreduce.entity.HeartDisease;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

// 高血糖发病率
public class Hyperglycemia extends JobHelper {
  public static Integer patientNumber = 0;
  public static Integer hyperPatientNumber = 0;
  public static Integer hyperNumer = 0;

  // 存入数据库的对象，转成JSON后存入
  @Data
  public class DataHub {
    double data;
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
        if (obj.getTarget() >= 2.0) {
          patientNumber++;
          if (obj.getFbs() > 0) {
            hyperPatientNumber++;
          }
        }
        if (obj.getFbs() > 0) {
          hyperNumer++;
        }
      }
    }

  }

  public static void Run() throws Exception {
    new Hyperglycemia().run();
  }

  // 构造函数
  public Hyperglycemia() {
    super("Hyperglycemia", "/data/心脏病诊断数据集.csv", "/test/jabin/Hyperglycemia-" + new Date().getTime(), MyMapper.class);
  }

  @Override
  public void preCompletion() throws Exception {

  }

  @Override
  public void afterCompletion() throws Exception {
    // 计算结果
    DataHub data1 = new DataHub();
    data1.setDescription("发病的病人中，糖尿病患者的比例");
    data1.setData(Double.valueOf(hyperPatientNumber.toString()) / Double.valueOf(patientNumber.toString()));

    DataHub data2 = new DataHub();
    data2.setDescription("糖尿病患者发病的比例");
    data2.setData(Double.valueOf(hyperPatientNumber.toString()) / Double.valueOf(hyperNumer.toString()));

    // 构建结果
    DataHub[] res = new DataHub[] { data1, data2 };

    // 结果转换为json string
    String key = "Hyperglycemia";
    String value = JSON.toJSONString(res);

    // 保存key-value
    save(key, value);

    // 静态变量归零
    clean();
  }

  public void clean() {
    patientNumber = 0;
    hyperPatientNumber = 0;
  }
}
