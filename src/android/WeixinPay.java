package com.qdc.plugins.weixin;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信支付插件
 * 
 * @author NCIT
 * 
 */
public class WeixinPay extends CordovaPlugin {

	/** JS回调接口对象 */
	public static CallbackContext cbContext;
	
	public static IWXAPI wxAPI;
	public static String wxAppId;
	public static BroadcastReceiver receiver;

	/** LOG TAG */
	private static final String LOG_TAG = WeixinPay.class.getSimpleName();

	private void initWxApp(){
		if(wxAPI == null){
			Context context = cordova.getActivity().getApplicationContext();
			int resId = context.getResources().getIdentifier("config", "xml", context.getPackageName());
			XmlResourceParser xrp = context.getResources().getXml(resId);

			try{
				xrp.next();
				while(xrp.getEventType() != XmlResourceParser.END_DOCUMENT){
					if("preference".equals(xrp.getName())){
						String key = xrp.getAttributeValue(null, "name");
						if("wxAppId".equals(key)){
							wxAppId = xrp.getAttributeValue(null, "value");
							break;
						}
					}
					xrp.next();
				}
			} catch(XmlPullParserException ex){
		
			} catch(IOException ex){
			
			}
			wxAPI = WXAPIFactory.createWXAPI(cordova.getActivity(), wxAppId);
		}
	}

	@Override
	public void onStart(){

		initWxApp();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.tencent.mm.plugin.openapi.Intent.ACTION_REFRESH_WXAPP");

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				wxAPI.registerApp(wxAppId);
			}
		};

     	cordova.getActivity().registerReceiver(receiver, filter);
	}

	@Override
	public void onStop() {
		if (receiver != null) {
			cordova.getActivity().unregisterReceiver(receiver);
			receiver = null;
		}
		super.onDestroy();
	}

	/**
	 * 插件主入口
	 */
	@Override
	public boolean execute(String action, final JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		LOG.d(LOG_TAG, "WeixinPay#execute");
		

		boolean ret = false;

		if ("payment".equalsIgnoreCase(action)) {
			LOG.d(LOG_TAG, "WeixinPay#payment.start");

			cbContext = callbackContext;

			// PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
			// pluginResult.setKeepCallback(true);
			// callbackContext.sendPluginResult(pluginResult);

			// 参数检查
			if (args.length() != 1) {
				LOG.e(LOG_TAG, "args is empty", new NullPointerException());
				ret = false;
	            PluginResult result = new PluginResult(PluginResult.Status.ERROR, "args is empty");
	            result.setKeepCallback(true);
	            cbContext.sendPluginResult(result);
				return ret;
			}

			JSONObject jsonObj = args.getJSONObject(0);

			final String noncestr = jsonObj.getString("noncestr");
			if (noncestr == null || "".equals(noncestr)) {
				LOG.e(LOG_TAG, "noncestr is empty", new NullPointerException());
				ret = false;
	            PluginResult result = new PluginResult(PluginResult.Status.ERROR, "noncestr is empty");
	            result.setKeepCallback(true);
	            cbContext.sendPluginResult(result);
				return ret;
			}
			final String packageValue = jsonObj.getString("package");
			if (packageValue == null || "".equals(packageValue)) {
				LOG.e(LOG_TAG, "packageValue is empty", new NullPointerException());
				ret = false;
	            PluginResult result = new PluginResult(PluginResult.Status.OK, "packageValue is empty");
	            result.setKeepCallback(true);
	            cbContext.sendPluginResult(result);
				return ret;
			}
			final String partnerid = jsonObj.getString("partnerid");
			if (partnerid == null || "".equals(partnerid)) {
				LOG.e(LOG_TAG, "partnerid is empty", new NullPointerException());
				ret = false;
	            PluginResult result = new PluginResult(PluginResult.Status.ERROR, "partnerid is empty");
	            result.setKeepCallback(true);
	            cbContext.sendPluginResult(result);
				return ret;
			}
			final String prepayid = jsonObj.getString("prepayid");
			if (prepayid == null || "".equals(prepayid)) {
				LOG.e(LOG_TAG, "prepayid is empty", new NullPointerException());
				ret = false;
	            PluginResult result = new PluginResult(PluginResult.Status.ERROR, "prepayid is empty");
	            result.setKeepCallback(true);
	            cbContext.sendPluginResult(result);
				return ret;
			}
			final String timestamp = jsonObj.getString("timestamp");
			if (timestamp == null || "".equals(timestamp)) {
				LOG.e(LOG_TAG, "timestamp is empty", new NullPointerException());
				ret = false;
	            PluginResult result = new PluginResult(PluginResult.Status.ERROR, "timestamp is empty");
	            result.setKeepCallback(true);
	            cbContext.sendPluginResult(result);
				return ret;
			}
			final String sign = jsonObj.getString("sign");
			if (sign == null || "".equals(timestamp)) {
				LOG.e(LOG_TAG, "sign is empty", new NullPointerException());
				ret = false;
	            PluginResult result = new PluginResult(PluginResult.Status.ERROR, "sign is empty");
	            result.setKeepCallback(true);
	            cbContext.sendPluginResult(result);
				return ret;
			}

			//////////////////////
			// 请求微信支付
			//////////////////////
			initWxApp();
			wxAPI.registerApp(wxAppId);
			
			if (!wxAPI.isWXAppInstalled()) {
				LOG.e(LOG_TAG, "Wechat is not installed", new IllegalAccessException());
				ret = false;
	            PluginResult result = new PluginResult(PluginResult.Status.ERROR, "Wechat is not installed");
	            result.setKeepCallback(true);
	            cbContext.sendPluginResult(result);
				return ret;
			}

			LOG.d(LOG_TAG, "WeixinPay#payment.end");

			cordova.getThreadPool().execute(new Runnable() {
				public void run() {
					PayReq payreq = new PayReq();

					payreq.appId = wxAppId;
					payreq.partnerId = partnerid;
					payreq.prepayId = prepayid;
					payreq.packageValue = packageValue;
					payreq.nonceStr = noncestr;
					payreq.timeStamp = timestamp;
					payreq.sign = sign;

					boolean ret = wxAPI.sendReq(payreq);
					if (!ret) {
			            PluginResult result = new PluginResult(PluginResult.Status.ERROR, "unifiedorder requst failured.");
			            result.setKeepCallback(true);
			            cbContext.sendPluginResult(result);
					}
				}
			});
			ret = true;
		}

		return ret;
	}

}
