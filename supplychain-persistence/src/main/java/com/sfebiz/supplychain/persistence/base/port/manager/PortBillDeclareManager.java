package com.sfebiz.supplychain.persistence.base.port.manager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.exposed.common.enums.BillType;
import com.sfebiz.supplychain.persistence.base.port.dao.PortBillDeclareDao;
import com.sfebiz.supplychain.persistence.base.port.domain.PortBillDeclareDO;

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

    /**
     * 获取 支付单申报结果记录
     * 
     * @param stockoutOrderId
     * @param portId
     * @param businessType
     * @return
     */
    public PortBillDeclareDO getPayBillDOByStockoutOrderIdAndPortId(Long stockoutOrderId,
                                                                    Long portId, String businessType) {
        PortBillDeclareDO portBillDeclareDO = new PortBillDeclareDO();
        portBillDeclareDO.setBillId(stockoutOrderId);
        portBillDeclareDO.setPortId(portId);
        portBillDeclareDO.setBillType(BillType.PAY_BILL.getType());
        portBillDeclareDO.setBusinessType(businessType);
        List<PortBillDeclareDO> portBillDeclareDOs = portBillDeclareDao.query(BaseQuery
            .getInstance(portBillDeclareDO));
        if (portBillDeclareDOs != null && portBillDeclareDOs.size() > 0) {
            return portBillDeclareDOs.get(0);
        }
        return null;
    }

    @Override
    public BaseDao<PortBillDeclareDO> getDao() {
        return portBillDeclareDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
            "/Users/liujunchi/git_projects/" + "supplychain/supplychain-persistence/"
                    + "src/main/resources/base/sqlmap/port/sc_port_bill_declare_sqlmap.xml",
            PortBillDeclareDao.class, PortBillDeclareDO.class, "sc_port_bill_declare");
    }

}
