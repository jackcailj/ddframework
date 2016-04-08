package com.dangdang.ddframework.util.security;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    /**
     * 随机密钥长度
     */
    private static final int _KEYTLENGTH = 20;

    private static MD5Utils md5;
    
    private MD5Utils() {
    }
    
    public static MD5Utils getInstance(){
    	if(md5 == null){
    		md5 = new MD5Utils();
    	}
    	return md5;
    }

    /**
     * 生成随机密钥
     * 
     * @param length
     *            密钥长度
     */
    private String getRandomKeyt(int length) throws Exception {
        if (length < 1)
            throw new Exception("密钥长度不能小于 1");
        String _keyt = "";
        for (int i = 0; i < length; i++) {
            _keyt += (char) (33 + (int) (Math.random() * (126 - 33 + 1)));
        }
        return _keyt;
    }

    /**
     * 32位标准 MD5 加密
     * 
     * @param plainText
     *            明文
     * @return 密文<br>
     *         返回 Null 值则出现异常
     */
    public String cell32(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();// 32位的加密

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 32 位 salt 加密
     * 
     * @param plainText
     *            明文
     * @return 索引 0 是密文，索引 1 是二次密钥
     */
    public String[] salt32(String plainText) throws Exception {
        return salt("cell32", plainText);
    }

    /**
     * 16 位标准 MD5 加密
     * 
     * @param plainText
     *            明文
     * @return 密文<br>
     *         返回 Null 值则出现异常
     */
    public String cell16(String plainText) {
        String result = cell32(plainText);
        if (result == null)
            return null;
        return result.toString().substring(8, 24);// 16位的加密
    }

    /**
     * 16 位 salt 加密
     * 
     * @param plainText
     *            明文
     * @return 索引 0 是密文，索引 1 是二次密钥
     */
    public String[] salt16(String plainText) throws Exception {
        return salt("cell16", plainText);
    }

    /**
     * 根据调用的方法名称执行不同的方法
     * 
     * @param saltFunctionName
     *            加密的方法名称
     */
    private String[] salt(String saltFunctionName, String plainText)
            throws Exception {
        String _keyt = getRandomKeyt(_KEYTLENGTH);
        return new String[] {(String) this.getClass().getMethod(saltFunctionName, Class.forName("java.lang.String")).invoke(this, (cell32(plainText) + _keyt)), _keyt };
    }
    
    /**
     * 
     * @param saltFunctionName
     * 加密的方法名
     * @param plainText
     * 明文
     * @param _keyt
     * 提供的密钥
     * @return 加密结果，用来验证密码
     * 
     */
    private String salt(String saltFunctionName, String plainText, String _keyt) throws Exception {
    	
    	 return (String) this.getClass().getMethod(saltFunctionName, Class.forName("java.lang.String")).invoke(this, (cell32(plainText) + _keyt));
    }
    
    /**
     * 
     * @param plainText
     * 明文
     * @param _keyt
     * 提供的密钥
     * @return 加密结果，用来验证密码
     * 
     */
    public String getSalt32WithKey(String plainText, String _keyt) throws Exception {
    	return salt("cell32",plainText, _keyt);
    }
    
    public String hexdigest(String string) {
		String s = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(string.getBytes());
			byte tmp[] = md.digest();
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);
		}
		catch (Exception e) {
		}
		return s;
	}

    
    public static void main(String[] args) throws Exception{
    	
    	String [] _tmp = MD5Utils.getInstance().salt32("dang@admin!@#");
        System.out.println(_tmp[0] + "\t" + _tmp[1]);
    }
}
