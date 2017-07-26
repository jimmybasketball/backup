package com.sfebiz.supplychain.provider.command.send.port.hangzhou;


import cn.gov.zjport.newyork.ws.CheckReceived;
import cn.gov.zjport.newyork.ws.CheckReceivedResponse;
import cn.gov.zjport.newyork.ws.bo.*;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.sku.enums.SkuDeclareStateType;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareDO;
import com.sfebiz.supplychain.protocol.common.DeclareType;
import com.sfebiz.supplychain.provider.biz.SkuSyncBizService;
import com.sfebiz.supplychain.provider.command.send.port.PortProductDeclareCommand;
import com.sfebiz.supplychain.provider.entity.ResponseState;
import com.sfebiz.supplychain.service.port.model.LogisticsPortBO;
import com.sfebiz.supplychain.service.sku.model.SkuDeclareBO;
import com.sfebiz.supplychain.util.XMLUtil;
import org.springframework.cglib.beans.BeanCopier;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>杭州口岸商品备案</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/12
 * Time: 下午1:52
 */
public class HzPortProductDeclareCommand extends PortProductDeclareCommand {

    private String responseString = "";

    private String remark = "";

    @Override
    public boolean execute() {
        try {
            Request productDeclareRequest = buildRequest();
            String productDeclare2XmlFormat = XMLUtil.convertToXml(productDeclareRequest);
            if (productDeclare2XmlFormat == null) {
                return false;
            }
            boolean result = sendProduct2HzPort4Declare(productDeclare2XmlFormat);
            return result;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-向杭州口岸下发指令异常]")
                    .addParm("原因", e.getMessage())
                    .setException(e)
                    .log();
        }
        return false;
    }

    /**
     * 给杭州电子口岸发送商品备案信息
     *
     * @param productInfo
     * @return
     */
    private boolean sendProduct2HzPort4Declare(String productInfo) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("cn.gov.zjport.newyork.ws");
            QName serviceName = new QName("http://impl.ws.newyork.zjport.gov.cn/", "ReceivedDeclareServiceImplService");
            QName portName = new QName("http://impl.ws.newyork.zjport.gov.cn/", "ReceivedDeclareServiceImplPort");
            Service service = Service.create(new URL(portBO.getUrl()), serviceName);

            Dispatch<Object> dispatch = service.createDispatch(portName, jaxbContext, Service.Mode.PAYLOAD);
            CheckReceived checkReceived = new CheckReceived();
            checkReceived.setXmlStr(productInfo);
            checkReceived.setXmlType(HzPortBusinessType.PRODUCT_RECORD.getType());
            checkReceived.setSourceType("1");
            JAXBElement<CheckReceivedResponse> resp = (JAXBElement<CheckReceivedResponse>) dispatch.invoke(checkReceived);
            responseString = resp.getValue().getReturn();
            Response response = XMLUtil.converyToJavaBean(responseString, Response.class);
            if (response == null) {
                return false;
            }
            List<JKFResult> results = response.getBody().getList();
            for (JKFResult result : results) {
                List<JKFResultDetail> resultDetail = result.getJkfResultDetail();
                if (resultDetail != null && resultDetail.size() > 0) {
                    remark = resultDetail.get(0).getResultInfo();
                }
                if (ResponseState.ONE.getCode().equals(result.getChkMark())) {
                    //return submitSuccess(remark);
                    return true;
                }
            }

        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-向杭州口岸下发指令异常]")
                    .addParm("原因", e.getMessage())
                    .setException(e)
                    .log();
        }

        //submitFailure(remark);
        return false;
    }


    /**
     * 构建 商品备案实体
     *
     * @return
     */
    private Request buildRequest() {
        Request request = new Request();
        request.getHead().setBusinessType(HzPortBusinessType.PRODUCT_RECORD.getType());
        ProductRecord productRecord = new ProductRecord();
        request.getBody().getProductRecordList().add(productRecord);

        SkuDeclareBO skuDeclareBO = this.getSkuDeclareBO();
        LogisticsPortBO portBO = this.getPortBO();
        productRecord.getJkfSign().setCompanyCode(portBO.getCompanyCode());
        //待备案的商品ID，skuID
        productRecord.getJkfSign().setBusinessNo(skuDeclareBO.getSkuId().toString());
        productRecord.getJkfSign().setBusinessType(HzPortBusinessType.PRODUCT_RECORD.getType());
        productRecord.getJkfSign().setDeclareType(DeclareType.CREATE.getType());
        productRecord.getJkfSign().setNote("");

        ProductRecordDto productRecordDto = new ProductRecordDto();
        productRecordDto.setCompanyCode(portBO.getCompanyCode());
        productRecordDto.setCompanyName(portBO.getCompanyName());
        productRecordDto.setPostTaxNo(skuDeclareBO.getPostTaxNo());
        productRecordDto.setGoodsModel("");
        if (skuDeclareBO.getDeclareName().length() > 100) {
            productRecordDto.setGoodsName(skuDeclareBO.getDeclareName().substring(0, 100));
        } else {
            productRecordDto.setGoodsName(skuDeclareBO.getDeclareName());
        }
        productRecordDto.setGoodsType(skuDeclareBO.getCategoryName());
        productRecordDto.setBarCode(skuDeclareBO.getBarCode());
        if (skuDeclareBO.getBrand().length() > 50) {
            productRecordDto.setBrand(skuDeclareBO.getBrand().substring(0, 50));
        } else {
            productRecordDto.setBrand(skuDeclareBO.getBrand());
        }
        if (skuDeclareBO.getCategoryId() != null) {
            productRecordDto.setCategoryCode(skuDeclareBO.getCategoryId().toString());
        } else {
            productRecordDto.setCategoryCode("");
        }
        productRecordDto.setMainElement("");
        productRecordDto.setPurpose("");
        productRecordDto.setStandards("");
        productRecordDto.setProductionCountry("");//最多三个字符
        productRecordDto.setProductionEnterprise("");
        productRecordDto.setLicenceKey("");
        productRecordDto.setMaterialAddress("");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        productRecordDto.setDeclareTimeStr(formatter.format(date));
        productRecord.setProductRecordDto(productRecordDto);

        return request;
    }


    /**
     * 提交成功
     *
     * @return
     */
    private boolean submitSuccess(String remark) {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg("商品备案提交成功")
                .setParms(responseString)
                .log();
        skuDeclareBO.setRemark(remark);
        skuDeclareBO.setState(SkuDeclareStateType.WAIT_DECLARE.getValue());
        skuDeclareBO.setSubmitTime(new Date());

        ProductDeclareDO productDeclareDO = new ProductDeclareDO();
        BeanCopier beanCopier =
                BeanCopier.create(SkuDeclareBO.class, ProductDeclareDO.class, false);
        beanCopier.copy(skuDeclareBO, productDeclareDO, null);
        SkuSyncBizService.getInstance().saveProductDeclareRecord(productDeclareDO);
        return true;
    }

    /**
     * 提交失败
     *
     * @return
     */
    private void submitFailure(String remark) {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg("商品备案提交失败")
                .setParms(responseString)
                .log();
        skuDeclareBO.setRemark(remark);
        skuDeclareBO.setState(SkuDeclareStateType.DECLARE_NOT_PASS.getValue());
        skuDeclareBO.setSubmitTime(new Date());
        ProductDeclareDO productDeclareDO = new ProductDeclareDO();
        BeanCopier beanCopier =
                BeanCopier.create(SkuDeclareBO.class, ProductDeclareDO.class, false);
        beanCopier.copy(skuDeclareBO, productDeclareDO, null);
        SkuSyncBizService.getInstance().saveProductDeclareRecord(productDeclareDO);
    }

}
