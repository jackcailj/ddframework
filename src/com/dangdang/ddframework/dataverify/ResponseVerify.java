package com.dangdang.ddframework.dataverify;

import java.lang.reflect.*;

public class ResponseVerify<T> extends VerifyBase{
	T _jsonValue;
	T _dbValue;
	String _classPath;
	boolean _listSort=false;
	
	public ResponseVerify(String classPath){
		_classPath = classPath;

	}
	public ResponseVerify(String classPath, T jsonValue, T dbValue){
		_classPath = classPath;
		_jsonValue = jsonValue;
		_dbValue = dbValue;
	}
	
	public ResponseVerify(String classPath, T jsonValue, T dbValue, boolean equal){
		_classPath = classPath;
		_jsonValue = jsonValue;
		_dbValue = dbValue;
		_listSort = equal;
	}
	
	@Override
	public boolean dataVerify() throws Exception {
		Class<?> c = null;
		c = Class.forName(_classPath);
		Field[] fields = c.getDeclaredFields();
		//for(int i=0; i<fields.length; i++);
		System.out.println(fields.toString());
		
		
		return false;
	}
	
	public static void main(String[] args) throws Exception{
		ResponseVerify r = new ResponseVerify("com.dangdang.readerV5.reponse.ChannelResponse");
		r.dataVerify();
	}

}
