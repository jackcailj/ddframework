package com.dangdang.ddframework.util;


import org.apache.commons.lang3.StringUtils;

/**
 * Created by cailianjie on 2015-5-26.
 * 字符相关操作集
 */
public class StringUtil {

    /*
    判断Object是否为空或者null
    Object需支持toString方法
     */
    public  static  boolean isBlank(Object o){
        if(o==null) {
            return true;
        }

        if(StringUtils.isBlank(o.toString())){
            return true;
        }

        return false;
    }

    /*
   判断Object是否不为空或者null
   Object需支持toString方法
    */
    public  static  boolean isNotBlank(Object o){
        if(o==null) {
            return false;
        }

        if(StringUtils.isNotBlank(o.toString())){
            return false;
        }

        return true;
    }
}
