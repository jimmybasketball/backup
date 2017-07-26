package com.sfebiz.supplychain.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 类说明：<br>
 * MD5加密类
 * 
 * <p>
 * 详细描述：<br>
 * 
 * 
 * </p>
 * 
 * @author 顺银收单开发组
 * 
 * CreateDate: 2013-7-22
 */
public final class MD5Util {

    private MD5Util() {
    }

    /**
     * Returns a MessageDigest for the given <code>algorithm</code>.
     */

    static MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5算法不存在", e);
        }
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 element
     * <code>byte[]</code>.
     * 
     * @param data
     *            Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(byte[] data) {
        return getDigest().digest(data);
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 element
     * <code>byte[]</code>.
     * 
     * @param data
     *            Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(String data) {
        try {
			return md5(data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return md5(data.getBytes());
		}
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex
     * string.
     * 
     * @param data
     *            Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(byte[] data) {
        return HexUtil.toHexString(md5(data));
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex
     * string.
     * 
     * @param data
     *            Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(String data) {
        return HexUtil.toHexString(md5(data));
    }
    
	public static String md5EncodeToBase64(String data) {
		return Base64.encodeBase64String(md5(data));
	}

	public static boolean checkSign(String data, String dataDigest) {
		String result = "";
		result = md5EncodeToBase64(data);
		if (result.equals(dataDigest)) {
			return true;
		}

		return false;
	}

    public static String MD5In32(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
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
            result = buf.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * MD5 小写
     * @param sourceStr
     * @return
     */
    public static String MD5In32ForLowerCase(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
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
            result = buf.toString().toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }




    public static void main(String[] args) {
        //String content = "<?xml version=\"1.0\" encoding=\"utf-8\"?><logisticsEventsRequest></logisticsEventsRequest>";


        //String content = "<logisticsEventsRequest><logisticsEvent><eventHeader><eventType>WMS_GOODS_WEIGHT</eventType><eventTime>2015-11-02 14:45:54</eventTime><eventSource>EFNZPW01</eventSource><eventTarget>CP_PARTNERFLAT</eventTarget></eventHeader><eventBody><tradeDetail><tradeOrders><tradeOrder><tradeOrderId>1823551</tradeOrderId></tradeOrder></tradeOrders></tradeDetail><logisticsDetail><logisticsOrders><logisticsOrder><occurTime>2015-11-02 14:45:54</occurTime><logisticsRemark>123</logisticsRemark><logisticsWeight>123</logisticsWeight><volume><length>123</length><width>123</width><height>123</height></volume></logisticsOrder></logisticsOrders></logisticsDetail></eventBody></logisticsEvent></logisticsEventsRequest>";
        //String content = "<logisticsEventsRequest><logisticsEvent><eventHeader><eventType>WMS_GOODS_WEIGHT</eventType><eventTime>2015-11-02 14:45:54</eventTime><eventSource>EFNZPW01</eventSource><eventTarget>CP_PARTNERFLAT</eventTarget></eventHeader><eventBody><tradeDetail><tradeOrders><tradeOrder><tradeOrderId>1126449</tradeOrderId></tradeOrder></tradeOrders></tradeDetail><logisticsDetail><logisticsOrders><logisticsOrder><occurTime>2015-11-02 14:45:54</occurTime><logisticsRemark>1</logisticsRemark><logisticsWeight>1</logisticsWeight><volume><length>1</length><width>1</width><height>1</height></volume></logisticsOrder></logisticsOrders></logisticsDetail></eventBody></logisticsEvent></logisticsEventsRequest>";
        //String content = "<logisticsEventsRequest><logisticsEvent><eventHeader><eventType>WMS_GOODS_WEIGHT</eventType><eventTime>2015-11-02 15:17:13</eventTime><eventSource>EFNZPW01</eventSource><eventTarget>CP_PARTNERFLAT</eventTarget></eventHeader><eventBody><tradeDetail><tradeOrders><tradeOrder><tradeOrderId>1126449</tradeOrderId></tradeOrder></tradeOrders></tradeDetail><logisticsDetail><logisticsOrders><logisticsOrder><occurTime>2015-11-02 15:17:13</occurTime><logisticsRemark>1</logisticsRemark><logisticsWeight>1</logisticsWeight><volume><length>1</length><width>1</width><height>1</height></volume></logisticsOrder></logisticsOrders></logisticsDetail></eventBody></logisticsEvent></logisticsEventsRequest>123456";
        //String content = "<logisticsEventsRequest><logisticsEvent><eventHeader><eventType>WMS_GOODS_WEIGHT</eventType><eventTime>2015-11-02 16:17:36</eventTime><eventSource>EFNZPW01</eventSource><eventTarget>CP_PARTNERFLAT</eventTarget></eventHeader><eventBody><tradeDetail><tradeOrders><tradeOrder><tradeOrderId>1126449</tradeOrderId></tradeOrder></tradeOrders></tradeDetail><logisticsDetail><logisticsOrders><logisticsOrder><occurTime>2015-11-02 16:17:36</occurTime><logisticsRemark>1</logisticsRemark><logisticsWeight>1</logisticsWeight><volume><length>1</length><width>1</width><height>1</height></volume></logisticsOrder></logisticsOrders></logisticsDetail></eventBody></logisticsEvent></logisticsEventsRequest>";

        //String content = "<logisticsEventsRequest><logisticsEvent><eventHeader><eventType>WMS_GOODS_WEIGHT</eventType><eventTime>2015-11-02 16:17:36</eventTime><eventSource>EFNZPW01</eventSource><eventTarget>CP_PARTNERFLAT</eventTarget></eventHeader><eventBody><tradeDetail><tradeOrders><tradeOrder><tradeOrderId>1126449</tradeOrderId></tradeOrder></tradeOrders></tradeDetail><logisticsDetail><logisticsOrders><logisticsOrder><occurTime>2015-11-02 16:17:36</occurTime><logisticsRemark>1</logisticsRemark><logisticsWeight>1</logisticsWeight><volume><length>1</length><width>1</width><height>1</height></volume></logisticsOrder></logisticsOrders></logisticsDetail></eventBody></logisticsEvent></logisticsEventsRequest>";
        String content = "<logisticsEventsRequest><logisticsEvent><eventHeader><eventType>WMS_GOODS_WEIGHT</eventType><eventTime>2015-11-02 16:17:36</eventTime><eventSource>EFNZPW01</eventSource><eventTarget>CP_PARTNERFLAT</eventTarget></eventHeader><eventBody><tradeDetail><tradeOrders><tradeOrder><tradeOrderId>1126449</tradeOrderId></tradeOrder></tradeOrders></tradeDetail><logisticsDetail><logisticsOrders><logisticsOrder><occurTime>2015-11-02 16:17:36</occurTime><logisticsRemark>1</logisticsRemark><logisticsWeight>1</logisticsWeight><volume><length>1</length><width>1</width><height>1</height></volume></logisticsOrder></logisticsOrders></logisticsDetail></eventBody></logisticsEvent></logisticsEventsRequest>";
        String pwd = "123456";
        String context = content + pwd;
        String hex = md5EncodeToBase64(context);

        boolean result = checkSign(context, hex);

		String param2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><logisticsEventsRequest><logisticsEvent><eventBody><logisticsDetail><logisticsOrders><logisticsOrder><carrierCode/><logisticsCode>SUCCESS</logisticsCode><logisticsRemark>SUCCESS</logisticsRemark><mailNo>LT00004</mailNo><occurTime>2015-02-02 16:50:05</occurTime><poNo>33256MLT00004</poNo></logisticsOrder></logisticsOrders></logisticsDetail><tradeDetail><tradeOrders><tradeOrder><tradeOrderId>33256</tradeOrderId></tradeOrder></tradeOrders></tradeDetail></eventBody><eventHeader><eventSource>COE-0001</eventSource><eventTarget>CP_PARTNERFLAT</eventTarget><eventTime>2015-02-02 16:50:05</eventTime><eventType>WMS_CHECK_ORDER</eventType></eventHeader></logisticsEvent></logisticsEventsRequest>";
		String param1= "<logisticsEventsRequest><logisticsEvent><eventHeader><eventType>LOGISTICS_TRADE_PAID</eventType><eventTime>2014-10-11 11:16:59</eventTime><eventSource>SF</eventSource><eventTarget>HOYOYO-0001</eventTarget></eventHeader><eventBody><clearanceDetail><carrierCode>SF</carrierCode><mailNo>927497532</mailNo></clearanceDetail><paymentDetail><paid><tradeOrderValue>410000.0</tradeOrderValue><tradeOrderValueUnit>CNF</tradeOrderValueUnit></paid><totalShippingFee>10000.0</totalShippingFee><totalShippingUnit>USC</totalShippingUnit><payableWeight>1000.0</payableWeight></paymentDetail><tradeDetail><tradeOrders><tradeOrder><tradeOrderId>364825925</tradeOrderId><buyer><name>张三</name><phone>15000814267</phone><mobile>15000814267</mobile><email>yinggang.zg@taobao.com</email><province>上海</province><city>上海</city><district>浦东区</district><streetAddress>xx路xx号</streetAddress><zipCode>200000</zipCode><identityNumber>360202200801011111</identityNumber><attachments><attachment1>http://test/1.jpg</attachment1><attachment2>http://test/2.jpg</attachment2></attachments></buyer><tradeRemark>如果没有白色的请发红色</tradeRemark><items><item><itemId>35363234</itemId><itemName>iphone4s32G正品包邮</itemName><itemCategoryName>数码</itemCategoryName><itemUnitPrice>400000.0</itemUnitPrice><itemQuantity>6</itemQuantity><qtyUnit>件</qtyUnit><usedNew>全新</usedNew><brand>品牌</brand><specification>规格</specification><model>型号</model><material>材质</material><used>用途</used><netWeight>1</netWeight><particles>粉末</particles><size>1</size><itemRemark>备注</itemRemark></item><item><itemId>35363236</itemId><itemName>商品2</itemName><itemCategoryName>数码</itemCategoryName><itemUnitPrice>400000.0</itemUnitPrice><itemQuantity>6</itemQuantity><qtyUnit>件</qtyUnit><usedNew>全新</usedNew><brand>品牌</brand><specification>规格</specification></item></items></tradeOrder></tradeOrders></tradeDetail><logisticsDetail><logisticsOrders><logisticsOrder><segmentCode>DOMESTIC</segmentCode><poNo>682343244</poNo><routeId>LAX-CN-AAE</routeId><carrierCode>UPS</carrierCode><mailNo>927497532</mailNo><itemsIncluded>35363234</itemsIncluded><senderDetail><name>张三</name><phone>02188776655</phone><mobile>1345678901</mobile><email>xxxx@seller.com</email><province>浙江</province><city>杭州</city><district>西湖区</district><streetAddress>xx路xx号</streetAddress><zipCode>310000</zipCode></senderDetail><receiverDetail><name>仓库联系人</name><phone>02188776655</phone><mobile>1345678901</mobile><email>jiyun@usa.com</email><province>上海</province><city>上海</city><district>浦东区</district><streetAddress>xx路xx号</streetAddress><zipCode>200000</zipCode></receiverDetail></logisticsOrder><logisticsOrder><segmentCode>DOMESTIC</segmentCode><poNo>682343245</poNo><routeId>LAX-CN-AAE</routeId><carrierCode>UPS</carrierCode><mailNo>927497533</mailNo><itemsIncluded>35363236</itemsIncluded><senderDetail><name>张三</name><phone>02188776655</phone><mobile>1345678901</mobile><email>xxxx@seller.com</email><province>浙江</province><city>杭州</city><district>西湖区</district><streetAddress>xx路xx号</streetAddress><zipCode>310000</zipCode></senderDetail><receiverDetail><name>仓库联系人</name><phone>02188776655</phone><mobile>1345678901</mobile><email>jiyun@usa.com</email><province>上海</province><city>上海</city><district>浦东区</district><streetAddress>xx路xx号</streetAddress><zipCode>200000</zipCode></receiverDetail></logisticsOrder><logisticsOrder><segmentCode>DOMESTIC</segmentCode><senderDetail><name>张三</name><phone>02188776655</phone><mobile>1345678901</mobile><email>xxxx@seller.com</email><province>浙江</province><city>杭州</city><district>西湖区</district><streetAddress>xx路xx号</streetAddress><zipCode>310000</zipCode></senderDetail><receiverDetail><name>仓库联系人</name><phone>02188776655</phone><mobile>1345678901</mobile><email>jiyun@usa.com</email><province>上海</province><city>上海</city><district>浦东区</district><streetAddress>xx路xx号</streetAddress><zipCode>200000</zipCode></receiverDetail><skuDetail><skus><sku><skuCode>098762382</skuCode><skuName>库存商品1</skuName><skuUnitPrice>10000.0</skuUnitPrice><skuPriceCurrency>CNY</skuPriceCurrency><skuQty>5</skuQty><skuRemark>remark</skuRemark></sku></skus></skuDetail></logisticsOrder></logisticsOrders></logisticsDetail></eventBody></logisticsEvent></logisticsEventsRequest>";
		String param = "<logisticsEventsRequest><logisticsEvent><eventHeader><eventType>LOGISTICS_TRADE_PAID</eventType><eventTime>2014-10-11 11:16:59</eventTime><eventSource>SF</eventSource><eventTarget>HOYOYO-0001</eventTarget></eventHeader><eventBody><clearanceDetail><carrierCode>SF</carrierCode><mailNo>927497532</mailNo></clearanceDetail><paymentDetail><paid><tradeOrderValue>410000.0</tradeOrderValue><tradeOrderValueUnit>CNF</tradeOrderValueUnit></paid><totalShippingFee>10000.0</totalShippingFee><totalShippingUnit>USC</totalShippingUnit><payableWeight>1000.0</payableWeight></paymentDetail><tradeDetail><tradeOrders><tradeOrder><tradeOrderId>364825925</tradeOrderId><buyer><name>张三</name><phone>15000814267</phone><mobile>15000814267</mobile><email>yinggang.zg@taobao.com</email><province>上海</province><city>上海</city><district>浦东区</district><streetAddress>xx路xx号</streetAddress><zipCode>200000</zipCode><identityNumber>360202200801011111</identityNumber><attachments><attachment1>http://test/1.jpg</attachment1><attachment2>http://test/2.jpg</attachment2></attachments></buyer><tradeRemark>如果没有白色的请发红色</tradeRemark><items><item><itemId>35363234</itemId><itemName>iphone4s32G正品包邮</itemName><itemCategoryName>数码</itemCategoryName><itemUnitPrice>400000.0</itemUnitPrice><itemQuantity>6</itemQuantity><qtyUnit>件</qtyUnit><usedNew>全新</usedNew><brand>品牌</brand><specification>规格</specification><model>型号</model><material>材质</material><used>用途</used><netWeight>1</netWeight><particles>粉末</particles><size>1</size><itemRemark>备注</itemRemark></item><item><itemId>35363236</itemId><itemName>商品2</itemName><itemCategoryName>数码</itemCategoryName><itemUnitPrice>400000.0</itemUnitPrice><itemQuantity>6</itemQuantity><qtyUnit>件</qtyUnit><usedNew>全新</usedNew><brand>品牌</brand><specification>规格</specification></item></items></tradeOrder></tradeOrders></tradeDetail><logisticsDetail><logisticsOrders><logisticsOrder><segmentCode>DOMESTIC</segmentCode><poNo>682343244</poNo><routeId>LAX-CN-AAE</routeId><carrierCode>UPS</carrierCode><mailNo>927497532</mailNo><itemsIncluded>35363234</itemsIncluded><senderDetail><name>张三</name><phone>02188776655</phone><mobile>1345678901</mobile><email>xxxx@seller.com</email><province>浙江</province><city>杭州</city><district>西湖区</district><streetAddress>xx路xx号</streetAddress><zipCode>310000</zipCode></senderDetail><receiverDetail><name>仓库联系人</name><phone>02188776655</phone><mobile>1345678901</mobile><email>jiyun@usa.com</email><province>上海</province><city>上海</city><district>浦东区</district><streetAddress>xx路xx号</streetAddress><zipCode>200000</zipCode></receiverDetail></logisticsOrder><logisticsOrder><segmentCode>DOMESTIC</segmentCode><poNo>682343245</poNo><routeId>LAX-CN-AAE</routeId><carrierCode>UPS</carrierCode><mailNo>927497533</mailNo><itemsIncluded>35363236</itemsIncluded><senderDetail><name>张三</name><phone>02188776655</phone><mobile>1345678901</mobile><email>xxxx@seller.com</email><province>浙江</province><city>杭州</city><district>西湖区</district><streetAddress>xx路xx号</streetAddress><zipCode>310000</zipCode></senderDetail><receiverDetail><name>仓库联系人</name><phone>02188776655</phone><mobile>1345678901</mobile><email>jiyun@usa.com</email><province>上海</province><city>上海</city><district>浦东区</district><streetAddress>xx路xx号</streetAddress><zipCode>200000</zipCode></receiverDetail></logisticsOrder><logisticsOrder><segmentCode>DOMESTIC</segmentCode><senderDetail><name>张三</name><phone>02188776655</phone><mobile>1345678901</mobile><email>xxxx@seller.com</email><province>浙江</province><city>杭州</city><district>西湖区</district><streetAddress>xx路xx号</streetAddress><zipCode>310000</zipCode></senderDetail><receiverDetail><name>仓库联系人</name><phone>02188776655</phone><mobile>1345678901</mobile><email>jiyun@usa.com</email><province>上海</province><city>上海</city><district>浦东区</district><streetAddress>xx路xx号</streetAddress><zipCode>200000</zipCode></receiverDetail><skuDetail><skus><sku><skuCode>098762382</skuCode><skuName>库存商品1</skuName><skuUnitPrice>10000.0</skuUnitPrice><skuPriceCurrency>CNY</skuPriceCurrency><skuQty>5</skuQty><skuRemark>remark</skuRemark></sku></skus></skuDetail></logisticsOrder></logisticsOrders></logisticsDetail></eventBody></logisticsEvent></logisticsEventsRequest>";
		System.out.println(new String(md5((param2+"123456"))));
		System.out.println(md5EncodeToBase64(param2+"123456"));
		System.out.println(checkSign(param2+"123456","5woj6N8PDDxW5YcRm2Uouw=="));
//		System.out.println(md5Hex(md5("5BeV4aLggGgZxQTfAf5kAA==")));
//		String str = "%3ClogisticsEventsRequest%3E%3ClogisticsEvent%3E%3CeventHeader%3E%3CeventType%3ELOGISTICS_TRADE_PAID%3C%2FeventType%3E%3CeventTime%3E2014-10-08+14%3A58%3A00%3C%2FeventTime%3E%3CeventSource%3ESF%3C%2FeventSource%3E%3CeventTarget%3EBANMA-0001%3C%2FeventTarget%3E%3C%2FeventHeader%3E%3CeventBody%3E%3CclearanceDetail%3E%3CcarrierCode%3ESF%3C%2FcarrierCode%3E%3CmailNo%3E927497532%3C%2FmailNo%3E%3C%2FclearanceDetail%3E%3CpaymentDetail%3E%3Cpaid%3E%3CtradeOrderValue%3E410000.0%3C%2FtradeOrderValue%3E%3CtradeOrderValueUnit%3ECNF%3C%2FtradeOrderValueUnit%3E%3C%2Fpaid%3E%3CtotalShippingFee%3E10000.0%3C%2FtotalShippingFee%3E%3CtotalShippingUnit%3EUSC%3C%2FtotalShippingUnit%3E%3CpayableWeight%3E1000.0%3C%2FpayableWeight%3E%3C%2FpaymentDetail%3E%3CtradeDetail%3E%3CtradeOrders%3E%3CtradeOrder%3E%3CtradeOrderId%3E364825924%3C%2FtradeOrderId%3E%3Cbuyer%3E%3Cname%3E%E5%BC%A0%E4%B8%89%3C%2Fname%3E%3Cphone%3E15000814267%3C%2Fphone%3E%3Cmobile%3E15000814267%3C%2Fmobile%3E%3Cemail%3Eyinggang.zg%40taobao.com%3C%2Femail%3E%3Cprovince%3E%E4%B8%8A%E6%B5%B7%3C%2Fprovince%3E%3Ccity%3E%E4%B8%8A%E6%B5%B7%3C%2Fcity%3E%3Cdistrict%3E%E6%B5%A6%E4%B8%9C%E5%8C%BA%3C%2Fdistrict%3E%3CstreetAddress%3Exx%E8%B7%AFxx%E5%8F%B7%3C%2FstreetAddress%3E%3CzipCode%3E200000%3C%2FzipCode%3E%3CidentityNumber%3E360202200801011111%3C%2FidentityNumber%3E%3Cattachments%3E%3Cattachment1%3Ehttp%3A%2F%2Ftest%2F1.jpg%3C%2Fattachment1%3E%3Cattachment2%3Ehttp%3A%2F%2Ftest%2F2.jpg%3C%2Fattachment2%3E%3C%2Fattachments%3E%3C%2Fbuyer%3E%3CtradeRemark%3E%E5%A6%82%E6%9E%9C%E6%B2%A1%E6%9C%89%E7%99%BD%E8%89%B2%E7%9A%84%E8%AF%B7%E5%8F%91%E7%BA%A2%E8%89%B2%3C%2FtradeRemark%3E%3Citems%3E%3Citem%3E%3CitemId%3E35363234%3C%2FitemId%3E%3CitemName%3Eiphone4s32G%E6%AD%A3%E5%93%81%E5%8C%85%E9%82%AE%3C%2FitemName%3E%3CitemCategoryName%3E%E6%95%B0%E7%A0%81%3C%2FitemCategoryName%3E%3CitemUnitPrice%3E400000.0%3C%2FitemUnitPrice%3E%3CitemQuantity%3E6%3C%2FitemQuantity%3E%3CqtyUnit%3E%E4%BB%B6%3C%2FqtyUnit%3E%3CusedNew%3E%E5%85%A8%E6%96%B0%3C%2FusedNew%3E%3Cbrand%3E%E5%93%81%E7%89%8C%3C%2Fbrand%3E%3Cspecification%3E%E8%A7%84%E6%A0%BC%3C%2Fspecification%3E%3Cmodel%3E%E5%9E%8B%E5%8F%B7%3C%2Fmodel%3E%3Cmaterial%3E%E6%9D%90%E8%B4%A8%3C%2Fmaterial%3E%3Cused%3E%E7%94%A8%E9%80%94%3C%2Fused%3E%3CnetWeight%3E1%3C%2FnetWeight%3E%3Cparticles%3E%E7%B2%89%E6%9C%AB%3C%2Fparticles%3E%3Csize%3E1%3C%2Fsize%3E%3CitemRemark%3E%E5%A4%87%E6%B3%A8%3C%2FitemRemark%3E%3C%2Fitem%3E%3Citem%3E%3CitemId%3E35363236%3C%2FitemId%3E%3CitemName%3E%E5%95%86%E5%93%812%3C%2FitemName%3E%3CitemCategoryName%3E%E6%95%B0%E7%A0%81%3C%2FitemCategoryName%3E%3CitemUnitPrice%3E400000.0%3C%2FitemUnitPrice%3E%3CitemQuantity%3E6%3C%2FitemQuantity%3E%3CqtyUnit%3E%E4%BB%B6%3C%2FqtyUnit%3E%3CusedNew%3E%E5%85%A8%E6%96%B0%3C%2FusedNew%3E%3Cbrand%3E%E5%93%81%E7%89%8C%3C%2Fbrand%3E%3Cspecification%3E%E8%A7%84%E6%A0%BC%3C%2Fspecification%3E%3C%2Fitem%3E%3C%2Fitems%3E%3C%2FtradeOrder%3E%3C%2FtradeOrders%3E%3C%2FtradeDetail%3E%3ClogisticsDetail%3E%3ClogisticsOrders%3E%3ClogisticsOrder%3E%3CsegmentCode%3EDOMESTIC%3C%2FsegmentCode%3E%3CpoNo%3E682343244%3C%2FpoNo%3E%3CrouteId%3ELAX-CN-AAE%3C%2FrouteId%3E%3CcarrierCode%3EUPS%3C%2FcarrierCode%3E%3CmailNo%3E927497532%3C%2FmailNo%3E%3CitemsIncluded%3E35363234%3C%2FitemsIncluded%3E%3CsenderDetail%3E%3Cname%3E%E5%BC%A0%E4%B8%89%3C%2Fname%3E%3Cphone%3E02188776655%3C%2Fphone%3E%3Cmobile%3E1345678901%3C%2Fmobile%3E%3Cemail%3Exxxx%40seller.com%3C%2Femail%3E%3Cprovince%3E%E6%B5%99%E6%B1%9F%3C%2Fprovince%3E%3Ccity%3E%E6%9D%AD%E5%B7%9E%3C%2Fcity%3E%3Cdistrict%3E%E8%A5%BF%E6%B9%96%E5%8C%BA%3C%2Fdistrict%3E%3CstreetAddress%3Exx%E8%B7%AFxx%E5%8F%B7%3C%2FstreetAddress%3E%3CzipCode%3E310000%3C%2FzipCode%3E%3C%2FsenderDetail%3E%3CreceiverDetail%3E%3Cname%3E%E4%BB%93%E5%BA%93%E8%81%94%E7%B3%BB%E4%BA%BA%3C%2Fname%3E%3Cphone%3E02188776655%3C%2Fphone%3E%3Cmobile%3E1345678901%3C%2Fmobile%3E%3Cemail%3Ejiyun%40usa.com%3C%2Femail%3E%3Cprovince%3E%E4%B8%8A%E6%B5%B7%3C%2Fprovince%3E%3Ccity%3E%E4%B8%8A%E6%B5%B7%3C%2Fcity%3E%3Cdistrict%3E%E6%B5%A6%E4%B8%9C%E5%8C%BA%3C%2Fdistrict%3E%3CstreetAddress%3Exx%E8%B7%AFxx%E5%8F%B7%3C%2FstreetAddress%3E%3CzipCode%3E200000%3C%2FzipCode%3E%3C%2FreceiverDetail%3E%3C%2FlogisticsOrder%3E%3ClogisticsOrder%3E%3CsegmentCode%3EDOMESTIC%3C%2FsegmentCode%3E%3CpoNo%3E682343245%3C%2FpoNo%3E%3CrouteId%3ELAX-CN-AAE%3C%2FrouteId%3E%3CcarrierCode%3EUPS%3C%2FcarrierCode%3E%3CmailNo%3E927497533%3C%2FmailNo%3E%3CitemsIncluded%3E35363236%3C%2FitemsIncluded%3E%3CsenderDetail%3E%3Cname%3E%E5%BC%A0%E4%B8%89%3C%2Fname%3E%3Cphone%3E02188776655%3C%2Fphone%3E%3Cmobile%3E1345678901%3C%2Fmobile%3E%3Cemail%3Exxxx%40seller.com%3C%2Femail%3E%3Cprovince%3E%E6%B5%99%E6%B1%9F%3C%2Fprovince%3E%3Ccity%3E%E6%9D%AD%E5%B7%9E%3C%2Fcity%3E%3Cdistrict%3E%E8%A5%BF%E6%B9%96%E5%8C%BA%3C%2Fdistrict%3E%3CstreetAddress%3Exx%E8%B7%AFxx%E5%8F%B7%3C%2FstreetAddress%3E%3CzipCode%3E310000%3C%2FzipCode%3E%3C%2FsenderDetail%3E%3CreceiverDetail%3E%3Cname%3E%E4%BB%93%E5%BA%93%E8%81%94%E7%B3%BB%E4%BA%BA%3C%2Fname%3E%3Cphone%3E02188776655%3C%2Fphone%3E%3Cmobile%3E1345678901%3C%2Fmobile%3E%3Cemail%3Ejiyun%40usa.com%3C%2Femail%3E%3Cprovince%3E%E4%B8%8A%E6%B5%B7%3C%2Fprovince%3E%3Ccity%3E%E4%B8%8A%E6%B5%B7%3C%2Fcity%3E%3Cdistrict%3E%E6%B5%A6%E4%B8%9C%E5%8C%BA%3C%2Fdistrict%3E%3CstreetAddress%3Exx%E8%B7%AFxx%E5%8F%B7%3C%2FstreetAddress%3E%3CzipCode%3E200000%3C%2FzipCode%3E%3C%2FreceiverDetail%3E%3C%2FlogisticsOrder%3E%3C%2FlogisticsOrders%3E%3C%2FlogisticsDetail%3E%3C%2FeventBody%3E%3C%2FlogisticsEvent%3E%3C%2FlogisticsEventsRequest%3E";
//		try {
//			System.out.println(URLDecoder.decode(str, "utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        String pwd1ds = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><logisticsEventsRequest><logisticsEvent><eventHeader><eventType>COE_ETK_ORDER_TRACK</eventType><eventTime>2014-12-12 15:11:01</eventTime><eventMessageId>sdgetsdgsdg345dfg345</eventMessageId><eventSource>sam</eventSource><eventTarget>COE</eventTarget></eventHeader><eventBody><trackingNo>EL009796414HK</trackingNo><customerNo>sam</customerNo><noType>track</noType></eventBody></logisticsEvent></logisticsEventsRequest>b5e3d9769218deb1";
        System.out.println(MD5In32(pwd1ds));
        System.out.println(md5EncodeToBase64(pwd1ds));
    }
}
