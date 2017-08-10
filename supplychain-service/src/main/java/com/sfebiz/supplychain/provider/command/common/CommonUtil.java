package com.sfebiz.supplychain.provider.command.common;

import com.alibaba.fastjson.JSON;
import com.sfebiz.supplychain.config.lp.LogisticsProviderConfig;
import com.sfebiz.supplychain.exposed.stockout.entity.LogisticsClearanceDetailEntity;
import com.sfebiz.supplychain.protocol.bsp.*;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.entity.BspServiceCode;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.ListUtil;
import net.pocrd.util.Md5Util;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 命令通用类
 * <p/>
 * Created by wang_cl on 2015/3/11.
 */
public class CommonUtil {

    public static final Integer YUAN_2_FEN_UNIT = Integer.valueOf(100);
    private static final Logger logger = LoggerFactory.getLogger("CommandLogger");


    static {
        ConvertUtils.register(new LongConverter(null), Long.class);
        ConvertUtils.register(new ShortConverter(null), Short.class);
        ConvertUtils.register(new IntegerConverter(null), Integer.class);
        ConvertUtils.register(new DoubleConverter(null), Double.class);
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
    }

    /**
     * 检查 BSP 运单号是否有路由信息
     *
     * @return
     */
    public static BSPRoute checkBspHasRoute(String bizId, String mailNo) {
        // 指定国外物流供应商中的meta信息
        Map<String, Object> meta = JSONUtil.parseJSONMessage(LogisticsProviderConfig.getBspMeta());
        BSPRequest req = new BSPRequest();
        req.service = BspServiceCode.ORDER_ROUTE;
        if (meta.containsKey("lang")) {
            req.lang = String.valueOf(meta.get("lang"));
        } else {
            req.lang = "";
        }
        req.header = LogisticsProviderConfig.getBspCode();
        BSPRouteRequest route = new BSPRouteRequest();
        req.getBody().getBody().add(route);
        route.methodType = 1;
        if (!StringUtils.isBlank(mailNo)) {
            route.trackingType = 1;
            route.trackingNumber = mailNo;
        } else {
            return null;
        }
        BSPResponse resp = ProviderBizService.getInstance().sendBSPRequest(
                LogisticsProviderConfig.getBspInterfaceUrl(),
                LogisticsProviderConfig.getBspInterfaceKey(),
                req, bizId);

        if (resp != null && BSPReturnCode.SUCCESS.getCode().equalsIgnoreCase(resp.getHeader())
                && resp.getBody().getBody().size() > 0) {
            BSPBody body = resp.getBody().getBody().get(0);
            if (body instanceof BSPRouteResponse) {
                BSPRouteResponse routeResponse = (BSPRouteResponse) body;
                if (routeResponse.getRoute().size() > 0) {
                    return routeResponse.getRoute().get(0);
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 需要对sku进行merge处理，如果存在批次，则根据sku+batch维度进行合并。
     *
     * @param stockoutOrderDetailBOS
     * @param isSupportBatch      是否支持批次，如果支持，则按照sku+batch维度进行合并，不支持按照sku维度。
     * @return
     */
    public static List<StockoutOrderDetailBO> mergeStockoutOrderSku(List<StockoutOrderDetailBO> stockoutOrderDetailBOS, Boolean isSupportBatch) {
        List<StockoutOrderDetailBO> resultList = new ArrayList<StockoutOrderDetailBO>();
        HashMap<String, StockoutOrderDetailBO> map = new HashMap();
        for (StockoutOrderDetailBO stockoutOrderSkuBO : stockoutOrderDetailBOS) {
            String mergeKey = stockoutOrderSkuBO.getSkuId().toString();
            if (isSupportBatch && StringUtils.isNotBlank(stockoutOrderSkuBO.getSkuBatch())) {
                mergeKey = mergeKey.concat("_").concat(stockoutOrderSkuBO.getSkuBatch());
            }
            if (map.containsKey(mergeKey)) {
                StockoutOrderDetailBO exitsStockoutOrderSkuBO = map.get(mergeKey);
                exitsStockoutOrderSkuBO.setQuantity(exitsStockoutOrderSkuBO.getQuantity() + exitsStockoutOrderSkuBO.getQuantity());
            } else {
                StockoutOrderDetailBO newStockoutOrderSkuBO = new StockoutOrderDetailBO();
                BeanUtils.copyProperties(stockoutOrderSkuBO, newStockoutOrderSkuBO);
                map.put(mergeKey, newStockoutOrderSkuBO);
            }
        }
        for (StockoutOrderDetailBO stockoutOrderSkuBO : map.values()) {
            resultList.add(stockoutOrderSkuBO);
        }

        return resultList;
    }

    /**
     * 需要对sku进行merge处理，如果存在批次，则根据sku+batch维度进行合并。
     *
     * @param stockoutOrderSkuDOs
     * @param isSupportBatch      是否支持批次，如果支持，则按照sku+batch维度进行合并，不支持按照sku维度。
     * @return
     */
//    public static List<StockinOrderSkuDO> mergeStockinOrderSku(List<StockinOrderSkuDO> stockoutOrderSkuDOs, Boolean isSupportBatch) {
//        List<StockinOrderSkuDO> resultList = new ArrayList<StockinOrderSkuDO>();
//        HashMap<String, StockinOrderSkuDO> map = new HashMap();
//        for (StockinOrderSkuDO stockinOrderSkuDO : stockoutOrderSkuDOs) {
//            String mergeKey = stockinOrderSkuDO.getSkuId().toString();
//            if (isSupportBatch && StringUtils.isNotBlank(stockinOrderSkuDO.getSkuBatch())) {
//                mergeKey = mergeKey.concat("_").concat(stockinOrderSkuDO.getSkuBatch());
//            }
//            if (map.containsKey(mergeKey)) {
//                StockinOrderSkuDO exitsStockoutOrderSkuDo = map.get(mergeKey);
//                exitsStockoutOrderSkuDo.setCount(exitsStockoutOrderSkuDo.getCount() + stockinOrderSkuDO.getCount());
//            } else {
//                StockinOrderSkuDO newStockinOrderskuD0 = new StockinOrderSkuDO();
//                BeanUtils.copyProperties(stockinOrderSkuDO, newStockinOrderskuD0);
//                map.put(mergeKey, newStockinOrderskuD0);
//            }
//        }
//        for (StockinOrderSkuDO stockinOrderSkuDO : map.values()) {
//            resultList.add(stockinOrderSkuDO);
//        }
//
//        return resultList;
//    }

//    /**
//     * 口岸和BSP是不支持组合商品的
//     * 所以需要将组合商品过滤掉
//     *
//     * @return
//     */
//    public static List<StockoutOrderSkuDO> removeMixedSku(List<StockoutOrderSkuDO> origin) {
//        List<StockoutOrderSkuDO> resultAfterFilter = new ArrayList<StockoutOrderSkuDO>();
//        for (StockoutOrderSkuDO stockoutOrderSkuDO : origin) {
//            if (stockoutOrderSkuDO.getSkuType() != SkuType.MIX_SKU.value) {
//                resultAfterFilter.add(stockoutOrderSkuDO);
//            } else {
//                LogBetter.instance(logger)
//                        .setLevel(LogLevel.WARN)
//                        .setMsg("[供应链报文-构建出库单商品-BSP-口岸不支持组合商品,跳过组合商品]:")
//                        .addParm("skuId", stockoutOrderSkuDO.getSkuId())
//                        .log();
//            }
//        }
//        return resultAfterFilter;
//    }

    /**
     * 根据仓库是否支持组合商品过滤商品列表
     *
     * @return
     */
//    public static List<StockoutOrderSkuDO> filterStockoutOrderSku(List<StockoutOrderSkuDO> origin, boolean isWarehouseSupportCombination) {
//        List<StockoutOrderSkuDO> resultAfterFilter = new ArrayList<StockoutOrderSkuDO>();
//        // 如果仓库支持组合商品,那么就只发组合商品
//        // 如果仓库不支持组合商品,那么就过滤掉组合商品
//        if (isWarehouseSupportCombination) {
//            for (StockoutOrderSkuDO stockoutOrderSkuDO : origin) {
//                if (stockoutOrderSkuDO.getSkuType() != SkuType.BASIC_SKU_OF_MIX_SKU.value) {
//                    resultAfterFilter.add(stockoutOrderSkuDO);
//                } else {
//                    LogBetter.instance(logger)
//                            .setMsg("[供应链报文-构建出库单商品-仓库支持组合商品,跳过组合商品中的基本商品]:")
//                            .addParm("skuId", stockoutOrderSkuDO.getSkuId())
//                            .log();
//                }
//            }
//        } else {
//            for (StockoutOrderSkuDO stockoutOrderSkuDO : origin) {
//                if (stockoutOrderSkuDO.getSkuType() != SkuType.MIX_SKU.value) {
//                    resultAfterFilter.add(stockoutOrderSkuDO);
//                } else {
//                    LogBetter.instance(logger)
//                            .setMsg("[供应链报文-构建出库单商品-仓库不支持组合商品,跳过组合商品]:")
//                            .addParm("skuId", stockoutOrderSkuDO.getSkuId())
//                            .log();
//                }
//            }
//        }
//        return resultAfterFilter;
//    }

    /**
     * 忽略批次，获取基本SKUID维度商品数
     *
     * @param origin
     * @return
     */
    public static int getBasicSkuCount(List<StockoutOrderDetailBO> origin) {
        int totalCount = 0;
        List<StockoutOrderDetailBO> basicSkuList = mergeStockoutOrderSku(origin, Boolean.FALSE);
        if (ListUtil.isNotEmpty(basicSkuList)) {
            for (StockoutOrderDetailBO s : basicSkuList) {
                totalCount = totalCount + s.getQuantity();
            }
        }
        return totalCount;
    }

    /**
     * 将金额元转为分
     *
     * @param moneyYuan
     * @return
     */
    public static Integer castMoneyYuanToFen(Integer moneyYuan) {
        if (null == moneyYuan) {
            return 0;
        }
        return moneyYuan * YUAN_2_FEN_UNIT;
    }

    /**
     * 返回统一的货主代码
     *
     * @return
     */
    public static String getCustomerCode() {
        return "SFHT";
    }

    /**
     * 根据路线上的清关供应商配置清关详情实体
     * >这里面的清关供应商除了完成清关外，并完成国内的包裹配送。
     *
     * @param line
     * @param stockoutOrderBO
     * @return
     */
    public static LogisticsClearanceDetailEntity buildClearanceDetailEntity(LogisticsLineBO line, StockoutOrderBO stockoutOrderBO) {
        if (line == null || stockoutOrderBO == null) {
            throw new IllegalArgumentException("路线或者出库单不能为空");
        }
        if (StringUtils.isBlank(line.getRouteCode())) {
            throw new IllegalArgumentException("路线中RouteCode不能为空");
        }
        LogisticsClearanceDetailEntity clearanceDetailEntity = new LogisticsClearanceDetailEntity();
        clearanceDetailEntity.orderId = stockoutOrderBO.getBizId();
        clearanceDetailEntity.carrierCode = line.getRouteCode();
        clearanceDetailEntity.mailNo = stockoutOrderBO.getIntrMailNo();
//        clearanceDetailEntity.shipperCode = stockoutOrderDO.getOrigincode();
        clearanceDetailEntity.deliveryCode = stockoutOrderBO.getDestcode();
        clearanceDetailEntity.senderAddress = line.getWarehouseBO().getSenderBO().getSenderAddress();
//        clearanceDetailEntity.custId = line.custId;
        clearanceDetailEntity.payMethod = "寄付月结";
        if (line.getClearanceLogisticsProviderBO() != null && StringUtils.isNotBlank(line.getClearanceLogisticsProviderBO().getInterfaceMeta().get("meta"))) {
            String meta = line.getClearanceLogisticsProviderBO().getInterfaceMeta().get("meta");
            Map<String, String> metaMap = JSON.parseObject(meta, Map.class);
            if (null != meta) {
                clearanceDetailEntity.logo = metaMap.get("logo");
            }
        }

        return clearanceDetailEntity;
    }


    /**
     * 由于日本关键字在商品名称中处于敏感词，所以在部分清关字段中需要移除
     *
     * @param originSkuName
     * @return
     */
    public static String removeJapanWordOnSkuName(String originSkuName) {
        if (StringUtils.isBlank(originSkuName)) {
            return originSkuName;
        } else {
            String finalSkuName = originSkuName.replace("日本", "");
            finalSkuName = finalSkuName.replace("日 本", "");
            return finalSkuName;
        }
    }


    /**
     * 获取购买人姓名
     *
     * @param stockoutOrderBO
     * @return
     */
    public static String getBuyerName(StockoutOrderBO stockoutOrderBO) {
        String userName = stockoutOrderBO.getDeclarePayerName();
        if (StringUtils.isBlank(userName)) {
            userName = stockoutOrderBO.getBuyerBO().getBuyerName();
        }
        return userName;
    }

    /**
     * 获取购买人身份证号码
     *
     * @param stockoutOrderBO
     * @return
     */
    public static String getBuyerIdNo(StockoutOrderBO stockoutOrderBO) {
        String userIdNo = stockoutOrderBO.getDeclarePayerCertNo();
        if (StringUtils.isBlank(userIdNo)) {
            userIdNo = stockoutOrderBO.getBuyerBO().getBuyerCertNo();
        }
        return userIdNo;
    }


    /**
     * 获取购买人注册号
     *
     * @param buyerName
     * @param buyerIdNo
     * @return
     */
    public static String getBuyerRegNo(String buyerName, String buyerIdNo) {
        if (StringUtils.isBlank(buyerName) || StringUtils.isBlank(buyerIdNo)) {
            throw new IllegalArgumentException("购买人姓名或者身份证号不能为空");
        }

        String regNo = Md5Util.computeToHex(String.valueOf(buyerName + buyerIdNo).getBytes()).toLowerCase();
        return regNo;
    }

    /**
     * 获取收件人姓名
     *
     * @param stockoutOrderBO
     * @return
     */
    public static String getConsigneeName(StockoutOrderBO stockoutOrderBO) {
        return stockoutOrderBO.getBuyerBO().getBuyerName();
    }


    /**
     * 获取收件人身份证号码
     *
     * @param stockoutOrderBO
     * @return
     */
    public static String getConsigneeIdNo(StockoutOrderBO stockoutOrderBO) {
        return stockoutOrderBO.getBuyerBO().getBuyerCertNo();
    }

    /**
     * 获取收件人的完整的收货地址
     *
     * @param stockoutOrderBO
     * @return
     */
    public static String getConsigneeFullAddress(StockoutOrderBO stockoutOrderBO) {
        StringBuilder fullAddress = new StringBuilder();
        fullAddress.append(stockoutOrderBO.getBuyerBO().getBuyerCountry());
        fullAddress.append(stockoutOrderBO.getBuyerBO().getBuyerProvince());
        fullAddress.append(stockoutOrderBO.getBuyerBO().getBuyerCity());
        fullAddress.append(stockoutOrderBO.getBuyerBO().getBuyerRegion());
        fullAddress.append(stockoutOrderBO.getBuyerBO().getBuyerAddress());

        return fullAddress.toString();
    }


    /**
     * 替代 BeanUtils.copyProperties 方法，解决封装数值类型为null时被转换为了0
     * 在本类中增加了静态初始化块
     *
     * @param dest
     * @param orig
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void copyProperties(Object dest, Object orig) throws IllegalAccessException, InvocationTargetException {
        org.apache.commons.beanutils.BeanUtils.copyProperties(dest, orig);
    }

}
