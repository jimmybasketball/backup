package com.sfebiz.supplychain.open.exposed.wms.enums;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 云仓API 操作类型枚举
 * 
 * @author matt
 * @version $Id: OpenWmsTradeActionType.java, v 0.1 2017年3月24日 下午2:50:03 matt Exp $
 */
public enum OpenWmsTradeActionType {
	
    CREATE("CREATE", "创建"),
    EDIT("EDIT","修改"),
    SEARCH("SEARCH","查询");

    /** 键值码 */
    private String code;

    /** 参数业务描述 */
    private String desc;

    /**
     * 根据键值码返回任务类型枚举
     * @param code 键值码
     * @return 类型枚举
     */
    public static OpenWmsTradeActionType getEnumByCode(String code) {

        if (StringUtils.isBlank(code)) {
            return null;
        }

        for (OpenWmsTradeActionType each : values()) {
            if (StringUtils.equals(each.getCode(), code)) {
                return each;
            }
        }

        return null;
    }
    
    /**
     * 获取枚举的字符串列表
     * 
     * @return code:desc;code:desc;...
     */
    public static String getCodeListStr() {
    	StringBuilder sb = new StringBuilder();
        for (OpenWmsTradeActionType each : values()) {
        	sb.append(each.code).append(":").append(each.desc).append(",");
        }
        return sb.toString();
    }

    /**
     * 构造方法
     * 
     * @param code 参数名称
     * @param desc 参数含义描述
     */
    OpenWmsTradeActionType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Getter method for property <tt>name</tt>.
     * 
     * @return property value of name
     */
    public String getCode() {
        return code;
    }

    /**
     * Getter method for property <tt>desc</tt>.
     * 
     * @return property value of desc
     */
    public String getDesc() {
        return desc;
    }

    /** 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
    public static void main(String[] args) {
		System.out.println(OpenWmsTradeActionType.values());
	}
}
