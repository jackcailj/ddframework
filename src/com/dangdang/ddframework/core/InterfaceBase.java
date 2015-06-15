package com.dangdang.ddframework.core;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dangdang.ddframework.drivers.HttpDriver;

public abstract class InterfaceBase extends FunctionalBase {
	
	protected String URL;
	protected boolean bHttps=false;
	
	public InterfaceBase() {
		// TODO Auto-generated constructor stub
	}
	
	public InterfaceBase(String param){
		super(param);
	}
	
	public InterfaceBase(Map<String,String> param) {
		super(param);
	}
	
	
	
	/*
	 * 发送请求，默认为post
	 */
	@Override
	public void doWork() throws Exception {
		// TODO Auto-generated method stub
		super.doWork();
		beforeRequest();
		result = HttpDriver.doGet(URL, paramMap,bHttps);
	}
	
	public void beforeRequest(){
		
	}
	
	
}
