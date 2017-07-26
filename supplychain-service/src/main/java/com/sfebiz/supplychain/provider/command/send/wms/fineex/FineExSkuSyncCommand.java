package com.sfebiz.supplychain.provider.command.send.wms.fineex;

import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.sku.entity.SkuEntity;
import com.sfebiz.supplychain.exposed.sku.enums.SkuWarehouseSyncStateType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsOperaterType;
import com.sfebiz.supplychain.factory.SpringBeanFactory;
import com.sfebiz.supplychain.lock.DistributedLock;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuBarcodeDO;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuWarehouseSyncDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuBarcodeManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuWarehouseSyncLogManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuWarehouseSyncManager;
import com.sfebiz.supplychain.protocol.fineex.FineExResponse;
import com.sfebiz.supplychain.protocol.fineex.skuSyn.FineExSkuSubSynRequest;
import com.sfebiz.supplychain.protocol.fineex.skuSyn.FineExSkuSynItem;
import com.sfebiz.supplychain.protocol.fineex.skuSyn.FineExSkuSynRequest;
import com.sfebiz.supplychain.provider.biz.CommonBizService;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSkuSyncCommand;
import com.sfebiz.supplychain.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wuyun on 2017/3/14.
 */
public class FineExSkuSyncCommand extends WmsOrderSkuSyncCommand{

    private static final String ACTION_TYPE_ADD = "ADD";//操作类型：ADD-新增 MODIFY-修改（actionType=ADD，发网已存在的商品进行修改操作）
    private static final Byte BATCH_RULE_PRODUCTION = 1;//批次规则（0生产日期，1失效日期）

    /**
     * 通用商品同步方法
     *
     * @return
     */
    @Override
    public boolean execute() {
        boolean flag = false;
        boolean isMockAutoCreated = MockConfig.isMocked("fineEx", "skuSyncCommand");
        if (isMockAutoCreated) {
            //直接返回仓库已发货
            logger.info("MOCK：fineExa发网云仓 商品同步 采用MOCK实现");
            return mockSkuSyncSuccess();
        }
        try {
        	List<SkuEntity> skuEntities = getSkuEntities();
        	if (null==skuEntities || skuEntities.size() < 1){
        		logger.warn("同步请求skuEntities实体 参数为空");
                return flag;
        	}
        	//构建同步主码及商品参数
        	FineExSkuSynRequest request = this.buildSkuSynMainFineExRequest();
        	if(request.getItems().size() > 0){
        		flag = this.syncMain(request);//参数有值 则同步主码及商品
        	}
        	//构建同步子码参数
        	List<FineExSkuSubSynRequest> subSkuListRequest = this.buildSkuSynSubcodeFineExRequest();
        	if(subSkuListRequest.size() > 0){
        		for (FineExSkuSubSynRequest fineExSkuSubSynRequest : subSkuListRequest) {
					this.syncSub(fineExSkuSubSynRequest);//同步子码
				}
        	}

        } catch (Exception e) {
            logger.error("商品同步内部异常", e);
        }

        return  flag;
    }
    /**
     * 同步主码及商品信息
     * @param request
     * @return
     */
    private boolean syncMain(FineExSkuSynRequest request) {
    	boolean flag = false;
        LogBetter.instance(logger)
                .setMsg(warehouseDO.getName() + "同步请求商品及主码to FineEx参数")
                .addParm("request", request)
                .setLevel(LogLevel.INFO)
                .log();
        if (null==request){
            logger.warn("同步请求商品及主码to FineEx参数为空");
            return flag;
        }

        FineExResponse responses = null;
        try {
        	String reqXml = XMLUtil.convertToXml(request);
        	logger.info("同步请求FineEx商品及主码报文："+reqXml);
            String responseXml = CommonBizService.getInstance().fineExSend("fineex.wms.products.sync",reqXml);
            logger.info("同步商品及主码响应FineEx报文： "+responseXml);
            if (StringUtils.isBlank(responseXml)){
                logger.warn("同步商品及主码FineEx返回xml为空");
                return flag;
            }
            responses = XMLUtil.converyToJavaBean(responseXml, FineExResponse.class);
            if (null!=responses && StringUtils.isNotBlank(responses.getFlag()) && "true".equalsIgnoreCase(responses.getFlag())){
                flag = true;
                logger.info("同步商品及主码到fineEx发网成功，返回");
            }
            LogBetter.instance(logger)
                    .setMsg(warehouseDO.getName() + "同步商品及主码响应FineEx参数")
                    .addParm("request", request)
                    .setLevel(LogLevel.INFO)
                    .log();
            SkuWarehouseSyncLogManager skuWarehouseSyncLogManager = SpringBeanFactory.getBean("skuWarehouseSyncLogManager", SkuWarehouseSyncLogManager.class);
            if (flag) {
            	logger.info("同步商品及主码sku成功");
            	DistributedLock distributedLock = SpringBeanFactory.getBean("distributedLock", DistributedLock.class);
            	try {
            		//防止并发插入
            		if (distributedLock.fetch("SKU-SYNC-LOCK")) {
                        SkuWarehouseSyncManager skuWarehouseSyncManager = SpringBeanFactory.getBean("skuWarehouseSyncManager", SkuWarehouseSyncManager.class);
            			for (FineExSkuSynItem item : request.getItems()) {
//            				SkuWarehouseSyncDO WarehouseSyncDO = SkuBO.getInstance().getSkuWarehouseSyncManager().getBySkuIdAndStorehouseId(Long.valueOf(item.getItemCode()), this.getWarehouseDO().getId(), SkuType.BASIC_SKU.getValue());
                            SkuWarehouseSyncDO warehouseSyncDO = skuWarehouseSyncManager.getBySkuIdAndWarehouseId(Long.valueOf(item.getItemCode()), this.warehouseDO.getId());
                            if (warehouseSyncDO != null) {
                                warehouseSyncDO.setBarCode(item.getBarCode());
                                logger.info("skuId:{},warehouseId:{} 存在,update", item.getItemCode(), this.getWarehouseDO().getId());
                                if (WmsOperaterType.ADD.equals(getWmsOperaterType())) {
                                    warehouseSyncDO.setSyncState(SkuWarehouseSyncStateType.SYNC_SUCCESS.value);
                                } else {
                                    warehouseSyncDO.setSyncUpdateState(SkuWarehouseSyncStateType.SYNC_UPDATE_SUCCESS.value);
                                }
                                warehouseSyncDO.setGmtModified(new Date());
                                skuWarehouseSyncManager.update(warehouseSyncDO);
            				} else {
            					logger.error("skuId:{},warehouseId:{} 不存在", item.getItemCode(), this.getWarehouseDO().getId());
            				}
            				skuWarehouseSyncLogManager.createLog(Long.valueOf(item.getItemCode()), this.getWarehouseDO().getId(), "成功", responses.toString(), 1);
            			}
            		}
				} catch (Exception e) {
					logger.error("同步商品主码，修改状态错误",e);
				}finally {
		        	if (null!=distributedLock) {
		        		distributedLock.realease("SKU-SYNC-LOCK");
					}
		        }
            } else {
            	if (null!=responses){
            		errorMessage +=";"+ responses.getMessage();
            	}
            	skuWarehouseSyncLogManager.createLog(0, this.getWarehouseDO().getId(), errorMessage, responses.toString(), 0);
            }
        }catch (Exception e){
            errorMessage = e.getMessage();
            logger.error(errorMessage,e);
        }
		return flag;
	}
    /**
     * 同步子码
     * @param request
     * @return
     */
    public boolean syncSub(FineExSkuSubSynRequest request) {
    	boolean flag = false;
    	FineExResponse responses = null;
    	try {
        	String reqXml = XMLUtil.convertToXml(request);
        	logger.info("同步请求FineEx商品子码报文："+reqXml);
            String responseXml = CommonBizService.getInstance().fineExSend("fineex.wms.product.subsync",reqXml);
            logger.info("同步响应商品子码FineEx报文："+responseXml);
            if (StringUtils.isBlank(responseXml)){
                logger.warn("同步商品子码FineEx返回xml为空");
                return flag;
            }
            responses = XMLUtil.converyToJavaBean(responseXml, FineExResponse.class);
            if (null!=responses && StringUtils.isNotBlank(responses.getFlag()) && "true".equalsIgnoreCase(responses.getFlag())){
                flag = true;
                logger.info("同步商品子码到fineEx发网成功，返回");
            }
            LogBetter.instance(logger)
                    .setMsg(warehouseDO.getName() + "同步商品子码响应FineEx参数")
                    .addParm("request", request)
                    .setLevel(LogLevel.INFO)
                    .log();
            SkuWarehouseSyncLogManager skuWarehouseSyncLogManager = SpringBeanFactory.getBean("skuWarehouseSyncLogManager", SkuWarehouseSyncLogManager.class);
            if (flag) {
            	logger.info("同步sku子码成功");
            	DistributedLock distributedLock = SpringBeanFactory.getBean("distributedLock", DistributedLock.class);
            	try {
    	            //防止并发插入
    	            if (distributedLock.fetch("SKU-SYNC-LOCK")) {
                		SkuWarehouseSyncManager skuWarehouseSyncManager = SpringBeanFactory.getBean("skuWarehouseSyncManager",SkuWarehouseSyncManager.class);
                		SkuWarehouseSyncDO skuSyncDo = new SkuWarehouseSyncDO();
                		skuSyncDo.setSkuId(request.getSkuId());
                		skuSyncDo.setBarCode(request.getSubCode());
                		skuSyncDo.setWarehouseId(request.getStorehouseId());
                		skuSyncDo.setSyncState(SkuWarehouseSyncStateType.SYNC_SUCCESS.value);
                		skuWarehouseSyncManager.insert(skuSyncDo);
                		LogBetter.instance(logger)
                		.setMsg(warehouseDO.getName() + "插入子码同步记录成功,参数:")
                		.addParm("request", request)
                		.setLevel(LogLevel.INFO)
                		.log();
                		String reason = "skuId" + request.getSkuId() + ",bacode:" + request.getSubCode()+"成功";
                		skuWarehouseSyncLogManager.createLog(request.getSkuId(), this.getWarehouseDO().getId(), reason, responses.toString(), 1);
    	            }
				} catch (Exception e) {
					logger.error("同步商品子码，修改状态错误",e);
				}finally {
		            if (null!=distributedLock) {
		        		distributedLock.realease("SKU-SYNC-LOCK");
					}
		        }
            } else {
            	if (null!=responses){
            		errorMessage +=";"+ responses.getMessage();
            	}
            	skuWarehouseSyncLogManager.createLog(request.getSkuId(), this.getWarehouseDO().getId(), errorMessage, responses.toString(), 0);
            }
        }catch (Exception e){
            errorMessage = e.getMessage();
            logger.error(errorMessage,e);
        }
		return flag;
	}
    /**
     * 构建主码参数
     * @return
     */
    private FineExSkuSynRequest buildSkuSynMainFineExRequest(){
        List<SkuEntity> skuEntities = getSkuEntities();
        FineExSkuSynRequest request = new FineExSkuSynRequest();
        List<FineExSkuSynItem> fineExSkuSynItemList = new ArrayList<FineExSkuSynItem>();
        FineExSkuSynItem skuSynItem = null;
        for (SkuEntity skuBean: skuEntities) {
        	SkuWarehouseSyncDO skuWarehouseSyncDOForQuery = new SkuWarehouseSyncDO();
    		skuWarehouseSyncDOForQuery.setSkuId(skuBean.getId());
    		skuWarehouseSyncDOForQuery.setWarehouseId(this.getWarehouseDO().getId());
            skuWarehouseSyncDOForQuery.setSyncState(SkuWarehouseSyncStateType.SYNC_SUCCESS.value);
            SkuWarehouseSyncManager skuWarehouseSyncManager = SpringBeanFactory.getBean("skuWarehouseSyncManager",SkuWarehouseSyncManager.class);
            BaseQuery<SkuWarehouseSyncDO> bq = new BaseQuery<SkuWarehouseSyncDO>(skuWarehouseSyncDOForQuery);
    		List<SkuWarehouseSyncDO> skuWarehouseSyncDOs = skuWarehouseSyncManager.query(bq);
    		if (null!=skuWarehouseSyncDOs && skuWarehouseSyncDOs.size() > 0) {
    			logger.info("此sku已经同步主码：skuId："+skuBean.getId()+";barCode:"+skuWarehouseSyncDOs.get(0).getBarCode());
				continue;
			}
            skuSynItem = new FineExSkuSynItem();
            skuSynItem.setActionType(FineExSkuSyncCommand.ACTION_TYPE_ADD);
            //barcode可能会有多个，此处取sc_sku_barcode主键最小的作为主码
            SkuBarcodeManager skuBarcodeManager = SpringBeanFactory.getBean("skuBarcodeManager",SkuBarcodeManager.class);
            List<SkuBarcodeDO> skuBarcodeList = skuBarcodeManager.getSkuBySkuId(skuBean.getId());
            if (null!=skuBarcodeList && skuBarcodeList.size() > 0) {
				Collections.sort(skuBarcodeList);//排序
			}
            skuSynItem.setBarCode(skuBarcodeList.size()>0?String.valueOf(skuBarcodeList.get(0).getBarcode()):null);
            skuSynItem.setItemCode(String.valueOf(skuBean.getId()));
            skuSynItem.setItemName(skuBean.getName());
//            skuSynItem.setCategoryName(skuBean.getCategoryName());
            StringBuffer sb = new StringBuffer();
            sb.append(skuBean.getAttributesDesc());
            //此字段发网限制长度100，超过则截取100长度
            String Property = sb.toString().length()>100?sb.toString().substring(0,99):sb.toString();
            skuSynItem.setProperty(Property);
            skuSynItem.setBatchRule(FineExSkuSyncCommand.BATCH_RULE_PRODUCTION);
            fineExSkuSynItemList.add(skuSynItem);
        }
        request.setItems(fineExSkuSynItemList);
        return request;
    }
    /**
     * 构建同步子码参数
     */
    private List<FineExSkuSubSynRequest> buildSkuSynSubcodeFineExRequest() {
    	List<SkuEntity> skuEntities = getSkuEntities();
    	List<FineExSkuSubSynRequest> skuSubList = new ArrayList<FineExSkuSubSynRequest>();
    	FineExSkuSubSynRequest sub = null;
    	for (SkuEntity skuEntity : skuEntities) {
    		SkuWarehouseSyncDO skuWarehouseSyncDOForQuery = new SkuWarehouseSyncDO();
    		skuWarehouseSyncDOForQuery.setSkuId(skuEntity.getId());
    		skuWarehouseSyncDOForQuery.setWarehouseId(this.getWarehouseDO().getId());
            skuWarehouseSyncDOForQuery.setSyncState(SkuWarehouseSyncStateType.SYNC_SUCCESS.value);
    		BaseQuery<SkuWarehouseSyncDO> base = new BaseQuery<SkuWarehouseSyncDO>(skuWarehouseSyncDOForQuery);
            logger.info("同步子码，查询已经同步的子码参数："+GsonUtil.getGsonInstance().toJson(base));
    		//查询出已经同步的barcode
            SkuWarehouseSyncManager skuWarehouseSyncManager = SpringBeanFactory.getBean("skuWarehouseSyncManager",SkuWarehouseSyncManager.class);
    		List<SkuWarehouseSyncDO> skuWarehouseSyncDOs = skuWarehouseSyncManager.query(base);
    		logger.info("同步子码，查询出的已经同步的skuWarehouseSyncDOs:"+GsonUtil.getGsonInstance().toJson(skuWarehouseSyncDOs));
    		if (null==skuWarehouseSyncDOs || skuWarehouseSyncDOs.size() < 1) {
    			logger.info("同步子码，查询出的已经同步的skuWarehouseSyncDOs为空，跳过本次");
				continue;
			}
    		List<String> synBarCode = new ArrayList<String>();
    		for (SkuWarehouseSyncDO skuWarehouseSyncDO : skuWarehouseSyncDOs) {
    			synBarCode.add(skuWarehouseSyncDO.getBarCode());
			}
    		//barcode可能会有多个，此处取sc_sku_barcode主键最小的作为主码
            SkuBarcodeManager skuBarcodeManager = SpringBeanFactory.getBean("skuBarcodeManager",SkuBarcodeManager.class);
            List<SkuBarcodeDO> skuBarcodeList = skuBarcodeManager.getSkuBySkuId(skuEntity.getId());
            logger.info("查询barcode记录表的barcode："+skuBarcodeList);
            if (null!=skuBarcodeList && skuBarcodeList.size() > 0) {
				Collections.sort(skuBarcodeList);//排序
			}

			// 获得该sku所有barcode集合
            List<String> barCodeList = new ArrayList<String>();
            if (CollectionUtils.isNotEmpty(skuBarcodeList) && skuBarcodeList.size() > 1){
                for (SkuBarcodeDO skuBarcodeDO : skuBarcodeList) {
                    barCodeList.add(skuBarcodeDO.getBarcode());
                }
                logger.info("该skuId："+skuEntity.getId()+",的所有barcode"+ skuBarcodeList);
            } else {
                continue;
            }

            logger.info("已经同步的barcode："+GsonUtil.getGsonInstance().toJson(synBarCode));
            if (null!=barCodeList) {
                barCodeList.removeAll(synBarCode);//移除已同步过的barcode
                logger.info("移除已经同步的barcode后的barcode："+GsonUtil.getGsonInstance().toJson(barCodeList));
            }else{
                return null;
            }

            logger.info("(排序后)查询barcode记录表的barcode："+skuBarcodeList);
    		for (String string : barCodeList) {
    			sub = new FineExSkuSubSynRequest();
    			sub.setActionType(FineExSkuSyncCommand.ACTION_TYPE_ADD);
    			sub.setBarCode(skuBarcodeList.size()>0?String.valueOf(skuBarcodeList.get(0).getBarcode()):null);
    			sub.setSubCode(string);
    			sub.setSkuId(skuWarehouseSyncDOs.get(0).getSkuId());
    			sub.setStorehouseId(skuWarehouseSyncDOs.get(0).getWarehouseId());
    			sub.setCombination(0);
    			logger.info("得到需要同步子码的实体:"+GsonUtil.getGsonInstance().toJson(sub));
    			skuSubList.add(sub);
			}
		}
    	logger.info("最后需要同步子码的实体集合:"+GsonUtil.getGsonInstance().toJson(skuSubList));
    	return skuSubList;
	}

    public static void main(String[] args) {
        FineExSkuSynRequest request = new FineExSkuSynRequest();
        List<FineExSkuSynItem> fineExSkuSynItemList = new ArrayList<FineExSkuSynItem>();
        FineExSkuSynItem skuSynItem = new FineExSkuSynItem();
        skuSynItem.setActionType(FineExSkuSyncCommand.ACTION_TYPE_ADD);
        skuSynItem.setItemCode("1456465");
        skuSynItem.setBarCode("TEST1");
        skuSynItem.setItemName("活性炭1kg");
        skuSynItem.setCategoryName("装潢/灯具/五金/安防/卫浴");
        skuSynItem.setProperty("颜色:红色;尺寸: M ");
        skuSynItem.setBatchRule(FineExSkuSyncCommand.BATCH_RULE_PRODUCTION);
        FineExSkuSynItem skuSynItem2 = new FineExSkuSynItem();
        skuSynItem2.setActionType(FineExSkuSyncCommand.ACTION_TYPE_ADD);
        skuSynItem2.setItemCode("1456465");
        skuSynItem2.setBarCode("TEST2");
        skuSynItem2.setItemName("活性炭2kg");
        skuSynItem2.setCategoryName("装潢/灯具/五金/安防/卫浴2");
        skuSynItem2.setProperty("颜色:红色;尺寸: M2 ");
        skuSynItem2.setBatchRule(FineExSkuSyncCommand.BATCH_RULE_PRODUCTION);
        fineExSkuSynItemList.add(skuSynItem);
        fineExSkuSynItemList.add(skuSynItem2);
        request.setItems(fineExSkuSynItemList);
        try{
            String reqXml = XMLUtil.convertToXml(request);
            System.out.println("测试生成报文："+reqXml);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
