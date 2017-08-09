package com.sfebiz.supplychain.service.stockout.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.LogisticsDynamicConfig;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.persistence.base.sku.manager.ProductDeclareManager;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDeclarePriceDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDeclarePriceDetailDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderDeclarePriceDetailManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderDeclarePriceManager;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.sku.model.SkuDeclareBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceDetailBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.convert.StockoutOrderConvert;
import com.sfebiz.supplychain.service.stockout.process.create.DataPrepareProcessor;

/**
 * <p>运费&税费拆分服务</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/6/9
 * Time: 上午11:08
 */
@Component("feeSplitBizService")
public class FeeSplitBizService {

    private static final Logger                    logger        = LoggerFactory
                                                                     .getLogger(FeeSplitBizService.class);
    private static final Logger                    commondLogger = LoggerFactory
                                                                     .getLogger("CommandLogger");
    private static final HaitaoTraceLogger         traceLogger   = HaitaoTraceLoggerFactory
                                                                     .getTraceLogger("order");

    @Resource
    private StockoutOrderDeclarePriceManager       stockoutOrderDeclarePriceManager;
    @Resource
    private StockoutOrderDeclarePriceDetailManager stockoutOrderDeclarePriceDetailManager;
    @Resource
    private ProductDeclareManager                  productDeclareManager;
    @Resource
    private DataPrepareProcessor                   dataPrepareProcessor;

    /**
     * 判断 number 是否是数字
     *
     * @param number
     * @return
     */
    private static boolean isNumber(String number) {
        String reg = "^\\d+$";
        return Pattern.compile(reg).matcher(number).find();
    }

    /**
     * 计算平均运费，只针对那些能够承担得起运费的商品
     *
     * @param stockoutOrderSkuDOs 商品列表
     * @param dropOutSkuMap       承担不起运费的商品列表（减去平均运费后商品单价为负数）
     * @param defaultFreight      系统配置的默认总运费
     * @return 计算出来的能承担运费的SKU需要平摊的运费
     */
    private static Integer calculateAverageFreightOnHighPriceSku(List<StockoutOrderDetailBO> stockoutOrderSkuDOs,
                                                                 Map<Long, Void> dropOutSkuMap,
                                                                 Integer defaultFreight) {
        int skuTotalCount = 0;
        for (StockoutOrderDetailBO skuBO : stockoutOrderSkuDOs) {
            if (dropOutSkuMap.containsKey(skuBO.getSkuId())) {
                continue;
            }
            skuTotalCount = skuTotalCount + skuBO.getQuantity();
            if (skuBO.getMerchantDiscountPrice() == null) {
                skuBO.setMerchantDiscountPrice(0);
            }
        }
        if (skuTotalCount == 0) {
            return 0;
        }
        //平摊到单个商品上的运费 （保留到角）
        Integer averageFreightOnSingleSku = new BigDecimal(defaultFreight)
            .divide(new BigDecimal(10), 0, BigDecimal.ROUND_UP)
            .divide(new BigDecimal(skuTotalCount), 0, BigDecimal.ROUND_UP).intValue();
        averageFreightOnSingleSku = averageFreightOnSingleSku * 10;

        for (StockoutOrderDetailBO detailBO : stockoutOrderSkuDOs) {
            if (dropOutSkuMap.containsKey(detailBO.getSkuId())) {
                continue;
            }
            if (detailBO.getMerchantPriceRoundDown() == null
                || detailBO.getMerchantPriceRoundDown() <= 0) {
                detailBO.setMerchantPriceRoundDown(detailBO.getMerchantPrice());
            }
            int skuDeclarePrice = detailBO.getMerchantPriceRoundDown() - averageFreightOnSingleSku;
            // 因为商品备案价格的精度保存到角，如果到分会自动舍去，所以这里需要需要大于1角
            if (skuDeclarePrice < 10) {
                dropOutSkuMap.put(detailBO.getSkuId(), null);
                return calculateAverageFreightOnHighPriceSku(stockoutOrderSkuDOs, dropOutSkuMap,
                    defaultFreight);
            }

        }
        return averageFreightOnSingleSku;
    }

    /**
     * 根据商品个数，计算每个商品需要承担的运费
     *
     * @param detailBOs 商品列表
     * @param freightFee          用户承担的运费
     * @return 计算出来的SKU需要平摊的运费
     */
    private static Integer averageFreightFeeOnSkus(List<StockoutOrderDetailBO> detailBOs,
                                                   Integer freightFee) {
        int skuTotalCount = 0;
        if (freightFee == null || freightFee == 0) {
            return 0;
        }
        for (StockoutOrderDetailBO detailDO : detailBOs) {
            skuTotalCount = skuTotalCount + detailDO.getQuantity();
        }
        if (skuTotalCount == 0) {
            return 0;
        }
        //平摊到单个商品上的运费
        Integer averageFreightOnSingleSku = new BigDecimal(freightFee).divide(
            new BigDecimal(skuTotalCount), 0, BigDecimal.ROUND_DOWN).intValue();
        return averageFreightOnSingleSku;
    }

    /**
     * 不进行运费税费的拆分 ，针对不走口岸的场景，比如一件代发
     * 但是会对同一个SKUID的商品价格进行平均，如果除不尽的部分，则会计算到运费中
     *
     * @param stockoutOrderBO
     * @param detailBOs
     * @return
     */
    public StockoutOrderDeclarePriceBO notCalculatPriceSplit(StockoutOrderBO stockoutOrderBO,
                                                             List<StockoutOrderDetailBO> detailBOs,
                                                             LogisticsLineBO lineBO, String reason)
                                                                                                   throws ServiceException {

        Map<Long, Integer> skuDeclarePriceMap = new HashMap<Long, Integer>();

        // SKU的平均销售价,需要在SKU进行Merge之前计算
        Map<Long, Integer> skuAverageMerchantPriceMap = calculationBasicSkuAverageSellingPriceNotActivityDiscount(
            stockoutOrderBO, detailBOs);
        Map<Long, Integer> skuAverageActivityDiscountPriceMap = calculationBasicSkuAverageActivityDiscountPrice(
            stockoutOrderBO, detailBOs);

        detailBOs = CommonUtil.mergeStockoutOrderSku(detailBOs, Boolean.FALSE);

        // 如果是销售订单，关心运费
        int freightFee = 0;
        if (StockoutOrderType.SALES_STOCK_OUT.getValue() == stockoutOrderBO.getOrderType()) {
            if (stockoutOrderBO.getUserFreightFee() > 0) {
                freightFee = stockoutOrderBO.getUserFreightFee();
            }
        }

        //如果运费不为0，则需要将运费平坦到商品信息中
        Integer averageFreightOnSingleSku = averageFreightFeeOnSkus(detailBOs, freightFee);
        int declareTotalPrice = 0;
        //总活动折扣
        int totalActivityDiscountPrice = 0;
        for (StockoutOrderDetailBO skuDO : detailBOs) {
            int averageprice = skuAverageMerchantPriceMap.get(skuDO.getSkuId());
            averageprice = averageprice + averageFreightOnSingleSku;
            if (averageprice == 0) {
                // 平均价格为0时，表示为赠品，所以设置为1分钱；
                averageprice = 1;
            }
            skuDeclarePriceMap.put(skuDO.getSkuId(), averageprice);
            totalActivityDiscountPrice = totalActivityDiscountPrice
                                         + skuAverageActivityDiscountPriceMap.get(skuDO.getSkuId())
                                         * skuDO.getQuantity();
            declareTotalPrice = declareTotalPrice + averageprice * skuDO.getQuantity();
        }

        //运费平摊后，重新将运费设置为0
        freightFee = 0;
        int taxFee = 0;
        Integer userGoodsPrice = declareTotalPrice;
        //刨除活动折扣外的折扣总和
        Integer userDiscountPrice = stockoutOrderBO.getUserDiscountPrice()
                                    - totalActivityDiscountPrice;

        if (userDiscountPrice < 0) {
            userDiscountPrice = 0;
        }

        //在用户使用积分只支付一分钱的情况下，由于商品单价向下取证，导致货款-折扣会小于0，所以此场景下需要重置折扣金额
        if (userDiscountPrice >= userGoodsPrice) {
            userDiscountPrice = userGoodsPrice - 1;
        }

        //实际支付金额：订单金额需大于等于支付单金额，订单与申报单金额需一致
        Integer userOrderTotal = userGoodsPrice + freightFee + taxFee - userDiscountPrice;

        if (userGoodsPrice < userOrderTotal) {
            LogBetter.instance(commondLogger).setLevel(LogLevel.ERROR).setMsg("[供应链-计算申报金额异常]")
                .addParm("包裹ID", stockoutOrderBO.getBizId()).addParm("申报运费", freightFee)
                .addParm("申报税费", taxFee).addParm("申报折扣", userDiscountPrice)
                .addParm("申报实际支付", userOrderTotal).addParm("申报总金额", declareTotalPrice)
                .addParm("申报订单总金额", userGoodsPrice + freightFee + taxFee)
                .addParm("申报货款", userGoodsPrice).addParm("申报商品单价", skuDeclarePriceMap).log();
            throw new IllegalArgumentException("订单申报货款不能小于实际支付");
        }

        if (userOrderTotal < 0 || userDiscountPrice < 0) {
            LogBetter.instance(commondLogger).setLevel(LogLevel.ERROR).setMsg("[供应链-计算申报金额异常]")
                .addParm("包裹ID", stockoutOrderBO.getBizId()).addParm("申报运费", freightFee)
                .addParm("申报税费", taxFee).addParm("申报折扣", userDiscountPrice)
                .addParm("申报实际支付", userOrderTotal).addParm("申报总金额", declareTotalPrice)
                .addParm("申报订单总金额", userGoodsPrice + freightFee + taxFee)
                .addParm("申报货款", userGoodsPrice).addParm("申报商品单价", skuDeclarePriceMap).log();
            throw new IllegalArgumentException("订单实际支付 或 申报折扣 不能小于零");
        }

        if (userOrderTotal > stockoutOrderBO.getUserOrderTotalAmount()) {
            LogBetter.instance(commondLogger).setLevel(LogLevel.WARN).setMsg("[供应链-计算申报金额异常]")
                .addParm("包裹ID", stockoutOrderBO.getBizId()).addParm("申报运费", freightFee)
                .addParm("申报税费", taxFee).addParm("申报折扣", userDiscountPrice)
                .addParm("申报实际支付", userOrderTotal).addParm("申报总金额", declareTotalPrice)
                .addParm("申报订单总金额", userGoodsPrice + freightFee + taxFee)
                .addParm("申报货款", userGoodsPrice).addParm("申报商品单价", skuDeclarePriceMap).log();

            userOrderTotal = stockoutOrderBO.getUserOrderTotalAmount();
        }

        //如果包裹实付金额大于5。为防止申报金额超限，-5，如果最后一个包裹的金额是0就把最后一个包裹金额+1用于申报
        /*if (userOrderTotal > 5) {
        	userOrderTotal = userOrderTotal - 5;
        }*/
        if (userOrderTotal == 0 && userGoodsPrice > 0) {
            userOrderTotal = 1;
        }

        StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceBO = new StockoutOrderDeclarePriceBO();
        stockoutOrderDeclarePriceBO.setFreightFee(freightFee);
        stockoutOrderDeclarePriceBO.setTaxFee(taxFee);
        stockoutOrderDeclarePriceBO.setDiscountTotalPrice(userDiscountPrice);
        stockoutOrderDeclarePriceBO.setOrderActualPrice(userOrderTotal);
        stockoutOrderDeclarePriceBO.setDeclareTotalPrice(declareTotalPrice);
        stockoutOrderDeclarePriceBO.setOrderTotalPrice(userGoodsPrice + freightFee + taxFee);
        stockoutOrderDeclarePriceBO.setGoodsTotalPrice(userGoodsPrice);

        stockoutOrderDeclarePriceBO.setSkuDeclarePriceMap(skuDeclarePriceMap);

        saveStockoutOrderDeclarePrice(stockoutOrderBO,
            stockoutOrderDeclarePriceBO.getOrderActualPrice(),
            stockoutOrderDeclarePriceBO.getGoodsTotalPrice(),
            stockoutOrderDeclarePriceBO.getOrderTotalPrice(),
            stockoutOrderDeclarePriceBO.getDeclareTotalPrice(),
            stockoutOrderDeclarePriceBO.getFreightFee(), stockoutOrderDeclarePriceBO.getTaxFee(),
            stockoutOrderDeclarePriceBO.getDiscountTotalPrice());
        saveStockoutOrderSkuDeclarePrice(stockoutOrderBO, detailBOs, skuDeclarePriceMap);

        LogBetter
            .instance(logger)
            .setLevel(LogLevel.INFO)
            .setTraceLogger(
                TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                    SystemConstants.TRACE_APP))
            .setMsg("[供应链-运费税费拆分(不拆分)]订单: " + stockoutOrderBO.getBizId()).addParm("不拆分原因", reason)
            .addParm("订单金额数据", stockoutOrderDeclarePriceBO).addParm("订单商品申报价", skuDeclarePriceMap)
            .log();
        return stockoutOrderDeclarePriceBO;
    }

    /**
     * 针对广州2.0海关的计算各个价格信息
     * （完税价格：售价中抛出税和运费的价格）
     * 应征关税 = 完税价格 *关税税率（关税税率为0%，因此应征关税为0）；
     * 法定计征的消费税 =（完税价格/(1-消费税税率)）*消费税税率；
     * 法定计征的增值税 =（完税价格+正常计征的消费税税额）*增值税税率；
     * 应征消费税 = 法定计征的消费税*0.7；
     * 应征增值税 = 法定计征的增值税*0.7
     * <p/>
     * 运杂费 = 运费 + 保费
     * 完税价格 = 申报货款 + 运费 + 保费
     *
     * @param stockoutOrderBO
     * @param stockoutOrderSkuDOs
     * @param lineEntity
     * @param skuDeclareEntityMap
     * @return
     * @throws ServiceException
     */
    public StockoutOrderDeclarePriceBO calculatPriceDeclarePriceOnGuangzhouVersion2(StockoutOrderBO stockoutOrderBO,
                                                                                    List<StockoutOrderDetailBO> stockoutOrderSkuDOs,
                                                                                    LogisticsLineBO lineEntity,
                                                                                    Map<Long, SkuDeclareBO> skuDeclareEntityMap)
                                                                                                                                throws ServiceException {
        Map<Long, Integer> skuDeclarePriceMap = new HashMap<Long, Integer>();
        Map<Long, StockoutOrderDeclarePriceDetailBO> skuDeclarePriceDetailMap = new HashMap<Long, StockoutOrderDeclarePriceDetailBO>();
        //SKU的平均销售价,需要在SKU进行Merge之前计算
        Map<Long, Integer> skuAverageMerchantPriceMap = calculationBasicSkuAverageSellingPriceNotAllDiscount(
            stockoutOrderBO, stockoutOrderSkuDOs);
        stockoutOrderSkuDOs = CommonUtil.mergeStockoutOrderSku(stockoutOrderSkuDOs, Boolean.FALSE);

        Integer freightFee = 0;

        int declareTotalGoodsPrice = 0; //申报总货值
        int tariffTotalPrice = 0;//关税，目前全部为0
        int consumptionDutyTotalPrice = 0;//总消费税
        int addedValueTaxTotalPrice = 0;//总增值税
        BigDecimal taxDisCountRate = new BigDecimal("0.7");
        for (StockoutOrderDetailBO skuDO : stockoutOrderSkuDOs) {
            if (skuDO.getMerchantPriceRoundDown() == null || skuDO.getMerchantPriceRoundDown() <= 0) {
                skuDO.setMerchantPriceRoundDown(skuDO.getMerchantPrice());
            }
            SkuDeclareBO productDeclareEntity = skuDeclareEntityMap.get(skuDO.getSkuId());
            if (productDeclareEntity == null
                || StringUtils.isBlank(productDeclareEntity.getConsumptionDutyRate())
                || StringUtils.isBlank(productDeclareEntity.getAddedValueTaxRate())) {
                LogBetter
                    .instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(
                        TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                            SystemConstants.TRACE_APP))
                    .addParm("[供应链-运费税费拆分]商品备案信息不存在或者商品税率为空，sku信息：", skuDO)
                    .addParm("备案信息", productDeclareEntity).log();
                throw new ServiceException(LogisticsReturnCode.SKU_DECLARE_NOT_EXIST,
                    "商品备案信息不存在或者商品税率为空，sku信息：" + skuDO + "，口岸："
                            + lineEntity.getPortBO().getPortNid());
            }

            BigDecimal consumptionDutyRate = new BigDecimal(
                productDeclareEntity.getConsumptionDutyRate());
            BigDecimal addedValueTaxRate = new BigDecimal(
                productDeclareEntity.getAddedValueTaxRate());
            if (consumptionDutyRate.doubleValue() < 0 || consumptionDutyRate.doubleValue() >= 1) {
                LogBetter
                    .instance(commondLogger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(
                        TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                            SystemConstants.TRACE_APP))
                    .setMsg(
                        "[供应链-运费税费拆分]商品消费税税率不合法，sku信息：" + skuDO + ",消费税税率：" + consumptionDutyRate)
                    .log();
                throw new ServiceException(LogisticsReturnCode.SKU_DECLARE_TAXRATE_ILLEGAL,
                    "商品消费税税率不合法，sku信息：" + skuDO + ",消费税税率：" + consumptionDutyRate + "，口岸："
                            + lineEntity.getPortBO().getPortNid());
            }
            if (addedValueTaxRate.doubleValue() < 0 || addedValueTaxRate.doubleValue() >= 1) {
                LogBetter
                    .instance(commondLogger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(
                        TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                            SystemConstants.TRACE_APP))
                    .setMsg("[供应链-运费税费拆分]商品增值税税率不合法，sku信息：" + skuDO + ",增值税税率：" + addedValueTaxRate)
                    .log();
                throw new ServiceException(LogisticsReturnCode.SKU_DECLARE_TAXRATE_ILLEGAL,
                    "商品税增值税税率不合法，sku信息：" + skuDO + ",增值税税率：" + addedValueTaxRate + "，口岸："
                            + lineEntity.getPortBO().getPortNid());
            }

            int skuContainTaxOriginPrice = skuAverageMerchantPriceMap.get(skuDO.getSkuId());

            //算税因子： 1 + c*（a + b ）/(1-a) , a 为消费税税率、b 增值税税率、c 折扣率
            BigDecimal molecule = taxDisCountRate.multiply(consumptionDutyRate
                .add(addedValueTaxRate));
            BigDecimal denominator = new BigDecimal(1).subtract(consumptionDutyRate);
            BigDecimal taxFactor = molecule.divide(denominator, 4, BigDecimal.ROUND_DOWN);
            taxFactor = new BigDecimal(1).add(taxFactor);

            String config = LogisticsDynamicConfig.getSplit().getRule("bptaxhscode",
                "firstMeasuringUnit");
            String config1 = LogisticsDynamicConfig.getSplit().getRule("bptaxhscode",
                "secondMeasuringUnit");
            List<String> firstWeightHsCodeList = Arrays.asList(config.split(","));
            List<String> secondWeightHsCodeList = Arrays.asList(config1.split(","));
            BigDecimal skuContainTaxOriginPriceRate = new BigDecimal(skuContainTaxOriginPrice);
            logger.info("skuContainTaxOriginPrice" + skuContainTaxOriginPrice + ",hsCode"
                        + productDeclareEntity.getHsCode() + ",FirstWeight"
                        + productDeclareEntity.getFirstWeight() + ",SecondWeight"
                        + productDeclareEntity.getSecondWeight());
            if (firstWeightHsCodeList.contains(productDeclareEntity.getHsCode().toString())) {
                BigDecimal criterionTax = new BigDecimal(skuContainTaxOriginPrice)
                    .divide(new BigDecimal(productDeclareEntity.getFirstWeight()), 0,
                        BigDecimal.ROUND_DOWN).divide(new BigDecimal(1000000));
                if (criterionTax.compareTo(taxFactor) > 0) {
                    consumptionDutyRate = new BigDecimal(0.15);
                    molecule = taxDisCountRate
                        .multiply(new BigDecimal(0.15).add(addedValueTaxRate));
                    denominator = new BigDecimal(1).subtract(new BigDecimal(0.15));
                    taxFactor = molecule.divide(denominator, 4, BigDecimal.ROUND_DOWN).add(
                        new BigDecimal(1));
                    if (criterionTax.compareTo(taxFactor) < 0) {
                        skuContainTaxOriginPriceRate = new BigDecimal(1000000).multiply(
                            new BigDecimal(productDeclareEntity.getFirstWeight())).multiply(
                            taxFactor);
                    }
                }

            }
            if (secondWeightHsCodeList.contains(productDeclareEntity.getHsCode().toString())) {
                BigDecimal criterionTax = new BigDecimal(skuContainTaxOriginPrice).divide(
                    new BigDecimal(productDeclareEntity.getSecondWeight()), 0,
                    BigDecimal.ROUND_DOWN).divide(new BigDecimal(1500), 4, BigDecimal.ROUND_DOWN);
                if (criterionTax.compareTo(taxFactor) > 0) {
                    consumptionDutyRate = new BigDecimal(0.15);
                    molecule = taxDisCountRate
                        .multiply(new BigDecimal(0.15).add(addedValueTaxRate));
                    denominator = new BigDecimal(1).subtract(new BigDecimal(0.15));
                    taxFactor = molecule.divide(denominator, 4, BigDecimal.ROUND_DOWN).add(
                        new BigDecimal(1));
                    if (criterionTax.compareTo(taxFactor) < 0) {
                        skuContainTaxOriginPriceRate = new BigDecimal(1500).multiply(
                            new BigDecimal(productDeclareEntity.getSecondWeight())).multiply(
                            taxFactor);
                    }
                }
            }

            //售价中拆除税费
            Integer skuDeclarePrice = skuContainTaxOriginPriceRate.divide(taxFactor, 0,
                BigDecimal.ROUND_DOWN).intValue();
            skuDeclarePrice = skuDeclarePrice == 0 ? 1 : skuDeclarePrice;
            skuDeclarePriceMap.put(skuDO.getSkuId(), skuDeclarePrice);
            declareTotalGoodsPrice = declareTotalGoodsPrice
                                     + Long.valueOf(skuDeclarePrice * skuDO.getQuantity())
                                         .intValue();
            logger.info("skuId:" + skuDO.getSkuId() + ",申报金额：" + declareTotalGoodsPrice + ",售价："
                        + skuContainTaxOriginPrice);
            //计算增值税的时候需要使用未打折扣的消费税
            int consumptionDutySinglePriceNoDisCount = new BigDecimal(skuDeclarePrice)
                .multiply(consumptionDutyRate)
                .divide(new BigDecimal(1).subtract(consumptionDutyRate), 0, BigDecimal.ROUND_DOWN)
                .intValue();
            int addedValueTaxSinglePrice = new BigDecimal(skuDeclarePrice
                                                          + consumptionDutySinglePriceNoDisCount)
                .multiply(addedValueTaxRate).multiply(taxDisCountRate).intValue();
            addedValueTaxTotalPrice = addedValueTaxTotalPrice + addedValueTaxSinglePrice
                                      * skuDO.getQuantity();

            logger.info("增值税：" + addedValueTaxTotalPrice);
            int consumptionDutySinglePrice = taxDisCountRate.multiply(
                new BigDecimal(consumptionDutySinglePriceNoDisCount)).intValue();
            consumptionDutyTotalPrice = consumptionDutyTotalPrice + consumptionDutySinglePrice
                                        * skuDO.getQuantity();

            logger.info("消费税：" + consumptionDutyTotalPrice);
            StockoutOrderDeclarePriceDetailBO stockoutOrderSkuDeclarePriceBO = new StockoutOrderDeclarePriceDetailBO();
            stockoutOrderSkuDeclarePriceBO.setBizId(stockoutOrderBO.getBizId());
            stockoutOrderSkuDeclarePriceBO.setSkuId(skuDO.getSkuId());
            stockoutOrderSkuDeclarePriceBO.setStockoutOrderId(stockoutOrderBO.getId());
            stockoutOrderSkuDeclarePriceBO.setQuantity(skuDO.getQuantity());
            stockoutOrderSkuDeclarePriceBO.setDeclarePrice(skuDeclarePrice);
            stockoutOrderSkuDeclarePriceBO.setDiscountPrice(skuDO.getMerchantDiscountPrice());
            stockoutOrderSkuDeclarePriceBO.setSalePrice(skuDO.getMerchantPriceRoundDown());
            BigDecimal consumptionDutyTax = new BigDecimal(skuDeclarePrice).multiply(
                consumptionDutyRate).divide(new BigDecimal(1).subtract(consumptionDutyRate), 0,
                BigDecimal.ROUND_DOWN);
            BigDecimal addValueTaxTmp = new BigDecimal(skuDeclarePrice).add(consumptionDutyTax);
            BigDecimal addValueTax = addValueTaxTmp.multiply(addedValueTaxRate)
                .multiply(taxDisCountRate).multiply(new BigDecimal(skuDO.getQuantity()));
            consumptionDutyTax = consumptionDutyTax.multiply(taxDisCountRate).multiply(
                new BigDecimal(skuDO.getQuantity()));
            BigDecimal totalTaxOnSku = consumptionDutyTax.add(addValueTax);
            stockoutOrderSkuDeclarePriceBO.setTariffTax(0);
            stockoutOrderSkuDeclarePriceBO.setAddedValueTax(addValueTax.intValue());
            stockoutOrderSkuDeclarePriceBO.setConsumptionDutyTax(consumptionDutyTax.intValue());
            stockoutOrderSkuDeclarePriceBO.setTotalTax(totalTaxOnSku.intValue());

            skuDeclarePriceDetailMap.put(skuDO.getSkuId(), stockoutOrderSkuDeclarePriceBO);
        }

        int userDiscountPrice = 0;

        //订单总税费
        int taxTotalFee = tariffTotalPrice + consumptionDutyTotalPrice + addedValueTaxTotalPrice;
        //订单总价值（不含折扣）
        int orderTotalPrice = declareTotalGoodsPrice + taxTotalFee + freightFee;

        int userActualPay = stockoutOrderBO.getUserActualPaymentAmount();

        //        if (orderTotalPrice < userActualPay) {
        //            userActualPay = orderTotalPrice;
        //        } else {
        //            //设立计算的货款总价+税款总价总和 是所有款项之和抛出折扣后的金额，应该小于等于实际支付
        //            LogBetter.instance(commondLogger).setLevel(LogLevel.ERROR)
        //                    .setMsg("[供应链-实际用户支付总金额不允许小于等于0")
        //                    .addParm("包裹ID", stockoutOrderDO.getBizId())
        //                    .addParm("申报运费", freightFee)
        //                    .addParm("申报税费", taxTotalFee)
        //                    .addParm("申报折扣", userDiscountPrice)
        //                    .addParm("申报实际支付", userActualPay)
        //                    .addParm("申报订单总金额", orderTotalPrice)
        //                    .addParm("申报货款", declareTotalGoodsPrice)
        //                    .log();
        //            throw new ServiceException(LogisticsReturnCode.ORDER_FEE_SPLIT_ERROR, "广州口岸价格拆分异常，海关要求 折扣为0，但是计算出来的申报总价大于实际支付");
        //        }
        if (orderTotalPrice < userActualPay) {
            userActualPay = orderTotalPrice;
            userDiscountPrice = 0;
        } else {
            userDiscountPrice = orderTotalPrice - userActualPay;
        }

        StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceBO = new StockoutOrderDeclarePriceBO();
        stockoutOrderDeclarePriceBO.setFreightFee(freightFee);
        stockoutOrderDeclarePriceBO.setTaxFee(taxTotalFee);
        stockoutOrderDeclarePriceBO.setDiscountTotalPrice(userDiscountPrice);
        stockoutOrderDeclarePriceBO.setOrderActualPrice(userActualPay);
        stockoutOrderDeclarePriceBO.setDeclareTotalPrice(declareTotalGoodsPrice);
        stockoutOrderDeclarePriceBO.setOrderTotalPrice(orderTotalPrice);
        stockoutOrderDeclarePriceBO.setGoodsTotalPrice(declareTotalGoodsPrice);
        stockoutOrderDeclarePriceBO.setInsuranceFee(0);
        stockoutOrderDeclarePriceBO.setConsumptionDutyTax(consumptionDutyTotalPrice);
        stockoutOrderDeclarePriceBO.setAddedValueTax(addedValueTaxTotalPrice);
        stockoutOrderDeclarePriceBO.setTariffFee(tariffTotalPrice);

        checkCustomsOfficePriceSplit(stockoutOrderBO, stockoutOrderDeclarePriceBO);

        saveStockoutOrderDeclarePrice(stockoutOrderBO,
            stockoutOrderDeclarePriceBO.getOrderActualPrice(),
            stockoutOrderDeclarePriceBO.getGoodsTotalPrice(),
            stockoutOrderDeclarePriceBO.getOrderTotalPrice(),
            stockoutOrderDeclarePriceBO.getDeclareTotalPrice(),
            stockoutOrderDeclarePriceBO.getFreightFee(), stockoutOrderDeclarePriceBO.getTaxFee(),
            stockoutOrderDeclarePriceBO.getConsumptionDutyTax(),
            stockoutOrderDeclarePriceBO.getAddedValueTax(),
            stockoutOrderDeclarePriceBO.getTariffFee(),
            stockoutOrderDeclarePriceBO.getInsuranceFee(),
            stockoutOrderDeclarePriceBO.getDiscountTotalPrice());

        saveStockoutOrderSkuDeclarePrice(skuDeclarePriceDetailMap);

        LogBetter
            .instance(logger)
            .setLevel(LogLevel.INFO)
            .setTraceLogger(
                TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                    SystemConstants.TRACE_APP))
            .setMsg("[供应链-运费税费拆分]订单: " + stockoutOrderBO.getBizId())
            .addParm("订单金额数据", stockoutOrderDeclarePriceBO).addParm("订单商品申报价", skuDeclarePriceMap)
            .log();

        stockoutOrderDeclarePriceBO.setSkuDeclarePriceMap(skuDeclarePriceMap);

        return stockoutOrderDeclarePriceBO;
    }

    /**
     * 针对杭州口岸LSCM保税仓没有运费字段的计算各个价格信息
     * （完税价格：售价中抛出税和运费的价格）
     * 应征关税 = 完税价格 *关税税率（关税税率为0%，因此应征关税为0）；
     * 法定计征的消费税 =（完税价格/(1-消费税税率)）*消费税税率；
     * 法定计征的增值税 =（完税价格+正常计征的消费税税额）*增值税税率；
     * 应征消费税 = 法定计征的消费税*0.7；
     * 应征增值税 = 法定计征的增值税*0.7
     * <p/>
     * 运杂费 = 运费 + 保费
     * 完税价格 = 申报货款 + 运费 + 保费
     *
     * @param stockoutOrderBO
     * @param detailBOs
     * @param lineEntity
     * @param skuDeclareEntityMap
     * @return
     * @throws ServiceException
     */
    public StockoutOrderDeclarePriceBO calculatPriceDeclarePriceOnHzPortNoFreight(StockoutOrderBO stockoutOrderBO,
                                                                                  List<StockoutOrderDetailBO> detailBOs,
                                                                                  LogisticsLineBO lineEntity,
                                                                                  Map<Long, SkuDeclareBO> skuDeclareEntityMap)
                                                                                                                              throws ServiceException {
        Map<Long, Integer> skuDeclarePriceMap = new HashMap<Long, Integer>();
        Map<Long, StockoutOrderDeclarePriceDetailBO> skuDeclarePriceDetailMap = new HashMap<Long, StockoutOrderDeclarePriceDetailBO>();
        //SKU的平均销售价,需要在SKU进行Merge之前计算
        Map<Long, Integer> skuAverageMerchantPriceMap = calculationBasicSkuAverageSellingPriceContainAllDiscount(
            stockoutOrderBO, detailBOs);
        detailBOs = CommonUtil.mergeStockoutOrderSku(detailBOs, Boolean.FALSE);

        //如果是销售订单，关心运费
        int freightFee = 0;
        if (StockoutOrderType.SALES_STOCK_OUT.getValue() == stockoutOrderBO.getOrderType()) {
            if (stockoutOrderBO.getUserFreightFee() > 0) {
                freightFee = stockoutOrderBO.getUserFreightFee();
            }
        }

        //如果运费不为0，则需要将运费平坦到商品信息中
        Integer averageFreightOnSingleSku = averageFreightFeeOnSkus(detailBOs, freightFee);

        int declareTotalGoodsPrice = 0; //申报总货值
        int tariffTotalPrice = 0;//关税，目前全部为0
        int consumptionDutyTotalPrice = 0;//总消费税
        int addedValueTaxTotalPrice = 0;//总增值税
        BigDecimal taxDisCountRate = new BigDecimal("0.7");
        for (StockoutOrderDetailBO skuDO : detailBOs) {
            if (skuDO.getMerchantPriceRoundDown() == null || skuDO.getMerchantPriceRoundDown() <= 0) {
                skuDO.setMerchantPriceRoundDown(skuDO.getMerchantPrice());
            }
            SkuDeclareBO productDeclareEntity = skuDeclareEntityMap.get(skuDO.getSkuId());
            if (productDeclareEntity == null
                || StringUtils.isBlank(productDeclareEntity.getConsumptionDutyRate())
                || StringUtils.isBlank(productDeclareEntity.getAddedValueTaxRate())) {
                LogBetter
                    .instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(
                        TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                            SystemConstants.TRACE_APP))
                    .addParm("[供应链-运费税费拆分]商品备案信息不存在或者商品税率为空，sku信息：", skuDO)
                    .addParm("备案信息", productDeclareEntity).log();
                throw new ServiceException(LogisticsReturnCode.SKU_DECLARE_NOT_EXIST,
                    "商品备案信息不存在或者商品税率为空，sku信息：" + skuDO + "，口岸："
                            + lineEntity.getPortBO().getPortNid());
            }

            BigDecimal consumptionDutyRate = new BigDecimal(
                productDeclareEntity.getConsumptionDutyRate());
            BigDecimal addedValueTaxRate = new BigDecimal(
                productDeclareEntity.getAddedValueTaxRate());
            if (consumptionDutyRate.doubleValue() < 0 || consumptionDutyRate.doubleValue() >= 1) {
                LogBetter
                    .instance(commondLogger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(
                        TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                            SystemConstants.TRACE_APP))
                    .setMsg(
                        "[供应链-运费税费拆分]商品消费税税率不合法，sku信息：" + skuDO + ",消费税税率：" + consumptionDutyRate)
                    .log();
                throw new ServiceException(LogisticsReturnCode.SKU_DECLARE_TAXRATE_ILLEGAL,
                    "商品消费税税率不合法，sku信息：" + skuDO + ",消费税税率：" + consumptionDutyRate + "，口岸："
                            + lineEntity.getPortBO().getPortNid());
            }
            if (addedValueTaxRate.doubleValue() < 0 || addedValueTaxRate.doubleValue() >= 1) {
                LogBetter
                    .instance(commondLogger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(
                        TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                            SystemConstants.TRACE_APP))
                    .setMsg("[供应链-运费税费拆分]商品增值税税率不合法，sku信息：" + skuDO + ",增值税税率：" + addedValueTaxRate)
                    .log();
                throw new ServiceException(LogisticsReturnCode.SKU_DECLARE_TAXRATE_ILLEGAL,
                    "商品税增值税税率不合法，sku信息：" + skuDO + ",增值税税率：" + addedValueTaxRate + "，口岸："
                            + lineEntity.getPortBO().getPortNid());
            }

            int skuContainTaxOriginPrice = skuAverageMerchantPriceMap.get(skuDO.getSkuId());

            //算税因子： 1 + c*（a + b ）/(1-a) , a 为消费税税率、b 增值税税率、c 折扣率
            BigDecimal molecule = taxDisCountRate.multiply(consumptionDutyRate
                .add(addedValueTaxRate));
            BigDecimal denominator = new BigDecimal(1).subtract(consumptionDutyRate);
            BigDecimal taxFactor = molecule.divide(denominator, 4, BigDecimal.ROUND_DOWN);
            taxFactor = new BigDecimal(1).add(taxFactor);
            //把运费算到商品原价之中，运费也是需要缴税的
            int skuContainTaxAndFreightOriginPrice = skuContainTaxOriginPrice
                                                     + averageFreightOnSingleSku;

            String config = LogisticsDynamicConfig.getSplit().getRule("bptaxhscode",
                "firstMeasuringUnit");
            String config1 = LogisticsDynamicConfig.getSplit().getRule("bptaxhscode",
                "secondMeasuringUnit");
            List<String> firstWeightHsCodeList = Arrays.asList(config.split(","));
            List<String> secondWeightHsCodeList = Arrays.asList(config1.split(","));
            BigDecimal skuContainTaxOriginPriceRate = new BigDecimal(
                skuContainTaxAndFreightOriginPrice);
            if (firstWeightHsCodeList.contains(productDeclareEntity.getHsCode().toString())) {
                BigDecimal criterionTax = new BigDecimal(skuContainTaxAndFreightOriginPrice)
                    .divide(new BigDecimal(productDeclareEntity.getFirstWeight()), 0,
                        BigDecimal.ROUND_DOWN).divide(new BigDecimal(1000000));
                if (criterionTax.compareTo(taxFactor) > 0) {
                    consumptionDutyRate = new BigDecimal(0.15);
                    molecule = taxDisCountRate.multiply(consumptionDutyRate.add(addedValueTaxRate));
                    denominator = new BigDecimal(1).subtract(consumptionDutyRate);
                    taxFactor = molecule.divide(denominator, 4, BigDecimal.ROUND_DOWN).add(
                        new BigDecimal(1));
                    if (criterionTax.compareTo(taxFactor) < 0) {
                        skuContainTaxOriginPriceRate = new BigDecimal(1000000).multiply(
                            new BigDecimal(productDeclareEntity.getFirstWeight())).multiply(
                            taxFactor);
                    }
                }

            }
            if (secondWeightHsCodeList.contains(productDeclareEntity.getHsCode().toString())) {
                BigDecimal criterionTax = new BigDecimal(skuContainTaxAndFreightOriginPrice)
                    .divide(new BigDecimal(productDeclareEntity.getSecondWeight()), 0,
                        BigDecimal.ROUND_DOWN).divide(new BigDecimal(1500), 4,
                        BigDecimal.ROUND_DOWN);
                if (criterionTax.compareTo(taxFactor) > 0) {
                    consumptionDutyRate = new BigDecimal(0.15);
                    molecule = taxDisCountRate.multiply(consumptionDutyRate.add(addedValueTaxRate));
                    denominator = new BigDecimal(1).subtract(consumptionDutyRate);
                    taxFactor = molecule.divide(denominator, 4, BigDecimal.ROUND_DOWN).add(
                        new BigDecimal(1));
                    if (criterionTax.compareTo(taxFactor) < 0) {
                        skuContainTaxOriginPriceRate = new BigDecimal(1500).multiply(
                            new BigDecimal(productDeclareEntity.getSecondWeight())).multiply(
                            taxFactor);
                    }
                }
            }

            //售价中拆除税费
            Integer skuDeclarePrice = skuContainTaxOriginPriceRate.divide(taxFactor, 0,
                BigDecimal.ROUND_DOWN).intValue();
            skuDeclarePrice = skuDeclarePrice == 0 ? 1 : skuDeclarePrice;
            skuDeclarePriceMap.put(skuDO.getSkuId(), skuDeclarePrice);
            declareTotalGoodsPrice = declareTotalGoodsPrice
                                     + Long.valueOf(skuDeclarePrice * skuDO.getQuantity())
                                         .intValue();

            //计算增值税的时候需要使用未打折扣的消费税
            int consumptionDutySinglePriceNoDisCount = new BigDecimal(skuDeclarePrice)
                .multiply(consumptionDutyRate)
                .divide(new BigDecimal(1).subtract(consumptionDutyRate), 0, BigDecimal.ROUND_DOWN)
                .intValue();
            int addedValueTaxSinglePrice = new BigDecimal(skuDeclarePrice
                                                          + consumptionDutySinglePriceNoDisCount)
                .multiply(addedValueTaxRate).multiply(taxDisCountRate).intValue();
            addedValueTaxTotalPrice = addedValueTaxTotalPrice + addedValueTaxSinglePrice
                                      * skuDO.getQuantity();

            int consumptionDutySinglePrice = taxDisCountRate.multiply(
                new BigDecimal(consumptionDutySinglePriceNoDisCount)).intValue();
            consumptionDutyTotalPrice = consumptionDutyTotalPrice + consumptionDutySinglePrice
                                        * skuDO.getQuantity();

            StockoutOrderDeclarePriceDetailBO stockoutOrderSkuDeclarePriceBO = new StockoutOrderDeclarePriceDetailBO();
            stockoutOrderSkuDeclarePriceBO.setBizId(stockoutOrderBO.getBizId());
            stockoutOrderSkuDeclarePriceBO.setSkuId(skuDO.getSkuId());
            stockoutOrderSkuDeclarePriceBO.setStockoutOrderId(stockoutOrderBO.getId());
            stockoutOrderSkuDeclarePriceBO.setQuantity(skuDO.getQuantity());
            stockoutOrderSkuDeclarePriceBO.setDeclarePrice(skuDeclarePrice);
            stockoutOrderSkuDeclarePriceBO.setDiscountPrice(skuDO.getMerchantDiscountPrice());
            stockoutOrderSkuDeclarePriceBO.setSalePrice(skuDO.getMerchantPriceRoundDown());
            BigDecimal consumptionDutyTax = new BigDecimal(skuDeclarePrice).multiply(
                consumptionDutyRate).divide(new BigDecimal(1).subtract(consumptionDutyRate), 0,
                BigDecimal.ROUND_DOWN);
            BigDecimal addValueTaxTmp = new BigDecimal(skuDeclarePrice).add(consumptionDutyTax);
            BigDecimal addValueTax = addValueTaxTmp.multiply(addedValueTaxRate)
                .multiply(taxDisCountRate).multiply(new BigDecimal(skuDO.getQuantity()));
            consumptionDutyTax = consumptionDutyTax.multiply(taxDisCountRate).multiply(
                new BigDecimal(skuDO.getQuantity()));
            BigDecimal totalTaxOnSku = consumptionDutyTax.add(addValueTax);
            stockoutOrderSkuDeclarePriceBO.setTariffTax(0);
            stockoutOrderSkuDeclarePriceBO.setAddedValueTax(addValueTax.intValue());
            stockoutOrderSkuDeclarePriceBO.setConsumptionDutyTax(consumptionDutyTax.intValue());
            stockoutOrderSkuDeclarePriceBO.setTotalTax(totalTaxOnSku.intValue());

            skuDeclarePriceDetailMap.put(skuDO.getSkuId(), stockoutOrderSkuDeclarePriceBO);
        }

        //由于运费平摊到商品申报单价中，所以这里对运费进行重置
        freightFee = 0;

        //订单总税费
        int taxTotalFee = tariffTotalPrice + consumptionDutyTotalPrice + addedValueTaxTotalPrice;
        // 用户折扣
        int userDiscountPrice = 0;
        //订单总价值（不含折扣）
        int orderTotalPrice = declareTotalGoodsPrice + taxTotalFee + freightFee;

        int userActualPay = stockoutOrderBO.getUserActualPaymentAmount();

        if (orderTotalPrice < userActualPay) {
            userActualPay = orderTotalPrice;
            userDiscountPrice = 0;
        } else {
            userDiscountPrice = orderTotalPrice - userActualPay;
        }

        StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceBO = new StockoutOrderDeclarePriceBO();
        stockoutOrderDeclarePriceBO.setFreightFee(freightFee);
        stockoutOrderDeclarePriceBO.setTaxFee(taxTotalFee);
        stockoutOrderDeclarePriceBO.setDiscountTotalPrice(userDiscountPrice);
        stockoutOrderDeclarePriceBO.setOrderActualPrice(userActualPay);
        stockoutOrderDeclarePriceBO.setDeclareTotalPrice(declareTotalGoodsPrice);
        stockoutOrderDeclarePriceBO.setOrderTotalPrice(orderTotalPrice);
        stockoutOrderDeclarePriceBO.setGoodsTotalPrice(declareTotalGoodsPrice);
        stockoutOrderDeclarePriceBO.setInsuranceFee(0);
        stockoutOrderDeclarePriceBO.setConsumptionDutyTax(consumptionDutyTotalPrice);
        stockoutOrderDeclarePriceBO.setAddedValueTax(addedValueTaxTotalPrice);
        stockoutOrderDeclarePriceBO.setTariffFee(tariffTotalPrice);

        checkCustomsOfficePriceSplit(stockoutOrderBO, stockoutOrderDeclarePriceBO);

        saveStockoutOrderDeclarePrice(stockoutOrderBO,
            stockoutOrderDeclarePriceBO.getOrderActualPrice(),
            stockoutOrderDeclarePriceBO.getGoodsTotalPrice(),
            stockoutOrderDeclarePriceBO.getOrderTotalPrice(),
            stockoutOrderDeclarePriceBO.getDeclareTotalPrice(),
            stockoutOrderDeclarePriceBO.getFreightFee(), stockoutOrderDeclarePriceBO.getTaxFee(),
            stockoutOrderDeclarePriceBO.getConsumptionDutyTax(),
            stockoutOrderDeclarePriceBO.getAddedValueTax(),
            stockoutOrderDeclarePriceBO.getTariffFee(),
            stockoutOrderDeclarePriceBO.getInsuranceFee(),
            stockoutOrderDeclarePriceBO.getDiscountTotalPrice());

        saveStockoutOrderSkuDeclarePrice(skuDeclarePriceDetailMap);

        LogBetter
            .instance(logger)
            .setLevel(LogLevel.INFO)
            .setTraceLogger(
                TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                    SystemConstants.TRACE_APP))
            .setMsg("[供应链-运费税费拆分]订单: " + stockoutOrderBO.getBizId())
            .addParm("订单金额数据", stockoutOrderDeclarePriceBO).addParm("订单商品申报价", skuDeclarePriceMap)
            .log();

        stockoutOrderDeclarePriceBO.setSkuDeclarePriceMap(skuDeclarePriceMap);

        return stockoutOrderDeclarePriceBO;
    }

    /**
     * 针对海关总署的计算各个价格信息
     * （完税价格：售价中抛出税和运费的价格）
     * 应征关税 = 完税价格 *关税税率（关税税率为0%，因此应征关税为0）；
     * 法定计征的消费税 =（完税价格/(1-消费税税率)）*消费税税率；
     * 法定计征的增值税 =（完税价格+正常计征的消费税税额）*增值税税率；
     * 应征消费税 = 法定计征的消费税*0.7；
     * 应征增值税 = 法定计征的增值税*0.7
     * <p/>
     * 运杂费 = 运费 + 保费
     * 完税价格 = 申报货款 + 运费 + 保费
     *
     * @param stockoutOrderBO
     * @param detailBOs
     * @param lineEntity
     * @param skuDeclareEntityMap
     * @return
     * @throws ServiceException
     */
    public StockoutOrderDeclarePriceBO calculatPriceDeclarePriceOnCustomsOffice(StockoutOrderBO stockoutOrderBO,
                                                                                List<StockoutOrderDetailBO> detailBOs,
                                                                                LogisticsLineBO lineEntity,
                                                                                Map<Long, SkuDeclareBO> skuDeclareEntityMap)
                                                                                                                            throws ServiceException {
        Map<Long, Integer> skuDeclarePriceMap = new HashMap<Long, Integer>();
        Map<Long, StockoutOrderDeclarePriceDetailBO> skuDeclarePriceDetailMap = new HashMap<Long, StockoutOrderDeclarePriceDetailBO>();
        //SKU的平均销售价,需要在SKU进行Merge之前计算
        Map<Long, Integer> skuAverageMerchantPriceMap = calculationBasicSkuAverageSellingPriceContainAllDiscount(
            stockoutOrderBO, detailBOs);
        detailBOs = CommonUtil.mergeStockoutOrderSku(detailBOs, Boolean.FALSE);

        //由于运费在多商品下无法计算税款，如果将运费需要平摊到每一个商品的申报单价中,则这部分也会产生税款，导致订单总金额变多
        //如果将运费算作折扣的话，在一些场景下实际支付会变成负数,这种情况重新计算折扣（比如商品9.9元，运费20，用户支付29.9元）
        Integer freightFee = 0;

        int declareTotalGoodsPrice = 0; //申报总货值
        int tariffTotalPrice = 0;//关税，目前全部为0
        int consumptionDutyTotalPrice = 0;//总消费税
        int addedValueTaxTotalPrice = 0;//总增值税
        BigDecimal taxDisCountRate = new BigDecimal("0.7");
        for (StockoutOrderDetailBO skuDO : detailBOs) {
            if (skuDO.getMerchantPriceRoundDown() == null || skuDO.getMerchantPriceRoundDown() <= 0) {
                skuDO.setMerchantPriceRoundDown(skuDO.getMerchantPrice());
            }
            SkuDeclareBO productDeclareEntity = skuDeclareEntityMap.get(skuDO.getSkuId());
            if (productDeclareEntity == null
                || StringUtils.isBlank(productDeclareEntity.getConsumptionDutyRate())
                || StringUtils.isBlank(productDeclareEntity.getAddedValueTaxRate())) {
                LogBetter
                    .instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(
                        TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                            SystemConstants.TRACE_APP))
                    .addParm("[供应链-运费税费拆分]商品备案信息不存在或者商品税率为空，sku信息：", skuDO)
                    .addParm("备案信息", productDeclareEntity).log();
                throw new ServiceException(LogisticsReturnCode.SKU_DECLARE_NOT_EXIST,
                    "商品备案信息不存在或者商品税率为空，sku信息：" + skuDO + "，口岸："
                            + lineEntity.getPortBO().getPortNid());
            }

            BigDecimal consumptionDutyRate = new BigDecimal(
                productDeclareEntity.getConsumptionDutyRate());
            BigDecimal addedValueTaxRate = new BigDecimal(
                productDeclareEntity.getAddedValueTaxRate());
            if (consumptionDutyRate.doubleValue() < 0 || consumptionDutyRate.doubleValue() >= 1) {
                LogBetter
                    .instance(commondLogger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(
                        TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                            SystemConstants.TRACE_APP))
                    .setMsg(
                        "[供应链-运费税费拆分]商品消费税税率不合法，sku信息：" + skuDO + ",消费税税率：" + consumptionDutyRate)
                    .log();
                throw new ServiceException(LogisticsReturnCode.SKU_DECLARE_TAXRATE_ILLEGAL,
                    "商品消费税税率不合法，sku信息：" + skuDO + ",消费税税率：" + consumptionDutyRate + "，口岸："
                            + lineEntity.getPortBO().getPortNid());
            }
            if (addedValueTaxRate.doubleValue() < 0 || addedValueTaxRate.doubleValue() >= 1) {
                LogBetter
                    .instance(commondLogger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(
                        TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                            SystemConstants.TRACE_APP))
                    .setMsg("[供应链-运费税费拆分]商品增值税税率不合法，sku信息：" + skuDO + ",增值税税率：" + addedValueTaxRate)
                    .log();
                throw new ServiceException(LogisticsReturnCode.SKU_DECLARE_TAXRATE_ILLEGAL,
                    "商品税增值税税率不合法，sku信息：" + skuDO + ",增值税税率：" + addedValueTaxRate + "，口岸："
                            + lineEntity.getPortBO().getPortNid());
            }

            int skuContainTaxOriginPrice = skuAverageMerchantPriceMap.get(skuDO.getSkuId());

            //算税因子： 1 + c*（a + b ）/(1-a) , a 为消费税税率、b 增值税税率、c 折扣率
            BigDecimal molecule = taxDisCountRate.multiply(consumptionDutyRate
                .add(addedValueTaxRate));
            BigDecimal denominator = new BigDecimal(1).subtract(consumptionDutyRate);
            BigDecimal taxFactor = molecule.divide(denominator, 4, BigDecimal.ROUND_DOWN);
            taxFactor = new BigDecimal(1).add(taxFactor);

            String config = LogisticsDynamicConfig.getSplit().getRule("bptaxhscode",
                "firstMeasuringUnit");
            String config1 = LogisticsDynamicConfig.getSplit().getRule("bptaxhscode",
                "secondMeasuringUnit");
            List<String> firstWeightHsCodeList = Arrays.asList(config.split(","));
            List<String> secondWeightHsCodeList = Arrays.asList(config1.split(","));
            BigDecimal skuContainTaxOriginPriceRate = new BigDecimal(skuContainTaxOriginPrice);
            if (firstWeightHsCodeList.contains(productDeclareEntity.getHsCode().toString())) {
                logger.info("sku" + skuDO.getSkuId() + ",firstWeight"
                            + productDeclareEntity.getFirstWeight());
                BigDecimal criterionTax = new BigDecimal(skuContainTaxOriginPrice)
                    .divide(new BigDecimal(productDeclareEntity.getFirstWeight()), 0,
                        BigDecimal.ROUND_DOWN).divide(new BigDecimal(1000000));
                if (criterionTax.compareTo(taxFactor) > 0) {
                    consumptionDutyRate = new BigDecimal(0.15);
                    molecule = taxDisCountRate
                        .multiply(new BigDecimal(0.15).add(addedValueTaxRate));
                    denominator = new BigDecimal(1).subtract(new BigDecimal(0.15));
                    taxFactor = molecule.divide(denominator, 4, BigDecimal.ROUND_DOWN).add(
                        new BigDecimal(1));
                    if (criterionTax.compareTo(taxFactor) < 0) {
                        skuContainTaxOriginPriceRate = new BigDecimal(1000000).multiply(
                            new BigDecimal(productDeclareEntity.getFirstWeight())).multiply(
                            taxFactor);
                    }
                }

            }
            if (secondWeightHsCodeList.contains(productDeclareEntity.getHsCode().toString())) {
                BigDecimal criterionTax = new BigDecimal(skuContainTaxOriginPrice).divide(
                    new BigDecimal(productDeclareEntity.getSecondWeight()), 0,
                    BigDecimal.ROUND_DOWN).divide(new BigDecimal(1500), 4, BigDecimal.ROUND_DOWN);
                if (criterionTax.compareTo(taxFactor) > 0) {
                    consumptionDutyRate = new BigDecimal(0.15);
                    molecule = taxDisCountRate
                        .multiply(new BigDecimal(0.15).add(addedValueTaxRate));
                    denominator = new BigDecimal(1).subtract(new BigDecimal(0.15));
                    taxFactor = molecule.divide(denominator, 4, BigDecimal.ROUND_DOWN).add(
                        new BigDecimal(1));
                    if (criterionTax.compareTo(taxFactor) < 0) {
                        skuContainTaxOriginPriceRate = new BigDecimal(1500).multiply(
                            new BigDecimal(productDeclareEntity.getSecondWeight())).multiply(
                            taxFactor);
                    }
                }
            }

            //售价中拆除税费
            Integer skuDeclarePrice = skuContainTaxOriginPriceRate.divide(taxFactor, 0,
                BigDecimal.ROUND_DOWN).intValue();
            skuDeclarePrice = skuDeclarePrice == 0 ? 1 : skuDeclarePrice;
            skuDeclarePriceMap.put(skuDO.getSkuId(), skuDeclarePrice);
            declareTotalGoodsPrice = declareTotalGoodsPrice
                                     + Long.valueOf(skuDeclarePrice * skuDO.getQuantity())
                                         .intValue();

            //计算增值税的时候需要使用未打折扣的消费税
            int consumptionDutySinglePriceNoDisCount = new BigDecimal(skuDeclarePrice)
                .multiply(consumptionDutyRate)
                .divide(new BigDecimal(1).subtract(consumptionDutyRate), 0, BigDecimal.ROUND_DOWN)
                .intValue();
            int addedValueTaxSinglePrice = new BigDecimal(skuDeclarePrice
                                                          + consumptionDutySinglePriceNoDisCount)
                .multiply(addedValueTaxRate).multiply(taxDisCountRate).intValue();
            addedValueTaxTotalPrice = addedValueTaxTotalPrice + addedValueTaxSinglePrice
                                      * skuDO.getQuantity();

            int consumptionDutySinglePrice = taxDisCountRate.multiply(
                new BigDecimal(consumptionDutySinglePriceNoDisCount)).intValue();
            consumptionDutyTotalPrice = consumptionDutyTotalPrice + consumptionDutySinglePrice
                                        * skuDO.getQuantity();

            StockoutOrderDeclarePriceDetailBO stockoutOrderSkuDeclarePriceBO = new StockoutOrderDeclarePriceDetailBO();
            stockoutOrderSkuDeclarePriceBO.setBizId(stockoutOrderBO.getBizId());
            stockoutOrderSkuDeclarePriceBO.setSkuId(skuDO.getSkuId());
            stockoutOrderSkuDeclarePriceBO.setStockoutOrderId(stockoutOrderBO.getId());
            stockoutOrderSkuDeclarePriceBO.setQuantity(skuDO.getQuantity());
            stockoutOrderSkuDeclarePriceBO.setDeclarePrice(skuDeclarePrice);
            stockoutOrderSkuDeclarePriceBO.setDiscountPrice(skuDO.getMerchantDiscountPrice());
            stockoutOrderSkuDeclarePriceBO.setSalePrice(skuDO.getMerchantPriceRoundDown());
            BigDecimal consumptionDutyTax = new BigDecimal(skuDeclarePrice).multiply(
                consumptionDutyRate).divide(new BigDecimal(1).subtract(consumptionDutyRate), 0,
                BigDecimal.ROUND_DOWN);
            BigDecimal addValueTaxTmp = new BigDecimal(skuDeclarePrice).add(consumptionDutyTax);
            BigDecimal addValueTax = addValueTaxTmp.multiply(addedValueTaxRate)
                .multiply(taxDisCountRate).multiply(new BigDecimal(skuDO.getQuantity()));
            consumptionDutyTax = consumptionDutyTax.multiply(taxDisCountRate).multiply(
                new BigDecimal(skuDO.getQuantity()));
            BigDecimal totalTaxOnSku = consumptionDutyTax.add(addValueTax);
            stockoutOrderSkuDeclarePriceBO.setTariffTax(0);
            stockoutOrderSkuDeclarePriceBO.setAddedValueTax(addValueTax.intValue());
            stockoutOrderSkuDeclarePriceBO.setConsumptionDutyTax(consumptionDutyTax.intValue());
            stockoutOrderSkuDeclarePriceBO.setTotalTax(totalTaxOnSku.intValue());

            skuDeclarePriceDetailMap.put(skuDO.getSkuId(), stockoutOrderSkuDeclarePriceBO);
        }

        //订单总税费
        int taxTotalFee = tariffTotalPrice + consumptionDutyTotalPrice + addedValueTaxTotalPrice;
        //订单总价值（不含折扣）
        int orderTotalPrice = declareTotalGoodsPrice + taxTotalFee + freightFee;

        int userActualPay = stockoutOrderBO.getUserActualPaymentAmount();
        int userDiscountPrice;
        if (orderTotalPrice < userActualPay) {
            userActualPay = orderTotalPrice;
            userDiscountPrice = 0;
        } else {
            userDiscountPrice = orderTotalPrice - userActualPay;
        }

        StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceDO = new StockoutOrderDeclarePriceBO();
        stockoutOrderDeclarePriceDO.setFreightFee(freightFee);
        stockoutOrderDeclarePriceDO.setTaxFee(taxTotalFee);
        int userActulPayNoEarnest = userActualPay;
        stockoutOrderDeclarePriceDO.setOrderActualPrice(userActulPayNoEarnest);
        stockoutOrderDeclarePriceDO.setDiscountTotalPrice(userDiscountPrice);
        stockoutOrderDeclarePriceDO.setDeclareTotalPrice(declareTotalGoodsPrice);
        stockoutOrderDeclarePriceDO.setOrderTotalPrice(orderTotalPrice);
        stockoutOrderDeclarePriceDO.setGoodsTotalPrice(declareTotalGoodsPrice);
        stockoutOrderDeclarePriceDO.setInsuranceFee(0);
        stockoutOrderDeclarePriceDO.setConsumptionDutyTax(consumptionDutyTotalPrice);
        stockoutOrderDeclarePriceDO.setAddedValueTax(addedValueTaxTotalPrice);
        stockoutOrderDeclarePriceDO.setTariffFee(tariffTotalPrice);

        checkCustomsOfficePriceSplit(stockoutOrderBO, stockoutOrderDeclarePriceDO);

        saveStockoutOrderDeclarePrice(stockoutOrderBO,
            stockoutOrderDeclarePriceDO.getOrderActualPrice(),
            stockoutOrderDeclarePriceDO.getGoodsTotalPrice(),
            stockoutOrderDeclarePriceDO.getOrderTotalPrice(),
            stockoutOrderDeclarePriceDO.getDeclareTotalPrice(),
            stockoutOrderDeclarePriceDO.getFreightFee(), stockoutOrderDeclarePriceDO.getTaxFee(),
            stockoutOrderDeclarePriceDO.getConsumptionDutyTax(),
            stockoutOrderDeclarePriceDO.getAddedValueTax(),
            stockoutOrderDeclarePriceDO.getTariffFee(),
            stockoutOrderDeclarePriceDO.getInsuranceFee(),
            stockoutOrderDeclarePriceDO.getDiscountTotalPrice());

        saveStockoutOrderSkuDeclarePrice(skuDeclarePriceDetailMap);

        LogBetter
            .instance(logger)
            .setLevel(LogLevel.INFO)
            .setTraceLogger(
                TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(),
                    SystemConstants.TRACE_APP))
            .setMsg("[供应链-运费税费拆分]订单: " + stockoutOrderBO.getBizId())
            .addParm("订单金额数据", stockoutOrderDeclarePriceDO).addParm("订单商品申报价", skuDeclarePriceMap)
            .log();

        stockoutOrderDeclarePriceDO.setSkuDeclarePriceMap(skuDeclarePriceMap);

        return stockoutOrderDeclarePriceDO;
    }

    /**
     * 检查技术的结果是否正确
     *
     * @param stockoutOrderDeclarePriceDO
     */
    private void checkCustomsOfficePriceSplit(StockoutOrderBO stockoutOrderDO,
                                              StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceDO) {
        if (stockoutOrderDeclarePriceDO.getOrderActualPrice() > stockoutOrderDO
            .getUserActualPaymentAmount()) {
            LogBetter.instance(commondLogger).setLevel(LogLevel.ERROR).setMsg("[供应链-计算申报金额异常]")
                .addParm("包裹ID", stockoutOrderDO.getBizId())
                .addParm("申报运费", stockoutOrderDeclarePriceDO.getFreightFee())
                .addParm("申报税费", stockoutOrderDeclarePriceDO.getTaxFee())
                .addParm("申报折扣", stockoutOrderDeclarePriceDO.getDiscountTotalPrice())
                .addParm("申报实际支付", stockoutOrderDeclarePriceDO.getOrderActualPrice())
                .addParm("申报总金额", stockoutOrderDeclarePriceDO.getDeclareTotalPrice())
                .addParm("申报订单总金额", stockoutOrderDeclarePriceDO.getOrderTotalPrice())
                .addParm("申报货款", stockoutOrderDeclarePriceDO.getGoodsTotalPrice()).log();
            throw new IllegalArgumentException("订单[申报实际支付] 不能 大于 包裹[用户实际支付]");
        }

        if (stockoutOrderDeclarePriceDO.getOrderActualPrice() <= 0) {
            LogBetter.instance(commondLogger).setLevel(LogLevel.ERROR)
                .setMsg("[供应链-实际用户支付总金额不允许小于等于0").addParm("包裹ID", stockoutOrderDO.getBizId())
                .addParm("申报运费", stockoutOrderDeclarePriceDO.getFreightFee())
                .addParm("申报税费", stockoutOrderDeclarePriceDO.getTaxFee())
                .addParm("申报折扣", stockoutOrderDeclarePriceDO.getDiscountTotalPrice())
                .addParm("申报实际支付", stockoutOrderDeclarePriceDO.getOrderActualPrice())
                .addParm("申报总金额", stockoutOrderDeclarePriceDO.getDeclareTotalPrice())
                .addParm("申报订单总金额", stockoutOrderDeclarePriceDO.getOrderTotalPrice())
                .addParm("申报货款", stockoutOrderDeclarePriceDO.getGoodsTotalPrice()).log();
            throw new IllegalArgumentException("实际用户支付总金额不允许小于等于0");
        }

        if (stockoutOrderDeclarePriceDO.getDiscountTotalPrice() < 0) {
            LogBetter.instance(commondLogger).setLevel(LogLevel.ERROR).setMsg("[供应链-订单折扣金额不能小于0")
                .addParm("包裹ID", stockoutOrderDO.getBizId())
                .addParm("申报运费", stockoutOrderDeclarePriceDO.getFreightFee())
                .addParm("申报税费", stockoutOrderDeclarePriceDO.getTaxFee())
                .addParm("申报折扣", stockoutOrderDeclarePriceDO.getDiscountTotalPrice())
                .addParm("申报实际支付", stockoutOrderDeclarePriceDO.getOrderActualPrice())
                .addParm("申报总金额", stockoutOrderDeclarePriceDO.getDeclareTotalPrice())
                .addParm("申报订单总金额", stockoutOrderDeclarePriceDO.getOrderTotalPrice())
                .addParm("申报货款", stockoutOrderDeclarePriceDO.getGoodsTotalPrice()).log();
            throw new IllegalArgumentException("订单折扣金额不能小于0");
        }

        if (stockoutOrderDeclarePriceDO.getTaxFee() < 0) {
            LogBetter.instance(commondLogger).setLevel(LogLevel.ERROR)
                .setMsg("[供应链-计算申报税款金额异常，综合税不允许小于0]").addParm("包裹ID", stockoutOrderDO.getBizId())
                .addParm("申报运费", stockoutOrderDeclarePriceDO.getFreightFee())
                .addParm("申报税费", stockoutOrderDeclarePriceDO.getTaxFee())
                .addParm("申报折扣", stockoutOrderDeclarePriceDO.getDiscountTotalPrice())
                .addParm("申报实际支付", stockoutOrderDeclarePriceDO.getOrderActualPrice())
                .addParm("申报总金额", stockoutOrderDeclarePriceDO.getDeclareTotalPrice())
                .addParm("申报订单总金额", stockoutOrderDeclarePriceDO.getOrderTotalPrice())
                .addParm("申报货款", stockoutOrderDeclarePriceDO.getGoodsTotalPrice()).log();
            throw new IllegalArgumentException("订单申报综合税总额不能小于0");
        }
    }

    /**
     * 从数据库中获取上一次计算好的价格字段信息，如果不存在，则不进行价格拆分
     *
     * @param stockoutOrderBO
     * @param detailDOs
     * @return
     * @throws ServiceException
     */
    public StockoutOrderDeclarePriceBO getPriceConfigFromDB(StockoutOrderBO stockoutOrderBO,
                                                            List<StockoutOrderDetailBO> detailDOs,
                                                            LogisticsLineBO lineBO)
                                                                                   throws ServiceException {

        StockoutOrderDeclarePriceDO stockoutOrderDeclarePriceDO = new StockoutOrderDeclarePriceDO();
        stockoutOrderDeclarePriceDO.setBizId(stockoutOrderBO.getBizId());
        stockoutOrderDeclarePriceDO.setStockoutOrderId(stockoutOrderBO.getId());
        List<StockoutOrderDeclarePriceDO> stockoutOrderDeclarePriceDOs = stockoutOrderDeclarePriceManager
            .query(BaseQuery.getInstance(stockoutOrderDeclarePriceDO));

        if (CollectionUtils.isEmpty(stockoutOrderDeclarePriceDOs)) {
            if (lineBO.getPortBO() == null) {
                return notCalculatPriceSplit(stockoutOrderBO, detailDOs, lineBO,
                    "数据库中获取计算后的订单申报价格失败");
            }

            Map<Long, SkuDeclareBO> productDeclareEntityMap = dataPrepareProcessor
                .getSkuDeclareEntitisMap(lineBO.getPortBO(), detailDOs, stockoutOrderBO,
                    lineBO.getLineType());
            if (PortNid.GUANGZHOU.getValue() == lineBO.getPortBO().getId()) {
                return calculatPriceDeclarePriceOnGuangzhouVersion2(stockoutOrderBO, detailDOs,
                    lineBO, productDeclareEntityMap);
            } else if (PortNid.HANGZHOU.getValue() == lineBO.getPortBO().getId()) {
                return calculatPriceDeclarePriceOnHzPortNoFreight(stockoutOrderBO, detailDOs,
                    lineBO, productDeclareEntityMap);
            } else if (PortNid.JINAN.getValue() == lineBO.getPortBO().getId()) {
                return notCalculatPriceSplit(stockoutOrderBO, detailDOs, lineBO, "不走口岸不需要拆分");
            } else if (PortNid.XIAMEN.getValue() == lineBO.getPortBO().getId()) {
                return notCalculatPriceSplit(stockoutOrderBO, detailDOs, lineBO, "不走口岸不需要拆分");
            } else if (PortNid.PINGTAN.getValue() == lineBO.getPortBO().getId()) {
                return notCalculatPriceSplit(stockoutOrderBO, detailDOs, lineBO, "不走口岸不需要拆分");
            } else if (PortNid.SHATIAN.getValue() == lineBO.getPortBO().getId()) {
                return notCalculatPriceSplit(stockoutOrderBO, detailDOs, lineBO, "不走口岸不需要拆分");
            } else {
                return calculatPriceDeclarePriceOnCustomsOffice(stockoutOrderBO, detailDOs, lineBO,
                    productDeclareEntityMap);
            }
        }

        StockoutOrderDeclarePriceBO stockoutOrderDeclarePriceBO = StockoutOrderConvert
            .convertDeclarePriceDOToDeclarePriceBO(stockoutOrderDeclarePriceDOs.get(0));

        if (stockoutOrderDeclarePriceBO.getOrderActualPrice() != null
            && stockoutOrderDeclarePriceBO.getOrderActualPrice() <= 0) {
            throw new ServiceException(
                LogisticsReturnCode.STOCKOUT_ORDER_DECLARE_ACTURE_PAY_IS_ZERO,
                "实际支付申报不能为0，出库单ID: " + stockoutOrderDeclarePriceDOs.get(0).getStockoutOrderId());
        }

        StockoutOrderDeclarePriceDetailDO stockoutorderSkuDeclarePriceDO = new StockoutOrderDeclarePriceDetailDO();
        stockoutorderSkuDeclarePriceDO.setStockoutOrderId(stockoutOrderBO.getId());
        stockoutorderSkuDeclarePriceDO.setBizId(stockoutOrderBO.getBizId());
        List<StockoutOrderDeclarePriceDetailDO> skuDeclarePriceDOs = stockoutOrderDeclarePriceDetailManager
            .query(BaseQuery.getInstance(stockoutorderSkuDeclarePriceDO));
        if (CollectionUtils.isEmpty(skuDeclarePriceDOs)) {
            if (lineBO.getPortBO() != null) {
                Map<Long, SkuDeclareBO> productDeclareEntityMap = dataPrepareProcessor
                    .getSkuDeclareEntitisMap(lineBO.getPortBO(), detailDOs, stockoutOrderBO,
                        lineBO.getLineType());
                return calculatPriceDeclarePriceOnCustomsOffice(stockoutOrderBO, detailDOs, lineBO,
                    productDeclareEntityMap);
            } else {
                return notCalculatPriceSplit(stockoutOrderBO, detailDOs, lineBO,
                    "数据库中获取计算后的订单申报价格失败");
            }
        }

        Map<Long, Integer> stockoutOrderSkuDeclareMap = new HashMap<Long, Integer>();
        for (StockoutOrderDeclarePriceDetailDO priceDO : skuDeclarePriceDOs) {
            stockoutOrderSkuDeclareMap.put(priceDO.getSkuId(), priceDO.getDeclarePrice());
        }
        stockoutOrderDeclarePriceBO.setSkuDeclarePriceMap(stockoutOrderSkuDeclareMap);
        return stockoutOrderDeclarePriceBO;
    }

    /**
     * 保存订单相关金额数据
     *
     * @param stockoutOrderBO
     * @param userTotalPay      用户实际支付价格
     * @param goodsTotalPrice   货款价格
     * @param orderTotalPrice   订单总金额
     * @param declareTotalPrice 订单申报总价
     * @param freightFee        总运费
     * @param taxFee            总税费
     * @param consumptionDuty   消费税
     * @param addedValueTax     增值税
     * @param tariffFee         关税
     * @param insuranceFee      保费
     * @param discountPrice     申报总折扣
     */
    private StockoutOrderDeclarePriceDO saveStockoutOrderDeclarePrice(StockoutOrderBO stockoutOrderBO,
                                                                      int userTotalPay,
                                                                      int goodsTotalPrice,
                                                                      int orderTotalPrice,
                                                                      int declareTotalPrice,
                                                                      int freightFee, int taxFee,
                                                                      int consumptionDuty,
                                                                      int addedValueTax,
                                                                      int tariffFee,
                                                                      int insuranceFee,
                                                                      int discountPrice)
                                                                                        throws ServiceException {
        StockoutOrderDeclarePriceDO stockoutOrderDeclarePriceDO = new StockoutOrderDeclarePriceDO();
        stockoutOrderDeclarePriceDO.setBizId(stockoutOrderBO.getBizId());
        stockoutOrderDeclarePriceDO.setStockoutOrderId(stockoutOrderBO.getId());
        List<StockoutOrderDeclarePriceDO> stockoutOrderDeclarePriceDOs = stockoutOrderDeclarePriceManager
            .query(BaseQuery.getInstance(stockoutOrderDeclarePriceDO));
        if (CollectionUtils.isEmpty(stockoutOrderDeclarePriceDOs)) {
            stockoutOrderDeclarePriceDO.setOrderActualPrice(userTotalPay);
            stockoutOrderDeclarePriceDO.setGoodsTotalPrice(goodsTotalPrice);
            stockoutOrderDeclarePriceDO.setOrderTotalPrice(orderTotalPrice);
            stockoutOrderDeclarePriceDO.setDeclareTotalPrice(declareTotalPrice);
            stockoutOrderDeclarePriceDO.setFreightFee(freightFee);
            stockoutOrderDeclarePriceDO.setTaxFee(taxFee);
            stockoutOrderDeclarePriceDO.setConsumptionDutyTax(consumptionDuty);
            stockoutOrderDeclarePriceDO.setAddedValueTax(addedValueTax);
            stockoutOrderDeclarePriceDO.setTariffFee(tariffFee);
            stockoutOrderDeclarePriceDO.setInsuranceFee(insuranceFee);
            stockoutOrderDeclarePriceDO.setDiscountTotalPrice(discountPrice);
            stockoutOrderDeclarePriceDO.setIsPayTax(0);
            stockoutOrderDeclarePriceManager.insert(stockoutOrderDeclarePriceDO);
            if (stockoutOrderDeclarePriceDO.getId() == null) {
                throw new ServiceException(
                    LogisticsReturnCode.STOCKOUT_ORDER_DECLARE_PRICE_SAVE_FAILURE,
                    "保存出库单申报价格异常：" + stockoutOrderBO);
            }
        } else {
            stockoutOrderDeclarePriceDO = stockoutOrderDeclarePriceDOs.get(0);
            stockoutOrderDeclarePriceDO.setOrderActualPrice(userTotalPay);
            stockoutOrderDeclarePriceDO.setGoodsTotalPrice(goodsTotalPrice);
            stockoutOrderDeclarePriceDO.setOrderTotalPrice(orderTotalPrice);
            stockoutOrderDeclarePriceDO.setDeclareTotalPrice(declareTotalPrice);
            stockoutOrderDeclarePriceDO.setFreightFee(freightFee);
            stockoutOrderDeclarePriceDO.setTaxFee(taxFee);
            stockoutOrderDeclarePriceDO.setConsumptionDutyTax(consumptionDuty);
            stockoutOrderDeclarePriceDO.setAddedValueTax(addedValueTax);
            stockoutOrderDeclarePriceDO.setTariffFee(tariffFee);
            stockoutOrderDeclarePriceDO.setInsuranceFee(insuranceFee);
            stockoutOrderDeclarePriceDO.setDiscountTotalPrice(discountPrice);
            stockoutOrderDeclarePriceDO.setIsPayTax(0);
            stockoutOrderDeclarePriceManager.update(stockoutOrderDeclarePriceDO);
        }
        return stockoutOrderDeclarePriceDO;
    }

    /**
     * 保存订单相关金额数据
     *
     * @param stockoutOrderBO
     * @param userTotalPay      用户实际支付价格
     * @param goodsTotalPrice   货款价格
     * @param orderTotalPrice   订单总金额
     * @param declareTotalPrice 订单申报总价
     * @param freightFee        总运费
     * @param taxFee            总税费
     * @param discountPrice     申报总折扣
     */
    private StockoutOrderDeclarePriceDO saveStockoutOrderDeclarePrice(StockoutOrderBO stockoutOrderBO,
                                                                      int userTotalPay,
                                                                      int goodsTotalPrice,
                                                                      int orderTotalPrice,
                                                                      int declareTotalPrice,
                                                                      int freightFee, int taxFee,
                                                                      int discountPrice)
                                                                                        throws ServiceException {
        return saveStockoutOrderDeclarePrice(stockoutOrderBO, userTotalPay, goodsTotalPrice,
            orderTotalPrice, declareTotalPrice, freightFee, taxFee, 0, 0, 0, 0, discountPrice);
    }

    /**
     * 保存订单商品相关金额数据
     *
     * @param skuDeclarePriceDetailMap
     * @throws ServiceException
     */
    private void saveStockoutOrderSkuDeclarePrice(Map<Long, StockoutOrderDeclarePriceDetailBO> skuDeclarePriceDetailMap)
                                                                                                                        throws ServiceException {
        for (Map.Entry<Long, StockoutOrderDeclarePriceDetailBO> skuDeclarePriceBOEntry : skuDeclarePriceDetailMap
            .entrySet()) {
            StockoutOrderDeclarePriceDetailDO stockoutorderSkuDeclarePriceDO = StockoutOrderConvert
                .convertDeclarePriceDetailBOToDeclarePriceDetailDO(skuDeclarePriceBOEntry
                    .getValue());
            StockoutOrderDeclarePriceDetailDO skuDeclarePriceDOForQuery = new StockoutOrderDeclarePriceDetailDO();
            skuDeclarePriceDOForQuery.setSkuId(stockoutorderSkuDeclarePriceDO.getSkuId());
            skuDeclarePriceDOForQuery.setStockoutOrderId(stockoutorderSkuDeclarePriceDO.getId());
            skuDeclarePriceDOForQuery.setBizId(stockoutorderSkuDeclarePriceDO.getBizId());
            List<StockoutOrderDeclarePriceDetailDO> skuDeclarePriceDOs = stockoutOrderDeclarePriceDetailManager
                .query(BaseQuery.getInstance(skuDeclarePriceDOForQuery));
            if (CollectionUtils.isEmpty(skuDeclarePriceDOs)) {
                stockoutOrderDeclarePriceDetailManager.insert(stockoutorderSkuDeclarePriceDO);
            } else {
                stockoutorderSkuDeclarePriceDO.setId(skuDeclarePriceDOs.get(0).getId());
                stockoutOrderDeclarePriceDetailManager.update(stockoutorderSkuDeclarePriceDO);
            }
        }
    }

    /**
     * 保存订单商品相关金额数据
     *
     * @param stockoutOrderBO
     * @param detailBOs
     * @param skuDeclarePriceMap
     * @throws ServiceException
     */
    private void saveStockoutOrderSkuDeclarePrice(StockoutOrderBO stockoutOrderBO,
                                                  List<StockoutOrderDetailBO> detailBOs,
                                                  Map<Long, Integer> skuDeclarePriceMap)
                                                                                        throws ServiceException {
        for (StockoutOrderDetailBO sku : detailBOs) {
            StockoutOrderDeclarePriceDetailDO stockoutorderSkuDeclarePriceDO = new StockoutOrderDeclarePriceDetailDO();
            stockoutorderSkuDeclarePriceDO.setSkuId(sku.getSkuId());
            stockoutorderSkuDeclarePriceDO.setStockoutOrderId(stockoutOrderBO.getId());
            stockoutorderSkuDeclarePriceDO.setBizId(stockoutOrderBO.getBizId());
            List<StockoutOrderDeclarePriceDetailDO> skuDeclarePriceDOs = stockoutOrderDeclarePriceDetailManager
                .query(BaseQuery.getInstance(stockoutorderSkuDeclarePriceDO));
            if (CollectionUtils.isEmpty(skuDeclarePriceDOs)) {
                stockoutorderSkuDeclarePriceDO.setQuantity(sku.getQuantity());
                if (!skuDeclarePriceMap.containsKey(sku.getSkuId())
                    || skuDeclarePriceMap.get(sku.getSkuId()) == null) {
                    throw new ServiceException(
                        LogisticsReturnCode.STOCKOUT_ORDER_SKU_DECLARE_PRICE_NOT_EXIST,
                        "商品ID：" + sku.getSkuId());
                }

                stockoutorderSkuDeclarePriceDO.setDeclarePrice(skuDeclarePriceMap.get(sku
                    .getSkuId()));
                stockoutorderSkuDeclarePriceDO.setSalePrice(sku.getMerchantPriceRoundDown());
                stockoutorderSkuDeclarePriceDO.setTariffTax(0);
                stockoutorderSkuDeclarePriceDO.setDiscountPrice(sku.getMerchantDiscountPrice());
                stockoutOrderDeclarePriceDetailManager.insert(stockoutorderSkuDeclarePriceDO);
            } else {
                stockoutorderSkuDeclarePriceDO = skuDeclarePriceDOs.get(0);
                stockoutorderSkuDeclarePriceDO.setQuantity(sku.getQuantity());
                if (!skuDeclarePriceMap.containsKey(sku.getSkuId())
                    || skuDeclarePriceMap.get(sku.getSkuId()) == null) {
                    throw new ServiceException(
                        LogisticsReturnCode.STOCKOUT_ORDER_SKU_DECLARE_PRICE_NOT_EXIST,
                        "商品ID：" + sku.getSkuId());
                }
                stockoutorderSkuDeclarePriceDO.setDeclarePrice(skuDeclarePriceMap.get(sku
                    .getSkuId()));
                stockoutorderSkuDeclarePriceDO.setDiscountPrice(sku.getMerchantDiscountPrice());
                stockoutOrderDeclarePriceDetailManager.update(stockoutorderSkuDeclarePriceDO);
            }
        }
    }

    /**
     * 计算基础商品的平均销售价格,刨除所有折扣，
     * 如果存在一个SKUID对应多个销售价格，则取平均值
     * 使用场景：杭州电子口岸中，海淘的秒杀商品与正常商品一起购买时，同一个SKUID存在不同价格，再与外部系统交互的时候是需要进行SKUID和价格合并的
     *
     * @param stockoutOrderSkuDOs
     * @return
     */
    private static Map<Long, Integer> calculationBasicSkuAverageSellingPriceNotAllDiscount(StockoutOrderBO stockoutOrderDO,
                                                                                           List<StockoutOrderDetailBO> stockoutOrderSkuDOs)
                                                                                                                                           throws ServiceException {
        Map<Long, List<StockoutOrderDetailBO>> skuDOListBySkuIdSort = getBasicSkuListBySkuIdSort(stockoutOrderSkuDOs);
        Map<Long, Integer> skuAveragePriceMap = new HashMap<Long, Integer>();
        for (Map.Entry<Long, List<StockoutOrderDetailBO>> entry : skuDOListBySkuIdSort.entrySet()) {
            Long skuId = entry.getKey();
            List<StockoutOrderDetailBO> skuDOList = entry.getValue();
            if (CollectionUtils.isNotEmpty(skuDOList)) {
                if (skuDOList.size() == 1) {
                    if (skuDOList.get(0).getMerchantPriceRoundDown() == null
                        || skuDOList.get(0).getMerchantPriceRoundDown() <= 0) {
                        skuDOList.get(0).setMerchantPriceRoundDown(
                            skuDOList.get(0).getMerchantPrice());
                    }
                    if (skuDOList.get(0).getMerchantPriceRoundDown() == 0) {
                        //当发现商品销售价格为0时，初始化为1（解决口岸申报问题，弊端是总金额可能有一分钱的偏差）
                        skuAveragePriceMap.put(skuId, 1);
                    } else {
                        skuAveragePriceMap
                            .put(skuId, skuDOList.get(0).getMerchantPriceRoundDown()
                                        - skuDOList.get(0).getMerchantDiscountPrice());
                    }
                } else {
                    int totalCount = 0;
                    int totalPriceRmb = 0;
                    for (StockoutOrderDetailBO skuDO : skuDOList) {
                        totalCount = totalCount + skuDO.getQuantity();
                        if (skuDO.getMerchantPriceRoundDown() == null
                            || skuDO.getMerchantPriceRoundDown() <= 0) {
                            skuDO.setMerchantPriceRoundDown(skuDO.getMerchantPrice());
                        }
                        totalPriceRmb = totalPriceRmb
                                        + (skuDO.getMerchantPriceRoundDown() - skuDO
                                            .getMerchantDiscountPrice()) * skuDO.getQuantity();
                    }
                    if (totalCount == 0) {
                        LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                            .setErrorMsg("[供应链-计算商品平均价格]商品数量不能为0")
                            .addParm("出库单信息", stockoutOrderDO).addParm("商品ID", skuId).log();
                        throw new ServiceException(
                            LogisticsReturnCode.STOCKOUT_ORDER_PARAM_ILLIGAL, "商品数量和不能为零，商品ID："
                                                                              + skuId);
                    }

                    int averagePrice = totalPriceRmb / totalCount;
                    if (averagePrice == 0) {
                        //当发现商品销售价格为0时，初始化为1（解决口岸申报问题，弊端是总金额可能有一分钱的偏差）
                        skuAveragePriceMap.put(skuId, 1);
                    } else {
                        skuAveragePriceMap.put(skuId, averagePrice);
                    }

                }
            } else {
                throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_PARAM_ILLIGAL,
                    "商品ID：" + skuId);
            }
        }
        logger.info("[计算运费税费拆分]商品平均售价：" + skuAveragePriceMap.toString());
        return skuAveragePriceMap;
    }

    /**
     * 不排除折扣的情况下计算商品的平均销售价
     *
     * @param stockoutOrderDO
     * @param stockoutOrderSkuDOs
     * @return
     * @throws ServiceException
     */
    private static Map<Long, Integer> calculationBasicSkuAverageSellingPriceContainAllDiscount(StockoutOrderBO stockoutOrderDO,
                                                                                               List<StockoutOrderDetailBO> stockoutOrderSkuDOs)
                                                                                                                                               throws ServiceException {
        Map<Long, List<StockoutOrderDetailBO>> skuDOListBySkuIdSort = getBasicSkuListBySkuIdSort(stockoutOrderSkuDOs);
        Map<Long, Integer> skuAveragePriceMap = new HashMap<Long, Integer>();
        for (Map.Entry<Long, List<StockoutOrderDetailBO>> entry : skuDOListBySkuIdSort.entrySet()) {
            Long skuId = entry.getKey();
            List<StockoutOrderDetailBO> skuDOList = entry.getValue();
            if (CollectionUtils.isNotEmpty(skuDOList)) {
                if (skuDOList.size() == 1) {
                    if (skuDOList.get(0).getMerchantPriceRoundDown() == null
                        || skuDOList.get(0).getMerchantPriceRoundDown() <= 0) {
                        skuDOList.get(0).setMerchantPriceRoundDown(
                            skuDOList.get(0).getMerchantPrice());
                    }
                    if (skuDOList.get(0).getMerchantPriceRoundDown() == 0) {
                        //当发现商品销售价格为0时，初始化为1（解决口岸申报问题，弊端是总金额可能有一分钱的偏差）
                        skuAveragePriceMap.put(skuId, 1);
                    } else {
                        skuAveragePriceMap.put(skuId, skuDOList.get(0).getMerchantPriceRoundDown());
                    }
                } else {
                    int totalCount = 0;
                    int totalPriceRmb = 0;
                    for (StockoutOrderDetailBO skuDO : skuDOList) {
                        totalCount = totalCount + skuDO.getQuantity();
                        if (skuDO.getMerchantPriceRoundDown() == null
                            || skuDO.getMerchantPriceRoundDown() <= 0) {
                            skuDO.setMerchantPriceRoundDown(skuDO.getMerchantPrice());
                        }
                        totalPriceRmb = totalPriceRmb + skuDO.getMerchantPriceRoundDown()
                                        * skuDO.getQuantity();
                    }
                    if (totalCount == 0) {
                        LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                            .setErrorMsg("[供应链-计算商品平均价格]商品数量不能为0")
                            .addParm("出库单信息", stockoutOrderDO).addParm("商品ID", skuId).log();
                        throw new ServiceException(
                            LogisticsReturnCode.STOCKOUT_ORDER_PARAM_ILLIGAL, "商品数量和不能为零，商品ID："
                                                                              + skuId);
                    }

                    int averagePrice = totalPriceRmb / totalCount;
                    if (averagePrice == 0) {
                        //当发现商品销售价格为0时，初始化为1（解决口岸申报问题，弊端是总金额可能有一分钱的偏差）
                        skuAveragePriceMap.put(skuId, 1);
                    } else {
                        skuAveragePriceMap.put(skuId, averagePrice);
                    }

                }
            } else {
                throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_PARAM_ILLIGAL,
                    "商品ID：" + skuId);
            }
        }
        logger.info("[计算运费税费拆分]商品平均售价：" + skuAveragePriceMap.toString());
        return skuAveragePriceMap;
    }

    /**
     * 计算基础商品的平均销售价格，刨除活动折扣，如果存在一个SKUID对应多个销售价格，则取平均值
     * 使用场景：海淘的秒杀商品与正常商品一起购买时，同一个SKUID存在不同价格，再与外部系统交互的时候是需要进行SKUID和价格合并的
     *
     * @param detailBOs
     * @return Map (key:skuId, value:平均价格)
     */
    private static Map<Long, Integer> calculationBasicSkuAverageSellingPriceNotActivityDiscount(StockoutOrderBO stockoutOrderBO,
                                                                                                List<StockoutOrderDetailBO> detailBOs)
                                                                                                                                      throws ServiceException {
        Map<Long, List<StockoutOrderDetailBO>> detailBOListBySkuIdSort = getBasicSkuListBySkuIdSort(detailBOs);
        Map<Long, Integer> skuAveragePriceMap = new HashMap<Long, Integer>();
        for (Map.Entry<Long, List<StockoutOrderDetailBO>> entry : detailBOListBySkuIdSort
            .entrySet()) {
            Long skuId = entry.getKey();
            List<StockoutOrderDetailBO> skuDOList = entry.getValue();
            if (CollectionUtils.isNotEmpty(skuDOList)) {
                if (skuDOList.size() == 1) {
                    if (skuDOList.get(0).getMerchantPriceRoundDown() == null
                        || skuDOList.get(0).getMerchantPriceRoundDown() <= 0) {
                        skuDOList.get(0).setMerchantPriceRoundDown(
                            skuDOList.get(0).getMerchantPrice());
                    }
                    if (skuDOList.get(0).getMerchantPriceRoundDown() == 0) {
                        //当发现商品销售价格为0时，初始化为1（解决口岸申报问题，弊端是总金额可能有一分钱的偏差）
                        skuAveragePriceMap.put(skuId, 1);
                    } else {
                        skuAveragePriceMap.put(skuId, skuDOList.get(0).getMerchantPriceRoundDown()
                                                      - skuDOList.get(0)
                                                          .getMerchantActivityDiscountPrice());
                    }
                } else {
                    int totalCount = 0;
                    int totalPriceRmb = 0;
                    for (StockoutOrderDetailBO skuDO : skuDOList) {
                        totalCount = totalCount + skuDO.getQuantity();
                        if (skuDO.getMerchantPriceRoundDown() == null
                            || skuDO.getMerchantPriceRoundDown() <= 0) {
                            skuDO.setMerchantPriceRoundDown(skuDO.getMerchantPrice());
                        }
                        totalPriceRmb = totalPriceRmb
                                        + (skuDO.getMerchantPriceRoundDown() - skuDO
                                            .getMerchantActivityDiscountPrice())
                                        * skuDO.getQuantity();
                    }
                    if (totalCount == 0) {
                        LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                            .setErrorMsg("[供应链-计算商品平均价格]商品数量不能为0")
                            .addParm("出库单信息", stockoutOrderBO).addParm("商品ID", skuId).log();
                        throw new ServiceException(
                            LogisticsReturnCode.STOCKOUT_ORDER_PARAM_ILLIGAL, "商品数量和不能为零，商品ID："
                                                                              + skuId);
                    }

                    int averagePrice = totalPriceRmb / totalCount;
                    if (averagePrice == 0) {
                        //当发现商品销售价格为0时，初始化为1（解决口岸申报问题，弊端是总金额可能有一分钱的偏差）
                        skuAveragePriceMap.put(skuId, 1);
                    } else {
                        skuAveragePriceMap.put(skuId, averagePrice);
                    }

                }
            } else {
                throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_PARAM_ILLIGAL,
                    "商品ID：" + skuId);
            }
        }
        logger.info("[计算运费税费拆分]商品平均售价：" + skuAveragePriceMap.toString());
        return skuAveragePriceMap;
    }

    /**
     * 计算基础商品的平均活动折扣价格，如果存在一个SKUID对应多个活动折扣价格，则取平均值
     * 使用场景：海淘的秒杀商品与正常商品一起购买时，同一个SKUID存在不同价格，再与外部系统交互的时候是需要进行SKUID和价格合并的
     *
     * @param detailBOs
     * @return
     */
    private static Map<Long, Integer> calculationBasicSkuAverageActivityDiscountPrice(StockoutOrderBO stockoutOrderBO,
                                                                                      List<StockoutOrderDetailBO> detailBOs)
                                                                                                                            throws ServiceException {
        Map<Long, List<StockoutOrderDetailBO>> skuDOListBySkuIdSort = getBasicSkuListBySkuIdSort(detailBOs);
        Map<Long, Integer> skuAverageActivityDiscountPriceMap = new HashMap<Long, Integer>();
        for (Map.Entry<Long, List<StockoutOrderDetailBO>> entry : skuDOListBySkuIdSort.entrySet()) {
            Long skuId = entry.getKey();
            List<StockoutOrderDetailBO> skuDOList = entry.getValue();
            if (CollectionUtils.isNotEmpty(skuDOList)) {
                if (skuDOList.size() == 1) {
                    skuAverageActivityDiscountPriceMap.put(skuId, skuDOList.get(0)
                        .getMerchantActivityDiscountPrice());
                } else {
                    int totalCount = 0;
                    int totalActivityDiscountPriceRmb = 0;
                    for (StockoutOrderDetailBO skuDO : skuDOList) {
                        totalCount = totalCount + skuDO.getQuantity();
                        totalActivityDiscountPriceRmb = totalActivityDiscountPriceRmb
                                                        + skuDO.getMerchantActivityDiscountPrice()
                                                        * skuDO.getQuantity();
                    }
                    if (totalCount == 0) {
                        LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                            .setErrorMsg("[供应链-计算商品平均价格]商品数量不能为0")
                            .addParm("出库单信息", stockoutOrderBO).addParm("商品ID", skuId).log();
                        throw new ServiceException(
                            LogisticsReturnCode.STOCKOUT_ORDER_PARAM_ILLIGAL, "商品数量和不能为零，商品ID："
                                                                              + skuId);
                    }

                    // 上取整, 防止折扣价算低导致申报总价高出实付总额导致申报失败
                    BigDecimal averageActivityDiscountPrice = new BigDecimal(
                        totalActivityDiscountPriceRmb).divide(new BigDecimal(totalCount), 0,
                        BigDecimal.ROUND_UP);
                    skuAverageActivityDiscountPriceMap.put(skuId,
                        averageActivityDiscountPrice.intValue());
                }
            } else {
                throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_PARAM_ILLIGAL,
                    "商品ID：" + skuId);
            }
        }
        return skuAverageActivityDiscountPriceMap;
    }

    /**
     * 通过SKUID分类，获取基础商品的SKU列表
     *
     * @param detailBOs
     * @return
     */
    private static Map<Long, List<StockoutOrderDetailBO>> getBasicSkuListBySkuIdSort(List<StockoutOrderDetailBO> detailBOs) {
        if (CollectionUtils.isEmpty(detailBOs)) {
            return null;
        }
        Map<Long, List<StockoutOrderDetailBO>> skuBOListBySkuIdSort = new HashMap<Long, List<StockoutOrderDetailBO>>();
        for (StockoutOrderDetailBO skuBO : detailBOs) {
            List<StockoutOrderDetailBO> detailBOList;
            if (skuBOListBySkuIdSort.containsKey(skuBO.getSkuId())) {
                detailBOList = skuBOListBySkuIdSort.get(skuBO.getSkuId());
            } else {
                detailBOList = new ArrayList<StockoutOrderDetailBO>();
            }
            StockoutOrderDetailBO newDetailBO = new StockoutOrderDetailBO();
            BeanUtils.copyProperties(skuBO, newDetailBO);
            detailBOList.add(newDetailBO);
            skuBOListBySkuIdSort.put(skuBO.getSkuId(), detailBOList);
        }
        return skuBOListBySkuIdSort;
    }

    public static void main(String[] args) throws Exception {
        List<StockoutOrderDetailBO> stockoutOrderSkuDOs = new ArrayList<StockoutOrderDetailBO>();
        StockoutOrderDetailBO skuDO = new StockoutOrderDetailBO();
        skuDO.setSkuId(1001352L);
        skuDO.setSkuBatch("20180731-SFHT-57-EXP-3LSX");
        skuDO.setQuantity(1);
        skuDO.setMerchantPrice(4900);
        skuDO.setMerchantDiscountPrice(2464);
        stockoutOrderSkuDOs.add(skuDO);

        skuDO = new StockoutOrderDetailBO();
        skuDO.setSkuId(1001352L);
        skuDO.setSkuBatch("20180731-SFHT-57-EXP-3LSX");
        skuDO.setQuantity(2);
        skuDO.setMerchantPrice(3750);
        skuDO.setMerchantDiscountPrice(1884);
        stockoutOrderSkuDOs.add(skuDO);

        skuDO = new StockoutOrderDetailBO();
        skuDO.setSkuId(1001354L);
        skuDO.setSkuBatch("20180731-SFHT-57-EXP-3LSZ");
        skuDO.setQuantity(2);
        skuDO.setMerchantPrice(3750);
        skuDO.setMerchantDiscountPrice(1884);
        stockoutOrderSkuDOs.add(skuDO);

        //skuAverageMerchantPriceNotDiscountMap.get(1001352L).intValue(),2056);
        //skuAverageMerchantPriceNotDiscountMap.get(1001354L).intValue(),1866);
        Map<Long, Integer> skuAverageMerchantPriceNotDiscountMap = calculationBasicSkuAverageSellingPriceNotAllDiscount(
            new StockoutOrderBO(), stockoutOrderSkuDOs);

    }

    private void BPtax(SkuDeclareBO productDeclareEntity,
                       Integer skuContainTaxAndFreightOriginPrice, BigDecimal taxFactor,
                       BigDecimal addedValueTaxRate) {
        BigDecimal taxDisCountRate = new BigDecimal("0.7");
        String config = LogisticsDynamicConfig.getSplit().getRule("filter",
            "skuAloneStockoutWarehouse");
        String config1 = LogisticsDynamicConfig.getSplit().getRule("filter",
            "skuAloneStockoutWarehouse");
        List<String> firstWeightHsCodeList = Arrays.asList(config.split(","));
        List<String> secondWeightHsCodeList = Arrays.asList(config1.split(","));
        if (firstWeightHsCodeList.contains(productDeclareEntity.getHsCode().toString())) {
            BigDecimal criterionTax = new BigDecimal(skuContainTaxAndFreightOriginPrice).divide(
                new BigDecimal(productDeclareEntity.getFirstWeight()), 0, BigDecimal.ROUND_DOWN)
                .divide(new BigDecimal(10));
            if (criterionTax.compareTo(taxFactor) > 0) {
                BigDecimal molecule = taxDisCountRate.multiply(new BigDecimal(0.15)
                    .add(addedValueTaxRate));
                BigDecimal denominator = new BigDecimal(1).subtract(new BigDecimal(0.15));
                taxFactor = molecule.divide(denominator, 4, BigDecimal.ROUND_DOWN).add(
                    new BigDecimal(1));
                if (criterionTax.compareTo(taxFactor) < 0) {
                    skuContainTaxAndFreightOriginPrice = new BigDecimal(10)
                        .subtract(new BigDecimal(productDeclareEntity.getFirstWeight()))
                        .subtract(taxDisCountRate).intValue();
                }
            }

        }
        if (secondWeightHsCodeList.contains(productDeclareEntity.getHsCode().toString())) {
            BigDecimal criterionTax = new BigDecimal(skuContainTaxAndFreightOriginPrice).divide(
                new BigDecimal(productDeclareEntity.getSecondWeight()), 0, BigDecimal.ROUND_DOWN)
                .divide(new BigDecimal(15));
            if (criterionTax.compareTo(taxFactor) > 0) {
                BigDecimal molecule = taxDisCountRate.multiply(new BigDecimal(0.15)
                    .add(addedValueTaxRate));
                BigDecimal denominator = new BigDecimal(1).subtract(new BigDecimal(0.15));
                taxFactor = molecule.divide(denominator, 4, BigDecimal.ROUND_DOWN).add(
                    new BigDecimal(1));
                if (criterionTax.compareTo(taxFactor) < 0) {
                    skuContainTaxAndFreightOriginPrice = new BigDecimal(10)
                        .subtract(new BigDecimal(productDeclareEntity.getSecondWeight()))
                        .subtract(taxDisCountRate).intValue();
                }
            }
        }
    }

}
