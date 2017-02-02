'use strict';
define(["Zepto"], function($) {
	var $loadingToastText = $("#loading_toast_text");
	var $loadingToast = $("#loadingToast");
	var $toastText = $("#toast_text");
	var $toastIcon = $("#toast_icon");
	var $toast = $("#toast");
	
	return {
		checkEmail: function(input) {
			var email_reg = /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/g;
			if (email_reg.test(input)) {
				return true;
			}
			return false;
		},
		
		checkPhone: function(input) {
			var phone_reg = /^[1][3578][0-9]{9}$/g;
			if (phone_reg.test(input)) {
				return true;
			}
			return false;
		},
		
		showLoadingToast: function(loadingText) {
			$loadingToastText.text(loadingText);
			$loadingToast.css("display", "block");
		},
		
		hideLoadingToast: function() {
			$loadingToastText.text("");
			$loadingToast.css("display", "none");
		},
		
		/**
		 * type (default, warn)
		 * timeout (1s for default)
		 */
		showToastMsg: function(text, type, timeout, callback) {
			$toastText.text(text);
			if ("warn" == type) {
				$toastIcon.addClass("toast_warn");
			} else if ("tips" == type) {
				$toastIcon.removeClass("weui_icon_toast");
			}
			$toast.css("display", "block");
			
			timeout = timeout || 1000;
			setTimeout(function(){
				$toast.css("display", "none");
				$toastText.text("");
				$toastIcon.removeClass("toast_warn");
				typeof callback == "function" && callback();
			}, timeout);
		}
	};
})