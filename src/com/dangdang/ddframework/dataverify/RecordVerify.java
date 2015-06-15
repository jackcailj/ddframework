package com.dangdang.ddframework.dataverify;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.DataSource;

import org.dom4j.DocumentException;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframework.dbutil.DbUtil;
import com.dangdang.ddframework.dbutil.websql.FiledInfo;
import com.dangdang.ddframework.dbutil.websql.MapperXmlParse;

/*
 * 数据库记录验证类,用于处理以下情况：
1、数据记录某个字段变化
2、新增数据记录，通过Orm类可以得到唯一的一条记录
 */

public class RecordVerify extends VerifyBase {
	
	protected Object __dbObject;
	protected String __dbConf;
	
	protected String __sql;
	
	
	
	public RecordVerify(String dbConf,Object dbObject) throws IllegalArgumentException, SecurityException, DocumentException, SAXException, IllegalAccessException, NoSuchFieldException{
		__dbObject = dbObject;
		__dbConf = dbConf;
		__sql=generateSql();
	}
	
	public RecordVerify(String dbConf,String sql) {
		//__dbObject = dbObject;
		__dbConf = dbConf;
		__sql=sql;
	}
	
	protected String generateSql() throws DocumentException, SAXException, IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException{
		Map<String, FiledInfo> filedInfoMap = MapperXmlParse.ParseXml(__dbObject.getClass());
		
		Field[] fields = __dbObject.getClass().getDeclaredFields();
		//拼接查询字符串
		String tableNameString = MapperXmlParse.getTableName(__dbObject.getClass());
		StringBuilder  hSqlString = new StringBuilder();
		hSqlString.append("select * from "+ tableNameString );
		hSqlString.append(" where ");
		
		for(Entry<String, FiledInfo> entry: filedInfoMap.entrySet()){
			FiledInfo filedInfo=entry.getValue();
			Field field=__dbObject.getClass().getDeclaredField(filedInfo.getAliasName());
			field.setAccessible(true);
			if(field.get(__dbObject)!=null){
				hSqlString.append(filedInfo.getColumnName());
				hSqlString.append("=");
				
				if(field.getType().toString().equals(String.class.toString()) ){
					hSqlString.append("'");
					hSqlString.append(field.get(__dbObject));
					hSqlString.append("'");
					
				}
				else if(field.getType().toString().equals(Date.class.toString())){
					hSqlString.append("'");
					hSqlString.append(((Date)field.get(__dbObject)).toLocaleString());
					hSqlString.append("'");
				}
				else {
					hSqlString.append(field.get(__dbObject));
				}
				hSqlString.append(" and ");
			}
		
		}
		
		hSqlString.delete(hSqlString.length() - " and ".length(), hSqlString.length());
		
		return hSqlString.toString();
		
		
	}
	
	
	/*
	 * 根据类型自动生成查询Sql，并验证内容是否正确
	 */
	@Override
	public VerifyResult dataVerify() {
		// TODO Auto-generated method stub
		try{
			List results=DbUtil.selectList( __dbConf,__sql);
			if(results.size() == 0){
				errorInfo="数据库中不存在此记录:"+JSONObject.toJSONString(__dbObject);
				verifyResult=VerifyResult.FAILED;
			}
			else if( results.size()==1){
				if(expectResult==VerifyResult.FAILED){
					errorInfo="用例期望失败，数据库中:"+JSONObject.toJSONString(results);
					throw new Exception(errorInfo);
				}
			}
			else if(results.size()>1){
				errorInfo="数据库中存在多条记录:"+JSONObject.toJSONString(results);
				throw new Exception(errorInfo);
			}
			
		}catch(Exception e){
			//记录日志
			errorInfo = "查询数据["+JSONObject.toJSONString(__dbObject)+"]发生异常："+e;
			verifyResult=VerifyResult.Exception;
		}
		
		return verifyResult;
		
	}

	
}
