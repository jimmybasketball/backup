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
import com.sfebiz.supplychain.exposed.merchant.enums.MerchantStateType;
import com.sfebiz.supplychain.lock.Lock;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantDO;
import com.sfebiz.supplychain.persistence.base.merchant.manager.MerchantManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 物流平台商户服务实现
 *
 * @author liujc
 * @create 2017-07-05 12:31
 **/
@Service("merchantService")
public class MerchantServiceImpl implements MerchantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantServiceImpl.class);

    @Resource
    private Lock distributedLock;

    @Resource
    private MerchantManager merchantManager;

    private static final String INSERT_KEY = MerchantServiceImpl.class + "INSERT_KEY";

    private static final String UPDATE_KEY = MerchantServiceImpl.class + "UPDATE_KEY";

    private static final String CHANGE_STATE_KEY = MerchantServiceImpl.class + "CHANGE_STATE_KEY";

    /**
     * 创建商户
     *
     * @param operator       操作人
     * @param merchantEntity 商户实体
     * @return               商户ID
     */
    @Override
    @MethodParamValidate
    public CommonRet<Long> createMerchant(
            @ParamNotBlank("操作人不能为空") String operator,
            @ParamNotBlank("商户不能为空") MerchantEntity merchantEntity) {
        CommonRet<Long> commonRet = new CommonRet<Long>();
        String key = INSERT_KEY + merchantEntity.merchantAccountId;
        // 并发控制
        if (distributedLock.fetch(key)) {
            try {
                // 判断账号ID是否已存在
                if (merchantManager.checkMerchantAccountIdIsExist(null, merchantEntity.merchantAccountId)) {
                    LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台商户-创建] 商户账户ID已存在")
                            .addParm("商户账户ID", merchantEntity.merchantAccountId)
                            .addParm("商户", merchantEntity).log();
                    commonRet.setRetCode(SupplyChainReturnCode.FAIL.code);
                    commonRet.setRetMsg("商户账户ID已存在");
                    return commonRet;
                }
                MerchantDO merchantDO = new MerchantDO();

                BeanCopier beanCopier =
                        BeanCopier.create(MerchantEntity.class, MerchantDO.class, false);
                beanCopier.copy(merchantEntity, merchantDO, null);

                //初始化状态为启用
                merchantDO.setState(MerchantStateType.ENABLE.getValue());
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
     * @param id             id
     * @param merchantEntity 商户实体
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> updateMerchantEntity(
            @ParamNotBlank("操作人不能为空") String operator,
            @ParamNotBlank("ID不能为空") Long id,
            @ParamNotBlank("商户不能为空") MerchantEntity merchantEntity) {

        CommonRet<Void> commonRet = new CommonRet<Void>();
        String key = UPDATE_KEY + id;
        // 并发控制
        if (distributedLock.fetch(key)) {
            try {
                // 判断账号ID是否已存在
                merchantEntity.setId(id);
                if (merchantManager.checkMerchantAccountIdIsExist(id, merchantEntity.merchantAccountId)) {
                    LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台商户-基本信息修改] 商户ID已存在")
                            .addParm("商户账户ID", merchantEntity.merchantAccountId)
                            .addParm("商户", merchantEntity)
                            .log();
                    commonRet.setRetCode(SupplyChainReturnCode.FAIL.code);
                    commonRet.setRetMsg("商户账户ID已存在");
                    return commonRet;
                }

                MerchantDO merchantDO = new MerchantDO();
                BeanCopier beanCopier =
                        BeanCopier.create(MerchantEntity.class, MerchantDO.class, false);
                beanCopier.copy(merchantEntity, merchantDO, null);

                merchantDO.setModifiedBy(operator);
                merchantDO.setId(id);

                merchantManager.update(merchantDO);

                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流平台商户-基本信息修改] 成功")
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
                    setMsg("[物流平台商户-基本信息修改] 并发异常")
                    .log();
            commonRet.setRetCode(SupplyChainReturnCode.FAIL.code);
            commonRet.setRetMsg("并发异常");
            return commonRet;
        }
    }

    /**
     * 改变商户状态
     * @param operator          操作人
     * @param id                ID
     * @param state             状态
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> changMerchantState(
            @ParamNotBlank String operator,
            @ParamNotBlank Long id,
            @ParamNotBlank String state) {

        CommonRet<Void> commonRet = new CommonRet<Void>();
        String key = CHANGE_STATE_KEY + id;
        if (distributedLock.fetch(key)) {
            try {
                // 状态值是否合法
                if (!MerchantStateType.ENABLE.getValue().equals(state)
                        && !MerchantStateType.DISABLE.getValue().equals(state)) {
                    commonRet.setRetCode(SupplyChainReturnCode.FAIL.code);
                    commonRet.setRetMsg("商户状态不合法");
                    return commonRet;
                }

                // 商户是否存在
                MerchantDO merchantDO = merchantManager.getById(id);
                if (merchantDO == null) {
                    LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台商户-修改商户状态] 商户不存在")
                            .addParm("id", id)
                            .log();
                    commonRet.setRetCode(SupplyChainReturnCode.FAIL.code);
                    commonRet.setRetMsg("商户不存在");
                    return commonRet;
                }

                //验证状态是否已修改过
                if (state.equals(merchantDO.getState())) {
                    commonRet.setRetCode(SupplyChainReturnCode.FAIL.code);
                    commonRet.setRetMsg("商户已被" + MerchantStateType.valueOf(state).name);
                    return commonRet;
                }

                MerchantDO updateDO = new MerchantDO();
                updateDO.setId(id);
                updateDO.setState(state);
                merchantManager.update(updateDO);

                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流平台商户-修改商户状态] 成功")
                        .addParm("id", id)
                        .addParm("state", state)
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
                    setMsg("[物流平台商户-修改商户状态] 并发异常")
                    .log();
            commonRet.setRetCode(SupplyChainReturnCode.FAIL.code);
            commonRet.setRetMsg("并发异常");
            return commonRet;
        }

    }


}
