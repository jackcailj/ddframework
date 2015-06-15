package com.dangdang.ddframework.dataverify;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hibernate.internal.util.ConfigHelper;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframework.dbutil.DbUtil;
import com.dangdang.ddframework.dbutil.websql.MapperXmlParse;
import com.dangdang.ddframework.util.Compare;


/*
 * 用于处理以下情况：
	1、不能唯一查出记录，通过记录变化及变化内容是否匹配处理
 */
public class RecordExVerify extends RecordVerify{
	

	protected String __idFiledString;
	protected String __filterSqlString;
	protected Object __excpectObject;
	protected List<Object> expectObjects;
	protected List __subTableObjectsList = new ArrayList();
	protected String  __mainTableID;
	
	protected Long maxID;
	protected int expectedAddCount =1;
	
	
	/*
	 * 参数：
	 * 		dbConf：数据库配置文件相对路径
	 * 		expectObject：希望数据，为ORM映射类实例
	 * 		idField：数据库表ID字段名称
	 * 		filterSql：过滤HSQL语句
	 */
	public RecordExVerify(String dbConf, Object expectObject,String idField, String filterSql) throws Exception {
		super(dbConf, expectObject);
		// TODO Auto-generated constructor stub
		__idFiledString=idField;
		__filterSqlString =filterSql;
		__excpectObject = expectObject;
		
		getMaxID();
	}
	
	/*
	 * 此构造参数适合于主子表均只有一条数据的情况并且子表和主表的关联id与预表id字段完全一致
	 * 参数：
	 * 		dbConf：数据库配置文件相对路径
	 * 		expectObject：希望数据，为ORM映射类实例
	 * 		idField：数据库表ID字段名称
	 * 		filterSql：过滤HSQL语句
	 * 		subTableExpectObjct:子表期望数据
	 */
	public RecordExVerify(String dbConf, Object expectObject,String idField,String filterSql,Object subTableExpectObjct) throws Exception {
		super(dbConf, expectObject);
		// TODO Auto-generated constructor stub
		__idFiledString=idField;
		__filterSqlString =filterSql;
		__excpectObject = expectObject;
		__subTableObjectsList.add(subTableExpectObjct);
		__mainTableID = idField;
		
		getMaxID();			
	}
	
	protected void getMaxID() throws Exception
	{
		//获取当前记录数和最大ID;
		//recordCount = DbUtil.executeHSql(dbConf, "select count(*) " + filterSql);
		List<Map<String,Object>> result = DbUtil.selectList(__dbConf, "select max("+__idFiledString+") as maxID " + __filterSqlString);
		if(result.size()==1 && result.get(0).get("maxID")==null){
		    maxID = (long) 0;
		}
		else
		{
			maxID =Long.parseLong(((Map<String,Object>)result.get(0)).get("maxID").toString());
		}
		
	}
	
	/*
	 * 此构造参数适合于主子表均只有一条数据的情况
	 * 参数：
	 * 		dbConf：数据库配置文件相对路径
	 * 		expectObject：希望数据，为ORM映射类实例
	 * 		idField：数据库表ID字段名称
	 * 		filterSql：过滤HSQL语句
	 * 		subTableExpectObjct:子表期望数据
	 * 		mainTableID:子表中对应主表的ID
	 */
	public RecordExVerify(String dbConf, Object expectObject,String idField,String filterSql,Object subTableExpectObjct,String mainTableID) throws Exception {
		super(dbConf, expectObject);
		// TODO Auto-generated constructor stub
		__idFiledString=idField;
		__filterSqlString =filterSql;
		__excpectObject = expectObject;
		__subTableObjectsList.add(subTableExpectObjct);
		__mainTableID = mainTableID;
		
		getMaxID();
	}
	

	/*
	 * 此构造参数适合于1对多情况
	 * 参数：
	 * 		dbConf：数据库配置文件相对路径
	 * 		expectObject：希望数据，为ORM映射类实例
	 * 		idField：数据库表ID字段名称
	 * 		filterSql：过滤HSQL语句
	 * 		subTableExpectObjct:子表期望数据
	 * 		mainTableID:子表中对应主表的ID
	 */
	public RecordExVerify(String dbConf, Object expectObject,String idField,String filterSql,List subTableExpectObjctList,String mainTableID) throws Exception {
		super(dbConf, expectObject);
		// TODO Auto-generated constructor stub
		__idFiledString=idField;
		__filterSqlString =filterSql;
		__excpectObject = expectObject;
		__subTableObjectsList = subTableExpectObjctList;
		__mainTableID = mainTableID;
		
		getMaxID();
	}
	
	
	
	@Override
	public VerifyResult dataVerify() {
		// TODO Auto-generated method stub
		try{
			
			//获取大于maxId的记录，
			String filterSqlString = "select * ";
			if(__filterSqlString.toLowerCase().contains("where")){
				filterSqlString +=__filterSqlString+" and "+__idFiledString+ " > "+maxID;
			}
			else {
				filterSqlString+=__filterSqlString+" where "+__idFiledString+ " > "+maxID;
			}
			List list= DbUtil.selectList(__dbConf, filterSqlString,__excpectObject.getClass());
			
			if(list.size() == 0){
				//等于0代表数据库没有新增记录
				errorInfo="主表返回的记录数为0，数据库中没有新增记录";
				verifyResult = VerifyResult.FAILED;
				//return verifyResult;
			}
			else if(list.size()==expectedAddCount){
				if(expectResult == VerifyResult.FAILED){
					errorInfo="主表返回的记录数为【"+list.size()+"】不等于期望的记录数【"+expectedAddCount+"】";
					throw new Exception("主表返回的记录数不等于期望的记录数");
				}
			}
			else if(list.size()!=expectedAddCount){
				//返回的记录数不等于expectedAddCount，增加的记录数不正确
				errorInfo="返回的记录数为【"+list.size()+"】不等于期望的记录数【"+expectedAddCount+"】";
				//verifyResult = VerifyResult.FAILED;
				//return verifyResult;
				throw new Exception("主表返回的记录数不等于期望的记录数");
			}
			
			//获取子表数据
			List subObject = null;
			if(__subTableObjectsList.size() != 0){
				Field field = list.get(0).getClass().getDeclaredField(MapperXmlParse.getOrmFiledName(__excpectObject.getClass(),__mainTableID));
				field.setAccessible(true);
				subObject = DbUtil.selectList(__dbConf, "select * from "+MapperXmlParse.getTableName(__subTableObjectsList.get(0).getClass())+ " where "+__mainTableID +"='"+field.get(list.get(0))+"'",__subTableObjectsList.get(0).getClass());
				if(subObject.size()==0){
					errorInfo="返回的子表记录数为0，数据库中没有新增记录";
					verifyResult = VerifyResult.FAILED;
				}
				else if(subObject.size()==__subTableObjectsList.size()){
					if(expectResult==VerifyResult.FAILED){
						errorInfo="验证期望失败，子表不应返回数据，但返回的数据与期望的数据个数一致";
						throw new Exception(errorInfo);
					}
				}
				else if(subObject.size()>__subTableObjectsList.size()){
					errorInfo="返回的子表记录数["+subObject.size()+"]与期望["+__subTableObjectsList.size()+"]不符";
					throw new Exception(errorInfo);
				}
			}
			
			//返回数据正确，与期望的数据对比
			if(list.size()==expectedAddCount
			  &&Compare.equals((Map<String, Object>)JSONObject.toJSON(list.get(0)), (Map<String, Object>)JSONObject.toJSON(__excpectObject))
			  && (subObject==null?true:Compare.equals((List)JSONObject.toJSON(subObject), (List)JSONObject.toJSON(__subTableObjectsList)))){
				
			}
			else {
				if(list.size()>0){
					errorInfo="与期望数据不符，返回数据:"+JSONObject.toJSONString(list.get(0))+"\n"+"期望数据："+JSONObject.toJSONString(__excpectObject)+"\n";
				}
				
				if(subObject!=null){
					errorInfo+="子表返回数据："+JSONObject.toJSONString(subObject)+"\n"+"子表期望数据："+JSONObject.toJSONString(__subTableObjectsList)+"\n";
				}
				
				verifyResult = VerifyResult.FAILED;

				}
		}catch(Exception e)
		{
			errorInfo="RecordExVerify 异常："+e;
			verifyResult = VerifyResult.Exception;
		}
		return verifyResult;
	}
}
