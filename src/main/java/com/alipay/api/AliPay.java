package com.alipay.api;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alipay.api.conf.AlipayConfigure;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.google.gson.Gson;



public class AliPay {
    
	private static Log logger = LogFactory.getLog(AliPay.class);
	
	private static AlipayClient alipayClient;

    static{
    	
    	alipayClient = new DefaultAlipayClient(
    			AlipayConfigure.getAPI_URL(),
    			AlipayConfigure.getAPP_ID(),
    			AlipayConfigure.getAPP_PRIVATE_KEY(),
    			AlipayConfigure.getFORMAT(),
    			AlipayConfigure.getCHARSET(),
    			AlipayConfigure.getALIPAY_PUBLIC_KEY(),
    			AlipayConfigure.getSIGN_TYPE());
    }
    
    public static void initAliPayCustomClient(String serverUrl, String appId, String privateKey, String format,
            String charset, String alipayPulicKey, String signType){
    	
    	alipayClient = new DefaultAlipayClient(serverUrl, appId, privateKey, format, charset, alipayPulicKey,signType);
    }
    
    /**
     * 通过业务参数组装支付请求参数包括签名信息
     * @param bizContent 业务参数
     * @return
     * @throws AlipayApiException 
     */
    public static String buildAppPayInfo(Map<String, Object> bizContent) throws AlipayApiException {

    	logger.debug("##in## bizContent:="+new Gson().toJson(bizContent));
    	
    	AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
    	request.setNotifyUrl(AlipayConfigure.getNotify_url());
    	request.setBizContent(new Gson().toJson(bizContent));
        AlipayResponse ar = alipayClient.sdkExecute(request);
        
        logger.debug("##out## body:="+ar.getBody());
        return  ar.getBody();
    }
    
    /**
     * 转账接口
     * @param bizContent
     * @return
     * @throws AlipayApiException
     */
    public static AlipayFundTransToaccountTransferResponse toaccountTransfer(Map<String, Object> bizContent) throws AlipayApiException{
    	
    	logger.debug("##in## bizContent:="+new Gson().toJson(bizContent));
    	
    	AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
    	request.setBizContent(new Gson().toJson(bizContent));
    	AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
    	if(response.isSuccess()){
    		logger.info("##out## response:="+new Gson().toJson(response));
    		return response;
    	} else {
    		logger.error("##out## response:="+new Gson().toJson(response));
    		throw new AlipayApiException(response.getSubMsg());
    	}
    }
    
    /**
     * 验证签名
     * @param paramsMap 回传参数带签名
     * @return
     * @throws AlipayApiException 
     */
    public static boolean verifySign(Map<String, String> paramsMap) throws AlipayApiException {

        return AlipaySignature.rsaCheckV1(paramsMap, AlipayConfigure.getALIPAY_PUBLIC_KEY(),
        		AlipayConfigure.getCHARSET(), AlipayConfigure.getSIGN_TYPE());
    }
    
    public static String notifyResponse(Response res){
    	return res.getResponse();
    }
    
    public static enum Response {
    	SUCCESS("success"), FAILURE("failure");

		private String response;
    	private Response(String response) {
			this.response = response;
		}
		public String getResponse() {
			return response;
		}
		public void setResponse(String response) {
			this.response = response;
		}
    	
    }
}
