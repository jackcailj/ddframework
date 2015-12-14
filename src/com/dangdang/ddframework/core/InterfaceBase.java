package com.dangdang.ddframework.core;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dangdang.ddframework.drivers.HttpDriver;

public abstract class InterfaceBase extends FunctionalBase {
	
	protected String URL;
	protected boolean bHttps=false;
	protected boolean bPost=false;
	protected String enviroment = "";
	
	public String getEnviroment() {
		return enviroment;
	}

	public void setEnviroment(String enviroment) {
		this.enviroment = enviroment;
	}

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
		if(bPost==false) {
			result = HttpDriver.doGet(URL, paramMap, bHttps);
		}
		else{
			result = HttpDriver.doPost(URL, bHttps, paramMap);
		}
	}
	
	public void beforeRequest(){
		
	}
	
	
}
