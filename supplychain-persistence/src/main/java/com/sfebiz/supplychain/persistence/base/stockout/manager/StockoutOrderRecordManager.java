package com.sfebiz.supplychain.persistence.base.stockout.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderRecordDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderRecordDO;

/**
 * 
 * <p>出库单记录manager类</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:49:29
 */
@Component("stockoutOrderRecordManager")
public class StockoutOrderRecordManager extends BaseManager<StockoutOrderRecordDO> {

    @Resource
    private StockoutOrderRecordDao stockoutOrderRecordDao;

    @Override
    public BaseDao<StockoutOrderRecordDO> getDao() {
        return stockoutOrderRecordDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order_record-sqlmap.xml",
            StockoutOrderRecordDao.class, StockoutOrderRecordDO.class, "sc_stockout_order_record");
    }
}
