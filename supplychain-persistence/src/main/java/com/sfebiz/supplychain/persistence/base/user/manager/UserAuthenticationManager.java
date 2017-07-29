package com.sfebiz.supplychain.persistence.base.user.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.exception.DataAccessException;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.user.dao.UserAuthenticationDao;
import com.sfebiz.supplychain.persistence.base.user.domain.UserAuthenticationDO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 
 * <p>
 * 用户实名认证信息管理理类
 * </p>
 * 
 * @author matt
 * @Date 2017年7月7日 上午10:44:58
 */
@Component("userAuthentication")
public class UserAuthenticationManager extends BaseManager<UserAuthenticationDO> {

    @Resource
    private UserAuthenticationDao userAuthenticationDao;

    @Override
    public BaseDao<UserAuthenticationDO> getDao() {
        return userAuthenticationDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("/Users/liujunchi/git_projects/" + "supplychain/supplychain-persistence/"
                        + "src/main/resources/base/sqlmap/user/base_user_authentication-sqlmap.xml",
                UserAuthenticationDao.class, UserAuthenticationDO.class, "base_user_authentication");
    }

    /**
     * 根据证件号获取实名认证信息
     * 
     * @param idNo
     * @return
     * @throws DataAccessException
     */
    public UserAuthenticationDO getIdNo(String idNo) throws DataAccessException {
        BaseQuery<UserAuthenticationDO> query = new BaseQuery<UserAuthenticationDO>(
            new UserAuthenticationDO());
        query.getData().setIdNo(idNo);
        List<UserAuthenticationDO> receiverCreditInfoDOList = userAuthenticationDao.query(query);
        if (CollectionUtils.isNotEmpty(receiverCreditInfoDOList)) {
            return receiverCreditInfoDOList.get(0);
        }
        return null;
    }
}
