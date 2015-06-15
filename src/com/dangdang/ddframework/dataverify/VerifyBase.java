package com.dangdang.ddframework.dataverify;

import org.testng.annotations.IFactoryAnnotation;

public abstract class VerifyBase implements IDataVerify {

	
	protected VerifyResult verifyResult = VerifyResult.SUCCESS;
	protected String errorInfo="";
	protected VerifyResult expectResult=VerifyResult.SUCCESS;
	protected String verifyContent="";
	
	
	public void setExpectResult(VerifyResult expectResult) {
		this.expectResult = expectResult;
	}

	@Override
	public boolean getVerifyResult() {
		// TODO Auto-generated method stub
		return   verifyResult==expectResult;
	}
	
	@Override
	public String getErrorInfo() {
		// TODO Auto-generated method stub
		return errorInfo;
	}
	
	@Override
	public String getVerifyContent() {
		// TODO Auto-generated method stub
		return verifyContent;
	}
	
	public IDataVerify setVerifyContent(String verifyContent) {
		this.verifyContent = verifyContent;
		return this;
	}

}
