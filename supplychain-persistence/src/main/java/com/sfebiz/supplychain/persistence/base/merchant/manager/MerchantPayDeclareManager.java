package com.sfebiz.supplychain.persistence.base.merchant.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.exposed.merchant.enums.MerchantProviderStateType;
import com.sfebiz.supplychain.persistence.base.merchant.dao.MerchantPayDeclareDao;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantPayDeclareDO;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantProviderDO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 货主申报方式Manager
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-19 11:34
 **/
@Component("merchantPayDeclareManager")
public class MerchantPayDeclareManager extends BaseManager<MerchantPayDeclareDO> {

    @Resource
    private MerchantPayDeclareDao merchantPayDeclareDao;


    @Override
    public BaseDao<MerchantPayDeclareDO> getDao() {
        return merchantPayDeclareDao;
    }


    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("/Users/liujunchi/git_projects/" +
                        "supplychain/supplychain-persistence/" +
                        "src/main/resources/base/sqlmap/merchant/sc_merchant_pay_declare_sqlmap.xml",
                MerchantPayDeclareDao.class,
                MerchantPayDeclareDO.class,
                "sc_merchant_pay_declare");
    }


    /**
     * 检查货主对应口岸和支付方式的申报配置是否已存在
     *
     * @param merchantId
     * @param payType
     * @param portId
     * @return
     */
    public boolean checkMerchantPayDeclareIsExist(Long merchantId, String payType, Long portId) {
        if (merchantId == null
                || portId == null
                || StringUtils.isBlank(payType)) {
            return false;
        }

        MerchantPayDeclareDO queryDO = new MerchantPayDeclareDO();
        queryDO.setMerchantId(merchantId);
        queryDO.setPayType(payType);
        queryDO.setPortId(portId);

        BaseQuery<MerchantPayDeclareDO> query = new BaseQuery<MerchantPayDeclareDO>(queryDO);
        long count = merchantPayDeclareDao.count(query);
        return count > 0;
    }

    /**
     * 检查货主是否配置申报方式
     * @param merchantId  货主ID
     * @return
     */
    public boolean isMerchantSetPayDeclare(Long merchantId) {

        if (merchantId == null) {
            return false;
        }

        MerchantPayDeclareDO queryDO = new MerchantPayDeclareDO();
        queryDO.setMerchantId(merchantId);
        BaseQuery<MerchantPayDeclareDO> baseQuery = new BaseQuery<MerchantPayDeclareDO>(queryDO);

        return merchantPayDeclareDao.count(baseQuery) > 0;
    }
}
