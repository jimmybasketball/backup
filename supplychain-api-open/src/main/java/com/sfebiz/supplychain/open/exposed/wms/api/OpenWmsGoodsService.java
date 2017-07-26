package com.sfebiz.supplychain.open.exposed.wms.api;

import net.pocrd.annotation.ApiGroup;
import net.pocrd.annotation.ApiParameter;
import net.pocrd.annotation.DesignedErrorCode;
import net.pocrd.annotation.HttpApi;
import net.pocrd.define.SecurityType;
import net.pocrd.entity.ServiceException;

import com.sfebiz.supplychain.open.exposed.api.SCOpenReturnCode;
import com.sfebiz.supplychain.open.exposed.wms.entity.WmsTradeOrderResponse;
import com.sfebiz.supplychain.open.exposed.wms.entity.request.WmsTradeOrderSkuSearchRequest;
import com.sfebiz.supplychain.open.exposed.wms.entity.request.WmsTradeOrderSkuSyncRequest;

/**
 * 
 * <p>开放云仓-商品服务接口</p>
 * @author matt
 * @Date 2017年7月17日 下午1:33:10
 */
@ApiGroup(name = "supplychain", minCode = 1000000, maxCode = 2000000, codeDefine = SCOpenReturnCode.class, owner = "matt")
public interface OpenWmsGoodsService {

	/**
	 * 商品信息同步
	 * @param RequestJson
	 */
	@HttpApi(name = "wmsTradeOpenApi.OrderSkuSync", desc = "仓库开放平台-商品信息同步 \nen-us:sync sku", security = SecurityType.Integrated, owner = "wuyun", needVerify = true)
	@DesignedErrorCode({ SCOpenReturnCode._C_COMMON_PARAMS_ILLEGAL})
	public WmsTradeOrderResponse wmsTradeOrderSkuSync(
            @ApiParameter(required = true, name = "request", desc = "请求 \nen-us Request") WmsTradeOrderSkuSyncRequest request)
					throws ServiceException;
	
	/**
	 * 商品信息查询
	 * @param RequestJson
	 * @return
	 */
	@HttpApi(name = "wmsTradeOpenApi.OrderSkuSearch", desc = "仓库开放平台-商品信息查询 \nen-us:sku search", security = SecurityType.Integrated, owner = "wuyun", needVerify = true)
	@DesignedErrorCode({ SCOpenReturnCode._C_COMMON_PARAMS_ILLEGAL})
	public WmsTradeOrderResponse wmsTradeOrderSkuSearch(
            @ApiParameter(required = true, name = "request", desc = "请求 \nen-us Request") WmsTradeOrderSkuSearchRequest request)
					throws ServiceException;
}
