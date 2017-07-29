package com.sfebiz.supplychain.service.user.yhj;

import com.alibaba.fastjson.JSONObject;
import com.sfebiz.supplychain.util.HttpClientUtil;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.RegUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 易汇金实名认证服务类（一般情况下接口调用一次收费一次，目前以下情况不收费：）
 * 返回结果如下：
 * PASS 认证通过
 * NOPASS 认证未通过
 * NOAUTH 身份信息在公安系统冻结
 * FAIL 认证服务失败，认证服务内部系统失败
 * Created by jianyuanyang on 15/11/23.
 */
public class YhjRealNameAuthentication {

    private final static Logger log  = LoggerFactory.getLogger(YhjRealNameAuthentication.class);

    /**
     * 实名认证url
     */
    private static final String rzUrl = "https://api.ehking.com/rz/auth";

    /**
     * 认证类型
     */
    private static final String rzAuthType = "AUTH_USER";

    /**
     * 认证商户编号
     */
    private static final String rzMerchantId = "120140192";

    /**
     * 认证编码key
     */
    private static final String rzKey = "ee8e1f76b49b489bbe6a09124a7ab2ed";

    /**
     * 认证结果状态key
     */
    public static final String rzResultKey= "status";

    /**
     * 认证通过结果value
     */
    public static final String rzResultPassValue="PASS";
    /**
     * 实名冻结，或者户口迁移，默认也通过
     */
    public static final String rzResultNOAUTHValue="NOAUTH";

    /**
     * 认证服务系统内部错误
     */
    public static final String rzResultInnerErrorValue="FAIL";

    /**
     * 实名认证接口
     * @param idCardNum 身份证号
     * @param name 姓名
     * @param requestId 请求唯一标示 建议uuid
     * @return 认证结果
     */
    public static String rz(String idCardNum,String name,String requestId) throws Exception{
        long startTime = System.currentTimeMillis();
        boolean isIdNum = RegUtil.isIdNum(idCardNum);
        if (!isIdNum || StringUtils.isBlank(name)){
            log.warn("name"+name +",idCardNum:"+idCardNum+", not regular idNum or empty name");
            throw new Exception("name"+name +",idCardNum:"+idCardNum+" not regular idNum or empty name");
        }
        if (StringUtils.isBlank(requestId)){
            log.warn("name"+name +",idCardNum:"+idCardNum+",but requestId is empty");
            throw new Exception("name"+name +",idCardNum:"+idCardNum+",but requestId is empty");
        }
        String hmac= generateDefaultDeclareSign(name,idCardNum, requestId);
        String postJsonStr = buildYhjRequestParam(rzMerchantId, requestId, rzAuthType, name, idCardNum, hmac);
        String rzResultStr= HttpClientUtil.postJson(rzUrl, postJsonStr, true);
        log.info("yihuijin rz postJsonStr:"+postJsonStr+",and resultStr:" + rzResultStr + ",costTime:" + (System.currentTimeMillis() - startTime));
        return rzResultStr;
    }


    /**
     * 构建请求参数
     * @param merchantId 商户编号
     * @param requestId  认证业务流水号
     * @param authType 认证类型
     * @param name 实名认证姓名
     * @param idCardNum 实名认证身份证号
     * @param sign  签名key
     * @return
     */
    private static String buildYhjRequestParam(String merchantId,String requestId,String authType,
                                   String name,String idCardNum,String sign) {
        String postJsonStr = "" ;
        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("merchantId",merchantId);
        paramMap.put("requestId",requestId);
        paramMap.put("authType",authType);
        paramMap.put("name",name);
        paramMap.put("idCardNum",idCardNum);
        paramMap.put("hmac",sign);
        try {
            //postJsonStr = JsonUtil.Map2JsonStr(paramMap);
            JSONObject json = new JSONObject();
            json.putAll( paramMap );
            postJsonStr = json.toJSONString();

        }catch (Exception e){
            log.error("yihuijin rz buildYhjRequestParam,e:"+e);
        }finally {
            log.info("merchantId:"+merchantId+",requestId:"+requestId+",authType:"+authType
                        +",name:"+name+",idCardNum:"+idCardNum+",sign:"+sign
                        +",postJsonStr:"+postJsonStr);
            return postJsonStr;
        }
    }


    /**
     * 默认生成易汇金key
     * @param name
     * @param idCardNum
     * @param actionNo
     * @return
     * @throws Exception
     */
    private static String generateDefaultDeclareSign(String name,String idCardNum,String actionNo){
        return generateDeclareSign(rzMerchantId,actionNo,rzAuthType,name,idCardNum);
    }

    /**
     * 易汇金支付申报签名key
     * @param merchantId 商户编号
     * @param joinPayNo  认证业务流水号
     * @param authType 认证类型
     * @param name 实名认证姓名
     * @param idCardNum 实名认证身份证号
     * @return 签名key
     * @throws Exception
     */
    private static String generateDeclareSign(String merchantId, String joinPayNo, String authType,
                                              String name,String idCardNum){
        StringBuilder hmacSource = new StringBuilder();
        hmacSource.append(StringUtils.defaultString(merchantId))
                .append(StringUtils.defaultString(joinPayNo, ""))
                .append(StringUtils.defaultString(authType, ""));

        hmacSource.append(StringUtils.defaultString(name))
                .append(ObjectUtils.defaultIfNull(idCardNum, ""));
        String signKey = rzKey;
        return signMd5(hmacSource.toString(), signKey);
    }

    /**
     * md5签名
     * @param source
     * @param key
     * @return
     */
    private static String signMd5(String source, String key) {
        byte k_ipad[] = new byte[64];
        byte k_opad[] = new byte[64];
        byte keyb[];
        byte value[];
        try {
            keyb = key.getBytes("UTF-8");
            value = source.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            keyb = key.getBytes();
            value = source.getBytes();
        }

        Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
        Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
        for (int i = 0; i < keyb.length; i++) {
            k_ipad[i] = (byte) (keyb[i] ^ 0x36);
            k_opad[i] = (byte) (keyb[i] ^ 0x5c);
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        md.update(k_ipad);
        md.update(value);
        byte dg[] = md.digest();
        md.reset();
        md.update(k_opad);
        md.update(dg, 0, 16);
        dg = md.digest();
        return Hex.encodeHexString(dg);
    }

    public static void main(String[] args) throws Exception{

        Map<String, String> rzParamsMap = new HashMap<String, String>();
        String requestId=UUID.randomUUID().toString();
        String authType="AUTH_USER";
        String name="李盛";
        String idCardNum="330824198902054516";
        //String hmac= generateDefaultDeclareSign(idCardNum, name, requestId);
        rzParamsMap.put("merchantId",rzMerchantId);
        rzParamsMap.put("requestId",requestId);
        rzParamsMap.put("authType",authType);
        rzParamsMap.put("name",name);
        rzParamsMap.put("idCardNum",idCardNum);
        //rzParamsMap.put("hmac",hmac);
        String postJsonStr = JSONUtil.toJson(rzParamsMap);
        rz(idCardNum,name,UUID.randomUUID().toString());
        System.out.println("postjsonStr:"+postJsonStr);
    }
}
