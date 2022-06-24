package com.unionpay.acp.demo.BS;

import com.unionpay.acp.sdk.SDKConstants;
import com.unionpay.acp.sdk.SDKUtil;
import com.unionpay.acp.sdk.gm.GmQrcService;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * 用途：GmDemo中用的的方法<br>
 * 日期： 2017-03<br>
 * 声明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障<br>
 */
public class GmDemoBase {
    private final static Logger logger = Logger.getLogger(GmDemoBase.class);

	//默认配置的是UTF-8
	public static String encoding = "UTF-8";

	//二维码报文版本号
	public static String version = "1.0.0";
	
	// 商户发送交易时间 格式:YYYYMMDDhhmmss
	public static String getCurrentTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	
	public static String getCurrentDate() {
		return new SimpleDateFormat("yyyyMMdd000000").format(new Date());
	}
	
	// AN8..40 商户订单号，不能含"-"或"_"
	public static String getOrderId() {
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	}
	
   /**
	 * 组装请求，返回报文字符串用于显示
	 * @param data
	 * @return
	 */
    public static String genHtmlResult(Map<String, String> data){

    	TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			String key = en.getKey(); 
			String value =  en.getValue();
			if("couponInfo".equals(key)||
				"payerInfo".equals(key)||
				"payeeInfo".equals(key)||
				"comInfo".equals(key)||
				"settleKey".equals(key)||
				"comInfoAddn".equals(key)||
				"settleKeyAddn".equals(key)){
				String decodedStr = GmQrcService.base64Decode(value, GmDemoBase.encoding);
				sf.append("<b>"+ key +"解base64后的值="+ decodedStr +"</br></b>");
			}
			if ("respCode".equals(key)) {
				sf.append("<b>" + key + SDKConstants.EQUAL + value + "</br></b>");
			} else {
				sf.append(key + SDKConstants.EQUAL + value + "</br>");
			}
		}
		return sf.toString();
    }

 	/**
	 * 组装收款方信息
	 * @param encoding 编码方式
	 * @return 用{}连接并base64后的收款方信息
	 */
	public static String getPayeeInfo(Map<String, String> payeeInfoMap,String encoding) {
		return formInfoBase64(payeeInfoMap,encoding);
    }
	
	/**
	 * 组装收款方信息(接入机构配置了敏感信息加密)
	 * @param encoding 编码方式
	 * @return 用{}连接并base64后的收款方信息
	 */
	public static String getPayeeInfoWithEncrpyt(Map<String, String> payeeInfoMap,String encoding) {
		return formInfoBase64WithEncrpyt(payeeInfoMap,encoding);
    }
	
	/**
     * 组装付款方信息
     * @param encoding 编码方式
     * @return 用{}连接并base64后的付款方信息
     */
	public static String getPayerInfo(Map<String, String> payarInfoMap, String encoding) {
		return formInfoBase64(payarInfoMap,encoding);
    }
	
	/**
     * 组装付款方信息(接入机构配置了敏感信息加密)
     * @param encoding 编码方式
     * @return 用{}连接并base64后的付款方信息
     */
	public static String getPayerInfoWithEncrpyt(Map<String, String> payarInfoMap, String encoding) {
		return formInfoBase64WithEncrpyt(payarInfoMap,encoding);
    }
	
	/**
     * 组装附加处理条件
     * @param encoding 编码方式
     * @return 用{}连接并base64后的附加处理条件
     */
	public static String getAddnCond(Map<String, String> addnCondMap,String encoding) {
		return formInfoBase64(addnCondMap,encoding);
    }
	
	/**
	 * 用{}连接并base64
	 * @param map
	 * @param encoding
	 * @return
	 */
	public static String formInfoBase64(Map<String, String> map,String encoding){
		StringBuffer sf = new StringBuffer();
        String info = sf.append(SDKConstants.LEFT_BRACE).append(SDKUtil.createLinkString(map, false, false, encoding)).append(SDKConstants.RIGHT_BRACE).toString();
        info = new String(GmQrcService.base64Encode(info, encoding));
        return info;
	}
	
	/**
	 * 用{}连接并base64(接入机构配置了敏感信息加密)
	 * @param map
	 * @param encoding
	 * @return
	 */
	public static String formInfoBase64WithEncrpyt(Map<String, String> map,String encoding){
		StringBuffer sf = new StringBuffer();
        String info = sf.append(SDKConstants.LEFT_BRACE).append(SDKUtil.createLinkString(map, false, false, encoding)).append(SDKConstants.RIGHT_BRACE).toString();
        info = GmQrcService.encryptData(info, encoding);
        return info;
	}
	
	/**
	 * 解析返回报文的payerInfo域，敏感信息不加密时使用：<br>
	 * @param payerInfoBase64<br>
	 * @param encoding<br>
	 * @return
	 */
	public static Map<String, String> parsePayerInfo(String payerInfoBase64, String encoding){
		String payerInfo = GmQrcService.base64Decode(payerInfoBase64,encoding);
		return SDKUtil.parseRespString(payerInfo);
	}
	
	/**
	 * 解析返回报文的payerInfo域，敏感信息不加密时使用：<br>
	 * @param payeeInfo<br>
	 * @param encoding<br>
	 * @return
	 */
	public static Map<String, String> parsePayeeInfo(String payeeInfo, String encoding){
		return parsePayerInfo(payeeInfo, encoding);
	}
	
	/**
	 * 解析返回报文的payerInfo域，敏感信息加密时使用：<br>
	 * @param payerInfoEnc<br>
	 * @param encoding<br>
	 * @return
	 */
	public static Map<String, String> parsePayerInfoEnc(String payerInfoEnc, String encoding){
		String payerInfo = GmQrcService.decryptData(payerInfoEnc, encoding);
		return SDKUtil.parseRespString(payerInfo);
	}
	
	/**
	 * 解析返回报文中的payeeInfo域，敏感信息加密时使用：<br>
	 * @param payeeInfo<br>
	 * @param encoding<br>
	 * @return
	 */
	public static Map<String, String> parsePayeeInfoEnc(String payeeInfo, String encoding){
		return parsePayerInfoEnc(payeeInfo, encoding);
	}
	
	/**
	 * 解析返回报文中的payerInfo域，敏感信息加密时使用，多证书方式。<br>
	 * @param payerInfoEnc<br>
	 * @param encoding<br>
	 * @return
	 */
	public static Map<String, String> parsePayerInfoEnc(String payerInfoEnc, String certPath, 
			String certPwd, String encoding){
		String payerInfo = GmQrcService.decryptData(payerInfoEnc, certPath, certPwd, encoding);
		return SDKUtil.parseRespString(payerInfo);
	}
	
	/**
	 * 解析返回报文中的payeeInfo域，敏感信息加密时使用，多证书方式。<br>
	 * @param payeeInfo<br>
	 * @param encoding<br>
	 * @return
	 */
	public static Map<String, String> parsePayeeInfoEnc(String payeeInfo, String certPath, 
			String certPwd, String encoding){
		return parsePayerInfoEnc(payeeInfo, certPath, certPwd, encoding);
	}
}
