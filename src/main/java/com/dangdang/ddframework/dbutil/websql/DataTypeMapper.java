package com.dangdang.ddframework.dbutil.websql;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DataTypeMapper {
	public static Map<String,String> dataTypeMap =new HashMap<String,String>();
	static
	{
		dataTypeMap.put("long", Long.class.getName());
		dataTypeMap.put("int", Integer.class.getName());
		dataTypeMap.put("double", Double.class.getName());
		dataTypeMap.put("big_decimal", BigDecimal.class.getName());
		dataTypeMap.put("bigint", Long.class.getName());
		
		dataTypeMap.put("binary", Byte.class.getName());
		dataTypeMap.put("byte", Byte.class.getName());
		dataTypeMap.put("bit", Boolean.class.getName());
		dataTypeMap.put("clob", String.class.getName());
		
		
		
		dataTypeMap.put("string", String.class.getName());
		dataTypeMap.put("timestamp", Date.class.getName());
		
	}
}
