package com.sfebiz.supplychain.persistence.base.stockin.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockin.dao.StockinOrderDetailDao;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDetailDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhangyajing on 2017/7/12.
 */
@Component("stockinOrderDetailManager")
public class StockinOrderDetailManager extends BaseManager<StockinOrderDetailDO> {

    @Resource
    private StockinOrderDetailDao stockinOrderDetailDao;

    @Override
    public BaseDao<StockinOrderDetailDO> getDao() {
        return stockinOrderDetailDao;
    }

    public void updateByBarcodeAndSkuId(StockinOrderDetailDO stockinOrderDetailDO) {
        stockinOrderDetailDao.updateByBarcodeAndSkuId(stockinOrderDetailDO);
    }

    public StockinOrderDetailDO getByStockinOrderIdAndSkuId(Long stockinOrderId, Long skuId) {
        StockinOrderDetailDO detailDO = new StockinOrderDetailDO();
        detailDO.setStockinOrderId(stockinOrderId);
        detailDO.setSkuId(skuId);
        BaseQuery<StockinOrderDetailDO> query = new BaseQuery<StockinOrderDetailDO>(detailDO);
        List<StockinOrderDetailDO> result = stockinOrderDetailDao.query(query);
        if (null != result && result.size() > 0) {
             return result.get(0);
        } else {
            return null;
        }
    }

    public List<StockinOrderDetailDO> queryByStockinOrderId(Long stockinOrderId){
        StockinOrderDetailDO stockinOrderDetailDO = new StockinOrderDetailDO();
        stockinOrderDetailDO.setStockinOrderId(stockinOrderId);
        return stockinOrderDetailDao.query(BaseQuery.getInstance(stockinOrderDetailDO));
    }

    public static void main(String[] args){
        DaoHelper.genXMLWithFeature("E:\\work\\cqcode\\haitao-b2b-supplychain\\supplychain-persistence\\src\\main\\resources\\base\\sqlmap\\stockin\\sc_stockin_order_detail_sqlmap.xml"
        ,StockinOrderDetailDao.class
        ,StockinOrderDetailDO.class
        ,"sc_stockin_order_detail");
    }
}
