package com.sfebiz.supplychain.service.line;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.aop.annotation.MethodParamValidate;
import com.sfebiz.supplychain.aop.annotation.ParamNotBlank;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.common.enums.SupplyChainReturnCode;
import com.sfebiz.supplychain.exposed.line.api.LogisticsLineService;
import com.sfebiz.supplychain.exposed.line.entity.LogisticsLineEntity;
import com.sfebiz.supplychain.exposed.line.enums.LogisticsLineOperateStateType;
import com.sfebiz.supplychain.exposed.line.enums.LogisticsLineStateType;
import com.sfebiz.supplychain.lock.Lock;
import com.sfebiz.supplychain.persistence.base.line.domain.LogisticsLineDO;
import com.sfebiz.supplychain.persistence.base.line.manager.LogisticsLineManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 物流线路服务实现
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-11 14:08
 **/
@Service("logisticsLineService")
public class LogisticsLineServiceImpl implements LogisticsLineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogisticsLineServiceImpl.class);

    private static final String UPDATE_KEY = LogisticsLineServiceImpl.class + "UPDATE_KEY";

    private static final String CHANGE_STATE_KEY = LogisticsLineServiceImpl.class + "CHANGE_STATE_KEY";

    @Resource
    private Lock distributedLock;

    @Resource
    private LogisticsLineManager logisticsLineManager;


    /**
     * 创建物流线路
     *
     * @param logisticsLineEntity 线路实体
     * @return id
     */
    @Override
    @MethodParamValidate
    public CommonRet<Long> createLogisticsLine(@ParamNotBlank LogisticsLineEntity logisticsLineEntity) {
        CommonRet<Long> commonRet = new CommonRet<Long>();
        try {
            LogisticsLineDO logisticsLineDO = new LogisticsLineDO();
            BeanCopier beanCopier =
                    BeanCopier.create(LogisticsLineEntity.class, LogisticsLineDO.class, false);
            beanCopier.copy(logisticsLineEntity, logisticsLineDO, null);

            //初始化状态
            logisticsLineDO.setState(LogisticsLineStateType.ENABLE.value);//默认启用
            logisticsLineDO.setOperateState(LogisticsLineOperateStateType.NORMAL.value);//运营状态默认正常
            logisticsLineDO.setGmtCreate(new Date());
            //生成线路名
            String lineName = generateLineName(logisticsLineEntity);
            logisticsLineDO.setLogisticsLineNid(lineName);
            logisticsLineManager.insert(logisticsLineDO);
            commonRet.setResult(logisticsLineDO.getId());

            //TODO 是否需要将线路同步到缓存或者diamond

            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[物流线路-创建] 成功")
                    .addParm("线路信息", logisticsLineDO)
                    .log();
            return commonRet;
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[物流线路-创建] 异常")
                    .setException(e)
                    .log();
            commonRet.reSet();
            commonRet.setRetCode(SupplyChainReturnCode.FAIL.code);
            commonRet.setRetMsg(e.getMessage());
            return commonRet;
        }
    }

    /**
     * 修改物流线路
     *
     * @param id                  主键ID
     * @param logisticsLineEntity 线路实体
     * @return void
     */
    @Override
    public CommonRet<Void> updateLogisticsLine(
            @ParamNotBlank Long id,
            @ParamNotBlank LogisticsLineEntity logisticsLineEntity) {
        CommonRet<Void> commonRet = new CommonRet<Void>();
        String key = UPDATE_KEY + id;
        if (distributedLock.fetch(key)) {
            try {

                LogisticsLineDO logisticsLineDO = new LogisticsLineDO();
                BeanCopier beanCopier =
                        BeanCopier.create(LogisticsLineEntity.class, LogisticsLineDO.class, false);
                beanCopier.copy(logisticsLineEntity, logisticsLineDO, null);

                logisticsLineDO.setId(id);
                //生成线路名
                String lineName = generateLineName(logisticsLineEntity);
                logisticsLineDO.setLogisticsLineNid(lineName);
                //将状态置空，这里只涉及到基本信息修改
                logisticsLineDO.setState(null);
                logisticsLineDO.setOperateState(null);
                logisticsLineManager.update(logisticsLineDO);

                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[物流线路-修改] 成功")
                        .addParm("线路信息", logisticsLineDO)
                        .log();

                //TODO 是否需要将线路同步到缓存或者diamond

                return commonRet;
            } catch (Exception e) {
                LogBetter.instance(LOGGER)
                        .setLevel(LogLevel.ERROR)
                        .setErrorMsg("[物流线路-修改] 异常")
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
                    setMsg("[物流线路-修改] 并发异常")
                    .log();
            commonRet.setRetCode(SupplyChainReturnCode.FAIL.code);
            commonRet.setRetMsg("并发异常");
            return commonRet;
        }
    }

    /**
     * 修改物流线路状态
     *
     * @param id    主键ID
     * @param state 状态值
     * @return void
     */
    @Override
    public CommonRet<Void> changeLogisticsLineState(Long id, String state) {
        return null;
    }

    /**
     * 修改物流线路的运营状态
     *
     * @param id    主键ID
     * @param state 状态值
     * @return void
     */
    @Override
    public CommonRet<Void> changechangeLogisticsLineOpState(Long id, String state) {
        return null;
    }


    /**
     * 生成线路名 生成规则：服务类型-国家-仓库名-清关口岸-清关供应商-国际物流供应商-国内物流供应商
     *
     * @param logisticsLineEntity 线路实体
     * @return 线路名
     */
    private String generateLineName(LogisticsLineEntity logisticsLineEntity) {
        StringBuilder lineName = new StringBuilder("");
        if (logisticsLineEntity == null) {
            return lineName.toString();
        }
        lineName.append(logisticsLineEntity.serviceTypeName)
                .append("-")
                .append(logisticsLineEntity.nationName)
                .append("-")
                .append(logisticsLineEntity.warehouseName)
                .append("-")
                .append(logisticsLineEntity.portName);


        if (StringUtils.isNotBlank(logisticsLineEntity.clearanceLogisticsProviderName)) {
            lineName.append("-")
                    .append(logisticsLineEntity.clearanceLogisticsProviderName);
        }

        if (StringUtils.isNotBlank(logisticsLineEntity.getInternationalLogisticsProviderName())) {
            lineName.append("-")
                    .append(logisticsLineEntity.getInternationalLogisticsProviderName());
        }

        if (StringUtils.isNotBlank(logisticsLineEntity.getDomesticLogisticsProviderName())) {
            lineName.append("-")
                    .append(logisticsLineEntity.getDomesticLogisticsProviderName());
        }
        return lineName.toString();
    }
}
