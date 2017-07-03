package com.sfebiz.supplychain.persistence.base.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.dao.DemoDao;
import com.sfebiz.supplychain.persistence.base.domain.DemoDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 测试用例
 *
 * @author liujc
 * @create 2017-06-30 09:54
 **/
@Component("demoManager")
public class DemoManager extends BaseManager<DemoDO>{

    @Resource
    private DemoDao demoDao;

    @Override
    public BaseDao<DemoDO> getDao() {
        return demoDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("/Users/liujunchi/git_projects/" +
                "supplychain/supplychain-persistence/" +
                "src/main/resources/base/sqlmap/demo/sc_demo.xml",
                DemoDao.class,
                DemoDO.class,
                "sc_demo");
    }
}
