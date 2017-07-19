package com.sfebiz.supplychain.config.wms;

import com.sfebiz.supplychain.config.LogisticsDynamicConfig;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>仓库配置</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/3
 * Time: 下午1:30
 */
public class WmsConfig {

    private static final Logger logger = LoggerFactory.getLogger(WmsConfig.class);

    /**
     * 虚拟仓是否需要国内物流供应商
     */
    public final static String VM_WMS_IS_NEED_TPL = "isNeedTplOnVmWms";

    /**
     * 虚拟仓是否需要提前给BSP下单 ,默认都是需要的
     * >使用场景：针对外部供应商自主介入BSP、通过后台导入SF运单号的场景
     *
     * @param warehouseId
     * @return
     * @throws ServiceException
     */
    public static Boolean isNeedTplOnVmWms(Long warehouseId) {
        if (null == warehouseId || warehouseId <= 0) {
            return true;
        }
        try {
            String isNeedTplOnVmWms = LogisticsDynamicConfig.getWmsConfig().getRule("isNeedTplOnVmWms", warehouseId.toString());
            if (StringUtils.isNotBlank(isNeedTplOnVmWms) && "false".equalsIgnoreCase(isNeedTplOnVmWms)) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error("获取虚拟仓是否需要提前给BSP下单,返回false", e);
        }
        return true;
    }

    public static Boolean exportSimplePDF(Long warehouseId) {
        if (warehouseId == null || warehouseId < 0) {
            return false;
        }
        String ids = LogisticsDynamicConfig.getWmsConfig().getRule("exportSimplePDF", "id");
        String[] idArray = ids.split(",");
        for(int i = 0; i < idArray.length; i++) {
            if(idArray[i].equals(warehouseId.toString())){
                return true;
            }
        }
        return false;
    }

//    /**
 //     * 根据销售渠道获取仓库列表
 //     *
 //     * @param bizType
 //     * @return 返回null时表示支持所有仓库
 //     */
//    public static List<Long> getWmsListByBizType(String bizType) {
//        List<Long> lines = LineConfig.getLinesOnNotPayMerchant(bizType);
//        if (CollectionUtils.isEmpty(lines)) {
//            return null;
//        }
//        Map<Long, Void> wmsIdMap = new HashMap<Long, Void>();
//        for (Long lineId : lines) {
//            Long warehouseId = null;
//            try {
//                warehouseId = OrderSplit.getWarehouseIdByLineId(lineId);
//            } catch (Exception e) {
//                logger.error("根据线路ID获取仓库ID失败,线路ID=" + lineId, e);
//                continue;
//            }
//            if (!wmsIdMap.containsKey(warehouseId)) {
//                wmsIdMap.put(warehouseId, null);
//            }
//        }
//
//        if (wmsIdMap == null || wmsIdMap.size() == 0) {
//            return null;
//        }
//
//        List<Long> wmsIdList = new ArrayList<Long>();
//        for (Map.Entry<Long, Void> entry : wmsIdMap.entrySet()) {
//            Long wmsId = entry.getKey();
//            wmsIdList.add(wmsId);
//        }
//
//        return wmsIdList;
//    }

    /**
     * 判断仓库导单是否切到了新后台
     * @param warehouseId
     * @return
     */
    public static Boolean isChangeToNewVenderSystemForExportOrderList(Long warehouseId) {
        if (warehouseId == null || warehouseId < 0) {
            return false;
        }
        try{
            String warehouseIds = LogisticsDynamicConfig.getWmsConfig().getRule("isStopAutoExportOrder", "closeWarehouseIds");
            if (StringUtils.isNotBlank(warehouseIds)) {
                String[] warehouseIdArray = warehouseIds.split(",");
                for (String id : warehouseIdArray) {
                    if(warehouseId.toString().equalsIgnoreCase(id)){
                        return true;
                    }
                }
            }
            return false;
        }catch (Exception e){
            logger.error("获取isStopAutoExportOrder配置异常",e);
            return false;
        }

    }

}
