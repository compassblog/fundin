'use strict';
define(["Zepto", "utils"], function($, utils) {
	 $(".invite").on("click", function() {
		 utils.showToastMsg("推荐朋友注册Fundin，输入您的手机号，即可获得红包哦!", "tips");
	    });
})