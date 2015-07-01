package com.dangdang.ddframework.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.dom4j.DocumentException;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframework.dbutil.websql.FiledInfo;
import com.dangdang.ddframework.dbutil.websql.MapperXmlParse;

public class Util {
	
	/**
	 * Generate a map from string
	 * 
	 * @param param
	 *        the string of request url
	 * 
	 * @author wuhaiyan
	 */
	public static Map<String,String> generateMap(String param){
		String[] strList = param.split("&");
    	Map<String,String> params = new HashMap<String,String>();
    	for(int i=0; i< strList.length; i++ ){
    		String[] para = strList[i].split("=");
    		params.put(para[0], para.length >1 ?para[1]:"");
    	}    	
    	return params;
	}
	
	public static <T> T ConvertToObject(Map<String, Object> value,Class classz) throws InstantiationException, IllegalAccessException, DocumentException, SAXException, SecurityException, NoSuchFieldException{
		
		Map<String, FiledInfo> filedInfos= MapperXmlParse.ParseXml(classz);
		if(filedInfos==null){
			return (T) JSONObject.parseObject(JSONObject.toJSONString(value), classz);
		}
		
		
		Map<String,Object> objectMap=new HashMap<String,Object>();
		for(Entry<String, Object> enrty: value.entrySet()){
			if(filedInfos.get(enrty.getKey())==null){
				continue;
			}
			
			objectMap.put(filedInfos.get(enrty.getKey()).getAliasName(), enrty.getValue());
			//Field field=classz.getDeclaredField(filedInfos.get(enrty.getKey()).getAliasName());
			//field.setAccessible(true);
			//field.set(object, enrty.getValue());
		}
		
		
		return (T) JSONObject.parseObject(JSONObject.toJSONString(objectMap), classz);
	}
	
	public static <T> List<T> ConvertToObject(List<Map<String, Object>> lists,Class classz) throws InstantiationException, IllegalAccessException, DocumentException, SAXException, SecurityException, NoSuchFieldException{
		List<T> listObjects =new ArrayList<T>();
		for(Map<String, Object> map:lists){
			listObjects.add((T) ConvertToObject(map, classz));
			
		}
		
		return listObjects;
	}
	
	public static String getRandomString(int length) { 
	    StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"); 
	    StringBuffer sb = new StringBuffer(); 
	    Random r = new Random(); 
	    int range = buffer.length(); 
	    for (int i = 0; i < length; i ++) { 
	        sb.append(buffer.charAt(r.nextInt(range))); 
	    } 
	    return sb.toString(); 
	}
}
