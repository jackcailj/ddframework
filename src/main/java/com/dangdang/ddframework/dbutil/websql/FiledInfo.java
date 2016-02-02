package com.dangdang.ddframework.dbutil.websql;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class FiledInfo {
	protected String columnName;
	protected String typeName;
	protected String aliasName;
	
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		if(DataTypeMapper.dataTypeMap.get(typeName)!=null){
			this.typeName=DataTypeMapper.dataTypeMap.get(typeName);
		}
		else{
			this.typeName = typeName;
		}
	}
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	
	public Date parseDate(String stringValue) throws ParseException {
		String fmtstr="";
		if (stringValue.indexOf(':') > 0) {  
            fmtstr = "yyyy-MM-dd HH:mm:ss";  
        } else {  
            fmtstr = "yyyy-MM-dd";  
        }  
		DateFormat df = new SimpleDateFormat(fmtstr,DateFormatSymbols.getInstance());//参数为你要格式化时间日期的模式
		Date date = df.parse(stringValue);//将字符串按照定义的模式转换为Date对象
		return date;
	}
	
	public Object Convert(String stringValue) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, ParseException {
		if(stringValue==null)
			return null;
		if(stringValue.toLowerCase().equals("null")){
			return null;
		}
		
		if(StringUtils.isBlank(stringValue)){
			return null;
		}
		//日期单独解析，其他数据类型使用反射调用构造函数生成。
		if(typeName.equals(Date.class.getName())){
			return parseDate(stringValue);
		}
		else {
			Constructor<?> constructor= Class.forName(typeName).getConstructor(String.class);
			Object object=constructor.newInstance(stringValue);
			return object;
		}
	}	
}
