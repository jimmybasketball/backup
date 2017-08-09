package com.sfebiz.supplychain.service.stockout.util;

import com.sfebiz.supplychain.service.stockout.enums.BizActionType;

public class StockoutOrderKeyUtil {

    public static final String STOCKOUT_DISTRIBUTED_LOCK_PREFIX = "SC_STOCKOUT";

    /**
     * 构建创建出库单分布式锁的key
     * 
     * @param merchantAccountId 货主id
     * @param merchantOrderId 商户订单号
     */
    public static String buildDistributedLockKey(String merchantAccountId, String merchantOrderId,
                                                 BizActionType action) {
        return STOCKOUT_DISTRIBUTED_LOCK_PREFIX + ":" + action.getAction() + ":"
               + merchantAccountId + ":" + merchantOrderId;
    }
}
