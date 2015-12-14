package com.dangdang.ddframework.dataverify;

import org.apache.log4j.Logger;

/**
 * Created by cailianjie on 2015-10-28.
 *
 * 验证布尔类型表达式
 */
public class ExpressionVerify extends VerifyBase{
	protected static Logger logger = Logger.getLogger(ExpressionVerify.class);
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
        
        logger.info("值对比--结果:"+(getVerifyResult()?"与期望一致":"与期望不一致"));
        return getVerifyResult();
    }
}
