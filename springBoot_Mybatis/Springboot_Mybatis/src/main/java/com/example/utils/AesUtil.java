package com.example.utils;


import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * 加密解密工具类
 *
 * @author zhaoyang10
 * @date 2020/12/15
 */
@Slf4j
public class AesUtil {
	
	
	
	
	

    private static final String KEY_AES = "AES";

    private static final int KEY_LENGTH = 16;

    
    
    
    /**
     * 加密
     */
    public static String encrypt(String src, String key) {
        try {
            if (key == null || key.length() != KEY_LENGTH) {
                throw new IllegalArgumentException("密钥长度必须是16位");
            }
            byte[] raw = key.getBytes();
            SecretKeySpec skySpec = new SecretKeySpec(raw, KEY_AES);
            Cipher cipher = Cipher.getInstance(KEY_AES);
            cipher.init(Cipher.ENCRYPT_MODE, skySpec);
            byte[] encrypted = cipher.doFinal(src.getBytes());
            return byte2hex(encrypted);
        } catch (Exception e) {
            log.error("AES加密方法出错", e);
        }
        return null;
    }

    public static String decrypt(String src, String key) {
        try {
            if (key == null || key.length() != KEY_LENGTH) {
                throw new IllegalArgumentException("密钥长度必须是16位");
            }
            byte[] raw = key.getBytes();
            SecretKeySpec skySpec = new SecretKeySpec(raw, KEY_AES);
            Cipher cipher = Cipher.getInstance(KEY_AES);
            cipher.init(Cipher.DECRYPT_MODE, skySpec);
            byte[] encrypted1 = hex2byte(src);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original);
        } catch (Exception e) {
            log.error("AES解密出错!", e);
            
        }
        return null;
    }

    public static byte[] hex2byte(String hex) {
        if (hex == null) {
            return new byte[0];
        }
        int l = hex.length();
        int n = 2;
        if (l % n == 1) {
            return new byte[0];
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / n; i++) {
            b[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }

    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String tmp;
        for (byte value : b) {
            tmp = (Integer.toHexString(value & 0XFF));
            if (tmp.length() == 1) {
                hs.append("0").append(tmp);
            } else {
                hs.append(tmp);
            }
        }
        return hs.toString().toUpperCase();
    }

    
    
    
    
	 public static void main(String[] args) {
	 	
	 	JSONObject jsonObj = new JSONObject();
	 	jsonObj.put("name", "张三");
	 	jsonObj.put("sex", "男");
	 	jsonObj.put("age", 11);
	 	Map<String, String> map = new HashMap<String, String>();
	 	map.put("key1", "val1");
	 	map.put("key2", "val2");
	 	jsonObj.put("map", map);
	 	
	    // String content = "http://s3zz.ad.weibo.com/license/1231xxqrq3rr44q.jpg#13121121231231";
	     String content = JSON.toJSONString(jsonObj);
	     log.info("原内容 = {}", content);
	     String encrypt = AesUtil.encrypt(content, "aWXoyC4UNb1frkey");
	     log.info("加密后 = {}", encrypt);
	     String decrypt = AesUtil.decrypt(encrypt, "aWXoyC4UNb1frkey");
	     log.info("解密后 = {}", decrypt);
	 }

}


