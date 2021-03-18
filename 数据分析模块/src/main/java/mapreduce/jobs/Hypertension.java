package mapreduce.jobs;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import mapreduce.JobHelper;
import mapreduce.entity.HeartDisease;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

// 高血压相关统计
public class Hypertension extends JobHelper {
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
          if (obj.getTrestbps() >= 140) {
            hyperPatientNumber++;
          }
        }
        if (obj.getTrestbps() >= 140) {
          hyperNumer++;
        }
      }
    }

  }

  public static void Run() throws Exception {
    new Hypertension().run();
  }

  // 构造函数
  public Hypertension() {
    super("Hypertension", "/data/心脏病诊断数据集.csv", "/test/jabin/Hyperglycemia-" + new Date().getTime(), MyMapper.class);
  }

  @Override
  public void preCompletion() throws Exception {
  }

  @Override
  public void afterCompletion() throws Exception {
    // 计算结果
    DataHub data1 = new DataHub();
    data1.setDescription("发病的病人中，高血压患者的比例");
    data1.setData(Double.valueOf(hyperPatientNumber.toString()) / Double.valueOf(patientNumber.toString()));

    DataHub data2 = new DataHub();
    data2.setDescription("高血压患者发病的比例");
    data2.setData(Double.valueOf(hyperPatientNumber.toString()) / Double.valueOf(hyperNumer.toString()));

    // 构建结果
    DataHub[] res = new DataHub[] { data1, data2 };

    // 结果转换为json string
    String key = "Hypertension";
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
