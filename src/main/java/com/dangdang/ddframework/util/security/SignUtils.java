package com.dangdang.ddframework.util.security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.codec.digest.DigestUtils;



import org.slf4j.Logger;  import org.slf4j.LoggerFactory;

import javacommon.util.CollectionUtils;
import javacommon.util.ConfigReader;

/**
 * 签名工具类.
 * 
 * @author wangguanhua 2014-7-25
 */
public final class SignUtils {

	public static final String CREATE_KEY_PUBLIC_KEY = "~!@##$%^$#@!~!@#$^&(*&^%";

	public static final String AUTHORIZEDID_KEY_MAP = "authorizedId.key.map";

	public static final String SUBMIT_ORDER_PRIVATEKEY = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAkoSLEwgxJkJijIKoqJ0uDmEu9wwkyDGQAW4cmi38VhV7VJE1263GpY03K9UQn7HpS/nV34H76ZaCqmmjjXWezwIDAQABAkAh4ZqTfRp4gXNukKxVyXskAh8tK9a5oJRvcDoXATlP4nAm9rS3LKjOAt9sK2M0auiU0upN1IKauZwbRWO56CwJAiEAysroa1CeQPsNFDevzLfgdcfLQXBSOBMq/cOYT80Yhh0CIQC49cnFH3OXZDN0tfgJ6w8+G6UpCd7tyyqODyXN/qE02wIgIeE2ZT9pwQdwTvFiwHvz1xmS/pLKWa3yH8U+vA5Sy40CIQCZ35E+t0248xmDzSdPSQjjSKh8ncOi3NTIXn27BE0k5QIgVxeLFZRqxWE+CGY+LHKb9wFU3CD7TGjdqMlBNOB1yZc=";

	private static Logger logger = LoggerFactory.getLogger(SignUtils.class);
	
	private SignUtils() {

	}

	/**
	 * Description: 生成提交订单时返回的key
	 * 
	 * @Version1.0 2014-8-27 下午05:54:29 by 王冠华（wangguanhua@dangdang.com）创建
	 * @param custId
	 * @param orderNO
	 * @param privateKey
	 * @return
	 */
	public static String genSubmitOrderKey(Long custId, String orderNo) {
		String md5Encrypt = encryptSubmitOrderByMd5(custId, orderNo);
		return RsaUtils.encryptByPrivateKey(SUBMIT_ORDER_PRIVATEKEY,
				md5Encrypt.getBytes());
	}

	/**
	 * 生成虚拟支付秘钥
	 * Description: 
	 * @Version1.0 2015-3-17 上午11:35:09 by 王冠华（wangguanhua@dangdang.com）创建
	 * @param custId
	 * @param productIds
	 * @return
	 */
	public static String genVirtualPayingKey(Long custId, String productIds){
		String md5Encrypt = encryptOrderVirtualPayment(custId, productIds);
		return RsaUtils.encryptByPrivateKey(SUBMIT_ORDER_PRIVATEKEY,
				md5Encrypt.getBytes());
	}
	/**
	 * Description: md5加密
	 * 
	 * @Version1.0 2014-8-27 下午06:06:55 by 王冠华（wangguanhua@dangdang.com）创建
	 * @param custId
	 * @param orderNo
	 * @return
	 */
	public static String encryptSubmitOrderByMd5(Long custId, String orderNo) {
		return DigestUtils.md5Hex(custId + CREATE_KEY_PUBLIC_KEY + orderNo);
	}
	
	/**
	 * 
	 * Description: 虚拟币支付订单key的md5加密
	 * @Version1.0 2015年3月16日 下午6:54:11 by 于楠（yunan@dangdang.com）创建
	 * @param custId
	 * @param productIds
	 * @return
	 */
	public static String encryptOrderVirtualPayment(Long custId, String productIds) {
		return DigestUtils.md5Hex(custId + CREATE_KEY_PUBLIC_KEY + productIds);
	}
	

	/**
	 * Description: 生成绑定权限接口签名
	 * @Version1.0 2014-8-28 下午04:58:18 by 王冠华（wangguanhua@dangdang.com）创建
	 * @param source
	 * @param key
	 * @return
	 * @throws IOException 
	 */
	public static String createBindPermissionSign(String source, String key) {
		byte[] orig = crossByte(key, source);
		InputStream is = new ByteArrayInputStream(orig);
		String dest = null;
		try {
			dest = DigestUtils.md5Hex(is);
			orig = switchBit(dest.getBytes());
			dest = Base64Utils.encodeBytes(orig);
		} catch (IOException e) {
			logger.error("生成权限签名失败，source = " + source + "，key = " + key, e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				logger.error("close fail", e);
			}
		}
		return dest;
	}
	
	/**
	 * 生成奖品签名key
	 * @param deviceSerialNo 设备号
	 * @param timestamp 时间戳
	 * @param prize 奖品（券批次号或电子书编号）
	 * @return 签名key
	 */
	public static String generatePrizeSignKey(String deviceSerialNo,
			String timestamp, String prize) {
		String md5Encrypt = encryptPrizeByMd5(deviceSerialNo, timestamp, prize);
		return RsaUtils.encryptByPrivateKey(SUBMIT_ORDER_PRIVATEKEY,
				md5Encrypt.getBytes());
	}
	
	/**
	 * MD5加密
	 * @param deviceSerialNo 设备号
	 * @param timestamp 时间戳
	 * @param prize 奖品信息（券批次号或电子书编号）
	 * @return MD5加密后信息
	 */
	public static String encryptPrizeByMd5(String deviceSerialNo,
			String timestamp, String prize) {
		return DigestUtils.md5Hex(deviceSerialNo + timestamp + CREATE_KEY_PUBLIC_KEY + prize);
	}

	/**
	 * 校验签名有效性
	 * @param source 加密数据信息
	 * @param key MD5加密信息
	 * @param sign 签名信息
	 * @return
	 */
	public static boolean checkPrizePermissionSign(String source, String key,
			String sign) {
		byte[] orig = crossByte(key, source);
		InputStream is = new ByteArrayInputStream(orig);
		String dest = null;
		try {
			dest = DigestUtils.md5Hex(is);
			orig = switchBit(dest.getBytes());
			dest = Base64Utils.encodeBytes(orig);
		} catch (IOException e) {
			logger.error("生成权限签名失败，source = " + source + "，key = " + key, e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				logger.error("close fail", e);
			}
		}
		return sign.equals(dest);
	}
	
	/**
	 * Description: 验证签名
	 * @Version1.0 2014-8-28 下午07:42:55 by 王冠华（wangguanhua@dangdang.com）创建
	 * @param source
	 * @param key
	 * @param sign
	 * @return
	 */
	public static boolean checkBindPermissionSign(String source, String key, String sign){
		String newSign = createBindPermissionSign(source, key);
		return sign.equals(newSign);
	}
	
	/**
	 * Description: 字节交叉，多余的追加后面，key在前面
	 * @Version1.0 2014-8-28 下午04:59:17 by 王冠华（wangguanhua@dangdang.com）创建
	 * @param source
	 * @param key
	 * @return
	 */
	public static byte[] crossByte(String key, String source) {
		byte [] kbs = key.getBytes();
		byte [] sbs = source.getBytes();
		byte [] dest = new byte[sbs.length + kbs.length];
		int minLen = sbs.length > kbs.length ? kbs.length : sbs.length;
		for (int i = 0; i < minLen; i++) {
			dest[i * 2	] = kbs[i];
			dest[i * 2	 + 1] = sbs[i];
		}
		if (sbs.length != kbs.length) {
			if (sbs.length > minLen) {
				for (int i = 0; i < sbs.length - minLen; i++) {
					dest[2 * minLen + i] = sbs[i + minLen];
				}
			} else {
				for (int i = 0; i < kbs.length - minLen; i++) {
					dest[2 * minLen + i] = kbs[i + minLen];
				}
			}
		} 
		return dest;
	}

	/**
	 * Description: 移位运算
	 * 
	 * @Version1.0 2014-8-27 下午06:12:52 by 王冠华（wangguanhua@dangdang.com）创建
	 * @param dest
	 * @return
	 */
	public static byte[] switchBit(byte[] dest) {
		int mod = 0;
		int [] tem = new int[dest.length];
		for (int i = 0; i < dest.length; i++) {
			int temp = dest[i] & 0xFF;
			tem[i] = temp;
			mod += temp;
		}
		mod = mod % 6 + 2; // make mod [2,7]
		for (int i = 0; i < tem.length; i++) {
			int move = tem[i] % mod + 1; // move between[1, 7]
			mod = (mod + tem[i]) % 6 + 2;
			byte mask = (byte) ((tem[i] << move) + (tem[i] >> (8 - move)));
			dest[i] = (byte) (tem[i] ^ mask);
		}
		return dest;
	}

	/**
	 * @Title: createSign
	 * @Description: 请求参数签名,对除sign参数以外的其他参数值进行MD5,加密原串按照参数字母进行排序链接.
	 * @return
	 */
	public static String createSign(String key,
			SortedMap<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		Set<Entry<String, String>> es = parameters.entrySet();
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			String k = entry.getKey();
			String v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		String sign = DigestUtils.md5Hex(sb.toString()).toLowerCase();
		return sign;
	}

	/**
	 * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 * 
	 * @return boolean
	 */
	public static boolean checkSign(String key,
			SortedMap<String, String> parameters, String charset) {
		StringBuffer sb = new StringBuffer();
		Set<Map.Entry<String, String>> es = parameters.entrySet();
		Iterator<Map.Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it
					.next();
			String k = entry.getKey();
			String v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		// 算出摘要
		String newSign = javacommon.util.security.DigestUtils.md5ToHex(sb.toString(), charset)
				.toLowerCase();
		String sourceSign = getSign(parameters).toLowerCase();
		return sourceSign.equals(newSign);
	}

	/**
	 * 获取签名
	 * 
	 * @param parameters
	 * @return
	 */
	public static String getSign(SortedMap<String, String> parameters) {
		String s = (String) parameters.get("sign");
		return (null == s) ? "" : s;
	}

	/**
	 * 根据授权ID生成签名key
	 * 
	 * @param authorizedId
	 *            授权ID
	 * @return
	 */
	public static String createBindKey(String authorizedId) {
		String key = DigestUtils.md5Hex(authorizedId + CREATE_KEY_PUBLIC_KEY);
		return key;
	}

	/**
	 * 通过授权id获取key
	 * 
	 * @param authorizedId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getBindKeyByAuthorizedId(String authorizedId) {
		String idkeys = ConfigReader.get(AUTHORIZEDID_KEY_MAP);
		String[] keyPairs = idkeys.split(",");
		Map<String, String> map = CollectionUtils
				.buildMap(new Object[] { keyPairs });
		if (map != null) {
			return map.get(authorizedId);
		}
		return null;
	}

	public static void main(String[] args) {
		// 生成授权ID及签名KEY
//		 Long authorizedId = System.currentTimeMillis();
//		 System.out.println(authorizedId + ","
//		 + createBindKey(authorizedId + ""));
		
//		String key = encryptSubmitOrderByMd5(22226500L, "test001");
//		System.out.println(key);
//		String ori = genSubmitOrderKey(22226500L, "test001");
//		System.out.println(ori);
//		String source = "1900089263test0012014-08-30 11:46:08";
////		String key = "b7f505b669176d347c7299426b6c01f9";
//		
//		String sign = createBindPermissionSign(source, key);
//		System.out.println(sign);
		
//		String deviceSerialNo = "00:26:e8:85:7b:6f";
//		String timestamp = String.valueOf(System.currentTimeMillis());
//		timestamp = "1421380631050";
//		String productId = "1900089274";
//		String sign = generatePrizeSignKey(deviceSerialNo, timestamp, productId);
//		String key = SignUtils.encryptPrizeByMd5(deviceSerialNo, timestamp, productId);
		for (int i = 0; i < 100; i++) {
			String key = "81789da06998da7dad7a2e6c2dfaec0c";
			String source = "19000892731427885218612";
			System.out.println(createBindPermissionSign(source, key));
		}
//    	sign = "cXFah1DrM6YzrEhWVRdIsqqsF0jrTnHhUBfwpfBLqqM=";
//		System.out.println(checkPrizePermissionSign(source, key, sign));
//		System.out.println(createBindPermissionSign(source, key));
//		System.out.println(RsaUtils.encryptByPrivateKey(SUBMIT_ORDER_PRIVATEKEY,
//				key.getBytes()));
    	
	}
	
}
