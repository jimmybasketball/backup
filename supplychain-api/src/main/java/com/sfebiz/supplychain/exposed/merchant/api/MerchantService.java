package com.sfebiz.supplychain.exposed.merchant.api;

import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.merchant.entity.*;

import java.util.List;

/**
 * 物流平台货主服务
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017/7/5 10:04
 */
public interface MerchantService {

    /* ------------ 货主 begin ------------  */

    /**
     * 创建货主
     *
     * @param operator       操作人
     * @param merchantEntity 货主实体
     * @return 货主id
     */
    public CommonRet<Long> createMerchant(String operator, MerchantEntity merchantEntity);

    /**
     * 修改货主信息
     *
     * @param operator       操作人
     * @param id             id
     * @param merchantEntity 货主实体
     * @return void
     */
    public CommonRet<Void> updateMerchantEntity(String operator, Long id, MerchantEntity merchantEntity);


    /**
     * 改变货主状态
     *
     * @param operator 操作人
     * @param id       ID
     * @param state    状态
     * @return void
     */
    public CommonRet<Void> changeMerchantState(String operator, Long id, String state);

    /* ------------ 货主 end  ------------ */



    /* ------------ 货主供应商 begin  ------------ */

    /**
     * 创建货主供应商
     *
     * @param operator               操作人
     * @param merchantProviderEntity 货主供应商实体
     * @return ID
     */
    public CommonRet<Long> createMerchantProvider(String operator, MerchantProviderEntity merchantProviderEntity);

    /**
     * 修改货主供应商基本信息
     *
     * @param operator               操作人
     * @param id                     主键
     * @param merchantProviderEntity 货主供应商实体
     * @return void
     */
    public CommonRet<Void> updateMerchantProvider(String operator, Long id, MerchantProviderEntity merchantProviderEntity);


    /**
     * 修改货主供应商状态
     *
     * @param operator 操作人
     * @param id       主键
     * @param state    状态值
     * @return void
     */
    public CommonRet<Void> changeMerchantProviderState(String operator, Long id, String state);

    /* ------------ 货主供应商 end  ------------ */







    /* ------------ 货主供应商线路配置 beigin  ------------ */

    /**
     * 添加供应商线路
     *
     * @param operator                       操作人
     * @param merchantProviderLineEntityList 供应商线路集合
     * @return void
     */
    public CommonRet<Void> addMerchantProviderLine(String operator, List<MerchantProviderLineEntity> merchantProviderLineEntityList);

    /**
     * 删除供应商线路
     *
     * @param operator 操作人
     * @param id       供应商线路关系主键ID
     * @return void
     */
    public CommonRet<Void> deleteMerchantProviderLine(String operator, Long id);


    /**
     * 修改供应商线路状态
     *
     * @param operator 操作人
     * @param id       供应商线路关系主键ID
     * @param state    状态
     * @return
     */
    public CommonRet<Void> changeMerchantProviderLineState(String operator, Long id, String state);


    /* ------------ 货主供应商线路配置 end  ------------ */





    /*  ------------  货主申报方式配置  beigin  ------------  */

    /**
     * 配置货主申报方式
     *
     * @param merchantId                   货主ID
     * @param payType                      支付方式
     * @param merchantPayDeclareEntityList 货主申报实体集合
     * @return void
     */
    public CommonRet<Void> setMerchantPayDeclare(Long merchantId, String payType, List<MerchantPayDeclareEntity> merchantPayDeclareEntityList);


    /**
     * 修改货主申报信息  不会修改货主、口岸、支付方式
     *
     * @param id             主键ID
     * @param declarePayType 申报支付类型
     * @param declareAccount 申报账号
     * @return
     */
    public CommonRet<Void> updateMerchantPayDeclare(Long id, String declarePayType, String declareAccount);

    /**
     * 删除货主申报方式
     *
     * @param id 主键ID
     * @return void
     */
    public CommonRet<Void> deleteMerchantPayDeclare(Long id);


    /*  ------------  货主申报方式配置  end  ------------  */







    /*  ------------  货主包材配置  beigin  ------------  */

    /**
     * 创建货主包材配置
     *
     * @param merchantPackageMaterialEntity 实体
     * @return id
     */
    public CommonRet<Long> createMerchantPackageMaterial(MerchantPackageMaterialEntity merchantPackageMaterialEntity);

    /**
     * 修改货主包材配置
     *
     * @param id                            主键ID
     * @param merchantPackageMaterialEntity 实体
     * @return void
     */
    public CommonRet<Void> updateMerchantPackageMaterial(Long id, MerchantPackageMaterialEntity merchantPackageMaterialEntity);

    /**
     * 删除货主包材配置
     *
     * @param id 主键ID
     * @return void
     */
    public CommonRet<Void> deleteMerchantPackageMaterial(Long id);


    /*  ------------  货主包材配置  end  ------------  */


}
