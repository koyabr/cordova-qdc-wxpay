var exec = require('cordova/exec');

exports.payment = function(json, successFn, failureFn) {
    exec(successFn, failureFn, 'WeixinPay', 'payment', [json]);
};
