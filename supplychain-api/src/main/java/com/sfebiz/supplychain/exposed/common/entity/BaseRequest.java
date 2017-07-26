package com.sfebiz.supplychain.exposed.common.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * <p>
 * 基础请求类
 * </p>
 * 
 * @author matt
 * @Date 2017年7月12日 下午6:06:59
 */
public class BaseRequest implements Serializable {

    /** 序号 */
    private static final long serialVersionUID = -5594218827017017342L;

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this,
		ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
