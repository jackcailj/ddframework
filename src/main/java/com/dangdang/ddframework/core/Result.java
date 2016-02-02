package com.dangdang.ddframework.core;

public class Result {
	boolean _verifyResult;
	Object _result;
	
	public Result(boolean verifyResult,Object result){
		_result=result;
		_verifyResult=verifyResult;
	}
	
	public boolean getVerifyResult() {
		return _verifyResult;
	}
	
	public Object getResult() {
		return _result;
	}
}
