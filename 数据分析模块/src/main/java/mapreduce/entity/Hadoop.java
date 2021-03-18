package mapreduce.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

// Hadoop 对应的是数据库表名
@Data
@Entity
public class Hadoop {
    @Id
    String resKey;
    String resValue;
}
