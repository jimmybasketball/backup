package com.sfebiz.supplychain.persistence.base.merchant.manager;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.merchant.dao.MerchantDao;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantDO;

/**
 * 物流平台货主Manager
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
        DaoHelper.genXMLWithFeature(
            "/Users/liujunchi/git_projects/" + "supplychain/supplychain-persistence/"
                    + "src/main/resources/base/sqlmap/merchant/sc_merchant_sqlmap.xml",
            MerchantDao.class, MerchantDO.class, "sc_merchant");
    }

    /**
     * 判断货主账户ID是否已存在
     * @param id            主键ID
     * @param merchantAccountId    货主账户ID
     * @return
     */
    public boolean checkMerchantAccountIdIsExist(Long id, String merchantAccountId) {
        if (StringUtils.isBlank(merchantAccountId)) {
            return false;
        }
        MerchantDO queryDO = new MerchantDO();
        queryDO.setMerchantAccountId(merchantAccountId);
        BaseQuery<MerchantDO> query = new BaseQuery<MerchantDO>(queryDO);
        if (id != null && id != 0L) {
            query.addNotEquals("id", id);
        }
        long count = merchantDao.count(query);
        return count > 0;
    }

    /**
     * 判断货主联系人邮箱是否已存在
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

    /**
     * 根据货主账户标识，获取货主实体信息
     * 
     * @param merchantAccountId
     * @return
     */
    public MerchantDO getMerchantByMerchantAccountId(String merchantAccountId) {

        if (StringUtils.isBlank(merchantAccountId)) {
            return null;
        }
        MerchantDO queryDO = new MerchantDO();
        queryDO.setMerchantAccountId(merchantAccountId);
        BaseQuery<MerchantDO> query = new BaseQuery<MerchantDO>(queryDO);
        List<MerchantDO> merchantDOList = merchantDao.query(query);
        if (CollectionUtils.isNotEmpty(merchantDOList)) {
            return merchantDOList.get(0);
        }

        return null;
    }
}
