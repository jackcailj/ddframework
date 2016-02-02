package com.dangdang.ddframework.util.security;

import java.net.URLEncoder;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.log4j.Logger;


/**
 * RSA算法，实现数据的加密解密.
 * 
 */
public abstract class RsaUtils {
	private static final Logger LOG = Logger.getLogger(RsaUtils.class);
	private static final String ALGORITHM = "RSA";
	private static final int KEY_BIT = 512;

	private static Cipher cipher;

	static {
		try {
			cipher = Cipher.getInstance(ALGORITHM);
		} catch (Exception e) {
			LOG.debug("初始化失败！", e);
			throw new RuntimeException(e);
		}
	}

	private RsaUtils() {
	}
	
	/**
	 * 生成密钥对 .
	 */
	public static Map<RsaKey, String> generateKeyPair() {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator
					.getInstance(ALGORITHM);
			// 密钥位数
			keyPairGen.initialize(KEY_BIT);
			// 密钥对
			KeyPair keyPair = keyPairGen.generateKeyPair();
			// 公钥
			PublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			// 私钥
			PrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			// 得到公钥字符串
			String publicKeyStr = getKeyString(publicKey);
			// 得到私钥字符串
			String privateKeyStr = getKeyString(privateKey);
			// 将生成的密钥对返回
			Map<RsaKey, String> map = new HashMap<RsaKey, String>();
			map.put(RsaKey.publicKey, publicKeyStr);
			map.put(RsaKey.privateKey, privateKeyStr);
			return map;
		} catch (Exception e) {
			LOG.error("生成密钥对失败！", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 得到公钥.
	 * @param key 密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static Key getPublicKey(String key) {
		try {
			byte[] keyBytes = Base64Utils.decode(key);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			Key publicKey = keyFactory.generatePublic(keySpec);
			return publicKey;
		} catch (Exception e) {
			LOG.error("得到公钥失败！", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 得到私钥.
	 * @param key 密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static Key getPrivateKey(String key) {
		try {
			byte[] keyBytes = Base64Utils.decode(key);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			Key privateKey = keyFactory.generatePrivate(keySpec);
			return privateKey;
		} catch (Exception e) {
			LOG.error("得到私钥失败！", e);
			throw new RuntimeException(e);
		}
	}	

	/**
	 * 得到密钥字符串（经过base64编码）.
	 * @return
	 */
	public static String getKeyString(Key key) {
		try {
			byte[] keyBytes = key.getEncoded();
			String keyStr = Base64Utils.encodeBytes(keyBytes);
			return keyStr;
		} catch (Exception e) {
			LOG.error("得到密钥字符串失败！", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 使用私钥对明文进行加密，返回BASE64编码的字符串.
	 * @param publicKey
	 * @param plainText
	 * @return
	 */
	public static String encryptByPrivateKey(String privateKey, byte[] plainContent) {
		return encrypt(getPrivateKey(privateKey), plainContent);
	}
	
	/**
	 * 使用公钥对明文进行加密，返回BASE64编码的字符串.
	 * @param publicKey
	 * @param plainText
	 * @return
	 */
	public static String encryptByPublicKey(String publickey, byte[] plainContent) {
		return encrypt(getPublicKey(publickey), plainContent);
	}
	
	/**
	 * 使用公钥对明文进行加密，返回字节数组.
	 * @param publickey
	 * @param plainContent
	 * @return
	 */
	public static byte[] encryptRetBytesByPublicKey(String publickey, byte[] plainContent) {
		return encryptRetBytes(getPublicKey(publickey), plainContent);
	}
	
	/**
	 * 使用公钥对明文进行加密，返回BASE64编码的字符串.
	 * @param publicKey
	 * @param plainText
	 * @return
	 */
	public static String encrypt(Key key, byte[] plainContent) {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return Base64Utils.encodeBytes(cipher.doFinal(plainContent));
		} catch (Exception e) {
			LOG.error("加密失败！", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用公钥对明文进行加密，返回字节数组.
	 * @param key
	 * @param plainContent
	 * @return
	 */
	public static byte[] encryptRetBytes(Key key, byte[] plainContent) {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(plainContent);
		} catch (Exception e) {
			LOG.error("加密失败！", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 使用私钥对明文密文进行解密.
	 * @param privateKey
	 * @param enStr
	 * @return
	 */
	public static String decryptByPublicKey(String publicKey, String encryptContent) {
		return decrypt(getPublicKey(publicKey), encryptContent);
	}
	
	/**
	 * 使用私钥对明文密文进行解密.
	 * @param privateKey
	 * @param enStr
	 * @return
	 */
	public static String decryptByPrivateKey(String privateKey, String encryptContent) {
		return decrypt(getPrivateKey(privateKey), encryptContent);
	}
	
	/**
	 * 使用私钥对明文密文进行解密.
	 * @param privateKey
	 * @param enStr
	 * @return
	 */
	public static String decrypt(Key key, String encryptContent) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] deBytes = cipher.doFinal(Base64Utils.decode(encryptContent));
			return Base64Utils.encodeBytes(deBytes);
		} catch (Exception e) {
			LOG.error("解密失败！", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @author yangming
	 *
	 */
	enum RsaKey {
		publicKey, privateKey;
	}



	
	public static void main(String args[]) throws Exception {
		
		
//		String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIbLuZtn/KNKBrAH2ag9" +
//				"WdiHR+xfjwqc3Ij7U0hfd0m+KbnJHTewtqYr+2kv5qyGHdvwuPS3ScioYvX5AFf" +
//				"hnS0CAwEAAQ==";
//		String privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAhsu" +
//				"5m2f8o0oGsAfZqD1Z2IdH7F+PCpzciPtTSF93Sb4puckdN7C2piv7aS/mrIYd2/" +
//				"C49LdJyKhi9fkAV+GdLQIDAQABAkBE9LcQjxOonNfq+TkZXbk2kY3zl7bfIPuYf" +
//				"6lNHrkxWX6AKRICyg0SvVKmlnw5GCplhFXeekrhvzfWjz9zGRIBAiEAvAHgWI0h" +
//				"Dh4e/pb2PpqIJEB6fRNw4jiSHsNcRIdx9e0CIQC3i2kkHZpF8kcht+dCZPH1xsg" +
//				"7hcP9Ji1A5pTOu7VcQQIgRIIGhx3+Io/VSX4nAs76twML31HbO4PTswQLNimI3E" +
//				"0CIEFdIW1eKXGdbV2Bq5bGcjCPwQEJixlc/iKtuvLAgD7BAiEAn9S0ByfRIulTl" +
//				"6qnB0CjSoAR5Q24hvn0vQhpXQWr4KI=";

		//汉王
//		String publicKey = "MEgCQQCz/WJPhHiQWFub3d5jPnuXWGuptAXOTe/tO7iScQsIhjMDitD84OJ25DWj2PbAMZhCl6YPgoIyHiKbyZAUiKa9AgMBAAE=";
//		String privateKey = "MIIBOwIBAAJBALP9Yk+EeJBYW5vd3mM+e5dYa6m0Bc5N7+07uJJxCwiGMwOK0Pzg4nbkNaPY9sAxmEKXpg+CgjIeIpvJkBSIpr0CAwEAAQJAARAW+4c5uwh6igIWZRmiWzNvd1IT0rMNHqaCoYK/YO5/ZCh8AvlJrEoeMv6L+TN5cTQsRg4avDpk/oOBpHtnlQIhAO4BKyffQVvOvAYX7WEUuB0Z6FuJkocU96Sb3ZvaaccrAiEAwZlHEqcETrWGdO39d2hd7yJTBuZEIULFGtXqhxZeVbcCIHdGxqX40Px9ouU64JAinhKXhUc6ruBbtbAdHPaagyB5AiEAiSu0Wvfxql8d5I2XsNpG7B/S2imfDj4D8C6vXIWx02MCIQCuJ274ZJ0hP9JWIQJv3g0+P7AX0u52FS4TfHb9SQzdMQ==";
		

		String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALjJrXDWno//xyMZYCmbdSM9gwghdgToCCOoZykp2YoMPNG2hLs1fOAeVGtscPECW2alwZEPzI8lXR/yQyJKvhsCAwEAAQ==";
		
//		String privateKey = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEA8WMMkN" +
//				"chKtXZBy4zWoRuq/DJXmSVaIorFdme0XqKyODUuuFyHLmVI3nJKEvwogBDQ1DMoNpI" +
//				"z/NsRh84Oim5EQIDAQABAkBvWdm6FwNd1zg+b7mQz/adqn255XxmtRYbBkCVMYpED0" +
//				"25JYQxHFcY6129DSGL8SYgY78G4TV1FIs9+5vWcR4BAiEA+c53d4VXedS813J/0lsv" +
//				"bUC+sjtsOrZGZPuw2CP1DHkCIQD3XySQpzsEbwfPsyGNxazY15seKuXsT6ZjD5KI2b" +
//				"y7WQIgY0CkeOmS6wYajeOhY7v1KQx7eCdiBiXb7E2QYyBOQckCIQDRv0GQyctX4hEc" +
//				"goXT4SXDI2WqebNE4PBzFFQqZ30I2QIhAO1KoMi9KnyM5sVMJSqzjH8pcxsjiKqVgB" +
//				"DJ8Tv2X6T2";

		
		
		String en = encryptByPublicKey(publicKey, Base64Utils.decode("FXu+ntF/i8hgyPlbAcodQg=="));		
		
		// String en = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rights version=\"1.0\"><agreement><asset><uid><![CDATA[bb.131813851356091287]]></uid><key><![CDATA[ftUSbqsYnNe9OpkrDpi2GcFNrQBXXJhnCPk2z4m8+NCWZNBFnKipxdUsh0r3QfTzzi4NzDuVOZYr+JGgIg5vzA==]]></key></asset><permission><play/></permission></agreement></rights>";
		System.out.println(generateKeyPair().get(RsaKey.publicKey));
//		
//		String de = decryptByPrivateKey(privateKey, en);
//		System.out.println(de);
	}
}
