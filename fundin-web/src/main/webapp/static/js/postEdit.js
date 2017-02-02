(function($, window){
	
	var projId = parseInt($("#projId").val());
	var fixed = $("#fixed").val();
	
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
    
    if (window.repayWayData != "") {
    	var repayWay = parseInt(window.repayWayData);
    	if (repayWay == 1 || repayWay == 2) {
    		$("#repayWay" + repayWay).attr("checked", "checked");
    	}
    };
    $(".intro").text($("textarea[name=projIntro]").val().trim());
    $(".cover").html("<img src='" + $("#coverImg").attr('src') + "'>");
    
    if (fixed == "false") {
    	$("textarea[name=projIntro]").on("keyup", function() {
        	$(".intro").text($(this).val());
        });
    	
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
    }
	
    /**
     * 	add new progress
     */
    $("#edit-on").on("click", function() {
		$(".edit-input").val("").show();
		$("#save-on").show();
		$(this).hide();
	});
    
	$("#save-on").on("click", function() {
		if ($(".edit-input").val().trim() == "") {
			return ;
		}
		ajaxServices.addNewProgress({"projId": projId, "progressDesc": 
			$(".edit-input").val().trim()}, function(data) {
				var newProgress = "<p class='progress-item' data-id='" + data.id + "'>" 
					+ "<span class='progress-circle'></span> " 
					+ "<span class='progress-time'>" + data.timeStr + "</span> "
					+ "<span class='progress-desc'>" + data.progressDesc + "</span> ";
				$(".p-edit-block").append(newProgress);
				
				$(".edit-input").val("").hide();
				$("#edit-on").show();
				$("#save-on").hide();
				showMessage("新增进度成功");
		})
	});
	
    /**
     * 	project save-edit submit
     */
    $("#save-edit-btn").on('click', function() {
//    	var repayData = [];
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
//                    showMessage("项目单个回报金额不能超过250元");
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
        	"id": projId,
            "intro": $("textarea[name=projIntro]").val(),
            "content": um.getContent(),
            "coverImg": $("#coverImg").attr("src"),
            "video":  $("textarea[name=projVideo]").val(),
            "repayWay": $("input[name=repayRadio]:checked").val()
        };
        
        ajaxServices.savePostEdit(data, function(data){
        	showMessage("保存更改成功");
        	setTimeout(function() {
        		window.location= "/proj/" + projId;
        	}, 1000);
        });
    });

})(jQuery, window);