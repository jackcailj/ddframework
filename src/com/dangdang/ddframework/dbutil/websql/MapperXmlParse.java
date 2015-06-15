package com.dangdang.ddframework.dbutil.websql;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import com.mysql.jdbc.Buffer;
import com.mysql.jdbc.Field;

public class MapperXmlParse {
	
	//映射文件缓存，保证每个映射文件只加载解析一次
	protected static Map<String,Map<String, FiledInfo>> bufferMap = new HashMap<String, Map<String, FiledInfo>>();
	protected static Map<String, String> tablebuffer =new HashMap<String, String>();
	
	public static String getTableName(Class classz) throws DocumentException, SAXException {
		String result =tablebuffer.get(classz.getSimpleName());
		if(result==null){
			ParseXml(classz);
		}
		result =tablebuffer.get(classz.getSimpleName());
		return result;
	}
	
	public static String getOrmFiledName(Class classz,String sqlColumnName) throws DocumentException, SAXException {
		String result ="";
		if(bufferMap.get(classz.getName())==null){
			ParseXml(classz);
		}
		result =bufferMap.get(classz.getName()).get(sqlColumnName).getAliasName();
		return result;
	}
	
	public static Map<String,FiledInfo> ParseXml(Class classz) throws DocumentException, SAXException {
		String mapFilePath=classz.getName().replace(".", "/")+".hbm.xml";
		InputStream stream =ClassLoader.getSystemClassLoader().getResourceAsStream(mapFilePath);
		if(stream ==null ){
			//没有找到映射文件
			return null;
		}
			
		
		Map<String,FiledInfo> fileds=new HashMap<String,FiledInfo>();
		//读取缓存，加载过不再加载
		if(bufferMap.containsKey(classz.getName())){
			return bufferMap.get(classz.getName());
		}
		else {
			bufferMap.put(classz.getName(), fileds);
		}
		
		//读取hibernate配置文件，并解析字段映射
		SAXReader saxReader = new SAXReader(false);
		saxReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		
		Document document = saxReader.read(stream);
		Element root=document.getRootElement();
		Element classElement=root.element("class");
		List<Element> idsElements= classElement.elements("id");
		
		//记录table映射
		tablebuffer.put(classz.getSimpleName(),classElement.attributeValue("table"));
		
		
		for(Element element:idsElements){
			FiledInfo filedInfo=parseElement(element);
			fileds.put(filedInfo.getColumnName(),filedInfo);
		}
		List<Element> propertyElements= classElement.elements("property");
		for(Element element:propertyElements){
			FiledInfo filedInfo=parseElement(element);
			fileds.put(filedInfo.getColumnName(),filedInfo);
		}
		
		return fileds;
	}
	
	protected static FiledInfo parseElement(Element fieldElement)
	{
		FiledInfo filedInfo =new FiledInfo();
		filedInfo.setAliasName(fieldElement.attributeValue("name"));
		filedInfo.setTypeName(fieldElement.attributeValue("type"));
			
		Element columnElement=fieldElement.element("column");
		filedInfo.setColumnName(columnElement.attributeValue("name"));
		return filedInfo;
	}
	
}
