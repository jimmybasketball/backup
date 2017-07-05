package com.sfebiz.supplychain.service.demo;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.aop.annotation.MethodParamValidate;
import com.sfebiz.supplychain.aop.annotation.ParamNotBlank;
import com.sfebiz.supplychain.export.demo.api.DemoApi;
import com.sfebiz.supplychain.export.common.entity.CommonRet;
import com.sfebiz.supplychain.export.demo.entity.DemoEntity;
import com.sfebiz.supplychain.export.common.entity.Void;
import com.sfebiz.supplychain.export.common.enums.SupplyChainReturnCode;
import com.sfebiz.supplychain.lock.DistributedLock;
import com.sfebiz.supplychain.persistence.base.demo.domain.DemoDO;
import com.sfebiz.supplychain.persistence.base.demo.manager.DemoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试用例
 *
 * @author liujc
 * @create 2017-06-29 18:32
 **/
@Service("demoApi")
public class DemoApiImpl implements DemoApi {

    private static Logger logger = LoggerFactory.getLogger(DemoApiImpl.class);

    @Resource
    private DemoManager demoManager;

    @Resource
    DataSourceTransactionManager transactionManager;

    @Resource
    DistributedLock distributedLock;

    private static final String UPDATEKEY = "demoApi_update";
    /**
     * 使用声明式注解切面事务，自动提交，出现异常手动回滚
     *
     * @param entity
     * @return
     */
    @Override
    @Transactional
    @MethodParamValidate
    public CommonRet<Long> addEntity(@ParamNotBlank("demoEntity不能为空") DemoEntity entity) {
        CommonRet<Long> ret = new CommonRet<Long>();
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[demo测试-新增] 开始")
                .addParm("DemoEntity", entity)
                .log();
        try {
            //具体业务逻辑
            DemoDO demoDO = new DemoDO();
            demoDO.setName(entity.name);

            demoManager.insert(demoDO);

            Long id = demoDO.getId();
            ret.setResult(id);

//            int i = 1 / 0;//模拟异常点

            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[demo测试-新增] 成功")
                    .addParm("ret", ret.toString())
                    .log();
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setException(e)
                    .setErrorMsg("[demo测试-新增] 异常")
                    .log();
            ret.reSet();
            ret.setRetCode(SupplyChainReturnCode.FAIL.code);
            ret.setRetMsg(e.getMessage());
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return ret;
    }

    /**
     * 使用代码编程式事务，手动控制事务开启和事务提交/回滚
     *
     * @param entity
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> updateEntity(
            @ParamNotBlank Long id,
            @ParamNotBlank DemoEntity entity) {
        CommonRet<Void> ret = new CommonRet<Void>();

        if (distributedLock.fetch(UPDATEKEY)) {
            //开始事务
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);//这里是定义事务传播行为
            TransactionStatus transactionStatus = transactionManager.getTransaction(def);//这里是初始化事务状态
            boolean isCommit = true;
            try {
                //具体业务逻辑
                DemoDO demoDO = new DemoDO();
                demoDO.setId(id);
                demoDO.setName(entity.name);
                demoManager.update(demoDO);

                //int i = 1/0;//模拟异常点

                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[demo测试-修改] 结束")
                        .addParm("DemoEntity", entity)
                        .addParm("ret", ret.toString())
                        .log();
            } catch (Exception e) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setException(e)
                        .setErrorMsg("[demo测试-修改] 异常")
                        .log();

                ret.reSet();
                ret.setRetCode(SupplyChainReturnCode.FAIL.code);
                ret.setRetMsg(e.getMessage());

                isCommit = false;
            } finally {
                distributedLock.realease(UPDATEKEY);
                if (isCommit) {
                    //提交事务
                    transactionManager.commit(transactionStatus);
                } else {
                    //回滚事务
                    transactionManager.rollback(transactionStatus);
                }
            }
        } else {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[demo测试-修改] 并发异常")
                    .log();
            ret.reSet();
            ret.setRetMsg("并发异常");
            ret.setRetCode(SupplyChainReturnCode.FAIL.code);
        }

        return ret;
    }

    @Override
    @Transactional
    public CommonRet<Void> deleteEntityById(Long id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public CommonRet<DemoEntity> getEntityById(Long id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public CommonRet<List<DemoEntity>> getEntityList() {
        return null;
    }

    /**
     * 测试一下利用aop做方法参数校验
     *
     * @param id
     * @param name
     * @param demoEntity
     * @return
     */
    @Override
    @MethodParamValidate
    public CommonRet<Void> validTest(
            @ParamNotBlank("id不能为空") Long id,
            @ParamNotBlank("name不能为空") String name,
            @ParamNotBlank("demoEntity不能为空") DemoEntity demoEntity) {

        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[demo测试-验证测试] 通过")
                .log();
        return new CommonRet<Void>();
    }
}
