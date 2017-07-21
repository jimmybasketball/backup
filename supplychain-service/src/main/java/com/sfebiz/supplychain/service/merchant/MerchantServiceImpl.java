package com.sfebiz.supplychain.service.merchant;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.aop.annotation.MethodParamValidate;
import com.sfebiz.supplychain.aop.annotation.ParamNotBlank;
import com.sfebiz.supplychain.exposed.common.code.MerchantReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.merchant.api.MerchantService;
import com.sfebiz.supplychain.exposed.merchant.entity.*;
import com.sfebiz.supplychain.exposed.merchant.enums.MerchantProviderLineStateType;
import com.sfebiz.supplychain.exposed.merchant.enums.MerchantProviderStateType;
import com.sfebiz.supplychain.exposed.merchant.enums.MerchantStateType;
import com.sfebiz.supplychain.lock.Lock;
import com.sfebiz.supplychain.persistence.base.merchant.domain.*;
import com.sfebiz.supplychain.persistence.base.merchant.manager.*;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 物流平台货主服务实现
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

    @Resource
    private MerchantProviderManager merchantProviderManager;

    @Resource
    private MerchantProviderLineManager merchantProviderLineManager;

    @Resource
    private MerchantPayDeclareManager merchantPayDeclareManager;

    @Resource
    private MerchantPackageMaterialManager merchantPackageMaterialManager;

    private static final String INSERT_KEY = MerchantServiceImpl.class + "INSERT_KEY";

    private static final String UPDATE_KEY = MerchantServiceImpl.class + "UPDATE_KEY";

    private static final String CHANGE_STATE_KEY = MerchantServiceImpl.class + "CHANGE_STATE_KEY";

    private static final String SET_PAY_DECLARE_KE = MerchantServiceImpl.class + "SET_PAY_DECLARE_KE";

    /**
     * 创建货主
     *
     * @param operator       操作人
     * @param merchantEntity 货主实体
     * @return 货主ID
     */
    @Override
    @MethodParamValidate
    public CommonRet<Long> createMerchant(
            @ParamNotBlank("操作人不能为空") String operator,
            @ParamNotBlank("货主不能为空") MerchantEntity merchantEntity) {
        CommonRet<Long> commonRet = new CommonRet<Long>();
        String key = INSERT_KEY + merchantEntity.merchantAccountId;
        // 并发控制
        if (distributedLock.fetch(key)) {
            try {
                // 判断账号ID是否已存在
                if (merchantManager.checkMerchantAccountIdIsExist(null, merchantEntity.merchantAccountId)) {
                    LogBetter.instance(LOGGER)
                            .setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台货主-创建] 货主账户ID已存在")
                            .addParm("货主账户ID", merchantEntity.merchantAccountId)
                            .addParm("货主", merchantEntity)
                            .log();
                    commonRet.setRetCode(MerchantReturnCode.MERCHANT_ACCOUNT_ID_ALREADY_EXISTS.getCode());
                    commonRet.setRetMsg(MerchantReturnCode.MERCHANT_ACCOUNT_ID_ALREADY_EXISTS.getDesc());
                    return commonRet;
                }
                MerchantDO merchantDO = new MerchantDO();

                BeanCopier beanCopier =
                        BeanCopier.create(MerchantEntity.class, MerchantDO.class, false);
                beanCopier.copy(merchantEntity, merchantDO, null);

                //初始化状态为禁用，需要配置货主供应商、货主包材、货主申报方式 后才能启用
                merchantDO.setState(MerchantStateType.DISABLE.getValue());
                merchantDO.setCreateBy(operator);
                merchantManager.insert(merchantDO);
                commonRet.setResult(merchantDO.getId());

                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流平台货主-创建] 成功")
                        .addParm("货主信息", merchantDO)
                        .log();
                return commonRet;
            } catch (Exception e) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setErrorMsg("[物流平台货主-创建] 异常")
                        .setException(e)
                        .log();
                commonRet.reSet();
                commonRet.setRetCode(MerchantReturnCode.MERCHANT_UNKNOWN_ERROR.getCode());
                commonRet.setRetMsg(e.getMessage());
                return commonRet;
            } finally {
                distributedLock.realease(key);
            }
        } else {
            LogBetter.instance(LOGGER).
                    setLevel(LogLevel.ERROR).
                    setMsg("[物流平台货主-创建] 并发异常")
                    .log();
            commonRet.setRetCode(MerchantReturnCode.MERCHANT_CONCURRENT_EXCEPTION.getCode());
            commonRet.setRetMsg("并发异常");
            return commonRet;
        }
    }

    /**
     * 修改货主信息
     *
     * @param operator       操作人
     * @param id             id
     * @param merchantEntity 货主实体
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> updateMerchantEntity(
            @ParamNotBlank("操作人不能为空") String operator,
            @ParamNotBlank("ID不能为空") Long id,
            @ParamNotBlank("货主不能为空") MerchantEntity merchantEntity) {

        CommonRet<Void> commonRet = new CommonRet<Void>();
        String key = UPDATE_KEY + id;
        // 并发控制
        if (distributedLock.fetch(key)) {
            try {
                // 判断货主是否存在
                MerchantDO checkDO = merchantManager.getById(id);
                if (checkDO == null) {
                    LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台货主-基本信息修改] 货主不存在")
                            .addParm("id", id)
                            .log();
                    commonRet.setRetCode(MerchantReturnCode.MERCHANT_NOT_EXIST.getCode());
                    commonRet.setRetMsg(MerchantReturnCode.MERCHANT_NOT_EXIST.getDesc());
                    return commonRet;
                }
                // 判断账号ID是否已存在
                merchantEntity.setId(id);
                if (merchantManager.checkMerchantAccountIdIsExist(id, merchantEntity.merchantAccountId)) {
                    LogBetter.instance(LOGGER)
                            .setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台货主-基本信息修改] 货主ID已存在")
                            .addParm("货主账户ID", merchantEntity.merchantAccountId)
                            .addParm("货主", merchantEntity)
                            .log();
                    commonRet.setRetCode(MerchantReturnCode.MERCHANT_ACCOUNT_ID_ALREADY_EXISTS.getCode());
                    commonRet.setRetMsg(MerchantReturnCode.MERCHANT_ACCOUNT_ID_ALREADY_EXISTS.getDesc());
                    return commonRet;
                }

                MerchantDO merchantDO = new MerchantDO();
                BeanCopier beanCopier =
                        BeanCopier.create(MerchantEntity.class, MerchantDO.class, false);
                beanCopier.copy(merchantEntity, merchantDO, null);

                merchantDO.setModifiedBy(operator);
                merchantDO.setId(id);
                merchantDO.setState(null);//这里将状态置空，避免修改到状态
                merchantManager.update(merchantDO);

                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流平台货主-基本信息修改] 成功")
                        .addParm("货主信息", merchantDO)
                        .log();

                return commonRet;
            } catch (Exception e) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setErrorMsg("[物流平台货主-基本信息修改] 异常")
                        .setException(e)
                        .log();
                commonRet.reSet();
                commonRet.setRetCode(MerchantReturnCode.MERCHANT_UNKNOWN_ERROR.getCode());
                commonRet.setRetMsg(e.getMessage());
                return commonRet;
            } finally {
                distributedLock.realease(key);
            }
        } else {
            LogBetter.instance(LOGGER).
                    setLevel(LogLevel.ERROR).
                    setMsg("[物流平台货主-基本信息修改] 并发异常")
                    .log();
            commonRet.setRetCode(MerchantReturnCode.MERCHANT_CONCURRENT_EXCEPTION.getCode());
            commonRet.setRetMsg("并发异常");
            return commonRet;
        }
    }

    /**
     * 改变货主状态
     *
     * @param operator 操作人
     * @param id       ID
     * @param state    状态
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> changeMerchantState(
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
                    LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台货主-修改货主状态] 货主状态值不合法")
                            .addParm("state", state)
                            .log();
                    commonRet.setRetCode(MerchantReturnCode.MERCHANT_WRONG_STATE.getCode());
                    commonRet.setRetMsg("货主状态不合法");
                    return commonRet;
                }

                // 货主是否存在
                MerchantDO merchantDO = merchantManager.getById(id);
                if (merchantDO == null) {
                    LogBetter.instance(LOGGER)
                            .setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台货主-修改货主状态] 货主不存在")
                            .addParm("id", id)
                            .log();
                    commonRet.setRetCode(MerchantReturnCode.MERCHANT_NOT_EXIST.getCode());
                    commonRet.setRetMsg("货主不存在");
                    return commonRet;
                }

                //验证状态是否已修改过
                if (state.equals(merchantDO.getState())) {
                    LogBetter.instance(LOGGER)
                            .setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台货主-修改货主状态] 货主状态和目的状态一致")
                            .addParm("货主当前状态", merchantDO.getState())
                            .addParm("目的状态", state)
                            .log();
                    commonRet.setRetCode(MerchantReturnCode.MERCHANT_ALREADY_CHANGE_STATE.getCode());
                    commonRet.setRetMsg("货主已被" + MerchantStateType.valueOf(state).name);
                    return commonRet;
                }

                if (MerchantStateType.ENABLE.getValue().equals(state)) {
                    StringBuilder badMsg = new StringBuilder("货主启用失败:");
                    boolean canEnable = true;//是否能恢复状态为启用

                    //检查货主是否配置了供应商
                    if (!merchantProviderManager.isMerchantSetProvider(id)) {
                        canEnable = false;
                        badMsg.append("  未配置供应商  ");
                    }

                    //检查货主是否配置了支付方式
                    if (!merchantPayDeclareManager.isMerchantSetPayDeclare(id)) {
                        canEnable = false;
                        badMsg.append("  未配置申报方式  ");
                    }

                    //检查货主是否配置了包材
                    if (!merchantPackageMaterialManager.isMerchantSetPackageMaterial(id)){
                        canEnable = false;
                        badMsg.append("  未配置包材  ");
                    }

                    if (!canEnable) {
                        LogBetter.instance(LOGGER)
                                .setLevel(LogLevel.ERROR)
                                .setMsg("[物流平台货主-修改货主状态] 启用货主失败 缺少必要配置")
                                .addParm("失败信息", badMsg.toString())
                                .addParm("货主当前状态", merchantDO.getState())
                                .addParm("目的状态", state)
                                .log();
                        commonRet.setRetCode(MerchantReturnCode.MERCHANT_ALREADY_START_USING_ERROR.getCode());
                        commonRet.setRetMsg(badMsg.toString());
                        return commonRet;
                    }
                }

                MerchantDO updateDO = new MerchantDO();
                updateDO.setId(id);
                updateDO.setModifiedBy(operator);
                updateDO.setState(state);
                merchantManager.update(updateDO);

                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流平台货主-修改货主状态] 成功")
                        .addParm("id", id)
                        .addParm("state", state)
                        .addParm("operator", operator)
                        .log();

                return commonRet;
            } catch (Exception e) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setErrorMsg("[物流平台货主-修改货主状态] 异常")
                        .setException(e)
                        .log();
                commonRet.reSet();
                commonRet.setRetCode(MerchantReturnCode.MERCHANT_UNKNOWN_ERROR.getCode());
                commonRet.setRetMsg(e.getMessage());
                return commonRet;
            } finally {
                distributedLock.realease(key);
            }
        } else {
            LogBetter.instance(LOGGER).
                    setLevel(LogLevel.ERROR).
                    setMsg("[物流平台货主-修改货主状态] 并发异常")
                    .log();
            commonRet.setRetCode(MerchantReturnCode.MERCHANT_CONCURRENT_EXCEPTION.getCode());
            commonRet.setRetMsg("并发异常");
            return commonRet;
        }

    }

    /**
     * 创建货主供应商
     *
     * @param operator               操作人
     * @param merchantProviderEntity 货主供应商实体
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<Long> createMerchantProvider(
            @ParamNotBlank("操作人不能为空") String operator,
            @ParamNotBlank("货主供应商实体不能为空") MerchantProviderEntity merchantProviderEntity) {
        CommonRet<Long> commonRet = new CommonRet<Long>();
        try {
            //检查货主是否存在
            Long merchantId = merchantProviderEntity.merchantId;
            MerchantDO merchantDO = merchantManager.getById(merchantId);
            if (merchantDO == null) {
                LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                        .setMsg("[物流平台货主-创建货主供应商] 货主不存在")
                        .addParm("merchantId", merchantId)
                        .log();
                commonRet.setRetCode(MerchantReturnCode.MERCHANT_NOT_EXIST.getCode());
                commonRet.setRetMsg("货主不存在");
                return commonRet;
            }

            MerchantProviderDO merchantProviderDO = new MerchantProviderDO();
            BeanCopier beanCopier =
                    BeanCopier.create(MerchantProviderEntity.class, MerchantProviderDO.class, false);
            beanCopier.copy(merchantProviderEntity, merchantProviderDO, null);


            //初始化状态为启用
            merchantProviderDO.setState(MerchantProviderStateType.ENABLE.value);
            merchantProviderDO.setCreateBy(operator);

            merchantProviderManager.insert(merchantProviderDO);

            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[物流平台货主-创建货主供应商] 成功")
                    .addParm("货主供应商信息", merchantProviderDO)
                    .log();
            commonRet.setResult(merchantProviderDO.getId());
            return commonRet;
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台货主-创建货主供应商] 异常")
                    .setException(e)
                    .log();
            commonRet.reSet();
            commonRet.setRetCode(MerchantReturnCode.MERCHANT_PROVIDER_UNKNOWN_ERROR.getCode());
            commonRet.setRetMsg(e.getMessage());
            return commonRet;
        }
    }

    /**
     * 修改货主供应商基本信息
     *
     * @param operator               操作人
     * @param id                     主键
     * @param merchantProviderEntity 货主供应商实体
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> updateMerchantProvider(
            @ParamNotBlank("操作人不能为空") String operator,
            @ParamNotBlank("货主供应商ID不能为空") Long id,
            @ParamNotBlank("货主供应商实体不能为空") MerchantProviderEntity merchantProviderEntity) {

        CommonRet<Void> commonRet = new CommonRet<Void>();
        String key = UPDATE_KEY + "MERCHANTPROVIDER" + id;

        if (distributedLock.fetch(key)) {
            try {
                //判断货主供应商是否存在
                MerchantProviderDO checkDO = merchantProviderManager.getById(id);
                if (checkDO == null) {
                    LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台货主-修改货主供应商基本信息] 货主供应商不存在")
                            .addParm("id", id)
                            .log();
                    commonRet.setRetCode(MerchantReturnCode.MERCHANT_PROVIDER_NOT_EXIST.getCode());
                    commonRet.setRetMsg("货主供应商不存在");
                    return commonRet;
                }

                //检查货主是否存在
                Long merchantId = merchantProviderEntity.merchantId;
                MerchantDO merchantDO = merchantManager.getById(merchantId);
                if (merchantDO == null) {
                    LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台货主-修改货主供应商基本信息] 货主不存在")
                            .addParm("merchantId", merchantId)
                            .log();
                    commonRet.setRetCode(MerchantReturnCode.MERCHANT_NOT_EXIST.getCode());
                    commonRet.setRetMsg("货主不存在");
                    return commonRet;
                }

                MerchantProviderDO merchantProviderDO = new MerchantProviderDO();
                BeanCopier beanCopier =
                        BeanCopier.create(MerchantProviderEntity.class, MerchantProviderDO.class, false);
                beanCopier.copy(merchantProviderEntity, merchantProviderDO, null);

                merchantProviderDO.setModifiedBy(operator);
                merchantProviderDO.setId(id);
                merchantProviderDO.setState(null);//这里将状态置空，避免修改到状态
                merchantProviderManager.update(merchantProviderDO);

                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流平台货主-修改货主供应商基本信息] 成功")
                        .addParm("货主供应商信息", merchantProviderDO)
                        .log();
                return commonRet;
            } catch (Exception e) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setErrorMsg("[物流平台货主-修改货主供应商基本信息] 异常")
                        .setException(e)
                        .log();
                commonRet.reSet();
                commonRet.setRetCode(MerchantReturnCode.MERCHANT_PROVIDER_UNKNOWN_ERROR.getCode());
                commonRet.setRetMsg(e.getMessage());
                return commonRet;
            } finally {
                distributedLock.realease(key);
            }
        } else {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台货主-修改货主供应商基本信息] 并发异常")
                    .log();
            commonRet.setRetCode(MerchantReturnCode.MERCHANT_CONCURRENT_EXCEPTION.getCode());
            commonRet.setRetMsg("并发异常");
            return commonRet;
        }
    }

    /**
     * 修改货主供应商状态
     *
     * @param operator 操作人
     * @param id       主键
     * @param state    状态值
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> changeMerchantProviderState(
            @ParamNotBlank("操作人不能为空") String operator,
            @ParamNotBlank("货主供应商ID不能为空") Long id,
            @ParamNotBlank("状态值不能为空") String state) {
        CommonRet<Void> commonRet = new CommonRet<Void>();
        String key = CHANGE_STATE_KEY + "MERCHANTPROVIDER" + id;
        if (distributedLock.fetch(key)) {
            try {
                // 状态值是否合法
                if (!MerchantProviderStateType.ENABLE.getValue().equals(state)
                        && !MerchantProviderStateType.DISABLE.getValue().equals(state)) {
                    LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台货主-修改货主供应商状态] 货主供应商状态值不合法")
                            .addParm("state", state)
                            .log();
                    commonRet.setRetCode(MerchantReturnCode.MERCHANT_PROVIDER_WRONG_STATE.getCode());
                    commonRet.setRetMsg("货主供应商状态值不合法");
                    return commonRet;
                }

                // 货主供应商是否存在
                MerchantProviderDO checkDO = merchantProviderManager.getById(id);
                if (checkDO == null) {
                    LogBetter.instance(LOGGER)
                            .setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台货主-修改货主供应商状态] 货主供应商不存在")
                            .addParm("id", id)
                            .log();
                    commonRet.setRetCode(MerchantReturnCode.MERCHANT_PROVIDER_NOT_EXIST.getCode());
                    commonRet.setRetMsg("货主供应商不存在");
                    return commonRet;
                }

                // 验证状态是否已修改过
                if (state.equals(checkDO.getState())) {
                    LogBetter.instance(LOGGER)
                            .setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台货主-修改货主供应商状态] 货主供应商状态和目的状态一致")
                            .addParm("货主供应商当前状态", checkDO.getState())
                            .addParm("目的状态", state)
                            .log();
                    commonRet.setRetCode(MerchantReturnCode.MERCHANT_PROVIDER_ALREADY_CHANGE_STATE.getCode());
                    commonRet.setRetMsg("货主供应商已被" + MerchantStateType.valueOf(state).name);
                    return commonRet;
                }

                MerchantProviderDO updateDO = new MerchantProviderDO();
                updateDO.setId(id);
                updateDO.setModifiedBy(operator);
                updateDO.setState(state);

                merchantProviderManager.update(updateDO);

                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流平台货主-修改货主供应商状态] 成功")
                        .addParm("id", id)
                        .addParm("state", state)
                        .addParm("operator", operator)
                        .log();

                return commonRet;
            } catch (Exception e) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setErrorMsg("[物流平台货主-修改货主供应商状态] 异常")
                        .setException(e)
                        .log();
                commonRet.reSet();
                commonRet.setRetCode(MerchantReturnCode.MERCHANT_PROVIDER_UNKNOWN_ERROR.getCode());
                commonRet.setRetMsg(e.getMessage());
                return commonRet;
            } finally {
                distributedLock.realease(key);
            }
        } else {
            LogBetter.instance(LOGGER).
                    setLevel(LogLevel.ERROR).
                    setMsg("[物流平台货主-修改货主供应商状态] 并发异常")
                    .log();
            commonRet.setRetCode(MerchantReturnCode.MERCHANT_CONCURRENT_EXCEPTION.getCode());
            commonRet.setRetMsg("并发异常");
            return commonRet;
        }
    }


    /**
     * 添加供应商线路
     *
     * @param operator                       操作人
     * @param merchantProviderLineEntityList 供应商线路集合
     * @return
     */
    @Override
    @MethodParamValidate
    @Transactional
    public CommonRet<Void> addMerchantProviderLine(
            @ParamNotBlank String operator,
            @ParamNotBlank List<MerchantProviderLineEntity> merchantProviderLineEntityList) {
        CommonRet<Void> commonRet = new CommonRet<Void>();
        try {
            Validator validator = new Validator();
            for (MerchantProviderLineEntity merchantProviderLineEntity : merchantProviderLineEntityList) {
                //校验实体信息
                List<ConstraintViolation> violations = validator.validate(merchantProviderLineEntity);
                if (violations != null && violations.size() > 0) {
                    LogBetter.instance(LOGGER)
                            .setLevel(LogLevel.ERROR)
                            .setErrorMsg("[物流平台货主-添加供应商线路] 实体校验失败！")
                            .addParm("merchantProviderLineEntity", merchantProviderLineEntity)
                            .addParm("失败信息", violations.toString())
                            .log();
                    //校验失败，直接返回 并且事务回归
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    commonRet.reSet();
                    commonRet.setRetCode(MerchantReturnCode.MERCHANT_PROVIDER_LINE_ENTITY_VALIDATE_FAIL.getCode());
                    commonRet.setRetMsg(MerchantReturnCode.MERCHANT_PROVIDER_LINE_ENTITY_VALIDATE_FAIL.getDesc());
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return commonRet;
                }

                MerchantProviderLineDO merchantProviderLineDO = new MerchantProviderLineDO();
                BeanCopier beanCopier =
                        BeanCopier.create(MerchantProviderLineEntity.class, MerchantProviderLineDO.class, false);
                beanCopier.copy(merchantProviderLineEntity, merchantProviderLineDO, null);

                //赋初识值
                merchantProviderLineDO.setState(MerchantProviderLineStateType.ENABLE.value);
                merchantProviderLineDO.setCreateBy(operator);

                merchantProviderLineManager.insertOrUpdate(merchantProviderLineDO);
            }

            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台货主-添加供应商线路] 成功！")
                    .addParm("merchantProviderLineEntityList", merchantProviderLineEntityList)
                    .log();

            return commonRet;
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台货主-添加供应商线路] 异常")
                    .setException(e)
                    .log();
            commonRet.reSet();
            commonRet.setRetCode(MerchantReturnCode.MERCHANT_PROVIDER_LINE_UNKNOWN_ERROR.getCode());
            commonRet.setRetMsg(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return commonRet;
        }
    }

    /**
     * 删除供应商线路
     *
     * @param operator 操作人
     * @param id       供应商线路关系主键ID
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> deleteMerchantProviderLine(
            @ParamNotBlank String operator,
            @ParamNotBlank Long id) {
        CommonRet<Void> commonRet = new CommonRet<Void>();
        try {
            merchantProviderLineManager.deleteById(id);
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台货主-删除供应商线路] 成功！")
                    .addParm("operator", operator)
                    .addParm("id", id)
                    .log();
            return commonRet;
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台货主-删除供应商线路] 异常")
                    .setException(e)
                    .log();
            commonRet.reSet();
            commonRet.setRetCode(MerchantReturnCode.MERCHANT_PROVIDER_LINE_UNKNOWN_ERROR.getCode());
            commonRet.setRetMsg(e.getMessage());
            return commonRet;
        }
    }

    /**
     * 改变供应商线路状态
     *
     * @param operator 操作人
     * @param id       供应商线路关系主键ID
     * @param state    状态
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> changeMerchantProviderLineState(
            @ParamNotBlank String operator,
            @ParamNotBlank Long id,
            @ParamNotBlank String state) {
        CommonRet<Void> commonRet = new CommonRet<Void>();
        String key = CHANGE_STATE_KEY + "MERCHANTPROVIDERLINE" + id;
        if (distributedLock.fetch(key)) {
            try {
                // 状态值是否合法
                if (!MerchantProviderLineStateType.ENABLE.getValue().equals(state)
                        && !MerchantProviderLineStateType.DISABLE.getValue().equals(state)) {
                    LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台货主-修改供应商线路] 状态值不合法")
                            .addParm("state", state)
                            .log();
                    commonRet.setRetCode(MerchantReturnCode.MERCHANT_PROVIDER_LINE_WRONG_STATE.getCode());
                    commonRet.setRetMsg("供应商线路状态值不合法");
                    return commonRet;
                }

                // 货主供应商线路是否存在
                MerchantProviderLineDO checkDO = merchantProviderLineManager.getById(id);
                if (checkDO == null) {
                    LogBetter.instance(LOGGER)
                            .setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台货主-修改供应商线路] 供应商线路不存在")
                            .addParm("id", id)
                            .log();
                    commonRet.setRetCode(MerchantReturnCode.MERCHANT_PROVIDER_LINE_NOT_EXIST.getCode());
                    commonRet.setRetMsg("供应商线路不存在");
                    return commonRet;
                }

                // 验证状态是否已修改过
                if (state.equals(checkDO.getState())) {
                    LogBetter.instance(LOGGER)
                            .setLevel(LogLevel.ERROR)
                            .setMsg("[物流平台货主-修改供应商线路] 供应商线路和目的状态一致")
                            .addParm("供应商线路当前状态", checkDO.getState())
                            .addParm("目的状态", state)
                            .log();
                    commonRet.setRetCode(MerchantReturnCode.MERCHANT_PROVIDER_LINE_ALREADY_CHANGE_STATE.getCode());
                    commonRet.setRetMsg("供应商线路已被" + MerchantStateType.valueOf(state).name);
                    return commonRet;
                }

                MerchantProviderLineDO updateDO = new MerchantProviderLineDO();
                updateDO.setId(id);
                updateDO.setModifiedBy(operator);
                updateDO.setState(state);

                merchantProviderLineManager.update(updateDO);

                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流平台货主-修改供应商线路] 成功")
                        .addParm("id", id)
                        .addParm("state", state)
                        .addParm("operator", operator)
                        .log();

                return commonRet;
            } catch (Exception e) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setErrorMsg("[物流平台货主-修改供应商线路] 异常")
                        .setException(e)
                        .log();
                commonRet.reSet();
                commonRet.setRetCode(MerchantReturnCode.MERCHANT_PROVIDER_LINE_UNKNOWN_ERROR.getCode());
                commonRet.setRetMsg(e.getMessage());
                return commonRet;
            } finally {
                distributedLock.realease(key);
            }
        } else {
            LogBetter.instance(LOGGER).
                    setLevel(LogLevel.ERROR).
                    setMsg("[物流平台货主-修改供应商线路] 并发异常")
                    .log();
            commonRet.setRetCode(MerchantReturnCode.MERCHANT_CONCURRENT_EXCEPTION.getCode());
            commonRet.setRetMsg("并发异常");
            return commonRet;
        }
    }

    /**
     * 配置货主申报方式
     *
     * @param merchantId                   货主ID
     * @param payType                      支付方式
     * @param merchantPayDeclareEntityList 货主申报实体集合
     * @return
     */
    @Override
    @Transactional
    @MethodParamValidate
    public CommonRet<Void> setMerchantPayDeclare(
            @ParamNotBlank Long merchantId,
            @ParamNotBlank String payType,
            @ParamNotBlank List<MerchantPayDeclareEntity> merchantPayDeclareEntityList) {
        CommonRet<Void> commonRet = new CommonRet<Void>();
        //逐个校验实体
        Validator validator = new Validator();
        for (MerchantPayDeclareEntity merchantPayDeclareEntity : merchantPayDeclareEntityList) {
            List<ConstraintViolation> violations = validator.validate(merchantPayDeclareEntity);
            if (violations != null && violations.size() > 0) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setErrorMsg("[物流平台货主-配置货主申报方式] 实体校验失败！")
                        .addParm("merchantProviderLineEntity", merchantPayDeclareEntity)
                        .addParm("merchantId", merchantId)
                        .addParm("payType", payType)
                        .addParm("失败信息", violations.toString())
                        .log();
                commonRet.reSet();
                commonRet.setRetCode(MerchantReturnCode.MERCHANT_PAY_DECLARE_ENTITY_VALIDATE_FAIL.getCode());
                commonRet.setRetMsg(MerchantReturnCode.MERCHANT_PAY_DECLARE_ENTITY_VALIDATE_FAIL.getDesc());
                return commonRet;
            }
        }

        //获取分布式乐观锁
        String key = SET_PAY_DECLARE_KE + merchantId + "_" + payType;
        if (distributedLock.fetch(key)) {
            try {
                BeanCopier beanCopier =
                        BeanCopier.create(MerchantPayDeclareEntity.class, MerchantPayDeclareDO.class, false);
                //添加新的申报配置
                List<MerchantPayDeclareDO> setSuccessList = new ArrayList<MerchantPayDeclareDO>();
                for (MerchantPayDeclareEntity merchantPayDeclareEntity : merchantPayDeclareEntityList) {
                    MerchantPayDeclareDO merchantPayDeclareDO = new MerchantPayDeclareDO();
                    beanCopier.copy(merchantPayDeclareEntity, merchantPayDeclareDO, null);
                    merchantPayDeclareDO.setPayType(payType);
                    merchantPayDeclareDO.setMerchantId(merchantId);

                    //如果申报配置已存在则跳过
                    if (merchantPayDeclareManager.checkMerchantPayDeclareIsExist(merchantId, payType, merchantPayDeclareEntity.portId)) {
                        continue;
                    }
                    merchantPayDeclareManager.insertOrUpdate(merchantPayDeclareDO);
                    setSuccessList.add(merchantPayDeclareDO);
                }

                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流平台货主-配置货主申报方式] 成功")
                        .addParm("merchantId", merchantId)
                        .addParm("payType", payType)
                        .addParm("成功数量", setSuccessList.size())
                        .addParm("merchantPayDeclareDOList", setSuccessList)
                        .log();
                return commonRet;
            } catch (Exception e) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setErrorMsg("[物流平台货主-配置货主申报方式] 异常")
                        .setException(e)
                        .log();
                commonRet.reSet();
                commonRet.setRetCode(MerchantReturnCode.MERCHANT_PAY_DECLARE_UNKNOWN_ERROR.getCode());
                commonRet.setRetMsg(e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return commonRet;
            } finally {
                distributedLock.realease(key);
            }
        } else {
            LogBetter.instance(LOGGER).
                    setLevel(LogLevel.ERROR).
                    setMsg("[物流平台货主-配置货主申报方式] 并发异常")
                    .log();
            commonRet.setRetCode(MerchantReturnCode.MERCHANT_CONCURRENT_EXCEPTION.getCode());
            commonRet.setRetMsg("并发异常");
            return commonRet;
        }
    }

    /**
     * 修改货主申报信息，不会修改货主、口岸、支付方式
     *
     * @param id             主键ID
     * @param declarePayType 申报支付类型
     * @param declareAccount 申报账号
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> updateMerchantPayDeclare(
            @ParamNotBlank Long id,
            @ParamNotBlank String declarePayType,
            @ParamNotBlank String declareAccount) {
        CommonRet<Void> commonRet = new CommonRet<Void>();

        try {

            MerchantPayDeclareDO merchantPayDeclareDO = new MerchantPayDeclareDO();
            //货主、口岸、支付方式置空，这不能被修改
            merchantPayDeclareDO.setDeclarePayType(declarePayType);
            merchantPayDeclareDO.setDeclareAccount(declareAccount);
            merchantPayDeclareDO.setId(id);
            merchantPayDeclareManager.update(merchantPayDeclareDO);

            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[物流平台货主-修改货主申报信息] 成功")
                    .addParm("id", id)
                    .addParm("declarePayType", declarePayType)
                    .addParm("declareAccount", declareAccount)
                    .log();
            return commonRet;
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台货主-修改货主申报信息] 异常")
                    .setException(e)
                    .log();
            commonRet.reSet();
            commonRet.setRetCode(MerchantReturnCode.MERCHANT_PAY_DECLARE_UNKNOWN_ERROR.getCode());
            commonRet.setRetMsg(e.getMessage());
            return commonRet;
        }
    }


    /**
     * 删除货主申报方式
     *
     * @param id 主键ID
     * @return void
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> deleteMerchantPayDeclare(@ParamNotBlank Long id) {
        CommonRet<Void> commonRet = new CommonRet<Void>();
        try {
            merchantPayDeclareManager.deleteById(id);
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[物流平台货主-删除货主申报方式] 成功")
                    .addParm("ID", id)
                    .log();
            return commonRet;
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台货主-删除货主申报方式] 异常")
                    .setException(e)
                    .log();
            commonRet.reSet();
            commonRet.setRetCode(MerchantReturnCode.MERCHANT_PAY_DECLARE_UNKNOWN_ERROR.getCode());
            commonRet.setRetMsg(e.getMessage());
            return commonRet;
        }
    }


    /**
     * 创建货主包材配置
     *
     * @param merchantPackageMaterialEntity 实体
     * @return id
     */
    @Override
    @MethodParamValidate
    public CommonRet<Long> createMerchantPackageMaterial(@ParamNotBlank MerchantPackageMaterialEntity merchantPackageMaterialEntity) {
        CommonRet<Long> commonRet = new CommonRet<Long>();
        try {
            //货主包材配置是否已存在
            if (merchantPackageMaterialManager
                    .checkMerchantPackageMaterialIsExist(null, merchantPackageMaterialEntity.merchantId, merchantPackageMaterialEntity.orderSource)) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setErrorMsg("[物流平台货主-创建货主包材配置] 货主包材配置已存在")
                        .addParm("merchantId", merchantPackageMaterialEntity.merchantId)
                        .addParm("orderSource", merchantPackageMaterialEntity.orderSource)
                        .log();
                commonRet.setRetCode(MerchantReturnCode.MERCHANT_PACKAGE_MATERIAL_ALREADY_EXISTS.getCode());
                commonRet.setRetMsg(MerchantReturnCode.MERCHANT_PACKAGE_MATERIAL_ALREADY_EXISTS.getDesc());
                return commonRet;
            }
            MerchantPackageMaterialDO merchantPackageMaterialDO = new MerchantPackageMaterialDO();
            BeanCopier beanCopier =
                    BeanCopier.create(MerchantPackageMaterialEntity.class, MerchantPackageMaterialDO.class, false);
            beanCopier.copy(merchantPackageMaterialEntity, merchantPackageMaterialDO, null);


            merchantPackageMaterialDO.setGmtCreate(new Date());
            merchantPackageMaterialManager.insertOrUpdate(merchantPackageMaterialDO);

            commonRet.setResult(merchantPackageMaterialDO.getId());
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[物流平台货主-创建货主包材配置] 成功")
                    .addParm("merchantPackageMaterialDO", merchantPackageMaterialDO)
                    .log();
            return commonRet;
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台货主-创建货主包材配置] 异常")
                    .setException(e)
                    .log();
            commonRet.reSet();
            commonRet.setRetCode(MerchantReturnCode.MERCHANT_PACKAGE_MATERIAL_UNKNOWN_ERROR.getCode());
            commonRet.setRetMsg(e.getMessage());
            return commonRet;
        }
    }

    /**
     * 修改货主包材配置
     *
     * @param id                            主键ID
     * @param merchantPackageMaterialEntity 实体
     * @return void
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> updateMerchantPackageMaterial(
            @ParamNotBlank Long id,
            @ParamNotBlank MerchantPackageMaterialEntity merchantPackageMaterialEntity) {

        CommonRet<Void> commonRet = new CommonRet<Void>();
        try {
            //货主包材配置是否已存在
            if (merchantPackageMaterialManager
                    .checkMerchantPackageMaterialIsExist(id, merchantPackageMaterialEntity.merchantId, merchantPackageMaterialEntity.orderSource)) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setErrorMsg("[物流平台货主-修改货主包材配置] 货主包材配置已存在")
                        .addParm("merchantId", merchantPackageMaterialEntity.merchantId)
                        .addParm("orderSource", merchantPackageMaterialEntity.orderSource)
                        .log();
                commonRet.setRetCode(MerchantReturnCode.MERCHANT_PACKAGE_MATERIAL_ALREADY_EXISTS.getCode());
                commonRet.setRetMsg(MerchantReturnCode.MERCHANT_PACKAGE_MATERIAL_ALREADY_EXISTS.getDesc());
                return commonRet;
            }

            MerchantPackageMaterialDO merchantPackageMaterialDO = new MerchantPackageMaterialDO();
            BeanCopier beanCopier =
                    BeanCopier.create(MerchantPackageMaterialEntity.class, MerchantPackageMaterialDO.class, false);
            beanCopier.copy(merchantPackageMaterialEntity, merchantPackageMaterialDO, null);

            merchantPackageMaterialDO.setId(id);
            merchantPackageMaterialManager.update(merchantPackageMaterialDO);

            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[物流平台货主-修改货主包材配置] 成功")
                    .addParm("merchantPackageMaterialDO", merchantPackageMaterialDO)
                    .log();
            return commonRet;
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台货主-修改货主包材配置] 异常")
                    .setException(e)
                    .log();
            commonRet.reSet();
            commonRet.setRetCode(MerchantReturnCode.MERCHANT_PACKAGE_MATERIAL_UNKNOWN_ERROR.getCode());
            commonRet.setRetMsg(e.getMessage());
            return commonRet;
        }
    }

    /**
     * 删除货主包材配置
     *
     * @param id 主键ID
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> deleteMerchantPackageMaterial(@ParamNotBlank Long id) {
        CommonRet<Void> commonRet = new CommonRet<Void>();
        try {
            merchantPackageMaterialManager.deleteById(id);

            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[物流平台货主-删除货主包材配置] 成功")
                    .addParm("id", id)
                    .log();
            return commonRet;
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流平台货主-删除货主包材配置] 异常")
                    .setException(e)
                    .log();
            commonRet.reSet();
            commonRet.setRetCode(MerchantReturnCode.MERCHANT_PACKAGE_MATERIAL_UNKNOWN_ERROR.getCode());
            commonRet.setRetMsg(e.getMessage());
            return commonRet;
        }
    }


}
