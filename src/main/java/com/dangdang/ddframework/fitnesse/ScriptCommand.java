package com.dangdang.ddframework.fitnesse;

import com.dangdang.ddframework.core.VariableStore;
import com.dangdang.ddframework.core.VariableType;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cailianjie on 2016-5-19.
 */
public class ScriptCommand extends CommandBase{

    String methodName;
    String[] methodParams;
    String varName;
    VariableType variableType;


    @Override
    public void parseCommandIml() throws Exception {

        //获取变量名称
        Matcher matcher = Pattern.compile(VariableType.regexExpressionString()).matcher(commandStr);
        if(matcher.find()){
            variableType = VariableType.getVariableType(matcher.group(1).trim());
            varName = matcher.group(2).trim();
        }

        matcher = Pattern.compile("#(.*?)#(.*?)$").matcher(commandStr);
        if(matcher.find()){
            methodName = matcher.group(1).toString().trim();
            if(StringUtils.isNotBlank(matcher.group(2).toString().trim())) {
                methodParams = ParamParse.parseParam(matcher.group(2).toString().trim());
            }
        }
        else {
            throw new Exception("Script命令格式错误:"+commandStr);
        }
    }

    @Override
    public void execute(Object... params) throws Exception {
        Object executeObject  =params[0];

        //通过反射执行方法
        Class[] classes = null;
        if(methodParams!=null && methodParams.length>0) {
            classes = new Class[methodParams.length];

            for (int i=0;i<classes.length;i++)
            {
                classes[i]=String.class;
            }
        }
        Method method = executeObject.getClass().getMethod(methodName,classes);
        Object returnObject = method.invoke(executeObject,methodParams);

        //将返回值保存到变量存储系统
        if(returnObject!=null){
            VariableStore.add(variableType,varName,returnObject);
        }


    }


}
