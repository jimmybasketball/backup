

package com.sfebiz.supplychain.exposed.common.enums;

import net.pocrd.entity.AbstractReturnCode;

/**
 * Created by xin.zhu on 2014/9/16.
 * <p/>
 * 错误范围限制在 2000000 -- 3000000
 */
public class LogisticsReturnCode extends AbstractReturnCode {

    /**
     * 系统异常
     * <p/>
     * 错误范围 2000000 -- 2000100
     */
    public final static int _C_LOGISTICS_CONCURRENT_ERR = 2000001;
    public final static int _C_LOGISTICS_DIAMOND_NOT_FOUND_ERR = 2000002;
    public final static int _C_LOGISTICS_JAXB_TRANSFER_ERR = 2000003;
    public final static int _C_LOGISTICS_REDIS_TRANSFER_ERR = 2000003;
    public final static int _C_LOGISTIC_LINE_DELAY_NOTICE_DATA_ERROR = 2000004;
    public final static int _C_LOGISTIC_SERVICE_PARAM_ERROR = 2000005;

    public final static LogisticsReturnCode LOGISTICS_CONCURRENT_ERR = new LogisticsReturnCode("系统并发异常", _C_LOGISTICS_CONCURRENT_ERR);
    public final static LogisticsReturnCode LOGISTICS_DIAMOND_NOT_FOUND_ERR = new LogisticsReturnCode("未找到对应的diamond配置", _C_LOGISTICS_DIAMOND_NOT_FOUND_ERR);
    public final static LogisticsReturnCode LOGISTICS_REDIS_NOT_FOUND_ERR = new LogisticsReturnCode("未找到对应的redis记录", _C_LOGISTICS_REDIS_TRANSFER_ERR);
    public final static LogisticsReturnCode LOGISTICS_JAXB_TRANSFER_ERR = new LogisticsReturnCode("JAXB转换XML为对象时发生异常", _C_LOGISTICS_JAXB_TRANSFER_ERR);
    public final static LogisticsReturnCode LOGISTIC_LINE_DELAY_NOTICE_DATA_ERROR = new LogisticsReturnCode("查询物流延迟信息错误", _C_LOGISTIC_LINE_DELAY_NOTICE_DATA_ERROR);
    public final static LogisticsReturnCode LOGISTIC_SERVICE_PARAM_ERROR = new LogisticsReturnCode("参数非法错误", _C_LOGISTIC_SERVICE_PARAM_ERROR);

    /**
     * stockoutServiceErrorCode, lineServiceErrorCode, logisticsCallbackServiceErrorCode
     * <p/>
     * 错误范围 2000100 -- 2010000
     */
    public final static int _C_LOGISTICS_CATEGORY_NOTEXIST_ERR = 2003000;
    public final static int _C_LOGISTICS_WAREHOUSE_NOTEXIST_ERR = 2003001;
    public final static int _C_LOGISTICS_IDTYPE_ERR = 2003002;
    public final static int _C_LOGISTICS_IDNO_ERR = 2003003;
    public final static int _C_LOGISTICS_STOCKINID_ERR = 2003004;
    public final static int _C_LOGISTICS_ORDER_CREATE_ERR = 2004000;
    public final static int _C_LOGISTICS_ORDER_ADD_ITEM_ERR = 2004001;
    public final static int _C_LOGISTICS_ORDER_ADD_PACK_ERR = 2004002;
    public final static int _C_LOGISTICS_ORDER_ADD_PACK_ITEM_ERR = 2004003;
    public final static int _C_LOGISTICS_REQUEST_ORDER_ADDRESS_ERR = 2004010;
    public final static int _C_LOGISTICS_ORDER_NOTEXIST_ERR = 2004020;
    public final static int _C_LOGISTICS_STATE_CANNOT_CHANGE_ERR = 2004021;
    public final static int _C_LOGISTICS_LINE_NOTEXIST_ERR = 2004022;
    public final static int _C_LOGISTICS_BUYER_ADDRESS_ERR = 2004023;
    public final static int _C_LOGISTICS_IDENTITY_ERR = 2004024;
    public final static int _C_LOGISTICS_TRADEITEMS_ERR = 2004025;
    public final static int _C_LOGISTICS_PACKAGE_ERR = 2004026;
    public final static int _C_LOGISTICS_ORDER_ERR = 2004027;
    public final static int _C_LOGISTICS_ORDER_CANT_CANCEL_ERR = 2004028;
    public final static int _C_LOGISTICS_ORDER_CANT_SEND_ERR = 2004029;
    public final static int _C_LOGISTICS_ORDER_EXIST_ERR = 2004030;
    public final static int _C_STOCKIN_ITEMS_EMPTY = 2005000;
    public final static int _C_STOCKIN_ITEM_ERR = 2005001;
    public final static int _C_LOGISTICS_STARTPOINT_NOTEXIST_ERR = 2005002;
    public final static int _C_LOGISTICS_SELF_ITEM_COMBINE_ERR = 2005003;
    public final static int _C_LOGISTICS_ORDER_CANT_CONFIRM_ERR = 2005004;
    public final static int _C_LOGISTICS_ORDER_PAYINFO_ERR = 2005005;
    public final static int _C_LOGISTICS_ORDER_DECLAREDVALUE_ERR = 2005006;
    public final static int _C_LOGISTICS_ORDER_TOTAL_ERR = 2005007;
    public final static int _C_LOGISTICS_INTERNAL_ERROR = 2006000;
    public final static int _C_LOGISTICS_STOCK_SKU_NOT_ENOUGH = 2007000;
    public final static int _C_LOGISTICS_STOCK_SKU_NOT_FOUND = 2007001;
    public final static int _C_LOGISTICS_WAREHOUSE_NOT_FOUND = 2007002;
    public final static int _C_LOGISTICS_SEND_ADDRESS_NOT_FOUND = 2007003;
    public final static int _C_LOGISTICS_STATE_MACHINE_INTERNAL_EXCEPTION = 2007004;
    public final static int _C_LOGISTICS_STOCK_SKU_RELEASE_ERR = 2007005;
    public final static int _C_LOGISTICS_SALE_STOCK_SKU_NOT_FOUND = 2007006;
    public final static int _C_LOGISTICS_UNSUPPORT_STOCKOUT_TYPE = 2007007;
    public final static int _C_LOGISTICS_BATCH_STOCK_SKU_NOT_ENOUGH = 2007008;
    public final static int _C_LOGISTICS_BATCH_STOCK_SKU_NOT_FOUND = 2007009;
    public final static int _C_LOGISTICS_BATCH_STOCK_ENOUGH_WAREHOUSE_NOT_FOUND = 2007010;
    public final static int _C_LOGISTICS_COMBINATION_MILK_SKU_QTY_NOT_TWO = 2007011;
    public final static int _C_LOGISTICS_COMBINATION_SKU_WAREHOUSE_NOT_FOUND = 2007012;
    public final static int _C_LOGISTICS_COMPUTE_ERROR = 2007013;
    public final static int _C_LOGISTICS_STOCKOUT_ORDER_PRICE_INCORRECT = 2007014;
    public final static int _C_LOGISTICS_STOCKOUT_ORDER_PARAMS_ILLEGAL = 2007015;
    public final static int _C_LOGISTICS_SYSTEM_MAINTENANCE = 2007016;
    public final static int LOGISTICS_BSP_REGIST_NOT_ALLOW_ON_OTHRE_TPL_CODE = 2007017;
    public final static int LOGISTICS_BSP_REGIST_INNER_EXCEPTION_CODE = 2007018;
    public final static int LOGISTICS_GET_LINE_EXCEPTION_ON_SPLIT_CODE = 2007019;
    public final static int LOGISTICS_ROUTE_REGIST_INNER_EXCEPTION_CODE = 2007020;
    public final static int LOGISTICS_RECEIVER_ADDRESS_TOO_LONG_CODE = 2007021;
    public final static int LOGISTICS_CARRIER_CODE_NOTEXIST_ERR_CODE = 2007022;
    public final static int LOGISTICS_APPEND_USERROUTES_ERROR_CODE = 2007023;
    public final static int _LOGISTICS_PRE_SALE_EXCEPTION_CODE = 2007024;

    public final static LogisticsReturnCode LOGISTICS_ORDER_CREATE_ERR = new LogisticsReturnCode("创建物流订单失败", _C_LOGISTICS_ORDER_CREATE_ERR);
    public final static LogisticsReturnCode LOGISTICS_ORDER_ADD_ITEM_ERR = new LogisticsReturnCode("创建物流订单添加商品失败", _C_LOGISTICS_ORDER_ADD_ITEM_ERR);
    public final static LogisticsReturnCode LOGISTICS_ORDER_ADD_PACK_ERR = new LogisticsReturnCode("创建物流订单添加包裹失败", _C_LOGISTICS_ORDER_ADD_PACK_ERR);
    public final static LogisticsReturnCode LOGISTICS_ORDER_ADD_PACK_ITEM_ERR = new LogisticsReturnCode("创建物流订单添加包裹商品失败", _C_LOGISTICS_ORDER_ADD_PACK_ITEM_ERR);
    public final static LogisticsReturnCode LOGISTICS_REQUEST_ORDER_ADDRESS_ERR = new LogisticsReturnCode("获取订单地址失败", _C_LOGISTICS_REQUEST_ORDER_ADDRESS_ERR);
    public final static LogisticsReturnCode LOGISTICS_ORDER_NOTEXIST_ERR = new LogisticsReturnCode("订单不存在失败", _C_LOGISTICS_ORDER_NOTEXIST_ERR);
    public final static LogisticsReturnCode LOGISTICS_STATE_CANNOT_CHANGE_ERR = new LogisticsReturnCode("订单状态不允许修改错误", _C_LOGISTICS_STATE_CANNOT_CHANGE_ERR);
    public final static LogisticsReturnCode LOGISTICS_LINE_NOTEXIST_ERR = new LogisticsReturnCode("订单线路不存在", _C_LOGISTICS_LINE_NOTEXIST_ERR);
    public final static LogisticsReturnCode LOGISTICS_ORDER_EXIST_ERR = new LogisticsReturnCode("订单已存在", _C_LOGISTICS_ORDER_EXIST_ERR);
    public final static LogisticsReturnCode LOGISTICS_BUYER_ADDRESS_ERR = new LogisticsReturnCode("联系人信息缺失或错误", _C_LOGISTICS_BUYER_ADDRESS_ERR);
    public final static LogisticsReturnCode LOGISTICS_IDENTITY_ERR = new LogisticsReturnCode("收货人证件信息缺失或错误", _C_LOGISTICS_IDENTITY_ERR);
    public final static LogisticsReturnCode LOGISTICS_TRADEITEMS_ERR = new LogisticsReturnCode("购买的商品信息缺失或错误", _C_LOGISTICS_TRADEITEMS_ERR);
    public final static LogisticsReturnCode LOGISTICS_PACKAGE_ERR = new LogisticsReturnCode("包裹信息缺失或错误", _C_LOGISTICS_PACKAGE_ERR);
    public final static LogisticsReturnCode LOGISTICS_ORDER_ERR = new LogisticsReturnCode("物流信息缺失或错误", _C_LOGISTICS_ORDER_ERR);
    public final static LogisticsReturnCode LOGISTICS_CATEGORY_NOTEXIST_ERR = new LogisticsReturnCode("品类信息不存在", _C_LOGISTICS_CATEGORY_NOTEXIST_ERR);
    public final static LogisticsReturnCode LOGISTICS_WAREHOUSE_NOTEXIST_ERR = new LogisticsReturnCode("仓库信息不存在", _C_LOGISTICS_WAREHOUSE_NOTEXIST_ERR);
    public final static LogisticsReturnCode LOGISTICS_IDTYPE_ERR = new LogisticsReturnCode("证件类型错误", _C_LOGISTICS_IDTYPE_ERR);
    public final static LogisticsReturnCode LOGISTICS_IDNO_ERR = new LogisticsReturnCode("证件号码错误", _C_LOGISTICS_IDNO_ERR);
    public final static LogisticsReturnCode LOGISTICS_ORDER_CANT_CANCEL_ERR = new LogisticsReturnCode("物流状态不允许取消操作", _C_LOGISTICS_ORDER_CANT_CANCEL_ERR);
    public final static LogisticsReturnCode LOGISTICS_ORDER_CANT_SEND_ERR = new LogisticsReturnCode("物流状态不允许发货操作", _C_LOGISTICS_ORDER_CANT_SEND_ERR);
    public final static LogisticsReturnCode LOGISTICS_ORDER_CANT_CONFIRM_ERR = new LogisticsReturnCode("物流状态不允确认关税操作", _C_LOGISTICS_ORDER_CANT_CONFIRM_ERR);
    public final static LogisticsReturnCode LOGISTICS_STOCKINID_ERR = new LogisticsReturnCode("SKU入库失败", _C_LOGISTICS_STOCKINID_ERR);
    public final static LogisticsReturnCode STOCKIN_ITEMS_EMPTY = new LogisticsReturnCode("SKU入库商品为空", _C_STOCKIN_ITEMS_EMPTY);
    public final static LogisticsReturnCode STOCKIN_ITEM_ERR = new LogisticsReturnCode("SKU入库商品信息缺失", _C_STOCKIN_ITEM_ERR);
    public final static LogisticsReturnCode LOGISTICS_STARTPOINT_NOTEXIST_ERR = new LogisticsReturnCode("起始位置不存在", _C_LOGISTICS_STARTPOINT_NOTEXIST_ERR);
    public final static LogisticsReturnCode LOGISTICS_SELF_ITEM_COMBINE_ERR = new LogisticsReturnCode("仓配商品与非仓配商品混合错误", _C_LOGISTICS_SELF_ITEM_COMBINE_ERR);
    public final static LogisticsReturnCode LOGISTICS_ORDER_PAYINFO_ERR = new LogisticsReturnCode("订单支付信息缺失", _C_LOGISTICS_ORDER_PAYINFO_ERR);
    public final static LogisticsReturnCode LOGISTICS_ORDER_DECLAREDVALUE_ERR = new LogisticsReturnCode("订单申报价值不能为0", _C_LOGISTICS_ORDER_DECLAREDVALUE_ERR);
    public final static LogisticsReturnCode LOGISTICS_ORDER_TOTAL_ERR = new LogisticsReturnCode("用户订单总金额不能为0", _C_LOGISTICS_ORDER_TOTAL_ERR);
    public final static LogisticsReturnCode LOGISTICS_INTERNAL_ERROR = new LogisticsReturnCode("物流系统内部异常", _C_LOGISTICS_INTERNAL_ERROR);
    public final static LogisticsReturnCode LOGISTICS_STOCK_SKU_NOT_ENOUGH = new LogisticsReturnCode("实物库存不足", _C_LOGISTICS_STOCK_SKU_NOT_ENOUGH);
    public final static LogisticsReturnCode LOGISTICS_STOCK_SKU_NOT_FOUND = new LogisticsReturnCode("找不到商品的实物库存信息", _C_LOGISTICS_STOCK_SKU_NOT_FOUND);
    public final static LogisticsReturnCode LOGISTICS_WAREHOUSE_NOT_FOUND = new LogisticsReturnCode("找不到商品的发货仓库", _C_LOGISTICS_WAREHOUSE_NOT_FOUND);
    public final static LogisticsReturnCode LOGISTICS_SEND_ADDRESS_NOT_FOUND = new LogisticsReturnCode("找不到商品的发货地", _C_LOGISTICS_SEND_ADDRESS_NOT_FOUND);
    public final static LogisticsReturnCode LOGISTICS_STATE_MACHINE_INTERNAL_EXCEPTION = new LogisticsReturnCode("状态机内部异常", _C_LOGISTICS_STATE_MACHINE_INTERNAL_EXCEPTION);
    public final static LogisticsReturnCode LOGISTICS_STOCK_SKU_RELEASE_ERR = new LogisticsReturnCode("商品库存释放异常", _C_LOGISTICS_STOCK_SKU_RELEASE_ERR);
    public final static LogisticsReturnCode LOGISTICS_SALE_STOCK_SKU_NOT_FOUND = new LogisticsReturnCode("找不到商品的销售库存信息", _C_LOGISTICS_SALE_STOCK_SKU_NOT_FOUND);
    public final static LogisticsReturnCode LOGISTICS_UNSUPPORT_STOCKOUT_TYPE = new LogisticsReturnCode("不支持的出库单类型", _C_LOGISTICS_UNSUPPORT_STOCKOUT_TYPE);
    public final static LogisticsReturnCode LOGISTICS_BATCH_STOCK_SKU_NOT_ENOUGH = new LogisticsReturnCode("批次库存不足", _C_LOGISTICS_BATCH_STOCK_SKU_NOT_ENOUGH);
    public final static LogisticsReturnCode LOGISTICS_BATCH_STOCK_SKU_NOT_FOUND = new LogisticsReturnCode("找不到商品的批次库存信息", _C_LOGISTICS_BATCH_STOCK_SKU_NOT_FOUND);
    public final static LogisticsReturnCode LOGISTICS_BATCH_STOCK_ENOUGH_WAREHOUSE_NOT_FOUND = new LogisticsReturnCode("找不到商品批次库存足够的仓库", _C_LOGISTICS_BATCH_STOCK_ENOUGH_WAREHOUSE_NOT_FOUND);
    public final static LogisticsReturnCode LOGISTICS_COMBINATION_MILK_SKU_QTY_NOT_TWO = new LogisticsReturnCode("类别为奶粉的组合商品数量不为2", _C_LOGISTICS_COMBINATION_MILK_SKU_QTY_NOT_TWO);
    public final static LogisticsReturnCode LOGISTICS_COMBINATION_SKU_WAREHOUSE_NOT_FOUND = new LogisticsReturnCode("找不到同时存在组合商品下基本商品的仓库", _C_LOGISTICS_COMBINATION_SKU_WAREHOUSE_NOT_FOUND);
    public final static LogisticsReturnCode LOGISTICS_COMPUTE_ERROR = new LogisticsReturnCode("计算时发生错误", _C_LOGISTICS_COMPUTE_ERROR);
    public final static LogisticsReturnCode LOGISTICS_STOCKOUT_ORDER_PRICE_INCORRECT = new LogisticsReturnCode("商品价值之和大于用户实际支付价格", _C_LOGISTICS_STOCKOUT_ORDER_PRICE_INCORRECT);
    public final static LogisticsReturnCode LOGISTICS_STOCKOUT_ORDER_PARAMS_ILLEGAL = new LogisticsReturnCode("出库单详情信息缺失，Item对应的信息不存在", _C_LOGISTICS_STOCKOUT_ORDER_PARAMS_ILLEGAL);
    public final static LogisticsReturnCode LOGISTICS_SYSTEM_MAINTENANCE = new LogisticsReturnCode("供应链系统维护中，请稍后再试", _C_LOGISTICS_SYSTEM_MAINTENANCE);
    public final static LogisticsReturnCode LOGISTICS_BSP_REGIST_NOT_ALLOW_ON_OTHRE_TPL = new LogisticsReturnCode("供应链国内供应商不为BSP，不能进行BSP路由注册", LOGISTICS_BSP_REGIST_NOT_ALLOW_ON_OTHRE_TPL_CODE);
    public final static LogisticsReturnCode LOGISTICS_BSP_REGIST_INNER_EXCEPTION = new LogisticsReturnCode("供应链国内供应商BSP路由注册内部异常", LOGISTICS_BSP_REGIST_INNER_EXCEPTION_CODE);
    public final static LogisticsReturnCode LOGISTICS_GET_LINE_EXCEPTION_ON_SPLIT = new LogisticsReturnCode("供应链分单时获取路线失败", LOGISTICS_GET_LINE_EXCEPTION_ON_SPLIT_CODE);
    public final static LogisticsReturnCode LOGISTICS_ROUTE_REGIST_INNER_EXCEPTION = new LogisticsReturnCode("供应链路由注册快递100内部异常", LOGISTICS_ROUTE_REGIST_INNER_EXCEPTION_CODE);
    public final static LogisticsReturnCode LOGISTICS_RECEIVER_ADDRESS_TOO_LONG = new LogisticsReturnCode("收件人地址过长", LOGISTICS_RECEIVER_ADDRESS_TOO_LONG_CODE);
    public final static LogisticsReturnCode LOGISTICS_CARRIER_CODE_NOTEXIST_ERR = new LogisticsReturnCode("快递公司编码不存在", LOGISTICS_CARRIER_CODE_NOTEXIST_ERR_CODE);
    public final static LogisticsReturnCode LOGISTICS_APPEND_USERROUTES_ERROR = new LogisticsReturnCode("追加自定义物流信息异常", LOGISTICS_APPEND_USERROUTES_ERROR_CODE);
    public final static LogisticsReturnCode LOGISTICS_PRE_SALE_EXCEPTION_CODE = new LogisticsReturnCode("预售创建出库单异常", _LOGISTICS_PRE_SALE_EXCEPTION_CODE);
    /**
     * purchaseServiceErrorCode
     * <p/>
     * 错误范围： 2000000 -- 2020000
     */
    public final static int PURCHASE_ORDER_INNER_EXCEPTION_CODE = 2020000;
    public final static int PURCHASE_ORDER_PARAMS_ILLEGAL_CODE = 2020001;
    public final static int PURCHASE_ORDER_NOT_ALLOW_UPDATE_CODE = 2020002;
    public final static int PURCHASE_ORDER_NOT_ALLOW_SUBMIT_AUDIT_CODE = 2020003;
    public final static int PURCHASE_ORDER_NOT_ALLOW_DELETE_CODE = 2020004;
    public final static int PURCHASE_ORDER_NOT_ALLOW_OPERATER_ITEM_CODE = 2020005;
    public final static int PURCHASE_ORDER_ADD_REPEAT_ITEM_CODE = 2020006;
    public final static int PURCHASE_ORDER_NOT_EXIST_CODE = 2020007;
    public final static int PURCHASE_ORDER_MORE_PROVIDER_CODE = 2020008;
    public final static int PURCHASE_ORDER_EMPTY_ITEM_CODE = 2020009;
    public final static int PURCHASE_ORDER_ITEM_MORE_CURRENCY_CODE = 2020010;
    public final static int PURCHASE_ORDER_ITEM_MORE_SETTLEMENT_CODE = 2020011;
    public final static int PURCHASE_ORDER_ITEM_EMPTY_CURRENCY_CODE = 2020012;
    public final static int PURCHASE_ORDER_ITEM_EMPTY_SETTLEMENT_CODE = 2020013;
    public final static int PURCHASE_ORDER_PREPAY_STATUS_ERROR_CODE = 2020014;
    public final static int PURCHASE_ORDER_ITEM_HAS_CREATED_ERROR_CODE = 2020015;
    
    public final static LogisticsReturnCode PURCHASE_ORDER_INNER_EXCEPTION = new LogisticsReturnCode("采购单系统内部异常", PURCHASE_ORDER_INNER_EXCEPTION_CODE);
    public final static LogisticsReturnCode PURCHASE_ORDER_PARAMS_ILLEGAL = new LogisticsReturnCode("采购单参数不合法", PURCHASE_ORDER_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode PURCHASE_ORDER_NOT_EXIST = new LogisticsReturnCode("采购单不存在", PURCHASE_ORDER_NOT_EXIST_CODE);
    public final static LogisticsReturnCode PURCHASE_ORDER_NOT_ALLOW_UPDATE = new LogisticsReturnCode("采购单不允许修改", PURCHASE_ORDER_NOT_ALLOW_UPDATE_CODE);
    public final static LogisticsReturnCode PURCHASE_ORDER_NOT_ALLOW_SUBMIT_AUDIT = new LogisticsReturnCode("采购单不允许提交审核", PURCHASE_ORDER_NOT_ALLOW_SUBMIT_AUDIT_CODE);
    public final static LogisticsReturnCode PURCHASE_ORDER_NOT_ALLOW_DELETE = new LogisticsReturnCode("采购单不允许删除", PURCHASE_ORDER_NOT_ALLOW_DELETE_CODE);
    public final static LogisticsReturnCode PURCHASE_ORDER_NOT_ALLOW_OPERATER_ITEM = new LogisticsReturnCode("采购单已提交，不可修改商品", PURCHASE_ORDER_NOT_ALLOW_OPERATER_ITEM_CODE);
    public final static LogisticsReturnCode PURCHASE_ORDER_ADD_REPEAT_ITEM = new LogisticsReturnCode("添加的商品已在采购单中存在", PURCHASE_ORDER_ADD_REPEAT_ITEM_CODE);
    public final static LogisticsReturnCode PURCHASE_ORDER_MORE_PROVIDER = new LogisticsReturnCode("采购单中商品中包含多个供应商", PURCHASE_ORDER_MORE_PROVIDER_CODE);
    public final static LogisticsReturnCode PURCHASE_ORDER_EMPTY_ITEM = new LogisticsReturnCode("采购单中商品不能为空", PURCHASE_ORDER_EMPTY_ITEM_CODE);
    public final static LogisticsReturnCode PURCHASE_ORDER_ITEM_MORE_CURRENCY = new LogisticsReturnCode("采购单中商品货币类型不一致", PURCHASE_ORDER_ITEM_MORE_CURRENCY_CODE);
    public final static LogisticsReturnCode PURCHASE_ORDER_ITEM_EMPTY_CURRENCY = new LogisticsReturnCode("采购单中商品货币类型不能为空", PURCHASE_ORDER_ITEM_EMPTY_CURRENCY_CODE);
    public final static LogisticsReturnCode PURCHASE_ORDER_ITEM_MORE_SETTLEMENT = new LogisticsReturnCode("采购单中商品付款方式不一致", PURCHASE_ORDER_ITEM_MORE_SETTLEMENT_CODE);
    public final static LogisticsReturnCode PURCHASE_ORDER_ITEM_EMPTY_SETTLEMENT = new LogisticsReturnCode("采购单中商品付款方式不能为空", PURCHASE_ORDER_ITEM_EMPTY_SETTLEMENT_CODE);
    public final static LogisticsReturnCode PURCHASE_ORDER_PREPAY_STATUS_ERROR = new LogisticsReturnCode("采购单预付款状态错误", PURCHASE_ORDER_PREPAY_STATUS_ERROR_CODE);
    public final static LogisticsReturnCode PURCHASE_ORDER_ITEM_HAS_CREATED_ERROR = new LogisticsReturnCode("采购单中寄售不入库商品已经有库存记录", PURCHASE_ORDER_ITEM_HAS_CREATED_ERROR_CODE);
    
    /**
     * stockinServiceErrorCode
     * <p/>
     * 错误范围： 2000000 -- 2030000
     */
    public final static int STOCKIN_ORDER_INNER_EXCEPTION_CODE = 2030000;
    public final static int STOCKIN_ORDER_PARAMS_ILLEGAL_CODE = 2030001;
    public final static int STOCKIN_ORDER_NOT_ALLOW_UPDATE_CODE = 2030002;
    public final static int STOCKIN_ORDER_SKU_NOT_ALLOW_DELETE_CODE = 2030003;
    public final static int STOCKIN_ORDER_NOT_ALLOW_OPERATER_ITEM_CODE = 2030004;
    public final static int STOCKIN_ORDER_ADD_REPEAT_ITEM_CODE = 2030005;
    public final static int STOCKIN_ORDER_NOT_EXIST_CODE = 2030006;
    public final static int STOCKIN_ORDER_SENDSTOCK_URLORKEY_NULL_CODE = 2030007;
    public final static int STOCKIN_ORDER_SENDSTOCK_RESPONSE_NULL_CODE = 2030008;
    public final static int STOCKIN_ORDER_SENDSTOCK_FAIL_CODE = 2030009;
    public final static int STOCKIN_ORDER_ONT_ALLOW_FINISH_CODE = 2030010;
    public final static int STOCKIN_ORDER_SUBMIT_FAILED = 2031111;
    public final static int STOCKIN_ORDER_CREATE_FAILED_CODE = 2031112;
    public final static int STOCKIN_ORDER_NOT_ALLOW_CANCEL_CODE = 2031113;
    public final static int STOCKIN_ORDER_SKUS_IS_EMPTY_CODE = 2031114;
    public final static int STOCKIN_ORDER_NOT_ALLOW_SUBMIT_CODE = 2031115;
    public final static int STOCKIN_ORDER_SUBMIT_CMD_NOT_FOUND_CODE = 2031116;
    public final static int STOCKIN_ORDER_NOT_SETTLED_CODE = 2031117;
    public final static int STOCKIN_ORDER_NOT_APPLY_SECOND_PAY_CODE = 2031118;
    public final static int STOCKIN_ORDER_CANCEL_CMD_NOT_FOUND_CODE = 2031119;
    public final static int STOCKIN_ORDER_INLAND_TRADE_CODE = 2031120;
    public final static int STOCKIN_ORDER_INTERNATIONAL_TRADE_CODE = 2031121;
    public final static int STOCKIN_ORDER_EDIT_WAREHOUSE_CODE = 2031122;
    public final static int STOCKIN_TRANS_INLAND_TRADE_CODE = 2031123;
    public final static int STOCKIN_TRANS_INTERNATIONAL_TRADE_CODE = 2031124;
    public final static int STOCKIN_TRANS_ORIGINE_CODE = 2031125;
    
    public final static LogisticsReturnCode STOCKIN_ORDER_INNER_EXCEPTION = new LogisticsReturnCode("入库单系统内部异常", STOCKIN_ORDER_INNER_EXCEPTION_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_PARAMS_ILLEGAL = new LogisticsReturnCode("入库单参数不合法", STOCKIN_ORDER_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_NOT_EXIST = new LogisticsReturnCode("入库单不存在", STOCKIN_ORDER_NOT_EXIST_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_NOT_ALLOW_UPDATE = new LogisticsReturnCode("当前状态不允许入库单修改", STOCKIN_ORDER_NOT_ALLOW_UPDATE_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_SKU_NOT_ALLOW_DELETE = new LogisticsReturnCode("入库单商品不允许删除", STOCKIN_ORDER_SKU_NOT_ALLOW_DELETE_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_NOT_ALLOW_OPERATER_ITEM = new LogisticsReturnCode("入库单已提交，不可修改商品", STOCKIN_ORDER_NOT_ALLOW_OPERATER_ITEM_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_ADD_REPEAT_ITEM = new LogisticsReturnCode("添加的商品已在入库单中存在", STOCKIN_ORDER_ADD_REPEAT_ITEM_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_SENDSTOCK_URLORKEY_NULL = new LogisticsReturnCode("调用仓库接口URL或者KEY为空", STOCKIN_ORDER_SENDSTOCK_URLORKEY_NULL_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_SENDSTOCK_RESPONSE_NULL = new LogisticsReturnCode("调用仓库接口返回信息为空", STOCKIN_ORDER_SENDSTOCK_RESPONSE_NULL_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_SENDSTOCK_FAIL = new LogisticsReturnCode("调用仓库接口失败", STOCKIN_ORDER_SENDSTOCK_FAIL_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_NOT_ALLOW_FINISH = new LogisticsReturnCode("当前状态不允许入库单完成", STOCKIN_ORDER_ONT_ALLOW_FINISH_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_NOT_ALLOW_CANCEL = new LogisticsReturnCode("当前状态不允许入库单取消", STOCKIN_ORDER_NOT_ALLOW_CANCEL_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_SKUS_IS_EMPTY = new LogisticsReturnCode("入库单中没有商品", STOCKIN_ORDER_SKUS_IS_EMPTY_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_NOT_ALLOW_SUBMIT = new LogisticsReturnCode("当前状态不允许入库单提交", STOCKIN_ORDER_NOT_ALLOW_SUBMIT_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_SUBMIT_CMD_NOT_FOUND = new LogisticsReturnCode("未找到对应的入库单提交命令", STOCKIN_ORDER_SUBMIT_CMD_NOT_FOUND_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_CANCEL_CMD_NOT_FOUND = new LogisticsReturnCode("未找到对应的入库单取消命令", STOCKIN_ORDER_CANCEL_CMD_NOT_FOUND_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_NOT_SETTLED = new LogisticsReturnCode("入库单未生成结算信息", STOCKIN_ORDER_NOT_SETTLED_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_NOT_APPLY_SECOND_PAY = new LogisticsReturnCode("入库单没有生成二次预付", STOCKIN_ORDER_NOT_APPLY_SECOND_PAY_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_INLAND_TRADE = new LogisticsReturnCode("入库单供应商为国内贸易，仓库只能为国内实体仓", STOCKIN_ORDER_INLAND_TRADE_CODE);
    public final static LogisticsReturnCode STOCKIN_TRANS_INLAND_TRADE = new LogisticsReturnCode("调拨入库的调出仓库为国内仓，改仓库只能为国内实体仓", STOCKIN_TRANS_INLAND_TRADE_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_INTERNATIONAL_TRADE = new LogisticsReturnCode("入库单供应商为国际贸易，仓库只能为海外实体仓、保税实体仓", STOCKIN_ORDER_INTERNATIONAL_TRADE_CODE);
    public final static LogisticsReturnCode STOCKIN_TRANS_INTERNATIONAL_TRADE = new LogisticsReturnCode("调拨入库的调出仓库为海外仓或保税仓，仓库只能为海外实体仓、保税实体仓", STOCKIN_TRANS_INTERNATIONAL_TRADE_CODE);
    public final static LogisticsReturnCode STOCKIN_ORDER_EDIT_WAREHOUSE = new LogisticsReturnCode("入库单状态为收货完成或已取消，不能改仓", STOCKIN_ORDER_EDIT_WAREHOUSE_CODE);
    public final static LogisticsReturnCode STOCKIN_TRANS_ORIGINE = new LogisticsReturnCode("入库单起运国为空",STOCKIN_TRANS_ORIGINE_CODE);

    /**
     * skuServiceErrorCode
     * <p/>
     * 错误范围 2000000 -- 2040000
     */
    public final static int SKU_SERVICE_PARAMS_ILLEGAL_CODE = 2040000;
    public final static int SKU_SERVICE_INNER_EXCEPTION_CODE = 2040001;
    public final static int SKU_SERVICE_DECLARE_NAME_NOT_EXIST_CODE = 2040002;
    public final static int SKU_SERVICE_DECLARE_EXIST_PASS_CODE = 2040003;
    public final static int SKU_NOT_EXIST_CODE = 2040004;
    public final static int SKU_SERVICE_DECLARE_EXIST_CODE = 2040004;
    public final static int SKU_DECLARE_NOT_ALLOW_DELETE_CODE = 2040005;
    public final static int MIXED_SKU_NOT_EXIST_CODE = 2040006;
    public final static int MIXED_SKU_PARAMS_ILLAGEL_CODE = 2040007;
    public final static int MIXSKU_EXITS_ERR_CODE = 2041000;
    public final static int MIXSKU_INSERT_ERR_CODE = 2041001;
    public final static int MIXSKU_UPDATE_ERR_CODE = 2041002;
    public final static int DUPLICATE_BARCODE_ERROR_CODE = 2041003;
    public final static int SAVE_BARCODE_ERROR_CODE = 2041004;
    public final static int SKU_POSTTAXCODE_NOT_MATCH_DECLARE_CODE = 2041005;
    public final static int SKU_DECLARE_EXIST_CODE = 2041006;
    public final static int STOCK_EXIST_SKU_DELETE_ERROR_CODE = 2041007;
    public final static int SKU_BARCODE_IS_EMPTY_CODE = 2041008;
    public final static int SKU_SERVICE_DECLARE_INNER_EXCEPTION_CODE = 2041009;
    public final static int STOCK_EXIST_SKU_BARCODE_EDIT_NOT_ALLOWED_CODE = 2041010;
    public final static int SKU_DECLARE_NOT_EXIST_CODE = 2041011;
    public final static int SKU_DECLARE_TAXRATE_ILLEGAL_CODE = 2041012;
    public final static int STOCKOUT_ORDER_DECLARE_PRICE_SAVE_FAILURE_CODE = 2041013;
    public final static int STOCKOUT_ORDER_SKU_DECLARE_PRICE_NOT_EXIST_CODE = 2041014;
    public final static int STOCKOUT_ORDER_PROVIDER_META_NOT_MATCH_CODE = 2041015;
    public final static int SKU_DECLARE_WEIGHT_IS_EMPTY_CODE = 2041016;
    public final static int STOCKOUT_ORDER_DECLARE_ACTURE_PAY_IS_ZERO_COEE = 2041017;
    
    public final static LogisticsReturnCode SKU_SERVICE_PARAMS_ILLEGAL = new LogisticsReturnCode("方法参数为空或不合法", SKU_SERVICE_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode SKU_SERVICE_INNER_EXCEPTION = new LogisticsReturnCode("商品服务系统内部异常", SKU_SERVICE_INNER_EXCEPTION_CODE);
    public final static LogisticsReturnCode SKU_SERVICE_DECLARE_NAME_NOT_EXIST = new LogisticsReturnCode("商品备案名称不存在", SKU_SERVICE_DECLARE_NAME_NOT_EXIST_CODE);
    public final static LogisticsReturnCode SKU_SERVICE_DECLARE_EXIST_PASS = new LogisticsReturnCode("商品已成功备案", SKU_SERVICE_DECLARE_EXIST_PASS_CODE);
    public final static LogisticsReturnCode SKU_SERVICE_DECLARE_EXIST = new LogisticsReturnCode("备案商品已存在", SKU_SERVICE_DECLARE_EXIST_CODE);
    public final static LogisticsReturnCode SKU_SERVICE_DECLARE_INNER_EXCEPTION = new LogisticsReturnCode("商品备案内部异常", SKU_SERVICE_DECLARE_INNER_EXCEPTION_CODE);
    public final static LogisticsReturnCode SKU_NOT_EXIST = new LogisticsReturnCode("商品不存在", SKU_NOT_EXIST_CODE);
    public final static LogisticsReturnCode SKU_DECLARE_NOT_ALLOW_DELETE = new LogisticsReturnCode("备案商品不允许删除", SKU_DECLARE_NOT_ALLOW_DELETE_CODE);
    public final static LogisticsReturnCode MIXED_SKU_NOT_EXIST = new LogisticsReturnCode("组合商品信息不存在", MIXED_SKU_NOT_EXIST_CODE);
    public final static LogisticsReturnCode MIXED_SKU_PARAMS_ILLAGEL = new LogisticsReturnCode("组合商品参数信息不合法", MIXED_SKU_PARAMS_ILLAGEL_CODE);
    public final static LogisticsReturnCode MIXSKU_EXITS_ERR = new LogisticsReturnCode("组合商品信息已存在", MIXSKU_EXITS_ERR_CODE);
    public final static LogisticsReturnCode MIXSKU_INSERT_ERR = new LogisticsReturnCode("组合商品信息新增失败", MIXSKU_INSERT_ERR_CODE);
    public final static LogisticsReturnCode MIXSKU_UPDATE_ERR = new LogisticsReturnCode("组合商品信息更新失败", MIXSKU_UPDATE_ERR_CODE);
    public final static LogisticsReturnCode DUPLICATE_BARCODE_ERR = new LogisticsReturnCode("重复的商品条码", DUPLICATE_BARCODE_ERROR_CODE);
    public final static LogisticsReturnCode SKU_POSTTAXCODE_NOT_MATCH_DECLARE = new LogisticsReturnCode("商品的行邮税号与申报的不一致", SKU_POSTTAXCODE_NOT_MATCH_DECLARE_CODE);
    public final static LogisticsReturnCode SKU_DECLARE_EXIST = new LogisticsReturnCode("商品的备案信息已存在", SKU_DECLARE_EXIST_CODE);
    public final static LogisticsReturnCode STOCK_EXIST_SKU_DELETE_ERROR = new LogisticsReturnCode("尚有库存的商品不允许删除", STOCK_EXIST_SKU_DELETE_ERROR_CODE);
    public final static LogisticsReturnCode SKU_BARCODE_IS_EMPTY = new LogisticsReturnCode("商品条码为空", SKU_BARCODE_IS_EMPTY_CODE);
    public final static LogisticsReturnCode STOCK_EXIST_SKU_BARCODE_EDIT_NOT_ALLOWED_CODE_ERROR = new LogisticsReturnCode("商品已存在库存，不允许修改条码", STOCK_EXIST_SKU_BARCODE_EDIT_NOT_ALLOWED_CODE);
    public final static LogisticsReturnCode SKU_DECLARE_NOT_EXIST = new LogisticsReturnCode("商品备案信息不存在", SKU_DECLARE_NOT_EXIST_CODE);
    public final static LogisticsReturnCode SKU_DECLARE_TAXRATE_ILLEGAL = new LogisticsReturnCode("商品备案税率不合法", SKU_DECLARE_TAXRATE_ILLEGAL_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_DECLARE_PRICE_SAVE_FAILURE = new LogisticsReturnCode("出库单备案价格保存失败", STOCKOUT_ORDER_DECLARE_PRICE_SAVE_FAILURE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_SKU_DECLARE_PRICE_NOT_EXIST = new LogisticsReturnCode("出库单商品申报价格不存在", STOCKOUT_ORDER_SKU_DECLARE_PRICE_NOT_EXIST_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_PROVIDER_META_NOT_MATCH = new LogisticsReturnCode("出库单供应商原信息不完整", STOCKOUT_ORDER_PROVIDER_META_NOT_MATCH_CODE);
    public final static LogisticsReturnCode SKU_DECLARE_WEIGHT_IS_EMPTY = new LogisticsReturnCode("商品备案重量为空", SKU_DECLARE_WEIGHT_IS_EMPTY_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_DECLARE_ACTURE_PAY_IS_ZERO = new LogisticsReturnCode("实际支付申报不能为0", STOCKOUT_ORDER_DECLARE_ACTURE_PAY_IS_ZERO_COEE);


    /**
     * providerServiceErrorCode
     * <p/>
     * 错误范围 2000000 -- 2050000
     */
    public final static int PROVIDER_CODE_ERROR = 2050000;
    public final static int PROVIDER_NAME_ERROR = 2050001;
    public final static int PROVIDER_FOREIGN_NAME_EMPTY = 2050002;
    public final static int PROVIDER_STATUS_IS_EMPTY = 2050003;
    public final static int PROVIDER_COMPANY_SIMPLE_NAME_EMPTY = 2050004;
    public final static int PROVIDER_CODE_ALREADY_EXISTS_ERROR = 2050005;
    public final static int PROVIDER_DATEBASE_ERROR = 2050006;
    public final static int ROVIDER_SERVICE_PARAMS_ILLEGAL_CODE = 2050007;
    public final static int PROVIDER_SERVICE_PROVIDERCODE_NOT_EXIST_CODE = 2050008;
    public final static int PROVIDER_SERVICE_PROVIDERID_NOT_EXIST_CODE = 2050009;
    
    public final static LogisticsReturnCode PROVIDER_CODE_ERROR_EXCEPTION = new LogisticsReturnCode("供应商编码为空或错误", PROVIDER_CODE_ERROR);
    public final static LogisticsReturnCode PROVIDER_NAME_ERROR_EXCEPTION = new LogisticsReturnCode("供应商名称不能为空", PROVIDER_NAME_ERROR);
    public final static LogisticsReturnCode PROVIDER_FOREIGN_NAME_EMPTY_EXCEPTION = new LogisticsReturnCode("供应商外文名称不能为空", PROVIDER_FOREIGN_NAME_EMPTY);
    public final static LogisticsReturnCode PROVIDER_STATUS_IS_EMPTY_EXCEPTION = new LogisticsReturnCode("供应商状态不能为空", PROVIDER_STATUS_IS_EMPTY);
    public final static LogisticsReturnCode PROVIDER_COMPANY_SIMPLE_NAME_EMPTY_EXCEPTION = new LogisticsReturnCode("供应商简称不能为空", PROVIDER_COMPANY_SIMPLE_NAME_EMPTY);
    public final static LogisticsReturnCode PROVIDER_CODE_ALREADY_EXISTS_ERROR_EXCEPTION = new LogisticsReturnCode("供应商编码已存在", PROVIDER_CODE_ALREADY_EXISTS_ERROR);
    public final static LogisticsReturnCode PROVIDER_DATEBASE_ERROR_EXCEPTION = new LogisticsReturnCode("数据库异常", PROVIDER_DATEBASE_ERROR);
    public final static LogisticsReturnCode ROVIDER_SERVICE_PARAMS_ILLEGAL = new LogisticsReturnCode("方法参数为空或不合法", ROVIDER_SERVICE_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode PROVIDER_SERVICE_PROVIDERCODE_NOT_EXIST = new LogisticsReturnCode("供应商编码不存在", PROVIDER_SERVICE_PROVIDERCODE_NOT_EXIST_CODE);
    public final static LogisticsReturnCode PROVIDER_SERVICE_PROVIDERID_NOT_EXIST = new LogisticsReturnCode("供应商不存在或已被删除", PROVIDER_SERVICE_PROVIDERID_NOT_EXIST_CODE);
    
    /**
     * WarehouseServiceErrorCode
     * <p/>
     * 错误范围 2000000 -- 2060000
     */
    public final static int WAREHOUSE_CODE_ERROR = 2060000;
    public final static int WAREHOUSE_NAME_ERROR = 2060001;
    public final static int WAREHOUSE_NATION_NOT_FOUND_ERROR = 2060002;
    public final static int WAREHOUSE_DATABASE_ERROR = 2060003;
    public final static int WAREHOUSE_NID_EMPTY_ERROR = 2060004;
    public final static int WAREHOUSE_NID_ALREADY_EXISTS_ERROR = 2060005;
    public final static int WAREHOUSE_NOT_EXISTS_ERROR_EXCEPTION_CODE = 2060006;
    
    public final static LogisticsReturnCode WAREHOUSE_CODE_ERROR_EXCEPTION = new LogisticsReturnCode("仓库编码为空或错误", WAREHOUSE_CODE_ERROR);
    public final static LogisticsReturnCode WAREHOUSE_NAME_ERROR_EXCEPTION = new LogisticsReturnCode("仓库名称不能为空", WAREHOUSE_NAME_ERROR);
    public final static LogisticsReturnCode WAREHOUSE_NATION_NOT_FOUND_ERROR_EXCEPTION = new LogisticsReturnCode("国家没找到", WAREHOUSE_NATION_NOT_FOUND_ERROR);
    public final static LogisticsReturnCode WAREHOUSE_DATABASE_ERROR_EXCEPTION = new LogisticsReturnCode("数据库异常", WAREHOUSE_DATABASE_ERROR);
    public final static LogisticsReturnCode WAREHOUSE_NID_EMPTY_ERROR_EXCEPTION = new LogisticsReturnCode("仓库编码不能为空", WAREHOUSE_NID_EMPTY_ERROR);
    public final static LogisticsReturnCode WAREHOUSE_NID_ALREADY_EXISTS_ERROR_EXCEPTION = new LogisticsReturnCode("仓库编码已存在", WAREHOUSE_NID_ALREADY_EXISTS_ERROR);
    public final static LogisticsReturnCode WAREHOUSE_NOT_EXISTS_ERROR_EXCEPTION = new LogisticsReturnCode("仓库不存在", WAREHOUSE_NOT_EXISTS_ERROR_EXCEPTION_CODE);
    
    /**
     * stockServiceErrorCode
     * <p/>
     * 错误范围 2000000 -- 2070000
     */
    public final static int STOCK_SERVICE_PARAMS_ILLEGAL_CODE = 2070000;
    public final static int STOCK_SERVICE_INNER_EXCEPTION_CODE = 2070001;
    public final static int STOCK_SERVICE_FREEZE_RECORD_NOT_FOUND_CODE = 2070002;
    public final static int STOCK_SERVICE_FREEZE_RECORD_NOT_MATCH_CODE = 2070003;
    public final static int STOCK_SERVICE_ACTUAL_RECORD_NOT_FOUND_CODE = 2070004;
    public final static int STOCK_SERVICE_FREEZE_RECORD_STATE_ERROR_CODE = 2070005;
    public final static int STOCK_SERVICE_FREEZE_COUNT_NOT_MATCH_CODE = 2070006;
    public final static int STOCK_SERVICE_STOCK_PRICE_INVALID_CODE = 2070007;
    public final static int STOCK_SERVICE_SALE_RECORD_UPDATE_ERROR_CODE = 2070008;
    public final static int STOCK_SERVICE_SALE_COUNT_NOT_ENOUGH_ERROR_CODE = 2070009;
    public final static int STOCK_SERVICE_SALE_RECORD_NOT_FOUND = 2070010;
    public final static int STOCK_SERVICE_BATCH_STOCK_RECORD_NOT_FOUND_CODE = 2070011;
    public final static int STOCK_SERVICE_BATCH_STOCK_PRICE_INVALID_CODE = 2070012;
    //入库回传数据错误
    public final static int STOCK_CALLBACK_ERROR = 2070013;
    public final static int STOCK_SERVICE_SALE_FREEZE_RECORD_NOT_FOUND_CODE = 2070014;
    public final static int STOCK_SERVICE_BATCH_STOCK_RECODE_DUPLICATE_CODE = 2070015;
    public final static int STOCK_SERVICE_FREEZE_IN_BATCH_FAIL_CODE = 2070016;
    public final static int STOCK_SERVICE_BATCH_STOCK_LOCK_LOG_NOT_FOUND_CODE = 2070017;
    public final static int STOCK_SERVICE_BATCH_STOCK_LOCK_LOG_EXIST_CODE = 2070018;
    public final static int STOCK_SERVICE_SAFE_COUNT_NOT_ENOUGH_ERROR_CODE = 2070019;
    public final static int OPERATE_CONSIGN_STOCK_ERROR_CODE = 2070020;
    public final static int SYNC_CONSIGN_STOCK_ERROR_CODE = 2070021;
    public final static int UNSUPPORT_BATCH_STOCK_LOCK_WAREHOUSE_CODE = 2070022;
    public final static int RELEASE_PRESELL_STOCK_ERROR_CODE = 2070023;

    public final static LogisticsReturnCode STOCK_SERVICE_PARAMS_ILLEGAL = new LogisticsReturnCode("方法参数为空或不合法", STOCK_SERVICE_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_INNER_EXCEPTION = new LogisticsReturnCode("库存服务系统内部异常", STOCK_SERVICE_INNER_EXCEPTION_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_FREEZE_RECORD_NOT_FOUND = new LogisticsReturnCode("未找到冻结库存记录", STOCK_SERVICE_FREEZE_RECORD_NOT_FOUND_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_FREEZE_RECORD_NOT_MATCH = new LogisticsReturnCode("冻结库存记录和实际出库记录不符", STOCK_SERVICE_FREEZE_RECORD_NOT_MATCH_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_ACTUAL_RECORD_NOT_FOUND = new LogisticsReturnCode("未找到实物库存记录", STOCK_SERVICE_ACTUAL_RECORD_NOT_FOUND_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_FREEZE_RECORD_STATE_ERROR = new LogisticsReturnCode("冻结库存的状态异常", STOCK_SERVICE_FREEZE_RECORD_STATE_ERROR_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_FREEZE_COUNT_NOT_MATCH = new LogisticsReturnCode("冻结库存的数量和最终实际消费数量不符", STOCK_SERVICE_FREEZE_COUNT_NOT_MATCH_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_STOCK_PRICE_INVALID = new LogisticsReturnCode("商品库存的成本价为空或者小于等于0", STOCK_SERVICE_STOCK_PRICE_INVALID_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_SALE_RECORD_UPDATE_ERROR = new LogisticsReturnCode("更改销售库存失败", STOCK_SERVICE_SALE_RECORD_UPDATE_ERROR_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_SALE_COUNT_NOT_ENOUGH_ERROR = new LogisticsReturnCode("可售库存不足", STOCK_SERVICE_SALE_COUNT_NOT_ENOUGH_ERROR_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_SALE_RECORD_NOT_FOUND_ERROR = new LogisticsReturnCode("未找到可售库存记录", STOCK_SERVICE_SALE_RECORD_NOT_FOUND);
    public final static LogisticsReturnCode STOCK_SERVICE_BATCH_STOCK_RECORD_NOT_FOUND = new LogisticsReturnCode("未找到商品批次库存记录", STOCK_SERVICE_BATCH_STOCK_RECORD_NOT_FOUND_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_BATCH_STOCK_PRICE_INVALID = new LogisticsReturnCode("商品批次库存的成本价为空或者小于等于0", STOCK_SERVICE_BATCH_STOCK_PRICE_INVALID_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_SALE_FREEZE_RECORD_NOT_FOUND_ERROR = new LogisticsReturnCode("未找到可售库存冻结记录", STOCK_SERVICE_SALE_FREEZE_RECORD_NOT_FOUND_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_FREEZE_IN_BATCH_FAIL = new LogisticsReturnCode("批量冻结库存失败", STOCK_SERVICE_FREEZE_IN_BATCH_FAIL_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_BATCH_STOCK_RECODE_DUPLICATE = new LogisticsReturnCode("商品批次库存重复", STOCK_SERVICE_BATCH_STOCK_RECODE_DUPLICATE_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_BATCH_STOCK_LOCK_LOG_NOT_FOUND = new LogisticsReturnCode("找不到批次禁用记录", STOCK_SERVICE_BATCH_STOCK_LOCK_LOG_NOT_FOUND_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_BATCH_STOCK_LOCK_LOG_EXIST = new LogisticsReturnCode("批次禁用记录已存在", STOCK_SERVICE_BATCH_STOCK_LOCK_LOG_EXIST_CODE);
    public final static LogisticsReturnCode STOCK_SERVICE_SAFE_COUNT_NOT_ENOUGH_ERROR = new LogisticsReturnCode("安全库存不足", STOCK_SERVICE_SAFE_COUNT_NOT_ENOUGH_ERROR_CODE);
    public final static LogisticsReturnCode OPERATE_CONSIGN_STOCK_ERROR = new LogisticsReturnCode("调整寄售不入库商品库存数量失败", OPERATE_CONSIGN_STOCK_ERROR_CODE);
    public final static LogisticsReturnCode SYNC_CONSIGN_STOCK_ERROR = new LogisticsReturnCode("同步寄售不入库商品库存失败", SYNC_CONSIGN_STOCK_ERROR_CODE);
    public final static LogisticsReturnCode UNSUPPORT_BATCH_STOCK_LOCK_WAREHOUSE = new LogisticsReturnCode("退货仓和存储仓不需要使用批次禁用功能", UNSUPPORT_BATCH_STOCK_LOCK_WAREHOUSE_CODE);
    public final static LogisticsReturnCode RELEASE_PRESELL_STOCK_ERROR = new LogisticsReturnCode("释放预售商品库存数量失败", RELEASE_PRESELL_STOCK_ERROR_CODE);

    /**
     * customCategoryServiceErrorCode
     * <p/>
     * 错误范围 2000000 -- 2080000
     */
    public final static int CUSTOMCATEGORY_SERVICE_PARAMS_ILLEGAL_CODE = 2080000;
    public final static int CUSTOMCATEGORY_SERVICE_INNER_EXCEPTION_CODE = 2080001;
    
    public final static LogisticsReturnCode CUSTOMCATEGORY_SERVICE_PARAMS_ILLEGAL = new LogisticsReturnCode("方法参数为空或不合法", CUSTOMCATEGORY_SERVICE_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode CUSTOMCATEGORY_SERVICE_INNER_EXCEPTION = new LogisticsReturnCode("海关类目服务系统内部异常", CUSTOMCATEGORY_SERVICE_INNER_EXCEPTION_CODE);
    
    /**
     * nationServiceErrorCode
     * <p/>
     * 错误范围 2000000 -- 2090000
     */
    public final static int NATION_SERVICE_PARAMS_ILLEGAL_CODE = 2090000;
    public final static int NATION_SERVICE_INNER_EXCEPTION_CODE = 2090001;
    
    public final static LogisticsReturnCode NATION_SERVICE_PARAMS_ILLEGAL = new LogisticsReturnCode("方法参数为空或不合法", NATION_SERVICE_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode NATION_SERVICE_INNER_EXCEPTION = new LogisticsReturnCode("原产地服务系统内部异常", NATION_SERVICE_INNER_EXCEPTION_CODE);
    
    /**
     * providerSkuServiceErrorCode
     * <p/>
     * 错误范围 2100000 -- 2110000
     */
    public final static int PROVIDERSKU_SERVICE_PARAMS_ILLEGAL_CODE = 2100000;
    public final static int PROVIDERSKU_SERVICE_INNER_EXCEPTION_CODE = 2100001;
    public final static int PROVIDERSKU_NOT_EXIST_EXCEPTION_CODE = 2100002;
    
    public final static LogisticsReturnCode PROVIDERSKU_SERVICE_PARAMS_ILLEGAL = new LogisticsReturnCode("方法参数为空或不合法", PROVIDERSKU_SERVICE_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode PROVIDERSKU_SERVICE_INNER_EXCEPTION = new LogisticsReturnCode("供应商商品映射服务系统内部异常", PROVIDERSKU_SERVICE_INNER_EXCEPTION_CODE);
    public final static LogisticsReturnCode PROVIDERSKU_NOT_EXIST_EXCEPTION = new LogisticsReturnCode("供应商不存在", PROVIDERSKU_NOT_EXIST_EXCEPTION_CODE);
    
    /**
     * StockoutServiceErrorCode
     * <p/>
     * 错误范围 2110000 -- 2120000
     */
    public final static int STOCKOUT_ORDER_WAVENO_UPDATE_ERROR_CODE = 2110000;
    public final static int STOCKOUT_ORDER_ORDER_STATE_UPDATE_ERROR_CODE = 2110001;
    public final static int STOCKOUT_ORDER_UPDATE_WAVENO_EXISTS_ERROR_CODE = 2110002;
    public final static int STOCKOUT_ORDER_STATE_CHANGE_EXCEPTION_CODE = 2110003;
    public final static int STOCKOUT_ORDER_PARAM_ILLIGAL_CODE = 2110004;
    public final static int STOCKOUT_ORDER_STOCKOUT_EXCEPTION_CODE = 2110005;
    public final static int STOCKOUT_ORDER_SEND_EXCEPTION_CODE = 2110006;
    public final static int STOCKOUT_ORDER_REQUEST_PARAM_ILLEGAL_CODE = 2110007;
    public final static int STOCKOUT_ORDER_TPL_CREATE_FAILURE_CODE = 2110010;
    public final static int STOCKOUT_ORDER_PAY_CREATE_FAILURE_CODE = 2110011;
    public final static int STOCKOUT_ORDER_PORT_CREATE_FAILURE_CODE = 2110012;
    public final static int STOCKOUT_ORDER_WMS_CREATE_FAILURE_CODE = 2110013;
    public final static int STOCKOUT_ORDER_CANNOT_CLOSE_CODE = 2110014;
    public final static int STOCKOUT_ORDER_ENGINE_PARAM_ILLAGLE_CODE = 2110015;
    public final static int STOCKOUT_ORDER_TPL_SEND_FAILURE_CODE = 2110016;
    public final static int STOCKOUT_ORDER_WMS_SEND_FAILURE_CODE = 2110017;
    public final static int STOCKOUT_COMMON_DECLARE_TYPE_NOT_FOUND_CODE = 2110018;
    public final static int STOCKOUT_ORDER_WMS_PORT_VALIDATE_FAILURE_CODE = 2110019;
    public final static int STOCKOUT_ORDER_CONFIG_FAILURE_CODE = 2110020;
    public final static int COMMAND_NOT_SUPPORT_CODE = 2110021;
    public final static int STOCKOUT_ORDER_STATE_TO_SINGED_NOT_ALLOW_CODE = 2110022;
    public final static int STOCKOUT_ORDER_OBJECT_CONVERT_ERROR_CODE = 2110023;
    public final static int STOCKOUT_ORDER_OBJECT_CONVERT_PARAMS_ILLEGAL_CODE = 2110024;
    public final static int STOCKOUT_ORDER_NOT_ALLOW_RESPLIT_CODE = 2110025;
    public final static int STOCKOUT_ORDER_NOT_GET_ZTO_REMARK_CODE = 2110026;
    public final static int PRESELL_ORDER_CREATE_ERROR_CODE = 2110027;
    public final static int STOCKOUT_ORDER_CCB_CONFIRM_WEIGHT_ERROR_CODE = 2110028;
    public final static int STOCKOUT_ORDER_CCB_CONFIRM_ERROR_CODE = 2110029;
    public final static int STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_ILLEGAL_CODE = 2110030;
    public final static int STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_SEND_ERROR_CODE = 2110031;
    
    public final static LogisticsReturnCode STOCKOUT_ORDER_WAVENO_UPDATE_ERROR = new LogisticsReturnCode("出库单波次号更新失败", STOCKOUT_ORDER_WAVENO_UPDATE_ERROR_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_ORDER_STATE_UPDATE_ERROR = new LogisticsReturnCode("供应链推送订单状态更改失败", STOCKOUT_ORDER_ORDER_STATE_UPDATE_ERROR_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_UPDATE_WAVENO_EXISTS_ERROR = new LogisticsReturnCode("波次号已存在，更新波次号失败", STOCKOUT_ORDER_UPDATE_WAVENO_EXISTS_ERROR_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_STATE_CHANGE_EXCEPTION = new LogisticsReturnCode("单据状态流转异常", STOCKOUT_ORDER_STATE_CHANGE_EXCEPTION_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_PARAM_ILLIGAL = new LogisticsReturnCode("出库单参数不合法", STOCKOUT_ORDER_PARAM_ILLIGAL_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_STOCKOUT_EXCEPTION = new LogisticsReturnCode("出库单下发出库异常", STOCKOUT_ORDER_STOCKOUT_EXCEPTION_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_SEND_EXCEPTION = new LogisticsReturnCode("出库单发货异常", STOCKOUT_ORDER_SEND_EXCEPTION_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_REQUEST_PARAM_ILLEGAL = new LogisticsReturnCode("出库单报文请求参数不合法", STOCKOUT_ORDER_REQUEST_PARAM_ILLEGAL_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_TPL_CREATE_FAILURE = new LogisticsReturnCode("三方物流TPL订单下发失败", STOCKOUT_ORDER_TPL_CREATE_FAILURE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_TPL_SEND_FAILURE = new LogisticsReturnCode("三方物流TPL运单确认失败", STOCKOUT_ORDER_TPL_SEND_FAILURE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_PAY_CREATE_FAILURE = new LogisticsReturnCode("支付单申报下发失败", STOCKOUT_ORDER_PAY_CREATE_FAILURE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_PORT_CREATE_FAILURE = new LogisticsReturnCode("口岸订单申报下发失败", STOCKOUT_ORDER_PORT_CREATE_FAILURE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_WMS_CREATE_FAILURE = new LogisticsReturnCode("仓库订单下发失败", STOCKOUT_ORDER_WMS_CREATE_FAILURE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_WMS_SEND_FAILURE = new LogisticsReturnCode("仓库订单下发发货失败", STOCKOUT_ORDER_WMS_SEND_FAILURE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_CANNOT_CLOSE = new LogisticsReturnCode("出库单状态不允许关闭", STOCKOUT_ORDER_CANNOT_CLOSE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_ENGINE_PARAM_ILLAGLE = new LogisticsReturnCode("出库单状态流转参数不合法，缺少ID", STOCKOUT_ORDER_ENGINE_PARAM_ILLAGLE_CODE);
    public final static LogisticsReturnCode STOCKOUT_COMMON_DECLARE_TYPE_NOT_FOUND = new LogisticsReturnCode("未找到共通的申报方式", STOCKOUT_COMMON_DECLARE_TYPE_NOT_FOUND_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_CONFIG_EXCEPTION = new LogisticsReturnCode("系统配置出错", STOCKOUT_ORDER_CONFIG_FAILURE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_WMS_PORT_VALIDATE_FAILURE = new LogisticsReturnCode("口岸避税验证失败", STOCKOUT_ORDER_WMS_PORT_VALIDATE_FAILURE_CODE);
    public final static LogisticsReturnCode COMMAND_NOT_SUPPORT = new LogisticsReturnCode("命令不支持", COMMAND_NOT_SUPPORT_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_STATE_TO_SINGED_NOT_ALLOW = new LogisticsReturnCode("出库单状态不允许流转到已签收", STOCKOUT_ORDER_STATE_TO_SINGED_NOT_ALLOW_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_OBJECT_CONVERT_ERROR = new LogisticsReturnCode("对象转换异常", STOCKOUT_ORDER_OBJECT_CONVERT_ERROR_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_OBJECT_CONVERT_PARAMS_ILLEGAL = new LogisticsReturnCode("对象转换参数不合法", STOCKOUT_ORDER_OBJECT_CONVERT_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_NOT_ALLOW_RESPLIT = new LogisticsReturnCode("出库单状态不允许重新分包", STOCKOUT_ORDER_NOT_ALLOW_RESPLIT_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_NOT_GET_ZTO_REMARK = new LogisticsReturnCode("获取中通大头笔失败", STOCKOUT_ORDER_NOT_GET_ZTO_REMARK_CODE);
    public final static LogisticsReturnCode PRESELL_ORDER_CREATE_ERROR = new LogisticsReturnCode("预售订单暂不能创建出库单", PRESELL_ORDER_CREATE_ERROR_CODE);
    public final static LogisticsReturnCode DELAY_ORDER_CREATE_ERROR = new LogisticsReturnCode("G20杭州订单暂不能创建出库单", PRESELL_ORDER_CREATE_ERROR_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_CCB_CONFIRM_WEIGHT_ERROR = new LogisticsReturnCode("出库单下发清关确认重量参数不合法", STOCKOUT_ORDER_CCB_CONFIRM_WEIGHT_ERROR_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_CCB_CONFIRM_ERROR = new LogisticsReturnCode("出库单下发清关确认失败", STOCKOUT_ORDER_CCB_CONFIRM_ERROR_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_ILLEGAL = new LogisticsReturnCode("订单下发海关总署，参数不合法", STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_ILLEGAL_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_SEND_ERROR = new LogisticsReturnCode("订单下发海关总署，发送异常", STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_SEND_ERROR_CODE);

    /**
     * LineServiceErrorCode
     * <p/>
     * 错误范围 2120000 -- 2130000
     */
    public final static int PAYTYPE_NOT_SUPPORT_ERROR_CODE = 2120000;
    public final static int PAYTYPE_SERVICE_INNER_EXCEPTION_CODE = 2120001;
    public final static int ADD_LINE_CONFIGURE_ERROR_CODE = 2120002;
    public final static int SAVE_LINE_DELAY_NOTICE_ERROR_CODE = 2120003;
    public final static int QUERY_LINE_DELAY_NOTICE_ERROR_CODE = 2120004;
    public final static int LINE_DELAY_NOTICE_NOT_EXIST_ERROR_CODE = 2120005;
    public final static int LINE_CHANNEL_WAREHOUSE_MAPPING_NOT_EXIST_ERROR_CODE = 2120006;
    public final static int LINE_DELAY_NOTICE_CACHE_ERROR_CODE = 2120007;
    public final static int LINE_CHANNEL_WAREHOUSE_MAPPING_NO_MATCH_ERROR_CODE = 2120008;

   
    public final static LogisticsReturnCode PAYTYPE_NOT_SUPPORT_ERROR = new LogisticsReturnCode("支付类型错误", PAYTYPE_NOT_SUPPORT_ERROR_CODE);
    public final static LogisticsReturnCode PAYTYPE_SERVICE_INNER_EXCEPTION = new LogisticsReturnCode("线路服务系统内部异常", PAYTYPE_SERVICE_INNER_EXCEPTION_CODE);
    public final static LogisticsReturnCode ADD_LINE_CONFIGURE_ERROR = new LogisticsReturnCode("新增线路配置信息异常", ADD_LINE_CONFIGURE_ERROR_CODE);
    public final static LogisticsReturnCode SAVE_LINE_DELAY_NOTICE_ERROR = new LogisticsReturnCode("保存线路提醒信息异常", SAVE_LINE_DELAY_NOTICE_ERROR_CODE);
    public final static LogisticsReturnCode QUERY_LINE_DELAY_NOTICE_ERROR = new LogisticsReturnCode("查询可用线路提醒信息异常", QUERY_LINE_DELAY_NOTICE_ERROR_CODE);
    public final static LogisticsReturnCode LINE_DELAY_NOTICE_NOT_EXIST_ERROR = new LogisticsReturnCode("未找到ID对应的有效线路", LINE_DELAY_NOTICE_NOT_EXIST_ERROR_CODE);
    public final static LogisticsReturnCode LINE_DELAY_NOTICE_CACHE_ERROR = new LogisticsReturnCode("设置物流提醒缓存异常", LINE_DELAY_NOTICE_CACHE_ERROR_CODE);
    public final static LogisticsReturnCode LINE_CHANNEL_WAREHOUSE_MAPPING_NOT_EXIST_ERROR = new LogisticsReturnCode("线路配置中此渠道的渠道仓库映射配置不存在", LINE_CHANNEL_WAREHOUSE_MAPPING_NOT_EXIST_ERROR_CODE);
    public final static LogisticsReturnCode LINE_CHANNEL_WAREHOUSE_MAPPING_NO_MATCH_ERROR = new LogisticsReturnCode("根据渠道仓库映射配置，过滤仓库列表，没有匹配到任何可用仓库", LINE_CHANNEL_WAREHOUSE_MAPPING_NO_MATCH_ERROR_CODE);
    
    /**
     * SendMsgErrorCode
     * 发送短信&邮件消息错误码
     * <p/>
     * 错误范围 2130000 -- 2140000
     */
    public final static int SEND_SMS_ERROR_CODE = 2130000;
    public final static int SEND_EMAIL_ERROR_CODE = 2130001;
    
    public final static LogisticsReturnCode SEND_SMS_ERROR = new LogisticsReturnCode("发送短信异常", SEND_SMS_ERROR_CODE);
    public final static LogisticsReturnCode SEND_EMAIL_ERROR = new LogisticsReturnCode("发送邮件异常", SEND_EMAIL_ERROR_CODE);
    
    /**
     * LiuLianServiceErrorCode
     * <p/>
     * 错误范围 2140000 -- 2150000
     */
    public final static int LIULIAN_SERVICE_PARAMS_ILLEGAL_CODE = 2140000;
    public final static int LIULIAN_SERVICE_FREIGHT_NOT_FOUND_CODE = 2140001;
    
    public final static LogisticsReturnCode LIULIAN_SERVICE_PARAMS_ILLEGAL = new LogisticsReturnCode("方法参数为空或不合法", LIULIAN_SERVICE_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode LIULIAN_SERVICE_FREIGHT_NOT_FOUND = new LogisticsReturnCode("未查询到符合条件的运费", LIULIAN_SERVICE_FREIGHT_NOT_FOUND_CODE);
    
    /**
     * LogisticsCompanyServiceErrorCode
     * 发送短信&邮件消息错误码
     * <p/>
     * 错误范围 2150000 -- 2160000
     */
    public final static int LOGISTICS_COMPANY_SERVICE_PARAMS_ILLEGAL_CODE = 2150000;
    public final static int LOGISTICS_COMPANY_SERVICE_STOCKOUTORDER_NOT_EXIST_CODE = 2150001;
    public final static int LOGISTICS_COMPANY_SERVICE_WAREHOUSE_NOT_EXIST_CODE = 2150002;
    public final static int LOGISTICS_COMPANY_SERVICE_WAREHOUSE_PROVIDER_NOT_EXIST_CODE = 2150003;
    public final static int LOGISTICS_COMPANY_SERVICE_PERSONAL_GOODES_COUNT_NOT_MATCH_CODE = 2150004;
    public final static int LOGISTICS_COMPANY_SERVICE_PERSONAL_GOODES_WAYBILL_NOT_MATCH_CODE = 2150005;
    public final static int LOGISTICS_COMPANY_SERVICE_WAREHOUSE_PROVIDER_META_PART_MISSING_CODE = 2150006;
    public final static int LOGISTICS_COMPANY_SERVICE_WAREHOUSE_PROVIDER_META_ILLEGAL_CODE = 2150007;
    public final static int LOGISTICS_COMPANY_SERVICE_PERSONAL_GOODES_PRODUCT_RECORD_MISSING_CODE = 2150008;
    
    public final static LogisticsReturnCode LOGISTICS_COMPANY_SERVICE_PARAMS_ILLEGAL = new LogisticsReturnCode("方法参数为空或不合法", LOGISTICS_COMPANY_SERVICE_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode LOGISTICS_COMPANY_SERVICE_STOCKOUTORDER_NOT_EXIST = new LogisticsReturnCode("出库单不存在", LOGISTICS_COMPANY_SERVICE_STOCKOUTORDER_NOT_EXIST_CODE);
    public final static LogisticsReturnCode LOGISTICS_COMPANY_SERVICE_WAREHOUSE_NOT_EXIST = new LogisticsReturnCode("仓库不存在", LOGISTICS_COMPANY_SERVICE_WAREHOUSE_NOT_EXIST_CODE);
    public final static LogisticsReturnCode LOGISTICS_COMPANY_SERVICE_WAREHOUSE_PROVIDER_NOT_EXIST = new LogisticsReturnCode("仓库的供应商不存在", LOGISTICS_COMPANY_SERVICE_WAREHOUSE_PROVIDER_NOT_EXIST_CODE);
    public final static LogisticsReturnCode LOGISTICS_COMPANY_SERVICE_PERSONAL_GOODES_COUNT_NOT_MATCH = new LogisticsReturnCode("订单中商品数与需申报的商品数不匹配", LOGISTICS_COMPANY_SERVICE_PERSONAL_GOODES_COUNT_NOT_MATCH_CODE);
    public final static LogisticsReturnCode LOGISTICS_COMPANY_SERVICE_PERSONAL_GOODES_WAYBILL_NOT_MATCH = new LogisticsReturnCode("订单中商品数与需申报的运单不匹配", LOGISTICS_COMPANY_SERVICE_PERSONAL_GOODES_WAYBILL_NOT_MATCH_CODE);
    public final static LogisticsReturnCode LOGISTICS_COMPANY_SERVICE_WAREHOUSE_PROVIDER_META_PART_MISSING = new LogisticsReturnCode("仓库的供应商元信息部分缺失", LOGISTICS_COMPANY_SERVICE_WAREHOUSE_PROVIDER_META_PART_MISSING_CODE);
    public final static LogisticsReturnCode LOGISTICS_COMPANY_SERVICE_WAREHOUSE_PROVIDER_META_ILLEGAL = new LogisticsReturnCode("仓库的供应商元信息格式不正确", LOGISTICS_COMPANY_SERVICE_WAREHOUSE_PROVIDER_META_ILLEGAL_CODE);
    public final static LogisticsReturnCode LOGISTICS_COMPANY_SERVICE_PERSONAL_GOODES_PRODUCT_RECORD_MISSING = new LogisticsReturnCode("个人申报单中商品国检备案编码部分缺失", LOGISTICS_COMPANY_SERVICE_PERSONAL_GOODES_PRODUCT_RECORD_MISSING_CODE);
    
    /**
     * collectingPurchaseServiceErrorCode
     * 集货采购服务 错误码
     * <p/>
     * 错误范围 2200000 -- 2210000
     */
    public final static int COLLECTING_PURCHASE_ORDER_NOT_EXIT_CODE = 2200000;
    public final static int COLLECTING_PURCHASE_ORDER_HAS_FINISHED_CODE = 2200001;
    public final static int COLLECTING_PURCHASE_ORDER_PARAM_ILLEGAL_CODE = 2200002;
    public final static int COLLECTING_PURCHASE_ORDER_SKU_NOT_EXIST_CODE = 2200003;
    public final static int COLLECTING_PURCHASE_RESULT_MAILNO_EXIST_CODE = 2200004;
    
    public final static LogisticsReturnCode COLLECTING_PURCHASE_ORDER_NOT_EXIT = new LogisticsReturnCode("采购单不存在", COLLECTING_PURCHASE_ORDER_NOT_EXIT_CODE);
    public final static LogisticsReturnCode COLLECTING_PURCHASE_ORDER_HAS_FINISHED = new LogisticsReturnCode("采购已经处于完成状态", COLLECTING_PURCHASE_ORDER_HAS_FINISHED_CODE);
    public final static LogisticsReturnCode COLLECTING_PURCHASE_ORDER_PARAM_ILLEGAL = new LogisticsReturnCode("参数不合法", COLLECTING_PURCHASE_ORDER_PARAM_ILLEGAL_CODE);
    public final static LogisticsReturnCode COLLECTING_PURCHASE_ORDER_SKU_NOT_EXIST = new LogisticsReturnCode("SKU不存在", COLLECTING_PURCHASE_ORDER_SKU_NOT_EXIST_CODE);
    public final static LogisticsReturnCode COLLECTING_PURCHASE_RESULT_MAILNO_EXIST = new LogisticsReturnCode("运单号已存在", COLLECTING_PURCHASE_RESULT_MAILNO_EXIST_CODE);
    
    /**
     * CollectingStockinServiceErrorCode
     * 集货入库服务 错误码
     * <p/>
     * 错误范围 2210000 -- 2220000
     */
    public final static int COLLECTING_STOCKIN_ORDER_PARAM_ILLEGAL_CODE = 2210000;
    public final static int COLLECTING_STOCKIN_ORDER_MAILNO_EXIST_CODE = 2210001;
    public final static int COLLECTING_STOCKIN_ORDER_PURCHASE_RESUL_NOT_EXIST_CODE = 2210002;
    
    public final static LogisticsReturnCode COLLECTING_STOCKIN_ORDER_PARAM_ILLEGAL = new LogisticsReturnCode("参数不合法", COLLECTING_PURCHASE_ORDER_PARAM_ILLEGAL_CODE);
    public final static LogisticsReturnCode COLLECTING_STOCKIN_ORDER_MAILNO_EXIST = new LogisticsReturnCode("运单号对应的入库记录已存在", COLLECTING_STOCKIN_ORDER_MAILNO_EXIST_CODE);
    public final static LogisticsReturnCode COLLECTING_STOCKIN_ORDER_PURCHASE_RESUL_NOT_EXIST = new LogisticsReturnCode("运单号对应的采购记录不存在", COLLECTING_STOCKIN_ORDER_PURCHASE_RESUL_NOT_EXIST_CODE);

    /**
     * PortServiceErrorCode
     * 口岸服务 错误码
     * <p/>
     * 错误范围 2220000 -- 2230000
     */
    public final static int PORT_SERVICE_PORT_NOT_EXIST_CODE = 2220000;
    public final static int PORT_SERVICE_ORDER_DELCARE_NOT_SUPPORT_CODE = 2220001;
    
    public final static LogisticsReturnCode PORT_SERVICE_PORT_NOT_EXIST = new LogisticsReturnCode("口岸不存在", PORT_SERVICE_PORT_NOT_EXIST_CODE);
    public final static LogisticsReturnCode PORT_SERVICE_ORDER_DELCARE_NOT_SUPPORT = new LogisticsReturnCode("口岸不支持订单申报", PORT_SERVICE_ORDER_DELCARE_NOT_SUPPORT_CODE);
    
    /**
     * PayCompanyServiceErrorCode
     * 支付企业服务 错误码
     * <p/>
     * 错误范围 2230000 -- 2240000
     */
    public final static int PAY_COMPANY_SERVICE_PARAMS_ILLEGAL_CODE = 2220000;
    public final static int PAY_COMPANY_SERVICE_ORDER_DELCARE_NOT_SUPPORT_CODE = 2220001;
    public final static int PAY_COMPANY_SERVICE_ORDER_DELCARE_STATE_NOT_ALLOW_CODE = 2220002;
    public final static int PAY_COMPANY_SERVICE_PAY_BILL_NOT_EXIST_CODE = 2220003;
    public final static int PAY_COMPANY_SERVICE_PAY_BILL_PROXY_ONLY_ON_HANGZHOU_PORT_CODE = 2220004;
    public final static int PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION_CODE = 2220005;
    
    public final static LogisticsReturnCode PAY_COMPANY_SERVICE_PARAMS_ILLEGAL = new LogisticsReturnCode("参数不合法", PAY_COMPANY_SERVICE_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode PAY_COMPANY_SERVICE_ORDER_DELCARE_NOT_SUPPORT = new LogisticsReturnCode("口岸不支持订单申报", PAY_COMPANY_SERVICE_ORDER_DELCARE_NOT_SUPPORT_CODE);
    public final static LogisticsReturnCode PAY_COMPANY_SERVICE_ORDER_DELCARE_STATE_NOT_ALLOW = new LogisticsReturnCode("支付单申报状态不允许再次申报", PAY_COMPANY_SERVICE_ORDER_DELCARE_STATE_NOT_ALLOW_CODE);
    public final static LogisticsReturnCode PAY_COMPANY_SERVICE_PAY_BILL_NOT_EXIST = new LogisticsReturnCode("支付单申报记录不存在", PAY_COMPANY_SERVICE_PAY_BILL_NOT_EXIST_CODE);
    public final static LogisticsReturnCode PAY_COMPANY_SERVICE_PAY_BILL_PROXY_ONLY_ON_HANGZHOU_PORT = new LogisticsReturnCode("电商代理推送支付单只允许在杭州口岸", PAY_COMPANY_SERVICE_PAY_BILL_PROXY_ONLY_ON_HANGZHOU_PORT_CODE);
    public final static LogisticsReturnCode PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION = new LogisticsReturnCode("支付申报异常", PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION_CODE);

    /**
     * GalOrderService ErrorCode
     * 损溢单服务 错误码
     * <p/>
     * 错误范围： 2230000 -- 2240000
     */
    public final static int GAL_ORDER_INNER_EXCEPTION_CODE = 2230000;
    public final static int GAL_ORDER_SKU_FREEZE_FAILURE_CODE = 2230001;
    public final static int GAL_ORDER_NOT_FOUND_CODE = 2230002;
    public final static int GAL_ORDER_SKU_NOT_FOUND_CODE = 2230003;
    public final static int WMS_GAL_ORDER_CREATE_CALLBACK_ERROR = 2230004; //仓库回传盘点数据错误
    public final static int GAL_ORDER_CALL_GOODS_SYSTEM_REDUCE_CHANNEL_COUNT_CODE = 2230005;
    public final static int GAL_ORDER_CONFIRM_FAIL_CODE = 2230006;
    public final static int GAL_ORDER_CANCEL_FAIL_CODE = 2230007;
    public final static int BATCH_STOCK_NOT_ENOUGH_CODE = 2230008;
    public final static int GAL_ORDER_PARAMS_ILLEGAL_CODE = 2230009;
    public final static LogisticsReturnCode GAL_ORDER_INNER_EXCEPTION = new LogisticsReturnCode("损溢单系统内部异常",
            GAL_ORDER_INNER_EXCEPTION_CODE);
    public final static LogisticsReturnCode GAL_ORDER_SKU_FREEZE_FAILURE_ERROR = new LogisticsReturnCode(
            "损溢明细冻结失败", GAL_ORDER_SKU_FREEZE_FAILURE_CODE);
    public final static LogisticsReturnCode GAL_ORDER_NOT_FOUND_ERROR = new LogisticsReturnCode(
            "未找到损溢单记录", GAL_ORDER_NOT_FOUND_CODE);
    public final static LogisticsReturnCode GAL_ORDER_SKU_NOT_FOUND_ERROR = new LogisticsReturnCode(
            "未找到损溢单明细记录", GAL_ORDER_SKU_NOT_FOUND_CODE);
    public final static LogisticsReturnCode GAL_ORDER_CALL_GOODS_SYSTEM_REDUCE_CHANNEL_COUNT_ERROR = new LogisticsReturnCode(
            "调用商品系统接口扣减渠道库存失败", GAL_ORDER_CALL_GOODS_SYSTEM_REDUCE_CHANNEL_COUNT_CODE);
    public final static LogisticsReturnCode GAL_ORDER_CONFIRM_FAIL_CODE_ERROR = new LogisticsReturnCode(
            "下发损益单确认命令失败", GAL_ORDER_CONFIRM_FAIL_CODE);
    public final static LogisticsReturnCode GAL_ORDER_CANCEL_FAIL_CODE_ERROR = new LogisticsReturnCode(
            "损溢单取消失败", GAL_ORDER_CANCEL_FAIL_CODE);
    public final static LogisticsReturnCode BATCH_STOCK_NOT_ENOUGH_CODE_ERROR = new LogisticsReturnCode(
            "批次实物库存小于损益库存", BATCH_STOCK_NOT_ENOUGH_CODE);
    public final static LogisticsReturnCode GAL_ORDER_PARAMS_ILLEGAL = new LogisticsReturnCode("损溢单参数不合法",
            GAL_ORDER_PARAMS_ILLEGAL_CODE);


    /**
     * TransferOrderService ErrorCode
     * 调拨服务 错误码
     * <p/>
     * 错误范围： 2250000 -- 2260000
     */

    public final static int TRANSFER_ORDER_INNER_EXCEPTION_CODE = 2250000;
    public final static int TRANSFER_ORDER_SKU_FREEZE_FAILURE_CODE = 2250001;
    public final static int TRANSFER_ORDER_NOT_FOUND_CODE = 2250002;
    public final static int TRANSFER_ORDER_SKU_NOT_FOUND_CODE = 2250003;
    public final static int TRANSFER_ORDER_CONFIRM_FAIL_CODE = 2250004;
    public final static int TRANSFER_ORDER_CANCEL_FAIL_CODE = 2250005;
    public final static int TRANSFER_ORDER_BATCH_STOCK_NOT_ENOUGH_CODE = 2250006;
    public final static int TRANSFER_ORDER_SALE_STOCK_NOT_ENOUGH_CODE = 2250007;
    public final static int TRANSFER_ORDER_PARAMS_ILLEGAL_CODE = 2250008;
    public final static int TRANSFER_ORDER_NOT_EXIST_CODE = 2250009;
    public final static int TRANSFER_ORDER_STATE_NOT_WAIT_AUDIT_CODE = 2250010;
    public final static int TRANSFER_ORDER_STATE_NOT_INIT_CODE = 2250011;
    public final static int TRANSFER_WAREHOUSE_NOT_EXIST_CODE = 2250012;
    public final static int TRANSFER_ORDER_STOCKOUT_ORDER_SYN_CODE = 2250013;
    public final static int TRANSFER_OUT_ORDER_EXIST_CODE = 2250014;
    public final static int TRANSFER_IN_ORDER_EXIST_CODE = 2250015;
    public final static int TRANSFER_ORDER_CALL_GOODS_SYSTEM_REDUCE_CHANNEL_COUNT_CODE = 2250016;
    
    public final static LogisticsReturnCode TRANSFER_ORDER_INNER_EXCEPTION = new LogisticsReturnCode("调拨单系统内部异常",
            TRANSFER_ORDER_INNER_EXCEPTION_CODE);
    public final static LogisticsReturnCode TRANSFER_ORDER_SKU_FREEZE_FAILURE_ERROR = new LogisticsReturnCode(
            "调拨明细冻结失败", TRANSFER_ORDER_SKU_FREEZE_FAILURE_CODE);
    public final static LogisticsReturnCode TRANSFER_ORDER_NOT_FOUND_ERROR = new LogisticsReturnCode(
            "未找到调拨单记录", TRANSFER_ORDER_NOT_FOUND_CODE);
    public final static LogisticsReturnCode TRANSFER_ORDER_SKU_NOT_FOUND_ERROR = new LogisticsReturnCode(
            "未找到调拨单明细记录", TRANSFER_ORDER_SKU_NOT_FOUND_CODE);
    public final static LogisticsReturnCode TRANSFER_ORDER_CONFIRM_FAIL_CODE_ERROR = new LogisticsReturnCode(
            "下发调拨单确认命令失败", TRANSFER_ORDER_CONFIRM_FAIL_CODE);
    public final static LogisticsReturnCode TRANSFER_ORDER_CANCEL_FAIL_CODE_ERROR = new LogisticsReturnCode(
            "调拨单取消失败", TRANSFER_ORDER_CANCEL_FAIL_CODE);
    public final static LogisticsReturnCode TRANSFER_ORDER_BATCH_STOCK_NOT_ENOUGH_CODE_ERROR = new LogisticsReturnCode(
            "批次实物库存小于申请调拨数量", TRANSFER_ORDER_BATCH_STOCK_NOT_ENOUGH_CODE);
    public final static LogisticsReturnCode TRANSFER_ORDER_SALE_STOCK_NOT_ENOUGH_CODE_ERROR = new LogisticsReturnCode(
            "可售库存小于申请调拨数量", TRANSFER_ORDER_SALE_STOCK_NOT_ENOUGH_CODE);
    public final static LogisticsReturnCode TRANSFER_ORDER_PARAMS_ILLEGAL = new LogisticsReturnCode("调拨单参数不合法", TRANSFER_ORDER_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode TRANSFER_ORDER_NOT_EXIST = new LogisticsReturnCode("调拨单不存在", TRANSFER_ORDER_NOT_EXIST_CODE);
    public final static LogisticsReturnCode TRANSFER_ORDER_STATE_NOT_WAIT_AUDIT = new LogisticsReturnCode("只有待审批状态的调拨单才能审批", TRANSFER_ORDER_STATE_NOT_WAIT_AUDIT_CODE);
    public final static LogisticsReturnCode TRANSFER_ORDER_STATE_NOT_INIT = new LogisticsReturnCode("只有初始化状态的调拨单才能提交审批", TRANSFER_ORDER_STATE_NOT_INIT_CODE);
    public final static LogisticsReturnCode TRANSFER_WAREHOUSE_NOT_EXIST = new LogisticsReturnCode("调拨单的调出仓库不存在", TRANSFER_WAREHOUSE_NOT_EXIST_CODE);
    public final static LogisticsReturnCode TRANSFER_ORDER_STOCKOUT_ORDER_SYN_ERROR = new LogisticsReturnCode(
            "同步出库单给仓库异常", TRANSFER_ORDER_STOCKOUT_ORDER_SYN_CODE);
    public final static LogisticsReturnCode TRANSFER_OUT_ORDER_EXIST = new LogisticsReturnCode(
            "调拨出库单已存在", TRANSFER_OUT_ORDER_EXIST_CODE);
    public final static LogisticsReturnCode TRANSFER_IN_ORDER_EXIST = new LogisticsReturnCode(
            "调拨入库单已存在", TRANSFER_IN_ORDER_EXIST_CODE);
    public final static LogisticsReturnCode TRANSFER_ORDER_CALL_GOODS_SYSTEM_REDUCE_CHANNEL_COUNT_ERROR = new LogisticsReturnCode(
            "调拨单调用商品系统接口扣减渠道库存失败", TRANSFER_ORDER_CALL_GOODS_SYSTEM_REDUCE_CHANNEL_COUNT_CODE);

    /**
     * PurchaseReturnService ErrorCode
     * 采购退货单 错误码
     * <p/>
     * 错误范围： 2270000 -- 2270100
     */
    public final static int PURCHASE_RETURN_INNER_EXCEPTION_CODE = 2270000;
    public final static LogisticsReturnCode PURCHASE_RETURN_INNER_EXCEPTION = new LogisticsReturnCode(
            "采购退货单系统内部异常", PURCHASE_RETURN_INNER_EXCEPTION_CODE);
    public final static int PURCHASE_RETURN_NOT_EXIST_CODE = 2270001;
    public final static LogisticsReturnCode PURCHASE_RETURN_NOT_EXIST = new LogisticsReturnCode(
            "采退单不存在", PURCHASE_RETURN_NOT_EXIST_CODE);
    public final static int PURCHASE_RETURN_NOT_ALLOW_DELETE_CODE = 2270002;
    public final static LogisticsReturnCode PURCHASE_RETURN_NOT_ALLOW_DELETE = new LogisticsReturnCode(
            "采退单不允许删除", PURCHASE_RETURN_NOT_ALLOW_DELETE_CODE);
    public final static int PURCHASE_RETURN_STATE_NOT_INIT_CODE = 2270003;
    public final static LogisticsReturnCode PURCHASE_RETURN_STATE_NOT_INIT = new LogisticsReturnCode(
            "只有初始化或是冻结失败的采退单才能提交审批", PURCHASE_RETURN_STATE_NOT_INIT_CODE);
    public final static int PURCHASE_RETURN_PARAMS_ILLEGAL_CODE = 2270004;
    public final static LogisticsReturnCode PURCHASE_RETURN_PARAMS_ILLEGAL = new LogisticsReturnCode(
            "采退单参数不合法", PURCHASE_RETURN_PARAMS_ILLEGAL_CODE);
    public final static int PURCHASE_RETURN_SKU_FREEZE_FAILURE_CODE = 2270011;
    public final static LogisticsReturnCode PURCHASE_RETURN_SKU_FREEZE_FAILURE_ERROR = new LogisticsReturnCode(
            "采退明细冻结失败", PURCHASE_RETURN_SKU_FREEZE_FAILURE_CODE);
    public final static int PURCHASE_RETURN_BATCH_STOCK_NOT_ENOUGH_CODE = 2270005;
    public final static LogisticsReturnCode PURCHASE_RETURN_BATCH_STOCK_NOT_ENOUGH_CODE_ERROR = new LogisticsReturnCode(
            "批次正品库存小于申请采退数量", PURCHASE_RETURN_BATCH_STOCK_NOT_ENOUGH_CODE);
    public final static int PURCHASE_RETURN_SALE_STOCK_NOT_ENOUGH_CODE = 2270006;
    public final static LogisticsReturnCode PURCHASE_RETURN_SALE_STOCK_NOT_ENOUGH_CODE_ERROR = new LogisticsReturnCode(
            "可售库存小于申请采退数量", PURCHASE_RETURN_SALE_STOCK_NOT_ENOUGH_CODE);
    public final static int PURCHASE_RETURN_STATE_NOT_WAIT_AUDIT_CODE = 2270007;
    public final static LogisticsReturnCode PURCHASE_RETURN_STATE_NOT_WAIT_AUDIT = new LogisticsReturnCode(
            "只有待审批状态的采退单才能审批", PURCHASE_RETURN_STATE_NOT_WAIT_AUDIT_CODE);
    public final static int PURCHASE_RETURN_OUT_ORDER_EXIST_CODE = 2270008;
    public final static LogisticsReturnCode PURCHASE_RETURN_OUT_ORDER_EXIST = new LogisticsReturnCode(
            "采退出库单已存在，不能成功创建", PURCHASE_RETURN_OUT_ORDER_EXIST_CODE);
    public final static int PURCHASE_RETURN_STOCK_OUT_EXCEPTION_CODE = 2270009;
    public final static LogisticsReturnCode PURCHASE_RETURN_STOCK_OUT_EXCEPTION = new LogisticsReturnCode(
            "不能成功创建采退出库单，采退信息赋值异常", PURCHASE_RETURN_STOCK_OUT_EXCEPTION_CODE);
    public final static int PURCHASE_RETURN_SKU_NOT_FOUND_CODE = 2270010;
    public final static LogisticsReturnCode PURCHASE_RETURN_SKU_NOT_FOUND_ERROR = new LogisticsReturnCode(
            "未找到采退单明细记录", PURCHASE_RETURN_SKU_NOT_FOUND_CODE);
    public final static int PURCHASE_RETURN_LINE_NOT_EXIST_CODE = 2270012;
    public final static LogisticsReturnCode PURCHASE_RETURN_LINE_NOT_EXIST = new LogisticsReturnCode(
            "退货仓库未找到路线信息", PURCHASE_RETURN_LINE_NOT_EXIST_CODE);
    public final static int PURCHASE_RETURN_BATCH_BAD_STOCK_NOT_ENOUGH_CODE = 2270013;
    public final static LogisticsReturnCode PURCHASE_RETURN_BATCH_BAD_STOCK_NOT_ENOUGH_CODE_ERROR = new LogisticsReturnCode(
            "批次坏品库存小于申请采退数量", PURCHASE_RETURN_BATCH_BAD_STOCK_NOT_ENOUGH_CODE);
    public final static int PURCHASE_RETURN_ACTUAL_BAD_STOCK_NOT_ENOUGH_CODE = 2270014;
    public final static LogisticsReturnCode PURCHASE_RETURN_ACTUAL_BAD_STOCK_NOT_ENOUGH_CODE_ERROR = new LogisticsReturnCode(
            "实物批次坏品库存小于申请采退数量", PURCHASE_RETURN_ACTUAL_BAD_STOCK_NOT_ENOUGH_CODE);

    /**
     * 批发销售单 错误码
     * <p/>
     * 错误范围： 2270301 -- 2270400
     */
    public final static int SALES_SLIP_INNER_EXCEPTION_CODE = 2270000;
    public final static LogisticsReturnCode SALES_SLIP_INNER_EXCEPTION = new LogisticsReturnCode(
            "批发销售单系统内部异常", SALES_SLIP_INNER_EXCEPTION_CODE);
    public final static int SALES_SLIP_NOT_EXIST_CODE = 2270001;
    public final static LogisticsReturnCode SALES_SLIP_NOT_EXIST = new LogisticsReturnCode(
            "批发销售单不存在", SALES_SLIP_NOT_EXIST_CODE);
    public final static int SALES_SLIP_NOT_ALLOW_DELETE_CODE = 2270002;
    public final static LogisticsReturnCode SALES_SLIP_NOT_ALLOW_DELETE = new LogisticsReturnCode(
            "批发销售单不允许删除", SALES_SLIP_NOT_ALLOW_DELETE_CODE);
    public final static int SALES_SLIP_STATE_NOT_INIT_CODE = 2270003;
    public final static LogisticsReturnCode SALES_SLIP_STATE_NOT_INIT = new LogisticsReturnCode(
            "只有初始化或是冻结失败的批发销售单才能提交审批", SALES_SLIP_STATE_NOT_INIT_CODE);
    public final static int SALES_SLIP_PARAMS_ILLEGAL_CODE = 2270004;
    public final static LogisticsReturnCode SALES_SLIP_PARAMS_ILLEGAL = new LogisticsReturnCode(
            "批发销售单参数不合法", SALES_SLIP_PARAMS_ILLEGAL_CODE);
    public final static int SALES_SLIP_STATE_EDIT_NOT_SUPPOT_CODE = 2270005;
    public final static LogisticsReturnCode SALES_SLIP_STATE_EDIT_NOT_SUPPOT = new LogisticsReturnCode(
            "只有初始化或是冻结失败的批发销售单才能编辑", SALES_SLIP_STATE_EDIT_NOT_SUPPOT_CODE);
    public final static int SALES_SLIP_SKU_FREEZE_FAILURE_CODE = 2270011;
    public final static LogisticsReturnCode SALES_SLIP_SKU_FREEZE_FAILURE_ERROR = new LogisticsReturnCode(
            "批发销售单明细冻结失败", SALES_SLIP_SKU_FREEZE_FAILURE_CODE);
    public final static int SALES_SLIP_BATCH_STOCK_NOT_ENOUGH_CODE = 2270015;
    public final static LogisticsReturnCode SALES_SLIP_BATCH_STOCK_NOT_ENOUGH_CODE_ERROR = new LogisticsReturnCode(
            "批次正品库存小于申请批发销售数量", SALES_SLIP_BATCH_STOCK_NOT_ENOUGH_CODE);
    public final static int SALES_SLIP_SALE_STOCK_NOT_ENOUGH_CODE = 2270006;
    public final static LogisticsReturnCode SALES_SLIP_SALE_STOCK_NOT_ENOUGH_CODE_ERROR = new LogisticsReturnCode(
            "可售库存小于申请批发销售数量", SALES_SLIP_SALE_STOCK_NOT_ENOUGH_CODE);
    public final static int SALES_SLIP_STATE_NOT_WAIT_AUDIT_CODE = 2270007;
    public final static LogisticsReturnCode SALES_SLIP_STATE_NOT_WAIT_AUDIT = new LogisticsReturnCode(
            "只有待审批状态的批发销售单才能审批", SALES_SLIP_STATE_NOT_WAIT_AUDIT_CODE);
    public final static int SALES_SLIP_OUT_ORDER_EXIST_CODE = 2270008;
    public final static LogisticsReturnCode SALES_SLIP_OUT_ORDER_EXIST = new LogisticsReturnCode(
            "批发销售出库单已存在，不能成功创建", SALES_SLIP_OUT_ORDER_EXIST_CODE);
    public final static int SALES_SLIP_STOCK_OUT_EXCEPTION_CODE = 2270009;
    public final static LogisticsReturnCode SALES_SLIP_STOCK_OUT_EXCEPTION = new LogisticsReturnCode(
            "不能成功创建批发销售出库单，批发销售信息赋值异常", SALES_SLIP_STOCK_OUT_EXCEPTION_CODE);
    public final static int SALES_SLIP_SKU_NOT_FOUND_CODE = 2270010;
    public final static LogisticsReturnCode SALES_SLIP_SKU_NOT_FOUND_ERROR = new LogisticsReturnCode(
            "未找到批发销售单明细记录", SALES_SLIP_SKU_NOT_FOUND_CODE);
    public final static int SALES_SLIP_LINE_NOT_EXIST_CODE = 2270012;
    public final static LogisticsReturnCode SALES_SLIPN_LINE_NOT_EXIST = new LogisticsReturnCode(
            "退货仓库未找到路线信息", SALES_SLIP_LINE_NOT_EXIST_CODE);
    public final static int SALES_SLIP_BATCH_BAD_STOCK_NOT_ENOUGH_CODE = 2270013;
    public final static LogisticsReturnCode SALES_SLIP_BATCH_BAD_STOCK_NOT_ENOUGH_CODE_ERROR = new LogisticsReturnCode(
            "批次坏品库存小于申请批发销售数量", SALES_SLIP_BATCH_BAD_STOCK_NOT_ENOUGH_CODE);
    public final static int SALES_SLIP_ACTUAL_BAD_STOCK_NOT_ENOUGH_CODE = 2270014;
    public final static LogisticsReturnCode SALES_SLIP_ACTUAL_BAD_STOCK_NOT_ENOUGH_CODE_ERROR = new LogisticsReturnCode(
            "实物批次坏品库存小于申请批发销售数量", SALES_SLIP_ACTUAL_BAD_STOCK_NOT_ENOUGH_CODE);
    public final static int SALES_SLIP_CUSTOMER_NOT_EXIST_CODE = 2270016;
    public final static LogisticsReturnCode SALES_SLIP_CUSTOMER_NOT_EXIST = new LogisticsReturnCode(
            "批发销售单客户不存在", SALES_SLIP_CUSTOMER_NOT_EXIST_CODE);


    /**
     * PresellOrderService ErrorCode
     * 预售申请单 错误码
     * <p/>
     * 错误范围： 2270101 -- 2270200
     */
    public final static int PRESELL_ORDER_INNER_EXCEPTION_CODE = 2270101;
    public final static LogisticsReturnCode PRESELL_ORDER_INNER_EXCEPTION = new LogisticsReturnCode(
            "预售申请单系统内部异常", PRESELL_ORDER_INNER_EXCEPTION_CODE);
    public final static int PRESELL_ORDER_NOT_EXIST_CODE = 2270102;
    public final static LogisticsReturnCode PRESELL_ORDER_NOT_EXIST = new LogisticsReturnCode(
            "预售申请单不存在", PRESELL_ORDER_NOT_EXIST_CODE);
    public final static int PRESELL_ORDER_STATE_NOT_INIT_CODE = 2270103;
    public final static LogisticsReturnCode PRESELL_ORDER_STATE_NOT_INIT = new LogisticsReturnCode(
            "只有待审批的申请单才能提交审批", PRESELL_ORDER_STATE_NOT_INIT_CODE);
    public final static int PRESELL_ORDER_NOPO_CODE = 2270104;
    public final static LogisticsReturnCode PRESELL_ORDER_NOPO = new LogisticsReturnCode(
            "只有申请无PO预售的申请单才能提交审批", PRESELL_ORDER_NOPO_CODE);
    public final static int PRESELL_ORDER_MIN_CODE = 2270105;
    public final static LogisticsReturnCode PRESELL_ORDER_MIN_CODE_ERROR = new LogisticsReturnCode(
            "可售库存表的可售库存小于申请预支可售数量", PRESELL_ORDER_MIN_CODE);

    /**
     * ProductDeclareService ErrorCode
     * 商品备案 错误码
     * <p/>
     * 错误范围： 2260000 -- 2270000
     */

    public final static int PRODUCT_DECLARE_INNER_EXCEPTION_CODE = 2260000;
    public final static int PRODUCT_DECLARE_NOT_FOUND_CODE = 2260001;
    public final static int PRODUCT_DECLARE_PARAMS_ILLEGAL_CODE = 2260002;
    public final static int PRODUCT_DECLARE_GZ_BONDED_NOT_FOUNT_EXCEPTION_CODE = 2260003;
    public final static int PRODUCT_DECLARE_GZ_DIREDCTMAIL_NOT_FOUNT_EXCEPTION_CODE = 2260004;
    public final static int PRODUCT_DECLARE_HZ_BONDED_NOT_FOUNT_EXCEPTION_CODE = 2260005;
    public final static int PRODUCT_DECLARE_HZ_DIREDCTMAIL_NOT_FOUNT_EXCEPTION_CODE = 2260006;
    public final static int PRODUCT_DECLARE_NB_BONDED_NOT_FOUNT_EXCEPTION_CODE = 2260007;
    public final static int PRODUCT_DECLARE_FINISHED_COLLECTING_STATE_CODE = 2260008;
    public final static int PRODUCT_DECLARE_STATE_NOT_FINISHED_COLLECTING_OR_DECLARE_PASS_CODE = 2260009;
    public final static int PRODUCT_DECLARE_INIT_EXCEPTION_CODE = 2260010;
    public final static int PRODUCT_DECLARE_DECLARING_STATE_ERROR_CODE = 2260011;
    public final static LogisticsReturnCode PRODUCT_DECLARE_INNER_EXCEPTION = new LogisticsReturnCode("商品备案系统内部异常",
            PRODUCT_DECLARE_INNER_EXCEPTION_CODE);
    public final static LogisticsReturnCode PRODUCT_DECLARE_NOT_FOUND_ERROR = new LogisticsReturnCode(
            "未找到商品备案记录", PRODUCT_DECLARE_NOT_FOUND_CODE);
    public final static LogisticsReturnCode PRODUCT_DECLARE_PARAMS_ILLEGAL = new LogisticsReturnCode("商品备案参数不合法",
            PRODUCT_DECLARE_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode PRODUCT_DECLARE_GZ_BONDED_NOT_FOUNT_EXCEPTION = new LogisticsReturnCode(
            "广州保税商品备案信息不存在",
            PRODUCT_DECLARE_GZ_BONDED_NOT_FOUNT_EXCEPTION_CODE);
    public final static LogisticsReturnCode PRODUCT_DECLARE_GZ_DIREDCTMAIL_NOT_FOUNT_EXCEPTION = new LogisticsReturnCode(
            "广州直邮商品备案信息不存在",
            PRODUCT_DECLARE_GZ_DIREDCTMAIL_NOT_FOUNT_EXCEPTION_CODE);
    public final static LogisticsReturnCode PRODUCT_DECLARE_HZ_BONDED_NOT_FOUNT_EXCEPTION = new LogisticsReturnCode(
            "杭州保税商品备案信息不存在",
            PRODUCT_DECLARE_HZ_BONDED_NOT_FOUNT_EXCEPTION_CODE);
    public final static LogisticsReturnCode PRODUCT_DECLARE_HZ_DIREDCTMAIL_NOT_FOUNT_EXCEPTION = new LogisticsReturnCode(
            "杭州直邮商品备案信息不存在",
            PRODUCT_DECLARE_HZ_DIREDCTMAIL_NOT_FOUNT_EXCEPTION_CODE);
    public final static LogisticsReturnCode PRODUCT_DECLARE_NB_BONDED_NOT_FOUNT_EXCEPTION = new LogisticsReturnCode(
            "宁波保税商品备案信息不存在",
            PRODUCT_DECLARE_NB_BONDED_NOT_FOUNT_EXCEPTION_CODE);
    public final static LogisticsReturnCode PRODUCT_DECLARE_FINISHED_COLLECTING_STATE_ERROR = new LogisticsReturnCode(
            "待备案状态无法操作资料收集完毕导入",
            PRODUCT_DECLARE_FINISHED_COLLECTING_STATE_CODE);
    public final static LogisticsReturnCode PRODUCT_DECLARE_STATE_NOT_FINISHED_COLLECTING_OR_DECLARE_PASS = new LogisticsReturnCode(
            "商品的备案状态不为资料收集完毕或备案通过,无法操作备案通过导入",
            PRODUCT_DECLARE_STATE_NOT_FINISHED_COLLECTING_OR_DECLARE_PASS_CODE);
    public final static LogisticsReturnCode PRODUCT_DECLARE_INIT_EXCEPTION = new LogisticsReturnCode(
            "初始化商品备案信息异常", PRODUCT_DECLARE_INIT_EXCEPTION_CODE);
    public final static LogisticsReturnCode PRODUCT_DECLARE_DECLARING_STATE_ERROR = new LogisticsReturnCode(
            "备案状态不为资料收集完毕, 无法操作备案中导入", PRODUCT_DECLARE_DECLARING_STATE_ERROR_CODE);


    private static final long serialVersionUID = 1832495036518929832L;


    public static final int _C_SUPPLIER_CHANNEL_DISTRIBUTION_PARAMS_ILLEGAL = 2280000;
    public static final int _C_SUPPLIER_CHANNEL_BATCH_STOCK_NOT_FOUND = 2280001;
    public final static LogisticsReturnCode SUPPLIER_CHANNEL_DISTRIBUTION_PARAMS_ILLEGAL = new LogisticsReturnCode("参数不合法", _C_SUPPLIER_CHANNEL_DISTRIBUTION_PARAMS_ILLEGAL);
    public final static LogisticsReturnCode SUPPLIER_CHANNEL_BATCH_STOCK_NOT_FOUND = new LogisticsReturnCode("找不到批次库存记录", _C_SUPPLIER_CHANNEL_BATCH_STOCK_NOT_FOUND);


    /**
     * CustomsLimitServiceImpl ErrorCode
     * <p/> 延迟发货
     * 错误范围 2280000 -- 2290000
     */
    public final static int CUSTOMSLIMIT_SERVICE_PARAMS_ILLEGAL_CODE = 2280000;
    public final static int CUSTOMSLIMIT_SERVICE_INNER_EXCEPTION_CODE = 2280001;
    public final static LogisticsReturnCode CUSTOMSLIMIT_SERVICE_PARAMS_ILLEGAL = new LogisticsReturnCode("方法参数为空或不合法", CUSTOMSLIMIT_SERVICE_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode CUSTOMSLIMIT_SERVICE_INNER_EXCEPTION = new LogisticsReturnCode("延迟发货服务系统内部异常", CUSTOMSLIMIT_SERVICE_INNER_EXCEPTION_CODE);


    /**
     * CustomsClearanceServiceErrorCode
     * 清关企业服务 错误码
     * <p/>
     * 错误范围 2290000 -- 2300000
     */
    public final static int CUSTOMS_CLEARANCE_SERVICE_PARAMS_ILLEGAL_CODE = 2290000;
    public final static int CUSTOMS_CLEARANCE_SERVICE_RECEIVE_ROUTE_ERROR_CODE = 2290001;
    public final static int CUSTOMS_CLEARANCE_SERVICE_ORDER_NOT_EXIST_ERROR_CODE = 2290002;
    public final static int CUSTOMS_CLEARANCE_SERVICE_ORDER_NOT_MERCHANT_ERROR_CODE = 2290003;
    public final static int CUSTOMS_CLEARANCE_SERVICE_DECLARATION_NULL_ERROR_CODE = 2290004;
    public final static int CUSTOMS_CLEARANCE_SERVICE_LINE_NULL_ERROR_CODE = 2290005;
    public final static int CUSTOMS_CLEARANCE_SERVICE_LINE_NOT_NEED_CARD_ERROR_CODE = 2290006;
    public final static int CUSTOMS_CLEARANCE_SERVICE_CARD_INFO_INCOMPLETE_ERROR_CODE = 2290007;
    public final static int CUSTOMS_CLEARANCE_SERVICE_LOGISTICS_NULL_ERROR_CODE = 2290008;
    public final static int CUSTOMS_CLEARANCE_SERVICE_LOGISTICSNID_ERROR_CODE = 2290009;
    public final static LogisticsReturnCode CUSTOMS_CLEARANCE_SERVICE_PARAMS_ILLEGAL = new LogisticsReturnCode("参数不合法", CUSTOMS_CLEARANCE_SERVICE_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode CUSTOMS_CLEARANCE_SERVICE_RECEIVE_ROUTE_ERROR = new LogisticsReturnCode("接收清关路由异常", CUSTOMS_CLEARANCE_SERVICE_RECEIVE_ROUTE_ERROR_CODE);
    public final static LogisticsReturnCode CUSTOMS_CLEARANCE_SERVICE_ORDER_NOT_EXIST_ERROR = new LogisticsReturnCode("调用清关接口异常，出库单不存在", CUSTOMS_CLEARANCE_SERVICE_ORDER_NOT_EXIST_ERROR_CODE);
    public final static LogisticsReturnCode CUSTOMS_CLEARANCE_SERVICE_DECLARATION_NULL_ERROR = new LogisticsReturnCode("调用清关接口异常，出库单没有申报信息", CUSTOMS_CLEARANCE_SERVICE_DECLARATION_NULL_ERROR_CODE);
    public final static LogisticsReturnCode CUSTOMS_CLEARANCE_SERVICE_ORDER_NOT_MERCHANT_ERROR = new LogisticsReturnCode("调用清关接口异常，出库单不属于该商户", CUSTOMS_CLEARANCE_SERVICE_ORDER_NOT_MERCHANT_ERROR_CODE);
    public final static LogisticsReturnCode CUSTOMS_CLEARANCE_SERVICE_LINE_NULL_ERROR = new LogisticsReturnCode("调用清关接口异常，出库单没有找到对应线路", CUSTOMS_CLEARANCE_SERVICE_LINE_NULL_ERROR_CODE);
    public final static LogisticsReturnCode CUSTOMS_CLEARANCE_SERVICE_LINE_NOT_NEED_CARD_ERROR = new LogisticsReturnCode("调用清关接口异常，出库单对应线路不需要身份证信息", CUSTOMS_CLEARANCE_SERVICE_LINE_NOT_NEED_CARD_ERROR_CODE);
    public final static LogisticsReturnCode CUSTOMS_CLEARANCE_SERVICE_CARD_INFO_INCOMPLETE_ERROR = new LogisticsReturnCode("调用清关接口异常，未获取到身份信息", CUSTOMS_CLEARANCE_SERVICE_CARD_INFO_INCOMPLETE_ERROR_CODE);
    public final static LogisticsReturnCode CUSTOMS_CLEARANCE_SERVICE_LOGISTICS_NULL_ERROR = new LogisticsReturnCode("调用清关接口异常，该订单线路未配置清关信息", CUSTOMS_CLEARANCE_SERVICE_LOGISTICS_NULL_ERROR_CODE);
    public final static LogisticsReturnCode CUSTOMS_CLEARANCE_SERVICE_LOGISTICSNID_ERROR = new LogisticsReturnCode("调用清关接口异常，清关公司编码错误", CUSTOMS_CLEARANCE_SERVICE_LOGISTICSNID_ERROR_CODE);

    /**
     * LogisticsOpenApi ErrorCode
     * 物流开放API 错误码
     * <p/>
     * 错误范围： 2300000 -- 2400000
     */
    public final static int OPENAPI_SERVICE_STOCKOUT_ORDER_NOT_EXIST_CODE = 2300001;
    public final static int OPENAPI_SERVICE_PARAM_ILLEGAL_CODE = 2300002;
    public final static int OPENAPI_SERVICE_STATE_CHANGE_ERROR_CODE = 2300003;
    public final static int OPENAPI_SERVICE_MAIL_NO_NOT_ALL_EMPTY_CODE = 2300004;
    public final static int UPDATE_ORDER_PACKAGE_MAIL_NO_NOT_FAIL_CODE = 2300005;

    public final static LogisticsReturnCode OPENAPI_SERVICE_STOCKOUT_ORDER_NOT_EXIST = new LogisticsReturnCode("出库单不存在", OPENAPI_SERVICE_STOCKOUT_ORDER_NOT_EXIST_CODE);
    public final static LogisticsReturnCode OPENAPI_SERVICE_PARAM_ILLEGAL = new LogisticsReturnCode("参数不合法", OPENAPI_SERVICE_PARAM_ILLEGAL_CODE);
    public final static LogisticsReturnCode OPENAPI_SERVICE_STATE_CHANGE_ERROR = new LogisticsReturnCode("订单状态流转异常", OPENAPI_SERVICE_STATE_CHANGE_ERROR_CODE);
    public final static LogisticsReturnCode OPENAPI_SERVICE_MAIL_NO_NOT_ALL_EMPTY = new LogisticsReturnCode("订单运单信息不能为空", OPENAPI_SERVICE_MAIL_NO_NOT_ALL_EMPTY_CODE);
    public final static LogisticsReturnCode UPDATE_ORDER_PACKAGE_MAIL_NO_NOT_FAIL = new LogisticsReturnCode("调用订单系统接口更新包裹运单号失败", UPDATE_ORDER_PACKAGE_MAIL_NO_NOT_FAIL_CODE);

    public LogisticsReturnCode(String desc, int code) {
        super(desc, code);
    }

    /**
     * 供应商商品价格调整服务 错误码
     * <p/>
     * 错误范围： 2400001 -- 2410000
     */
    public static final int _C_PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_NOT_FOUND_ERROR = 2400001;
    public static final AbstractReturnCode PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_NOT_FOUND_ERROR = new LogisticsReturnCode("未找到价格调整申请单记录", _C_PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_NOT_FOUND_ERROR);

    public static final int _C_PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_START_GREATER_THAN_END_ERROR = 2400002;
    public static final AbstractReturnCode PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_START_GREATER_THAN_END_ERROR = new LogisticsReturnCode("活动价的生效开始时间必须小于生效结束时间", _C_PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_START_GREATER_THAN_END_ERROR);

    public static final int _C_PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_EXIST_WAIT_APPROVE_ERROR = 2400003;
    public static final AbstractReturnCode PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_EXIST_WAIT_APPROVE_ERROR = new LogisticsReturnCode("存在待审批状态的价格调整申请单记录, 需要先完成该申请单的审批", _C_PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_EXIST_WAIT_APPROVE_ERROR);

    public static final int _C_PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_EXIST_TIME_PERIOD_OVERLAP_ERROR = 2400004;
    public static final AbstractReturnCode PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_EXIST_TIME_PERIOD_OVERLAP_ERROR = new LogisticsReturnCode("存在生效时间段重复的价格调整申请单记录, 请修改生效起止时间", _C_PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_EXIST_TIME_PERIOD_OVERLAP_ERROR);

    public static final int _C_PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_STATE_NOT_WAIT_APPROVE_ERROR = 2400005;
    public static final AbstractReturnCode PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_STATE_NOT_WAIT_APPROVE_ERROR = new LogisticsReturnCode("价格调整申请单的状态不为待审批, 无法操作审批", _C_PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_STATE_NOT_WAIT_APPROVE_ERROR);

    public static final int _C_PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_DATA_DUPLICATE_ERROR = 2400006;
    public static final AbstractReturnCode PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_DATA_DUPLICATE_ERROR = new LogisticsReturnCode("已存在相同的活动价格调整申请记录, 无法操作新增", _C_PROVIDER_SKU_ACTIVITY_PRICE_ADJUST_DATA_DUPLICATE_ERROR);

    public static final int _C_PROVIDER_SKU_SUPPLY_PRICE_ADJUST_DATA_DUPLICATE_ERROR = 2400007;
    public static final AbstractReturnCode PROVIDER_SKU_SUPPLY_PRICE_ADJUST_DATA_DUPLICATE_ERROR = new LogisticsReturnCode("已存在待审核的供货价格调整申请记录, 无法操作新增", _C_PROVIDER_SKU_SUPPLY_PRICE_ADJUST_DATA_DUPLICATE_ERROR);

    public static final int _C_PROVIDER_SKU_SUPPLY_PRICE_ADJUST_NOT_FOUND_ERROR = 2400008;
    public static final AbstractReturnCode PROVIDER_SKU_SUPPLY_PRICE_ADJUST_NOT_FOUND_ERROR = new LogisticsReturnCode("未找到价格调整申请单记录", _C_PROVIDER_SKU_SUPPLY_PRICE_ADJUST_NOT_FOUND_ERROR);


    /**
     * 出库单下发异常
     * 错误范围： 2410001 -- 2420000
     */
    public static final int _C_LINE_IS_CLOSED = 2410001;
    public static final AbstractReturnCode LINE_IS_CLOSED = new LogisticsReturnCode("路线被关闭", _C_LINE_IS_CLOSED);

    public static final int _C_DATA_PREPARE_ERROR = 2410002;
    public static final AbstractReturnCode DATA_PREPARE_ERROR = new LogisticsReturnCode("出库单下发数据准备异常", _C_DATA_PREPARE_ERROR);

    public static final int _C_STOCKOUT_DATA_IS_NULL = 2410003;
    public static final AbstractReturnCode STOCKOUT_DATA_IS_NULL = new LogisticsReturnCode("出库单相关参数实体为空", _C_STOCKOUT_DATA_IS_NULL);

    public static final int _C_ORDER_FEE_SPLIT_ERROR = 2410004;
    public static final AbstractReturnCode ORDER_FEE_SPLIT_ERROR = new LogisticsReturnCode("税费运费拆分逻辑异常", _C_ORDER_FEE_SPLIT_ERROR);

    public static final int _C_CCB_ORDER_CREATE_ERROR = 2410005;
    public static final AbstractReturnCode CCB_ORDER_CREATE_ERROR = new LogisticsReturnCode("出库单下发清关公司异常", _C_CCB_ORDER_CREATE_ERROR);

    public static final int _C_CONFIRM_REAL_NAME_ERROR = 2410006;
    public static final AbstractReturnCode CONFIRM_REAL_NAME_ERROR = new LogisticsReturnCode("收货人的实名认证失败", _C_CONFIRM_REAL_NAME_ERROR);

    public static final int _C_SKU_FOREIGN_NAME_NOT_NULL = 2410007;
    public static final AbstractReturnCode SKU_FOREIGN_NAME_NOT_NULL = new LogisticsReturnCode("商品外文名称不能为空", _C_SKU_FOREIGN_NAME_NOT_NULL);

    public static final int _C_WH_BATCH_STOCK_NOT_ENOUGH = 2410008;
    public static final AbstractReturnCode WH_BATCH_STOCK_NOT_ENOUGH = new LogisticsReturnCode("仓库返回商品批次库存数量不够", _C_WH_BATCH_STOCK_NOT_ENOUGH);

    public static final int _C_DATA_PREPARE_CALL_USER_SERVICE_FAIL = 2410009;
    public static final AbstractReturnCode DATA_PREPARE_CALL_USER_SERVICE_FAIL = new LogisticsReturnCode("出库单下发数据准备调用用户系统服务异常", _C_DATA_PREPARE_CALL_USER_SERVICE_FAIL);

    public static final int _C_ID_CARD_PHOTO_NOT_UPLOADED = 2410010;
    public static final AbstractReturnCode ID_CARD_PHOTO_NOT_UPLOADED = new LogisticsReturnCode("出库单下发数据准备收货人身份证未上传异常", _C_ID_CARD_PHOTO_NOT_UPLOADED);

    public static final int _C_ID_CARD_PHOTO_NOT_AUDITED = 2410011;
    public static final AbstractReturnCode ID_CARD_PHOTO_NOT_AUDITED = new LogisticsReturnCode("出库单下发数据准备收货人身份证未审核异常", _C_ID_CARD_PHOTO_NOT_AUDITED);

    public static final int _C_ID_CARD_PHOTO_AUDIT_NOT_PASS = 2410012;
    public static final AbstractReturnCode ID_CARD_PHOTO_AUDIT_NOT_PASS = new LogisticsReturnCode("出库单下发数据准备收货人身份证审核未通过异常", _C_ID_CARD_PHOTO_AUDIT_NOT_PASS);

    /**
     * 商品属性操作相关 错误码
     * <p/>
     * 错误范围： 2420001 -- 2430000
     */
    public final static int PRODUCT_ATTRIBUTE_OPT_PARAM_ILLEGAL_CODE = 2420001;

    public final static int PRODUCT_ATTRIBUTE_TEMPALATE_CREATE_ERROR_CODE = 2420010;
    public final static int PRODUCT_ATTRIBUTE_TEMPALATE_NOT_EXIST_CODE = 2420010;
    public final static int PRODUCT_ATTRIBUTE_TEMPALATE_QUERY_ERROR_CODE = 2420011;
    public final static int PRODUCT_ATTRIBUTE_TEMPALATE_ALEARY_EXIST_CODE = 2420012;
    public final static int PRODUCT_ATTRIBUTE_KEY_QUERY_ERROR_CODE = 2420013;
    public final static int PRODUCT_ATTRIBUTE_VALUE_NOT_EXIST_CODE = 2420014;
    public final static int PRODUCT_ATTRIBUTE_KEY_NOT_EXIST_CODE = 2420015;
    public final static int PRODUCT_ATTRIBUTE_KEY_CREATE_ERROR_CODE = 2420016;
    public final static int PRODUCT_ATTRIBUTE_VALUE_CREATE_ERROR_CODE = 2420017;
    public final static int PRODUCT_ATTRIBUTE_KEY_EXIST_CODE = 2420018;
    public final static int PRODUCT_ATTRIBUTE_VALUE_EXIST_CODE = 2420019;
    public final static int PRODUCT_GROSSWEIGHT_ERROR_CODE = 2420020;

    public final static LogisticsReturnCode PRODUCT_ATTRIBUTE_OPT_PARAM_ILLEGAL = new LogisticsReturnCode("参数不合法", PRODUCT_ATTRIBUTE_OPT_PARAM_ILLEGAL_CODE);
    public final static LogisticsReturnCode PRODUCT_ATTRIBUTE_TEMPALATE_CREATE_ERROR = new LogisticsReturnCode("创建属性模板失败", PRODUCT_ATTRIBUTE_TEMPALATE_CREATE_ERROR_CODE);
    public final static LogisticsReturnCode PRODUCT_ATTRIBUTE_TEMPALATE_NOT_EXIST = new LogisticsReturnCode("属性模板不存在", PRODUCT_ATTRIBUTE_TEMPALATE_NOT_EXIST_CODE);
    public final static LogisticsReturnCode PRODUCT_ATTRIBUTE_TEMPALATE_QUERY_ERROR = new LogisticsReturnCode("查询属性模板失败", PRODUCT_ATTRIBUTE_TEMPALATE_QUERY_ERROR_CODE);
    public final static LogisticsReturnCode PRODUCT_ATTRIBUTE_TEMPALATE_ALEARY_EXIST = new LogisticsReturnCode("属性模板已经存在", PRODUCT_ATTRIBUTE_TEMPALATE_ALEARY_EXIST_CODE);
    public final static LogisticsReturnCode PRODUCT_ATTRIBUTE_KEY_QUERY_ERROR = new LogisticsReturnCode("查询属性项失败", PRODUCT_ATTRIBUTE_KEY_QUERY_ERROR_CODE);
    public final static LogisticsReturnCode PRODUCT_ATTRIBUTE_VALUE_NOT_EXIST = new LogisticsReturnCode("属性值不存在", PRODUCT_ATTRIBUTE_VALUE_NOT_EXIST_CODE);
    public final static LogisticsReturnCode PRODUCT_ATTRIBUTE_KEY_NOT_EXIST = new LogisticsReturnCode("属性项不存在", PRODUCT_ATTRIBUTE_KEY_NOT_EXIST_CODE);
    public final static LogisticsReturnCode PRODUCT_ATTRIBUTE_KEY_CREATE_ERROR = new LogisticsReturnCode("属性项新建失败", PRODUCT_ATTRIBUTE_KEY_CREATE_ERROR_CODE);
    public final static LogisticsReturnCode PRODUCT_ATTRIBUTE_VALUE_CREATE_ERROR = new LogisticsReturnCode("属性值新建失败", PRODUCT_ATTRIBUTE_VALUE_CREATE_ERROR_CODE);
    public final static LogisticsReturnCode PRODUCT_ATTRIBUTE_KEY_EXIST = new LogisticsReturnCode("属性项已存在", PRODUCT_ATTRIBUTE_KEY_EXIST_CODE);
    public final static LogisticsReturnCode PRODUCT_ATTRIBUTE_VALUE_EXIST = new LogisticsReturnCode("属性值已存在", PRODUCT_ATTRIBUTE_VALUE_EXIST_CODE);
    public final static LogisticsReturnCode PRODUCT_GROSSWEIGHT_ERROR = new LogisticsReturnCode("备案表中商品净重大于商品毛重", PRODUCT_GROSSWEIGHT_ERROR_CODE);

    /**
     * 选品工具操作相关 错误码
     * <p/>
     * 错误范围： 2430001 -- 2440000
     */
    public final static int SKU_SELECT_OPT_PARAM_ILLEGAL_CODE = 2430001;
    public final static int SKU_SELECT_OPT_OBJ_CONVERT_ERROR_CODE = 2430002;
    public final static int SKU_SELECT_CREATE_RECORD_ERROR_CODE = 2430003;
    public final static int SKU_SELECT_STATE_CHANGE_ERROR_CODE = 2430004;
    public final static int SKU_SELECT_SKUID_IS_EMPTH_CODE = 2430005;
    public final static int SKU_SELECT_QUERY_BY_PAGE_ERROR_CODE = 2430006;
    public final static int SKU_SELECT_CROSS_RATE_EMPTY_CODE = 2430007;
    public final static int SKU_SELECT_CATEGORY_GROSSPROFIT_EMPTY_CODE = 2430008;

    public final static LogisticsReturnCode SKU_SELECT_OPT_PARAM_ILLEGAL = new LogisticsReturnCode("参数不合法", SKU_SELECT_OPT_PARAM_ILLEGAL_CODE);
    public final static LogisticsReturnCode SKU_SELECT_OPT_OBJ_CONVERT_ERROR = new LogisticsReturnCode("对象转换异常", SKU_SELECT_OPT_OBJ_CONVERT_ERROR_CODE);
    public final static LogisticsReturnCode SKU_SELECT_CREATE_RECORD_ERROR = new LogisticsReturnCode("选品工具创建清单异常", SKU_SELECT_CREATE_RECORD_ERROR_CODE);
    public final static LogisticsReturnCode SKU_SELECT_STATE_CHANGE_ERROR = new LogisticsReturnCode("选品详情状态流转异常", SKU_SELECT_STATE_CHANGE_ERROR_CODE);
    public final static LogisticsReturnCode SKU_SELECT_SKUID_IS_EMPTH = new LogisticsReturnCode("SKUID不能为空", SKU_SELECT_SKUID_IS_EMPTH_CODE);
    public final static LogisticsReturnCode SKU_SELECT_QUERY_BY_PAGE_ERROR = new LogisticsReturnCode("分页查询选品详情异常", SKU_SELECT_QUERY_BY_PAGE_ERROR_CODE);
    public final static LogisticsReturnCode SKU_SELECT_CROSS_RATE_EMPTY = new LogisticsReturnCode("类目跨境消费税或增值税税率为空", SKU_SELECT_CROSS_RATE_EMPTY_CODE);
    public final static LogisticsReturnCode SKU_SELECT_CATEGORY_GROSSPROFIT_EMPTY = new LogisticsReturnCode("类目毛利率类型不准确或为空", SKU_SELECT_CATEGORY_GROSSPROFIT_EMPTY_CODE);


    /**
     * 开放平台出库服务异常
     * 错误范围： 2500001 -- 2600000
     */
    public static final int _C_UNKNOWN_ERROR = 2500001;
    public static final int _C_PARAMS_ILLEGAL = 2500002;
    public static final int _C_OPEN_CONCURRENT_ERR = 2500003;
    public static final int _C_STOCKOUT_ORDER_EXIST_ERR = 2500009;
    public static final int _C_NO_REGISTER_WAREHOUSE_CODE_ERR = 2500010;
    public static final int _C_UNUSABLE_WAREHOUSE_CODE_ERR = 2500011;
    public static final int _C_STOCKOUT_ORDER_NOT_EXIST_ERR = 2500012;
    public static final int _C_CURRENT_STATE_CANNOT_CANCEL_ERR = 2500013;
    public static final int _C_CURRENT_STATE_CANNOT_RECREATE_ERR = 2500014;
    public static final int _C_ONE_TIME_QUERY_NUM_OVER_LIMIT_ERR = 2500015;
    public static final int _C_STOCKOUT_ORDER_EDIT_BUT_NOT_FOUND_ERR = 2500016;
    public static final int _C_STOCKOUT_ORDER_SKU_NOT_FOUND_ERR = 2500017;
    public static final int _C_STOCKOUT_ORDER_WAREHOUSE_CODE_NOT_ENABLED = 2500018;
    public static final int _C_STOCKOUT_ORDER_SKU_STOCK_NOT_ENOUGH = 2500019;
    

    // 参数判空处理
    public static final int _C_PARAMS_SHOP_ID_EMPTY_ERR = 2500100;
    public static final int _C_PARAMS_COMPANY_CODE_EMPTY_ERR = 2500101;
    public static final int _C_PARAMS_COMPANY_NAME_EMPTY_ERR = 2500102;
    public static final int _C_PARAMS_WAREHOUSE_CODE_EMPTY_ERR = 2500104;
    public static final int _C_PARAMS_BUYER_ACCOUNT_EMPTY_ERR = 2500105;
    public static final int _C_PARAMS_BUYER_PHONE_EMPTY_ERR = 2500106;
    public static final int _C_PARAMS_PAYMENT_NO_EMPTY_ERR = 2500107;
    public static final int _C_PARAMS_PAY_ID_NO_EMPTY_ERR = 2500108;
    public static final int _C_PARAMS_PAY_NAME_EMPTY_ERR = 2500109;
    public static final int _C_PARAMS_CARRIER_CODE_EMPTY_ERR = 2500110;
    public static final int _C_PARAMS_CONSIGNEE_NAME_EMPTY_ERR = 2500111;
    public static final int _C_PARAMS_CONSIGNEE_PROVINCE_EMPTY_ERR = 2500112;
    public static final int _C_PARAMS_CONSIGNEE_CITY_EMPTY_ERR = 2500113;
    public static final int _C_PARAMS_CONSIGNEE_DISTRICT_EMPTY_ERR = 2500114;
    public static final int _C_PARAMS_CONSIGNEE_ADDRESS_EMPTY_ERR = 2500115;
    public static final int _C_PARAMS_CONSIGNEE_TELEPHONE_EMPTY_ERR = 2500116;
    public static final int _C_PARAMS_SKU_DETAIL_EMPTY_ERR = 2500117;
    public static final int _C_NO_REGISTER_SHOP_ID_ERR = 2500118;
    public static final int _C_ORDER_ILLEGAL_AMOUNT_ERR = 2500119;
    public static final int _C_ORDER_SKU_ILLEGAL_AMOUNT_ERR = 2500120;
    public static final int _C_ORDER_SKU_COUNT_ILLEGAL_ERR = 2500121;
    public static final int _C_ORDER_GOODS_AMOUNT_ILLEGAL_ERR = 2500122;
    public static final int _C_REAL_NAME_AUTHENTICATION_FAIL_ERR = 2500123;

    public static final AbstractReturnCode UNKNOWN_ERROR = new LogisticsReturnCode("系统内部异常", _C_UNKNOWN_ERROR);
    public static final AbstractReturnCode PARAMS_ILLEGAL = new LogisticsReturnCode("参数不合法", _C_PARAMS_ILLEGAL);
    public static final AbstractReturnCode OPEN_CONCURRENT_ERR = new LogisticsReturnCode("系统并发异常", _C_OPEN_CONCURRENT_ERR);
    public static final AbstractReturnCode STOCKOUT_ORDER_EXIST_ERR = new LogisticsReturnCode("该订单已创建过出库单", _C_STOCKOUT_ORDER_EXIST_ERR);
    public static final AbstractReturnCode NO_REGISTER_WAREHOUSE_CODE_ERR = new LogisticsReturnCode("未注册的仓库编码", _C_NO_REGISTER_WAREHOUSE_CODE_ERR);
    public static final AbstractReturnCode UNUSABLE_WAREHOUSE_CODE_ERR = new LogisticsReturnCode("不可用的仓库编码", _C_UNUSABLE_WAREHOUSE_CODE_ERR);
    public static final AbstractReturnCode STOCKOUT_ORDER_NOT_EXIST_ERR = new LogisticsReturnCode("订单不存在", _C_STOCKOUT_ORDER_NOT_EXIST_ERR);
    public static final AbstractReturnCode CURRENT_STATE_CANNOT_CANCEL_ERR = new LogisticsReturnCode("订单已出库不允许取消", _C_CURRENT_STATE_CANNOT_CANCEL_ERR);
    public static final AbstractReturnCode CURRENT_STATE_CANNOT_RECREATE_ERR = new LogisticsReturnCode("订单已出库不允许取消", _C_CURRENT_STATE_CANNOT_RECREATE_ERR);
    public static final AbstractReturnCode ONE_TIME_QUERY_NUM_OVER_LIMIT_ERR = new LogisticsReturnCode("一次性查询订单量不能大于500", _C_ONE_TIME_QUERY_NUM_OVER_LIMIT_ERR);
    public static final AbstractReturnCode STOCKOUT_ORDER_EDIT_BUT_NOT_FOUND_ERR = new LogisticsReturnCode("查找不到要修改的出库单", _C_STOCKOUT_ORDER_EDIT_BUT_NOT_FOUND_ERR);
    public static final AbstractReturnCode STOCKOUT_ORDER_SKU_NOT_FOUND_ERR = new LogisticsReturnCode("找不到对应的商品信息", _C_STOCKOUT_ORDER_SKU_NOT_FOUND_ERR);
    public static final AbstractReturnCode STOCKOUT_ORDER_WAREHOUSE_CODE_NOT_ENABLED = new LogisticsReturnCode("该仓库未被启用", _C_STOCKOUT_ORDER_WAREHOUSE_CODE_NOT_ENABLED);
    public static final AbstractReturnCode STOCKOUT_ORDER_SKU_STOCK_NOT_ENOUGH = new LogisticsReturnCode("商品库存不足", _C_STOCKOUT_ORDER_SKU_STOCK_NOT_ENOUGH);
    
    public static final AbstractReturnCode PARAMS_SHOP_ID_EMPTY_ERR = new LogisticsReturnCode("店铺ID不能为空", _C_PARAMS_SHOP_ID_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_COMPANY_CODE_EMPTY_ERR = new LogisticsReturnCode("企业代码不能为空", _C_PARAMS_COMPANY_CODE_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_COMPANY_NAME_EMPTY_ERR = new LogisticsReturnCode("企业名称不能为空", _C_PARAMS_COMPANY_NAME_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_ORDER_NO_EMPTY_ERR = new LogisticsReturnCode("订单号不能为空", _C_PARAMS_COMPANY_NAME_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_WAREHOUSE_CODE_EMPTY_ERR = new LogisticsReturnCode("仓库编码不能为空", _C_PARAMS_WAREHOUSE_CODE_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_BUYER_ACCOUNT_EMPTY_ERR = new LogisticsReturnCode("购物网站买家账号不能为空", _C_PARAMS_BUYER_ACCOUNT_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_BUYER_PHONE_EMPTY_ERR = new LogisticsReturnCode("购物网站买家手机号不能为空", _C_PARAMS_BUYER_PHONE_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_PAYMENT_NO_EMPTY_ERR = new LogisticsReturnCode("支付流水号不能为空", _C_PARAMS_PAYMENT_NO_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_PAY_ID_NO_EMPTY_ERR = new LogisticsReturnCode("买家身份证号码不能为空", _C_PARAMS_PAY_ID_NO_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_PAY_NAME_EMPTY_ERR = new LogisticsReturnCode("买家真实姓名不能为空", _C_PARAMS_PAY_NAME_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_CARRIER_CODE_EMPTY_ERR = new LogisticsReturnCode("国内物流承运商编码不能为空", _C_PARAMS_CARRIER_CODE_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_CONSIGNEE_NAME_EMPTY_ERR = new LogisticsReturnCode("收货人名称不能为空", _C_PARAMS_CONSIGNEE_NAME_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_CONSIGNEE_PROVINCE_EMPTY_ERR = new LogisticsReturnCode("收货人地址（省）不能为空", _C_PARAMS_CONSIGNEE_PROVINCE_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_CONSIGNEE_CITY_EMPTY_ERR = new LogisticsReturnCode("收货人地址（市）不能为空", _C_PARAMS_CONSIGNEE_CITY_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_CONSIGNEE_DISTRICT_EMPTY_ERR = new LogisticsReturnCode("收货人地址（区）不能为空", _C_PARAMS_CONSIGNEE_DISTRICT_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_CONSIGNEE_ADDRESS_EMPTY_ERR = new LogisticsReturnCode("收货人地址（详细地址）不能为空", _C_PARAMS_CONSIGNEE_ADDRESS_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_CONSIGNEE_TELEPHONE_EMPTY_ERR = new LogisticsReturnCode("收货人电话号码不能为空", _C_PARAMS_CONSIGNEE_TELEPHONE_EMPTY_ERR);
    public static final AbstractReturnCode PARAMS_SKU_DETAIL_EMPTY_ERR = new LogisticsReturnCode("订单商品明细不能为空", _C_PARAMS_SKU_DETAIL_EMPTY_ERR);
    public static final AbstractReturnCode NO_REGISTER_SHOP_ID_ERR = new LogisticsReturnCode("未注册的企业代码", _C_NO_REGISTER_SHOP_ID_ERR);
    public static final AbstractReturnCode ORDER_ILLEGAL_AMOUNT_ERR = new LogisticsReturnCode("订单相关金额必须大于等于0", _C_ORDER_ILLEGAL_AMOUNT_ERR);
    public static final AbstractReturnCode ORDER_SKU_ILLEGAL_AMOUNT_ERR = new LogisticsReturnCode("订单下商品相关金额必须大于等于0", _C_ORDER_SKU_ILLEGAL_AMOUNT_ERR);
    public static final AbstractReturnCode ORDER_SKU_COUNT_ILLEGAL_ERR = new LogisticsReturnCode("订单下商品购买数量必须大于0", _C_ORDER_SKU_COUNT_ILLEGAL_ERR);
    public static final AbstractReturnCode ORDER_GOODS_AMOUNT_ILLEGAL_ERR = new LogisticsReturnCode("订单货物价值必须等于实付金额+税金+优惠金额+运费", _C_ORDER_GOODS_AMOUNT_ILLEGAL_ERR);
    public static final AbstractReturnCode REAL_NAME_AUTHENTICATION_FAIL_ERR = new LogisticsReturnCode("实名认证失败", _C_REAL_NAME_AUTHENTICATION_FAIL_ERR);

    /**
     * 开放平台集货相关异常
     * 错误范围： 2600001 -- 2700000
     */
    public static final int _C_TWS_MERCHANTID_EMPTY_ERROR = 2600001;
    public static final AbstractReturnCode TWS_MERCHANTID_EMPTY_ERROR = new LogisticsReturnCode("集货回传-商户编码为空 \nen-us: The content of MERCHANTID, which is passed from Shippers is null", _C_TWS_MERCHANTID_EMPTY_ERROR);

    public static final int _C_TWS_ORDERID_ERROR = 2600002;
    public static final AbstractReturnCode TWS_ORDERID_ERROR = new LogisticsReturnCode("集货回传-出库单ID数值错误 \nen-us: The order ID of stock out, which is passed back from delivery is wrong", _C_TWS_ORDERID_ERROR);

    public static final int _C_TWS_DATE_ERROR = 2600003;
    public static final AbstractReturnCode TWS_DATE_ERROR = new LogisticsReturnCode("集货-日期错误 \nen-us: The date", _C_TWS_DATE_ERROR);

    public static final int _C_TWS_WEIGHT_ERROR = 2600004;
    public static final AbstractReturnCode TWS_WEIGHT_ERROR = new LogisticsReturnCode("集货回传-重量值错误", _C_TWS_WEIGHT_ERROR);

    public static final int _C_TWS_BILLNO_EMPTY_ERROR = 2600005;
    public static final AbstractReturnCode TWS_BILLNO_EMPTY_ERROR = new LogisticsReturnCode("集货回传-提货单号为空", _C_TWS_BILLNO_EMPTY_ERROR);

    public static final int _C_TWS_ORDER_NOT_EXIST_ERROR = 2600006;
    public static final AbstractReturnCode TWS_ORDER_NOT_EXIST_ERROR = new LogisticsReturnCode("集货回传-订单不存在", _C_TWS_ORDER_NOT_EXIST_ERROR);

    public static final int _C_TWS_ROUTE_EMPTY_ERROR = 2600007;
    public static final AbstractReturnCode TWS_ROUTE_EMPTY_ERROR = new LogisticsReturnCode("集货回传-路由信息为空", _C_TWS_ROUTE_EMPTY_ERROR);

    public static final int _C_TWS_ROUTE_DATE_ERROR = 2600008;
    public static final AbstractReturnCode TWS_ROUTE_DATE_ERROR = new LogisticsReturnCode("集货回传-路由时间错误", _C_TWS_ROUTE_DATE_ERROR);

    public static final int _C_TWS_ROUTE_CODE_ERROR = 2600009;
    public static final AbstractReturnCode TWS_ROUTE_CODE_ERROR = new LogisticsReturnCode("集货回传-路由编码为空", _C_TWS_ROUTE_CODE_ERROR);

    public static final int _C_TWS_ROUTE_CITY_ERROR = 2600010;
    public static final AbstractReturnCode TWS_ROUTE_CITY_ERROR = new LogisticsReturnCode("集货回传-路由城市为空", _C_TWS_ROUTE_CITY_ERROR);

    public static final int _C_TWS_ROUTE_DESCRIPTION_ERROR = 2600011;
    public static final AbstractReturnCode TWS_ROUTE_DESCRIPTION_ERROR = new LogisticsReturnCode("集货回传-路由详情为空", _C_TWS_ROUTE_DESCRIPTION_ERROR);

    public static final int _C_TWS_SAVE_OUT_ERROR = 2600012;
    public static final AbstractReturnCode TWS_SAVE_OUT_ERROR = new LogisticsReturnCode("集货-丰趣海淘保存出库信息失败", _C_TWS_SAVE_OUT_ERROR);

    public static final int _C_TWS_MERCHANT_NOT_EXIST = 2600013;
    public static final AbstractReturnCode TWS_MERCHANT_NOT_EXIST = new LogisticsReturnCode("集货-商户编码不存在", _C_TWS_MERCHANT_NOT_EXIST);

    public static final int _C_TWS_DATE_ORDER_ERROR = 2600014;
    public static final AbstractReturnCode TWS_DATE_ORDER_ERROR = new LogisticsReturnCode("集货查询-开始日期大于结束日期", _C_TWS_DATE_ORDER_ERROR);

    public static final int _C_TWS_ORDER_STATE_EMPTY = 2600015;
    public static final AbstractReturnCode TWS_ORDER_STATE_EMPTY = new LogisticsReturnCode("集货查询-订单状态内容为空", _C_TWS_ORDER_STATE_EMPTY);

    public static final int _C_TWS_ORDER_STATE_ERROR = 2600016;
    public static final AbstractReturnCode TWS_ORDER_STATE_ERROR = new LogisticsReturnCode("集货查询-订单状态值错误", _C_TWS_ORDER_STATE_ERROR);

    public static final int _C_TWS_ORDER_SAVE_SENDOUT_ERROR = 2600017;
    public static final AbstractReturnCode TWS_ORDER_SAVE_SENDOUT_ERROR = new LogisticsReturnCode("集货回传-非出库中的订单无法调转到已出库", _C_TWS_ORDER_SAVE_SENDOUT_ERROR);

    public static final int _C_ILLEGLE_ARGUMENT = 2600018;
    public static final AbstractReturnCode ILLEGLE_ARGUMENT = new LogisticsReturnCode("集货-请求参数有误", _C_ILLEGLE_ARGUMENT);

    /**
     * 开放平台供货相关异常
     * 错误范围： 2700001 -- 2800000
     */
    public static final int _C_SUPPLIER_MERCHANTID_EMPTY_ERROR = 2700001;
    public static final AbstractReturnCode SUPPLIER_MERCHANTID_EMPTY_ERROR = new LogisticsReturnCode("供货商回传-商户编码为空 \nen-us: The content of MERCHANTID, which is passed from Shippers is null", _C_SUPPLIER_MERCHANTID_EMPTY_ERROR);

    public static final int _C_SUPPLIER_ORDERID_ERROR = 2700002;
    public static final AbstractReturnCode SUPPLIER_ORDERID_ERROR = new LogisticsReturnCode("供货商回传-出库单ID数值错误 \nen-us: The order ID of stock out, which is passed back from delivery is wrong", _C_SUPPLIER_ORDERID_ERROR);

    public static final int _C_SUPPLIER_DATE_ERROR = 2700003;
    public static final AbstractReturnCode SUPPLIER_DATE_ERROR = new LogisticsReturnCode("供货商-日期错误 \nen-us: The date is error", _C_SUPPLIER_DATE_ERROR);

    public static final int _C_SUPPLIER_WEIGHT_ERROR = 2700004;
    public static final AbstractReturnCode SUPPLIER_WEIGHT_ERROR = new LogisticsReturnCode("供货商回传-重量值错误 \nen-us: The weight is error", _C_SUPPLIER_WEIGHT_ERROR);

    public static final int _C_SUPPLIER_BILLNO_EMPTY_ERROR = 2700005;
    public static final AbstractReturnCode SUPPLIER_BILLNO_EMPTY_ERROR = new LogisticsReturnCode("供货商回传-提货单号为空 \nen-us: billno is empty", _C_SUPPLIER_BILLNO_EMPTY_ERROR);

    public static final int _C_SUPPLIER_ORDER_NOT_EXIST_ERROR = 2700006;
    public static final AbstractReturnCode SUPPLIER_ORDER_NOT_EXIST_ERROR = new LogisticsReturnCode("供货商回传-订单不存在 \nen-us: The order id does not exist ", _C_SUPPLIER_ORDER_NOT_EXIST_ERROR);

    public static final int _C_SUPPLIER_ROUTE_EMPTY_ERROR = 2700007;
    public static final AbstractReturnCode SUPPLIER_ROUTE_EMPTY_ERROR = new LogisticsReturnCode("供货商回传-路由信息为空 \nen-us: Routing information is empty ", _C_SUPPLIER_ROUTE_EMPTY_ERROR);

    public static final int _C_SUPPLIER_ROUTE_DATE_ERROR = 2700008;
    public static final AbstractReturnCode SUPPLIER_ROUTE_DATE_ERROR = new LogisticsReturnCode("供货商回传-路由时间错误 \nen-us: Routing time error", _C_SUPPLIER_ROUTE_DATE_ERROR);

    public static final int _C_SUPPLIER_ROUTE_CODE_ERROR = 2700009;
    public static final AbstractReturnCode WMS_ROUTE_CODE_ERROR = new LogisticsReturnCode("供货商回传-路由编码为空 \nen-us: The routing code is empty", _C_SUPPLIER_ROUTE_CODE_ERROR);

    public static final int _C_SUPPLIER_ROUTE_CITY_ERROR = 2700010;
    public static final AbstractReturnCode SUPPLIER_ROUTE_CITY_ERROR = new LogisticsReturnCode("供货商回传-路由城市为空 \nen-us: Routing is empty city", _C_SUPPLIER_ROUTE_CITY_ERROR);

    public static final int _C_SUPPLIER_ROUTE_DESCRIPTION_ERROR = 2700011;
    public static final AbstractReturnCode SUPPLIER_ROUTE_DESCRIPTION_ERROR = new LogisticsReturnCode("供货商回传-路由详情为空 \nen-us: Routing information is empty", _C_SUPPLIER_ROUTE_DESCRIPTION_ERROR);

    public static final int _C_SUPPLIER_SAVE_OUT_ERROR = 2700012;
    public static final AbstractReturnCode SUPPLIER_SAVE_OUT_ERROR = new LogisticsReturnCode("供货商-丰趣海淘保存出库信息失败 \nen-us: FQ save the outbound information failure", _C_SUPPLIER_SAVE_OUT_ERROR);

    public static final int _C_SUPPLIER_MERCHANT_NOT_EXIST = 2700013;
    public static final AbstractReturnCode SUPPLIER_MERCHANT_NOT_EXIST = new LogisticsReturnCode("供货商-商户编码不存在 \nen-us: Merchant code does not exist", _C_SUPPLIER_MERCHANT_NOT_EXIST);

    public static final int _C_SUPPLIER_DATE_ORDER_ERROR = 2700014;
    public static final AbstractReturnCode SUPPLIER_DATE_ORDER_ERROR = new LogisticsReturnCode("供货商查询-开始日期大于结束日期 \nen-us: Start date is greater than end date", _C_SUPPLIER_DATE_ORDER_ERROR);

    public static final int _C_SUPPLIER_ORDER_STATE_EMPTY = 2700015;
    public static final AbstractReturnCode SUPPLIER_ORDER_STATE_EMPTY = new LogisticsReturnCode("供货商查询-订单状态内容为空 \nen-us: The content of the order status is empty", _C_SUPPLIER_ORDER_STATE_EMPTY);

    public static final int _C_SUPPLIER_ORDER_STATE_ERROR = 2700016;
    public static final AbstractReturnCode SUPPLIER_ORDER_STATE_ERROR = new LogisticsReturnCode("供货商查询-订单状态值错误 \nen-us: Order status value error", _C_SUPPLIER_ORDER_STATE_ERROR);

    public static final int _C_SUPPLIER_ORDER_PDF_REGION_ERROR = 2700017;
    public static final AbstractReturnCode SUPPLIER_ORDER_PDF_REGION_ERROR = new LogisticsReturnCode("供货商查询PDF面单-异常 \nen-us: Supplier query PDF surface single - exception", _C_SUPPLIER_ORDER_PDF_REGION_ERROR);

    public static final int _C_SUPPLIER_SKU_SYNC_ERROR = 2700018;
    public static final AbstractReturnCode SUPPLIER_SKU_SYNC_ERROR = new LogisticsReturnCode("供货商同步sku库存-同步数据为空", _C_SUPPLIER_SKU_SYNC_ERROR);

    public static final int _C_SUPPLIER_DATE_OVER_ERROR = 2700019;
    public static final AbstractReturnCode SUPPLIER_DATE_OVER_ERROR = new LogisticsReturnCode("供货商订单查询时间跨度不能超过一个月 \nen-us: Supplier order query time span no more than a month", _C_SUPPLIER_DATE_OVER_ERROR);

    public static final int _C_SUPPLIER_SKUID_EMPTY_EMPTY = 2700020;
    public static final AbstractReturnCode SUPPLIER_SKUID_EMPTY_EMPTY = new LogisticsReturnCode("供货商回传商品库存skuID或thirdSkuid不能同时为空 \nen-us: skuID or thirdSkuid cannot at the same time is empty", _C_SUPPLIER_SKUID_EMPTY_EMPTY);

    public static final int _C_SUPPLIER_OPERATE_TYPE_ERROR = 2700021;
    public static final AbstractReturnCode SUPPLIER_OPERATE_TYPE_ERROR = new LogisticsReturnCode("供货商更新商品库存-操作类型错误 \nen-us: Operation type error", _C_SUPPLIER_OPERATE_TYPE_ERROR);

    public static final int _C_SUPPLIER_OPERATE_COUNT_ERROR = 2700022;
    public static final AbstractReturnCode SUPPLIER_OPERATE_COUNT_ERROR = new LogisticsReturnCode("供货商更新商品库存-操作数量小于零 \nen-us: Operation number less than zero", _C_SUPPLIER_OPERATE_COUNT_ERROR);

    public static final int _C_SUPPLIER_QUERY_COUNT_ERROR = 2700023;
    public static final AbstractReturnCode SUPPLIER_QUERY_COUNT_ERROR = new LogisticsReturnCode("供货商查询商品库存-批次库存记录不存在 \nen-us: inventory record does not exist", _C_SUPPLIER_QUERY_COUNT_ERROR);

    public static final int _C_SUPPLIER_ORDER_PDF_INFO_ERROR = 2700024;
    public static final AbstractReturnCode SUPPLIER_ORDER_PDF_INFO_ERROR = new LogisticsReturnCode("供货商获取PDF面单信息-异常",_C_SUPPLIER_ORDER_PDF_INFO_ERROR);

    public static final int _C_SUPPLIER_BIZID_ERROR = 2700025;
    public static final AbstractReturnCode SUPPLIER_BIZID_ERROR = new LogisticsReturnCode("供货商回传-子订单ID数值错误 \nen-us: The biz ID of stock out, which is passed back from delivery is wrong", _C_SUPPLIER_BIZID_ERROR);

    public static final int _C_SUPPLIER_CARRIERCODE_MAILNO_ERROR = 2700026;
    public static final AbstractReturnCode SUPPLIER_CARRIERCODE_MAILNO_ERROR = new LogisticsReturnCode("供货商回传-carriercode和mailno不能相同 \nen-us: mailno and carriercode may not be the same", _C_SUPPLIER_CARRIERCODE_MAILNO_ERROR);

    public static final int _C_SUPPLIER_ILLEGLE_ARGUMENT = 2700027;
    public static final AbstractReturnCode SUPPLIER_ILLEGLE_ARGUMENT = new LogisticsReturnCode("供货商-请求参数有误", _C_SUPPLIER_ILLEGLE_ARGUMENT);

    public static final int _C_SUPPLIER_LINE_NULL = 2700028;
    public final static AbstractReturnCode SUPPLIER_LINE_NULL_ERROR = new LogisticsReturnCode("调用供货商接口异常，出库单没有找到对应线路", _C_SUPPLIER_LINE_NULL);

    public static final int _C_SUPPLIER_CARD_INFO_INCOMPLETE = 2700030;
    public final static LogisticsReturnCode SUPPLIER_CARD_INFO_INCOMPLETE_ERROR = new LogisticsReturnCode("调用供货商接口异常，未获取到身份信息", _C_SUPPLIER_CARD_INFO_INCOMPLETE);

    /**
     * SKU分值计算相关异常
     * 错误范围： 2800001 -- 2801000
     */
    public static final int _C_SKU_SCORE_INNER_ERROR = 2800001;
    public static final AbstractReturnCode SKU_SCORE_INNER_ERROR = new LogisticsReturnCode("系统内部异常", _C_SKU_SCORE_INNER_ERROR);
    public static final int _C_SKU_SCORE_PARAMS_ILLEGAL = 2800002;
    public static final AbstractReturnCode SKU_SCORE_PARAMS_ILLEGAL = new LogisticsReturnCode("非法参数", _C_SKU_SCORE_PARAMS_ILLEGAL);
    public static final int _C_SKU_ACTUALSTOCK_NOT_EXIST = 2800003;
    public static final AbstractReturnCode SKU_ACTUALSTOCK_NOT_EXIST = new LogisticsReturnCode("SKU实物库存不存在", _C_SKU_ACTUALSTOCK_NOT_EXIST);

    /**
     * StockAdjustOrderServiceErrorCode
     * 库存调整单错误码
     * <p/>
     * 错误范围 2801001 -- 2802000
     */
    public final static int STOCK_ADJUST_ORDER_INNER_EXCEPTION_CODE = 2801001;
    public final static LogisticsReturnCode STOCK_ADJUST_ORDER_INNER_EXCEPTION = new LogisticsReturnCode("库存调整系统内部异常", STOCK_ADJUST_ORDER_INNER_EXCEPTION_CODE);
    public final static int STOCK_ADJUST_ORDER_PARAMS_ILLEGAL_CODE = 2801002;
    public final static LogisticsReturnCode STOCK_ADJUST_ORDER_PARAMS_ILLEGAL = new LogisticsReturnCode("参数不合法", STOCK_ADJUST_ORDER_PARAMS_ILLEGAL_CODE);
    public final static int STOCK_ADJUST_ORDER_FREEZE_FAILED_CODE = 2801003;
    public final static LogisticsReturnCode STOCK_ADJUST_ORDER_FREEZE_FAILED = new LogisticsReturnCode("冻结失败", STOCK_ADJUST_ORDER_FREEZE_FAILED_CODE);
    public final static int STOCK_ADJUST_ORDER_UNFREEZE_FAILED_CODE = 2801004;
    public final static LogisticsReturnCode STOCK_ADJUST_ORDER_UNFREEZE_FAILED = new LogisticsReturnCode("解冻失败", STOCK_ADJUST_ORDER_UNFREEZE_FAILED_CODE);
    public final static int STOCK_ADJUST_WEAR_STOCK_NOT_ENOUGH_CODE = 2801005;
    public final static LogisticsReturnCode STOCK_ADJUST_WEAR_STOCK_NOT_ENOUGH = new LogisticsReturnCode("坏品库存不足", STOCK_ADJUST_WEAR_STOCK_NOT_ENOUGH_CODE);

    /**
     * SPU服务相关异常
     * 错误范围： 2900001 -- 2900000
     */
    public static final int _C_SPU_PARAM_ILLEGAL_ERROR = 2900001;
    public static final int _C_SPU_CREATE_ERROR_ERROR = 2900002;
    public static final int _C_SPU_UPDATE_SKU_ERROR_ERROR = 2900003;
    public static final int _C_SPU_CREATE_OR_UPDATE_SPU_ERROR_ERROR = 2900004;
    public static final int _C_SPU_CREATE_OR_UPDATE_SKU_EMPTY_ERROR = 2900005;
    public static final int _C_SPU_SYNC_BASIC_INFO_TO_GOODS_EXECPTION = 2900006;
    public static final int _C_SPU_SYNC_DETAIL_INFO_TO_GOODS_EXECPTION = 2900007;
    public static final int _C_SPU_SYNC_FULL_INFO_TO_GOODS_EXECPTION = 2900008;
    public static final AbstractReturnCode SPU_PARAM_ILLEGAL_ERROR = new LogisticsReturnCode("参数不合法", _C_SPU_PARAM_ILLEGAL_ERROR);
    public static final AbstractReturnCode SPU_CREATE_ERROR_ERROR = new LogisticsReturnCode("SPU创建异常", _C_SPU_CREATE_ERROR_ERROR);
    public static final AbstractReturnCode SPU_UPDATE_SKU_ERROR_ERROR = new LogisticsReturnCode("SKU更新异常", _C_SPU_UPDATE_SKU_ERROR_ERROR);
    public static final AbstractReturnCode SPU_CREATE_OR_UPDATE_SPU_ERROR_ERROR = new LogisticsReturnCode("SPU创建或者更新异常", _C_SPU_CREATE_OR_UPDATE_SPU_ERROR_ERROR);
    public static final AbstractReturnCode SPU_CREATE_OR_UPDATE_SKU_EMPTY_ERROR = new LogisticsReturnCode("SPU创建或者时SKU不能为空", _C_SPU_CREATE_OR_UPDATE_SKU_EMPTY_ERROR);
    public static final AbstractReturnCode SPU_SYNC_BASIC_INFO_TO_GOODS_EXECPTION = new LogisticsReturnCode("同步SPU基本信息到商品系统异常", _C_SPU_SYNC_BASIC_INFO_TO_GOODS_EXECPTION);
    public static final AbstractReturnCode SPU_SYNC_DETAIL_INFO_TO_GOODS_EXECPTION = new LogisticsReturnCode("同步SPU详情信息到商品系统异常", _C_SPU_SYNC_DETAIL_INFO_TO_GOODS_EXECPTION);
    public static final AbstractReturnCode SPU_SYNC_FULL_INFO_TO_GOODS_EXECPTION = new LogisticsReturnCode("同步SPU全部信息到商品系统异常", _C_SPU_SYNC_FULL_INFO_TO_GOODS_EXECPTION);

    /**
     * EffectiveAlarm服务相关异常
     * 错误范围： 2900100 -- 2900200
     */
    public static final int _C_ALARM_EDITTEMPLATE_CODE_EXCEPTION = 2900100;
    public static final int _C_ALARM_UPDATETEMPLATE_CODE_EXCEPTION = 2900101;
    public static final int _C_ALARM_SERVICE_PARAMS_ILLEGAL = 2900102;
    public static final AbstractReturnCode ALARM_EDITTEMPLATE_CODE_EXCEPTION = new LogisticsReturnCode("编辑时效预警错误",_C_ALARM_EDITTEMPLATE_CODE_EXCEPTION);
    public static final AbstractReturnCode ALARM_UPDATETEMPLATE_CODE_EXCEPTION = new LogisticsReturnCode("更新时效预警模板错误，templateid or dayruleid is 0",_C_ALARM_UPDATETEMPLATE_CODE_EXCEPTION);
    public static final AbstractReturnCode ALARM_SERVICE_PARAMS_ILLEGAL = new LogisticsReturnCode("方法参数为空或不合法", _C_ALARM_SERVICE_PARAMS_ILLEGAL);

    /**
     * 渠道平台channel服务相关异常
     * 错误范围： 2900200 -- 2900300
     */
    public static final int _C_CHANNEL_PROVIDER_EMPTY_ERROR =  2900201;
    public static final int _C_CHANNEL_SELECT_SKU_SAVR_ERROR =  2900202;
    public static final int _C_CHANNEL_CREATE_SKU_ERROR =  2900203;
    public static final int _C_CHANNEL_CREATE_SKU_DETAIL_ERROR =  2900204;
    public static final int _C_CHANNEL_CREATE_ITEM_ERROR =  2900205;
    public static final int _C_CHANNEL_SELECT_SKU_NOT_EXIST_ERROR =  2900206;
    public static final int _C_CHANNEL_LEAF_CATEGORY_NOT_EXIST_ERROR =  2900207;
    public static final int _C_CHANNEL_PROVIDER_CONFIG_NOT_EXIST_ERROR =  2900208;
    public static final int _C_CHANNEL_BRAND_CONFIG_NOT_EXIST_ERROR =  2900209;
    public static final int _C_CHANNEL_CATEGORY_CONFIG_NOT_EXIST_ERROR =  2900210;
    public static final int _C_CHANNEL_SELECT_CHANNELSKU_LIST_ERROR =  2900211;
    public static final int _C_CHANNEL_QUERY_CHANNELSKU_DB_ERROR =  2900212;
    public static final AbstractReturnCode CHANNEL_PROVIDER_EMPTY_ERROR = new LogisticsReturnCode("供应商id不能为空",_C_CHANNEL_PROVIDER_EMPTY_ERROR);
    public static final AbstractReturnCode CHANNEL_SELECT_SKU_SAVR_ERROR = new LogisticsReturnCode("保存渠道爬虫商品错误",_C_CHANNEL_SELECT_SKU_SAVR_ERROR);
    public static final AbstractReturnCode CHANNEL_CREATE_SKU_ERROR = new LogisticsReturnCode("渠道商品生成sku失败",_C_CHANNEL_CREATE_SKU_ERROR);
    public static final AbstractReturnCode CHANNEL_CREATE_SKU_DETAIL_ERROR = new LogisticsReturnCode("渠道商品生成sku详情失败",_C_CHANNEL_CREATE_SKU_DETAIL_ERROR);
    public static final AbstractReturnCode CHANNEL_CREATE_ITEM_ERROR = new LogisticsReturnCode("渠道商品生成item失败",_C_CHANNEL_CREATE_ITEM_ERROR);
    public static final AbstractReturnCode CHANNEL_SELECT_SKU_NOT_EXIST_ERROR = new LogisticsReturnCode("渠道商品不能为空",_C_CHANNEL_SELECT_SKU_NOT_EXIST_ERROR);
    public static final AbstractReturnCode CHANNEL_LEAF_CATEGORY_NOT_EXIST_ERROR = new LogisticsReturnCode("二级类目错误或叶子类目不存在",_C_CHANNEL_LEAF_CATEGORY_NOT_EXIST_ERROR);
    public static final AbstractReturnCode CHANNEL_PROVIDER_CONFIG_NOT_EXIST_ERROR = new LogisticsReturnCode("渠道供应商配置为空",_C_CHANNEL_PROVIDER_CONFIG_NOT_EXIST_ERROR);
    public static final AbstractReturnCode CHANNEL_BRAND_CONFIG_NOT_EXIST_ERROR = new LogisticsReturnCode("匹配品牌配置为空",_C_CHANNEL_BRAND_CONFIG_NOT_EXIST_ERROR);
    public static final AbstractReturnCode CHANNEL_CATEGORY_CONFIG_NOT_EXIST_ERROR = new LogisticsReturnCode("匹配类目配置为空",_C_CHANNEL_CATEGORY_CONFIG_NOT_EXIST_ERROR);
    public static final AbstractReturnCode CHANNEL_SELECT_CHANNELSKU_LIST_ERROR = new LogisticsReturnCode("查询ChannelSelectSku列表失败",_C_CHANNEL_SELECT_CHANNELSKU_LIST_ERROR);
    public static final AbstractReturnCode CHANNEL_QUERY_CHANNELSKU_DB_ERROR = new LogisticsReturnCode("查询ChannelSelectSku数据库错误",_C_CHANNEL_QUERY_CHANNELSKU_DB_ERROR);

    /**
     * 商品系统同步品牌和类目数据到供应链接口相关异常
     * 错误范围： 2900300 -- 2900400
     */
    public static final int _C_BRAND_SYNC_PARAM_EMPTY_ERROR =  2900301;
    public static final AbstractReturnCode BRAND_SYNC_PARAM_EMPTY_ERROR = new LogisticsReturnCode("品牌记录参数不能为空",_C_BRAND_SYNC_PARAM_EMPTY_ERROR);
    public static final int _C_CATEGORY_SYNC_PARAM_EMPTY_ERROR =  2900302;
    public static final AbstractReturnCode CATEGORY_SYNC_PARAM_EMPTY_ERROR = new LogisticsReturnCode("类目记录参数不能为空",_C_CATEGORY_SYNC_PARAM_EMPTY_ERROR);
    public static final int _C_DEL_ID_EMPTY_ERROR =  2900303;
    public static final AbstractReturnCode DEL_ID_EMPTY_ERROR = new LogisticsReturnCode("ID参数为空或未对应有效记录",_C_DEL_ID_EMPTY_ERROR);
}

