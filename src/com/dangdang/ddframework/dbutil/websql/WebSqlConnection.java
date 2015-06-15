package com.dangdang.ddframework.dbutil.websql;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframework.drivers.HttpDriver;
import com.dangdang.ddframework.util.ResourceLoader;

public class WebSqlConnection {
	protected String url;
	protected Map<String, String> dbParam;
	protected static Logger logger = Logger.getLogger(WebSqlConnection.class);
	
	public WebSqlConnection(String conf) {
		// TODO Auto-generated constructor stub
		Configure(conf);
	}
	
	protected void Configure(String confFile) {
		Properties properties = ResourceLoader.loadCurrentPropertyFile(confFile);
		url =properties.getProperty("url");
		properties.remove("url");
		dbParam = new HashMap<String, String>((Map)properties);		
	}
	
	
	
	public  List selectListT(String sql,Class classz,String msg) throws Exception {
		Map<String, String> params= new HashMap<String, String>();
		
		params.putAll(dbParam);
		params.put("sqlstr", sql);
		params.put("select", "确认");
		logger.info("执行sql--"+sql);
		String resultString = HttpDriver.doPost(url, params,"gbk",false,false);
		
		ResultSet resultSet =new ResultSet(resultString, classz);
		logger.info("执行sql--结束，结果："+JSONObject.toJSONString(resultSet.getResults()));
		
		return resultSet.getResults();
	}
	
	public  <T> T selectOneT(String sql,Class classz,String msg) throws Exception {
		logger.info("执行sql--"+sql);
		List result = selectListT(sql, classz, msg);
		if(result.size()>1){
			throw new Exception("selectOneT返回了多个结果，sql："+sql);
		}
		else if(result.size()==0){
			throw new Exception("selectOneT没有返回结果，sql："+sql);
		}
		
		logger.info("执行sql--结束，结果："+JSONObject.toJSONString(result.get(0)));
		
		return (T) result.get(0);
	}
	
	public  List<Map<String, Object>> selectList(String sql,String msg) throws Exception {
		Map<String, String> params= new HashMap<String, String>();
		
		params.putAll(dbParam);
		params.put("sqlstr", sql);
		params.put("select", "确认");
		
		logger.info("执行sql--"+sql);
		
		String resultString = HttpDriver.doPost(url, params, "UTF-8", true,false);
		
		ResultSet resultSet =new ResultSet(resultString);
		logger.info("执行sql--结束，结果："+JSONObject.toJSONString(resultSet.getResults()));
		return resultSet.getResults();
	}
	
	
	/*
	 *   返回一行值
	 */
	public Map<String, Object> selectOne(String sql,String msg) throws Exception {
		logger.info("执行sql--"+sql);
		List result = selectList(sql, msg);
		if(result.size()>1){
			throw new Exception("selectOneT返回了多个结果，sql："+sql);
		}
		else if(result.size()==0){
			throw new Exception("selectOneT没有返回结果，sql："+sql);
		}
		logger.info("执行sql--结束，结果："+JSONObject.toJSONString(result.get(0)));
		return (Map<String, Object>) result.get(0);
	}

}
