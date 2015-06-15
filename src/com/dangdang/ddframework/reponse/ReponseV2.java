package com.dangdang.ddframework.reponse;

import java.util.Date;
import java.util.List;

import javax.persistence.Convert;


/*
 * 接口返回格式为：
{ "status": {"code":1, "msg":""},"systemDate":"1365326315112", "data" : { "ebookList":... } }
	T可以为一个类，也可以为列表或Map
	
	实例：
		T为类
			Reponse<user> object=JSONObject.parseObject(json, new TypeReference<Reponse<user>>(){});
		T为列表：
			Reponse<List<user>> object=JSONObject.parseObject(json, new TypeReference<Reponse<List<user>>>(){});
 */
public class ReponseV2<T> extends ReponseV2Base{
	
	protected T data;
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
}
