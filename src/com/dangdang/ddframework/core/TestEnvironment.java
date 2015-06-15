package com.dangdang.ddframework.core;

public enum TestEnvironment {
	TESTING("testing"),
	STAGING("staging"),
	ONLINE("online");
	
	
	
	
   private String context;
	
   private TestEnvironment(String value) {
		// TODO Auto-generated constructor stub
	   context=value;
	}
   
   @Override
	public String toString() {
		// TODO Auto-generated method stub
		return context;
	}
}
