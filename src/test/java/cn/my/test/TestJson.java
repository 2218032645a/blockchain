package cn.my.test;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class TestJson {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("dnaod");
        list.add("djda");
        list.add("sdjadhid");
        System.out.println(JSONObject.toJSONString(list,true));
        System.out.println(JSONObject.toJSONString(list));
    }
}
