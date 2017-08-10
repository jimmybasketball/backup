package com.sfebiz.supplychain.service.stock.util;


import com.sfebiz.supplychain.persistence.base.stock.domain.StockBatchDO;

import java.util.Comparator;

/**
 * Created by wang_cl on 2015/3/15.
 */
public class ComparatorExpirationDate implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        StockBatchDO StockBatchDO1=(StockBatchDO)o1;
        StockBatchDO StockBatchDO2=(StockBatchDO)o2;

        if (StockBatchDO1.getExpirationDate() == null && StockBatchDO2.getExpirationDate() == null) {
            return 0;
        } else if (StockBatchDO1.getExpirationDate() == null){
            return 1;
        } else if (StockBatchDO2.getExpirationDate() == null) {
            return -1;
        } else {
            return StockBatchDO1.getExpirationDate().compareTo(StockBatchDO2.getExpirationDate());
        }
    }
}
