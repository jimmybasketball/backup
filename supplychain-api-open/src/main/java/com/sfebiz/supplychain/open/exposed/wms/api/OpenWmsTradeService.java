package com.sfebiz.supplychain.open.exposed.wms.api;

import java.util.List;

import net.pocrd.annotation.ApiGroup;
import net.pocrd.annotation.ApiParameter;
import net.pocrd.annotation.DesignedErrorCode;
import net.pocrd.annotation.HttpApi;
import net.pocrd.define.SecurityType;
import net.pocrd.entity.ServiceException;

import com.sfebiz.supplychain.open.exposed.api.SCOpenReturnCode;
import com.sfebiz.supplychain.open.exposed.wms.entity.WmsOrderRoutesResult;
import com.sfebiz.supplychain.open.exposed.wms.entity.request.OpenWmsTradeOrderCreateRequest;

/**
 * 
 * <p>
 * 开放云仓-出库单相关服务API
 * </p>
 * 
 * @author matt
 * @Date 2017年7月17日 上午10:57:07
 */
@ApiGroup(name = "supplychain", minCode = 1000000, maxCode = 2000000, codeDefine = SCOpenReturnCode.class, owner = "zhangyajing")
public interface OpenWmsTradeService {

    @HttpApi(name = "wmsTradeOpenApi.orderRouteSearch", desc = "仓库开放平台-查询出库单路由信息 \nen-us:Return order routes", security = SecurityType.Integrated, owner = "zhangyajing", needVerify = true)
    @DesignedErrorCode({ SCOpenReturnCode._C_COMMON_SYSTEM_MAINTAINING,
            SCOpenReturnCode._C_COMMON_PARAMS_ILLEGAL })
    public List<WmsOrderRoutesResult> orderRouteSearch(@ApiParameter(required = true, name = "customerCode", desc = "货主编码 \nen-us Customer code") String customerCode,
                                                       @ApiParameter(required = true, name = "orderNoList", desc = "客户销售订单号列表 \nen-us Customer sales order list") List<String> orderNoList)
                                                                                                                                                                                           throws ServiceException;

    /**
     * 云仓交易出库单创建
     * 
     * @param request
     *            接口请求参数
     * @return
     * @throws net.pocrd.entity.ServiceException
     */
    @HttpApi(name = "wmsTradeOpenApi.createOrder", desc = "仓库开放平台-创建出库单 \nen-us:Create order", security = SecurityType.Integrated, owner = "matt", needVerify = true)
    @DesignedErrorCode({ SCOpenReturnCode._C_COMMON_PARAMS_ILLEGAL })
    public boolean createOrder(@ApiParameter(required = true, name = "request", desc = "请求 \nen-us Request") OpenWmsTradeOrderCreateRequest request)
                                                                                                                                                    throws ServiceException;

}
