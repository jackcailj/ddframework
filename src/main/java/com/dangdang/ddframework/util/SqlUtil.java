package com.dangdang.ddframework.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;

public class SqlUtil {
    protected static Logger logger = Logger.getLogger(SqlUtil.class);
	
	public static List<Map<Object, Object>> executeSql(Session session,String sql,String msg) {
		if(msg != null && !StringUtils.isEmpty(msg)){
			logger.info(msg);
		}
		session.clear();	
		List<Map<Object, Object>> result = session
				.createSQLQuery(sql).setCacheMode(CacheMode.IGNORE)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		
		logger.info("执行SQL："+sql.replace("'", ""));
		logger.info("执行结果："+result);
		return result;
	}
	
	public static void executeUpdate(Session session,String sql,String msg){
		if(msg != null && !StringUtils.isEmpty(msg)){
			logger.info(msg);
		}
		Transaction tx = null;
		tx = session.beginTransaction();		
        session.createSQLQuery(sql).executeUpdate();
        logger.info("执行SQL："+sql.replace("'", ""));
        tx.commit();
	}


}
