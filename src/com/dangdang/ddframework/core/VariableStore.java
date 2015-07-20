package com.dangdang.ddframework.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cailianjie on 2015-7-14.
 * 变量存储类，再测试过程中需要上下级传递变量一种方式，最好在每个用例执行完清除
 */
public class VariableStore {
    static Map<String,Object> storeMap = new HashMap<String,Object>();

    public static void add(String key, Object variable){
        storeMap.put(key,variable);
    }

    public static Object get(String key){
        return storeMap.get(key);
    }

    public static void clear(){
        storeMap.clear();
    }
}
