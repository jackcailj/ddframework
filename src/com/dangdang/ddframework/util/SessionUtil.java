package com.dangdang.ddframework.util;



import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.LockMode;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.transform.Transformers;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;


@SuppressWarnings("deprecation")
public class SessionUtil {
    //private static SessionFactory factory;
    //private static Session session;
    //private static Map<String, Session> sessionManager = new HashMap<String, Session>();
	public static Logger logger = Logger.getLogger(SessionUtil.class);
    private static Map<String, SessionFactory> factoryManager = new HashMap<String, SessionFactory>();

    public static Session getSession(){
    	String conf = "conf/hibernate.cfg.xml";
    	return __getSession(conf);
        
    } 
    
    public static Session getSession(String propertyFile){
        if(StringUtils.isEmpty(propertyFile)){
            return  getSession();
        }
        else {
            return __getSession(propertyFile);
        }
    } 
    
    private static Session __getSession(String conf)
    {
    	if(factoryManager.containsKey(conf))
    	{
    		return factoryManager.get(conf).openSession(); 
    	}
    	else {
    		Configuration cfg = new Configuration().configure(conf);
    		ServiceRegistry resgistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
    		SessionFactory factory = cfg.buildSessionFactory(resgistry);
    		
        	Session session = factory.openSession();
        	factoryManager.put(conf, factory);
        	return session;
		}
    }
    public static void closeSession(Session session){
        if(session != null){
        	session.close();
        	/*for (Entry<String, Session> entry: sessionManager.entrySet()) {
        		if(entry.getValue() == session)
        		{
        			sessionManager.remove(entry.getKey());
        			break;
        		}
        	}*/
        }
    }
    
    
    /*
     * 执行sql返回对象列表
     * 参数：
     * 		conf：数据库被指文件相对路径
     * 		sql： 要执行的sql语句
     * 		msg：执行Sql注解，描述此查询的目的
     */
    public static List<Map<String, Object>> executeSql(String conf,String sql,String msg) throws Exception {
		if(msg != null && !StringUtils.isEmpty(msg)){
			logger.info(msg);
		}
		
		Session session = null;
		try{
			session =getSession(conf);
			List<Map<String, Object>> result = session
					.createSQLQuery(sql).setCacheMode(CacheMode.IGNORE)
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			logger.info("执行SQL："+sql);			
			logger.info("执行结果："+result);
		
			return result;
		}
		catch(Exception e){
			logger.error("执行SQL异常："+e);
			throw e;
		}
		finally{
			closeSession(session);
		}
		
	}
    
    
    /*
     * 执行sql返回对象列表
     * 参数：
     * 		conf：数据库被指文件相对路径
     * 		sql： 要执行的sql语句
     *      classz:返回对象类型
     * 		msg：执行Sql注解，描述此查询的目的
     */
    public static List executeSqlT(String conf,String sql,Class classz,String msg) throws Exception {
		if(msg != null && !StringUtils.isEmpty(msg)){
			logger.info(msg);
		}
		
		Session session = null;
		try{
			logger.info("执行SQL："+sql);
			session =getSession(conf);
			if(session.getSessionFactory().getClassMetadata(classz)==null){
				//如果是自定义类型，使用fastjson进行解析
				
				List result = session
						.createSQLQuery(sql).setCacheMode(CacheMode.IGNORE)
						.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
						.list();
							
				logger.info("执行结果："+result);
				
				//List resultObject = JSONObject.parseArray(JSONObject.toJSONString(result), classz);
				
				return Util.ConvertToObject(result, classz);
			}
			else{
				List result = session
						.createSQLQuery(sql).setCacheMode(CacheMode.IGNORE)
						.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
						.list();
				logger.info("执行SQL："+sql);			
				logger.info("执行结果："+result);
				
				return Util.ConvertToObject(result, classz);
			}
		
			
		}
		catch(Exception e){
			logger.error("执行SQL异常："+e);
			throw e;
		}
		finally{
			closeSession(session);
		}
		
	}
    
    /*
     * 返回一个值
     */
    public static Map<String, Object> selectOne(String conf,String sql,String msg) throws Exception {
		List<Map<String, Object>> result=executeSql(conf, sql, msg);
		if(result.size()>1){
			throw new Exception("selectOne返回了多个结果，sql："+sql);
		}
		else if(result.size()==0){
			throw new Exception("selectOne没有返回结果，sql："+sql);
		}
		
		return result.get(0);
	}
    
    /*
     * 执行sql返回对象列表
     * 参数：
     * 		conf：数据库被指文件相对路径
     * 		sql： 要执行的sql语句
     *      classz:返回对象类型
     * 		msg：执行Sql注解，描述此查询的目的
     */
    public static <T> T selectOneT(String conf,String sql,Class classz,String msg) throws Exception {
		if(msg != null && !StringUtils.isEmpty(msg)){
			logger.info(msg);
		}
		
		Session session = null;
		try{
			session =getSession(conf);
			T result = (T) session.createSQLQuery(sql).setCacheMode(CacheMode.IGNORE)
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
					.uniqueResult();
			logger.info("执行SQL："+sql);			
			logger.info("执行结果："+result);
		
			return Util.ConvertToObject((Map<String,Object>)result, classz);
		}
		catch(Exception e){
			logger.error("执行SQL异常："+e);
			throw e;
		}
		finally{
			closeSession(session);
		}
		
	}
    
    /*
     * 执行Hsql返回1个对象并转换成此对象类型
     * 参数：
     * 		conf：数据库被指文件相对路径
     * 		sql： 要执行的sql语句
     * 		msg：执行Sql注解，描述此查询的目的
     */
    public static <T> T executeHSql(String conf,String sql,String msg) throws Exception {
    	return  executeHSql(conf,sql,1,msg);
	}
    
    /*
     * 执行Hsql返回设置的对象个数
     * 参数：
     * 		conf：数据库被指文件相对路径
     * 		sql： 要执行的sql语句
     * 		rowNumber：要返回的最大行数
     * 		msg：执行Sql注解，描述此查询的目的
     */
    public static <T> T executeHSql(String conf,String sql, int rowNumber, String msg) throws Exception {
		if(msg != null && !StringUtils.isEmpty(msg)){
			logger.info(msg);
		}
		
		Session session = null;
		try{
			session =getSession(conf);
			if(rowNumber==1)
			 {
				T result = (T)session.createQuery(sql).setMaxResults(rowNumber).uniqueResult();
				logger.info("执行结果："+result);
				return result;
			 }
			else {
				T result = (T)session.createQuery(sql).setMaxResults(rowNumber).list();
				logger.info("执行结果："+result);
				return result;
			}
					//.setCacheMode(CacheMode.IGNORE);
					//.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);//.list();

		}
		catch(Exception e){
			logger.error("执行SQL异常："+e);
			throw e;
		}
		finally{
			closeSession(session);
		}
		
	}
    
    /*
     * 执行Hsql返回数据列表，并将数据转换成对应的类型
     * 参数：
     * 		conf：数据库被指文件相对路径
     * 		sql： 要执行的sql语句
     * 		msg：执行Sql注解，描述此查询的目的
     */
    public static <T> List<T> getList(String conf,String sql,String msg) throws Exception {
		if(msg != null && !StringUtils.isEmpty(msg)){
			logger.info(msg);
		}
		
		Session session = null;
		try{
			session =getSession(conf);			

			 List result = session.createQuery(sql).list();
					//.setCacheMode(CacheMode.IGNORE);
					//.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);//.list();
			 
			logger.info("执行结果："+result);
		
			return result;
		}
		catch(Exception e){
			logger.error("执行SQL异常："+e);
			throw e;
		}
		finally{
			closeSession(session);
		}
		
	}
    
    
    /*
     * 保存或者更新对象，
     * 参数：
     * 		conf：数据库配置文件相对路径
     * 		o：	  需要保存或者更新的对象实例，为ORM生成类
     */
    public static void Save(String conf,Object o) throws Exception {
    	Session session = null;
		try{
			session =getSession(conf);
			session.saveOrUpdate(o);
		}
		catch(Exception e){
			logger.error("执行SQL异常："+e);
			throw e;
		}
		finally{
			closeSession(session);
		}
	}
    
    
	public static void executeUpdate(Session session,String sql,String msg){
		if(msg != null && !StringUtils.isEmpty(msg)){
			logger.info(msg);
		}
		Transaction tx = null;
		tx = session.beginTransaction();
		logger.info("执行SQL："+sql);
        session.createSQLQuery(sql).executeUpdate();
        tx.commit();
	}
	
	 public static void executeUpdate(String conf,String sql,String msg) throws Exception {
			if(msg != null && !StringUtils.isEmpty(msg)){
				logger.info(msg);
			}
			
			Session session = null;
			try{
				session =getSession(conf);			

				Transaction tx = null;
				tx = session.beginTransaction();
				logger.info("执行SQL："+sql);
		        session.createSQLQuery(sql).executeUpdate();
		        tx.commit();

			}
			catch(Exception e){
				logger.error("执行SQL异常："+e);
				throw e;
			}
			finally{
				closeSession(session);
			}
			
		}
	
}