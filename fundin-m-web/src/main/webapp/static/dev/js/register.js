'use strict';
define(["Zepto", "service", "utils"], function($, service, utils) {
	var $name = $("#input-name");
	var $phone = $("#input-phone");
	var $code = $("#input-code");
	var $pwd = $("#input-pwd");
	var $conf_pwd = $("#input-conf-pwd");
	var $invPhone = $("#input-invPhone");
	var $token = $("#page-token");
	
	var isSend = false;
	var sec = 60;
	var sec_func;
	
	$("#get-vcode").tap(function() {
		if (isSend) {
			return ;
		}
		
		if (!utils.checkPhone($phone.val())) {
			utils.showToastMsg("请输入正确的手机号", "warn");
			return ;
		}
		
		isSend = true;
		sec = 60;
		$(this).css("background-color", "#cecece");
		sec_func = setInterval(sec_count, 1000);
		
		var data = {
			"phone": $phone.val(),
			"token": $token.val()
		};
		service.sendVcode(data, function(){});
	});
	
	var sec_count = function() {
		if (-- sec <= 0) {
			isSend = false;
			$("#get-vcode").css("background-color", "#ff6c00");
			$("#get-vcode").text("获取验证码");
			clearInterval(sec_func);
		} else {
			$("#get-vcode").text("重发剩余" + sec + "秒");
		}
	};
	
	$("#reg-link").tap(function() {
		if ($name.val().trim() == "") {
			utils.showToastMsg("请输入名称", "warn");
			return ;
		}
		if (!utils.checkPhone($phone.val())) {
			utils.showToastMsg("请输入正确的手机号", "warn");
			return ;
		}
		if ($code.val().trim() == "") {
			utils.showToastMsg("请输入手机验证码", "warn");
			return ;
		}
		if ($pwd.val().length < 6) {
			utils.showToastMsg("密码不能小于6位", "warn");
			return ;
		}
		if ($pwd.val() != $conf_pwd.val()) {
			utils.showToastMsg("两次输入的密码不一致", "warn");
	    	return ;
	    }
		if (!utils.checkPhone($invPhone.val())) {
			utils.showToastMsg("请输入正确的手机号", "warn");
			return ;
		}
		
		var data = {
			"regName": $name.val(),
			"regPhone": $phone.val(),
			"vcode": $code.val(),
			"regPasswd": $pwd.val(),
			"regType": $("select[name=reg-name] option:selected").val(),
			"invPhone": $invPhone.val(),
			"token": $token.val()
		};
		utils.showLoadingToast("注册中...");
		
		service.register(data, function() {
			utils.hideLoadingToast();
			utils.showToastMsg("注册成功", null, null, function() {
				window.location.href = "/me";
			});
		}, function(msg, data) {
			utils.hideLoadingToast();
			utils.showToastMsg(msg, "warn");
		});
	});
})