package mapreduce.entity;

import lombok.Data;

@Data
/**
 * HeartDisease 心脏病诊断数据集
 */
public class HeartDisease {
  private Double age;// 年龄
  private Double sex;// 性别
  private Double cp;// 胸痛类型
  private Double trestbps;// 静息血压
  private Double chol;// 胆固醇含量(mg/dl) 110-220为正常
  private Double fbs;// 空腹血糖>120mg/dl
  private Double restecg;// 静息心电图结果
  private Double thalach;// 最高心率
  private Double exang;// 运动型心绞痛
  private Double oldpeak;// 运动引起的ST下降
  private Double slope;// 最大运动量时心电图ST的斜率
  private Double ca;// 使用荧光染色法测定的主血管数
  private Double thal;// THAL
  private Double target;// 患病情况

  public HeartDisease(String src) {
    String[] srcList = src.split(",");
    age = Double.valueOf(srcList[0]);
    sex = Double.valueOf(srcList[1]);
    cp = Double.valueOf(srcList[2]);
    trestbps = Double.valueOf(srcList[3]);
    chol = Double.valueOf(srcList[4]);
    fbs = Double.valueOf(srcList[5]);
    restecg = Double.valueOf(srcList[6]);
    thalach = Double.valueOf(srcList[7]);
    exang = Double.valueOf(srcList[8]);
    oldpeak = Double.valueOf(srcList[9]);
    slope = Double.valueOf(srcList[10]);
    ca = Double.valueOf(srcList[11]);
    thal = Double.valueOf(srcList[12]);
    target = Double.valueOf(srcList[13]);
  }
}
