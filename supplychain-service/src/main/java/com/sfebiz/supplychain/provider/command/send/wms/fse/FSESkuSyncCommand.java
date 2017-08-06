package com.sfebiz.supplychain.provider.command.send.wms.fse;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import com.sfebiz.supplychain.exposed.line.enums.LineType;
import com.sfebiz.supplychain.exposed.sku.entity.SkuEntity;
import com.sfebiz.supplychain.exposed.sku.enums.SkuWarehouseSyncStateType;
import com.sfebiz.supplychain.factory.SpringBeanFactory;
import com.sfebiz.supplychain.lock.DistributedLock;
import com.sfebiz.supplychain.persistence.base.port.manager.PortParamManager;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareCqBondedDO;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareDO;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuWarehouseSyncDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.ProductDeclareCqBondedManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.ProductDeclareManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuWarehouseSyncLogManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuWarehouseSyncManager;
import com.sfebiz.supplychain.protocol.wms.fse.*;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSkuSyncCommand;
import com.sfebiz.supplychain.util.JSONUtil;
import net.pocrd.entity.ServiceException;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/4.
 */
public class
FSESkuSyncCommand extends WmsOrderSkuSyncCommand {

    private ProductDeclareManager productDeclareManager;
    private PortParamManager portParamManager;
    private SkuWarehouseSyncLogManager skuWarehouseSyncLogManager;
    private ProductDeclareCqBondedManager productDeclareCqBondedManager;

    private static String methodName = "sendCommodity";

    @Override
    public boolean execute(){
        boolean isMockAutoCreated = MockConfig.isMocked("fse", "skuSyncCommand");
        if (isMockAutoCreated) {
            //直接返回仓库已发货
            logger.info("MOCK：重庆仓库 商品同步 采用MOCK实现");
            return mockSkuSyncSuccess();
        }
        try {
            productDeclareManager = (ProductDeclareManager) CommandConfig.getSpringBean("productDeclareManager");
            portParamManager = (PortParamManager) CommandConfig.getSpringBean("portParamManager");
            skuWarehouseSyncLogManager = (SkuWarehouseSyncLogManager)  CommandConfig.getSpringBean("skuWarehouseSyncLogManager");
            productDeclareCqBondedManager = (ProductDeclareCqBondedManager) CommandConfig.getSpringBean("productDeclareCqBondedManager");

            String meta = logisticsProviderBO.getInterfaceMeta().get("meta");
            Map<String, Object> metaData = JSONUtil.parseJSONMessage(meta, Map.class);
            String warehouseCode = "";
            String version = "";
            String ip = "";
            String sessionKey = "";
            String companyCode = "";
            if (metaData != null && metaData.containsKey("version") && metaData.containsKey("sessionKey") && metaData.containsKey("ip")
                    && metaData.containsKey("warehouseCode") && metaData.containsKey("companyCode")) {
                version = (String) metaData.get("version");
                ip = (String) metaData.get("ip");
                sessionKey = (String) metaData.get("sessionKey");
                warehouseCode = (String) metaData.get("warehouseCode");
                companyCode = (String) metaData.get("companyCode");
            } else {
                logger.error("费舍尔logisticsprovider meta信息不全" + meta);
            }
            FSESkuSyncItem FSESkuSyncItem = buildSku(warehouseCode, companyCode);
            sendSkuInfo2Wms(FSESkuSyncItem, version, ip, sessionKey);
        } catch (ServiceException e) {
            errorMessage = e.getMessage();
            logger.error("", e);
            return false;
        }
        return true;
    }

    public FSESkuSyncItem buildSku(String wareHouseCode, String companyCode) throws ServiceException {
        FSESkuSyncItem skuSyncItem = new FSESkuSyncItem();
        List<FSESkuItem> skuItems = new ArrayList<FSESkuItem>();
        for(SkuEntity sku:skuEntities){
            FSESkuItem skuItem = new FSESkuItem();
            skuItem.setWarehouseCode(wareHouseCode);
            skuItem.setCommodityCode(sku.id+"");
            skuItem.setCommodityName(sku.name);
            skuItem.setCompanyCode(companyCode);
            ProductDeclareDO productDeclareDO = getProductDeclare(sku.id);
            if(null != productDeclareDO && productDeclareDO.getMeasuringUnit() != null
                    && productDeclareDO.getNetWeight() != null
                    && productDeclareDO.getOrigin() != null
                    && productDeclareDO.getFirstMeasuringUnit() != null){
                ProductDeclareCqBondedDO productDeclareCqBondedDO = getCQProductDeclare(productDeclareDO.getId());
                if(null != productDeclareCqBondedDO && productDeclareCqBondedDO.getHsCode() != null){
                    skuItem.setHScode(productDeclareCqBondedDO.getHsCode());
                }else{
                    LogBetter.instance(logger).setParms(productDeclareDO).setMsg("商品重庆备案信息hscode不存在").log();
                    throw new ServiceException(LogisticsReturnCode.SKU_DECLARE_NOT_EXIST, "商品重庆备案信息hscode不存在");
                }
                skuItem.setUnit(productDeclareDO.getMeasuringUnit());
                skuItem.setFirstUnit(productDeclareDO.getFirstMeasuringUnit());
                skuItem.setSecondUnit(productDeclareDO.getSecondMeasuringUnit());
                skuItem.setWeight(productDeclareDO.getNetWeight());
                skuItem.setTradeCountryName(productDeclareDO.getOrigin());
                skuItem.setHScode(productDeclareDO.getHsCode());
                skuItem.setParentCode(productDeclareDO.getBarCode());
                if(StringUtils.isNotBlank(productDeclareDO.getAttributes())){
                    if(productDeclareDO.getAttributes().length() > 255){
                        skuItem.setModels(productDeclareDO.getAttributes().substring(0,255));
                    }else{
                        skuItem.setModels(productDeclareDO.getAttributes());
                    }
                }
            }else{
                LogBetter.instance(logger).setParms(productDeclareDO).setMsg("商品备案信息不存在").log();
                throw new ServiceException(LogisticsReturnCode.SKU_DECLARE_NOT_EXIST, "商品备案信息不存在");
            }
            skuItems.add(skuItem);
        }
        skuSyncItem.setCommoditys(skuItems);
        return skuSyncItem;
    }

    public boolean sendSkuInfo2Wms(FSESkuSyncItem FSESkuSyncItem, String version, String ip, String sessionKey){
        String json = JSONUtil.toJson(FSESkuSyncItem);
        logger.info("费舍尔商品同步信息："+ json);
        try {
            Response response = FSEUtil.send(version, ip, sessionKey,json,logisticsProviderBO.getInterfaceMeta().get("interfaceUrl"),logisticsProviderBO.getInterfaceMeta().get("interfaceKey"),methodName);
            String jsonResponse = response.body().string();
            logger.info("费舍尔商品同步回传：" +jsonResponse);
            FSEResponse fseResponse = JSONUtil.parseJSONMessage(jsonResponse, FSEResponse.class);
            DistributedLock distributedLock = SpringBeanFactory.getBean("distributedLock", DistributedLock.class);
            if (distributedLock.fetch("SKU-SYNC-LOCK")) {
                if (CollectionUtils.isEmpty(fseResponse.ROWSET.ERROR)) {
                    logger.info("同步sku成功");
                    SkuWarehouseSyncManager skuWarehouseSyncManager = SpringBeanFactory.getBean("skuWarehouseSyncManager", SkuWarehouseSyncManager.class);
                    for (SkuEntity skuEntity : skuEntities) {
                        SkuWarehouseSyncDO warehouseSyscDO = skuWarehouseSyncManager.getBySkuIdAndWarehouseId(skuEntity.getId(), this.getWarehouseBO().getId());
                        if (warehouseSyscDO != null) {
                            logger.info("skuId:{},warehouseId:{} 存在,update", skuEntity.id, this.getWarehouseBO().getId());
                            warehouseSyscDO.setSyncState(SkuWarehouseSyncStateType.SYNC_SUCCESS.value);
                            skuWarehouseSyncManager.update(warehouseSyscDO);
                        } else {
                            logger.error("skuId:{},warehouseId:{} 不存在", skuEntity.id, this.getWarehouseBO().getId());
                        }
                        skuWarehouseSyncLogManager.createLog(skuEntity.id, this.getWarehouseBO().getId(), "成功", fseResponse.toString(), 1);
                    }
                    return true;
                } else {
                    for (FSESkuSyncError error : fseResponse.ROWSET.ERROR) {
                    //防止并发插入
                    errorMessage = error.errorMsg;
                    skuWarehouseSyncLogManager.createLog(Long.parseLong(error.code), this.getWarehouseBO().getId(), errorMessage, fseResponse.toString(), 0);
                    logger.info("同步sku:"+error.code+"失败");
                    }
            }
        }
        } catch (Exception e) {
            logger.error("",e);
            return false;
        }
        return true;
    }

    public ProductDeclareDO getProductDeclare(long skuid){
        ProductDeclareDO productDeclareDO = new ProductDeclareDO();
        productDeclareDO.setPortId(Long.valueOf(PortNid.CHONGQING.getValue()));
        productDeclareDO.setDeclareMode(LineType.BONDED.getValue());
        productDeclareDO.setSkuId(skuid);
        List<ProductDeclareDO> productDeclareDOs = productDeclareManager.query(BaseQuery.getInstance(productDeclareDO));
        if (CollectionUtils.isNotEmpty(productDeclareDOs)) {
            productDeclareDO = productDeclareDOs.get(0);
            productDeclareDO.setMeasuringUnit(portParamManager.getPortParamCode(Long.valueOf(PortNid.CHONGQING.getValue()),0,productDeclareDO.getMeasuringUnit(),false));
            productDeclareDO.setFirstMeasuringUnit(portParamManager.getPortParamCode(Long.valueOf(PortNid.CHONGQING.getValue()),0,productDeclareDO.getFirstMeasuringUnit(),false));
            if(StringUtils.isNotBlank(productDeclareDO.getSecondMeasuringUnit())){
                productDeclareDO.setSecondMeasuringUnit(portParamManager.getPortParamCode(Long.valueOf(PortNid.CHONGQING.getValue()),0,productDeclareDO.getSecondMeasuringUnit(),false));
            }
            return productDeclareDO;
        }else{
            return null;
        }
    }

    public ProductDeclareCqBondedDO getCQProductDeclare(long productdeclareId){
        ProductDeclareCqBondedDO productDeclareCqBondedDO = new ProductDeclareCqBondedDO();
        productDeclareCqBondedDO.setProductDeclareId(productdeclareId);
        List<ProductDeclareCqBondedDO> productDeclareCqBondedDOs = productDeclareCqBondedManager.query(BaseQuery.getInstance(productDeclareCqBondedDO));
        if (CollectionUtils.isNotEmpty(productDeclareCqBondedDOs)) {
            productDeclareCqBondedDO = productDeclareCqBondedDOs.get(0);
            return productDeclareCqBondedDO;
        }else{
            return null;
        }
    }

    public static void main(String args[]){
        String baseString = "1.0127.0.0.12016FQHTAV120161107155714FQHT";
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
        System.out.println(sign.toString());
    }
}
