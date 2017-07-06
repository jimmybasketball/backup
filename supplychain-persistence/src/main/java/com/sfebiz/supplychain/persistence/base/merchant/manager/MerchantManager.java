package com.sfebiz.supplychain.persistence.base.merchant.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.merchant.dao.MerchantDao;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 物流平台商户Manager
 *
 * @author liujc
 * @create 2017-07-04 18:23
 **/
@Component("merchantManager")
public class MerchantManager extends BaseManager<MerchantDO> {

    @Resource
    private MerchantDao merchantDao;

    @Override
    public BaseDao<MerchantDO> getDao() {
        return merchantDao;
    }


    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("/Users/liujunchi/git_projects/" +
                        "supplychain/supplychain-persistence/" +
                        "src/main/resources/base/sqlmap/merchant/sc_merchant.xml",
                MerchantDao.class,
                MerchantDO.class,
                "sc_merchant");
    }


    /**
     * 判断商户账户ID是否已存在
     * @param id            主键ID
     * @param merchantId    商户账户ID
     * @return
     */
    public boolean checkMerchantIdIsExist(Long id, String merchantId){
        if (StringUtils.isBlank(merchantId)) {
            return false;
        }
        MerchantDO queryDO = new MerchantDO();
        queryDO.setMerchantId(merchantId);
        BaseQuery<MerchantDO> query = new BaseQuery<MerchantDO>(queryDO);
        if (id != null && id != 0L) {
            query.addNotEquals("id", id);
        }
        long count = merchantDao.count(query);
        return count > 0;
    }

    /**
     * 判断商户联系人邮箱是否已存在
     * @param id
     * @param email
     * @return
     */
    public boolean checkMerchantLinkmanEmailIsExist(Long id, String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        MerchantDO queryDO = new MerchantDO();
        queryDO.setLinkmanEmail(email);
        BaseQuery<MerchantDO> query = new BaseQuery<MerchantDO>(queryDO);
        if (id != null && id != 0L) {
            query.addNotEquals("id", id);
        }
        long count = merchantDao.count(query);
        return count > 0;
    }

}
