package com.sfebiz.supplychain.persistence.base.port.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.port.domain.PortBillDeclareDO;
import java.util.List;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/9
 * Time: 下午5:48
 */
public interface PortBillDeclareDao extends BaseDao<PortBillDeclareDO> {

    /**
     * 获取待发送物流运单的列表
     * @return
     */
    public List<PortBillDeclareDO> getWaitSendWayBillList();

}
