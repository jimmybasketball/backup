package com.sfebiz.supplychain.persistence.base.merchant.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.merchant.dao.MerchantPackageMaterialDao;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantPackageMaterialDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 货主包材manager
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-20 12:48
 **/
@Component("merchantPackageMaterialManager")
public class MerchantPackageMaterialManager extends BaseManager<MerchantPackageMaterialDO>{

    @Resource
    private MerchantPackageMaterialDao merchantPackageMaterialDao;

    @Override
    public BaseDao<MerchantPackageMaterialDO> getDao() {
        return merchantPackageMaterialDao;
    }

    /**
     * 检查货主包材配置是否已存在
     * @param id            主键ID（编辑操作时判断依据）
     * @param merchantId    货主ID
     * @param orderSource   订单来源
     * @return
     */
    public boolean checkMerchantPackageMaterialIsExist(Long id, Long merchantId, String orderSource){
        if (merchantId == null || StringUtils.isBlank(orderSource)) {
            return false;
        }
        MerchantPackageMaterialDO queryDO = new MerchantPackageMaterialDO();
        queryDO.setMerchantId(merchantId);
        queryDO.setOrderSource(orderSource);
        BaseQuery<MerchantPackageMaterialDO> query = new BaseQuery<MerchantPackageMaterialDO>(queryDO);
        if (id != null && id != 0L) {
            query.addNotEquals("id", id);
        }
        long count = merchantPackageMaterialDao.count(query);
        return count > 0;
    }

    /**
     * 检查货主是否配置包材
     * @param merchantId  货主ID
     * @return
     */
    public boolean isMerchantSetPackageMaterial(Long merchantId) {

        if (merchantId == null) {
            return false;
        }

        MerchantPackageMaterialDO queryDO = new MerchantPackageMaterialDO();
        queryDO.setMerchantId(merchantId);
        BaseQuery<MerchantPackageMaterialDO> baseQuery = new BaseQuery<MerchantPackageMaterialDO>(queryDO);

        return merchantPackageMaterialDao.count(baseQuery) > 0;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("D:/development/IDEA/ifunq-supplychain/haitao-b2b-supplychain/" +
                        "supplychain-persistence/src/main/resources/base/sqlmap/merchant/sc_merchant_package_material_sqlmap.xml",
                MerchantPackageMaterialDao.class,
                MerchantPackageMaterialDO.class,
                "sc_merchant_package_material");
    }
}
