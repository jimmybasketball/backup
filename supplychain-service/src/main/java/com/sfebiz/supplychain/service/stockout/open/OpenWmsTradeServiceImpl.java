package com.sfebiz.supplychain.service.stockout.open;

import java.util.List;

import net.pocrd.entity.ServiceException;

import org.springframework.stereotype.Service;

import com.sfebiz.supplychain.open.exposed.wms.api.OpenWmsTradeService;
import com.sfebiz.supplychain.open.exposed.wms.entity.WmsOrderRoutesResult;
import com.sfebiz.supplychain.open.exposed.wms.entity.request.OpenWmsTradeOrderCreateRequest;

@Service("openWmsTradeService")
public class OpenWmsTradeServiceImpl implements OpenWmsTradeService {

    @Override
    public List<WmsOrderRoutesResult> orderRouteSearch(String customerCode, List<String> orderNoList)
                                                                                                     throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean createOrder(OpenWmsTradeOrderCreateRequest request) throws ServiceException {
        // TODO Auto-generated method stub
        return false;
    }

}
