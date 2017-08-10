package com.sfebiz.supplychain.protocol.zto;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.net.util.Base64;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * Description:
 * Created by yanghua on 2017/4/25.
 */
public class ZTOCodeUtil {
    public static String encodeForUTF8(String str) {
        String target;
        try {
            target = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return target;
    }

    // 将字符串 UTF-8 解码
    public static String decodeForUTF8(String str) {
        String target = null;
        try {
            target = URLDecoder.decode(str, "UTF-8");
        } catch (Exception e) {
            e.getMessage();
        }
        return target;
    }

    public static String encodeForBase64(String str) {
        return Base64.encodeBase64String(str.getBytes());
    }

    //将字符串 Base64 编码
    public static String encodeForBase64(byte[] str) {
        return Base64.encodeBase64String(str);
    }

    // 将字符串 Base64 解码
    public static String decodeForBase64(String str) {
        return new String(Base64.decodeBase64(str.getBytes()));
    }

    // 将字符串 MD5 加密
    public static String encryptForMD5(String str) {
        return DigestUtils.md5Hex(str);
    }

    // 创建随机数
    public static String createRandomNumber(int count) {
        return RandomStringUtils.randomNumeric(count);
    }

    // 获取UUID（32位）
    public static String createUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}