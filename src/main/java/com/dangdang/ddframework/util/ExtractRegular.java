package com.dangdang.ddframework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 提取正则表达式
 * @author guohaiying
 */
public class ExtractRegular {	
	public static List<String> get(String str, String regular){
		Pattern p = Pattern.compile(regular);
		Matcher m = p.matcher(str);
		List list = new ArrayList<String>();
		while(m.find()){
			list.add(m.group(1));
		}
		return list;
	}

	public static void main(String[] args){
		String str="\"banner\": [{\"coverPic\": \"http://img61.ddimg.cn/ddreader/yuanchuang/kanian710-240.jpg\",\"title\": \"开年读书忙\",\"desc\":\"开学or开工，都来充个电先，1元起，GO！\",\"specialId\": \"670\"}]";
		String regular= "specialId\": \"(\\d+)";
		List<String> aa = ExtractRegular.get(str, regular);
		System.out.println(aa.size());
		System.out.println(aa.get(0));
		
		
	}
	
}
