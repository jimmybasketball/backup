package com.sfebiz.supplychain.persistence.base.stockout.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.domain.UpdateByQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.exposed.common.code.RouteReturnCode;
import com.sfebiz.supplychain.exposed.route.enums.RouteType;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>出库单manager类</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:49:29
 */
@Component("stockoutOrderManager")
public class StockoutOrderManager extends BaseManager<StockoutOrderDO> {

    @Resource
    private StockoutOrderDao stockoutOrderDao;

    @Override
    public BaseDao<StockoutOrderDO> getDao() {
        return stockoutOrderDao;
    }

    /**
     * 通过商户订单号和商户编码查询订单信息
     *
     * @param merchantOrderNo 商户订单号
     * @param merchantId      货主ID
     * @return
     */
    public StockoutOrderDO queryByMerchantOrderNoAndMerchantId(String merchantOrderNo,
                                                               Long merchantId) {
        StockoutOrderDO stockoutOrderDO = new StockoutOrderDO();
        stockoutOrderDO.setMerchantOrderNo(merchantOrderNo);
        stockoutOrderDO.setMerchantId(merchantId);
        BaseQuery<StockoutOrderDO> baseQuery = new BaseQuery<StockoutOrderDO>(stockoutOrderDO);
        List<StockoutOrderDO> stockoutOrderDOs = stockoutOrderDao.query(baseQuery);
        if (stockoutOrderDOs == null || stockoutOrderDOs.size() == 0) {
            return null;
        } else {
            return stockoutOrderDOs.get(0);
        }
    }

    /**
     * 根据bizId获取出库单对象
     *
     * @param bizId 业务ID
     * @return
     */
    public StockoutOrderDO getByBizId(String bizId) {
        if (StringUtils.isBlank(bizId)) {
            return null;
        }
        StockoutOrderDO query = new StockoutOrderDO();
        query.setBizId(bizId);
        BaseQuery<StockoutOrderDO> baseQuery = new BaseQuery<StockoutOrderDO>(query);
        List<StockoutOrderDO> stockoutOrderDOs = stockoutOrderDao.query(baseQuery);
        if (stockoutOrderDOs == null || stockoutOrderDOs.size() == 0) {
            return null;
        } else {
            return stockoutOrderDOs.get(0);
        }
    }

    /**
     * 更新出库单申报支付单号
     *
     * @param stockoutOrderId
     * @param payNo
     * @return
     * @author tanzx [tanzongxi@ifunq.com]
     * @date 2017/7/27 16:59
     */
    public int updatePayNo(long stockoutOrderId, String payNo) {
        StockoutOrderDO updateDO = new StockoutOrderDO();
        updateDO.setId(stockoutOrderId);
        updateDO.setDeclarePayNo(payNo);
        return this.update(updateDO);
    }

    /**
     * 更新出库单支付申报相关信息
     *
     * @param stockoutOrderId
     * @param payNo
     * @param payerName
     * @param payerCertNo
     * @return
     * @author tanzx [tanzongxi@ifunq.com]
     * @date 2017/7/28 14:46
     */
    public int updatePayBillInfo(long stockoutOrderId, String payNo, String payerName, String payerCertNo) {
        StockoutOrderDO updateDO = new StockoutOrderDO();
        updateDO.setId(stockoutOrderId);
        updateDO.setDeclarePayNo(payNo);
        updateDO.setDeclarePayerName(payerName);
        updateDO.setDeclarePayerCertNo(payerCertNo);
        return this.update(updateDO);
    }


    /**
     * 根据运单号出库单信息
     * @param mailNo        运单号
     * @return
     */
    public StockoutOrderDO getByMailNo(String mailNo) throws ServiceException{
        if (StringUtils.isBlank(mailNo)) {
            return null;
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("mailNo", mailNo);
        List<StockoutOrderDO> stockoutOrderDOS = stockoutOrderDao.getByMailNo(paramMap);
        if (stockoutOrderDOS != null && stockoutOrderDOS.size() > 0) {
            if (stockoutOrderDOS.size() > 1) {
                throw new ServiceException(RouteReturnCode.MAIL_NO_FIND_MULTI_STOCKOUT_ORDER);
            }
        }
        return null;
    }

    /**
     * 修改出库单承运商编码
     * @param id
     * @param routeType
     * @param carrierCode
     */
    public void updateCarrierCodeById(Long id, RouteType routeType, String carrierCode) {
        if (id == null || routeType == null || StringUtils.isBlank(carrierCode)) {
            return;
        }

        StockoutOrderDO update = new StockoutOrderDO();
        update.setId(id);
        if (RouteType.INTERNAL == routeType) {
            update.setIntrCarrierCode(carrierCode);
        } else if (RouteType.INTERNATIONAL == routeType) {
            update.setIntlCarrierCode(carrierCode);
        } else {
            return;
        }

        stockoutOrderDao.update(update);
    }

    /**
     * 更新出库单计费重量
     *
     * @param stockoutOrderId
     * @param calWeight 计费重量 g为单位
     * @return
     *
     * @author tanzx [tanzongxi@ifunq.com]
     * @date 2017/8/6 22:51
     */
    public int updateCalWeight(long stockoutOrderId, int calWeight) {
        StockoutOrderDO stockoutOrderDO = new StockoutOrderDO();
        stockoutOrderDO.setId(stockoutOrderId);
        BaseQuery<StockoutOrderDO> q = BaseQuery.getInstance(stockoutOrderDO);
        StockoutOrderDO e = new StockoutOrderDO();
        e.setActualWeight(calWeight);
        e.setCalWeight(calWeight);
        UpdateByQuery<StockoutOrderDO> up = new UpdateByQuery<StockoutOrderDO>(q, e);
        return this.updateByQuery(up);
    }

    /**
     * 更新出库单国内运单号
     *
     * @param stockoutOrderId
     * @param intrMailNo
     * @return
     *
     * @author tanzx [tanzongxi@ifunq.com]
     * @date 2017/8/6 22:51
     */
    public int updateMailNo(long stockoutOrderId, String intrMailNo) {
        StockoutOrderDO stockoutOrderDO = new StockoutOrderDO();
        stockoutOrderDO.setId(stockoutOrderId);
        BaseQuery<StockoutOrderDO> q = BaseQuery.getInstance(stockoutOrderDO);
        StockoutOrderDO e = new StockoutOrderDO();
        e.setIntrMailNo(intrMailNo);
        UpdateByQuery<StockoutOrderDO> up = new UpdateByQuery<StockoutOrderDO>(q, e);
        return this.updateByQuery(up);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order-sqlmap.xml", StockoutOrderDao.class,
                StockoutOrderDO.class, "sc_stockout_order");
    }
}
