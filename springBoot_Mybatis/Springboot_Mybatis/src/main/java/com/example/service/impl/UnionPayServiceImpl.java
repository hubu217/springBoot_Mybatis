package com.example.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.example.service.IUnionPayService;
import com.unionpay.acp.demo.BS.GmDemoBase;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.gm.GmQrcService;

/**
 * @Author:0xOO
 * @Date: 2018/9/26 0026
 * @Time: 15:23
 */


@Service
public class UnionPayServiceImpl implements IUnionPayService{
	
	
	
	
	
	
	

	@Override
	public Map<String, String> getUnionPayCode(Map<String, String> reqMap) {
		
		
		System.out.println("getUnionPayCode() 入参 reqMap="+JSON.toJSONString(reqMap));
		
		String issCode = reqMap.get("issCode");
		String accNo = reqMap.get("accNo");
		String name = reqMap.get("name");
		String mobile = reqMap.get("mobile");
		String cardAttr = reqMap.get("cardAttr");
		String payerBankInfo = reqMap.get("payerBankInfo");
		String acctClass = reqMap.get("acctClass");
		
		String pinFree = reqMap.get("pinFree");
		String maxAmont = reqMap.get("maxAmont");
		String addnOpUrl = reqMap.get("addnOpUrl");
		
		String couponInfo_type = reqMap.get("couponInfo_type");
		String couponInfo_id = reqMap.get("couponInfo_id");
		String backUrl = reqMap.get("backUrl");
		String emvCodeIn = reqMap.get("emvCodeIn");
		
		
		
		Map<String, String> contentData = new HashMap<String, String>();
		contentData.put("version", "1.0.0");
		contentData.put("reqType", "0210000903");
		contentData.put("issCode", issCode); 
		contentData.put("qrType", "35"); //35借记卡   51 贷记卡
		
		//付款方申码交易主键
		contentData.put("qrOrderNo", GmDemoBase.getOrderId()); 
		contentData.put("qrOrderTime", GmDemoBase.getCurrentTime());
		
		contentData.put("emvCodeIn", emvCodeIn); 
		
		//riskInfo必送，详细参考规范说明
		contentData.put("riskInfo", GmQrcService.base64Encode("{deviceID=123456999&deviceType=1&mobile=13525677809&accountIdHash=00000002&sourceIP=111.13.100.91}", GmDemoBase.encoding));
		
        Map<String, String> payerInfoMap = new HashMap<String, String>(); 
        payerInfoMap.put("accNo", accNo);
        if(null!=name && !"".equals(name)) {
        	  payerInfoMap.put("name", name);
        }	
        if(null!=payerBankInfo && !"".equals(payerBankInfo)) {
        	   payerInfoMap.put("payerBankInfo", payerBankInfo);
        } 
        if(null!=acctClass && !"".equals(acctClass)) {
        	   payerInfoMap.put("acctClass", acctClass);
        }
        payerInfoMap.put("cardAttr", cardAttr);//01 借记卡 02 贷记卡（含准贷记卡）
        payerInfoMap.put("mobile", mobile);//手机号必送
        
		//敏感信息不加密使用DemoBase.getPayerInfo方法
		//contentData.put("payerInfo", GmDemoBase.getPayerInfo(payerInfoMap,"UTF-8"));
		
		//如果对机构号issCode 配置了敏感信息加密，必须1.送encryptCertId 2.对payerInfo，payeeInfo的值整体加密
		//目前二维码系统要求所有接入均采用加密方式，使用正式机构号测试的时候参考如下方法上送
		
		contentData.put("payerInfo", GmDemoBase.getPayerInfoWithEncrpyt(payerInfoMap,"UTF-8"));
		contentData.put("encryptCertId", GmQrcService.getEncryptCertId());
		
		if(!"".equals(couponInfo_type) && !"".equals(couponInfo_id)){
				String couponInfoStr = "[{\"type\": \"" + couponInfo_type + "\", \"id\" : \"" + couponInfo_id + "\"}]"; //可替换使用第三方json库组json
				//付款方适配活动：付款方需要明确指出活动的出资方，允许以下三种情况，即付款方单独出资、付款方与银联共同出资、银联单独出资
				contentData.put("couponInfo", GmQrcService.base64Encode(couponInfoStr,"UTF-8"));
		}
		
        if( !("".equals(pinFree.trim()) && "".equals(maxAmont.trim()) && "".equals(addnOpUrl.trim())) ) {
	        	//pinFree maxAmont addnOpUrl 只要存在一个 则执行添加逻辑
	        	Map<String, String> addnCondMap = new HashMap<String, String>(); 
	            addnCondMap.put("currency", "156"); //金额币种
	            addnCondMap.put("pinFree", pinFree);//免密限额
	            addnCondMap.put("maxAmont", maxAmont);//最高交易金额
	        	contentData.put("addnCond", GmDemoBase.getAddnCond(addnCondMap,"UTF-8")); //附加处理条件
	    		contentData.put("addnOpUrl", addnOpUrl);//附加处理服务器地址
        }
        
		contentData.put("reqReserved", "reserved"+GmDemoBase.getOrderId());
		//c2b交易通知发送地址
		contentData.put("backUrl", backUrl);
		//国密改造
		contentData.put("signType", "03");
		
		System.out.println("【加密前】  contentData="+JSON.toJSONString(contentData));
		Map<String, String> reqData = GmQrcService.sign(contentData,GmDemoBase.encoding);			 //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String requestUrl = SDKConfig.getConfig().getQrcB2cIssBackTransUrl();
		
		
		
		System.out.println("【post前】reqData="+JSON.toJSONString(reqData)+"; requestUrl="+requestUrl+"; encoding="+GmDemoBase.encoding);
		Map<String, String> rspData = GmQrcService.post(reqData,requestUrl,GmDemoBase.encoding);  //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
		System.out.println("【post后】rspData="+JSON.toJSONString(rspData));
		
		
		String rspDataStr = JSON.toJSONString(rspData);
		System.out.println("【post后】rspDataStr="+rspDataStr);
		
		if(!rspData.isEmpty()){
			if(GmQrcService.validate(rspData, GmDemoBase.encoding)){
				System.out.println("验证签名成功");
				String respCode = rspData.get("respCode") ;
				if(!"00".equals(respCode)){
							return null;
					}
			}else{
					System.out.println("验证签名失败");
					return null;
			}
		}else{
				//未返回正确的http状态
				System.out.println("未获取到返回报文或返回http状态码非200");
		}
		
		
		
		 return rspData;
	}
	
	
	
	
	
   
    
    
    
}
