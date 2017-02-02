(function($, window){
	$(document).ready(function() {
		if ("false" == $("#loginStatus").val()) {
			showLogin();
			$("#close-login").hide();
			$("#close-reg").hide();
		}
	});
	
	var projId;
	if ($("#projId").val().trim != "") {
		projId = parseInt($("#projId").val());
	};
	
    /**
     *  umeditor
     */
    var um;
    setTimeout(function() {
    	um = (UM && UM.getEditor('myEditor'));
        if(!$("#myEditor").is(":visible")) {
            try {
                um && um.destroy();
            } catch (e) {}
            um = (UM && UM.getEditor('myEditor'));
        }
        
        um.addListener("contentChange", function() {
        	$(".details").html(um.getContent());
        });
        um.setContent($("#contentHtml").val());
    }, 100);
    
    /**
     *  project type select
     */
    $(".cate-list span").on("click", function() {
        $(this).siblings().removeClass("selected");
        $(this).attr("class", "selected");
    });
    if (window.subjectData != "") {
    	var subject = parseInt(window.subjectData);
    	if (subject == 3 || subject == 4) {
    		$(".cate-list[data-type=" + subject + "]").trigger("click");
    	}
    };
    if (window.repayWayData != "") {
    	var repayWay = parseInt(window.repayWayData);
    	if (repayWay == 1 || repayWay == 2) {
    		$("#repayWay" + repayWay).attr("checked", "checked");
    	}
    };
    
    /**
     * 	intro to preview
     */
    $("textarea[name=projIntro]").on("keyup", function() {
    	$(".intro").text($(this).val());
    });
    if ($("textarea[name=projIntro]").val().trim() != "") {
    	$(".intro").text($("textarea[name=projIntro]").val().trim());
    };
    
    /**
     * 	image upload
     */
    $("#cover_img").on('change', function(){
	    if($(this).val() == ""){
	        return ;
	    }
	    $("#coverForm").ajaxSubmit({
	        url: "/uploadImage",
	        dataType: 'json',
	        resetForm: true,
	        clearForm: true,
	        success: function(resp){
	            if (resp.code) {
	                $("#coverImg").attr('src', resp.data).show();
	                $(".cover").html("<img src='" + resp.data + "'>");
	            }
	        }
	    });
	});
    if ($("#coverImg").attr('src') != "") {
    	$(".cover").html("<img src='" + $("#coverImg").attr('src') + "'>");
    };
    
    /**
     * repay image upload
     */
//    var bindRepayImg = function() {
//    	$(".repay_img").unbind("change");
//    	$(".remove-x").unbind("click");
//    	
//    	$(".repay_img").on('change', function(){
//    	    var $this = $(this);
//    		if($this.val() == ""){
//    	        return ;
//    	    }
//    	    
//    		$this.parent("form").ajaxSubmit({
//    	        url: "/uploadImage",
//    	        dataType: 'json',
//    	        resetForm: true,
//    	        clearForm: true,
//    	        success: function(resp){
//    	            if (resp.code) {
//    	            	var $imgEntity = $this.parent().parent()
//    	            		.siblings(".repay-img-entity");
//    	            	$imgEntity.find(".img-entity").attr('src', resp.data);
//    	            	$imgEntity.css('display', 'inline-block');
//    	            }
//    	        }
//    	    });
//    	});
//    	
//    	$(".remove-x").on("click", function() {
//    		showOperation("确定删除该图片？", function(params) {
//    			var $elem = params.elem;
//    			$elem.siblings(".img-entity").attr('src', '');
//    			$elem.parent().hide();
//    		}, {"elem": $(this)});
//    	});
//    };
//    bindRepayImg();
	
//    var bindDelRepay = function() {
//    	$(".delRepay").unbind("click");
//    	$(".delRepay").on("click", function() {
//    		showOperation("确定删除该回报？", function(params) {
//    			var $elem = params.elem;
//    			if ($elem.attr("data-id") != "") {
//    				ajaxServices.deleteRepay({"repayId": $elem.attr("data-id")}, 
//    						function(data){});
//    			}
//    			$elem.remove();
//    		}, {"elem": $(this).parent()});
//    	});
//    };
//    bindDelRepay();
    
//    $("#addRepay").on("click", function() {
//    	if ($(".repay-item").size() == 5) {
//    		return ;
//    	}
//    	
//    	var newRepay = "<div class='repay-item clearfix' data-id=''>\
//    				<span class='delRepay'>一</span>\
//	                <span>￥</span>\
//	                <input class='repay' type='text' maxlength='3' \
//	                	name='repay_input' placeholder='支持金额' value=''>\
//	                <textarea maxlength='150' name='repay_text'\
//	                	placeholder='项目成功后对支持者的回馈'></textarea>\
//	                <span class='repay-img'>\
//	                    <form method='post' enctype='multipart/form-data'>\
//	                        <input type='file' name='imgFile' class='repay_img'\
//	                               multiple='multiple'\
//	                               accept='image/jpg,image/gif,image/png,image/jpeg'>\
//	                    </form>\
//	                </span>\
//	                <div class='repay-img-entity' style='display: none'>\
//	                	<img src='' class='img-entity'>\
//	                	<img src='/static/img/remove-x.png' class='remove-x'>\
//	                </div>\
//	            </div>";
//    	$(this).before(newRepay);
//    	bindRepayImg();
//    	bindDelRepay();
//    });
    
    /**
     * 	project save draft submit
     */
    $("#draft-btn").on('click', function() {
//    	var totalAmount = $("input[name=projAmount]").val();
//        var amountReg = /^[1-9]\d{0,4}$/;
//        if (totalAmount != "") {
//	    	if (!amountReg.test(totalAmount)) {
//	    		showMessage("请输入正整数");
//	    		return ;
//	    	}
//        }
//       
        var daysReg = /^[1-9]\d{0,1}$/;
//        var days = $("input[name=projDays]").val();
//        if (days != "") {
//        	if (!daysReg.test(days)) {
//                showMessage("请输入正整数");
//                return ;
//            } else {
//                if (parseInt(days) > 60) {
//                	showMessage("项目周期不能超过60天");
//                    return ;
//                }
//            }
//        }
//        
        var repayDays = $("input[name=repayDays]").val();
        if (repayDays != "") {
	        if (!daysReg.test(repayDays)) {
	        	showMessage("请输入正整数");
	            return ;
	        } else {
	            if (parseInt(repayDays) > 60) {
	            	showMessage("回报发放不能超过60天");
	                return ;
	            }
	        }
        }
//        
//        var repayData = [];
//        var repayReg = /^[1-9]\d{0,2}$/;
//        var repayFlag = false;
//    	$.each($(".repay-item"), function(idx, item){
//            var repayItem = {};
//            if ($(this).attr("data-id") != "") {
//            	repayItem["id"] = parseInt($(this).attr("data-id"));
//            }
//            if (projId) {
//            	repayItem["projId"] = projId;
//            };
//            
//            repayItem["amount"] = $(this).find("input[name=repay_input]").val();
//            if (!repayReg.test(repayItem["amount"])) {
//            	showMessage("项目回报金额必须为正整数");
//            	repayFlag = true;
//                return false;
//            } else {
//                if (parseInt(repayItem["amount"]) > 250) {
//                    showMessage("项目单个支持金额不能超过250元");
//                    repayFlag = true;
//                    return false;
//                }
//            }
//            repayItem["content"] = $(this).find("textarea[name=repay_text]").val();
//            if ("" == repayItem["content"]) {
//                showMessage("请填写项目回报内容");
//                repayFlag = true;
//                return false;
//            }
//            repayItem["image"] = $(this).find(".img-entity").attr("src");
//            repayData.push(repayItem);
//        });
//	    if (repayFlag) {
//	    	return ;
//        }
	 
        var data = {
            "title": $("input[name=projTitle]").val(),
            "intro": $("textarea[name=projIntro]").val(),
            "subject" : $(".cate-list span.selected").attr("data-type"),
            "content": um.getContent(),
            "coverImg": $("#coverImg").attr("src"),
            "video":  $("textarea[name=projVideo]").val(),
//            "totalAmount": totalAmount,
//            "days": days,
            "repayDays": repayDays,
            "repayWay": $("input[name=repayRadio]:checked").val()
//            "repayStr" :  JSON.stringify(repayData)
        };
        if (projId) {
        	data["id"] = projId;
        }
        
        ajaxServices.savePostDraft(data, function(data){
        	showMessage("草稿保存成功");
        	reloadPage();
        });
    });
    
    /**
     * 	project startup submit
     */
    $("#startup-btn").on('click', function() {
    	ajaxServices.hasPersonalInfo({}, function(data) {
    		if (data) {
    			if (! $("#agree-box").is(':checked')) {
    	        	showMessage("请阅读《Fundin服务声明》");
    	            return ;
    	        }
    	        var title = $("input[name=projTitle]").val();
    	        if ("" == title) {
    	        	showMessage("请填写标题");
    	            return ;
    	        }
    	        var intro = $("textarea[name=projIntro]").val();
    	        if ("" == intro) {
    	        	showMessage("请填写简介");
    	            return ;
    	        }
    	        var coverImg = $("#coverImg").attr("src");
    	        if ("" == coverImg) {
    	        	showMessage("请选择封面图片");
    	            return ;
    	        }
//    	        var totalAmount = $("input[name=projAmount]").val();
//    	        var amountReg = /^[1-9]\d{0,4}$/;
//    	        if (!amountReg.test(totalAmount)) {
//    	        	showMessage("请输入正整数");
//    	            return ;
//    	        }
    	        
    	        var daysReg = /^[1-9]\d{0,1}$/;
//    	        var days = $("input[name=projDays]").val();
//    	        if (!daysReg.test(days)) {
//    	            showMessage("请输入正整数");
//    	            return ;
//    	        } else {
//    	            if (parseInt(days) > 60) {
//    	            	showMessage("项目周期不能超过60天");
//    	                return ;
//    	            }
//    	        }
    	        var repayDays = $("input[name=repayDays]").val();
    	        if (!daysReg.test(repayDays)) {
    	        	showMessage("请输入正整数");
    	            return ;
    	        } else {
    	            if (parseInt(repayDays) > 60) {
    	            	showMessage("回报发放不能超过60天");
    	                return ;
    	            }
    	        }
    	        
//    	        var repayData = [];
//    	        var repayReg = /^[1-9]\d{0,2}$/;
//    	        var repayFlag = false;
//    	        $.each($(".repay-item"), function(idx, item){
//    	            var repayItem = {};
//    	            if ($(this).attr("data-id") != "") {
//    	            	repayItem["id"] = parseInt($(this).attr("data-id"));
//    	            }
//    	            if (projId) {
//    	            	repayItem["projId"] = projId;
//    	            };
//    	            
//    	            repayItem["amount"] = $(this).find("input[name=repay_input]").val();
//    	            if (!repayReg.test(repayItem["amount"])) {
//    	            	showMessage("项目回报金额必须为正整数");
//    	                repayFlag = true;
//    	                return false;
//    	            } else {
//    	                if (parseInt(repayItem["amount"]) > 250) {
//    	                	showMessage("项目单个支持金额不能超过250元");
//    	                    repayFlag = true;
//    	                    return false;
//    	                }
//    	            }
//    	            repayItem["content"] = $(this).find("textarea[name=repay_text]").val();
//    	            if ("" == repayItem["content"]) {
//    	                showMessage("请填写项目回报内容");
//    	                repayFlag = true;
//    	                return false;
//    	            }
//    	            repayItem["image"] = $(this).find(".img-entity").attr("src");
//    	            repayData.push(repayItem);
//    	        });
//    	        if (repayFlag) {
//    	            return ;
//    	        }

    	        $("#startup-btn").attr("disabled", true);

    	        var data = {
    	            "title": title,
    	            "intro": intro,
    	            "subject" : $(".cate-list span.selected").attr("data-type"),
    	            "content": um.getContent(),
    	            "coverImg": coverImg,
    	            "video":  $("textarea[name=projVideo]").val(),
//    	            "totalAmount": totalAmount,
//    	            "days": days,
    	            "repayDays": repayDays,
    	            "repayWay": $("input[name=repayRadio]:checked").val()
//    	            "repayStr" :  JSON.stringify(repayData)
    	        };
    	        if (projId) {
    	        	data["id"] = projId;
    	        }
    	        
    	        ajaxServices.startNewPost(data, function(data){
    	        	showMessage("项目发起成功, 请等待审核");
    	        	setTimeout(function() {
    	        		window.location= "/user/" + loginUserId;
    	        	}, 1000);
    	        });
    		} else {
    			showMessage("请先到个人页面完善个人资料后再发起项目");
    		}
    	});
    });

})(jQuery, window);