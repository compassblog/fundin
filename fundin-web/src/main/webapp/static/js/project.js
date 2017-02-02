(function($, window){

	var projectId = parseInt($("#projectId").val());
	
	/**
	 * 	关注和点赞功能
	 */
	$("#follow").on("click", function(){
        ajaxServices.saveUserAction({"projId": projectId, "type": 1}, 
        		function(){
        			location.reload();
        		});
    });
    $("#good").on("click", function(){
        ajaxServices.saveUserAction({"projId": projectId, "type": 2}, 
        		function(){
        			location.reload();
        		});
    });
    
    /**
	 * 	支持项目功能
	 */
    $(".btn-support").on("click", function(){
    	if ("false" == $("#loginStatus").val()) {
    		window.showLogin();
    		return ;
    	}
    	
    	ajaxServices.hasPersonalInfo({}, function(data) {
    		if (data) {
    	    	$(".page-mask").show();
    	        $(".pay-block").show();
    		} else {
    			showMessage("请先到个人页面完善个人基本资料后再支持该项目");
    		}
    	});
    });
    
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
    	$('input:checkbox[name=redpacket]:checked').each(function(){
    		curRepayAmount -= $(this).val();
    	});
    	if(curRepayAmount <= 0)
    		$(".amount").text(0 + "元");
    	else
    		$(".amount").text(curRepayAmount + "元");
    	
    	computeAfter = curRepayAmount;
    }
    
    $('input:checkbox[name=redpacket]').click(function(){
		if($(this).prop('checked'))
			computeAfter = Number(computeAfter) - Number($(this).val());
        else
        	computeAfter = Number(computeAfter) + Number($(this).val());
		if(computeAfter < 0)
			$(".amount").text(0 + "元");
		else
			$(".amount").text(computeAfter + "元");
		check();
    });
    
    function check(){
    	var curRepayAmount = $("#payAmount").val();
    	var redPackAmount = 0 ;
    	$('input:checkbox[name=redpacket]:checked').each(function(){
    		redPackAmount = Number(redPackAmount) + Number($(this).val());
    		curRepayAmount -= $(this).val();
    	});
    	if(curRepayAmount <= 0){
    		$("#payAmount").val(redPackAmount);
    		computeAfter = 0;
    	}
    }
    
    $(".pay-btn").on("click", function () {
    	var rAmount = 0;
    	var redIds = '';
    	$('input:checkbox[name=redpacket]:checked').each(function(){
    		rAmount += Number($(this).val());
    		redIds += $(this).attr("id").substring(9) + "_";
    	});

    	if(computeAfter > 0){
    		 ajaxServices.createNewWXPay({"projId": projectId,"amount":computeAfter,
    			 "rAmount":rAmount}, function(data){
    	        	$(".amount").text(computeAfter + "元");
    	        	$(".QR-img").attr("src", "/pay/getWXPayQRCode?wxPayId=" + data);
    	        	
    	        	$(".pay-block").hide();
    	        	$(".QR-block").show();
    	        	
    	        	wxPayId = data;
    	        	setTimeout(queryWXPayed, 3000);
    	        });
    	} else if(rAmount > 0 && computeAfter <= 0) {
    		ajaxServices.createNewPay({"projId": projectId,
   			 "rAmount":rAmount,"redIds":redIds}, function(date) {
    			if (date) {
    				showMessage("支付成功，感谢您的支持，请持续关注项目后续发展！");
    				reloadPage();
    			} else {
    				showMessage("支付失败！请联系客服帮助解决！");
    				reloadPage();
    			}
    		});
    	}
    });
    
    var wxPayId;
    var queryWXPayed = function(){
		ajaxServices.hasWXPayed({"wxPayId": wxPayId}, function(hasWXPayed) {
			if (hasWXPayed) {
				showMessage("支付成功，感谢您的支持，请持续关注项目后续发展！");
				reloadPage();
			} else {
				setTimeout(queryWXPayed, 3000);
			}
		});
    };
    
    $(".top1 i").on("click", function () {
        $(".pay-block").hide();
        $(".page-mask").hide();
    });
    
    $(".top2 i").on("click", function () {
        $(".QR-block").hide();
        $(".page-mask").hide();
    });
    
    /**
     * 评论互动功能
     */
    $("#comment-btn").on('click',function(){
        var comment = $("#new-comment-text").val().trim();
        if ("" == comment) {
            return ;
        }

        ajaxServices.newComment({"projId" : projectId, "msg" : comment}, 
        		function(data) {
            		location.reload();
        		});
    });

    $(".link-reply").on('click',function(){
    	$(".reply-comment").remove();
        var replyCommentHtml = "<div class='reply-comment'>\
	            <div class='user-head'>\
	                <img src='" + $(".add-comment .user-head img").attr("src") + "'>\
	            </div>\
	            <div class='comment-form'>\
	                <textarea placeholder='说点什么吧'\
	                    maxlength='100' name='comment-input'></textarea>\
	                <input type='button' class='reply-btn' data-id='" + $(this).attr('data-id')
	                    + "' data-user-id='" + $(this).attr('data-user-id') + "' value='回复'>\
	            </div>\
	            </div>";
        
        $(this).parent().parent().append(replyCommentHtml);
        bindOneReply();
    });

    var bindOneReply = function() {
        $(".reply-btn").on('click', function() {
            var reply = $(this).siblings("textarea").val().trim();
            if ("" == reply) {
                return ;
            }

            ajaxServices.newComment({"projId" : projectId, 
            	"replyToId": $(this).attr('data-id'), 
            	"replyUserId": $(this).attr('data-user-id'), 
            	"msg" : reply}, function(data) {
            		location.reload();
            });
        });
    };
    
    $(".link-del").on('click',function(){
    	ajaxServices.delComment({"commentId" : $(this).attr('data-id')}, 
    			function(data) {
        			location.reload();
        		});
    });

    /**
     * 分页显示
     */
    $.each($(".c-page"), function(idx, pageItem) {
    	$(pageItem).find("#prev").on("click", function() {
    		var pageNum = parseInt($(this).siblings("#pageNum").text());
    		if (pageNum == 1) {
    			return ;
    		}
    		
    		var $this = $(this);
    		var $cbody = $(this).parent().siblings(".support-body");
    		var $pageInfo = $(this).siblings("#pageInfo");
    		var dataTmpl = $pageInfo.attr('data-tmpl');
    		var $dataTmpl = $("#" + dataTmpl);
    		ajaxServices[$pageInfo.attr('data-ajax')]({"projId": projectId, "pageNum": pageNum - 1, 
    			"pageSize": $pageInfo.attr("data-page-size")}, function(data) {
    				
    				$cbody.html($dataTmpl.tmpl({"dataList": data.elements}));
    				$this.siblings("#pageNum").text(data.pageNum);
    				$this.siblings("#pageCount").text(data.pageCount);
    				
    		});
    	});
    	
    	$(pageItem).find("#next").on("click", function() {
    		var pageNum = parseInt($(this).siblings("#pageNum").text());
    		var pageCount = parseInt($(this).siblings("#pageCount").text());
    		if (pageNum == pageCount) {
    			return ;
    		}
    		
    		var $this = $(this);
    		var $cbody = $this.parent().siblings(".support-body");
    		var $pageInfo = $this.siblings("#pageInfo");
    		var dataTmpl = $pageInfo.attr('data-tmpl');
    		var $dataTmpl = $("#" + dataTmpl);
    		ajaxServices[$pageInfo.attr('data-ajax')]({"projId": projectId, "pageNum": pageNum + 1, 
    			"pageSize": $pageInfo.attr("data-page-size")}, function(data) {
    				
    				$cbody.html($dataTmpl.tmpl({"dataList": data.elements}));
    				$this.siblings("#pageNum").text(data.pageNum);
    				$this.siblings("#pageCount").text(data.pageCount);

    		});
    	});
    });
})(jQuery, window);