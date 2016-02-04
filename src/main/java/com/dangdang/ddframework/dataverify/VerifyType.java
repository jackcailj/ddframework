package com.dangdang.ddframework.dataverify;

/**
 * Created by cailianjie on 2016-2-3.
 */
public enum VerifyType {
    EQUALS("相等"),
    CONTAINS("包含");

    String content="";

    VerifyType(String verityType){
        content=verityType;
    }
}
