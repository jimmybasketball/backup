package com.sfebiz.supplychain.exposed.warehouse.enums;

/**
 * <p>与仓库交互的消息类型</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 14/12/31
 * Time: 下午10:14
 */
public enum WmsMessageType {

    //系统发送给物流企业的消息类型
    REMAINDER_COUNT("logistics.event.tpl.counter", "出库报文", "SF->LP", ""),

    //系统发送给仓库的消息类型
    TWS_TRANS_ORDER_FORECAST("logistics.event.tws.trans.create","中转仓转运订单预报","SF-LP",""),
    TWS_STORE_ORDER_FORECAST("logistics.event.tws.store.create","中转仓集货订单预报","SF-LP",""),
    STOCK_OUT("logistics.event.wms.create", "出库报文", "SF->LP", ""),
    STOCK_OUT_QUERY("logistics.event.wms.query", "出库报文", "SF->LP", ""),
    STOCK_IN("logistics.event.wms.skustockin", "入库报文", "SF->LP", ""),
    CUSTOMS_STOCK_IN("logistics.event.wms.customsskustockin", "海关退货入库报文", "SF->LP", ""),
    RE_STOCK_IN("logistics.event.wms.reskustockin", "重新下发入库报文", "SF->LP", ""),
    STOCK_IN_CANCEL("logistics.event.wms.skustockin.cancel", "取消入库报文", "SF->LP", ""),
    DELIVER_GOODS("logistics.event.wms.send", "发货报文", "SF->LP", ""),
    CANCEL_DELIVER("logistics.event.wms.cancel", "取消发货报文", "SF->LP", ""),
    CONFIRM_TARIFF("logistics.event.wms.confirm", "确认关税报文", "SF->LP", ""),
    DISSENT_MSG("logistics.event.wms.dissent", "异议消息报文", "SF->LP", ""),
    CLEAR_IDENTITY("logistics.event.wms.tm.clearidentity", "身份证信息", "SF->LP", ""),
    REFUND_GOODS("logistics.event.wms.refund", "退货信息", "SF->LP", ""),
    UPDATE_STOCK_OUT("logistics.event.wms.update", "轮询出库信息并更新出库单", "SF->LP", ""),
    GET_ROUTES("logistics.event.wms.getroutes", "获取路由信息", "SF->LP", ""),
    BOM_SYNC("logistics.event.wms.bomsync", "同步bom物料给仓库", "SF->LP", ""),
    SKU_SYNC("logistics.event.wms.skusync", "同步商品基本信息给仓库", "SF->LP", ""),
    RE_STOCK_OUT("logistics.event.wms.recreate", "重新下发出库报文", "SF->LP", ""),
    SUPPLIER_SYNC("logistics.event.wms.suppliersync","供应商同步","SF->LP",""),
    CONFIRM_WEIGHT("logistics.event.wms.confirm_weight","向仓库确认重量","SF->LP",""),
    STOCK_SEARCH("logistics.event.wms.stocksearch","向仓库查询库存","SF->LP",""),
    GAL_ORDER_CONFIRM("logistics.event.wms.galorder.confirm", "向仓库确认损益操作", "SF->LP", ""),
    TRANSFER_STOCK_OUT("logistics.event.wms.transferout.create", "向仓库发送调拨出库单", "SF->LP", ""),
    PURCHASE_RETURN_STOCK_OUT("logistics.event.wms.purchasereturn.create", "向仓库发送采退出库单出库报文", "SF->LP", ""),
    //FineEx 定义请求消息类型
    FINEEX_WMS_PRODUCTS_SYNC("fineex.wms.products.sync","商品信息同步接口","SF->LP",""),
    FINEEX_WMS_PURCHASE_OUTINORDER_ADD("fineex.wms.purchase.outinorder.add","创建出入库单接口；采购入库、调拨出库。","SF->LP",""),
    FINEEX_WMS_PURCHASE_OUTINORDER_CONFIRM("fineex.wms.purchase.outinorder.confirm","出入库单确认接口","SF->LP",""),
    FINEEX_WMS_TRADES_ADD("fineex.wms.trades.add","订单创建接口","",""),
    FINEEX_WMS_TRADES_CONFIRM("fineex.wms.trade.orderdetail.confirm","订单确认接口(带明细)","SF->LP",""),

    //仓库返回的回调消息类型
    CALLBACK_CHECKORDER("logistics.event.wms.checkorder", "回传审单信息", "LP->SF", ""),
    CALLBACK_TRANSPORT_STOCKIN("logistics.event.wms.stockin", "仓库回传转运入库信息", "LP->SF", ""),

    CALLBACK_SELF_STOCKIN("logistics.event.wms.skustockin.info", "仓库回传自营入库信息", "LP->SF", ""),
    CALLBACK_WEIGHT("logistics.event.wms.weight", "回传仓库称重信息", "LP->SF", ""),
    CALLBACK_EXCEPTION("logistics.event.wms.exception", "回传仓内异常信息", "LP->SF", ""),
    CALLBACK_STOCKOUT("logistics.event.wms.stockout", "回传仓库出仓信息", "LP->SF", ""),
    CALLBACK_ROUTE_PUSH("logistics.event.wms.routepush", "回传路由状态信息", "LP->SF", ""),
    CALLBACK_CANCEL("logistics.event.wms.cancelcallback", "回传仓库订单取消信息", "LP->SF", ""),
    CALLBACK_STOCKOUT_EXCEPTION("logistics.event.wms.stockout.exception", "回传仓库出仓异常信息", "LP->SF", ""),
    CALLBACK_FREIGHT("logistics.event.wms.freight", "回传出库运费信息", "LP->SF", ""),
    CALLBACK_SKU_CHECK_GOODS("logistics.event.wms.checkgoods", "回传SKU清点信息", "LP->SF", ""),
    CALLBACK_ORDER_TAX("logistics.event.wms.tax", "回传关税信息", "LP->SF", ""),

    CALLBACK_ZEBRA_CHECKORDER("wms.event.zebra.checkorder.callback", "斑马回传审单信息", "LP->SF", ""),
    CALLBACK_ZEBRA_STOCKIN("wms.event.zebra.stockin.callback", "斑马回传仓库入库信息", "LP->SF", ""),
    CALLBACK_ZEBRA_WEIGHT("wms.event.zebra.weight.callback", "斑马仓库称重信息", "LP->SF", ""),
    CALLBACK_ZEBRA_EXCEPTION("wms.event.zebra.exception.callback", "斑马回传仓内异常信息", "LP->SF", ""),
    CALLBACK_ZEBRA_STOCKOUT("wms.event.zebra.stockout.callback", "斑马回传仓库出仓信息", "LP->SF", ""),
    CALLBACK_ZEBRA_FREIGHT("wms.event.zebra.freight.callback", "斑马回传出库运费信息", "LP->SF", ""),
    CALLBACK_ZEBRA_DUTYS("wms.event.zebra.dutys.callback", "斑马回传关税信息", "LP->SF", ""),
    CALLBACK_ZEBRA_DISPATCH("wms.event.zebra.dispatch.callback", "斑马回传派送信息", "LP->SF", ""),

    CALLBACK_FSE_STOCKIN("wms.event.fse.stockin.callback", "费舍尔回传仓库入库信息", "LP->SF", ""),
    CALLBACK_FSE_STOCKOUT("wms.event.fse.stockout.callback", "费舍尔回传仓库出库信息", "LP->SF", ""),
    CALLBACK_FSE_STATUS("wms.event.fse.status.callback", "费舍尔回传出库单状态信息", "LP->SF", ""),

    CALLBACK_GALORDER_CREATE("logistics.event.wms.galorder.create", "仓库回传盘点后生成的损溢单信息", "LP->SF", ""),
    CALLBACK_GALORDER_FINISH("logistics.event.wms.galorder.finish", "仓库回传盘点完成信息", "LP->SF", ""),

    //杭州口岸返回的回调消息类型
    CALLBACK_HZ_PORT_ORDER_DECLARE("port.event.hz.order.declare.callback", "回传杭州口岸订单备案信息", "LP->SF", ""),
    CALLBACK_HZ_PORT_PRODUCT_DECLARE("port.event.hz.purduct.declare.callback", "回传杭州口岸商品备案信息", "LP->SF", ""),
    CALLBACK_HZ_PORT_PERSONAL_GOODS_DECLARE("port.event.hz.personal.goods.declare.callback", "回传杭州口岸个人物品申报审单结果信息", "LP->SF", ""),
    CALLBACK_HZ_PORT_WAY_BILL_DECLARE("port.event.hz.waybill.declare.callback", "回传杭州口岸运单申报审单结果信息", "LP->SF", ""),

    //LSCM 返回的回调消息类型
    CALLBACK_LSCM_STOCKIN("wms.event.lscm.stockin.callback", "回传顺丰冷库入库信息", "LP->SF", ""),
    CALLBACK_LSCM_STOCKOUT("wms.event.lscm.stockout.callback", "回传顺丰冷库出库信息", "LP->SF", ""),

    //OMS 返回的回调消息类型
    CALLBACK_OMS_STOCKIN("wms.event.oms.stockin.callback", "回传顺丰入库信息", "LP->SF", ""),
    CALLBACK_OMS_STOCKOUT("wms.event.oms.stockout.callback", "回传顺丰出库信息", "LP->SF", ""),


    //第三方支付 返回的回调消息类型
    CALLBACK_LIANLIAN_PA_DECLARE("wms.event.lianlian.pay.callback", "回传支付申报信息", "LP->SF", ""),

    //快递100回调信息类型
    CALLBACK_KD100_DECLARE("logistics.event.kd100.registroutes", "快递100回调信息", "SF-KD100", ""),
    QUERY_KD100_DECLARE("logistics.event.kd100.queryroutes", "快递100实时查询", "SF-KD100", ""),

    CALLBACK_SKU_STOCK("logistics.event.sku.stock.sync", "仓库同步库存情况", "LP->SF", ""),


    //与清关公司交互类型
    CCB_ORDER_CREATE("logistics.event.ccb.create", "清关公司订单下发", "SF->LP", ""),
    CCB_ORDER_CANCEL("logistics.event.ccb.cancel", "清关公司取消订单", "SF->LP", ""),
    CCB_ORDER_CONFIRM("logistics.event.ccb.confirm", "清关公司订单确认", "SF->LP", ""),
    CCB_ORDER_QUERY("logistics.event.ccb.query", "清关公司订单查询", "SF->LP", ""),
    CCB_SKU_SYNC("logistics.event.ccb.skusync", "同步商品基本信息给清关公司", "SF->LP", ""),

    //供应商查询的交互类型
    QUERY_ORDER_LIST("logistics.event.order.query", "供应商订单查询", "LP->SF", ""),
    QUERY_ORDER_PDF("logistics.event.order.pdf.query", "供应商面单查询", "LP->SF", ""),

    //开放仓库的交互类型
    OPENWMS_RPUTE_FETCH("logistics.event.openwms.routesfetch", "通知商户出库单状态更新", "FQ-LP", "");

    /**
     * 类型值
     */
    private String value;

    /**
     * 描述
     */
    private String description;

    /**
     * 消息方向，SF->LP 或 LP->SF
     */
    private String direction;

    /**
     * 使用场景
     */
    private String useScene;

    WmsMessageType(String value, String description, String direction, String useScene) {
        this.value = value;
        this.description = description;
        this.direction = direction;
        this.useScene = useScene;
    }

    public String getValue() {
        return value;
    }
}
