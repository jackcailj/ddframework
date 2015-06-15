package com.dangdang.ddframework.dataverify;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframework.util.Compare;

/*
 * 验证两个map是否相等
 */
public class MapVerify extends VerifyBase{
	
	Map<String,Object> __map1; 
	Map<String,Object> __map2;
	
	public MapVerify(Map<String,Object> map1, Map<String,Object> map2) {
		// TODO Auto-generated constructor stub
		__map1 = map1;
		__map2 = map2;
	}
	
	public MapVerify(Object dbMapObject1,Object dbMapObject2) {
		// TODO Auto-generated constructor stub
		__map1 = (Map<String,Object>)JSONObject.toJSON(dbMapObject1);
		__map2 = (Map<String,Object>)JSONObject.toJSON(dbMapObject2);
	}
	
	@Override
	public VerifyResult dataVerify() throws Exception {
		// TODO Auto-generated method stub
		try{
			boolean result = Compare.equals(__map1, __map2);
			if(!result){
				verifyResult = VerifyResult.FAILED;
			}
		}catch(Exception e){
			errorInfo = "ListVerify异常："+e;
			verifyResult=VerifyResult.Exception;
		}
		
		return verifyResult;
		
	}

}
