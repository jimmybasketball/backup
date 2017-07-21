package com.sfebiz.supplychain.exposed.common.code;

/**
 * 商品服务响应码
 * <p>
 * [1040000 - 1050000)
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017/7/15 15:57
 */
public class SkuReturnCode extends SCReturnCode {

    private static final long serialVersionUID = 4917363879785036959L;

    public SkuReturnCode(String desc, int code) {
        super(desc, code);
    }

    /** 基础商品相关 */
    public final static SkuReturnCode SKU_NOT_EXIST = new SkuReturnCode("商品不存在", 1041003);

    /** 货主商品相关 */
    public final static SkuReturnCode MERCHANT_GOOD_EXISTENT_EXCEPTION = new SkuReturnCode("货主已添加过对应商品信息", 1042006);

    /** 商品条码相关 */
    public final static SkuReturnCode BARCODE_GOOD_EXISTENT = new SkuReturnCode("条码对应商品信息存在", 1043006);
    public final static SkuReturnCode BARCODE_DIFFERENT_GOODS_EXCEPTION = new SkuReturnCode("多个条码对应不同商品信息", 1043007);
    public final static SkuReturnCode BARCODE_GOOD_NON_EXISTENT_EXCEPTION = new SkuReturnCode("条码对应商品信息不存在", 1043008);

    /** 商品备案相关 */
    public final static SkuReturnCode DECLARE_UNKNOWN_ERROR = new SkuReturnCode("备案服务未知异常", 1044001);
    public final static SkuReturnCode DECLARE_INNER_EXCEPTION = new SkuReturnCode("备案服务内部异常", 1044002);
    public final static SkuReturnCode DECLARE_CONCURRENT_EXCEPTION = new SkuReturnCode("备案服务并发异常", 1044003);
    public final static SkuReturnCode DECLARE_EXIST = new SkuReturnCode("备案商品已存在", 1044006);
    public final static SkuReturnCode DECLARE_NOT_ALLOW_DELETE = new SkuReturnCode("备案商品不允许删除", 1044007);


}
