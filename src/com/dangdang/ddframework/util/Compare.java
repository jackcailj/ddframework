package com.dangdang.ddframework.util;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.*;
import com.google.gson.Gson;

public class Compare {
	public static Logger logger = Logger.getLogger(Compare.class);
	/*
	 * 验证map1包含map2
	 * 以map2为基准，如果map2的字段为null，则不进行不对
	 * */
	public static boolean Contains(Map<String, Object> map1,Map<String, Object> map2 ) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		
		for(Entry<String, Object> entry: map2.entrySet()){
			String keyString=entry.getKey();
			Object valueString=entry.getValue();
			
			if(!map1.containsKey(keyString)){
				logger.info("不包含key:"+keyString);
				return false;
			}
			
			if(valueString == null ){
				logger.info(keyString+"值为null，不进行比对");
				continue;
			}

			if(map1.get(keyString)==null){
				logger.info(keyString+"值为null,不相同");
				return false;
			}
			
			//如果是列表，比对类表是否包含
			if(valueString instanceof List){
				List tempList1=(List)map1.get(keyString);
				List tempList2=(List)valueString;
				if(!Contains(tempList1,tempList2)){
					return false;
				}
			}
			else if(valueString instanceof Map)
			{
				Map<String, Object> tempMap1=(Map<String, Object>)map1.get(keyString);
				Map<String, Object> tempMap2=(Map<String, Object>)valueString;
				if(!Contains(tempMap1,tempMap2)){
					return false;
				}
			}
			else {
				if(valueString instanceof Timestamp || valueString instanceof Date){
					long time1=(Long) valueString.getClass().getMethod("getTime",null).invoke(valueString);
					long time2 =(Long) map1.get(keyString).getClass().getMethod("getTime",null).invoke(valueString);
					
					if(time1!=time2){
						logger.info("不一致的地方：值【key:"+keyString+" value:"+valueString+"】和【key:"+keyString+" value:"+map1.get(keyString)+"】");
						return false;
					}
				}
				else if(valueString instanceof BigDecimal){
                    //BigDecimal比较大小时使用equals不行，需要用compareTo
					if(((BigDecimal) valueString).compareTo(new BigDecimal(map1.get(keyString).toString()))!=0){
						logger.info("不一致的地方：值【key:"+keyString+" value:"+valueString+"】和【key:"+keyString+" value:"+map1.get(keyString)+"】");
						return false;
					}
				}
				else if(!valueString.toString().equals(map1.get(keyString).toString())){
					
					logger.info("不一致的地方：值【key:"+keyString+" value:"+valueString+"】和【key:"+keyString+" value:"+map1.get(keyString)+"】");
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 验证list1是否包含list2
	 * @param list1
	 * @param list2
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public static boolean Contains(List list1, List list2) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		List machingList = new ArrayList();
		for(Object object : list2){
			boolean bContains = false;
			for(Object o1:list1){
				
				if(machingList.contains(o1)){
					continue;
				}
				
				if(object instanceof Map &&  o1 instanceof Map ){
					
						if (!Contains((Map<String, Object>)o1,(Map<String, Object>)object)) {
							continue;
						}
						else {
							
							machingList.add(o1);
							bContains =true; 
							break;
						}
				}
				else{
						if(!object.toString().equals(o1.toString())){
							continue;
						}
						else {
							machingList.add(o1);
							bContains =true;
							break;
						}
				}
				
			}
			if(!bContains){
				logger.info("不包含对象:"+object);
				return false;
			}
			
		}
		return true;
	}
	
	
	/*
	 * 验证map1包含map2
	 * 以map2为基准，如果map2的字段为null，则不进行不对
	 * */
	public static boolean equals(Map<String, Object> map1,Map<String, Object> map2 ) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		if(map1.size() != map2.size()){
			logger.error("字典大小不相等");
			return false;
		}
		
		return Contains(map1, map2);
	}
	
	
	/*
	 * 
	 */
	public static boolean equals(List list1, List list2) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		if(list1.size() != list2.size()){
			logger.error("list大小不相等");
			return false;
		}
	
		return Contains(list1, list2);
	}
	
	public static boolean equalsAndSort(List list1, List list2) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		if(list1.size() != list2.size()){
			logger.error("list大小不相等");
			return false;
		}
	
		for (int i = 0; i < list1.size(); i++) {
			boolean bContains = false;
			Object object = list1.get(i);
			Object o1 = list2.get(i);
			if (object instanceof Map && o1 instanceof Map) {
				if (!equals((Map<String, Object>) o1,(Map<String, Object>) object)) {
					logger.error("第" + i + "行数据不相等");
					return false;
				} else {
					continue;
				}
			}
			else {
				if (!object.toString().equals(o1.toString())) {
					logger.error("第" + i + "行数据不相等:"+object.toString()+"和"+o1.toString());
					return false;
				} else {
					continue;
				}
			}
		}
		
		return true;
	}
	
	
	
	public  static void  main(String[] args) {
//		Map<String, Object> json= JSON.parseObject("{'data':{'currentDate':'2015-01-05 10:47:05','otherCounsumeList':[{'consumeTime':1419505679000,'consumeType':'购买道具','mainPrice':2,'subPrice':0},{'consumeContent':'绝世天魔','consumeTime':1419235368000,'consumeType':'打赏','mainPrice':999,'subPrice':0},{'consumeTime':1419234919000,'consumeType':'购买道具','mainPrice':2,'subPrice':0},{'consumeTime':1418718351000,'consumeType':'购买道具','mainPrice':2,'subPrice':0},{'consumeTime':1418718339000,'consumeType':'购买道具','mainPrice':2,'subPrice':0},{'consumeContent':'绝世天魔','consumeTime':1418356551000,'consumeType':'打赏','mainPrice':999,'subPrice':0}],'systemDate':'1420426025063'},'status':{'code':0},'systemDate':1420426025062}",new TypeReference<Map<String, Object>>() {});
//		Map<String, Object> json1= JSON.parseObject("{'data':{'currentDate':'2015-01-05 10:47:05','otherCounsumeList':[{'consumeTime':1419505679000,'consumeType':'购买道具','mainPrice':2,'subPrice':0},{'consumeContent':'绝世天魔','consumeTime':1419235368000,'consumeType':'打赏','mainPrice':999,'subPrice':0},{'consumeTime':1419234919000,'consumeType':'购买道具','mainPrice':2,'subPrice':0},{'consumeTime':1418718351000,'consumeType':'购买道具','mainPrice':2,'subPrice':0},{'consumeTime':1418718339000,'consumeType':'购买道具','mainPrice':2,'subPrice':0},{'consumeContent':'绝世天魔','consumeTime':1418356551000,'consumeType':'打赏','mainPrice':999,'subPrice':0}],'systemDate':'1420426025063'},'status':{'code':0},'systemDate':1420426025062}",new TypeReference<Map<String, Object>>() {});
//		
//		boolean result = Compare.equals(json, json1);
		List list = new ArrayList();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("d", "2");
		list.add("a");
		list.add("b");
		list.add("c");
		list.add(map1);
		List list2 = new ArrayList();
		list2.add("a");
		list2.add("c");
		list2.add("b");
		
		list2.add(map1);
		

		
	
		
		
		
	}
}
