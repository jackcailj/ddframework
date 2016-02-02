package com.dangdang.ddframework.util;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;


/**
 * 
 * Description: DES工具类 All Rights Reserved.
 * 
 * @version 1.0 2014年12月26日 下午12:01:51 by 许文轩（xuwenxuan@dangdang.com）创建
 */
public class DesUtils {

	/**
	 * 
	 * Description: 生成DES秘钥
	 * 
	 * @Version1.0 2014年12月26日 上午11:45:50 by 许文轩（xuwenxuan@dangdang.com）创建
	 * @param strKey
	 * @return
	 * @throws Exception
	 */
	public static Key getKey(String strKey) throws Exception {
		try {
			javax.crypto.KeyGenerator _generator = javax.crypto.KeyGenerator.getInstance("DES");
			_generator.init(new SecureRandom(strKey.getBytes()));
			return _generator.generateKey();
		} catch (Exception e) {
			throw new Exception("获取秘钥异常" + e);
		}
	}

	/**
	 * 
	 * Description: DES加密
	 * 
	 * @Version1.0 2014年12月26日 上午11:45:00 by 许文轩（xuwenxuan@dangdang.com）创建
	 * @param byteS
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptBytes(byte[] byteS, Key key) throws Exception {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			throw new Exception("加密异常" + e);
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 
	 * Description: DES解密
	 * 
	 * @Version1.0 2014年12月26日 上午11:45:18 by 许文轩（xuwenxuan@dangdang.com）创建
	 * @param byteD
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBytes(byte[] byteD, Key key) throws Exception {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byteFina = cipher.doFinal(byteD);
		} catch (Exception e) {
			throw new Exception("解密异常： " + e);
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 
	 * Description: 获取String类型key
	 * 
	 * @Version1.0 2014年12月26日 下午1:50:06 by 许文轩（xuwenxuan@dangdang.com）创建
	 * @param key
	 * @return
	 */
	public static String getStringKey(Key key) {
		byte[] keyBytes = key.getEncoded();
		return Base64.encodeBase64String(keyBytes);
	}

	/**
	 * 
	 * Description: 通过秘钥字符串获取秘钥
	 * 
	 * @Version1.0 2014年12月26日 下午2:51:39 by 许文轩（xuwenxuan@dangdang.com）创建
	 * @param keyStr
	 * @return
	 * @throws Exception
	 */
	public static Key getKeyFromString(String keyStr) throws Exception {
		try {
			byte[] keyByte = Base64.decodeBase64(keyStr);
			DESKeySpec desKeySpec = new DESKeySpec(keyByte);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(desKeySpec);
			return key;
		} catch (Exception e) {
			throw new Exception("获取DES秘钥异常", e);
		}
	}

	/**
	 * 
	 * Description: 加密custId
	 * 
	 * @Version1.0 2014年12月26日 下午2:51:56 by 许文轩（xuwenxuan@dangdang.com）创建
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public static String encryptCustId(Long custId) throws Exception {
		String keyStr = "MYabzjizBOY=";
		if (StringUtils.isBlank(keyStr)) {
			throw new Exception("请在配置文件中配置custId转换秘钥");
		}
		Key key = getKeyFromString(keyStr);
		if (custId == null) {
			throw new Exception("custId不能为空");
		}
		byte[] originalBytes = String.valueOf(custId).getBytes("UTF-8");
		byte[] encryptBytes = encryptBytes(originalBytes, key);
		return Base64.encodeBase64String(encryptBytes);
		//return Base64Utils.encodeBytes(encryptBytes);
	}

	/**
	 * 
	 * Description: 解密custId
	 * 
	 * @Version1.0 2014年12月26日 下午2:52:06 by 许文轩（xuwenxuan@dangdang.com）创建
	 * @param encryptCustId
	 * @return
	 * @throws Exception
	 */
	public static Long decryptCustId(String encryptCustId) throws Exception {
		String keyStr = "MYabzjizBOY=";
		if (StringUtils.isBlank(keyStr)) {
			throw new Exception("请在配置文件中配置custId转换秘钥");
		}
		Key key = getKeyFromString(keyStr);
		byte[] encryptBytes = Base64.decodeBase64(encryptCustId);
		byte[] originalBytes = decryptBytes(encryptBytes, key);
		return Long.valueOf(Long.valueOf(new String(originalBytes)));
	}
	
	
	/**
	 * Description: 红包id加密
	 * @Version1.0 2015年1月7日 下午4:23:33 by 周伟洋（zhouweiyang@dangdang.com）创建
	 * @param redPacketId
	 * @return
	 * @throws Exception
	 */
	public static String encryptRedPacketId(Long redPacketId) throws Exception {
		//String keyStr = ConfigPropertieUtils.getString("cust.id.key");
		String keyStr = "MYabzjizBOY=";
		if (StringUtils.isBlank(keyStr)) {
			throw new Exception("请在配置文件中配置custId转换秘钥[红包id加密]");
		}
		Key key = getKeyFromString(keyStr);
		if (redPacketId == null) {
			throw new Exception("红包Id不能为空");
		}
		byte[] originalBytes = String.valueOf(redPacketId).getBytes("UTF-8");
		byte[] encryptBytes = encryptBytes(originalBytes, key);
		return Base64.encodeBase64String(encryptBytes);
		//return Base64Utils.encodeBytes(encryptBytes);
	}

	/**
	 * Description: 红包id解密
	 * @Version1.0 2015年1月7日 下午4:23:42 by 周伟洋（zhouweiyang@dangdang.com）创建
	 * @param encryptRedPacketId
	 * @return
	 * @throws Exception
	 */
	public static Long decryptRedPacketId(String encryptRedPacketId) throws Exception {
		//String keyStr = ConfigPropertieUtils.getString("cust.id.key");
		String keyStr = "MYabzjizBOY=";
		if (StringUtils.isBlank(keyStr)) {
			throw new Exception("请在配置文件中配置custId转换秘钥[红包id解密调用]");
		}
		Key key = getKeyFromString(keyStr);
		byte[] encryptBytes = Base64.decodeBase64(encryptRedPacketId);
		byte[] originalBytes = decryptBytes(encryptBytes, key);
		return Long.valueOf(Long.valueOf(new String(originalBytes)));
	}
	
	public static void main(String[] args) throws Exception {
//		System.out.println(decryptRedPacketId("WArjn982uI8%3D"));
//		String name=java.net.URLEncoder.encode("vN0tWaH0+jw=", "UTF-8");
//		System.out.println(name);
		System.out.println(encryptRedPacketId(179L));
	}
	
}
