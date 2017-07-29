package com.sfebiz.supplychain.exposed.user.api;

/**
 * 实名认证服务接口
 * Created by jianyuanyang on 15/11/23.
 */
public interface RealNameAuthenticationService {

    /**
     * 实名认证
     * @param name 姓名
     * @param id 身份证
     * @return 是否通过
     */
    public boolean rz(String name, String id);

    /**
     * 实名认证
     * @param name 姓名
     * @param id 身份证
     * @param from 实名认证来源
     * @return  是否通过
     */
    public boolean rz(String name, String id, String from);
}
