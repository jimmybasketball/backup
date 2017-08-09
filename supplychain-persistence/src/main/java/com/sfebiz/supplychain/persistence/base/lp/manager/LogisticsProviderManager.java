package com.sfebiz.supplychain.persistence.base.lp.manager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.lp.dao.LogisticsProviderDao;
import com.sfebiz.supplychain.persistence.base.lp.domain.LogisticsProviderDO;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderBuyerDao;

/**
 * 
 * <p>manager类</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:49:29
 */
@Component("logisticsProviderManager")
public class LogisticsProviderManager extends BaseManager<LogisticsProviderDO> {

    @Resource
    private LogisticsProviderDao logisticsProviderDao;

    @Override
    public BaseDao<LogisticsProviderDO> getDao() {
        return logisticsProviderDao;
    }

    /**
     * 根据 物流供应商标识列表 批量获取 物流提供商列表 信息
     * 
     * @param nidList
     * @return
     */
    public List<LogisticsProviderDO> queryLogisticsProviderByNidList(List<String> nidList) {
        LogisticsProviderDO queryDO = new LogisticsProviderDO();
        BaseQuery<LogisticsProviderDO> baseQuery = new BaseQuery<LogisticsProviderDO>(queryDO);
        baseQuery.addIn("logistics_provider_nid", nidList);
        return logisticsProviderDao.query(baseQuery);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_logistics_provider-sqlmap.xml",
            StockoutOrderBuyerDao.class, LogisticsProviderDO.class, "sc_logistics_provider");
    }
}
