package com.dangdang.ddframework.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.text.StyledEditorKit.BoldAction;

import com.dangdang.ddframework.dataverify.verify_annotation.AnnotationVerifyProcessor;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dangdang.ddframework.dataverify.DataVerifyManager;
import com.dangdang.ddframework.util.SessionUtil;
import com.dangdang.ddframework.util.Util;

/**
 * 
 * @author cailianjie
 *
 *用来描述功能实现需要的步骤:
 *1、解析参数
 *2、管理期望验证的数据
 *3、进行相关操作
 *4、对数据进行校验
 */
public abstract class FunctionalBase {
	
	public static Logger logger = Logger.getLogger(FunctionalBase.class);
	
	protected Map<String, String> originalParamMap = new TreeMap<String, String>();
	protected Map<String, String> paramMap = new TreeMap<String, String>();
	protected DataVerifyManager dataVerifyManager = new DataVerifyManager();
	
	
	
	private final String CASE_NAME = "用例名称";
	public final String EXPECTED = "期望";
	
	protected Object result;
	protected boolean dataVerifyResult=true;
	protected boolean expectedOperateResult = true;
	
	protected String caseName;
	protected boolean EXCEPTSUCCESS=true;
 	
	public boolean isEXCEPTSUCCESS() {
		return EXCEPTSUCCESS;
	}

	public void setEXCEPTSUCCESS(boolean eXCEPTSUCCESS) {
		EXCEPTSUCCESS = eXCEPTSUCCESS;
		dataVerifyManager.setCaseExpectResult(EXCEPTSUCCESS);
	}

	public Object getResult() {
		return result;
	}
	
	public <T> T GetParam(Class classz){
		return (T) JSONObject.parseObject(JSONObject.toJSONString(paramMap),classz);
	}

	/*
	 * 可以在操作前产生期望数据并使用DataVerifyManager管理验证数据时重写此方法
	 */
	public boolean getDataVerifyResult() {
		return dataVerifyResult;
	}

	public FunctionalBase() {
		// TODO Auto-generated constructor stub
	}
	
	public  FunctionalBase(String param) {
		// TODO Auto-generated method stub
		paramMap = Util.generateMap(param);
		originalParamMap.putAll(paramMap);
		handleParam();
	}

	/*
	 * 构造函数，传入测试数据
	 */
	public  FunctionalBase(Map<String,String> param) {
		
		if(param!=null){
			paramMap = param;
			originalParamMap.putAll(paramMap);
			handleParam();
		}
		
	}
	
	//
	protected void handleParam(){
		if(paramMap!=null){
			String expected = paramMap.get(EXPECTED);
			String name = paramMap.get(CASE_NAME);
			if(expected!=null && expected.toString().trim().toLowerCase().equals("0")){
				setEXCEPTSUCCESS(true);
			}
			else {
				setEXCEPTSUCCESS(false);
			}
			
			if(name!=null){
				caseName=paramMap.get(CASE_NAME).toString();
			}	
			
			paramMap.remove(CASE_NAME);
			paramMap.remove(EXPECTED);
		}
	}

	protected void beforeParseParam() throws Exception {

	}

	protected  void afterParseParam(){}


	/*
	 * 解析参数
	 */
	protected void parseParam() throws Exception {
		
	}
	
	/*
	 * 生成需要验证的数据
	 */
	protected void genrateVerifyData() throws Exception {
		
	}
	

	
	/*
	 * 进行具体功能操作
	 */
	public void doWork() throws Exception {
        beforeParseParam();
		parseParam();
        afterParseParam();
		try{
			genrateVerifyData();
		}
		catch(Exception e){
			logger.error(e);
		}
	}
	
	/*
	 * 对数据进行验证，实现了dataVerifyManager管理的验证
	 * 
	 * 对于使用非dataVerifyManager管理的验证需要重写此方法
	 * 
	 */
	protected void dataVerify() throws Exception {

		//自动寻找reponse类，解析相应的注解，加入字段规则检查
		AnnotationVerifyProcessor.handleVerifyAnnotation(dataVerifyManager,this);
		dataVerifyResult = dataVerifyManager.dataVerify();
		
		/*//期望操作的结果与数据验证结果应一致
		//期望操作成功，数据验证应成功
		//期望操作失败，数据验证应失败
		if(expectedOperateResult!=verifyResult){
			dataVerifyResult = false;
			logger.error(dataVerifyManager.getErrorInfos());
		}
		else if(expectedOperateResult==false) {
			List<String> errorsList = dataVerifyManager.getNotExpectedVerifyResults(false);
			if(errorsList.size() ==0 ){
				//应保证每个验证都失败
				dataVerifyResult = true;
			}
			else {
				dataVerifyResult = false;
				logger.error(errorsList);
			}
		}
		else {
			dataVerifyResult = true;
		}*/
		
	}
	
	/*
	 * 进行功能操作并进行数据验证
	 */
	public void doWorkAndVerify() throws Exception {
		doWork();
		dataVerify();
	}
}
