package com.dangdang.ddframework.dataverify;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.text.StyledEditorKit.BoldAction;

import com.dangdang.ddframework.util.Util;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.log4j.Logger;

public class DataVerifyManager {
	
	protected static Logger logger = Logger.getLogger(DataVerifyManager.class);
	
	private List<IDataVerify> verifys =new ArrayList<IDataVerify>();
	protected List<String> errorInfos = new ArrayList<String>();
	
	protected VerifyResult caseExpectResult=VerifyResult.SUCCESS;
	
	public void setCaseExpectResult(boolean caseExpectResult) {
		if(caseExpectResult){
			this.caseExpectResult = VerifyResult.SUCCESS;
		}
		else {
			this.caseExpectResult = VerifyResult.FAILED;
		}
	}

	public List<String> getErrorInfos() {
		return errorInfos;
	}

	/*
	 * 添加数据验证类
	 */
	public void add(IDataVerify dataVerify) {
		if(dataVerify == null){
			return;
		}
		//验证期望成功与否默认与Case期望成功与否一致
		dataVerify.setExpectResult(caseExpectResult);
		verifys.add(dataVerify);
	}
	
	public void add(IDataVerify dataVerify,VerifyResult excepctResult) {
		if(dataVerify == null){
			return;
		}
		dataVerify.setExpectResult(excepctResult);
		verifys.add(dataVerify);
	}

	public  void clear(){
		verifys.clear();
		errorInfos.clear();
		caseExpectResult=VerifyResult.SUCCESS;
	}
	
	/*
	 * 进行数据验证，并返回结果
	 */
	public boolean dataVerify() throws Exception {
		

		List<Boolean> results = verifys.parallelStream().map(verify->{
		//for(IDataVerify verify : verifys){
			boolean result = true;
			logger.info("开始执行验证："+verify.getClass());
			logger.info("验证内容："+verify.getVerifyContent());
			try {
				verify.dataVerify();
				if(verify.getVerifyResult()==false){
					logger.info(verify.getErrorInfo());
					errorInfos.add(verify.getErrorInfo());
					result =false;
				}
				logger.info("结束执行验证："+verify.getClass()+"结果："+verify.getVerifyResult());
			} catch (Exception e) {
				logger.error(Util.getStrackTrace(e));
			}

			return result;

		}).collect(Collectors.toList());

		Boolean bResult = results.parallelStream().allMatch(o-> {return o;});

		return bResult;
	}
	
	
	/*
	 * 
	 
	public List<String> getNotExpectedVerifyResults(boolean expectedVerifyResult) {
		
		List<String> errors = new ArrayList<String>();
		for(IDataVerify verify : verifys){
			if(verify.getVerifyResult()!= expectedVerifyResult){
				errors.add(verify.getErrorInfo());
			}
		}
		return errors;
	}*/
}
