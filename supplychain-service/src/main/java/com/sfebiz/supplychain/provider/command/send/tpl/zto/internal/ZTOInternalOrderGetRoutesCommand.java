package com.sfebiz.supplychain.provider.command.send.tpl.zto.internal;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.protocol.zto.ZTOInternalClient;
import com.sfebiz.supplychain.protocol.zto.internal.ZTOInternalOrderRoutesRequest;
import com.sfebiz.supplychain.protocol.zto.internal.ZTOInternalRouteNode;
import com.sfebiz.supplychain.protocol.zto.internal.ZTOInternalRouteResponse;
import com.sfebiz.supplychain.provider.command.send.tpl.TplOrderGetRoutesCommand;
import com.sfebiz.supplychain.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 主动查询ZTO国内段路由信息
 */
public class ZTOInternalOrderGetRoutesCommand extends TplOrderGetRoutesCommand {

    private static final String messageType = "mail.trace";

    @Override
    public boolean execute() {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg("[ZTO]路由信息查询")
                .addParm("订单ID", orderId)
                .log();
        if (!checkParam()) {
            return false;
        }
        
        if (MockConfig.isMocked("zto", "tplOrderGetRoutesCommand")) {

            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("MOCK获取中通订单路由")
                    .setParms(orderId)
                    .log();

            mockZTOInternalRouteResponse();
            return true;
        }
        
        try {
            ZTOInternalOrderRoutesRequest request = buildZtoRoutesRequest();
            //TODO 这个配置信息后续从diamond里面取
            String responseString = ZTOInternalClient.sendByPost(request,
                    orderId,
                    "",//url,
                    "",//interfaceKey,
                    "",//code,
                    messageType);

            return handleRouteResult(responseString);
        } catch (Exception e) {
            logger.error("查询路由信息异常", e);
        } catch (Throwable t) {
            logger.error("查询路由信息异常", t);
        }
        return false;
    }

    /**
     * 检查参数信息是否存在；
     *
     * @return
     */
    public boolean checkParam() {
        if (StringUtils.isBlank(orderId)) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("路由信息查询失败-订单为null").log();
            return false;
        }
        if (StringUtils.isEmpty(mailNo)) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("路由信息查询失败-运单号不存在")
                    .addParm("订单:", orderId)
                    .log();
            return false;
        }
        return true;
    }

    /**
     * 构建路由获取请求信息
     *
     * @return
     */
    private ZTOInternalOrderRoutesRequest buildZtoRoutesRequest() {
        ZTOInternalOrderRoutesRequest request = new ZTOInternalOrderRoutesRequest();
        request.setMailno(mailNo);
        return request;
    }

    /**
     * 处理请求结果
     *
     * @param responseString
     * @return
     * @throws Exception
     */
    private boolean handleRouteResult(String responseString) throws Exception {
        List<LogisticsUserRouteEntity> logisticsRouteEntities = new ArrayList<LogisticsUserRouteEntity>();
        if (StringUtils.isBlank(responseString)) {
            return false;
        }

        ZTOInternalRouteResponse response = null;
		try {
			response = JSON.parseObject(responseString,
					ZTOInternalRouteResponse.class);
		} catch (Exception e) {
			LogBetter.instance(logger).setLevel(LogLevel.WARN)
					.setMsg("[ZTO路由信息查询]：中通查询的物流信息解析异常")
					.addParm("订单ID", orderId)
					.addParm("中通路由查询接口返回", responseString).log();
		}
        if (response == null) {
            return false;
        }

        if (CollectionUtils.isEmpty(response.getTraces())) {
            return true;
        }

        for (ZTOInternalRouteNode routeNode : response.getTraces()) {
            LogisticsUserRouteEntity routeEntity = new LogisticsUserRouteEntity();
            routeEntity.content = routeNode.getRemark();
            routeEntity.position = routeNode.getAcceptAddress();
            routeEntity.orderId = orderId;
            routeEntity.mailNo = mailNo;
            if (StringUtils.isBlank(routeNode.getAcceptTime())) {
                logger.warn("[ZTO路由信息查询]：中通路由信息不合法，时间信息缺失，" + responseString);
                return false;
            }
            long eventTime = DateUtil.getDatetime(DateUtil.DEF_PATTERN, routeNode.getAcceptTime());
            routeEntity.eventTime = eventTime;
            logisticsRouteEntities.add(routeEntity);
        }

        this.setRoutes(logisticsRouteEntities);
        return true;
    }
    
    /**
     * mock空路由信息，如果需要，可以在方法里面添加数据
     */
    private void mockZTOInternalRouteResponse(){
    	
    }

    public static void main(String[] args) {
        String url = "http://partner.zto.cn/client/interface.php";
        String key = "WU46OKFJRI";
        String code = "1000014068";

        String mailNo = "358618346998";
        ZTOInternalOrderRoutesRequest request = new ZTOInternalOrderRoutesRequest();
        request.setMailno(mailNo);
        String responseString = ZTOInternalClient.sendByGet(request,
                null,
                url,
                key,
                code,
                messageType);
        System.out.println(responseString);
    }

}
