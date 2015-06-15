package com.dangdang.ddframework.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.From;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.mapping.Collection;


public class TestGroups {
	public final static String ALL="testing,staging,online,android,ios";
	
	public final static String ALL_ENV="testing,staging,online";
	public final static String TESTING="testing";
	public final static String STAGING ="staging";
	public final static String ONLINE ="online";
	
	public final static String ALL_DEVICE="android,ios";
	public final static String ANDROID="android";
	public final static String IOS ="ios";
	
	public final static String DDREADER="ddreader";
	public final static String YC ="yc";
	
	public final static List<String> group_env = Arrays.asList("testing","staging","online");
	public final static List<String> group_device = Arrays.asList("android","ios");
	
	
	public static String getGroup(TestDevice testDevice,TestEnvironment testEnvironment){
		StringBuilder builder = new StringBuilder();
		if(testDevice==TestDevice.ANDROID){
			builder.append(testDevice.toString());
		}
		else {
			builder.append("ios");
		}
		builder.append("_");
		builder.append(testEnvironment.toString());
		return builder.toString();
	}
	
	/*
	 * 检查是否包含要运行分组，必须同时包含
	 */
	public static boolean CheckGroups(String group) {
		//为空表示所有场景均运行
		if(StringUtils.isBlank(group)){
			return true;
		}
		
		group=group.replace(" ", "");
		
		String runGroups=ConfigCore.getGroups();
		if(StringUtils.isBlank(runGroups)){
			runGroups=getGroup(ConfigCore.getDevice(),ConfigCore.getEnvironment());
		}
		
		//简化配置，如果没有写测试设备或者测试环境，表示全部支持
		List<String> groups=new ArrayList<String>();
		Collections.addAll(groups, StringUtils.split(group,","));
		
		//如果没有写设备相关信息，表示支持所有设备		
		java.util.Collection temp =CollectionUtils.intersection(groups, group_device);
		if(temp.size()==0){
			groups.addAll(group_device);
		}
		
		//如果没有写环境相关信息，表示支持所有环境
		temp=CollectionUtils.intersection(groups, group_env);
		if(temp.size()==0){
			groups.addAll(group_env);
		}
		
		List<String> runGroupList=new ArrayList<String>();
		Collections.addAll(runGroupList,StringUtils.split(runGroups,"_"));
		return groups.containsAll(runGroupList)? true:false;
	}
}
