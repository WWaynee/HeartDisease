package mapreduce;

import io.ebean.DB;
import lombok.Data;
import mapreduce.entity.Hadoop;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

@Data
public abstract class JobHelper {
  // 外部指定
  private String fileSystem = "fs.defaultFS";
  private String jobName = "";
  private String hdfsAddr = "hdfs://fisher.lazybone.xyz:9000";
  private String inPath;
  private String outPath;
  Class<? extends Mapper<Object, Text, Text, Text>> mapperClass;

  // 内部创建
  private Job job;
  private Configuration conf;

  // 可供选择使用的函数
  public abstract void preCompletion() throws Exception;

  public abstract void afterCompletion() throws Exception;

  public JobHelper(String jobName, String inPath, String outPath,
      Class<? extends Mapper<Object, Text, Text, Text>> mapperClass) {
    setJobName(jobName);
    setInPath(inPath);
    setOutPath(outPath);
    setMapperClass(mapperClass);
  }

  public void run() throws Exception {
    init();
    submit();
  }

  private void init() throws Exception {
    initConfig();
    initJob();
    initIO();
  }

  private void submit() throws Exception {
    preCompletion();
    job.waitForCompletion(true);
    afterCompletion();
  }

  private void initConfig() {
    conf = new Configuration();
    conf.set(fileSystem, hdfsAddr);
  }

  private void initJob() throws Exception {
    job = Job.getInstance(conf, getJobName());
    job.setJarByClass(JobHelper.class);
    job.setMapperClass(mapperClass);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
  }

  private void initIO() throws Exception {
    FileInputFormat.addInputPath(job, new Path(inPath));
    FileOutputFormat.setOutputPath(job, new Path(outPath));
    LazyOutputFormat.setOutputFormatClass(job, TextOutputFormat.class);
  }

  public void save(String key, String value) {
    Hadoop entity = new Hadoop();
    entity.setResKey(key);
    entity.setResValue(value);
    Hadoop exist = DB.find(Hadoop.class, key);
    if (exist != null) {
      System.out.println("key:" + key + "已存在，自动转为更新");
      DB.update(entity);
    } else {
      System.out.println("开始保存：key=" + key + " value=" + value);
      DB.save(entity);
    }
  }
}
