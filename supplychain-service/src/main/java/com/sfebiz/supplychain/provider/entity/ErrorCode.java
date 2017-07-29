package com.sfebiz.supplychain.provider.entity;

import com.alibaba.dubbo.common.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;


public class ErrorCode {

	public static Map<String,String> codeMap = new HashMap<String, String>();
	
	
	static{
		
		codeMap.put("S01", "非法的XML/JSON");
		codeMap.put("S02", "非法的数字签名");
		codeMap.put("S03", "非法的物流公司/仓库公司");
		codeMap.put("S04", "非法的通知类型");
		codeMap.put("S05", "非法的通知类容");
		codeMap.put("S06", "网络超时，请重试");
		codeMap.put("S07", "系统异常，请重试");
		codeMap.put("S08", "HTTP 状态异常（非200）");
		codeMap.put("S09", "返回报文为空");
		codeMap.put("S10", "找不到对应网关");
		codeMap.put("S11", "非法网关信息");
		codeMap.put("S12", "非法请求参数");
		codeMap.put("S13", "业务服务异常");
		codeMap.put("S14", "请求的服务未找到，请检查消息类型");
		
		codeMap.put("B0001", "查询失败");
		codeMap.put("B0002", "未知业务错误");
		codeMap.put("B0003", "仓库编码不正确");
		codeMap.put("B0004", "地址代码不正确");
		codeMap.put("B0005", "订单不存在");
		codeMap.put("B0006", "订单已关闭");
		codeMap.put("B0007", "转运商ID不正确");
		codeMap.put("B0008", "用户查询失败");
		codeMap.put("B0009", "用户身份证信息不存在");
		codeMap.put("B0010", "无法识别eventTarget");
		codeMap.put("B0011", "无法识别的仓库类型");
		codeMap.put("B0012", "无法识别logistics code");
		codeMap.put("B0013", "当前状态不允许获取运费");
		codeMap.put("B0014", "当前状态不能付款");

        codeMap.put("B0100", "当前订单状态不允许变更");
		codeMap.put("B0101", "订单状态已关闭");
		codeMap.put("B0102", "订单状态已出库");
		codeMap.put("B0103", "订单状态已发货");
		codeMap.put("B0104", "订单状态已收货");
		codeMap.put("B0105", "仓库未发货");

        codeMap.put("B0200", "订单已存在");
        codeMap.put("B0201", "订单重量未回传");

		codeMap.put("B2000", "包裹信息不存在");

		
		codeMap.put("B9999", "");
		codeMap.put("S9999", "");
		
	}
	
	
	public static String getErrorCode(String code){
		String result = codeMap.get(code);
		if(StringUtils.isEmpty(result)){
			result = "未知错误,错误代码：["+code+"]";
		}
		return result;
	}
}
