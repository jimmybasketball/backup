package com.sfebiz.supplychain.service.customs.biz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 海关限制相关业务服务
 * 
 * Created by jianyuanyang on 15/11/25.
 */
@Component("customsLimitBizService")
public class CustomsLimitBizService {

    private final static Logger logger = LoggerFactory.getLogger(CustomsLimitBizService.class);

    //    @Resource
    //    private CustomsLimitDao     customsLimitDao;
    //
    //    @Resource
    //    private PaymentService      paymentService;
    //
    //    @Resource
    //    private PayerLimitManager   payerLimitManager;
    //
    //    @Override
    //    public CustomLimitEntity getCustomsMaxLimitByOrderId(String orderId, Integer portId) {
    //        CustomLimitEntity customLimitEntity = null;
    //        //参数验证
    //        if (StringUtils.isBlank(orderId) || null == portId) {
    //            logger.warn("orderId or portId is null");
    //            return customLimitEntity;
    //        }
    //        String portType = PortNid.getDescByCode(Long.valueOf(portId.toString()));
    //        if (portType.equals(PortNid.typeExceptionDesc)) {
    //            logger.warn("portId:{} has no portDesc:{}", portId, portType);
    //            return customLimitEntity;
    //        }
    //        try {
    //            //orderId 查询 支付人信息（支付接口）
    //            List<QueryBuyerRequest> queryBuyerRequestList = Lists.newArrayList();
    //            QueryBuyerRequest queryBuyerRequest = new QueryBuyerRequest();
    //            queryBuyerRequest.setOrderId(orderId);
    //            queryBuyerRequestList.add(queryBuyerRequest);
    //            List<OrderBuyerInfo> orderBuyerInfoList = paymentService
    //                .queryOrderBuyer(queryBuyerRequestList);
    //            if (CollectionUtils.isNotEmpty(orderBuyerInfoList)) {
    //                OrderBuyerInfo orderBuyerInfo = orderBuyerInfoList.get(0);
    //                String buyerId = orderBuyerInfo.buyerId;
    //                //buyerId，portId 查询，只获取一条记录(limit_time最大记录)
    //                BaseQuery<CustomsLimitDO> qy2 = BaseQuery.getInstance(new CustomsLimitDO());
    //                qy2.addEquals("port_id", portId);
    //                qy2.addEquals("buyer_id", buyerId);
    //                qy2.addOrderBy("limit_time", 0);
    //                List<CustomsLimitDO> customsLimitDOList2 = customsLimitDao.query(qy2);
    //                if (CollectionUtils.isNotEmpty(customsLimitDOList2)) {
    //                    CustomsLimitDO lastestCustomsLimitDO = customsLimitDOList2.get(0);
    //                    customLimitEntity = new CustomLimitEntity();
    //                    BeanUtils.copyProperties(customLimitEntity, lastestCustomsLimitDO);
    //                }
    //
    //                CustomLimitEntity customLimitEntityFromBI = getMaxLimitRecodeFromBI(portId, buyerId);
    //                if (null != customLimitEntityFromBI) {
    //                    if (null == customLimitEntity
    //                        || customLimitEntity.getLimitTime().before(
    //                            customLimitEntityFromBI.getLimitTime())) {
    //                        customLimitEntity = customLimitEntityFromBI;
    //                    }
    //                }
    //
    //            }
    //        } catch (Exception e) {
    //            logger.error("getCustomsMaxLimitByOrderId has e:{}", e);
    //        } finally {
    //            return customLimitEntity;
    //        }
    //    }
    //
    //    protected CustomLimitEntity getMaxLimitRecodeFromBI(Integer portId, String buyerId)
    //                                                                                       throws Exception {
    //
    //        CustomLimitEntity customLimitEntity = null;
    //        Date current = new Date();
    //        BaseQuery<PayerLimitDO> query = new BaseQuery<PayerLimitDO>(new PayerLimitDO());
    //        query.getData().setBuyerId(buyerId);
    //        query.getData().setPortId(portId);
    //        query.addIn(
    //            "port_pass_time",
    //            Arrays.asList(new String[] { DateUtil.formatDateStr(current, "yyyyMM"),
    //                    DateUtil.formatDateStr(current, "yyyy") }));
    //        List<PayerLimitDO> payerLimitDOList = payerLimitManager.query(query);
    //
    //        if (null != payerLimitDOList) {
    //            for (PayerLimitDO payerLimitDO : payerLimitDOList) {
    //                Date limitDate = null;
    //                if (StringUtils.equalsIgnoreCase(CustomsReturnReason.YEAR_EXCEED.getValue(),
    //                    payerLimitDO.getReason())) {
    //                    limitDate = DateUtil.getNextYearFirstDayRandomTime(null);
    //                } else {
    //                    limitDate = DateUtil.getNextMonthFirstDayRandomTime(null);
    //                }
    //                if (null == customLimitEntity || customLimitEntity.getLimitTime().before(limitDate)) {
    //                    customLimitEntity = convertPayerLimitDO(payerLimitDO);
    //                    customLimitEntity.setLimitTime(limitDate);
    //                }
    //            }
    //        }
    //
    //        return customLimitEntity;
    //    }
    //
    //    private CustomLimitEntity convertPayerLimitDO(PayerLimitDO payerLimitDO) {
    //        CustomLimitEntity customLimitEntity = new CustomLimitEntity();
    //        customLimitEntity.setBuyerId(payerLimitDO.getBuyerId());
    //        customLimitEntity.setPortId(payerLimitDO.getPortId());
    //        customLimitEntity.setReason(payerLimitDO.getReason());
    //        return customLimitEntity;
    //    }

}
