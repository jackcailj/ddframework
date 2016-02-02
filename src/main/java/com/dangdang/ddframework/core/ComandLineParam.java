package com.dangdang.ddframework.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.metamodel.source.annotations.entity.ConfiguredClassType;
import org.testng.CommandLineArgs;
import org.testng.remote.RemoteTestNG;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.beust.testng.TestNG;

public class ComandLineParam {
	protected static String[] testNgArgs =new String[0];
	public static void parse(String[] args) throws Exception {
		//设置测试环境及设备
		for(int i=0;i<args.length;i++){
			String[] splitStrings=args[i].split("=");
		    if(splitStrings[0].equals("env"))
			{
				ConfigCore.setEnvironment(TestEnvironment.valueOf(splitStrings[1].toUpperCase()));
			}
			else if(splitStrings[0].equals("device")){
				ConfigCore.setDevice(TestDevice.valueOf(splitStrings[1].toUpperCase()));
			}
			else if(splitStrings[0].equals("version")){
				ConfigCore.setTestData(splitStrings[1].toLowerCase());
			}
			else if(splitStrings[0].equals("testng")){
				String[] splitStrings2=splitStrings[1].split(" ");
				List<String> list =new ArrayList<String>();
				Collections.addAll(list, splitStrings2);
				//list.add("-listener org.uncommons.reportng.HTMLReporter");
				testNgArgs = (String[]) list.toArray(testNgArgs);
			}
			
		}
		
		//必须设置脚本版本
		if(StringUtils.isBlank(ConfigCore.getTestData())){
			throw new Exception("请设置脚本版本，参数：version");
		}
			
		//设置当前要运行的group
		ConfigCore.setGroups(TestGroups.getGroup(ConfigCore.getDevice(), ConfigCore.getEnvironment()));

	}
	
	public static void runTestNG() {
		//调用testng跑用例
		TestNG.main(testNgArgs);
	}
}
