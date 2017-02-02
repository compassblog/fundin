'use strict';
define(["Zepto", "service", "utils", "wxClient"], function($, service, utils, wxClient) {
	var timeline = {
		title: projTitle, // 分享标题
	 	link: wxClient.getOauthUrl("http://m.fundin.cn/pay/" + projId), // 分享链接
	    imgUrl: projImgUrl, // 分享图标
	};
	var appMessage = {
		title: projTitle, // 分享标题
		desc: $("#projDesc").val(), // 分享描述
	 	link: wxClient.getOauthUrl("http://m.fundin.cn/pay/" + projId), // 分享链接
	    imgUrl: projImgUrl, // 分享图标
	};
		
	if (wxClient.isInWeixin) {
		service.getJsApiConfig({url : window.location.href}, function(config){wxClient.wxConfig(config)});
		wxClient.wxConfigReady(function(){
			wxClient.shareTimeline(timeline);
			wxClient.shareAppMessage(appMessage);
		});
	}
	
	if (! wxClient.isInWeixin) {
		$("#support-now").tap(function(){
			utils.showToastMsg("请在微信中打开此网页进行支付操作", "warn");
		});
	} else {
		$("#support-now").tap(function(){
			var rAmount = 0;
	    	var redIds = '';
	    	$('input[name=redpacket]:checked').each(function(){
	    		rAmount += Number($(this).val());
	    		redIds += $(this).attr("id").substring(9) + "_";
	    	});
	    	
			var pay_data = {
				projId: projId,
				openid: openid,
				rAmount: rAmount,
				amount: computeAfter
			};
			utils.showLoadingToast("提交中...");
			
			if(computeAfter > 0){
				service.createNewWXPayForM(pay_data, function(WXPay){
					utils.hideLoadingToast();
					wxClient.chooseWXPay({
						timestamp: WXPay.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
					    nonceStr: WXPay.nonceStr, // 支付签名随机串，不长于 32 位
					    'package': WXPay.pkg, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
					    signType: WXPay.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
					    paySign: WXPay.paySign, // 支付签名
					    success: function (res) {
					    	location.href = "http://m.fundin.cn/proj/" + projId;
					    }
					});
				}, function(msg){
					utils.hideLoadingToast();
					utils.showToastMsg(msg, "warn");
				});
			} else if(rAmount > 0 && computeAfter <= 0) {
				service.createNewPay({"projId": projId,
	      			 "rAmount":rAmount,"redIds":redIds}, function(date) {
	       			if (date) {
	       				location.href = "http://m.fundin.cn/proj/" + projId;
	       			}
	       		});
	       	}
			
		});
	}
	
	$('input[name=repay-radio]').change(function(){
		$('#support-amt').text($(this).attr('data-repay-amt'));
	});
	$('input[name=repay-radio]:first').trigger("click");
	
	//michael add
	 function onlyNumber(){
	    	var curRepayAmount = $("#payAmount").val();
	    	if (curRepayAmount.length == 0) {
	    		$("#payAmount").val(0);
			} else {
				$("#payAmount").val(curRepayAmount.replace(/^0\d+|\D+/, ''));
			}
	    }
	    
	 	$("#payAmount").keyup(function(){
	    	onlyNumber();
	    	compute();
	    });
	    
	    $("#payAmount").blur(function(){
	    	onlyNumber();
	    	check();
	    });
	    
	    var computeAfter = 0;
	    function compute(){
	    	var curRepayAmount = $("#payAmount").val();
	    	$('input[name="redpacket"]:checked').each(function(){
	    		curRepayAmount -= $(this).val();
	    	});
	    	if(curRepayAmount <= 0)
	    		$("#support-amt").text(0);
	    	else
	    		$("#support-amt").text(curRepayAmount);
	    	
	    	computeAfter = curRepayAmount;
	    }
	    
	    $('input[name="redpacket"]').click(function(){
			if($(this).prop('checked'))
				computeAfter = Number(computeAfter) - Number($(this).val());
	        else
	        	computeAfter = Number(computeAfter) + Number($(this).val());
			if(computeAfter < 0)
				$("#support-amt").text(0);
			else
				$("#support-amt").text(computeAfter);
			check();
	    });
	    
	    function check(){
	    	var curRepayAmount = $("#payAmount").val();
	    	var redPackAmount = 0 ;
	    	$('input[name="redpacket"]:checked').each(function(){
	    		redPackAmount = Number(redPackAmount) + Number($(this).val());
	    		curRepayAmount -= $(this).val();
	    	});
	    	if(curRepayAmount <= 0){
	    		$("#payAmount").val(redPackAmount);
	    		computeAfter = 0;
	    	}
	    }
	
})