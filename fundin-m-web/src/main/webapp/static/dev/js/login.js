'use strict';
define(["Zepto", "service", "utils", "wxClient"], function($, service, utils, wxClient) {
	var $phone = $("#input-phone");
	var $pwd = $("#input-pwd");
	
	if (! wxClient.isInWeixin) {
		$("#login-wx").remove();
	};
	
	$("#login-wx").tap(function() {
		location.href = wxClient.getUserinfoUrl("http://m.fundin.cn/loginByWx");
	});
	
	$("#login-link").tap(function() {
		if (!utils.checkPhone($phone.val())) {
			utils.showToastMsg("请输入正确的手机号", "warn");
			return ;
		}
		if ($pwd.val().length < 6) {
			utils.showToastMsg("密码不能小于6位", "warn");
			return ;
		}
		
		var data = {
			"phone": $phone.val(),
			"passwd": $pwd.val(),
			"rememberMe": $("#remeberMe").is(":checked") ? "on" : "off"
		};
		utils.showLoadingToast("登录中...");
		
		service.login(data, function() {
			utils.hideLoadingToast();
			utils.showToastMsg("登录成功", null, null, function() {
				var redirect = "/me";
				var search = window.location.search.substring(1);
				var params = search.split("&");
				for (var i in params) {
					var p = params[i];
					if(p.indexOf("returnUrl=") > -1) {
						redirect = p.substring(10);
						break;
					}
				}
				window.location.href = redirect;
			});
		}, function(msg, data) {
			utils.hideLoadingToast();
			utils.showToastMsg(msg, "warn");
		});
	});
})