package com.sfebiz.supplychain.persistence.base.line.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.line.dao.LogisticsLineDao;
import com.sfebiz.supplychain.persistence.base.line.domain.LogisticsLineDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 线路配置Manager
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-11 11:20
 **/
@Component("logisticsLineManager")
public class LogisticsLineManager extends BaseManager<LogisticsLineDO>{

    @Resource
    private LogisticsLineDao logisticsLineDao;

    @Override
    public BaseDao<LogisticsLineDO> getDao() {
        return logisticsLineDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("/Users/liujunchi/git_projects/" +
                        "supplychain/supplychain-persistence/" +
                        "src/main/resources/base/sqlmap/line/sc_logistics_line_sqlmap.xml",
                LogisticsLineDao.class,
                LogisticsLineDO.class,
                "sc_logistics_line");
    }


}
