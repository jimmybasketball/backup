package com.sfebiz.supplychain.protocol.zto.internation;

import com.sfebiz.supplychain.protocol.zto.ZTODigestUtil;
import com.sfebiz.supplychain.protocol.zto.ZTOHttpUtil;
import com.sfebiz.supplychain.protocol.zto.common.ZTOReceiver;
import com.sfebiz.supplychain.protocol.zto.common.ZTOSender;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.QRUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangdi on 2015/11/16.
 * 中通订单内容类，类名对应中通订单下单接口
 */
public class ZTOInternationSubmit {

    /**
     * 订单id
     */
    private String id;

    /**
     * 发件人
     */
    private ZTOSender sender;

    /**
     * 收件人
     */
    private ZTOReceiver receiver;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZTOSender getSender() {
        return sender;
    }

    public void setSender(ZTOSender sender) {
        this.sender = sender;
    }

    public ZTOReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(ZTOReceiver receiver) {
        this.receiver = receiver;
    }

    public static void main(String args[]){
        testSubmit();
        //testTrace();
        //testBarCode();
    }

    public static void testSubmit(){

        try {
            int a = 10;

            ZTOInternationSubmit ztoInternationSubmit = new ZTOInternationSubmit();
            ztoInternationSubmit.setId("10101010");
            ZTOSender ztoSender = new ZTOSender();
            ztoSender.setName("丰趣海淘");
            ztoSender.setMobile("13636312766");
            ztoSender.setCity("上海市,上海市,徐汇区");
            ztoSender.setAddress("虹梅路2007号");
            ztoInternationSubmit.setSender(ztoSender);
            ZTOReceiver ztoReceiver = new ZTOReceiver();
            ztoReceiver.setName("张三");
            ztoReceiver.setMobile("13501002068");
            ztoReceiver.setCity("北京市,北京市,海淀区");
            ztoReceiver.setAddress("北四环");
            ztoInternationSubmit.setReceiver(ztoReceiver);

            String data = JSONUtil.toJson(ztoInternationSubmit);
            String content = ZTODigestUtil.encryptBase64(data);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datetime = sdf.format(new Date());

            Map<String,Object> map = new HashMap<String,Object>();
            map.put("content",content);
            map.put("style","json");
            map.put("func","order.submit");
            map.put("partner","test");
            map.put("datetime",datetime);
            //key等信息需从diamond获取
            map.put("verify",ZTODigestUtil.digest("test",datetime,content,"ZTO123"));

            String url = "http://testpartner.zto.cn/client/interface.php";
            String res = ZTOHttpUtil.post(url, "utf-8", map);
            ZTOInternationSubmitResp ztoInternationSubmitResp = JSONUtil.parseJSONMessage(res,ZTOInternationSubmitResp.class);
            if( ztoInternationSubmitResp !=null && ztoInternationSubmitResp.getResult().equals("true")){
                String mailNo = ztoInternationSubmitResp.getKeys().getMailno();
                QRUtil.getBarcodeWriteFile(mailNo,1000,400,new File("D:/barCode.png"));
            }

        }catch (Exception e){
        }
    }

    public static void testBarCode(){
        try {
            String mailNo = "368806661236";
            QRUtil.getBarcodeWriteFile(mailNo,200,40,new File("D:/barCode.jpeg"));
        }catch (Exception e){
        }
    }


    public static void testTrace(){

        try {
            String url = "http://testpartner.zto.cn/client/interface.php";
            Map<String, Object> map = new HashMap<String, Object>();
            String data= "{\"mailno\": \"718595286704\"}" ;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datetime = sdf.format(new Date());

            String content = ZTODigestUtil.encryptBase64(data);
            map.put("content", content);
            map.put("style", "json");
            map.put("func", "mail.trace");
            map.put("partner", "test");
            map.put("datetime", datetime);

            map.put("verify", ZTODigestUtil.digest("test" , datetime , content , "ZTO123"));

            String res = ZTOHttpUtil.post(url, "utf-8", map);
            System.out.println(res);
        }
        catch (Exception e){
        }
    }
}
