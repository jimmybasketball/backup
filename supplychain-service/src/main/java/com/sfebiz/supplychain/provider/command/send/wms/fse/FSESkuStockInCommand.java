package com.sfebiz.supplychain.provider.command.send.wms.fse;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.ProductDeclareManager;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDetailDO;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderDetailManager;
import com.sfebiz.supplychain.protocol.wms.fse.*;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.send.CommandResponse;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSkuStockInCommand;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderBO;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderDetailBO;
import com.sfebiz.supplychain.util.JSONUtil;
import net.pocrd.entity.ServiceException;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zyj on 2016/11/17.
 * description:采购入库，通知仓库商品入库（提交商品给费舍尔仓库）
 */
public class FSESkuStockInCommand extends WmsOrderSkuStockInCommand {

    private static String methodName = "pushPurchaseOrder";
    ProductDeclareManager declareManager;
    StockinOrderDetailManager stockinOrderDetailManager;
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    protected CommandResponse sendStockInCommandToWarehouse() throws ServiceException {
        String meta = warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("meta");
        Map<String, Object> metaData = JSONUtil.parseJSONMessage(meta, Map.class);
        String callInWarehouse = "";
        String companyCode = "";
        String version = "";
        String ip = "";
        String sessionKey = "";
        if (metaData != null && metaData.containsKey("version") && metaData.containsKey("sessionKey") && metaData.containsKey("ip")
                && metaData.containsKey("callInWarehouse") && metaData.containsKey("companyCode")) {
            version = (String) metaData.get("version");
            ip = (String) metaData.get("ip");
            sessionKey = (String) metaData.get("sessionKey");
            callInWarehouse = (String) metaData.get("callInWarehouse");
            companyCode = (String) metaData.get("companyCode");
        } else {
            throw new ServiceException(LogisticsReturnCode.STOCKIN_ORDER_SENDSTOCK_URLORKEY_NULL,
                    "[供应链-提交入库单异常]: " + LogisticsReturnCode.STOCKIN_ORDER_SENDSTOCK_URLORKEY_NULL.getDesc() + " "
                            + "[入库单ID: " + getStockinOrderBO().getId()
                            + ", callInWarehouse: " + callInWarehouse
                            + ", companyCode: " + companyCode
                            + ", version:" + version
                            + ", ip:" + ip
                            + ", sessionKey" + sessionKey
                            + "]");
        }
        declareManager = (ProductDeclareManager) CommandConfig.getSpringBean("productDeclareManager");
        threadPoolTaskExecutor = (ThreadPoolTaskExecutor) CommandConfig.getSpringBean("threadPoolTaskExecutor");
        stockinOrderDetailManager = (StockinOrderDetailManager) CommandConfig.getSpringBean("stockinOrderDetailManager");
        FSESkuStockInItem skuStockInItem = buildRequest(companyCode, callInWarehouse);
        return send(skuStockInItem, version, ip, sessionKey);
    }

    public FSESkuStockInItem buildRequest(String companyCode, String callInWarehouse) throws ServiceException {
        FSESkuStockInItem skuStockInItem = new FSESkuStockInItem();
        StockinOrderBO stockinOrderBO = getStockinOrderBO();
        List<Porder> porders = new ArrayList<Porder>();
        Porder porder = new Porder();
        porder.code = stockinOrderBO.getStockinId();
        porder.companyCode = companyCode;
        porder.callInWarehouse = callInWarehouse;

        List<Item> items = new ArrayList<Item>();
        List<StockinOrderDetailBO> StockinOrderBO = getStockinOrderDetailBOs();
        for(StockinOrderDetailBO orderSkuDO:stockinOrderDetailBOs){
            Item item = new Item();
            item.commodityName = orderSkuDO.getSkuName();
            item.commodityBarcode = orderSkuDO.getSkuId()+"";
            item.shouldInQty = orderSkuDO.getCount()+"";
            if(orderSkuDO.getExpirationDate() != null) {
                item.expirationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderSkuDO.getExpirationDate());
            }
            if(orderSkuDO.getProductionDate() != null) {
                item.manufactureDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderSkuDO.getProductionDate());
            }
            if(StringUtils.isNotBlank(orderSkuDO.getSkuBatch())){
                int length = orderSkuDO.getSkuBatch().length();
                porder.batch = orderSkuDO.getSkuBatch().substring(length - 12, length);
            }
            ProductDeclareDO productDeclareDO = declareManager.queryDeclaredSku(orderSkuDO.getSkuId(), PortNid.CHONGQING.getValue());
            if(productDeclareDO != null && StringUtils.isNotBlank(productDeclareDO.getOrigin())) {
                porder.originCountry = productDeclareDO.getOrigin();
            }else{
                throw new ServiceException(LogisticsReturnCode.STOCKIN_TRANS_ORIGINE,
                        "[供应链-提交入库单异常]: " + LogisticsReturnCode.STOCKIN_TRANS_ORIGINE.getDesc() + " "
                        + "[入库单ID: " + getStockinOrderBO().getId()
                        + ", callInWarehouse: " + callInWarehouse
                        + ", companyCode: " + companyCode
                        + "]");
            }
            items.add(item);
        }
        porder.setItem(items);
        porders.add(porder);
        skuStockInItem.PorderList = porders;

        final List<StockinOrderDetailBO> stockinOrderDetailDOList = stockinOrderDetailBOs;
        final String batch = porder.batch;

        //异步更新入库单明细表中批次号
        if(StringUtils.isNotBlank(batch)){
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    for(StockinOrderDetailBO orderSkuDO:stockinOrderDetailDOList){
                        StockinOrderDetailDO stockinOrderDetailDO = new StockinOrderDetailDO();
                        stockinOrderDetailDO.setId(orderSkuDO.getId());
                        stockinOrderDetailDO.setSkuBatch(batch);
                        stockinOrderDetailManager.update(stockinOrderDetailDO);
                    }
                }
            });
        }
        return skuStockInItem;
    }

    public CommandResponse send(FSESkuStockInItem skuStockInItem, String version, String ip, String sessionKey){
        CommandResponse result = new CommandResponse();
        String json = JSONUtil.toJson(skuStockInItem);
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setTraceLogger(TraceLogEntity.instance(traceLogger, getStockinOrderBO().getId() , SystemConstants.TRACE_APP))
                .setErrorMsg("[供应链-提交入库单]")
                .addParm("触发原因", "推送入库单信息给仓库")
                .addParm("请求参数", json)
                .log();
        try{
            Response response = FSEUtil.send(version, ip, sessionKey,json,warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("interfaceUrl"),warehouseBO.getLogisticsProviderBO().getInterfaceMeta().get("interfaceKey"),methodName);
            String jsonResponse = response.body().string();
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, getStockinOrderBO().getId() , SystemConstants.TRACE_APP))
                    .setErrorMsg("[供应链-提交入库单]")
                    .addParm("触发原因", "推送入库单信息给仓库")
                    .addParm("返回报文", jsonResponse)
                    .log();
            FSEResponse fseResponse = JSONUtil.parseJSONMessage(jsonResponse, FSEResponse.class);
            if (CollectionUtils.isEmpty(fseResponse.ROWSET.ERROR)) {
                result.setIsSuccess(Boolean.TRUE);
            }else{
                result.setIsSuccess(Boolean.FALSE);
                result.setErrorMessage(fseResponse.getROWSET().getResultMsg());
            }
        }catch (Exception e){
            result.setIsSuccess(Boolean.FALSE);
            result.setErrorMessage("入库单信息推送到费舍尔仓库失败");
            logger.error("",e);
        }
        return result;
    }
}
