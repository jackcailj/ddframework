package com.dangdang.ddframework.util;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cailianjie on 2016-4-29.
 */
public class PropertyUtils {

    public static Logger logger = Logger.getLogger(PropertyUtils.class);

    public static void copyProperty(Object source,Object target) throws Exception {

        if(source==null || target==null){
            throw  new Exception("source、target不能为null");
        }



        Method[] sourceGetMethods=source.getClass().getMethods();

        Map<String,Method> getMethods = new HashMap<>();
        for(Method getMethod:sourceGetMethods){
            if(getMethod.getName().substring(0,3).equals("get")) {
                String propertyName = getMethod.getName().substring(3, 4).toLowerCase() + getMethod.getName().substring(4);
                getMethods.put(propertyName, getMethod);
            }
        }

        Method[] targetSetMethods=target.getClass().getMethods();
        Map<String,Method> setMethods = new HashMap<>();
        for(Method setMethod:targetSetMethods){

            if(setMethod.getName().substring(0,3).equals("set")) {
                String propertyName = setMethod.getName().substring(3, 4).toLowerCase() + setMethod.getName().substring(4);
                setMethods.put(propertyName, setMethod);
            }
        }

        //赋值
        for(String property:setMethods.keySet()){

            if(getMethods.containsKey(property)){

                try {
                    Method getMethod = getMethods.get(property);
                    Object propertyValue = getMethod.invoke(source);

                    Method setMethod = setMethods.get(property);
                    setMethod.invoke(target, propertyValue);
                }catch (Exception e){
                    logger.error(e.getStackTrace());
                }
            }

        }
    }

}
