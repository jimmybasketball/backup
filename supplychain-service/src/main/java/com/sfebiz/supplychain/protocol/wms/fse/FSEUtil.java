package com.sfebiz.supplychain.protocol.wms.fse;

import com.sfebiz.supplychain.util.DateUtil;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/11/8.
 */
public class FSEUtil {

    public static String getSign(String baseString){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuilder sign = new StringBuilder();
        try {
            byte[] bytes = md5.digest(baseString.toString().getBytes("UTF-8"));
            for(int i=0;i<bytes.length;i++){
                String hex = Integer.toHexString(bytes[i] & 0xFF);
                if(hex.length()==1){
                    sign.append("0");
                }
                sign.append(hex);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sign.toString();
    }

    public static boolean checkSign(String baseString, String sign) {
        String result = getSign(baseString);
        if(result.toUpperCase().equals(sign)){
            return true;
        }else{
            return false;
        }
    }

    public static Response send(String version, String ip, String sessionKey, String json, String url, String appkey, String methodName) throws Exception {
        Response response = null;

        String datetime = DateUtil.getCurrentDate("yyyyMMddHHmmss");
        String JSON_OBJ = Base64.encodeBase64String(json.getBytes("UTF-8"));
        String baseString = version + ip + sessionKey + datetime + appkey;
        String sign = getSign(baseString);
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("v", version)
                .add("ip", ip)
                .add("sessionKey", sessionKey)
                .add("datetime", datetime)
                .add("sign", sign.toUpperCase())
                .add("JSON_OBJ", JSON_OBJ)
                .build();
        Request request = new Request.Builder()
                .url(url + methodName)
                .post(formBody)
                .build();
        response = client.newCall(request).execute();

        return response;
    }
}
