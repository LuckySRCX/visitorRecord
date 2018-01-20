/*----------------------------------------------------------------
 *  Copyright (C) 2016山东金视野教育科技股份有限公司
 * 版权所有。 
 *
 * 文件名：EncryptUtil
 * 文件功能描述：加密工具类
 * 包括对称加密DES和AES、非对称RSA、MD5，BASE64
 * 
 * 创建标识：konglm20161026
 *
 * 修改标识：
 * 修改描述：
 *----------------------------------------------------------------*/
package net.jiaobaowang.visitor.utils;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class EncryptUtil {
	private static final String MAC_NAME = "HmacSHA1";    
    private static final String ENCODING = "UTF-8";  

	public static void main(String[] args) throws Exception {
//		System.out.println(hmacSHA1Encrypt("appid=HBuilder&token=Oqy/Ol3qBKg1bzLB0fuI6YmrKt4=&top=-1&utid=1&uuid=867831026005009&vvl1=-1&vvl=1,2,7,58,133,186,71,134,3,203,202,195,207,208,205,206,204,220,72,227","jsy309"));
//		System.out.println(desEncrypt("admin", "jsy11111"));
		System.out.println(desDecrypt("JKG5DkSGuOA=", "jsy11111"));
		System.out.println(getBase64("admin"));
		System.out.println(getFromBase64("YWRtaW4="));
		System.out.println(getMd5("admin"));
//		System.out.println(aesEncrypt("admin", "jsy11111"));
//		System.out.println(aesDecrypt("0Zh3fQRDM93wW8WUv2M/nw==", "jsy11111"));
		//制作非对称密钥
		HashMap<String, Object> map = getKeys();
		// 生成公钥和私钥
		RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
		RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
		// 模
		String modulus = publicKey.getModulus().toString();
		// 公钥指数
		String public_exponent = publicKey.getPublicExponent().toString();
		// 私钥指数
		String private_exponent = privateKey.getPrivateExponent().toString();
		// 明文
		String ming = "123456789";
		// 使用模和指数生成公钥和私钥
		RSAPublicKey pubKey = getPublicKey("B127925DA8A6EA73C92576A1B23D7036A5B93878A784DEB30B7B33B5659D1D5E40A76472D750309E80038414567DF44735A4008A946CFCF6D2AABAB0E4929326A33375CEB61E833DD63225B8F301BAF741EEA99746E84793355338751FFD38EDD1418EC04BAA40AEAE4A50CD770EDB1EDC969998B03A1F89152C8F3C2F0D215F", "010001");
		RSAPrivateKey priKey = getPrivateKey(modulus, private_exponent);
		// 加密后的密文
		String mi = encryptByPublicKey(ming, pubKey);
		System.out.println(mi);
		// 解密后的明文
//		ming = decryptByPrivateKey(mi, priKey);
//		System.out.println(ming);
	}
	
	/**
	 * HMACSHA1 加密（签名用）
	 * @param encryptText
	 * @param encryptKey
	 * @return
	 * @throws Exception
	 */
//	public static String hmacSHA1Encrypt(String encyryptText, String encryptKe) throws Exception
//    {
//        System.out.println(encryptText);
//		byte[] data=encryptKey.getBytes(ENCODING);
//        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
//        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
//        //生成一个指定 Mac 算法 的 Mac 对象
//        Mac mac = Mac.getInstance(MAC_NAME);
//        //用给定密钥初始化 Mac 对象
//        mac.init(secretKey);
//
//        byte[] text = encryptText.getBytes(ENCODING);
//        //完成 Mac 操作
//        return new BASE64Encoder().encode(mac.doFinal(text));
//    }


	/**
	 * DES加密
	 * 
	 * @param inputData
	 * @param inputKey
	 * @return
	 * @throws Exception
	 */
//	public static String desEncrypt(String inputData, String inputKey) throws Exception {
//		byte[] DESkey = inputKey.getBytes();// 设置密钥
//		DESKeySpec keySpec = new DESKeySpec(DESkey);// 设置密钥参数
//		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
//		Key key = keyFactory.generateSecret(keySpec);// 得到密钥对象
//
//		Cipher enCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");// 得到加密对象Cipher
//		enCipher.init(Cipher.ENCRYPT_MODE, key);// 设置工作模式为加密模式，给出密钥和向量
//		byte[] pasByte = enCipher.doFinal(inputData.getBytes("utf-8"));
////		BASE64Encoder base64Encoder = new BASE64Encoder();
////		return base64Encoder.encode(pasByte);
//	}

	/**
	 * DES解密
	 * 
	 * @param inputData
	 * @param inputKey
	 * @return
	 * @throws Exception
	 */
	public static String desDecrypt(String inputData, String inputKey) throws Exception {
//		BASE64Decoder base64Decoder = new BASE64Decoder();
//		byte[] byteInputData = base64Decoder.decodeBuffer(inputData);

		byte[] DESkey = inputKey.getBytes();// 设置密钥
		DESKeySpec keySpec = new DESKeySpec(DESkey);// 设置密钥参数
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
		Key key = keyFactory.generateSecret(keySpec);// 得到密钥对象

		// using DES in ECB mode
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, key);
//		byte decryptedData[] = cipher.doFinal(byteInputData);
//		String value = new String(decryptedData, "UTF-8");
		return "";// decryptedData;
	}

	/**
	 * Base64加密转换
	 * 
	 * @param str
	 * @return
	 */
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
//		try {
//			b = str.getBytes("utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		if (b != null) {
//			s = new BASE64Encoder().encode(b);
//		}
		return s;
	}

	/**
	 * Base64解密转换
	 * 
	 * @param s
	 * @return
	 */
	public static String getFromBase64(String s) {
//		byte[] b = null;
//		String result = null;
//		if (s != null) {
//			BASE64Decoder decoder = new BASE64Decoder();
//			try {
//				b = decoder.decodeBuffer(s);
//				result = new String(b, "utf-8");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		return "";
	}

	/**
	 * MD5加密
	 * 
	 * @param plainText
	 * @return
	 */
	public static String getMd5(String plainText) {
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
			// 32位加密
			return buf.toString();
			// 16位的加密
			// return buf.toString().substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * AES加密
	 * 
	 * @param content
	 *            待加密的内容
	 * @param encryptKey
	 *            加密密钥
	 * @return 加密后的byte[]
	 * @throws Exception
	 */
	public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, new SecureRandom(encryptKey.getBytes()));

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

		return cipher.doFinal(content.getBytes("utf-8"));
	}

	/**
	 * AES加密为base 64 code
	 * 
	 * @param content
	 *            待加密的内容
	 * @param encryptKey
	 *            加密密钥
	 * @return 加密后的base 64 code
	 * @throws Exception
	 */
//	public static String aesEncrypt(String content, String encryptKey) throws Exception {
//		return new BASE64Encoder().encode(aesEncryptToBytes(content, encryptKey));
//	}

	/**
	 * AES解密
	 * 
	 * @param encryptBytes
	 *            待解密的byte[]
	 * @param decryptKey
	 *            解密密钥
	 * @return 解密后的String
	 * @throws Exception
	 */
	public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, new SecureRandom(decryptKey.getBytes()));

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
		byte[] decryptBytes = cipher.doFinal(encryptBytes);

		return new String(decryptBytes);
	}

	/**
	 * 将base 64 code AES解密
	 * 
	 * @param encryptStr
	 *            待解密的base 64 code
	 * @param decryptKey
	 *            解密密钥
	 * @return 解密后的string
	 * @throws Exception
	 */
//	public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
//		return aesDecryptByBytes(new BASE64Decoder().decodeBuffer(encryptStr), decryptKey);
//	}

	/**
	 * 生成公钥和私钥
	 * 
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public static HashMap<String, Object> getKeys() throws NoSuchAlgorithmException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		map.put("public", publicKey);
		map.put("private", privateKey);
		return map;
	}

	/**
	 * 使用模和指数生成RSA公钥
	 * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
	 * /None/NoPadding】
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @return
	 */
	public static RSAPublicKey getPublicKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus,16);
			BigInteger b2 = new BigInteger(exponent,16);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 使用模和指数生成RSA私钥
	 * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
	 * /None/NoPadding】
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @return
	 */
	public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
		try {
			modulus="yMXdLJjwdiM8qTFNYA3u2AwZ+7CV2pUEGmj8Z48GXgJGl9MOTkg10kNJJiTwDXPXYz1VbYAs/lBfE4Ti9wsFxmh53Xj1lUzls+oZZ4S9FkSCz4ivHMVQjM/N266kNrwn0iVRU4ibDIYJ9QupIpUeb7/QfTw1klxHPZ/J4M62q+s=";
			exponent="AQAB";
			BigInteger b1 = new BigInteger(modulus,16);
			BigInteger b2 = new BigInteger(exponent,16);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 公钥加密
	 * 
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String data, RSAPublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		// 模长
		int key_len = publicKey.getModulus().bitLength() / 8;
		// 加密数据长度 <= 模长-11
		String[] datas = splitString(data, key_len - 11);
		String mi = "";
		// 如果明文长度大于模长-11则要分组加密
		for (String s : datas) {
			mi += bcd2Str(cipher.doFinal(s.getBytes()));
		}
		return mi;
	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		// 模长
		int key_len = privateKey.getModulus().bitLength() / 8;
		byte[] bytes = data.getBytes();
		byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
		// 如果密文长度大于模长则要分组解密
		String ming = "";
		byte[][] arrays = splitArray(bcd, key_len);
		for (byte[] arr : arrays) {
			ming += new String(cipher.doFinal(arr));
		}
		return ming;
	}

	/**
	 * ASCII码转BCD码
	 * 
	 */
	public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc_to_bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	public static byte asc_to_bcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
		return bcd;
	}

	/**
	 * BCD转字符串
	 */
	public static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;

		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}

	/**
	 * 拆分字符串
	 */
	public static String[] splitString(String string, int len) {
		int x = string.length() / len;
		int y = string.length() % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		String[] strings = new String[x + z];
		String str = "";
		for (int i = 0; i < x + z; i++) {
			if (i == x + z - 1 && y != 0) {
				str = string.substring(i * len, i * len + y);
			} else {
				str = string.substring(i * len, i * len + len);
			}
			strings[i] = str;
		}
		return strings;
	}

	/**
	 * 拆分数组
	 */
	public static byte[][] splitArray(byte[] data, int len) {
		int x = data.length / len;
		int y = data.length % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		byte[][] arrays = new byte[x + z][];
		byte[] arr;
		for (int i = 0; i < x + z; i++) {
			arr = new byte[len];
			if (i == x + z - 1 && y != 0) {
				System.arraycopy(data, i * len, arr, 0, y);
			} else {
				System.arraycopy(data, i * len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}
}
