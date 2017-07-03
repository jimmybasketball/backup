package com.sfebiz.supplychain.entity;

import java.io.Serializable;

/**
 * 空对象，作为接口通用响应的泛型标识
 * 使用此对象时，通用响应对象应只包含响应码和响应消息
 * @author liujc
 * @create 2017-06-29 16:23
 **/
public class Void implements Serializable{
    private static final long serialVersionUID = 7437613581392434572L;

    private Void(){}
}
