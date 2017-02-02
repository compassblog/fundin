'use strict';
define(["Zepto", "service", "utils", "wxClient"], function($, service, utils, wxClient) {
	var timeline = {
		title: projTitle, // 分享标题
	 	link: location.href, // 分享链接
	    imgUrl: projImgUrl, // 分享图标
	};
	var appMessage = {
		title: projTitle, // 分享标题
		desc: $("#projDesc").text(), // 分享描述
	 	link: location.href, // 分享链接
	    imgUrl: projImgUrl, // 分享图标
	};
	
	/**
	 * 分享设置
	 */
	if (! wxClient.isInWeixin) {
		$("#share-wechat").remove();
		$("#share-tip").remove();
	} else {
		service.getJsApiConfig({"url" : window.location.href}, function(config){wxClient.wxConfig(config)});
		wxClient.wxConfigReady(function(){
			wxClient.shareTimeline(timeline);
			wxClient.shareAppMessage(appMessage);
		});
		
		$("#share-wechat").tap(function(){
			$("#share-tip").css('display', 'block');
		});
	}
	
	/**
	 * 分享操作
	 */
	$("#share-btn").tap(function(){
		var mask = $('#share-mask');
        var action = $('#share-action');
        action.addClass('weui_actionsheet_toggle');
        mask.css('display', 'block')
            .focus()	// 加focus()是为了触发一次页面的重排(reflow or layout thrashing), 使mask的transition动画得以正常触发
            .addClass('weui_fade_toggle').one('tap', function () {
	            hideActionSheet(action, mask);
	        });
        $('#share-cancle').one('tap', function () {
            hideActionSheet(action, mask);
        });
        mask.unbind('transitionend').unbind('webkitTransitionEnd');

        function hideActionSheet(action, mask) {
        	$("#share-tip").css('display', 'none');
        	action.removeClass('weui_actionsheet_toggle');
            mask.removeClass('weui_fade_toggle');
            mask.on('transitionend', function () {
                mask.css('display', 'none');
            }).on('webkitTransitionEnd', function () {
                mask.css('display', 'none');
            });
        }
	});
	
	/**
	 * 我要支持
	 */
	if (! wxClient.isInWeixin) {
		$("#support-btn").tap(function(){
			utils.showToastMsg("请在微信中打开此网页进行支付操作", "warn");
		});
	} else {
		$("#support-btn").tap(function(){
			if (!loginStatus) {
				location.href = "/m/login?returnUrl=" + location.href;
				return ;
			}
			location.href = wxClient.getOauthUrl("http://m.fundin.cn/pay/" + projId);
		});
	}
	
	/**
	 * 关注
	 */
	var flw_data = {
		"projId": projId, 
		"type": 1
	};
	$("#follow-btn").tap(function(){
		if (!loginStatus) {
			location.href = "/m/login?returnUrl=" + location.href;
			return ;
		}
		service.saveUserAction(flw_data, function(){
			utils.showToastMsg("关注成功", null, null, function() {
				location.reload();
			});
		});
    });
	
})