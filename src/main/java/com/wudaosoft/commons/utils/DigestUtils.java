/**
 *    Copyright 2009-2018 Wudao Software Studio(wudaosoft.com)
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.wudaosoft.commons.utils;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

/**
 * <p>消息摘要类,有md5和sha1 </p>
 * @author Changsoul.Wu
 * @date 2014年3月28日 下午3:53:55
 */
public class DigestUtils {
	
	private static final String MD5_ALGORITHM = "MD5";
	private static final String SHA1_ALGORITHM = "SHA1";
	 
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
 
    /**
     * SHA1编码
     * @param str
     * @return String
     */
    public static String sha1(String str) {
        return encode(SHA1_ALGORITHM, str);
    }
    
    /**
     * MD5编码
     * @param str
     * @return String
     */
    public static String md5(String str) {
    	return encode(MD5_ALGORITHM, str);
    }
    
    public static String md5EncryptAndBase64(String str) {
		return encodeBase64(md5Encrypt(str));
	}
    
	private static byte[] md5Encrypt(String encryptStr) {
		try {
			MessageDigest md5 = MessageDigest.getInstance(MD5_ALGORITHM);
			md5.update(encryptStr.getBytes("utf8"));
			return md5.digest();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static String encodeBase64(byte[] b) {
		return Base64.encodeBase64String(b);
	}
    
    /**
     * @param algorithm
     * @param str
     * @return
     */
    private static String encode(final String algorithm, final String str) {
        if (str == null) 
            return null;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(str.getBytes("UTF-8"));
            return byte2String(messageDigest.digest());
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }
        
        return null;
    }
 
	private static String byte2String(byte[] bytes) {
		int len = bytes.length;
		StringBuilder sb = new StringBuilder(len * 2);
		for (int i = 0; i < len; i++) {
			sb.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
			sb.append(HEX_DIGITS[bytes[i] & 0x0f]);
		}
		return sb.toString();
	}

}
