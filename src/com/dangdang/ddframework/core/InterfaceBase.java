package com.dangdang.ddframework.core;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dangdang.ddframework.drivers.HttpDriver;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class InterfaceBase extends FunctionalBase {
	
	protected String URL;
	protected boolean bHttps=false;
	protected boolean bPost=false;
	protected String enviroment;
	
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

		handleResult();
	}
	
	public void beforeRequest(){
		
	}

	/*
	处理返回结果中的特殊字符
	 */
	public void handleResult(){
		if(result!=null){
			String tempString=result.toString();
			tempString=StringUtils.replace(tempString,"\\u00A0"," ");//将 \u00A0替换为空格
			result=tempString;
		}
	}
}
