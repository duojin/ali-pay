package com.alipay.api;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alipay.api.conf.AlipayConfigure;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.google.gson.Gson;



public class AliPay2 {
    
	private static Log logger = LogFactory.getLog(AliPay2.class);
	
	private static AlipayClient defaultAlipayClient;

    static{
    	
    	defaultAlipayClient = new DefaultAlipayClient(
    			AlipayConfigure.getAPI_URL(),
    			AlipayConfigure.getAPP_ID(),
    			AlipayConfigure.getAPP_PRIVATE_KEY(),
    			AlipayConfigure.getFORMAT(),
    			AlipayConfigure.getCHARSET(),
    			AlipayConfigure.getALIPAY_PUBLIC_KEY(),
    			AlipayConfigure.getSIGN_TYPE());
    }
    
    /**
     * 通过业务参数组装支付请求参数包括签名信息
     * @param bizContent 业务参数
     * @return
     * @throws AlipayApiException 
     */
    public static String buildAppPayInfo(Map<String, Object> bizContent,AliPaySettings settings) throws AlipayApiException {

    	logger.debug("##in## bizContent:="+new Gson().toJson(bizContent));
    	AlipayClient alipayClient = getAlipayClient(settings);
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
    public static AlipayFundTransToaccountTransferResponse toaccountTransfer(Map<String, Object> bizContent,AliPaySettings settings) throws AlipayApiException{
    	
    	logger.debug("##in## bizContent:="+new Gson().toJson(bizContent));
    	AlipayClient alipayClient = getAlipayClient(settings);
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
     * 退款接口
     * @param bizContent
     * @return
     * @throws AlipayApiException
     */
    public static AlipayTradeRefundResponse refund(Map<String, Object> bizContent,AliPaySettings settings) throws AlipayApiException{
    	
    	logger.debug("##in## bizContent:="+new Gson().toJson(bizContent));
    	AlipayClient alipayClient = getAlipayClient(settings);
    	
    	AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
    	request.setBizContent(new Gson().toJson(bizContent));
    	AlipayTradeRefundResponse response = alipayClient.execute(request);
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
    public static boolean verifySign(Map<String, String> paramsMap,AliPaySettings settings) throws AlipayApiException {

    	if(settings==null){
            return AlipaySignature.rsaCheckV1(paramsMap, AlipayConfigure.getALIPAY_PUBLIC_KEY(),
            		AlipayConfigure.getCHARSET(), AlipayConfigure.getSIGN_TYPE());
    	}else{
            return AlipaySignature.rsaCheckV1(paramsMap, settings.getAlipayPublicKey(),
            		AlipayConfigure.getCHARSET(), AlipayConfigure.getSIGN_TYPE());
    	}
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
    

    private static AlipayClient getAlipayClient(AliPaySettings settings) {
    	AlipayClient alipayClient = defaultAlipayClient;
    	if(settings==null){
        	logger.info("使用默认配置===========");
    	}else{
    		alipayClient = new  DefaultAlipayClient(
        			AlipayConfigure.getAPI_URL(),
        			settings.getAppId(),
        			settings.getPrivateKey(),
        			AlipayConfigure.getFORMAT(),
        			AlipayConfigure.getCHARSET(),
        			settings.getAlipayPublicKey(),
        			AlipayConfigure.getSIGN_TYPE());
    	}
    	return alipayClient;
	}

}
