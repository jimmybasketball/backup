package com.sfebiz.supplychain.service.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.aop.annotation.MethodParamValidate;
import com.sfebiz.supplychain.aop.annotation.ParamNotBlank;
import com.sfebiz.supplychain.config.rz.RZConfig;
import com.sfebiz.supplychain.exposed.user.api.RealNameAuthenticationService;
import com.sfebiz.supplychain.exposed.user.enums.RNAType;
import com.sfebiz.supplychain.lock.Lock;
import com.sfebiz.supplychain.persistence.base.user.domain.UserAuthenticationDO;
import com.sfebiz.supplychain.persistence.base.user.domain.UserAuthenticationLogDO;
import com.sfebiz.supplychain.persistence.base.user.manager.UserAuthenticationLogManager;
import com.sfebiz.supplychain.persistence.base.user.manager.UserAuthenticationManager;
import com.sfebiz.supplychain.service.user.yhj.YhjRealNameAuthentication;
import com.sfebiz.supplychain.util.RegUtil;
import com.yiji.openapi.tool.YijifuGateway;
import com.yiji.openapi.tool.YijipayConstants;
import com.yiji.openapi.tool.util.Ids;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 实名认证服务实现
 * @author liujc [liujunchi@ifunq.com]
 * @date  2017/7/26 17:50
 */
@Service("realNameAuthenticationService")
public class RealNameAuthenticationServiceImpl implements RealNameAuthenticationService {

    private final static Logger log = LoggerFactory.getLogger(RealNameAuthenticationServiceImpl.class);

    @Resource
    private UserAuthenticationManager userAuthenticationManager;

    @Resource
    private UserAuthenticationLogManager userAuthenticationLogManager;

    @Resource
    private Lock lock;

    private ThreadPoolExecutor pool = new ThreadPoolExecutor(60, 60,
            120, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(500));


    @Override
    @MethodParamValidate
    public boolean rz(
            @ParamNotBlank("姓名不能为空") String name,
            @ParamNotBlank("身份证号不能为空") String idNo) {
        return rz(name, idNo, RNAType.YIHUIJIN.getValue());
    }


    @Override
    @MethodParamValidate
    public boolean rz(
            @ParamNotBlank("姓名不能为空") String name,
            @ParamNotBlank("身份证号不能为空") String idNo,
            String from) {
        LogBetter.instance(log)
                .setLevel(LogLevel.INFO)
                .setMsg("[用户实名认证] 开始")
                .addParm("name", name)
                .addParm("idNo", idNo)
                .addParm("from", from)
                .log();

        if (StringUtils.isBlank(from) || !RNAType.containValue(from)) {
            from = RNAType.YIHUIJIN.getValue();
        }
        //身份证正则判断
        boolean isIdNum = RegUtil.isIdNum(idNo);
        if (!isIdNum) {
            LogBetter.instance(log)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[用户实名认证] 身份证校验失败")
                    .addParm("name", name)
                    .addParm("idNo", idNo)
                    .addParm("from", from)
                    .log();
            return false;
        }
        long startTime = System.currentTimeMillis();
        boolean isNeedRealNameRequest = true;//是否需要请求三方服务
        boolean verifyFlag = false;//实名验证是否成功
        name = name.trim();
        idNo = idNo.trim();
        String realNameKey = buildKey(idNo);
        boolean lockKey = Boolean.FALSE;
        if (lock.fetch(realNameKey)) {
            try {

                //数据库查询
                UserAuthenticationDO queryDO = new UserAuthenticationDO();
                queryDO.setIdNo(idNo);
                BaseQuery<UserAuthenticationDO> query = new BaseQuery<UserAuthenticationDO>(queryDO);
                List<UserAuthenticationDO> list = userAuthenticationManager.query(query);
                if (CollectionUtils.isNotEmpty(list)) {
                    for (UserAuthenticationDO userAuthenticationDO : list) {
                        //系统中存在该身份证的有效实名认证信息
                        if (userAuthenticationDO.getVerifyFlag() == 1) {
                            //判断名字是否匹配
                            if (userAuthenticationDO.getName().equalsIgnoreCase(name)) {
                                verifyFlag = true;//验证成功
                            } else {
                                verifyFlag = false;//验证失败
                            }
                            isNeedRealNameRequest = false;//不需要请求三方服务
                            break;
                        } else {
                            if (userAuthenticationDO.getName().equals(name)
                                    && userAuthenticationDO.getIdNo().equals(idNo)) {
                                verifyFlag = false;
                                isNeedRealNameRequest = false;
                                break;
                            }
                        }
                    }
                }

                //请求第三方实名认证服务且处理结果
                if (isNeedRealNameRequest) {
                    //实名认证方式选择
                    if (RNAType.YIHUIJIN.getValue().equals(from)) {
                        //易汇金
                        verifyFlag = realNameAuthenticationYihuijin(idNo, name, from);
                    } else if (RNAType.YIJIFU.getValue().equals(from)) {
                        //易极付
                        verifyFlag = realNameAuthenticationYijifu(idNo, name, from);
                    }
                }
                return verifyFlag;
            } catch (Exception e) {
                LogBetter.instance(log)
                        .setLevel(LogLevel.ERROR)
                        .setErrorMsg("[用户实名认证] 异常")
                        .setException(e)
                        .log();
                return false;
            } finally {
                lock.realease(realNameKey);
            }
        } else {
            LogBetter.instance(log)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[用户实名认证] 并发异常")
                    .addParm("name", name)
                    .addParm("idNo", idNo)
                    .addParm("from", from)
                    .log();
            return false;
        }
    }

    /**
     * 易汇金实名认证
     *
     * @param idNo 身份证号
     * @param name 姓名
     * @param from 认证渠道
     * @return
     * @throws Exception
     */
    private boolean realNameAuthenticationYihuijin(String idNo, String name, String from) throws Exception {
        boolean verifyFlag = false;
        final String requestId = UUID.randomUUID().toString();
        //构建请求请求第三方
        final String resultStr = YhjRealNameAuthentication.rz(idNo, name, requestId);
        //异步 log表
        final String logIdNo = idNo;
        final String logName = name;
        final String logFrom = from;
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    saveRealNameAuthenticationLog(logIdNo, logName, requestId, resultStr, logFrom);
                } catch (Exception e) {
                    LogBetter.instance(log)
                            .setLevel(LogLevel.ERROR)
                            .setErrorMsg("[用户实名认证] 保存三方服务请求记录异常")
                            .setException(e)
                            .log();
                }
            }
        });

        if (StringUtils.isBlank(resultStr)) {//请求第三方异常，默认是实名认证放过
            log.warn("idNo:" + idNo + ",name:" + name + ",requestId:" + requestId + ",resultStr is null");
            return Boolean.TRUE;
        }

        Map<String, Object> rzMap = JSON.parseObject(resultStr, Map.class);
        if (MapUtils.isEmpty(rzMap)) {//请求第三方处理失败，默认是实名认证放过
            log.warn("idNo:" + idNo + ",name:" + name + ",requestId:" + requestId + ",resultStr:" + resultStr + ",but rzMap is empty");
            return Boolean.TRUE;
        }

        String resultStatus = (String) rzMap.get(YhjRealNameAuthentication.rzResultKey);
        //对方系统内部错误，默认通过，但不保存到实名认证表
        if (resultStatus.equals(YhjRealNameAuthentication.rzResultInnerErrorValue)) {
            log.info("idNo:" + idNo + ",name:" + name + ",requestId:" + requestId + " third status:" + resultStatus + ",verifyFlag:" + verifyFlag);
            return Boolean.TRUE;
        }

        if (resultStatus.equals(YhjRealNameAuthentication.rzResultPassValue) || resultStatus.equals(YhjRealNameAuthentication.rzResultNOAUTHValue)) {
            verifyFlag = Boolean.TRUE;
        }
        LogBetter.instance(log)
                .setLevel(LogLevel.INFO)
                .setMsg("[用户实名认证] 请求三方服务结果")
                .addParm("idNo", idNo)
                .addParm("name", name)
                .addParm("resultStatus", resultStatus)
                .addParm("resultStatus", resultStatus)
                .addParm("verifyFlag", verifyFlag)
                .log();
        //请求成功的结果入db
        Date now = new Date();
        UserAuthenticationDO userAuthenticationDO = new UserAuthenticationDO();
        userAuthenticationDO.setName(name);
        userAuthenticationDO.setIdNo(idNo);
        userAuthenticationDO.setGmtCreate(now);
        userAuthenticationDO.setGmtModified(now);
        userAuthenticationDO.setVerifyFlag(verifyFlag ? 1 : 0);
        userAuthenticationDO.setFeatures(resultStr);
        userAuthenticationDO.setVerifyChannel(from);

        userAuthenticationManager.insert(userAuthenticationDO);
        LogBetter.instance(log)
                .setLevel(LogLevel.INFO)
                .setMsg("[用户实名认证] 添加实名认证信息")
                .addParm("userAuthenticationDO", userAuthenticationDO)
                .log();
        return verifyFlag;
    }

    /**
     * 易极付实名认证
     *
     * @param idNo 身份证号
     * @param name 姓名
     * @param from 认证渠道
     * @return
     * @throws Exception
     */
    private boolean realNameAuthenticationYijifu(String idNo, String name, String from) throws Exception {
        boolean verifyFlag = false;
        // 易极付接口调用地址url
        String url = RZConfig.yiJiFuRz.url;
        // 易极付的创建交易的支付是由公司国内的账户向国际的账户付款
        // 实名认证的商户id
        String partnerIdQuery = RZConfig.yiJiFuRz.partnerid;
        // 实名认证的商户对应的KEY
        String key = RZConfig.yiJiFuRz.partnerkey;
        // 参数Map
        Map<String, String> map = new HashMap<String, String>();
        // 请求号(必填，并且每次请求都必须不同)
        map.put(YijipayConstants.ORDER_NO, Ids.oid());
        // 商户ID
        map.put(YijipayConstants.PARTNER_ID, partnerIdQuery);
        // 服务码
        map.put("service", "realNameQuery");
        // 服务版本
        map.put("version", "1.0");
        // 姓名
        map.put("realName", name);
        // 身份证号
        map.put("certNo", idNo);
        final String request = JSONObject.toJSONString(map);
        final String response = YijifuGateway.getOpenApiClientService().doPost(url, map, key);

        //异步 log表
        final String logIdNo = idNo;
        final String logName = name;
        final String logFrom = from;
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    saveRealNameAuthenticationLog(logIdNo, logName, request, response, logFrom);
                } catch (Exception e) {
                    LogBetter.instance(log)
                            .setLevel(LogLevel.ERROR)
                            .setErrorMsg("[用户实名认证] 保存三方服务请求记录异常")
                            .setException(e)
                            .log();
                }
            }
        });

        log.info("易极付实名认证：request " + request + "response " + response);
        JSONObject resJson = JSON.parseObject(response);
        if (resJson.containsKey("resultCode") && resJson.getString("resultCode").equals("EXECUTE_SUCCESS")
                && resJson.containsKey("realNameQueryResult") && resJson.getString("realNameQueryResult").equals("pass")) {
            verifyFlag = Boolean.TRUE;
        } else {
            verifyFlag = Boolean.FALSE;
        }
        //请求成功的结果入db
        Date now = new Date();
        UserAuthenticationDO userAuthenticationDO = new UserAuthenticationDO();
        userAuthenticationDO.setName(name);
        userAuthenticationDO.setIdNo(idNo);
        userAuthenticationDO.setGmtCreate(now);
        userAuthenticationDO.setGmtModified(now);
        userAuthenticationDO.setVerifyFlag(verifyFlag ? 1 : 0);
        userAuthenticationDO.setFeatures(response);
        userAuthenticationDO.setVerifyChannel(from);

        userAuthenticationManager.insert(userAuthenticationDO);
        LogBetter.instance(log)
                .setLevel(LogLevel.INFO)
                .setMsg("[用户实名认证] 添加实名认证信息")
                .addParm("userAuthenticationDO", userAuthenticationDO)
                .log();
        return verifyFlag;
    }


    /**
     * 保存请求第三方实名认证服务记录
     *
     * @param idNo
     * @param name
     * @param requestId
     * @param resultStr
     * @param from
     */
    private void saveRealNameAuthenticationLog(String idNo, String name, String requestId, String resultStr, String from) {
        UserAuthenticationLogDO logDO = new UserAuthenticationLogDO();
        logDO.setIdNo(idNo);
        logDO.setName(name);
        StringBuilder memoStringBuilder = new StringBuilder("");
        memoStringBuilder.append("requestId:")
                .append(requestId)
                .append(",resultStr:")
                .append(resultStr);
        logDO.setFeatures(memoStringBuilder.toString());
        logDO.setChannel(from);

        userAuthenticationLogManager.insert(logDO);
    }

    /**
     * 构建实名认证的key
     *
     * @param idNo
     * @return
     */
    private String buildKey(String idNo) {
        StringBuilder keyBuilder = new StringBuilder("realName_");
        keyBuilder.append("idNo_").append(idNo);
        return keyBuilder.toString();
    }

}
