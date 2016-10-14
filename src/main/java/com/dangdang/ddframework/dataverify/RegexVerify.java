package com.dangdang.ddframework.dataverify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cailianjie on 2015-7-14.
 *
 * 适用于Regex匹配字符串进行验证
 */
public class RegexVerify extends VerifyBase{

    protected static Logger logger = LoggerFactory.getLogger(RegexVerify.class);

    String __pattern;
    String __text;


    public   RegexVerify(String pattern,String text){
        __pattern=pattern;
        __text=text;
    }
    @Override
    public boolean dataVerify() throws Exception {
        try {
            logger.info("Regex表达式："+__pattern);
            logger.info("匹配字符串："+__text);
            logger.info("Regex匹配--期望:"+(expectResult== VerifyResult.SUCCESS?"相同":"不相同"));

            Matcher matcher = Pattern.compile(__pattern, Pattern.DOTALL).matcher(__text);
            if (matcher.find()) {
                verifyResult = VerifyResult.SUCCESS;
            } else {
                verifyResult = VerifyResult.FAILED;
            }
        }catch (Exception e){
            errorInfo = "RegexVerify异常："+e;
            verifyResult=VerifyResult.Exception;
        }

        logger.info("Regex匹配--结果:"+(getVerifyResult()?"与期望一致":"与期望不一致"));


        return getVerifyResult();
    }
}
