package com.sfebiz.supplychain.persistence.base.merchant.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.merchant.dao.MerchantProviderLineDao;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantProviderLineDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<MerchantProviderLineDO> getByProviderAndWarehouse(Long merchantId, Long providerId, Long warehouseId){
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put("providerId", providerId);
        paramMap.put("warehouseId", warehouseId);
        paramMap.put("merchantId",merchantId);
        return merchantProviderLineDao.getByProviderAndWarehouse(paramMap);
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
