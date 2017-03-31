# wxpay for Cordova

微信支付cordova插件，修改自 https://github.com/mrwutong/cordova-qdc-wxpay.git


## 安装

```
ionic plugin add https://github.com/koyabr/cordova-qdc-wxpay.git --variable APP_ID={微信APPID}
```

## 卸载
```
ionic plugin rm com.qdc.plugins.wxpay
```


## JS调用

* 事先前调用后台预支付API生成订单数据及签名数据
* 调用plugin的JS方法`wxpay.payment`进行支付

```js

var payData = {
	appid: 公众账号ID
	noncestr: 随机字符串
	package: 扩展字段
	partnerid: 商户号
	prepayid: 预支付交易会话ID
	timestamp: 时间戳
	sign: 签名
};

cordova.wxpay.payment(payData, cb_success, cb_failure);
// cb_success:调用成功回调方法
// cb_failure:调用失败回调方法

```

