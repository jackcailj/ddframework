package com.dangdang.ddframework.core;

import java.util.*;
import java.util.Map.Entry;
import java.lang.reflect.Method;

import org.slf4j.Logger;  import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.*;

import com.dangdang.ddframework.drivers.HttpDriver;
import com.dangdang.ddframework.util.*;


public abstract class TestCaseBase {
	
	public String testDataFile = "";
	Util utils = new Util();
	Logger log = LoggerFactory.getLogger(TestCaseBase.class);
	
//	@DataProvider(name="dataProvider1")
//	public Iterator<Object[]> DataProvider(Method method){
//		if (testDataFile.isEmpty()){
//			//testDataFile = method.getName();
//			ExcelData data = new ExcelData("./testData/" + method.getName(), method.getName());
//			return data;
//		}
//		else {
//			ExcelData data = new ExcelData("./testData/" + testDataFile, method.getName());	
//			return data;
//		}
//	}
//	
	@DataProvider(name="dataProvider")
	public Iterator<Object[]> DataProvider(ITestContext context,Method method){
		String[] includeGroups=context.getIncludedGroups();
		String[] excludeGroups=context.getExcludedGroups();
	
		String filePath="";
		if (testDataFile.isEmpty()){
			filePath="testData/"+ConfigCore.getTestData() +"/"+ method.getName();
		}
		else {
			filePath="testData/"+ConfigCore.getTestData() +"/"+ testDataFile;
		}
		
		ExcelData data = new ExcelData(filePath, method.getName(),includeGroups,excludeGroups);	
		return data;
	}
	
	public void setDataFile(String path){
		testDataFile = path;
	}
}
