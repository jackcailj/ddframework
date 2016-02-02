package com.dangdang.ddframework.dbutil.websql;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.DocumentException;
import org.xml.sax.SAXException;

public class WebSql {
	
	public static List<Map<String, Object>> selectList(String conf,String sql,String msg) throws Exception {
		return WebSqlUtil.getConnection(conf).selectList(sql, msg);
	}
	
	public static Map<String, Object> selectOne(String conf,String sql,String msg) throws Exception {
		return WebSqlUtil.getConnection(conf).selectOne(sql, msg);
	}
	
	public static List selectListT(String conf,String sql,Class classz,String msg) throws Exception {
		return WebSqlUtil.getConnection(conf).selectListT(sql, classz, msg);
	}
	
	public static <T> T selectOneT(String conf,String sql,Class classz,String msg) throws Exception {
		return WebSqlUtil.getConnection(conf).selectOneT(sql, classz, msg);
	}
}
