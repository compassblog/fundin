'use strict';
define(["Zepto", "utils"], function($, utils) {
	var services = {
		"login": {method: 'POST', url: '/login'},
		"register": {method: 'POST', url: '/register'},
		"saveUserAction": {method: 'POST', url: '/saveUserAction'},
		"getJsApiConfig": {method: 'POST', url: '/wx/getJsApiConfig'},
		"createNewWXPayForM": {method: 'POST', url: '/pay/createNewWXPayForM'},
		"createNewPay": {method: 'POST', url: '/pay/createNewPay'},
		"updateSomeInfo": {method: 'POST', url: '/user/updateSomeInfo'},
		"getUnivArray" : {url : "/user/getUnivArray", method : "POST"},
        "getSchool4Univ" : {url : "/user/getSchool4Univ", method : "POST"},
        "updateMessageStatus" : {url : "/updateMessageStatus", method : "POST"},
        "saveDraft": {url : "/proj/saveDraft", method : "POST"},
        "startNew": {url : "/proj/startNew", method : "POST"},
        "sendVcode" : {url : "/sendVcode", method : "POST"},
        "hasPersonalInfo" : {url : "/user/hasPersonalInfo", method : "POST"}
	};
	
	$.each(services, function(funcName, request) {
		services[funcName] = function(data, callback1, callback0) {
			data = data || {};
			return $.ajax({
				type: request.method,
				url: request.url,
				cache: false,
				data: data,
				headers: {'Content-Type': 'application/x-www-form-urlencoded'},
				success: function(resp){
					if (resp.code) {
                        typeof callback1 == "function" && callback1(resp.data);
                    } else {
                    	typeof callback0 == "function" && callback0(resp.msg, resp.data);
                    }
				}, 
				error: function() {
					utils.hideLoadingToast();
					utils.showToastMsg("亲~网络不给力哦", "warn");
				}
			});
		};
	});
	
	return services;
});