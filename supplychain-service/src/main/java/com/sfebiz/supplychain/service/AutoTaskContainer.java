package com.sfebiz.supplychain.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.SupplychainConfig;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.exposed.stockout.enums.SubTaskType;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskStatus;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskType;
import com.sfebiz.supplychain.lock.Lock;
import com.sfebiz.supplychain.persistence.base.line.manager.LogisticsLineManager;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderTaskDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderTaskManager;
import com.sfebiz.supplychain.service.stockout.service.exception.ExceptionHandlerFactory;

import net.pocrd.entity.ServiceException;

@Component("autotask")
public class AutoTaskContainer implements ApplicationContextAware, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AutoTaskContainer.class);
    private static final String PROCESS_KEY = "AutoTaskContainer-Process";
    private static final String UPDATELOGISTICSNO_KEY = "AutoTaskContainer-Update";
    private static final String SYNCSKUWAREHOUSE_KEY = "SkuWarehouse-Sync";
    private static ApplicationContext applicationContext;
//    @Resource
//    private SkuWarehouseSyscManager skuWarehouseSyscManager;
    @Resource
    private Lock distributedLock;
//    @Resource
//    private ClearanceCompanyServiceImpl clearanceCompanyService;
//    @Resource
//    private PortBillDeclareManager portBillDeclareManager;
    @Resource
    private StockoutOrderManager stockoutOrderManager;
//    @Resource
//    private StockoutBizService stockoutBizService;
    @Resource
    private LogisticsLineManager logisticsLineManager;
    @Resource
    private StockoutOrderTaskManager stockoutOrderTaskManager;
//    @Resource
//    private GalOrderService galOrderService;
//    @Resource
//    private OrderCreateProcessor orderCreateProcessor;
    @Resource
    private ExceptionHandlerFactory exceptionHandlerFactory;
    
    @Resource
    private RouteService souteService;

    private String localIp;
    //key:消息TOPIC value:消息的发送者 失败消息重发根据TOPIC 查询producer
//    private Map<String, MessageProducer> msgProducerMap = new HashMap<String, MessageProducer>(4);

    @Override
    public void afterPropertiesSet() throws Exception {
        //获取本地ip
        localIp = HaitaoTraceLoggerFactory.getIp();

        if (StringUtils.isEmpty(localIp)) {
            throw new RuntimeException("Can't get local ip");
        }

        logger.info("local ip is " + localIp);
        
        LogBetter.instance(logger)
        .setLevel(LogLevel.INFO)
        .setMsg("====异常处理任务开始执行====")
        .log();

        /**
         * 启动轮询线程
         */
        AutoTaskContainerProcessThread autoTaskContainerProcessThread = new AutoTaskContainerProcessThread("AutoTaskContainerProcessThread-Process");
        autoTaskContainerProcessThread.start();

//        AutoTaskContainerUpdateThread autoTaskContainerUpdateThread = new AutoTaskContainerUpdateThread("AutoTaskContainerProcessThread-Update");
//        autoTaskContainerUpdateThread.start();

//        AutoTaskContainerSyncWarehouseThread autoTaskContainerSyncWarehouseThread = new AutoTaskContainerSyncWarehouseThread("AutoTaskContainerSyncWarehouseThread-Sync");
//        autoTaskContainerSyncWarehouseThread.start();

        /**
         * 重新发送失败消息
         */
//        AutoTaskContainerMsgResendThread autoTaskContainerMsgResendThread = new AutoTaskContainerMsgResendThread("AutoTaskContainerMsgResendThread-MsgResend");
//        autoTaskContainerMsgResendThread.start();

    }

    /**
     * 解决网络异常等造成的重试问题
     */
    public void process() {

        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("====进入自动轮询事件(process)====")
                .log();

        //加上分布式锁的判断，防止多个service重复扫表
        if (distributedLock.fetch(PROCESS_KEY, SupplychainConfig.getInstance().getProcessInterval().longValue() / 2)) {
            try {
                //0.处理创建出库单异常(出库单创建异常无需自动重试)
                //checkAndRecreateStockoutOrder();

                //1.处理出库单下发异常
                //checkAndResendCreateCommandFromTaskTable();

                //2. 尝试重发发货报文
                checkAndResendSenderCommandFromTaskTable();

                //6. 尝试重发损益确认接口(暂无)
                //checkAndResendGalOrderConfirm();

                //7. 尝试重试出库异常的订单(暂时不用)
                //checkAndRetryStockout();
                
                //8. 重新向快递100请求订阅
                retryRegistRoute();

            } finally {
                //释放分布是锁
                distributedLock.realease(PROCESS_KEY);
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setMsg("====结束自动轮询事件(process)====")
                        .log();
            }
        } else {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("====无法开始自动完成业务(process)因为业务已经被锁定====")
                    .log();
        }
    }

    /**
     * 从异常任务表中查询出需要重新创建出库单的任务
     */
    public void checkAndRecreateStockoutOrder() {
        //从任务表中找出创建出库单失败的DO单进行重试
        List<StockoutOrderTaskDO> orderTasks = new ArrayList<StockoutOrderTaskDO>();
        StockoutOrderTaskDO stockoutOrderTaskDO = new StockoutOrderTaskDO();
        stockoutOrderTaskDO.setTaskType(TaskType.CREATE_STOCKOUT_EXCEPTION.getValue());
        stockoutOrderTaskDO.setTaskState(TaskStatus.WAIT_HANDLE.getValue());
        BaseQuery<StockoutOrderTaskDO> taskQuery = BaseQuery.getInstance(stockoutOrderTaskDO);
        taskQuery.addLte("gmtCreate", getOneHoursAgoOnCurrentDate());
        taskQuery.addLte("excuteTime", new Date());
        List<StockoutOrderTaskDO> createOrderTasks = stockoutOrderTaskManager.query(taskQuery);
        if (CollectionUtils.isNotEmpty(createOrderTasks)) {
            orderTasks.addAll(createOrderTasks);
        }

        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("===开始处理所有需要重新创建出库单的订单====")
                .setParms(orderTasks.size())
                .log();

        //重新下单,改成同步执行，否则分布式锁没用
        for (StockoutOrderTaskDO orderTaskDO : orderTasks) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("===尝试重新创建出库单====")
                    .addParm("主订单ID", orderTaskDO.getBizId())
                    .log();
            try {
                exceptionHandlerFactory.getExceptionHandlerByExceptionType(orderTaskDO.getTaskType()).handle(orderTaskDO);
                Thread.sleep(500);
            } catch (Exception e) {
                LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                        .addParm("定时任务重新创建出库单失败,销售订单ID", orderTaskDO.getBizId())
                        .setMsg(e.getMessage())
                        .setException(e)
                        .log();
            }
        }
    }

    /**
     * 从异常任务表中查询出出库异常的任务
     */
    public void checkAndRetryStockout() {
        //从任务表中找出创建出库单失败的DO单进行重试
        List<StockoutOrderTaskDO> orderTasks = new ArrayList<StockoutOrderTaskDO>();
        StockoutOrderTaskDO stockoutOrderTaskDO = new StockoutOrderTaskDO();
        stockoutOrderTaskDO.setTaskType(TaskType.STOCKOUT_EXCEPTION.getValue());
        stockoutOrderTaskDO.setTaskState(TaskStatus.WAIT_HANDLE.getValue());
        BaseQuery<StockoutOrderTaskDO> taskQuery = BaseQuery.getInstance(stockoutOrderTaskDO);
        taskQuery.addLte("gmtCreate", getOneHoursAgoOnCurrentDate());
        taskQuery.addLte("excuteTime", new Date());
        List<StockoutOrderTaskDO> createOrderTasks = stockoutOrderTaskManager.query(taskQuery);
        if (CollectionUtils.isNotEmpty(createOrderTasks)) {
            orderTasks.addAll(createOrderTasks);
        }

        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("===开始处理所有出库异常的订单====")
                .setParms(orderTasks.size())
                .log();

        //重新下单,改成同步执行，否则分布式锁没用
        for (StockoutOrderTaskDO orderTaskDO : orderTasks) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("===尝试重试出库异常订单====")
                    .addParm("主订单ID", orderTaskDO.getBizId())
                    .log();
            try {
//                exceptionHandlerFactory.getExceptionHandlerByExceptionType(orderTaskDO.getTaskType()).handle(orderTaskDO);
                Thread.sleep(500);
            } catch (Exception e) {
                LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                        .addParm("定时任务重试出库异常订单失败,销售订单ID", orderTaskDO.getBizId())
                        .setMsg(e.getMessage())
                        .setException(e)
                        .log();
            }
        }
    }

    /**
     * 从异常任务表中查询出需要重试下发出库单的任务
     */
    protected void checkAndResendCreateCommandFromTaskTable() {
        //从任务表中找出下发出库单失败的DO单进行重试
        List<StockoutOrderTaskDO> orderTasks = new ArrayList<StockoutOrderTaskDO>();
        StockoutOrderTaskDO stockoutOrderTaskDO = new StockoutOrderTaskDO();
        stockoutOrderTaskDO.setTaskType(TaskType.STOCKOUT_CREATE_EXCEPTION.getValue());
        stockoutOrderTaskDO.setStockoutOrderState(StockoutOrderState.WAIT_STOCKOUT.getValue());
        stockoutOrderTaskDO.setTaskState(TaskStatus.WAIT_HANDLE.getValue());
        BaseQuery<StockoutOrderTaskDO> taskQuery = BaseQuery.getInstance(stockoutOrderTaskDO);
        taskQuery.addLte("gmtCreate", getOneHoursAgoOnCurrentDate());
        taskQuery.addNotIn("sub_task_type", SubTaskType.allSubTaskType());
        taskQuery.addLte("retryExcuteTime", new Date());
        List<StockoutOrderTaskDO> createOrderTasks = stockoutOrderTaskManager.query(taskQuery);
        if (CollectionUtils.isNotEmpty(createOrderTasks)) {
            orderTasks.addAll(createOrderTasks);
        }

        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("===开始处理所有需要重发出库报文的出库单====")
                .setParms(orderTasks.size())
                .log();

        //重新下单,改成同步执行，否则分布式锁没用
        for (StockoutOrderTaskDO orderTaskDO : orderTasks) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("===尝试重发出库报文====")
                    .addParm("主订单ID", orderTaskDO.getBizId())
                    .log();
            try {
                //处理异常任务
                exceptionHandlerFactory.getExceptionHandlerByExceptionType(orderTaskDO.getTaskType()).handle(orderTaskDO);
                Thread.sleep(500);
            } catch (Exception e) {
                LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                        .addParm("下发出库指令异常,销售订单ID", orderTaskDO.getBizId())
                        .setMsg(e.getMessage())
                        .setException(e)
                        .log();
            }
        }
    }

    /**
     * 从异常任务表中 处理下发发货指令失败的任务
     */
    protected void checkAndResendSenderCommandFromTaskTable() {
        List<StockoutOrderTaskDO> orderTasks = new ArrayList<StockoutOrderTaskDO>();
        StockoutOrderTaskDO stockoutOrderTaskDO = new StockoutOrderTaskDO();
        stockoutOrderTaskDO.setTaskType(TaskType.STOCKOUT_SEND_EXCEPTION.getValue());
        stockoutOrderTaskDO.setStockoutOrderState(StockoutOrderState.STOCKOUTING.getValue());
        stockoutOrderTaskDO.setTaskState(TaskStatus.WAIT_HANDLE.getValue());
        BaseQuery<StockoutOrderTaskDO> taskQuery = BaseQuery.getInstance(stockoutOrderTaskDO);
        taskQuery.addLte("gmtCreate", getOneHoursAgoOnCurrentDate());
        taskQuery.addNotIn("sub_task_type", SubTaskType.allSubTaskType());
        List<StockoutOrderTaskDO> sendOrderTasks = stockoutOrderTaskManager.query(taskQuery);
        orderTasks.addAll(sendOrderTasks);

        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("===开始处理所有需要重发发货报文的出库单====")
                .setParms(orderTasks.size())
                .log();

        for (StockoutOrderTaskDO taskOrderDO : orderTasks) {

            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("===尝试重发发货报文====")
                    .addParm("主订单ID", taskOrderDO.getBizId())
                    .log();
            try {
                exceptionHandlerFactory.getExceptionHandlerByExceptionType(taskOrderDO.getTaskType()).handle(taskOrderDO);
                Thread.sleep(500);
            } catch (Exception e) {
                LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                        .addParm("AUTO TASK 重发发货指令异常,销售订单ID", taskOrderDO.getBizId())
                        .setMsg(e.getMessage())
                        .setException(e)
                        .log();
            }
        }
    }

    /**
     * 重新发送损溢益确认接口
     */
    public void checkAndResendGalOrderConfirm() {
        List<StockoutOrderTaskDO> orderTaskDOList = stockoutOrderTaskManager.getAllFailDataByTaskType(TaskType.GAL_ORDER_CONFIRM_EXCEPTION.getValue());
        if (CollectionUtils.isNotEmpty(orderTaskDOList)) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("===开始处理所有需要重发确认命令的损益订单====")
                    .setParms(orderTaskDOList.size())
                    .log();

            for (StockoutOrderTaskDO stockoutOrderTaskDO : orderTaskDOList) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setMsg("===尝试重发损益订单确认报文====")
                        .addParm("损益单ID：", stockoutOrderTaskDO.getStockoutOrderId())
                        .log();
                try {
                    //galOrderService.sendGalOrderConfirm(stockoutOrderTaskDO.getStockoutOrderId());
                    stockoutOrderTaskDO.setTaskState(TaskStatus.HANDLE_SUCCESS.getValue());
                    stockoutOrderTaskManager.update(stockoutOrderTaskDO);
                    Thread.sleep(500);
                    //对接出库工作流后干掉
                    throw new ServiceException(SCReturnCode.PARAM_ILLEGAL_ERR, "[供应链-立即重试异常任务]: ");
                } catch (ServiceException e) {
                    logger.error(e.getMessage(), e);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 向快递100发起订阅
     */
    protected void retryRegistRoute() {
        StockoutOrderTaskDO stockoutOrderTaskDO = new StockoutOrderTaskDO();
        stockoutOrderTaskDO.setTaskType(TaskType.KD100_REGIST_EXCEPTION.getValue());
        stockoutOrderTaskDO.setTaskState(TaskStatus.WAIT_HANDLE.getValue());
        BaseQuery<StockoutOrderTaskDO> taskQuery = BaseQuery.getInstance(stockoutOrderTaskDO);
        List<StockoutOrderTaskDO> createOrderTasks = stockoutOrderTaskManager.query(taskQuery);
        if (CollectionUtils.isEmpty(createOrderTasks)) {
            return;
        }
        for (StockoutOrderTaskDO task : createOrderTasks) {
            if (null == task || StringUtils.isBlank(task.getBizId())) {
                LogBetter.instance(logger)
                .setLevel(LogLevel.WARN)
                .setMsg("[异常单重试自动任务:向快递100发起订阅]异常单bizId为空")
                .setParms(task)
                .log();
                continue;
            }
        	StockoutOrderDO stockoutOrder = stockoutOrderManager.getByBizId(task.getBizId());
        	if (null == stockoutOrder) {
        	    continue;
            }
        	boolean flag = false;
        	// 重调
        	try {
        	    
        	    CommonRet<Void> commonRet = souteService.registKD100Routes(task.getBizId(), task.getFeatures());
        	    if (null != commonRet && null != commonRet.getRetCode() && SCReturnCode.COMMON_SUCCESS.getCode() == commonRet.getRetCode()) {
        	        flag = true;
                }
                
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
        	if (flag) {
        	    // 修改taskStatus
        	    task.setTaskState(TaskStatus.HANDLE_SUCCESS.getValue());
        	    stockoutOrderTaskManager.update(task);
            }
        }
	}

    /**
     * 获取当前时间前一个小时的时间
     *
     * @return
     */
    private Date getOneHoursAgoOnCurrentDate() {
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.HOUR_OF_DAY, -1);
        return cal.getTime();
    }

    /**
     * 获取当前时间前一天的时间
     *
     * @return
     */
    private Date getOneDayAgoOnCurrentDate() {
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return cal.getTime();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    class AutoTaskContainerProcessThread extends Thread {

        public AutoTaskContainerProcessThread() {
            super();
        }

        public AutoTaskContainerProcessThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (SupplychainConfig.getInstance().getEnableProcess()
                            && (SupplychainConfig.getInstance().getProcessMachineList().size() == 0
                            || SupplychainConfig.getInstance().getProcessMachineList().contains(localIp))) {
                        process();
                    } else {
                        logger.info("AutoTaskContainerProcess Skip");
                    }
                } catch (Exception e) {
                    logger.error("AutoTaskContainerProcessThreadException", e);
                }

                try {
                    Thread.sleep(SupplychainConfig.getInstance().getProcessInterval());
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

}
