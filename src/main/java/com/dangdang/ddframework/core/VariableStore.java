package com.dangdang.ddframework.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cailianjie on 2015-7-14.
 * 变量存储类，再测试过程中需要上下级传递变量一种方式
 */
public class VariableStore {
    //一个用例级别变量存储地
    static Map<String,Object> storeMap = new HashMap<String,Object>();
    //全局变量
    static Map<String,Object> storeMapCase = new HashMap<String,Object>();

    static Map<VariableType,Map<String,Object>> variableMap = new HashMap<VariableType,Map<String,Object>>();

    public static void add(String key, Object variable){
        storeMap.put(key,variable);
    }

    public static void addGlobalVar(String key, Object variable){
        storeMapCase.put(key,variable);
    }

    public static Object get(String key){
        return storeMap.get(key);
    }

    public static Object getGlobalVar(String key){
        return storeMapCase.get(key);
    }

    public static void clear(){
        storeMap.clear();
    }

    public static void clearGlobalVar(){
        storeMapCase.clear();
    }

    public static void add(VariableType variableType,String key,Object variable){
        if(!variableMap.containsKey(variableType)){
            Map<String,Object> map = new HashMap<>();

            variableMap.put(variableType,map);
        }

        variableMap.get(variableType).put(key,variable);

    }

    public static Object get(VariableType variableType,String key){
        Map<String,Object> map= variableMap.get(variableType);
        if(map!=null){
            return map.get(key);
        }

        return null;
    }

    public static void clear(VariableType variableType){
        Map<String,Object> map= variableMap.get(variableType);
        if(map!=null){
            map.clear();
        }
    }

    public static void clearAll(){
        variableMap.clear();
    }
}
