package com.dangdang.ddframework.core;

public enum TestDevice {
	IPHONE("iphone"),
	IPAD("ipad"),
	ANDROID("android");
	

	private String context;
	
	private TestDevice(String value) {
	// TODO Auto-generated constructor stub
		  context=value;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return context;
	}
}
