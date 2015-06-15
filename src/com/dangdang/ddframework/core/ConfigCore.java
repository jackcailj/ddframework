package com.dangdang.ddframework.core;

public class ConfigCore {
	
	protected static TestEnvironment environment=TestEnvironment.TESTING;
	protected static TestDevice device=TestDevice.ANDROID;

	protected static String groups="";
	//定义测试脚本的版本，需要和testData目录名一致
	protected static String testData = "readerV4";
	
	
	public static String getGroups() {
		return groups;
	}
	public static void setGroups(String groups) {
		ConfigCore.groups = groups;
	}
	
	public static TestEnvironment getEnvironment() {
		return environment;
	}
	public static void setEnvironment(TestEnvironment environment) {
		ConfigCore.environment = environment;
	}
	public static TestDevice getDevice() {
		return device;
	}
	public static void setDevice(TestDevice device) {
		ConfigCore.device = device;
	}
	
	public static String getConfigFile()
	{
		return "conf/config_"+environment.toString()+".properties";
	}
	
	public static String getTestData() {
		return testData;
	}
	public static void setTestData(String data) {
		ConfigCore.testData = data;
	}
}
