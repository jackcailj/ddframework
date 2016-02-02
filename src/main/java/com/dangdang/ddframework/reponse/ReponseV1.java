package com.dangdang.ddframework.reponse;

import java.util.Date;

/*
 * 
{ "statusCode":1, "errorCode":200,"systemDate":"1365326315112" "ebookList":...}
只包括公共的，其中数据部分由于名字不固定，导致不能进行解析，需要继承此类，自定义数据部分
 */
public class ReponseV1 {
	private int statusCode;
	private int errorCode;
	private Date systemDate;
}
