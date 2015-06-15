package com.dangdang.ddframework.dataverify;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframework.util.Compare;

/*
 * 列表对比，比较顺序，比较列表每一项值是否相等
 */
public class ListVerify extends VerifyBase {

	List __list1;
	List __list2;
	
	/*
	 * 参数：
	 * 	isObject：如果list中全是orm映射类，设置为true，需要转换成map再进行比较
	 */
	public ListVerify(List list1, List list2,boolean isObject) {
		// TODO Auto-generated constructor stub
		__list1 = list1;
		__list2 = list2;
		if(isObject){
			convertToMap(__list1);
			convertToMap(__list2);
		}
	}
	
	/*
	 * 将列表中Object转换成Map，方便比对
	 */
	protected void convertToMap(List list) {
		 Iterator<Object> it = list.iterator();  
		 int index = 0;
	     while(it.hasNext()){  
	    	 Object object = it.next();
	    	 Map<String, Object> map = (Map<String, Object>)JSONObject.toJSON(object);
	    	 list.set(index++, map);
	     }
	}
	
	@Override
	public VerifyResult dataVerify() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// TODO Auto-generated method stub
		try{
			boolean result= Compare.equalsAndSort(__list1, __list2);
			if(result==false){
				verifyResult=VerifyResult.FAILED;
			}
		}catch(Exception e){
			errorInfo = "ListVerify异常："+e;
			verifyResult=VerifyResult.Exception;
		}
		return verifyResult;
	}

	

}
