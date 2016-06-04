package com.dangdang.ddframework.dbutil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.criteria.From;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Select;
import org.hibernate.Session;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframework.core.ConfigCore;
import com.dangdang.ddframework.core.TestEnvironment;
import com.dangdang.ddframework.dbutil.websql.MapperXmlParse;
import com.dangdang.ddframework.dbutil.websql.WebSql;
import com.dangdang.ddframework.util.SessionUtil;
import com.dangdang.ddframework.util.Util;

/*
 * 数据库操作类，主要为查询数据方法
 */
public class DbUtil {
	
//	public static String  generateHSql(Object dbMapObject) throws IllegalArgumentException, IllegalAccessException {
//		
//		Field[] fields = dbMapObject.getClass().getDeclaredFields();
//		//拼接查询字符串
//		String tableNameString = dbMapObject.getClass().getSimpleName();
//		StringBuilder  hSqlString = new StringBuilder();
//		hSqlString.append("from "+ tableNameString );
//		hSqlString.append(" where ");
//		
//		for(Field field: fields){
//			field.setAccessible(true);
//			if(field.get(dbMapObject) != null){
//				hSqlString.append(field.getName());
//				hSqlString.append("=");
//				
//				if(field.getType().toString().equals(String.class.toString()) ){
//					hSqlString.append("'");
//					hSqlString.append(field.get(dbMapObject));
//					hSqlString.append("'");
//					
//				}
//				else if(field.getType().toString().equals(Date.class.toString())){
//					hSqlString.append("'");
//					hSqlString.append(((Date)field.get(dbMapObject)).toLocaleString());
//					hSqlString.append("'");
//				}
//				else {
//					hSqlString.append(field.get(dbMapObject));
//				}
//				hSqlString.append(" and ");
//			}
//		}
//		
//		hSqlString.delete(hSqlString.length() - " and ".length(), hSqlString.length());
//		
//		return hSqlString.toString();
//	}
//
//	/*
//	 * 从数据库中获取一条记录
//	 * 根据dbMapObject类中部位null的字段做过滤条件，自动拼接查询语句
//	 * 参数：
//	 * 		dbMapObject  使用orm工具生成的类
//	 * 
//	 * return：
//	 * 		返回一条记录，如果没有返回null
//	 */
//	public static <T> T getOne(String dbConf,Object dbMapObject) throws Exception{
//		
//		String sqlString = generateHSql(dbMapObject);
//		//去数据库中查询
//		T object = SessionUtil.executeHSql(dbConf, sqlString,"获取一条记录");
//		return object;
//	}
//	
//	/*
//	 * 从数据库中获取一条记录
//	 * 根据dbMapObject类中部位null的字段做过滤条件，自动拼接查询语句
//	 * 参数：
//	 * 		dbMapObject  使用orm工具生成的类
//	 * 
//	 * return：
//	 * 		返回一条记录，如果没有返回null
//	 */
//	public static <T> T getOne(String dbConf,Class classz, String filterSql) throws Exception{
//		
//		String sqlString = "from " + classz.getSimpleName() + " where " + filterSql;
//		//去数据库中查询
//		T object = SessionUtil.executeHSql(dbConf, sqlString,"获取一条记录");
//		return object;
//	}
//	
//	
//
//	
//	/*
//	 * 根据过滤条件从数据库表中返回数据
//	 * 根据dbMapObject类中部位null的字段做过滤条件，自动拼接查询语句
//	 * 参数：
//	 * 		dbMapObject  使用orm工具生成的类
//	 * 
//	 * return：
//	 * 		返回实例列表，如果没有返回空列表
//	 */
//	public static <T> List<T> filter(String dbConf,Object dbMapObject) throws Exception{
//		String sqlString = generateHSql(dbMapObject);
//		//去数据库中查询
//		List<T> object = SessionUtil.getList(dbConf, sqlString,"获取一条记录");
//		return object;
//	}
//	
//	/*
//	 * 根据过滤条件从数据库表中返回数据
//	 * 根据dbMapObject类中部位null的字段做过滤条件，自动拼接查询语句
//	 * 参数：
//	 * 		dbMapObject  使用orm工具生成的类
//	 * 
//	 * return：
//	 * 		返回实例列表，如果没有返回空列表
//	 */
//	public static <T> List<T> filter(String dbConf,String hsql) throws Exception{
//		//String sqlString = generateHSql(dbMapObject);
//		//去数据库中查询
//		List<T> object = SessionUtil.getList(dbConf, hsql,"获取一条记录");
//		return object;
//	}
//	
//	/*
//	 * 从数据库表中返回所有记录
//	 * 根据cls类型确定要查询的数据表，自动拼接查询语句
//	 * 参数：
//	 * 		cls  使用orm工具生成的类的类型
//	 * 
//	 * return：
//	 * 		返回实例列表，如果没有返回空列表
//	 */
//	 
//	public static <T> List<T> getAll(String dbConf,Class cls) throws Exception {
//		String sqlString = "from " + cls.getSimpleName();
//		List<T> object = SessionUtil.getList(dbConf, sqlString,"获取一条记录");
//		return object;	
//		}
//	
//	
//	
//	/*
//	 * 执行HSql语句查询，返回Map，返回但行数据，并将返回数据转换成对应数据类型
//	 */
//	public static <T> T executeHSql(String dbConf,String sql) throws Exception{
//		return SessionUtil.executeHSql(dbConf, sql, "");
//	}
//	
//	
//	/*
//	 * 执行HSql语句查询，返回指定行数数据，并将数据转换成对应数据类型
//	 */
//	public static <T> T executeHSql(String dbConf,String sql,int returnRowCount) throws Exception{
//		return SessionUtil.executeHSql(dbConf, sql, returnRowCount,"");
//	}
//	
//	 /*
//     * 保存或者更新对象，
//     * 参数：
//     * 		conf：数据库配置文件相对路径
//     * 		o：	  需要保存或者更新的对象实例，为ORM生成类
//     */
//	public static void Save(String conf,Object o) throws Exception {
//		SessionUtil.Save(conf, o);
//	}
//	
	
	/*
	 * 执行Sql语句查询，返回Map
	 */
	public static List<Map<String, Object>> selectList(String dbConf,String sql) throws Exception{
		if(ConfigCore.getEnvironment() == TestEnvironment.TESTING){
			return SessionUtil.executeSql(dbConf, sql, "");
		}else {
			return WebSql.selectList(dbConf, sql, "");
		}
		
	}
	
	/*
	 * 执行Sql语句查询，返回Map
	 */
	public static Map<String, Object> selectOne(String dbConf,String sql) throws Exception{
		if(ConfigCore.getEnvironment() == TestEnvironment.TESTING){
			return SessionUtil.selectOne(dbConf, sql, "");
		}else {
			return WebSql.selectOne(dbConf, sql, "");
		}
		
	}

	
	/*
	 * mysql随机几条记录，返回表Id字段
	 * 参数：
	 * 		conf：数据库配置文件相对路径
	 * 		table：要随机的表明
	 * 		idField：表的id字段
	 * 		randomNumber：随机的个数
	 * 返回：
	 * 		返回数据的列表
	 */
	public static List<Map<String, Object>> Random(String conf, String table, String idField,String whereCaulse, int randomNumber) throws Exception{
		return Random(conf,table,idField,idField,whereCaulse,randomNumber);
		
	}
	
	/*
	 * mysql随机几条记录，返回表Id字段
	 * 参数：
	 * 		conf：数据库配置文件相对路径
	 * 		table：要随机的表明
	 * 		idField：表的id字段
	 * 		randomNumber：随机的个数
	 * 返回：
	 * 		返回idField的列表
	 */
	public static <T> List<T>  Random(String conf, String table, String idField,String whereCaulse, int randomNumber,Class classz) throws Exception{
		return Util.ConvertToObject(Random(conf,table,idField,idField,whereCaulse,randomNumber),classz);
		
	}
	
	/*
	 * mysql随机1条记录，返回表Id字段
	 * 参数：
	 * 		conf：数据库配置文件相对路径
	 * 		table：要随机的表明
	 * 		idField：表的id字段
	 * 		randomNumber：随机的个数
	 * 返回：
	 * 		返回idField的列表
	 */
	public static <T> T Random(String conf, String table, String idField,String whereCaulse,Class classz) throws Exception{
		List<Map<String, Object>> lists=Random(conf,table,idField,whereCaulse,1);
	
		return Util.ConvertToObject(lists.get(0), classz);
	}
	
	/*
	 * mysql随机1条记录，返回表Id字段
	 * 参数：
	 * 		conf：数据库配置文件相对路径
	 * 		table：要随机的表明
	 * 		idField：表的id字段
	 * 		randomNumber：随机的个数
	 * 返回：
	 * 		返回idField的列表
	 */
	public static Map<String, Object> Random(String conf, String table, String idField,String whereCaulse) throws Exception{
		List<Map<String, Object>> lists=Random(conf,table,idField,whereCaulse,1);
		return lists.get(0);
		
	}
	
	/*
	 * mysql随机几条记录，返回表Id字段
	 * 参数：
	 * 		conf：数据库配置文件相对路径
	 * 		table：要随机的表明
	 * 		idField：表的id字段
	 * 		returnFiled:要返回的字段
	 * 		randomNumber：随机的个数
	 * 返回：
	 * 		返回idField的列表
	 */
	public static List<Map<String, Object>> Random(String conf, String table, String idField,String returnFiled,String whereCaulse,int randomNumber) throws Exception{
		String[] returnFieldsStrings= returnFiled.split(",");
		
		StringBuilder builder =new StringBuilder();
		for (int i = 0; i < returnFieldsStrings.length; i++) {
			builder.append("t1.");
			builder.append(returnFieldsStrings[i]);
			if(i < returnFieldsStrings.length-1){
				builder.append(",");
			}
		}
		
		String getidSqlString = "select "+idField+" from "+ table + (StringUtils.isBlank(whereCaulse) ? "":" where "+ whereCaulse);
		List<Map<String, Object>> idResultsList =DbUtil.selectList(conf,getidSqlString);
		
		List<String> selectIdsList = new ArrayList<String>();
		if(idResultsList.size() < randomNumber){
			for(Map<String, Object> map: idResultsList){
				selectIdsList.add(map.get(idField).toString());
			}
		}
		else {
			Random random = new Random();
			for(int i=0;i<randomNumber;i++ ){
				int nIndex=random.nextInt(idResultsList.size());
				selectIdsList.add(idResultsList.get(nIndex).get(idField).toString());
				idResultsList.remove(nIndex);
			}
		}
		
		getidSqlString = "select * from "+table+ (StringUtils.isBlank(whereCaulse) ? " where " : " where " + whereCaulse+" and ")+ idField +" in ("+StringUtils.join(selectIdsList,",")+")";
		return DbUtil.selectList(conf, getidSqlString);
	}
	
	
	
	
	/*
	 * 执行sql语句封装函数，staging和online测试执行websql，测试环境使用hiberanate
	 *  参数：
     * 		conf：数据库被指文件相对路径
     * 		sql： 要执行的sql语句
     *      classz:返回对象类型
     * 		msg：执行Sql注解，描述此查询的目的
     * 返回：
     * 		返回classz类型对象列表
	 */
	public static List selectList(String conf,String sql,Class classz) throws Exception {
		if(ConfigCore.getEnvironment()==TestEnvironment.TESTING){
			return SessionUtil.executeSqlT(conf, sql, classz, "");
		}
		else {
			return WebSql.selectListT(conf, sql, classz, "");
		}
		
	}
	
	
	/*
	 * 执行sql语句封装函数，staging和online测试执行websql，测试环境使用hiberanate
	 *  参数：
     * 		conf：数据库被指文件相对路径
     * 		sql： 要执行的sql语句
     *      classz:返回对象类型
     * 		msg：执行Sql注解，描述此查询的目的
     * 返回：
     * 		返回classz类型对象
	 */
	public static <T> T selectOne(String conf,String sql,Class classz) throws Exception {
		if(ConfigCore.getEnvironment()==TestEnvironment.TESTING){
			return SessionUtil.selectOneT(conf, sql, classz, "");
		}
		else {
			return WebSql.selectOneT(conf, sql, classz, "");
		}
		
	}
	
	public static void executeUpdate(String conf, String sql) throws Exception{
		if(ConfigCore.getEnvironment()==TestEnvironment.TESTING){
			SessionUtil.executeUpdate(conf, sql, "");
		}else{
			throw new Exception("只有测试环境才支持增删改操作！");
		}
	}
	

    /*
    将Map中转换成where 条件语句，全部为and关系
     */
	public static String ConvertFilterString(Map<String,Object> paramMap){

		String returnStr="";
		if(paramMap==null){
			return returnStr;
		}

        int nIndex =0;
		for(String key: paramMap.keySet()){

			returnStr+=" "+key;
			if(paramMap.get(key) instanceof List){
				returnStr+=" in ("+StringUtils.join(paramMap.get(key),",")+")";
			}
			else{
				returnStr+=" = "+ paramMap.get(key);
			}

            if(nIndex < paramMap.keySet().size()-1) {
                returnStr += " and ";
            }
            nIndex++;
		}

        return returnStr;
	}
}
