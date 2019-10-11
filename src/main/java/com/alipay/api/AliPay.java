package com.alipay.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.alipay.api.request.*;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alipay.api.conf.AlipayConfigure;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
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
     * 退款接口
     * @param bizContent
     * @return
     * @throws AlipayApiException
     */
    public static AlipayTradeRefundResponse refund(Map<String, Object> bizContent) throws AlipayApiException{
    	
    	logger.debug("##in## bizContent:="+new Gson().toJson(bizContent));
    	
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
    public static boolean verifySign(Map<String, String> paramsMap) throws AlipayApiException {

        return AlipaySignature.rsaCheckV1(paramsMap, AlipayConfigure.getALIPAY_PUBLIC_KEY(),
        		AlipayConfigure.getCHARSET(), AlipayConfigure.getSIGN_TYPE());
    }

    public static AlipayTradeQueryResponse queryTrade(Map<String, Object> bizContent) throws AlipayApiException {
    	
    	logger.debug("##in## paramsMap:="+new Gson().toJson(bizContent));
    	
    	AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
    	request.setBizContent(new Gson().toJson(bizContent));
    	AlipayTradeQueryResponse response = alipayClient.execute(request);
    	if(response.isSuccess()){
    		logger.info("##out## response:="+new Gson().toJson(response));
    		return response;
    	} else {
    		logger.error("##out## response:="+new Gson().toJson(response));
    		throw new AlipayApiException(response.getSubMsg());
    	}
    }

	public static void downloadBill(Map<String, Object> bizContent,String filePath) throws AlipayApiException {

		bizContent.put("bill_type", "trade");
		logger.debug("##in## paramsMap:="+new Gson().toJson(bizContent));

		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		request.setBizContent(new Gson().toJson(bizContent));
		AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
			logger.info("##out## response:="+new Gson().toJson(response));
			downloadBillFile(filePath,response.getBillDownloadUrl());
		} else {
			logger.error("##out## response:="+new Gson().toJson(response));
			throw new AlipayApiException(response.getSubMsg());
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

    private static void downloadBillFile(String filePath, String urlStr){
		//将接口返回的对账单下载地址传入urlStr
		//String urlStr = "http://dwbillcenter.alipay.com/downloadBillFile.resource?bizType=X&userId=X&fileType=X&bizDates=X&downloadFileName=X&fileId=X";
//指定希望保存的文件路径
		//String filePath = "/Users/fund_bill_20160405.zip";
		URL url = null;
		HttpURLConnection httpUrlConnection = null;
		InputStream fis = null;
		FileOutputStream fos = null;
		try {
			url = new URL(urlStr);
			httpUrlConnection = (HttpURLConnection) url.openConnection();
			httpUrlConnection.setConnectTimeout(5 * 1000);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setRequestMethod("GET");
			httpUrlConnection.setRequestProperty("Charsert", "UTF-8");
			httpUrlConnection.connect();
			fis = httpUrlConnection.getInputStream();
			byte[] temp = new byte[1024];
			int b;
			fos = new FileOutputStream(new File(filePath));
			while ((b = fis.read(temp)) != -1) {
				fos.write(temp, 0, b);
				fos.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			try {
				if(fis!=null){
					fis.close();
				}
				if(fos!=null){
					fos.close();
				}
				if(httpUrlConnection!=null) {
					httpUrlConnection.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
