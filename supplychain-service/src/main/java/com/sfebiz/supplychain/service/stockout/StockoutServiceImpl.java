/**
 * Copyright (C) 2017 上海牵趣网络科技有限公司 版权所有
 */
package com.sfebiz.supplychain.service.stockout;

import org.springframework.stereotype.Service;

import com.sfebiz.supplychain.aop.annotation.ParamNotBlank;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.stockout.api.StockoutService;

/**
 * <p>
 * 出库
 * </p>
 * 
 * @author wuyun
 * @Date 2017年7月18日 下午2:38:08
 */
@Service("stockoutService")
public class StockoutServiceImpl implements StockoutService {

    /*
     * (non-Javadoc)
     * 
     * @see com.sfebiz.supplychain.exposed.stockout.api.StockoutService#
     * executeStockoutExceptionTaskByHandle(java.lang.Long, java.lang.Long, java.lang.String)
     */
    @Override
    public CommonRet<Void> executeStockoutExceptionTaskByHandle(
            @ParamNotBlank("业务订单ID不能为空") Long id, Long userId, String userName) {
        // TODO Auto-generated method stub
        return null;
    }

}
