package com.alipay.api;

public class AliPaySettings {

    private String appId;
    private String privateKey;
    private String alipayPublicKey;
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		if(appId==null||appId.isEmpty()){
			throw new RuntimeException("appId can not is empty!");
		}
		this.appId = appId;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		if(privateKey==null||privateKey.isEmpty()){
			throw new RuntimeException("privateKey can not is empty!");
		}
		this.privateKey = privateKey;
	}
	public String getAlipayPublicKey() {
		return alipayPublicKey;
	}
	public void setAlipayPublicKey(String alipayPublicKey) {
		if(alipayPublicKey==null||alipayPublicKey.isEmpty()){
			throw new RuntimeException("alipayPublicKey can not is empty!");
		}
		this.alipayPublicKey = alipayPublicKey;
	}

	public AliPaySettings(String appId,String privateKey,String alipayPublicKey){
		setAppId(appId);
		setPrivateKey(privateKey);
		setAlipayPublicKey(alipayPublicKey);
	}
}
