package com.dangdang.ddframework.dbutil;

import java.net.UnknownHostException;

import org.slf4j.Logger;  import org.slf4j.LoggerFactory;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDBUtil {
	
	static Logger logger = LoggerFactory.getLogger(MongoDBUtil.class);
	static DBCollection collection;
	private static final String HOST = "10.255.223.180";  	  
    private static final String dbName = "temp"; 
    private static final int port = 27002; 
    private static final String collectionName = "ddclick_log"; 
	
	public static DBCollection connect() throws UnknownHostException, MongoException{
		 return connect(HOST, port, dbName, collectionName);
	}
	
	public static DBCollection connect(String hostIP, int port, String dbName, String tableName) throws UnknownHostException, MongoException{
		 Mongo mg = new Mongo(hostIP, port);	     
	     logger.info("dbName: " + dbName);
	     DB db = mg.getDB(dbName);
	     collection = getCollection(db, tableName);
	     return collection;	        
	}
	
	public static DBCollection getCollection(DB db, String collectionName) {  
	     logger.info("collectionName: " + collectionName);
        return db.getCollection(collectionName);  
    } 
	
	/**
	 * 查找降序或升序后的第一条数据
	 * 
	 * @param key 字段
	 * @param value 1（升序）, -1（降序）       
	 * 
	 * @return str 一条数据
	 * 
	 * */
	public static String findSortOne(String key, Object value) throws Exception{
		//connect();
		DBCursor sort = collection.find().sort(new BasicDBObject(key,value)).limit(1);
		String str = sort.next().toString();
		return str;
	}
	
	public static String findOne(String actionName){
		DBObject sort = collection.findOne(new BasicDBObject("actionName", "\"+actionName+\""));
		return sort.toString();
	}
	
	public static int getAllCount() throws Exception{
		connect();
		DBCursor cursor = collection.find();
		return cursor.count();
	}
	
//	public List<String> findList(){
//		
//	}


}
