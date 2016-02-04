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

import org.apache.commons.lang3.StringUtils;
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
	
	
	public ResultSet(String result,Class classz) throws Exception {
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

	protected List parseResult(String result,Class classz) throws Exception {
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
					String colgroup=tdmatcher.group(1).trim();
					//colgroup=handleSpecitalString(colgroup);
					if(i==1){
						//为列名
						columns.add(colgroup);
					}
					else {
						//为row数据。
                        String cloumnName="";
                        Map<String, Object> temp = (Map<String, Object>) row;

						if(filedInfoMap!=null) {
							FiledInfo info = filedInfoMap.get(columns.get(j++));
							if(info==null){
                                throw new Exception("获取字段异常，字段名:"+columns.get(j-1));
								//logger.error("获取字段异常，字段名:"+columns.get(j-1));
							}
							else {
                                cloumnName=info.getAliasName();
								//Map<String, Object> temp = (Map<String, Object>) row;

                                //查出来的字符串可能为Object或者Array，需要进行处理
								//Object先用正则^{.*}$来匹配
								//Array先用正则^[.*]$来匹配
								//Matcher objectMacher = Pattern.compile("^\\{.+\\}$",Pattern.DOTALL).matcher(colgroup.trim());
                                //Matcher arrayMacher = Pattern.compile("^[.+]$",Pattern.DOTALL).matcher(colgroup.trim());

							}
						}
						else//非数据库映射类
						{
							//Map<String, Object> temp = (Map<String, Object>) row;
                            cloumnName=columns.get(j++);
							//temp.put(columns.get(j++), colgroup);
						}

                        if(colgroup.startsWith("{") && colgroup.endsWith("}")) {
                            //logger.info("处理Map字符串");
                            Map<String,Object> objectMap = JSONObject.parseObject(colgroup.trim());
                            temp.put(cloumnName, objectMap);
                        }
                        else if(colgroup.startsWith("[") && colgroup.endsWith("]")){
                            List objectArray = JSONObject.parseArray(colgroup.trim());
                            temp.put(cloumnName, objectArray);
                        }
                        else{
                            temp.put(cloumnName, colgroup);
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

		logger.info("JSONString:"+JSONObject.toJSONString(list));
		return JSONObject.parseArray(JSONObject.toJSONString(list), classz);
	}

	/*
	处理结果中的特殊字符
	 */
	protected String handleSpecitalString(String text){
		if(text!=null){
			if(text.startsWith("http")){
				return text;
			}

			text=StringUtils.replace(text,"?"," ");
		}

		return text;
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


