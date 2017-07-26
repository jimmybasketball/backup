package com.sfebiz.supplychain.protocol.ceb.util;

import org.apache.commons.lang3.StringUtils;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/10
 * Time: 下午6:52
 */
public class AppandUtil {

    /**
     * 追加字符串，如果为null，不追加
     *
     * @param stringBuilder
     * @param value
     * @return
     */
    public static StringBuilder appendStringNotNull(StringBuilder stringBuilder, String value) {
        if (StringUtils.isBlank(value)) {
            return stringBuilder;
        }else{
            return stringBuilder.append(value);
        }
    }
}
