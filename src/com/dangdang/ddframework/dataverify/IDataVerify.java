package com.dangdang.ddframework.dataverify;

import java.lang.reflect.InvocationTargetException;

public interface IDataVerify {
	//获取验证内容
	String getVerifyContent();
	//设置验证内容
	IDataVerify setVerifyContent(String verifyContent);
	
	/*
	 * 数据验证函数
	 */
	public VerifyResult dataVerify() throws Exception;
	
	/*
	 * 数据验证失败时，获取验证失败的详细信息
	 */
	public String getErrorInfo();
	
	//获取验证结果
	public boolean getVerifyResult();
	
	//设置期望的验证结果
	public void setExpectResult(VerifyResult expectResult);
}
