package com.sfebiz.supplychain.persistence.base.route.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.route.dao.RegistRoutesLogDao;
import com.sfebiz.supplychain.persistence.base.route.domain.RegistRoutesLogDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 快递100订阅记录
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-08-01 10:03
 **/
@Component("registRoutesLogManager")
public class RegistRoutesLogManager extends BaseManager<RegistRoutesLogDO>{

    @Resource
    private RegistRoutesLogDao registRoutesLogDao;

    @Override
    public BaseDao<RegistRoutesLogDO> getDao() {
        return registRoutesLogDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
                "/Users/liujunchi/git_projects/" + "supplychain/supplychain-persistence/"
                        + "src/main/resources/base/sqlmap/route/sc_regist_routes_log_sqlmap.xml",
                RegistRoutesLogDao.class, RegistRoutesLogDO.class, "sc_regist_routes_log");
    }
}
