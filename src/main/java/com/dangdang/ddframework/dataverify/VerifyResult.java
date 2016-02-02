package com.dangdang.ddframework.dataverify;

/*
 * author:cailj
 * desc:验证结果枚举，对验证结果进行分类
 */
public enum VerifyResult {
	SUCCESS("success"),
	FAILED("failed"),
	Exception("exception");
	
	private String context;
	
	private VerifyResult(String value) {
		// TODO Auto-generated constructor stub
			  context=value;
		}
	
}
