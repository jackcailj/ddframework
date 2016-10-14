package com.dangdang.ddframework.util;



import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;  import org.slf4j.LoggerFactory;
import org.apache.ibatis.io.Resources;  
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;  
import org.apache.ibatis.session.SqlSessionFactory;  
import org.apache.ibatis.session.SqlSessionFactoryBuilder;  




@SuppressWarnings("deprecation")
public class MyBatisUtil {
    //private static SessionFactory factory;
    //private static Session session;
    //private static Map<String, Session> sessionManager = new HashMap<String, Session>();
	public static Logger logger = LoggerFactory.getLogger(MyBatisUtil.class);
    private static Map<String, SqlSessionFactory > factoryManager = new HashMap<String, SqlSessionFactory >();

    public static SqlSession getSession() throws IOException{
    	String conf = "conf/mybatis.xml";
    	return __getSession(conf);
        
    } 
    
    public static SqlSession getSession(String propertyFile) throws IOException{
    	return __getSession(propertyFile);
    } 
    
    private static SqlSession __getSession(String conf) throws IOException
    {
    	if(factoryManager.containsKey(conf))
    	{
    		return factoryManager.get(conf).openSession(); 
    	}
    	else {
    		
    		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(conf));
    		SqlSession session = factory.openSession();
    		
        	factoryManager.put(conf, factory);
        	return session;
		}
    }
    public static void closeSession(SqlSession session){
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
     * mybatis执行Sql
     * 参数说明：
     * 			conf：mybatis配置文件路径
     *          sqlConf：mybatis映射文件中sql语句id
     *          param：参数：
     *          msg：消息：
     * */
    public static List<Map<String, Object>> executeSql(String conf,String sqlConf,Object param, String msg) throws Exception {
		if(msg != null && !StringUtils.isEmpty(msg)){
			logger.info(msg);
		}
		
		SqlSession session = null;
		try{
			session =getSession(conf);
			logger.info("执行SQL："+sqlConf);
			List<Map<String, Object>> result = session.selectList(sqlConf,param);	
			
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
     * mybatis执行获取映射对象
     * 参数说明：
     * 			conf：mybatis配置文件路径
     *          sqlConf：mybatis映射文件中sql语句id
     *          param：参数：
     *          msg：消息：
     * */
    public static <T> T getMapper(String conf,Class<T> classtype) throws Exception {
		
		
		SqlSession session = null;
		try{
			session =getSession(conf);
			logger.info("执行SQL：");
			T  result = session.getMapper(classtype);
			
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
    
}