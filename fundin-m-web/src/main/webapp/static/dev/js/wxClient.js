'use strict';
define(["Zepto", "service", "jWeixin"], function($, service, jWeixin) {
	var oauth_url = "https://open.weixin.qq.com/connect/oauth2/authorize";
	
	var isInWeixin = function() {
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)=="micromessenger") {
			return true;
	 	} else {
			return false;
		}
	}();
	
	return {
		isInWeixin: isInWeixin,
		getOauthUrl: function(redirectUrl) {
			return oauth_url + "?appid=" + appId + "&redirect_uri=" + redirectUrl 
					+ "&response_type=code&scope=snsapi_base&state=" + loginUserId 
					+ "#wechat_redirect";
		},
		getUserinfoUrl: function(redirectUrl) {
			return oauth_url + "?appid=" + appId + "&redirect_uri=" + redirectUrl 
					+ "&response_type=code&scope=snsapi_userinfo&state=login" 
					+ "#wechat_redirect";
		},
		wxConfig: function(config) {
			jWeixin.config($.extend({debug: false}, config));
		},
		wxConfigReady: function(readyCallback) {
			jWeixin.ready(function() {
				typeof readyCallback == "function" && readyCallback();
			});
		},
		shareTimeline: function(data) {
			jWeixin.onMenuShareTimeline(data);
		},
		shareAppMessage: function(data) {
			jWeixin.onMenuShareAppMessage(data);
		},
		chooseWXPay: function(data) {
			jWeixin.chooseWXPay(data);
		}
	};
})

//wx.config({
//    debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
//    appId: '', // 必填，公众号的唯一标识
//    timestamp: , // 必填，生成签名的时间戳
//    nonceStr: '', // 必填，生成签名的随机串
//    signature: '',// 必填，签名，见附录1
//    jsApiList: [] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
//});