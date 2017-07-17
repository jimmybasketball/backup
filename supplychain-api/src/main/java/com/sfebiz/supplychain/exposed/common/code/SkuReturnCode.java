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

    public final static int _C_MERCHANT_GOOD_EXISTENT_EXCEPTION = 1042001;
    public final static SkuReturnCode MERCHANT_GOOD_EXISTENT_EXCEPTION = new SkuReturnCode("货主已添加过对应商品信息", _C_MERCHANT_GOOD_EXISTENT_EXCEPTION);

    public final static int _C_BARCODE_GOOD_EXISTENT= 1043000;
    public final static int _C_BARCODE_DIFFERENT_GOODS_EXCEPTION = 1043001;
    public final static int _C_BARCODE_GOOD_NON_EXISTENT_EXCEPTION = 1043002;
    public final static SkuReturnCode BARCODE_GOOD_EXISTENT = new SkuReturnCode("条码对应商品信息存在", _C_BARCODE_GOOD_EXISTENT);
    public final static SkuReturnCode BARCODE_DIFFERENT_GOODS_EXCEPTION = new SkuReturnCode("多个条码对应不同商品信息", _C_BARCODE_DIFFERENT_GOODS_EXCEPTION);
    public final static SkuReturnCode BARCODE_GOOD_NON_EXISTENT_EXCEPTION = new SkuReturnCode("条码对应商品信息不存在", _C_BARCODE_GOOD_NON_EXISTENT_EXCEPTION);


}
