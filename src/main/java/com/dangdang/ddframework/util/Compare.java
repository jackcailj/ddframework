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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionContext;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.*;
import com.google.gson.Gson;

public class Compare {
	public static Logger logger = Logger.getLogger(Compare.class);


	public static boolean compareObject(Object object1,Object object2) throws Exception {

		if(object1 == null && object2==null
				|| (object1!=null && StringUtils.isBlank(object1.toString()) && object2==null)
				|| (object1==null && (object2!=null && StringUtils.isBlank(object2.toString())))){
			//logger.info(keyString+"值为null，不进行比对");
			return true;
		}

		if(object1==null || object2 ==null){
		    return false;
        }

		//如果是列表，比对类表是否包含
		if(object1 instanceof List){
			List tempList1=(List)object2;
			List tempList2=(List)object1;
			if(!equalsAndSort(tempList1,tempList2)){
				return false;
			}
		}
		else if(object1 instanceof Map)
		{
			Map<String, Object> tempMap1=(Map<String, Object>)object2;
			Map<String, Object> tempMap2=(Map<String, Object>)object1;
			if(!equals(tempMap1,tempMap2)){
				return false;
			}
		}
		else {
			if(object1 instanceof Timestamp || object1 instanceof Date){
				long time1=(Long) object1.getClass().getMethod("getTime",null).invoke(object1);
				Long time2=null;
				if(object2 instanceof Timestamp || object2 instanceof Date ) {
					time2 = (Long) object2.getClass().getMethod("getTime", null).invoke(object2);
				}
				else{
					time2= (Long) object2;
				}

				if(time1!=time2){
					//logger.error("不一致的地方：值【key:"+keyString+" value:"+object2+"】和【key:"+keyString+" value:"+valueString+"】");
					return false;
				}
			}
			else if(object1 instanceof BigDecimal){
				//BigDecimal比较大小时使用equals不行，需要用compareTo
				if(((BigDecimal) object1).compareTo(new BigDecimal(object2.toString()))!=0){
					//logger.error("不一致的地方：值【key:"+keyString+" value:"+object2+"】和【key:"+keyString+" value:"+valueString+"】");
					return false;
				}
			}
			else if(!object1.toString().equals(object2.toString())){

				//logger.error("不一致的地方：值【key:"+keyString+" value:"+object2+"】和【key:"+keyString+" value:"+valueString+"】");
				return false;
			}
		}
		return true;
	}


	/*
	 * 验证map1包含map2
	 * 以map2为基准，如果map2的字段为null，则不进行不对
	 * */
	public static boolean Contains(Map<String, Object> map1,Map<String, Object> map2 ) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		List<CompareResult> compareResults =  map2.entrySet().parallelStream().map(entry->{

			CompareResult cresult = new CompareResult();

			Object object1 = entry.getValue();
			Object object2 = map1.get(entry.getKey());
			boolean result =false;
			cresult.setContent("属性【"+entry.getKey()+"】");
            cresult.setValue1(object1);
            cresult.setValue2(object2);

			try {
				result = compareObject(object1,object2);
			} catch (Exception e) {
				cresult.setInfo(Util.getStrackTrace(e));
				e.printStackTrace();
			}
			finally {
				cresult.setResult(result);
			}
			return cresult;
		}).collect(Collectors.toList());

		List<CompareResult> failedResults =compareResults.stream().filter(compareResult -> compareResult.getResult()==false).collect(Collectors.toList());
		if(failedResults.size()>0){
			logger.error("对象不一致："+JSONObject.toJSONString(failedResults));
			return false;
		}

		return true;

		/*for(Entry<String, Object> entry: map2.entrySet()){
			String keyString=entry.getKey();
			Object valueString=entry.getValue();

			logger.info("开始对比属性:"+keyString);

			if(!map1.containsKey(keyString)){
				logger.error("不包含key:"+keyString);
				return false;
			}
			
			if(valueString == null ){
				logger.info(keyString+"值为null，不进行比对");
				continue;
			}


			if(map1.get(keyString)==null){
				//字符串空值和null认为相等。
				if(StringUtils.isBlank(valueString.toString())){
					continue;
				}
				logger.error("value1 "+keyString+"值为null，不正确");
				return false;
			}
			
			//如果是列表，比对类表是否包含
			if(valueString instanceof List){
				List tempList1=(List)map1.get(keyString);
				List tempList2=(List)valueString;
				if(!equalsAndSort(tempList1,tempList2)){
					return false;
				}
			}
			else if(valueString instanceof Map)
			{
				Map<String, Object> tempMap1=(Map<String, Object>)map1.get(keyString);
				Map<String, Object> tempMap2=(Map<String, Object>)valueString;
				if(!equals(tempMap1,tempMap2)){
					return false;
				}
			}
			else {
				if(valueString instanceof Timestamp || valueString instanceof Date){
					long time1=(Long) valueString.getClass().getMethod("getTime",null).invoke(valueString);
					Long time2=null;
					if(map1.get(keyString) instanceof Timestamp || map1.get(keyString) instanceof Date ) {
						time2 = (Long) map1.get(keyString).getClass().getMethod("getTime", null).invoke(valueString);
					}
					else{
						time2= (Long) map1.get(keyString);
					}
					
					if(time1!=time2){
						logger.error("不一致的地方：值【key:"+keyString+" value:"+map1.get(keyString)+"】和【key:"+keyString+" value:"+valueString+"】");
						return false;
					}
				}
				else if(valueString instanceof BigDecimal){
                    //BigDecimal比较大小时使用equals不行，需要用compareTo
					if(((BigDecimal) valueString).compareTo(new BigDecimal(map1.get(keyString).toString()))!=0){
						logger.error("不一致的地方：值【key:"+keyString+" value:"+map1.get(keyString)+"】和【key:"+keyString+" value:"+valueString+"】");
						return false;
					}
				}
				else if(!valueString.toString().equals(map1.get(keyString).toString())){

					logger.error("不一致的地方：值【key:"+keyString+" value:"+map1.get(keyString)+"】和【key:"+keyString+" value:"+valueString+"】");
					return false;
				}
			}
		}
		return true;*/
	}
	
	/**
	 * 验证list2是否包含list1
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

        Boolean allResult = list1.parallelStream().allMatch(o -> {

            CompareResult compareResult = new CompareResult();
            compareResult.setContent("是否包含:" + JSONObject.toJSONString(o));
            Boolean bResult = list2.parallelStream().anyMatch(object -> {

                boolean result = false;
                try {
                    if (object instanceof Map && o instanceof Map) {
                        if (Contains((Map<String, Object>) o, (Map<String, Object>) object)) {
                            result = true;
                        }
                    } else {
                        if (object.toString().equals(o.toString())) {
                            result = true;
                        }
                    }
                } catch (Exception e) {
                    logger.error(Util.getStrackTrace(e));
                }
                return result;
            });
            compareResult.setResult(bResult);
            if(bResult ==false){
                logger.error("不包含对象:"+o);
            }
            return bResult;

        });

        return allResult;


		/*List machingList = new ArrayList();
		for(Object object : list1){
			boolean bContains = false;
			for(Object o1:list2){

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
				logger.error("不包含对象:"+object);
				return false;
			}
			
		}
		return true;*/
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
		logger.info("List对比...");
		logger.info("List1:"+list1);
		logger.info("List2:"+list2);

		if(list1.size() != list2.size()){
			logger.error("list大小不相等");
			return false;
		}
	
		return Contains(list1, list2);
	}
	
	public static boolean equalsAndSort(List list1, List list2) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		logger.info("List对比...");
		logger.info("List1:"+list1);
		logger.info("List2:"+list2);

		if(list1.size() != list2.size()){
			logger.error("list大小不相等");
			return false;
		}

        Boolean allResult = IntStream.range(0, list1.size()).parallel().allMatch(idx->{
           Object object1 =  list1.get(idx);
            Object object2 =  list2.get(idx);

            Boolean bResult = false;
            try {
                if (object1 instanceof Map && object2 instanceof Map) {
                    if (Contains((Map<String, Object>) object1, (Map<String, Object>) object2)) {
                        bResult=true;
                    }
                } else {
                    if (object1.toString().equals(object2.toString())) {
                        return true;
                    }
                }
            }catch (Exception e){
                logger.error(Util.getStrackTrace(e));
            }
            finally {

                if(bResult ==false){
                    CompareResult compareResult = new CompareResult();
                    compareResult.setValue1(object1);
                    compareResult.setValue1(object2);
                    compareResult.setResult(bResult);
                    compareResult.setInfo("第【"+idx +"】条记录不相等");
                }

                return bResult;
            }
        });

        return allResult;
//
//        for (int i = 0; i < list1.size(); i++) {
//
//			logger.info("开始对比第"+i+"行数据...");
//			boolean bContains = false;
//			Object object = list1.get(i);
//			Object o1 = list2.get(i);
//			if (object instanceof Map && o1 instanceof Map) {
//				if (!Contains((Map<String, Object>)object,(Map<String, Object>) o1)) {
//					logger.error("结束对比对比第" + i + "行数据---不相等");
//					return false;
//				}
//			}
//			else {
//				if (!object.toString().equals(o1.toString())) {
//					logger.error("结束对比对比第" + i + "行数据---不相等:" + object.toString() + "和" + o1.toString());
//					return false;
//				}
//			}
//			logger.info("结束对比对比第"+i+"行数据---正确");
//		}
//
//		return true;
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


class CompareResult{
	String content;
	Boolean result;
    Object value1;
    Object value2;
	String  info;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

    public Object getValue1() {
        return value1;
    }

    public void setValue1(Object value1) {
        this.value1 = value1;
    }

    public Object getValue2() {
        return value2;
    }

    public void setValue2(Object value2) {
        this.value2 = value2;
    }
}