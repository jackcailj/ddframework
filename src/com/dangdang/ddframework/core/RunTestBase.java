package com.dangdang.ddframework.core;

import org.apache.ibatis.migration.CommandLine;

public class RunTestBase {

	/*
	 * 解析命令行，读取配置，运行testng执行用例
	 */
	public void run(String[] args) throws Exception{
		ComandLineParam.parse(args);
		Config();
		ComandLineParam.runTestNG();
	}
	
	
	/*
	 * 子类需重写此函数，加载需要的配置文件
	 */
	public  void Config(){
		
	}
	
	
}	
