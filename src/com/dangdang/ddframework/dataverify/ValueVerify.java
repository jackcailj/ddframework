package com.dangdang.ddframework.dataverify;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframework.util.Compare;

/*
 * 对象对比
 * 两种方式：
 * 1、对象实现了equals方法，使用equals方法对比
 * 2、对象没有实现equals方法，将会转换成Map进行对比(构造函数中的mapCompare设置为true)
 */
public class ValueVerify<T> extends VerifyBase{
	T __value1;
	T __value2;
	//boolean bequals =true;
	boolean __isObject=false;
	
	protected static Logger logger = Logger.getLogger(ValueVerify.class);
	
	public ValueVerify(T value1,T value2) {
		__value1=value1;
		__value2=value2;
	}
	
/*	public ValueVerify(T value1,T value2,boolean bequals) {
		__value1=value1;
		__value2=value2;
		this.bequals=bequals;
	}*/
	
	public ValueVerify(T value1,T value2, Boolean mapCompare) {
		__value1=value1;
		__value2=value2;
		//this.bequals=bequals;
		__isObject = mapCompare;
		
		
	}

	@Override
	public VerifyResult dataVerify() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// TODO Auto-generated method stub
		try{
			if(__isObject){
				Map<String,Object> value1=(Map<String, Object>)JSONObject.toJSON(__value1);
				Map<String,Object> value2=(Map<String, Object>)JSONObject.toJSON(__value2);
				
				logger.info("值对比--开始");
				logger.info("值对比--value1:"+value1);
				logger.info("值对比--value2:"+value2);
				logger.info("值对比--期望:"+(expectResult== VerifyResult.SUCCESS?"相同":"不相同"));
				if(!Compare.equals(value1,value2)){
					verifyResult = VerifyResult.FAILED;
				}
				
			}
			else{
				logger.info("值对比--开始");
				logger.info("值对比--value1:"+__value1);
				logger.info("值对比--value2:"+__value2);
				logger.info("值对比--期望:"+(expectResult== VerifyResult.SUCCESS?"相同":"不相同"));
				
				if(!__value1.equals(__value2) ){
					verifyResult = VerifyResult.FAILED;
				}
				
			}
		}catch(Exception e){
			errorInfo = "ListVerify异常："+e;
			verifyResult=VerifyResult.Exception;
		}
		logger.info("值对比--结果:"+(getVerifyResult()?"与期望一致":"与期望不一致"));
		
		return verifyResult;
	}
}
