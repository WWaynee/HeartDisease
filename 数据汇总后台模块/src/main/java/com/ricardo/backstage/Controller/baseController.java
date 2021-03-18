package com.ricardo.backstage.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.ricardo.backstage.Entity.hadoop;
import com.ricardo.backstage.Entity.value;
import com.ricardo.backstage.Repository.hadoopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
public class baseController {

    @Autowired
    hadoopRepository hadoopRepository;

    @GetMapping("/get/Count")
    public value getValueOne()
    {
        hadoop hadoop = hadoopRepository.getOne("GetCount");
        value v = new value();
        JSONObject jsonObject = JSONObject.parseObject(hadoop.getRes_value());
        v.setData(jsonObject.getString("count"));
        v.setDescription("这是样本总数");
        return v;
    }

    @GetMapping("/get/{res_key}")
    public JSON getValueTwo(@PathVariable("res_key") String res_key)
    {
        hadoop hadoop = hadoopRepository.getOne(res_key);
        JSONArray array = JSONArray.parseArray(hadoop.getRes_value().toString());
        Map<String, Map<String,Object>> map = new LinkedHashMap<>();
//        Multimap<String,Map<String,Object>> map = ArrayListMultimap.create();
        //Map<String,Object> map1 = new LinkedHashMap<>();
        for (int i=0;i<array.size();i++)
        {
            JSONObject jsonObject2 = array.getJSONObject(i);
            value m = JSONObject.toJavaObject(jsonObject2,value.class);
//            Map<String,Object> map1 = new HashMap<>();
            Map<String,Object> map1 = new LinkedHashMap<>();
            map1.put("data",m.getData());
            map1.put("description",m.getDescription());
//            list.add(m.getData());
//            list.add(m.getDescription());
//            map.put("data-"+i,m.getData());
//            map.put("description-"+i,m.getDescription());
            String key = "";
            if(i==0)
            {
                key = "proportion";
            }
            else
            {
                key = "incidence";
            }
            map.put(key,map1);
        }
        //map.put(res_key,map1);
        return (JSON) JSON.toJSON(map);
    }

    @GetMapping("/get/thal")
    public JSON getValueThree()
    {
        hadoop hadoop = hadoopRepository.getOne("thal");
//        hadoop.setRes_key("这是thal的数据");
//        return JSON.toJSONString(hadoop);
        JSONObject jsonObject = JSONObject.parseObject(hadoop.getRes_value());
        String string3 = jsonObject.get("thal3").toString();
        String string6 = jsonObject.get("thal6").toString();
        String string7 = jsonObject.get("thal7").toString();
        JSONArray array3 = JSONArray.parseArray(string3);
        JSONArray array6 = JSONArray.parseArray(string6);
        JSONArray array7 = JSONArray.parseArray(string7);
        Map<String, Map<String,Map<String,Object>>> map = new LinkedHashMap<>();
        Map<String,Map<String,Object>> map1 = new LinkedHashMap<>();
        Map<String,Map<String,Object>> map3 = new LinkedHashMap<>();
        Map<String,Map<String,Object>> map4 = new LinkedHashMap<>();
//        Multimap<String,Map<String,Object>> map = ArrayListMultimap.create();
        for (int i=0;i<array3.size();i++)
        {
            JSONObject jsonObject2 = array3.getJSONObject(i);
            value m = JSONObject.toJavaObject(jsonObject2,value.class);
            Map<String,Object> map2 = new HashMap<>();
            map2.put("data",m.getData());
            map2.put("description",m.getDescription());
            if(i==0)
            {
                map1.put("proportion", map2);
            }
            else
            {
                map1.put("incidence",map2);
            }
        }
        map.put("thal3",map1);

        for (int i=0;i<array6.size();i++)
        {
            JSONObject jsonObject2 = array6.getJSONObject(i);
            value m = JSONObject.toJavaObject(jsonObject2,value.class);
            Map<String,Object> map2 = new HashMap<>();
            map2.put("data",m.getData());
            map2.put("description",m.getDescription());
            if(i==0)
            {
                map3.put("proportion", map2);
            }
            else
            {
                map3.put("incidence",map2);
            }
        }
        map.put("thal6",map3);

        for (int i=0;i<array7.size();i++)
        {
            JSONObject jsonObject2 = array7.getJSONObject(i);
            value m = JSONObject.toJavaObject(jsonObject2,value.class);
            Map<String,Object> map2 = new HashMap<>();
            map2.put("data",m.getData());
            map2.put("description",m.getDescription());
            if(i==0)
            {
                map4.put("proportion", map2);
            }
            else
            {
                map4.put("incidence",map2);
            }
        }
        map.put("thal7",map4);

        return (JSON) JSON.toJSON(map);
    }

    @GetMapping("/get/MainBloodVessels")
    public JSON getValueFour()
    {
        hadoop hadoop = hadoopRepository.getOne("MainBloodVessels");
//        hadoop.setRes_key("这是thal的数据");
//        return JSON.toJSONString(hadoop);
        JSONObject jsonObject = JSONObject.parseObject(hadoop.getRes_value());
        String string0 = jsonObject.get("vessel0").toString();
        String string1 = jsonObject.get("vessel1").toString();
        String string2 = jsonObject.get("vessel2").toString();
        String string3 = jsonObject.get("vessel3").toString();
        JSONArray array0 = JSONArray.parseArray(string0);
        JSONArray array1 = JSONArray.parseArray(string1);
        JSONArray array2 = JSONArray.parseArray(string2);
        JSONArray array3 = JSONArray.parseArray(string3);
        Map<String, Map<String,Map<String,Object>>> map = new LinkedHashMap<>();
        Map<String,Map<String,Object>> map1 = new LinkedHashMap<>();
        Map<String,Map<String,Object>> map3 = new LinkedHashMap<>();
        Map<String,Map<String,Object>> map4 = new LinkedHashMap<>();
        Map<String,Map<String,Object>> map5 = new LinkedHashMap<>();
//        Multimap<String,Map<String,Object>> map = ArrayListMultimap.create();
        for (int i=0;i<array0.size();i++)
        {
            JSONObject jsonObject2 = array0.getJSONObject(i);
            value m = JSONObject.toJavaObject(jsonObject2,value.class);
            Map<String,Object> map2 = new HashMap<>();
            map2.put("data",m.getData());
            map2.put("description",m.getDescription());
            if(i==0)
            {
                map1.put("proportion", map2);
            }
            else
            {
                map1.put("incidence",map2);
            }
        }
        map.put("vessel0",map1);

        for (int i=0;i<array1.size();i++)
        {
            JSONObject jsonObject2 = array1.getJSONObject(i);
            value m = JSONObject.toJavaObject(jsonObject2,value.class);
            Map<String,Object> map2 = new HashMap<>();
            map2.put("data",m.getData());
            map2.put("description",m.getDescription());
            if(i==0)
            {
                map3.put("proportion", map2);
            }
            else
            {
                map3.put("incidence",map2);
            }
        }
        map.put("vessel1",map3);

        for (int i=0;i<array2.size();i++)
        {
            JSONObject jsonObject2 = array2.getJSONObject(i);
            value m = JSONObject.toJavaObject(jsonObject2,value.class);
            Map<String,Object> map2 = new HashMap<>();
            map2.put("data",m.getData());
            map2.put("description",m.getDescription());
            if(i==0)
            {
                map4.put("proportion", map2);
            }
            else
            {
                map4.put("incidence",map2);
            }
        }
        map.put("vessel2",map4);

        for (int i=0;i<array3.size();i++)
        {
            JSONObject jsonObject2 = array3.getJSONObject(i);
            value m = JSONObject.toJavaObject(jsonObject2,value.class);
            Map<String,Object> map2 = new HashMap<>();
            map2.put("data",m.getData());
            map2.put("description",m.getDescription());
            if(i==0)
            {
                map5.put("proportion", map2);
            }
            else
            {
                map5.put("incidence",map2);
            }
        }
        map.put("vessel3",map5);

        return (JSON) JSON.toJSON(map);
    }

}
