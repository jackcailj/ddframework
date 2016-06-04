package com.dangdang.ddframework.fitnesse;

/**
 * Created by cailianjie on 2015-10-13.
 */
public class ParamParse {
    //参数分隔符
    final static String paramSeparator=",|;";
    public static String AND="and";

    public static String[] parseParam(String param){
        return parseParam(param,paramSeparator);
    }

    public static String[] parseParam(String param, String separator){
        return param.split(separator);
    }

}
