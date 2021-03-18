package com.ricardo.backstage.Entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.codehaus.jettison.json.JSONString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Array;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "hadoop")
//@TypeDef(name = "json", typeClass = JsonStringType.class)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class hadoop{

    @Id
    private String res_key;

    @Column
    private String res_value;

    public String getRes_key() {
        return res_key;
    }

    public void setRes_key(String res_key) {
        this.res_key = res_key;
    }

    public String getRes_value() { return res_value; }


}
