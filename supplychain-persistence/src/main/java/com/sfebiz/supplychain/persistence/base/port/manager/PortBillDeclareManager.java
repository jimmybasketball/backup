package com.sfebiz.supplychain.persistence.base.port.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.port.dao.PortBillDeclareDao;
import com.sfebiz.supplychain.persistence.base.port.domain.PortBillDeclareDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/9
 * Time: 下午5:50
 */
@Component("portBillDeclareManager")
public class PortBillDeclareManager extends BaseManager<PortBillDeclareDO> {

    @Resource
    private PortBillDeclareDao portBillDeclareDao;

    /**
     * 获取待发送物流运单的列表
     *
     * @return
     */
    public List<PortBillDeclareDO> getWaitSendWayBillList() {
        return portBillDeclareDao.getWaitSendWayBillList();
    }

    @Override
    public BaseDao<PortBillDeclareDO> getDao() {
        return portBillDeclareDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("/Users/liujunchi/git_projects/" +
                        "supplychain/supplychain-persistence/" +
                        "src/main/resources/base/sqlmap/port/sc_port_bill_declare_sqlmap.xml",
                PortBillDeclareDao.class,
                PortBillDeclareDO.class,
                "sc_port_bill_declare");
    }

}
