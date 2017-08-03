package com.sfebiz.supplychain.protocol.zto;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

/**
 * Created by zhangdi on 2015/10/10.
 */
public class ZTODigestUtil {

    public static final String UTF8="UTF-8";
    public final static char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    /**
     * base64
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptBase64(String data) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(data.getBytes(UTF8)).trim();
    }

    /**
     * MD5
     * @param data
     * @return
     * @throws Exception
     */
    private static String encryptMD5(String data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data.getBytes(UTF8));
        byte[] b = md5.digest();
        // 把密文转换成十六进制的字符串形式
        int j = b.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = b[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str).toLowerCase();
    }

    /**
     * 生成签名信息
     * @param partner 合作接入码
     * @param dataTime 接口请求时间
     * @param dataJsonOnBase64 请求数据
     * @param pass 签名秘钥
     * @return 摘要
     * @throws Exception
     */
    public static String digest(String partner,String dataTime,String dataJsonOnBase64,String pass) throws Exception {
        return encryptMD5((partner+dataTime+dataJsonOnBase64+pass));
    }
}
