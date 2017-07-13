package com.sfebiz.supplychain.exposed.common.code;

/**
 * <p>
 * 仓库相关服务的错误码定义 
 * </br> 错误码范围：[1010000 - 1020000)
 * </p>
 * 
 * @author matt
 * @Date 2017年7月13日 下午3:05:37
 */
public class WarehouseReturnCode extends SCReturnCode {

    private static final long serialVersionUID = 1276395419162099579L;
    public WarehouseReturnCode(String desc, int code) {
	super(desc, code);
    }

    public final static int _C_WAREHOUSE_NOT_EXIST_ERR = 1010001;
    public final static SCReturnCode WAREHOUSE_NOT_EXIST_ERR = new SCReturnCode("仓库不存在",
	    _C_WAREHOUSE_NOT_EXIST_ERR);

    public final static int _C_WAREHOUSE_NOW_CANNOT_DEL_ERR = 1010002;
    public final static SCReturnCode WAREHOUSE_NOW_CANNOT_DEL_ERR = new SCReturnCode("仓库当前不允许删除",
	    _C_WAREHOUSE_NOW_CANNOT_DEL_ERR);
}
