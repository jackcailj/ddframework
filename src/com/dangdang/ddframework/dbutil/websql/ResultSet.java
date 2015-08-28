package com.dangdang.ddframework.dbutil.websql;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.expr.NewArray;

import javax.xml.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONObject;

public class ResultSet {

	public static Logger logger = Logger.getLogger(ResultSet.class);
	
	List results;
	
	
	public ResultSet(String result,Class classz) throws SecurityException, ParserConfigurationException, InstantiationException, IllegalAccessException, DocumentException, NoSuchFieldException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, ParseException, SAXException {
		// TODO Auto-generated constructor stub
		results = parseResult(result,classz);
	}
	
	public ResultSet(String result) throws SecurityException, ParserConfigurationException, InstantiationException, IllegalAccessException, DocumentException, NoSuchFieldException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, ParseException, SAXException {
		// TODO Auto-generated constructor stub
		results = parseResult(result);
	}
	
	public List getResults() {
		return results;
	}

	protected List parseResult(String result,Class classz) throws ParserConfigurationException, InstantiationException, IllegalAccessException, DocumentException, SecurityException, NoSuchFieldException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, ParseException, SAXException{
		Matcher rowmatcher = Pattern.compile("<tr>(.*?)</tr>",Pattern.DOTALL).matcher(result);
		List list =new ArrayList();
		
		List<String> columns= new ArrayList<String>();
		

		//读取hibernate配置文件，查找映射关系，获得映射后的属性名
		Map<String, FiledInfo> filedInfoMap = MapperXmlParse.ParseXml(classz);
			
		int i=0;
		while(rowmatcher.find()){
				if(i==0){
					i++;
					continue;
				}
				String group=rowmatcher.group(1);
				Matcher tdmatcher = Pattern.compile("<td>(.*?)</td>",Pattern.DOTALL).matcher(group);
				
				int j=0;
				
				Object row=new HashMap<String, String>();	
				while(tdmatcher.find()){
					String colgroup=tdmatcher.group(1);
					if(i==1){
						//为列名
						columns.add(colgroup);
					}
					else {
						//为row数据。
						if(filedInfoMap!=null) {
							FiledInfo info = filedInfoMap.get(columns.get(j++));
							if(info==null){
								logger.error("获取字段异常，字段名:"+columns.get(j-1));
							}
							else {
								Map<String, Object> temp = (Map<String, Object>) row;
								temp.put(info.getAliasName(), colgroup);
							}
						}
						else//非数据库映射类
						{
							Map<String, Object> temp = (Map<String, Object>) row;
							temp.put(columns.get(j++), colgroup);
						}

					}
				}
				
				//>1为行数据， 1为列数据
				if(i>1)
				{
					list.add(row);
				}
				i++;
				
			}

		return JSONObject.parseArray(JSONObject.toJSONString(list), classz);
	}
	
	protected List parseResult(String result)
			throws ParserConfigurationException, InstantiationException,
			IllegalAccessException, DocumentException, SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, ParseException, SAXException {
		Matcher rowmatcher = Pattern.compile("<tr>(.*?)</tr>",Pattern.DOTALL).matcher(result);
		List list = new ArrayList();

		List<String> columns = new ArrayList<String>();

		int i = 0;
		while (rowmatcher.find()) {
			if (i == 0) {
				i++;
				continue;
			}
			String group = rowmatcher.group(1);
			Matcher tdmatcher = Pattern.compile("<td>(.*?)</td>",Pattern.DOTALL)
					.matcher(group);

			int j = 0;
			Map<String, Object> row = new HashMap<String, Object>();
			while (tdmatcher.find()) {
				String colgroup = tdmatcher.group(1);
				if (i == 1) {
					// 为列名
					columns.add(colgroup);
				} else {
					// 为row数据。
					row.put(columns.get(j++), colgroup);

				}
			}

			// >1为行数据， 1为列数据
			if (i > 1) {
				list.add(row);
			}
			i++;

		}
		// }
		return list;
	}
}


