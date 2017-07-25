package com.sfebiz.supplychain.persistence.base.stockin.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockin.dao.StockinOrderFileDao;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderFileDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/1/1 下午6:27
 */
 @Component("stockinOrderFileManager")
public class StockinOrderFileManager extends BaseManager<StockinOrderFileDO> {

    @Resource
    StockinOrderFileDao stockinOrderFileDao;

    @Override
    public BaseDao<StockinOrderFileDO> getDao() {
        return stockinOrderFileDao;
    }


    public static void main(String[] args) {
        DaoHelper.genXML(
                "E:\\work\\cqcode\\haitao-b2b-supplychain\\supplychain-persistence\\src\\main\\resources\\base\\sqlmap\\stockin\\stockin_order_file_sqlmap.xml",
                StockinOrderFileDao.class,
                StockinOrderFileDO.class,
                "sc_stockin_order_file",
                true);
    }
}
