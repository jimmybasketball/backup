package com.sfebiz.supplychain.persistence.base.merchant.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.exposed.stock.enums.StockBatchStateType;
import com.sfebiz.supplychain.persistence.base.merchant.dao.MerchantProviderLineDao;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantProviderLineDO;

/**
 * 供应商线路管理
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-07 18:11
 **/
@Component("merchantProviderLineManager")
public class MerchantProviderLineManager extends BaseManager<MerchantProviderLineDO>{

    @Resource
    private MerchantProviderLineDao merchantProviderLineDao;

    @Override
    public BaseDao<MerchantProviderLineDO> getDao() {
        return merchantProviderLineDao;
    }

    /**
     * 根据供应商id和仓库id查询
     * @param providerId
     * @param warehouseId
     * @return
     */
    public List<MerchantProviderLineDO> getByProviderAndWarehouse(Long providerId, Long warehouseId){
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put("providerId", providerId);
        paramMap.put("warehouseId", warehouseId);
        return merchantProviderLineDao.getByProviderAndWarehouse(paramMap);
    }
    
    /**
     * 根据货主的供应商id和线路id，获取货主供应商线路实体
     * 
     * @param merchantProviderId
     * @param lineId
     * @return
     */
    public List<MerchantProviderLineDO> getByMerchantProviderIdAndLineId(Long merchantProviderId, Long lineId){
        MerchantProviderLineDO queryDO = new MerchantProviderLineDO();
        queryDO.setMerchantProviderId(merchantProviderId);
        queryDO.setLineId(lineId);
        queryDO.setState(StockBatchStateType.ENABLE.value);
        BaseQuery<MerchantProviderLineDO> baseQuery = new BaseQuery<MerchantProviderLineDO>(queryDO);
        return merchantProviderLineDao.query(baseQuery);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("/Users/liujunchi/git_projects/" +
                        "supplychain/supplychain-persistence/" +
                        "src/main/resources/base/sqlmap/merchant/sc_merchant_provider_line_sqlmap.xml",
                MerchantProviderLineDao.class,
                MerchantProviderLineDO.class,
                "sc_merchant_provider_line");
    }
}
