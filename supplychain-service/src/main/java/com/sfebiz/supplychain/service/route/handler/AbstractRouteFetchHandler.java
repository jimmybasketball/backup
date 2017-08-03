package com.sfebiz.supplychain.service.route.handler;

import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import net.pocrd.entity.ServiceException;

/**
 * 路由获取抽象处理者，  具体的链条关系请查看springXml的每个bean的配置
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-08-02 10:09
 **/
public abstract class AbstractRouteFetchHandler {


    /**
     * 作为责任链的下一个处理者
     */
    private AbstractRouteFetchHandler nextHandler;


    /**
     * 具体执行路由获取更新的方法
     *
     * @param stockoutOrderBO 出库单业务对象
     * @return 是否需要继续发送路由获取消息，仅代表本段路由执行的态度，仅在链条尾端的处理结果才能决定是否继续轮询
     */
    protected abstract boolean doFetch(StockoutOrderBO stockoutOrderBO) throws ServiceException;

    /**
     * 根据出库单BO对象获取用户路由信息
     *
     * @param stockoutOrderBO 出库单业务对象
     * @return 是否需要继续发送路由获取消息
     */
    public boolean fetchRouteByStockOrder(StockoutOrderBO stockoutOrderBO) throws ServiceException {
        boolean isPolling = doFetch(stockoutOrderBO);
        if (nextHandler != null) {
            //如果有的话，交给下一段路由获取更新处理器执行
            return nextHandler.fetchRouteByStockOrder(stockoutOrderBO);
        } else {
            //本段路由做为链条的结尾，有权利决定是否还要继续轮询
            return isPolling;
        }
    }

    public void setNextHandler(AbstractRouteFetchHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

}
