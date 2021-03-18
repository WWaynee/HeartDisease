package mapreduce.jobs;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import mapreduce.JobHelper;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

public class GetCount extends JobHelper {
  // 静态变量，被静态内部类MyMapper引用
  private static Integer count = 0;

  // 存入数据库的对象，转成JSON后存入
  @Data
  public class Result {
    private Integer count;
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
        count++;
      }
    }
  }

  // 静态运行方法，内部new一个对象并执行run方法
  public static void Run() throws Exception {
    new GetCount().run();
  }

  // 构造函数，
  public GetCount() {
    super("GetCount", // 任务名，可以与类名保持一致
        "/data/心脏病诊断数据集.csv", // 源数据，hdfs的路径
        "/test/jabin/CountAll-" + new Date().getTime(), // hadoop运行会产生数据，在hadoop的hdfs内保存的路径
        MyMapper.class);// 同上静态内部类
  }

  // 任务提交执行前会触发
  @Override
  public void preCompletion() throws Exception {
    System.out.println("GetCount:" + "preCompletion");
  }

  // 任务结束后触发，用于将最终结果持久化到mysql
  @Override
  public void afterCompletion() throws Exception {
    System.out.println("GetCount:" + "afterCompletion");
    System.out.println("GetCount:" + "count=" + count.toString());

    // 构建结果
    Result res = new Result();
    res.setCount(count);

    // 结果转换为json string
    String key = "GetCount";
    String value = JSON.toJSONString(res);

    // 保存key-value
    save(key, value);

    // 静态变量归零
    clean();
  }

  // 静态方法依赖静态变量
  // 执行完成最好恢复一下变量，避免下次使用的时候
  // 上次使用的数据造成影响
  public void clean() {
    count = 0;
  }
}
