package com.sfebiz.supplychain.provider.command.send.pay.yihuijin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.config.order.OrderConfig;
import com.sfebiz.supplychain.config.pay.PayConfig;
import com.sfebiz.supplychain.exposed.common.enums.PortBillState;
import com.sfebiz.supplychain.exposed.user.api.RealNameAuthenticationService;
import com.sfebiz.supplychain.persistence.base.port.manager.PortBillDeclareManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.protocol.pay.newyihuijin.*;
import com.sfebiz.supplychain.protocol.pay.yihuijin.CustomOrderBuilder;
import com.sfebiz.supplychain.protocol.pay.yihuijin.CustomsInfo;
import com.sfebiz.supplychain.protocol.pay.yihuijin.YihuijinSignUtils;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.pay.PayOrderDeclareCommand;
import com.sfebiz.supplychain.service.port.PortUtil;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.util.HttpUtil;
import com.sfebiz.supplychain.util.JSONUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * <p>
 * 易汇金支付支付单申报
 * </p>
 * User: yangfeilong Date: 16/12/21
 */
public class NewYihuijinPayOrderDeclareCommand extends PayOrderDeclareCommand {

	public static final String createServiceName = "hg/member";
	public static final String transferServiceName = "hg/transfer/order";
	public static final String payServiceName = "hg/pay/order";
	public static final String queryServiceName = "hg/member/account/query";
	public static final String serviceName = "customs/order";

	private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
	private StockoutOrderManager stockoutOrderManager;
	private RealNameAuthenticationService realNameAuthenticationService;

	@Override
	public boolean execute() {
		try {

			if (portBillDeclareDO == null || stockoutOrderBO == null || StringUtils.isBlank(payBillDeclareProviderNid)
					|| logisticsLineBO == null || logisticsLineBO.getPortBO() == null) {
				LogBetter.instance(logger).setLevel(LogLevel.INFO)
						.setMsg("易汇金支付支付单申报单参数缺失")
						.addParm("订单号：", stockoutOrderBO.getBizId())
						.log();
				return false;
			}
			if (PortBillState.SEND_SUCCESS.getValue().equals(portBillDeclareDO.getState())
					|| PortBillState.VERIFY_CALLBACK.getValue().equals(portBillDeclareDO.getState())) {
				return true;
			}

			boolean isMockAutoCreated = MockConfig.isMocked("yihuijinpay", "createCommand");
			if (isMockAutoCreated) {
				// 直接返回
				logger.info("MOCK：易汇金支付申报 采用MOCK实现");
				return mockPayBillDeclareSuccess();
			}

			realNameAuthenticationService = (RealNameAuthenticationService) CommandConfig.getSpringBean("realNameAuthenticationService");
			String buyerName = CommonUtil.getBuyerName(stockoutOrderBO);
			String buyerIdNo = CommonUtil.getBuyerIdNo(stockoutOrderBO);

			if (!OrderConfig.getIsSkipRealNameAuthentication(stockoutOrderBO.getBizId())
					&& !realNameAuthenticationService.rz(buyerName, buyerIdNo)) {
				LogBetter.instance(logger).setLevel(LogLevel.WARN)
						 .setMsg("[供应链报文-向易汇金支付下发支付单申报指令失败]购买人的实名认证失败！")
						 .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
						 .addParm("出库单ID", stockoutOrderBO.getId())
						 .addParm("购买人姓名", buyerName)
						 .log();
				createFailureMessage = "收货人的实名认证失败,购买人姓名:" + buyerName;
				return false;
			} else {
				LogBetter.instance(logger).setLevel(LogLevel.INFO)
						 .setMsg("[供应链报文-向易汇金支付下发支付单申报指令成功] 购买人的实名认证成功！")
						 .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
						 .addParm("出库单ID", stockoutOrderBO.getId())
						 .addParm("购买人姓名", buyerName)
						 .log();
			}

			String companyMemberId = PayConfig.getPayProviderMeta(stockoutOrderBO.getDeclarePayType()).get("memberId").toString();
			if (StringUtils.isBlank(companyMemberId)) {
				return false;
			}

			String usetMemberId = sendMessageCreateAccount();
			if (StringUtils.isBlank(usetMemberId)) {
				return false;
			}

			String balance = sendMessageQueryBalanece(companyMemberId);
			if (StringUtils.isBlank(balance)) {
				return false;
			}
			int totalprice = stockoutOrderDeclarePriceBO.getOrderActualPrice();
			if (Integer.valueOf(balance) - totalprice < 0) {
				LogBetter.instance(logger).setLevel(LogLevel.ERROR)
						.setMsg("易汇金余额不足")
						.addParm("包裹总金额", totalprice)
						.addParm("易汇金剩余金额", balance)
						.log();
				return false;
			}
			
			boolean flag = sendMessageTransferAmount(companyMemberId, usetMemberId, totalprice);
			if (!flag) {
				return false;
			}
			
			String serialNumber = sendMessagePqyYiHuiJin(usetMemberId, totalprice);
			if (StringUtils.isBlank(serialNumber)) {
				return false;
			}
			stockoutOrderManager = (StockoutOrderManager) CommandConfig.getSpringBean("stockoutOrderManager");
			stockoutOrderManager.updatePayNo(stockoutOrderBO.getId(), serialNumber);

			String request = buildDeclareRequest(serialNumber);
			boolean result = sendMessage2DeclarePayBill(request);
			return result;
		} catch (Exception e) {
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).addParm("易汇金支付支付单申报请求报文", e).setException(e).log();
		}
		return false;
	}

	private String buildCreateAccount() throws Exception {
		String merchantId = getMerchantId();
		String mobile = stockoutOrderBO.getBuyerBO().getBuyerTelephone();
		String email = "cqjren@126.com";
		String realname = stockoutOrderBO.getBuyerBO().getBuyerName();
		String idNum = stockoutOrderBO.getBuyerBO().getBuyerCertNo();
		String userType = "CUSTOMER";
		String bindPayment = "true";
		String accountType = "JOINT_ACCOUNT";
		String customerId = String.valueOf(stockoutOrderBO.getUserId());
		StringBuilder hmacSource = new StringBuilder();
		hmacSource.append(StringUtils.defaultString(merchantId))
				.append(StringUtils.defaultString(mobile, ""))
				.append(StringUtils.defaultString(email, ""))
				.append(StringUtils.defaultString(realname, ""))
				.append(StringUtils.defaultString(idNum, ""))
				.append(StringUtils.defaultString(userType, ""))
				.append(StringUtils.defaultString(bindPayment, ""))
				.append(StringUtils.defaultString(accountType, ""))
				.append(StringUtils.defaultString(customerId, ""));
		CreateAccount account = new CreateAccount();
		account.setMerchantId(merchantId);
		account.setMobile(mobile);
		account.setEmail(email);
		account.setRealname(realname);
		account.setIdNum(idNum);
		account.setUserType(userType);
		account.setBindPayment(bindPayment);
		account.setAccountType(accountType);
		account.setCustomerId(customerId);
		account.setHmac(signMd5(hmacSource.toString(), getMerchantKey()));
		return JSON.toJSONString(account);
	}

	private String buildTransfer(String companyMemberId, String usetMemberId, int totalprice) throws Exception {
		String merchantId = getMerchantId();
		String requestId = stockoutOrderBO.getBizId();
		String currency = "CNY";
		TransferAccount account = new TransferAccount();
		StringBuilder hmacSource = new StringBuilder();
		hmacSource.append(StringUtils.defaultString(merchantId))
				.append(StringUtils.defaultString(requestId, ""))
				.append(StringUtils.defaultString(companyMemberId, ""))
				.append(StringUtils.defaultString(usetMemberId, ""))
				.append(StringUtils.defaultString(String.valueOf(totalprice), ""))
				.append(StringUtils.defaultString(currency, ""));
		account.setMerchantId(merchantId);
		account.setRequestId(requestId);
		account.setFromMember(companyMemberId);
		account.setToMember(usetMemberId);
		account.setAmount(totalprice);
		account.setCurrency(currency);
		account.setHmac(signMd5(hmacSource.toString(), getMerchantKey()));
		return JSON.toJSONString(account);
	}


	private String buildQueryBalance(String companyMemberId) throws Exception {
		String merchantId = getMerchantId();
		QueryBalance balance = new QueryBalance();
		balance.setMerchantId(merchantId);
		balance.setMemberId(companyMemberId);
		
		StringBuilder hmacSource = new StringBuilder();
		hmacSource.append(StringUtils.defaultString(merchantId))
				.append(StringUtils.defaultString(companyMemberId, ""));
		balance.setHmac(signMd5(hmacSource.toString(), getMerchantKey()));
		
		return JSON.toJSONString(balance);
	}

	private String buildPqyYiHuiJin(String usetMemberId, int totalprice) throws Exception {
		String merchantId = getMerchantId();
		String requestId = stockoutOrderBO.getBizId();
		String currency = "CNY";
		StringBuilder hmacSource = new StringBuilder();
		hmacSource.append(StringUtils.defaultString(merchantId))
				.append(StringUtils.defaultString(requestId, ""))
				.append(StringUtils.defaultString(usetMemberId, ""))
				.append(StringUtils.defaultString(String.valueOf(totalprice), ""))
				.append(StringUtils.defaultString(currency, ""));
		List<ProductDetails> productDetails = new ArrayList<ProductDetails>();
//		List<StockoutOrderSkuDO> resultAfterFilter = CommonUtil.removeMixedSku(stockoutOrderSkuDOs);
//		List<StockoutOrderSkuDO> resultAfterMerge = CommonUtil.mergeStockoutOrderSku(resultAfterFilter, Boolean.FALSE);
		for (StockoutOrderDetailBO skuDO : stockoutOrderDetailBOList) {
			ProductDetails productDetail = new ProductDetails();
			productDetail.setName(skuDO.getSkuName());
			productDetail.setQuantity(skuDO.getQuantity());
			productDetail.setAmount(declarePriceDetailMap.get(skuDO.getSkuId()).getDeclarePrice().longValue());
			productDetail.setReceiver("上海牵趣网络科技有限公司");
			productDetail.setDescription(skuDO.getSkuName());
			productDetails.add(productDetail);
			hmacSource.append(StringUtils.defaultString(skuDO.getSkuName(), ""))
					.append(StringUtils.defaultString(String.valueOf(skuDO.getQuantity()), ""))
					.append(StringUtils.defaultString(declarePriceDetailMap.get(skuDO.getSkuId()).getDeclarePrice().toString(), ""))
					.append("上海牵趣网络科技有限公司")
					.append(StringUtils.defaultString(skuDO.getSkuName(), ""));
		}
		PayYiHuiJin payYiHuiJin = new PayYiHuiJin();
		payYiHuiJin.setMerchantId(merchantId);
		payYiHuiJin.setRequestId(requestId);
		payYiHuiJin.setPayerMember(usetMemberId);
		payYiHuiJin.setAmount(totalprice);
		payYiHuiJin.setCurrency(currency);
		payYiHuiJin.setProductDetails(productDetails);
		payYiHuiJin.setHmac(signMd5(hmacSource.toString(), getMerchantKey()));
		return JSON.toJSONString(payYiHuiJin);
	}

	/**
	 * 给易汇金发送创建账户信息
	 *
	 * @return
	 * @throws Exception
	 */
	private String sendMessageCreateAccount() {
		String requestBodyJsonString = "";
		try {
			String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);
			if (StringUtils.isNotBlank(url)) {
				url = url + createServiceName;
			}

			requestBodyJsonString = buildCreateAccount();

			String response = HttpUtil.postJsonFormByHttps(url, requestBodyJsonString);
			LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链报文-向易汇金支付下发创建账户指令]")
					.setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
					.addParm("请求报文", requestBodyJsonString)
					.addParm("回复报文", response)
					.log();

			if (StringUtils.isBlank(response)) {
				LogBetter.instance(logger).setLevel(LogLevel.ERROR).setMsg("易汇金创建用户账号反馈为空").log();
				return null;
			}

			JSONObject resJson = JSON.parseObject(response);
			if (resJson.containsKey("memberId")) {
				if ("SUCCESS".equalsIgnoreCase(resJson.getString("status"))) {
					return resJson.getString("memberId");
				}
			}
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).setMsg("易汇金创建用户账号失败").log();
		} catch (Exception e) {
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).setMsg("[供应链报文-向易汇金支付下发创建账户指令异常]")
					.addParm("请求报文", requestBodyJsonString)
					.setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
					.setException(e)
					.log();
		}
		return null;
	}

	/**
	 * 给易汇金发送查询余额信息
	 * 
	 * @param companyMemberId
	 *            会员id
	 * @return
	 * @throws Exception
	 */
	private String sendMessageQueryBalanece(String companyMemberId) {
		String requestBodyJsonString = "";
		try {
			String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);
			if (StringUtils.isNotBlank(url)) {
				url = url + queryServiceName;
			}
			requestBodyJsonString = buildQueryBalance(companyMemberId);

			String response = HttpUtil.postJsonFormByHttps(url, requestBodyJsonString);
			LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链报文-向易汇金支付下发查询指令]")
					.setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
					.addParm("请求报文", requestBodyJsonString)
					.addParm("回复报文", response)
					.log();

			if (StringUtils.isBlank(response)) {
				LogBetter.instance(logger).setLevel(LogLevel.ERROR).setMsg("易汇金查询余额反馈为空").log();
				return null;
			}

			JSONObject resJson = JSON.parseObject(response);
			if (resJson.containsKey("balance")) {
				return resJson.getString("balance");
			}
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).setMsg("易汇金查询余额失败").log();
		} catch (Exception e) {
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).setMsg("[供应链报文-向易汇金支付下发查询指令异常]")
					.addParm("请求报文", requestBodyJsonString)
					.setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
					.setException(e)
					.log();
		}
		return null;
	}

	/**
	 * 给易汇金发送转账信息
	 * 
	 * @param
	 * @return
	 */
	private boolean sendMessageTransferAmount(String companyMemberId, String usetMemberId, int totalprice) {
		String requestBodyJsonString = "";
		try {
			String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);
			if (StringUtils.isNotBlank(url)) {
				url = url + transferServiceName;
			}
			
			requestBodyJsonString = buildTransfer(companyMemberId, usetMemberId, totalprice);

			String response = HttpUtil.postJsonFormByHttps(url, requestBodyJsonString);
			LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链报文-向易汇金支付下发转账指令]")
					.setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
					.addParm("请求报文", requestBodyJsonString)
					.addParm("回复报文", response)
					.log();

			if (StringUtils.isBlank(response)) {
				LogBetter.instance(logger).setLevel(LogLevel.ERROR).setMsg("易汇金转账反馈为空").log();
				return false;
			}
			
			JSONObject resJson = JSON.parseObject(response);
			if (resJson.containsKey("merchantId") && resJson.containsKey("status")) {
				if("SUCCESS".equalsIgnoreCase(resJson.getString("status"))){
					return true;
				}
			}
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).setMsg("易汇金转账失败").log();
		} catch (Exception e) {
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).setMsg("[供应链报文-向易汇金支付下发转账指令异常]")
					.addParm("请求报文", requestBodyJsonString)
					.setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
					.setException(e)
					.log();
		}
		return false;
	}

	/**
	 * 给易汇金发送支付信息
	 * 
	 * @param usetMemberId
	 *            会员id
	 * @return
	 * @throws Exception
	 */
	private String sendMessagePqyYiHuiJin(String usetMemberId, int totalprice) {
		String requestBodyJsonString = "";
		try {
			String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);
			if (StringUtils.isNotBlank(url)) {
				url = url + payServiceName;
			}
			
			requestBodyJsonString = buildPqyYiHuiJin(usetMemberId, totalprice);

			String response = HttpUtil.postJsonFormByHttps(url, requestBodyJsonString);
			LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链报文-向易汇金支付下发支付单指令]")
					.setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
					.addParm("请求报文", requestBodyJsonString)
					.addParm("回复报文", response)
					.log();

			if (StringUtils.isBlank(response)) {
				LogBetter.instance(logger).setLevel(LogLevel.ERROR).setMsg("易汇金支付反馈为空").log();
				return null;
			}

			JSONObject resJson = JSON.parseObject(response);
			if (resJson.containsKey("serialNumber")) {
				if ("SUCCESS".equalsIgnoreCase(resJson.getString("status"))) {
					return resJson.getString("serialNumber");
				}
			}
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).setMsg("易汇金支付失败").log();
		} catch (Exception e) {
			LogBetter.instance(logger).setLevel(LogLevel.ERROR).setMsg("[供应链报文-向易汇金支付下发支付单指令异常]")
					.addParm("请求报文", requestBodyJsonString)
					.setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
					.setException(e)
					.log();
		}
		return null;
	}

	/**
	 * 获取商户号
	 *
	 * @return
	 * @throws Exception
	 */
	private String getMerchantId() throws Exception {
		String merchantIds = PayConfig.getECodeOnPayProvider(payBillDeclareProviderNid);
		String merchantId;
		if (StringUtils.isNotBlank(merchantIds) && merchantIds.contains(",")) {
			String[] merchantIdArray = merchantIds.split(",");
			merchantId = merchantIdArray[0].trim();
		} else {
			merchantId = merchantIds;
		}
		return merchantId;
	}

	/**
	 * 获取商户号对应的KEY
	 *
	 * @return
	 * @throws Exception
	 */
	private String getMerchantKey() throws Exception {
		String signKeys = PayConfig.getPayProviderInterfaceKey(payBillDeclareProviderNid);
		String signKey;
		if (StringUtils.isNotBlank(signKeys) && signKeys.contains(",")) {
			String[] signKeyArray = signKeys.split(",");
			signKey = signKeyArray[0].trim();
		} else {
			signKey = signKeys;
		}

		return signKey;
	}


	/**
	 * 获取商户号对应的KEY
	 *
	 * @return
	 * @throws Exception
	 */
	private String getMerchantKey(Long warehouseId) throws Exception {
		String signKeys = PayConfig.getPayProviderInterfaceKey(payBillDeclareProviderNid);
		Map<String, Object> signKeyMap = JSONUtil.parseJSONMessage(signKeys);
		String signKey;
		String signKeyS;
		if(signKeyMap.containsKey(warehouseId.toString())){
			signKeyS = (String)signKeyMap.get(warehouseId.toString());
		}else{
			signKeyS = (String)signKeyMap.get("DEFAULT");
		}
		if (StringUtils.isNotBlank(signKeys) && signKeys.contains(",")) {
			String[] signKeyArray = signKeyS.split(",");
			signKey = signKeyArray[0].trim();
		} else {
			signKey = signKeyS;
		}

		return signKey;
	}



	/**
	 * 构建易汇金待申报请求参数信息
	 *
	 * @return
	 * @throws Exception
	 */
	private String buildDeclareRequest(String joinPayNo) throws Exception {

		String merchantId = getMerchantId();
		CustomOrderBuilder builder = new CustomOrderBuilder(merchantId);
		builder.setPaySerialNumber(joinPayNo);
		CustomsInfo customsInfo = new CustomsInfo();

		//各个口岸的meta信息中需要填写在连连支付中的海关编码
		Map<String, Object> portMeta = JSONUtil.parseJSONMessage(logisticsLineBO.getPortBO().getMeta());
		if (portMeta.containsKey("yihuijin_eport_code") && StringUtils.isNotBlank(portMeta.get("yihuijin_eport_code").toString())) {
			customsInfo.setCustomsChannel(portMeta.get("yihuijin_eport_code").toString());
		} else {
			throw new IllegalArgumentException("易汇金支付在口岸元信息中的 yihuijin_eport_code 值不存在");
		}


		if (portMeta.containsKey("dxpId") && StringUtils.isNotBlank(portMeta.get("dxpId").toString())) {
			customsInfo.setDxpid(portMeta.get("dxpId").toString());
		} else {
			throw new IllegalArgumentException("易汇金支付在口岸元信息中的 yihuijin_eport_code 值不存在");
		}

		//报关金额 分
		customsInfo.setAmount((long) stockoutOrderDeclarePriceBO.getOrderActualPrice());
		//货款
		customsInfo.setGoodsAmount((long) stockoutOrderDeclarePriceBO.getGoodsTotalPrice());
		//税款
		customsInfo.setTax((long) stockoutOrderDeclarePriceBO.getTaxFee());
		//运费
		customsInfo.setFreight((long) stockoutOrderDeclarePriceBO.getFreightFee());

		//电商在海关的企业备案编码
		customsInfo.setMerchantCommerceCode(PortUtil.getCustomsCode(logisticsLineBO));
		//电商在海关的企业备案名称
		customsInfo.setMerchantCommerceName(PortUtil.getCustomsName(logisticsLineBO));

		builder.addCustomsInfo(customsInfo);
		String notifyUrl = PayConfig.getPayProviderCallbackUrl(payBillDeclareProviderNid);
		builder.setNotifyUrl(notifyUrl);

		JSONObject request = builder.build(generateDeclareSign(merchantId, joinPayNo, notifyUrl, customsInfo));
		return request.toJSONString();
	}


	/**
	 * 生产易汇金支付申报签名key
	 *
	 * @param merchantId  商户ID
	 * @param joinPayNo   联名支付流水号
	 * @param notifyUrl   回调地址
	 * @param customsInfo 支付订单相关信息
	 * @return
	 */
	private String generateDeclareSign(String merchantId, String joinPayNo, String notifyUrl, CustomsInfo customsInfo) throws Exception {
		StringBuilder hmacSource = new StringBuilder();
		hmacSource.append(StringUtils.defaultString(merchantId)).append(StringUtils.defaultString(joinPayNo, "")).append(StringUtils.defaultString(notifyUrl, ""));
		hmacSource.append(StringUtils.defaultString(customsInfo.getCustomsChannel()))
				.append(ObjectUtils.defaultIfNull(customsInfo.getAmount(), ""))
				.append(ObjectUtils.defaultIfNull(customsInfo.getFreight(), ""))
				.append(ObjectUtils.defaultIfNull(customsInfo.getGoodsAmount(), ""))
				.append(ObjectUtils.defaultIfNull(customsInfo.getTax(), ""))
				.append(ObjectUtils.defaultIfNull(customsInfo.getMerchantCommerceName(), ""))
				.append(ObjectUtils.defaultIfNull(customsInfo.getMerchantCommerceCode(), ""))
				.append(ObjectUtils.defaultIfNull(customsInfo.getDxpid(), ""));
		String signKey = getMerchantKey();
		return YihuijinSignUtils.signMd5(hmacSource.toString(), signKey);
	}



	/**
	 * 给易汇金支付发送支付单申报信息
	 *
	 * @param requestBodyJsonString 请求体JSON字符串
	 * @return
	 */
	private boolean sendMessage2DeclarePayBill(String requestBodyJsonString) {
		try {
			String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);
			if (StringUtils.isNotBlank(url)) {
				url = url + serviceName;
			}

			String response = HttpUtil.postJsonByHttps(url, requestBodyJsonString);

			LogBetter.instance(logger)
					.setLevel(LogLevel.INFO)
					.setMsg("[供应链报文-向易汇金支付下发支付单申报指令]")
					.setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
					.addParm("请求报文", requestBodyJsonString)
					.addParm("回复报文", response)
					.log();

			if (StringUtils.isBlank(response)) {
				return false;
			}
			JSONObject resJson = JSON.parseObject(response);
			if (resJson.containsKey("status")) {
				String returnStatus = resJson.getString("status");
				String errorMessage = resJson.getString("error");
				PortBillDeclareManager portBillDeclareManager = (PortBillDeclareManager) CommandConfig.getSpringBean("portBillDeclareManager");
				if ("SUCCESS".equalsIgnoreCase(returnStatus)||errorMessage.contains("exception.record.exists")) {
					portBillDeclareDO.setState(PortBillState.SEND_SUCCESS.getValue());
					portBillDeclareDO.setResult(returnStatus);
					portBillDeclareDO.setBillSendTime(new Date());
					portBillDeclareManager.update(portBillDeclareDO);
					return true;
				} else {
					portBillDeclareDO.setState(PortBillState.PARAMS_EXCEPTION.getValue());
					portBillDeclareDO.setResult(returnStatus);
					portBillDeclareDO.setBillSendTime(new Date());
					portBillDeclareManager.update(portBillDeclareDO);
					setCreateFailureMessage(response);
				}
			}

		} catch (Exception e) {
			LogBetter.instance(logger)
					.setLevel(LogLevel.ERROR)
					.setMsg("[供应链报文-向易汇金支付下发支付单申报指令异常]")
					.addParm("请求报文", requestBodyJsonString)
					.setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
					.setException(e)
					.log();
		}
		return false;
	}

	/**
	 * md5签名
	 * @param source
	 * @param key
	 * @return
	 */
	private String signMd5(String source, String key) {
		byte k_ipad[] = new byte[64];
		byte k_opad[] = new byte[64];
		byte keyb[];
		byte value[];
		try {
			keyb = key.getBytes("UTF-8");
			value = source.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			keyb = key.getBytes();
			value = source.getBytes();
		}

		Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
		Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
		for (int i = 0; i < keyb.length; i++) {
			k_ipad[i] = (byte) (keyb[i] ^ 0x36);
			k_opad[i] = (byte) (keyb[i] ^ 0x5c);
		}

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		md.update(k_ipad);
		md.update(value);
		byte dg[] = md.digest();
		md.reset();
		md.update(k_opad);
		md.update(dg, 0, 16);
		dg = md.digest();
		return Hex.encodeHexString(dg);
	}
}
