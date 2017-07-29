package com.sfebiz.supplychain.persistence.base.user.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.user.dao.UserAuthenticationLogDao;
import com.sfebiz.supplychain.persistence.base.user.domain.UserAuthenticationLogDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 实名认证第三方服务请求记录manager
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-27 09:35
 **/
@Component("userAuthenticationLogManager")
public class UserAuthenticationLogManager extends BaseManager<UserAuthenticationLogDO>{

    @Resource
    private UserAuthenticationLogDao userAuthenticationLogDao;

    @Override
    public BaseDao<UserAuthenticationLogDO> getDao() {
        return userAuthenticationLogDao;
    }



    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("/Users/liujunchi/git_projects/" + "supplychain/supplychain-persistence/"
                        + "src/main/resources/base/sqlmap/user/base_user_authentication_log-sqlmap.xml",
                UserAuthenticationLogDao.class, UserAuthenticationLogDO.class, "base_user_authentication_log");
    }
}
