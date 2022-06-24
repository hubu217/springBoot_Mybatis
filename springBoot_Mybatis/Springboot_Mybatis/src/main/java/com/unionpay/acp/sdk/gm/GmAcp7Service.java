package com.unionpay.acp.sdk.gm;

import java.io.IOException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.unionpay.acp.sdk.HttpsUtil;
import com.unionpay.acp.sdk.SDKConstants;
import com.unionpay.acp.sdk.SDKUtil;
import com.unionpay.acp.sdk.SecureUtil;



/**
 * @ClassName Acp6Service
 * @Description 全渠道6.0接口服务类，接入商户集成请可以直接参考使用本类中的方法
 * @date 2020/03
 * 声明：以下代码只是为了方便接入方测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障
 */
public class GmAcp7Service {

	private final static Logger logger = Logger.getLogger(GmAcp7Service.class);
	private final static String ENCODING = "utf-8";
	
	/**
	 * 请求报文签名(使用配置文件中配置的私钥证书或者对称密钥签名)<br>
	 * 功能：对请求报文进行签名,并计算赋值certid,signature字段并返回<br>
	 * @param reqData 请求报文map<br>
	 * @return　签名后的map对象<br>
	 */
	public static Map<String, String> sign(Map<String, String> reqData) {
        return signByCertInfo(reqData, 
        		GmSDKConfig.getConfig().getSignCertPath(), 
        		GmSDKConfig.getConfig().getSignCertPwd());
	}
	
	/**
	 * 多证书签名(通过传入私钥证书路径和密码签名）<br>
	 * 功能：如果有多个商户号接入银联,每个商户号对应不同的证书可以使用此方法:传入私钥证书和密码(并且在acp_sdk.properties中 配置 acpsdk.singleMode=false)<br>
	 * @param reqData 请求报文map<br>
	 * @param certPath 签名私钥文件（带路径）<br>
	 * @param certPwd 签名私钥密码<br>
	 * @return　签名后的map对象<br>
	 */
	public static Map<String, String> signByCertInfo(Map<String, String> reqData, String certPath, 
			String certPwd) {

        Map<String, String> data = SDKUtil.filterBlank(reqData);

        if (SDKUtil.isEmpty(certPath) || SDKUtil.isEmpty(certPwd)) {
            logger.error("CertPath or CertPwd is empty");
            return data;
        }
        
        try {
			data.put(SDKConstants.param_signId, GmCertUtil.getCertIdByKeyStoreMap(certPath, certPwd));
			
			/*
			 * 1. 按version+appId+bizMethod+reqId+body排序。
			 * 2. 对1的结果sm3得到byte数组。
			 * 3. 对2的结果用16进制小写字符串表示。【注意必须是小写】
			 * 4. 对3的结果取byte数组。【注意不是16进制字符串转byte数组，而是当普通字符串转】
			 * 5. 对4的结果用私钥算签名，算法为sm3withsm2。
			 * 6. 对5的结果做base64，得到一个字符串就是签名。
			 */
			String stringData = getSignStr(data);
			logger.info("打印排序后待签名请求报文串（交易返回11验证签名失败时可以用来同正确的进行比对）:[" + stringData + "]");
			byte[] sm3 = SecureUtil.sm3(stringData.getBytes(ENCODING));
			String sm3Hex = SDKUtil.byteArrayToHexString(sm3).toLowerCase();
			logger.info("sm3（交易返回11验证签名失败可以用来同正确的进行比对）:[" + sm3Hex + "]");
			String sign = Base64.encodeBase64String(GmUtil.signSm3WithSm2(sm3Hex.getBytes(), 
            		GmCertUtil.getCertIdByKeyStoreMap(certPath, certPwd).getBytes(ENCODING),
            		GmCertUtil.getSignCertPrivateKey(certPath, certPwd)));
			data.put(SDKConstants.param_sign, sign);
			return data;
        } catch (Exception e) {
            logger.error("Sign Error", e);
            return data;
        }
	}

	private static String getSignStr(Map<String, String> data){
		return SDKConstants.param_version + SDKConstants.EQUAL + data.get(SDKConstants.param_version)
		+ SDKConstants.AMPERSAND + SDKConstants.param_appId + SDKConstants.EQUAL
		+ data.get(SDKConstants.param_appId) + SDKConstants.AMPERSAND + SDKConstants.param_bizMethod
		+ SDKConstants.EQUAL + data.get(SDKConstants.param_bizMethod) + SDKConstants.AMPERSAND
		+ SDKConstants.param_reqId + SDKConstants.EQUAL + data.get(SDKConstants.param_reqId)
		+ SDKConstants.AMPERSAND + SDKConstants.param_body + SDKConstants.EQUAL
		+ data.get(SDKConstants.param_body);
	}
	
    /**
	 * 验证签名<br>
	 * @param data 返回报文数据<br>
	 * @return true 通过 false 未通过<br>
	 */
	public static boolean validate(Map<String, String> data) {
        logger.info("验签处理开始");
        
        String certId = data.get(SDKConstants.param_signId);
        logger.info("对返回报文串验签使用的验签公钥序列号：[" + certId + "]");
        PublicKey verifyKey = GmCertUtil.getValidatePublicKey(certId);
        if(verifyKey == null) {
        	logger.error("未找到此序列号证书。");
            return false;
        }
        
        try {
            String stringSign = data.get(SDKConstants.param_sign);
        	if(SDKUtil.isEmpty(stringSign)) {
                logger.error("signature is null. verifyRsa2 fail.");
                return false;
        	}
            logger.info("签名原文：[" + stringSign + "]");
            String stringData = getSignStr(data);
            logger.info("待验签串：[" + stringData + "]");
            byte[] sm3 = SecureUtil.sm3(stringData.getBytes(ENCODING));
            String sm3Hex = SDKUtil.byteArrayToHexString(sm3).toLowerCase();
            logger.info("sm3结果:[" + sm3Hex + "]");
            boolean result = GmUtil.verifySm3WithSm2(sm3Hex.getBytes(ENCODING), 
            		certId.getBytes(ENCODING), 
            		Base64.decodeBase64(stringSign), 
            		verifyKey);
            logger.info("验证签名" + (result ? "成功" : "失败"));
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
	}

    /**
     * 获取应答报文中的加密公钥证书,并存储到本地,备份原始证书,并自动替换证书<br>
     * 更新成功则返回1，无更新返回0，失败异常返回-1<br>
     * @return
     */
    public static int updateEncryptCert(String strCert, String certType) {
        return SDKUtil.updateEncryptCert(strCert, certType);
    }

	/**
	 * 敏感信息加密并做base64(卡号，手机号，cvn2,有效期）<br>
	 * @param data 送 phoneNo,cvn2,有效期<br>
	 * @return 加密的密文<br>
	 */
	public static String encryptData(String data) {
        return GmAcpService.encryptData(data, ENCODING);
    }


	/**
	 * 敏感信息解密，使用配置文件acp_sdk.properties解密<br>
	 * @param base64EncryptedInfo 加密信息<br>
	 * @return 解密后的明文<br>
	 */
	public static String decryptData(String base64EncryptedInfo) {
        return GmAcpService.decryptData(base64EncryptedInfo, ENCODING);
    }
	
	/**
	 * 敏感信息解密,通过传入的私钥解密<br>
	 * @param base64EncryptedInfo 加密信息<br>
	 * @param certPath 私钥文件（带全路径）<br>
	 * @param certPwd 私钥密码<br>
	 * @return
	 */
	public static String decryptData(String base64EncryptedInfo, String certPath, 
			String certPwd) {
        return GmAcpService.decryptData(base64EncryptedInfo, certPath, certPwd, ENCODING);
	}
	
	/**
	 * 获取敏感信息加密证书的物理序列号<br>
	 * @return
	 */
	public static String getEncryptCertId(){
		return GmCertUtil.getEncryptCert().getCertId();
	}
	
	/**
	 * 功能：后台交易提交请求报文并接收同步应答报文<br>
	 * @param reqData 请求报文<br>
	 * @param reqUrl  请求地址<br>
	 * @return 应答http 200返回true ,其他false<br>
	 */
	public static Map<String,String> post(Map<String, String> reqData, String reqUrl) {
		if(reqData == null || reqUrl == null) {
			logger.error("null input");
			return null;
		}
		if(reqUrl.startsWith("https://") && !GmSDKConfig.getConfig().isIfValidateRemoteCert()) {
			reqUrl = "u" + reqUrl;
		}
		try{
			reqData.put("content-type", "application/json;charset=utf-8"); //全渠道验了必须准确的这个值，为了避免误修改，编码啥的就不从外部取了。
			reqData.put("Accept", "application/json"); 
			reqData.put("Accept-Charset", "utf-8"); 
			
			Map<String, String> headers = new HashMap<String, String>(reqData);
			String body = headers.remove(SDKConstants.param_body);
			logger.info("请求银联地址：" + reqUrl + "，请求头：" + headers.toString() + "，请求体：" + body);
			HttpsUtil.Response resp = HttpsUtil.postForResponse(reqUrl, body.getBytes(ENCODING), headers);
			if(resp == null || resp.body == null) {
				logger.error("post失败");
				return null;
			}
			String bodyString = new String(resp.body, resp.encoding);
			logger.info("应答头：" + resp.headers + "，应答体：" + bodyString);
			Map<String, String> result = new HashMap<String, String>(resp.headers);
			result.put(SDKConstants.param_body, bodyString);
			return result;
		} catch (Exception e) {
			logger.error("post失败：" + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 对字符串做base64<br>
	 * @param rawStr<br>
	 * @return<br>
	 * @throws IOException
	 */
	public static String base64Encode(String rawStr){
		return GmAcpService.base64Encode(rawStr, ENCODING);
	}

	/**
	 * 对字符串做base64<br>
	 * @param base64Str<br>
	 * @return<br>
	 * @throws IOException
	 */
	public static String base64Decode(String base64Str){
		return GmAcpService.base64Decode(base64Str, ENCODING);
	}
}
