package com.sfebiz.supplychain.service.stockout.statemachine.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.sku.model.SkuDeclareBO;
import com.sfebiz.supplychain.service.statemachine.EngineRequest;
import com.sfebiz.supplychain.service.statemachine.EngineType;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBuyerBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderRecordBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderTaskBO;

import net.pocrd.entity.ServiceException;

/**
 * 
 * <p>出库单流程相关请求对象</p>
 * 
 * @author matt
 * @Date 2017年7月20日 上午10:45:29
 */
public class StockoutOrderRequest extends EngineRequest {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
                                                           .getTraceLogger("order");

    @Override
    public HaitaoTraceLogger getTraceLogger() {

        return traceLogger;
    }

    @Override
    public String getTraceId() {
        if (null != stockoutOrderBO && StringUtils.isNotEmpty(stockoutOrderBO.getBizId())) {
            return stockoutOrderBO.getMerchantAccountId() + "_" + stockoutOrderBO.getBizId();
        }
        return bizId;
    }

    /**
     * 业务订单号
     */
    private String                  bizId;

    /**
     * 出库单业务对象
     */
    private StockoutOrderBO         stockoutOrderBO;

    /**
     * 起始处理器标识，如果设置了此TAG，则Processor会从指定位置执行
     */
    private String                  startProcessorTag;

    /**
     * 当前处理器标识
     */
    private String                  currentProcssorTag;

    /**
     * 如果在当前节点发生异常，此属性记录异常类型
     */
    private String                  exceptionType;

    /**
     * 此属性记录子异常类型
     */
    private String                  subExceptionType;

    /**
     * 如果在当前节点发生异常，此属性记录异常信息
     */
    private String                  exceptionMessage;

    /**
     * 任务重试时间
     */
    private Date                    nextRetryTime;

    /**
     * 时间发生时间
     */
    private Date                    eventTime;

    /**
     * 路线口岸限制单数截止时间
     */
    private Date[]                  timeLimitRange;

    /**
     * SKU 备案信息映射关系
     */
    // TODO matt
    private Map<Long, SkuDeclareBO> productDeclareEntityMap;

    /**
     * 状态流转备注
     */
    private String                  stateChangeRemark;

    /**
     * 流程引擎标识
     */
    private EngineType              engineType = EngineType.STOCKOUT_ORDER;

    /**
     * 用于后续获取异常code
     */
    private ServiceException        serviceException;
    
    private String pdfRegion;

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public StockoutOrderBO getStockoutOrderBO() {
        return stockoutOrderBO;
    }

    public void setStockoutOrderBO(StockoutOrderBO stockoutOrderBO) {
        this.stockoutOrderBO = stockoutOrderBO;
    }

    public boolean equals(Object arg0) {
        return stockoutOrderBO.equals(arg0);
    }

    public Long getId() {
        return stockoutOrderBO.getId();
    }

    public void setId(Long id) {
        stockoutOrderBO.setId(id);
    }

    public Date getGmtCreate() {
        return stockoutOrderBO.getGmtCreate();
    }

    public void setGmtCreate(Date gmtCreate) {
        stockoutOrderBO.setGmtCreate(gmtCreate);
    }

    public Date getGmtModified() {
        return stockoutOrderBO.getGmtModified();
    }

    public int hashCode() {
        return stockoutOrderBO.hashCode();
    }

    public void setGmtModified(Date gmtModified) {
        stockoutOrderBO.setGmtModified(gmtModified);
    }

    public String getMerchantOrderNo() {
        return stockoutOrderBO.getMerchantOrderNo();
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        stockoutOrderBO.setMerchantOrderNo(merchantOrderNo);
    }

    public String getOrderState() {
        return stockoutOrderBO.getOrderState();
    }

    public void setOrderState(String orderState) {
        stockoutOrderBO.setOrderState(orderState);
    }

    public Integer getOrderType() {
        return stockoutOrderBO.getOrderType();
    }

    public void setOrderType(Integer orderType) {
        stockoutOrderBO.setOrderType(orderType);
    }

    public String getOrderSource() {
        return stockoutOrderBO.getOrderSource();
    }

    public void setOrderSource(String orderSource) {
        stockoutOrderBO.setOrderSource(orderSource);
    }

    public Integer getServiceType() {
        return stockoutOrderBO.getServiceType();
    }

    public void setServiceType(Integer serviceType) {
        stockoutOrderBO.setServiceType(serviceType);
    }

    public String getMerchantAccountId() {
        return stockoutOrderBO.getMerchantAccountId();
    }

    public void setMerchantAccountId(String merchantAccountId) {
        stockoutOrderBO.setMerchantAccountId(merchantAccountId);
    }

    public Long getMerchantId() {
        return stockoutOrderBO.getMerchantId();
    }

    public void setMerchantId(Long merchantId) {
        stockoutOrderBO.setMerchantId(merchantId);
    }

    public Long getMerchantProviderId() {
        return stockoutOrderBO.getMerchantProviderId();
    }

    public void setMerchantProviderId(Long merchantProviderId) {
        stockoutOrderBO.setMerchantProviderId(merchantProviderId);
    }

    public Long getWarehouseId() {
        return stockoutOrderBO.getWarehouseId();
    }

    public void setWarehouseId(Long warehouseId) {
        stockoutOrderBO.setWarehouseId(warehouseId);
    }

    public String getWarehouseNid() {
        return stockoutOrderBO.getWarehouseNid();
    }

    public void setWarehouseNid(String warehouseNid) {
        stockoutOrderBO.setWarehouseNid(warehouseNid);
    }

    public Long getLineId() {
        return stockoutOrderBO.getLineId();
    }

    public void setLineId(Long lineId) {
        stockoutOrderBO.setLineId(lineId);
    }

    public String getDestcode() {
        return stockoutOrderBO.getDestcode();
    }

    public void setDestcode(String destcode) {
        stockoutOrderBO.setDestcode(destcode);
    }

    public String getWaveNo() {
        return stockoutOrderBO.getWaveNo();
    }

    public void setWaveNo(String waveNo) {
        stockoutOrderBO.setWaveNo(waveNo);
    }

    public String getNeedCheck() {
        return stockoutOrderBO.getNeedCheck();
    }

    public void setNeedCheck(String needCheck) {
        stockoutOrderBO.setNeedCheck(needCheck);
    }

    public String getDeliveryMode() {
        return stockoutOrderBO.getDeliveryMode();
    }

    public void setDeliveryMode(String deliveryMode) {
        stockoutOrderBO.setDeliveryMode(deliveryMode);
    }

    public String getIntlCarrierCode() {
        return stockoutOrderBO.getIntlCarrierCode();
    }

    public void setIntlCarrierCode(String intlCarrierCode) {
        stockoutOrderBO.setIntlCarrierCode(intlCarrierCode);
    }

    public String getIntlMailNo() {
        return stockoutOrderBO.getIntlMailNo();
    }

    public void setIntlMailNo(String intlMailNo) {
        stockoutOrderBO.setIntlMailNo(intlMailNo);
    }

    public String getIntrCarrierCode() {
        return stockoutOrderBO.getIntrCarrierCode();
    }

    public void setIntrCarrierCode(String intrCarrierCode) {
        stockoutOrderBO.setIntrCarrierCode(intrCarrierCode);
    }

    public String getIntrMailNo() {
        return stockoutOrderBO.getIntrMailNo();
    }

    public void setIntrMailNo(String intrMailNo) {
        stockoutOrderBO.setIntrMailNo(intrMailNo);
    }

    public Integer getPkgLength() {
        return stockoutOrderBO.getPkgLength();
    }

    public void setPkgLength(Integer pkgLength) {
        stockoutOrderBO.setPkgLength(pkgLength);
    }

    public Integer getPkgWidth() {
        return stockoutOrderBO.getPkgWidth();
    }

    public void setPkgWidth(Integer pkgWidth) {
        stockoutOrderBO.setPkgWidth(pkgWidth);
    }

    public Integer getPkgHeight() {
        return stockoutOrderBO.getPkgHeight();
    }

    public void setPkgHeight(Integer pkgHeight) {
        stockoutOrderBO.setPkgHeight(pkgHeight);
    }

    public Integer getCalWeight() {
        return stockoutOrderBO.getCalWeight();
    }

    public void setCalWeight(Integer calWeight) {
        stockoutOrderBO.setCalWeight(calWeight);
    }

    public Integer getActualWeight() {
        return stockoutOrderBO.getActualWeight();
    }

    public void setActualWeight(Integer actualWeight) {
        stockoutOrderBO.setActualWeight(actualWeight);
    }

    public String getMerchantPayType() {
        return stockoutOrderBO.getMerchantPayType();
    }

    public void setMerchantPayType(String merchantPayType) {
        stockoutOrderBO.setMerchantPayType(merchantPayType);
    }

    public String getMerchantPayNo() {
        return stockoutOrderBO.getMerchantPayNo();
    }

    public void setMerchantPayNo(String merchantPayNo) {
        stockoutOrderBO.setMerchantPayNo(merchantPayNo);
    }

    public String getDeclarePayType() {
        return stockoutOrderBO.getDeclarePayType();
    }

    public void setDeclarePayType(String declarePayType) {
        stockoutOrderBO.setDeclarePayType(declarePayType);
    }

    public String getDeclarePayNo() {
        return stockoutOrderBO.getDeclarePayNo();
    }

    public void setDeclarePayNo(String declarePayNo) {
        stockoutOrderBO.setDeclarePayNo(declarePayNo);
    }

    public String getDeclarePayerName() {
        return stockoutOrderBO.getDeclarePayerName();
    }

    public void setDeclarePayerName(String declarePayerName) {
        stockoutOrderBO.setDeclarePayerName(declarePayerName);
    }

    public String getDeclarePayerCertNo() {
        return stockoutOrderBO.getDeclarePayerCertNo();
    }

    public void setDeclarePayerCertNo(String declarePayerCertNo) {
        stockoutOrderBO.setDeclarePayerCertNo(declarePayerCertNo);
    }

    public String getDeclarePayerCertType() {
        return stockoutOrderBO.getDeclarePayerCertType();
    }

    public void setDeclarePayerCertType(String declarePayerCertType) {
        stockoutOrderBO.setDeclarePayerCertType(declarePayerCertType);
    }

    public Integer getUserGoodsPrice() {
        return stockoutOrderBO.getUserGoodsPrice();
    }

    public void setUserGoodsPrice(Integer userGoodsPrice) {
        stockoutOrderBO.setUserGoodsPrice(userGoodsPrice);
    }

    public Integer getUserDiscountPrice() {
        return stockoutOrderBO.getUserDiscountPrice();
    }

    public void setUserDiscountPrice(Integer userDiscountPrice) {
        stockoutOrderBO.setUserDiscountPrice(userDiscountPrice);
    }

    public Integer getUserFreightFee() {
        return stockoutOrderBO.getUserFreightFee();
    }

    public void setUserFreightFee(Integer userFreightFee) {
        stockoutOrderBO.setUserFreightFee(userFreightFee);
    }

    public Integer getSettleFreightFee() {
        return stockoutOrderBO.getSettleFreightFee();
    }

    public void setSettleFreightFee(Integer settleFreightFee) {
        stockoutOrderBO.setSettleFreightFee(settleFreightFee);
    }

    public Integer getCalFreightFee() {
        return stockoutOrderBO.getCalFreightFee();
    }

    public void setCalFreightFee(Integer calFreightFee) {
        stockoutOrderBO.setCalFreightFee(calFreightFee);
    }

    public Date getEstimatedShippingTime() {
        return stockoutOrderBO.getEstimatedShippingTime();
    }

    public void setEstimatedShippingTime(Date estimatedShippingTime) {
        stockoutOrderBO.setEstimatedShippingTime(estimatedShippingTime);
    }

    public String getRemarks() {
        return stockoutOrderBO.getRemarks();
    }

    public void setRemarks(String remarks) {
        stockoutOrderBO.setRemarks(remarks);
    }

    public StockoutOrderBuyerBO getBuyerBO() {
        return stockoutOrderBO.getBuyerBO();
    }

    public void setBuyerBO(StockoutOrderBuyerBO buyerBO) {
        stockoutOrderBO.setBuyerBO(buyerBO);
    }

    public StockoutOrderRecordBO getRecordBO() {
        return stockoutOrderBO.getRecordBO();
    }

    public void setRecordBO(StockoutOrderRecordBO recordBO) {
        stockoutOrderBO.setRecordBO(recordBO);
    }

    public List<StockoutOrderDetailBO> getDetailBOs() {
        return stockoutOrderBO.getDetailBOs();
    }

    public void setDetailBOs(List<StockoutOrderDetailBO> detailBOs) {
        stockoutOrderBO.setDetailBOs(detailBOs);
    }

    public List<StockoutOrderTaskBO> getTaskBO() {
        return stockoutOrderBO.getTaskBO();
    }

    public void setTaskBO(List<StockoutOrderTaskBO> taskBO) {
        stockoutOrderBO.setTaskBO(taskBO);
    }

    public StockoutOrderDeclarePriceBO getDeclarePriceBO() {
        return stockoutOrderBO.getDeclarePriceBO();
    }

    public void setDeclarePriceBO(StockoutOrderDeclarePriceBO declarePriceBO) {
        stockoutOrderBO.setDeclarePriceBO(declarePriceBO);
    }

    public LogisticsLineBO getLineBO() {
        return stockoutOrderBO.getLineBO();
    }

    public void setLineBO(LogisticsLineBO lineBO) {
        stockoutOrderBO.setLineBO(lineBO);
    }

    public String getStartProcessorTag() {
        return startProcessorTag;
    }

    public void setStartProcessorTag(String startProcessorTag) {
        this.startProcessorTag = startProcessorTag;
    }

    public String getCurrentProcssorTag() {
        return currentProcssorTag;
    }

    public void setCurrentProcssorTag(String currentProcssorTag) {
        this.currentProcssorTag = currentProcssorTag;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getSubExceptionType() {
        return subExceptionType;
    }

    public void setSubExceptionType(String subExceptionType) {
        this.subExceptionType = subExceptionType;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public Date getNextRetryTime() {
        return nextRetryTime;
    }

    public void setNextRetryTime(Date nextRetryTime) {
        this.nextRetryTime = nextRetryTime;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public Date[] getTimeLimitRange() {
        return timeLimitRange;
    }

    public void setTimeLimitRange(Date[] timeLimitRange) {
        this.timeLimitRange = timeLimitRange;
    }

    public Map<Long, SkuDeclareBO> getProductDeclareEntityMap() {
        return productDeclareEntityMap;
    }

    public void setProductDeclareEntityMap(Map<Long, SkuDeclareBO> productDeclareEntityMap) {
        this.productDeclareEntityMap = productDeclareEntityMap;
    }

    public String getStateChangeRemark() {
        return stateChangeRemark;
    }

    public void setStateChangeRemark(String stateChangeRemark) {
        this.stateChangeRemark = stateChangeRemark;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public ServiceException getServiceException() {
        return serviceException;
    }

    public void setServiceException(ServiceException serviceException) {
        this.serviceException = serviceException;
    }

    public static HaitaoTraceLogger getTracelogger() {
        return traceLogger;
    }

    public String getPdfRegion() {
        return pdfRegion;
    }

    public void setPdfRegion(String pdfRegion) {
        this.pdfRegion = pdfRegion;
    }

}
