package com.dangdang.ddframework.dataverify;

/**
 * Created by cailianjie on 2015-10-28.
 *
 * 验证布尔类型表达式
 */
public class ExpressionVerify extends VerifyBase{

    Boolean __expression;

    public ExpressionVerify(Boolean expression){
        __expression =expression;
    }


    @Override
    public boolean dataVerify() throws Exception {
        if(__expression){
            verifyResult=VerifyResult.SUCCESS;
        }
        else{
            verifyResult=VerifyResult.FAILED;
        }

        return getVerifyResult();
    }
}
