package com.dangdang.ddframework.dataverify.verify_annotation;

import com.dangdang.ddframework.dataverify.DataVerifyManager;
import com.dangdang.ddframework.dataverify.ValueVerify;
import com.dangdang.ddframework.dataverify.VerifyResult;
import com.dangdang.ddframework.reponse.ReponseV1;
import com.dangdang.ddframework.reponse.ReponseV2;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by cailianjie on 2015-8-29.
 * 反射类中字段是否有verify_annotation包里的注解，如果有进行相关处理
 */
public class AnnotationVerifyProcessor {

    public static void handleVerifyAnnotation(DataVerifyManager dataVerifyManager,Object o) throws IllegalAccessException {
        if(o ==null) {
            return;
        }

        Field[] fields=o.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            Object fieldValue = field.get(o);
            if(fieldValue instanceof ReponseV2 || fieldValue instanceof ReponseV1) {
                _handleVerifyAnnotation(dataVerifyManager,fieldValue);
            }
        }
    }

    protected static void _handleVerifyAnnotation(DataVerifyManager dataVerifyManager,Object o) throws IllegalAccessException {
        if(o ==null) {
            return;
        }

        Field[] fields=o.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            Object fieldValue = field.get(o);
            //String type=field.getType().getName();
            //根据注解产生验证数据
            Null nullAnnotation = field.getAnnotation(Null.class);
            if (nullAnnotation != null) {
                dataVerifyManager.add(new ValueVerify<Object>(null, fieldValue), VerifyResult.SUCCESS);
            }
            NotNull notNullAnnotation = field.getAnnotation(NotNull.class);
            if (notNullAnnotation != null) {
                dataVerifyManager.add(new ValueVerify<Object>(null, fieldValue), VerifyResult.FAILED);
            }

            //处理list或者自定义类型数据
            if (isList(field.get(o))) {
                List list = (List) field.get(o);
                for (Object element : list) {
                    _handleVerifyAnnotation(dataVerifyManager, element);
                }
            } else if (!isJavaType(field)) {//自定义类型递归处理自定义类型所有属性
                _handleVerifyAnnotation(dataVerifyManager, fieldValue);
            }

        }
    }

    /*
    检测是否为java类型
     */
    public static boolean isJavaType(Field field){

        if (field.getGenericType().toString().equals("class java.lang.String")
                ||field.getGenericType().toString().equals("class java.lang.Integer")
                ||field.getGenericType().toString().equals("class java.lang.Double")
                ||field.getGenericType().toString().equals("class java.lang.Boolean")
                ||field.getGenericType().toString().equals("class java.util.Date")
                ||field.getGenericType().toString().equals("class java.lang.Short")
                ||field.getGenericType().toString().equals("class java.lang.Float")
                ||field.getGenericType().toString().equals("class java.lang.BigInteger")
                ||field.getGenericType().toString().equals("class java.lang.BigDecmail")
                ||field.getGenericType().toString().equals("class java.lang.Character")
                ||field.getGenericType().toString().equals("class java.lang.Long")
                ||field.getGenericType().toString().equals("class java.lang.Byte")
                ||field.getGenericType().toString().equals("boolean")
                ||field.getGenericType().toString().equals("byte")
                ||field.getGenericType().toString().equals("short")
                ||field.getGenericType().toString().equals("int")
                ||field.getGenericType().toString().equals("long")
                ||field.getGenericType().toString().equals("float")
                ||field.getGenericType().toString().equals("double")
                ||field.getGenericType().toString().equals("char")
                ) {
            return true;
        }
        return false;
    }

    /*
    判断是否为List类型
     */
    public static boolean isList(Object field){
        if(field instanceof List){
            return true;
        }
        return false;
    }
}
