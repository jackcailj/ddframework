package com.dangdang.ddframework.core;

/**
 * Created by cailianjie on 2016-5-18.
 */
public enum VariableType {
    CASE("$"),
    SUITE("&");


    String content="";

    VariableType(String id){
        content=id;
    }

    public static String regexExpressionString(){
        return "^([\\$&])(.*?)=";
    }

    public static String regexVarString(){
        return "^([\\$&])(.*?)$";
    }

    public static VariableType getVariableType(String sybol) throws Exception {
        if(sybol.equals("$")){
            return VariableType.CASE;
        }
        else if(sybol.equals("&")){
            return VariableType.SUITE;
        }

        throw new Exception("变量符号错误:"+sybol);
    }

}
