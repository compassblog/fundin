'use strict';
define(["Zepto", "service", "utils", "wxClient", "Zepto.touch", "wangEditor"], function($, service, utils, wxClient) {
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
	
	 /**
     *  project type select
     */
    $(".cate-list span").on("click", function() {
        $(this).siblings().removeClass("selected");
        $(this).attr("class", "selected");
    });
    
	var subject = parseInt(window.projSubject);
	if (subject == 3 || subject == 4) {
		$("[data-type-s='" + subject + "']").addClass("selected");
	} else {
		$('[data-type-s="1"]').addClass("selected");
	}
	var repayWay = parseInt(window.projRepayWay);
	if (repayWay == 1 || repayWay == 2) {
		$("[data-type-w='" + repayWay + "']").addClass("selected");
	} else {
		$("[data-type-w='0']").addClass("selected");
	}
	
    /**
     * 	image upload
     */
    $("#cover_img").on('change', function(){
	  	var fd = new FormData();
	  	var file =$("#cover_img")[0].files;
	  	fd.append("imgFile", file[0]);
	  	$.ajax({
	          url: '/uploadImage',  
	          type: 'POST',
	          data: fd,
	          cache : false,
	          contentType : false, 
	          processData : false, 
	          success: function (resp) {  
	        	  if (resp.code) {
		                $("#coverImg").attr('src', resp.data).show();
		              //  $(".cover").html("<img src='" + resp.data + "'>");
		                $("#cover_img_v").val(resp.data);
		            }
	          }
	    });
    	
	});
    
    if ($("#coverImg").attr('src') != "") {
    	$(".cover").html("<img src='" + $("#coverImg").attr('src') + "'>");
    };
    
    /**
     * wangEditor-mobile
     */
    var editor = new ___E('wang');
 //   editor.config.uploadImgUrl = '/uploadImage';
    editor.config.menus = [
							'head',
							'bold',
							'color',
							'quote',
							'list',
							'check'
                     ];
    editor.init();
    if (projContent != "")
    	editor.$txt.html(projContent);
    
    /**
     * repay image upload
     */
    var bindRepayImg = function() {
    	$(".repay_img").unbind("change");
    	$(".remove-x").unbind("click");
    	
    	$("#repay_img").on('change', function(){
    		var fd = new FormData();
    	  	var file =$("#repay_img")[0].files;
    	  	fd.append("imgFile", file[0]);
    		$.ajax({
    	        url: "/uploadImage",
    	        type: 'POST',
    	        data: fd,
    	        cache : false,
    	        contentType : false, 
    	        processData : false,
    	        success: function(resp){
    	            if (resp.code) {
    	            	$("#repayImg").attr('src', resp.data).show();
 		              //  $(".cover").html("<img src='" + resp.data + "'>");
    	            	$("#repay_img_v").val(resp.data);
    	            }
    	        }
    	    });
    	});
    	
    	$(".remove-x").on("click", function() {
    		showOperation("确定删除该图片？", function(params) {
    			var $elem = params.elem;
    			$elem.siblings(".img-entity").attr('src', '');
    			$elem.parent().hide();
    		}, {"elem": $(this)});
    	});
    };
    bindRepayImg();
    
    /**
     * 	project save draft submit
     */
    $("#draft-btn").on('click', function() {
    	var totalAmount = $("input[name=projAmount]").val();
        var amountReg = /^[1-9]\d{0,4}$/;
        if (totalAmount != "") {
	    	if (!amountReg.test(totalAmount)) {
	    		utils.showToastMsg("请输入正整数", "warn");
	    		return ;
	    	}
        }
       
        var daysReg = /^[1-9]\d{0,1}$/;
        var days = $("input[name=projDays]").val();
        if (days != "") {
        	if (!daysReg.test(days)) {
        		utils.showToastMsg("请输入正整数", "warn");
                return ;
            } else {
                if (parseInt(days) > 60) {
                	utils.showToastMsg("项目周期不能超过60天", "warn");
                    return ;
                }
            }
        }
        
        var repayDays = $("input[name=repayDays]").val();
        if (repayDays != "") {
	        if (!daysReg.test(repayDays)) {
	        	utils.showToastMsg("请输入正整数", "warn");
	            return ;
	        } else {
	            if (parseInt(repayDays) > 60) {
		        	utils.showToastMsg("回报发放不能超过60天", "warn");
	                return ;
	            }
	        }
        }
        var content = editor.$txt.html();
        var data = {
            "title": $("input[name=projTitle]").val(),
            "intro": $("textarea[name=projIntro]").val(),
            "subject" : $("#type span.selected").attr("data-type-s"),
            "content": content,
            "coverImg": $("#cover_img_v").val(),
            "totalAmount": totalAmount,
            "days": days,
            "repayDays": repayDays,
            "repayWay": $("#way span.selected").attr("data-type-w"),
            "repayContent" : $("textarea[name=repay_text]").val(),
            "repayImage" : $("#repay_img_v").val()
        };
        if (projId) {
        	data["id"] = projId;
        }
        
        service.saveDraft(data, function(data){
        	utils.showToastMsg("草稿保存成功");
        });
    });
    
    /**
     * 	project startup submit
     */
    $("#startup-btn").on('click', function() {
    	service.hasPersonalInfo({}, function(data) {
    		if (data) {
    	        var title = $("input[name=projTitle]").val();
    	        if ("" == title) {
    	        	utils.showToastMsg("请填写标题", "warn");
    	            return ;
    	        }
    	        var intro = $("textarea[name=projIntro]").val();
    	        if ("" == intro) {
    	        	utils.showToastMsg("请填写简介", "warn");
    	            return ;
    	        }
    	        var coverImg = $("#cover_img_v").val();
    	        if ("" == coverImg) {
    	        	utils.showToastMsg("请选择封面图片", "warn");
    	            return ;
    	        }
    	        var totalAmount = $("input[name=projAmount]").val();
    	        var amountReg = /^[1-9]\d{0,4}$/;
    	        if (!amountReg.test(totalAmount)) {
    	        	utils.showToastMsg("请输入正整数", "warn");
    	            return ;
    	        }
    	        
    	        var daysReg = /^[1-9]\d{0,1}$/;
    	        var days = $("input[name=projDays]").val();
    	        if (!daysReg.test(days)) {
    	        	utils.showToastMsg("请输入正整数", "warn");
    	            return ;
    	        } else {
    	            if (parseInt(days) > 60) {
    	            	utils.showToastMsg("项目周期不能超过60天", "warn");
    	                return ;
    	            }
    	        }
    	        var repayDays = $("input[name=repayDays]").val();
    	        if (!daysReg.test(repayDays)) {
    	        	utils.showToastMsg("请输入正整数", "warn");
    	            return ;
    	        } else {
    	            if (parseInt(repayDays) > 60) {
    	            	utils.showToastMsg("回报发放不能超过60天", "warn");
    	                return ;
    	            }
    	        }
    	            
    	        $("#startup-btn").attr("disabled", true);

    	        var data = {
    	            "title": title,
    	            "intro": intro,
    	            "subject" : $("#type span.selected").attr("data-type-s"),
    	            "content": editor.$txt.html(),
    	            "coverImg": coverImg,
    	            "totalAmount": totalAmount,
    	            "days": days,
    	            "repayDays": repayDays,
    	            "repayWay": $("#way span.selected").attr("data-type-w"),
    	            "repayContent" : $("textarea[name=repay_text]").val(),
    	            "repayImage" : $("#repay_img_v").val()
    	        };
    	        if (projId) {
    	        	data["id"] = projId;
    	        }
    	        
    	        service.startNew(data, function(data){
    	        	utils.showToastMsg("项目发起成功, 请等待审核");
    	        	setTimeout(function() {
    	        		window.location= "/user/" + loginUserId;
    	        	}, 1000);
    	        });
    		} else {
    			utils.showToastMsg("请先到个人页面完善个人资料后再发起项目", "warn");
    		}
    	});
    });
})