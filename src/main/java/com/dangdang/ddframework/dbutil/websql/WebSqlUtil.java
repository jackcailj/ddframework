package com.dangdang.ddframework.dbutil.websql;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;



public class WebSqlUtil {
	private static Map<String, WebSqlConnection> factoryManager = new HashMap<String, WebSqlConnection>();

    public static WebSqlConnection getConnection(){
    	String conf = "conf/websql.properties";
    	return __getConnection(conf);
        
    } 
    
    public static WebSqlConnection getConnection(String conf){
    	return __getConnection(conf);
    } 
    
    private static WebSqlConnection __getConnection(String conf)
    {
    	if(factoryManager.containsKey(conf))
    	{
    		return factoryManager.get(conf); 
    	}
    	else {
    		WebSqlConnection connection = new WebSqlConnection(conf);
    		factoryManager.put(conf, connection);
        	return connection;
		}
    }
}
