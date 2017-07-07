package com.sfebiz.supplychain.service.merchant;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.aop.annotation.MethodParamValidate;
import com.sfebiz.supplychain.aop.annotation.ParamNotBlank;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.common.enums.SupplyChainReturnCode;
import com.sfebiz.supplychain.exposed.merchant.api.MerchantService;
import com.sfebiz.supplychain.exposed.merchant.entity.MerchantEntity;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantDO;
import com.sfebiz.supplychain.persistence.base.merchant.manager.MerchantManager;
import com.sfebiz.supplychain.lock.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;

import javax.annotation.Resource;

/**
 * 物流平台商户服务实现
 *
 * @author liujc
 * @create 2017-07-05 12:31
 **/
public class MerchantServiceImpl implements MerchantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantServiceImpl.class);

    @Resource
    private Lock distributedLock;

    @Resource
    private MerchantManager merchantManager;

    private static final String INSERT_KEY = MerchantServiceImpl.class + "INSERT_KEY";

    private static final String UPDATE_KEY = MerchantServiceImpl.class + "UPDATE_KEY";

    /**
     * 创建商户
     *
     * @param operator       操作人
     * @param merchantEntity 商户实体
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<Long> createMerchant(
            @ParamNotBlank("操作人不能为空") String operator,
            @ParamNotBlank("商户不能为空") MerchantEntity merchantEntity) {
        CommonRet<Long> commonRet = new CommonRet<Long>();
        String key = INSERT_KEY + merchantEntity.accountName;
        // 并发控制
        if (distributedLock.fetch(key)) {
            try {
                // 判断账号名是否已存在
                if (merchantManager.checkMerchantIsExist(merchantEntity)) {
                    LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台商户-创建] 商户账户名已存在")
                            .addParm("商户账户名", merchantEntity.accountName)
                            .addParm("商户", merchantEntity).log();
                    commonRet.setRetCode(SupplyChainReturnCode.FAIL.code);
                    commonRet.setRetMsg("商户账户名已存在");
                    return commonRet;
                }
                MerchantDO merchantDO = new MerchantDO();

                BeanCopier beanCopier =
                        BeanCopier.create(MerchantEntity.class, MerchantDO.class, false);
                beanCopier.copy(merchantEntity, merchantDO, null);

                merchantDO.setCreateBy(operator);
                merchantManager.insert(merchantDO);
                commonRet.setResult(merchantDO.getId());

                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流平台商户-创建] 成功")
                        .addParm("商户信息", merchantDO)
                        .log();
                return commonRet;
            } catch (Exception e) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setException(e)
                        .log();
                commonRet.reSet();
                commonRet.setRetCode(SupplyChainReturnCode.FAIL.code);
                commonRet.setRetMsg(e.getMessage());
                return commonRet;
            } finally {
                distributedLock.realease(key);
            }
        } else {
            LogBetter.instance(LOGGER).
                    setLevel(LogLevel.ERROR).
                    setMsg("[物流平台商户-创建] 并发异常")
                    .log();
            commonRet.setRetCode(SupplyChainReturnCode.FAIL.code);
            commonRet.setRetMsg("并发异常");
            return commonRet;
        }
    }

    /**
     * 修改商户信息
     *
     * @param operator       操作人
     * @param merchantId     商户id
     * @param merchantEntity 商户实体
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> updateMerchantEntity(
            @ParamNotBlank("操作人不能为空") String operator,
            @ParamNotBlank("商户ID不能为空") Long merchantId,
            @ParamNotBlank("商户不能为空") MerchantEntity merchantEntity) {

        CommonRet<Void> commonRet = new CommonRet<Void>();
        String key = UPDATE_KEY + merchantId;
        // 并发控制
        if (distributedLock.fetch(key)) {
            try {
                // 判断账号名是否已存在
                if (merchantManager.checkMerchantIsExist(merchantEntity)) {
                    LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台商户-修改] 商户账户名已存在")
                            .addParm("商户账户名", merchantEntity.accountName)
                            .addParm("商户", merchantEntity)
                            .log();
                    commonRet.setRetCode(SupplyChainReturnCode.FAIL.code);
                    commonRet.setRetMsg("商户账户名已存在");
                    return commonRet;
                }

                MerchantDO merchantDO = new MerchantDO();
                BeanCopier beanCopier =
                        BeanCopier.create(MerchantEntity.class, MerchantDO.class, false);
                beanCopier.copy(merchantEntity, merchantDO, null);

                merchantDO.setModifiedBy(operator);
                merchantDO.setId(merchantId);

                merchantManager.update(merchantDO);

                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流平台商户-创建] 成功")
                        .addParm("商户信息", merchantDO)
                        .log();

                return commonRet;
            } catch (Exception e) {


            } finally {
                distributedLock.realease(key);
            }
        }

        return null;
    }
}
