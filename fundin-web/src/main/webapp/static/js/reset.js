(function($, window){
	
	var putToMid = function(window, elem) {
		var window = $(window).width();
		var offset = ((window - $(elem).width()) / 2).toFixed(2);
		var offPercent = ((offset / window).toFixed(2)) * 100;
		$(elem).css("left", offPercent + "%");
	};
	
	window.showMessage = function(msg) {
		var msg = arguments[0];
		var time = arguments[1] ? arguments[1] : 1000;
		$(".page-mask-light").show();
		$("#message-content").text(msg);
		
		putToMid(window, $(".message-block"));
		$(".message-block").show();
		
		setTimeout(function() {
			$(".message-block").hide();
			$(".page-mask-light").hide();
		}, time);
	};
	
	$("#resetPasswdBtn").on("click",function(){
    	var newPasswd = $("#newPasswdInput").val().trim();
    	var newPasswdComfirm = $("#newPasswdComfirmInput").val().trim();
    	var key = $("#key").val().trim();
    	if("" == newPasswd){
    		showMessage("请填写新密码！");
    		return ;
    	}
    	if("" == newPasswdComfirm){
    		showMessage("请填写新密码确认！");
    		return ;
    	}
    	
    	if(newPasswd != newPasswdComfirm){
    		showMessage("新密码前后两次输入不一致");
    		return ;
    	}
    	
    	if(newPasswd.length < 6){
    		showMessage("密码长度至少为6位！");
    		return ;
    	}
    	
    	var data = {
    			"key" : key,
    			"newPasswd" : newPasswd
    	};
    	
    	ajaxServices.resetPasswd(data,function(){
    		showMessage("重置密码成功！ 跳转首页...");
    		setTimeout(function(){location.href = "/"},2000); 
    	},function(){
    		showMessage("重置密码失败！");
    	});
    	
    });
})(jQuery, window);