(function($, window){
	window.drawCenterLine = function() {
		$(".center-line").css("height", $(".wrapper").height() - $(".user-area").height() - 20);
	};
	drawCenterLine();
	
	var userId = $("#userId").val();
	var userType = $("#userType").val();
	var canEdit = parseInt($("#canEdit").val());

	/**
	 * 更改头像
	 */
	$("#headInput").on('change', function(){
        if($(this).val() == ""){
            return ;
        }
        $("#headForm").ajaxSubmit({
            url: "/uploadImage",
            dataType: 'json',
            success: function(resp){
                if (resp.code) {
                    $("#headImg").attr('src', resp.data);
                    $("#header-headImg").attr('src', resp.data);
                    ajaxServices.updateSomeInfo({"id": userId, "headImg": resp.data}, 
                    		function(data){});
                }
            }
        });
    });
	
	/**
	 * 关注功能
	 */
	$("#attention-link").on("click", function() {
		ajaxServices.addNewAttention({"userId": userId}, function() {
			location.reload();
		});
	});
	$("#cancle-attention-link").on("click", function() {
		showOperation("确定取消关注？", function(params) {
			ajaxServices.cancleAttention({"userId": userId}, function() {
				location.reload();
			});
		}, {});
	});
	
	/**
	 * 编辑资料
	 */
	$("#editInfo").on("click", function() {
		$(".page-mask").show();
		$("#editBlock").show();
	});
    $(".info-block i").on("click", function() {
    	$(this).parent().parent().hide();
    	$(".page-mask").hide();
    });
    
    $(".split-btn").on("click", function() {
    	$(this).addClass("selected").siblings().removeClass("selected");
    	$("#content-" + $(this).attr("content-id")).show()
    		.siblings(".info-content").hide();
    });
    
    $("#updatePersonalBtn").on("click", function() {
    	if(userType == "0"){
    		var sign = $("#signInput").val().trim();
        	if ("" == sign) {
        		showMessage("请填写个性签名！");
        		return ;
        	}
        	
        	var phone = $("#phoneInput").val().trim();
        	if ("" == phone) {
        		showMessage("请填写联系电话！");
        		return ;
        	}
        	
        	var schoolId = $("#univSelect option:selected").attr("data-id").trim();
        	if ("" == schoolId) {
        		showMessage("请选择学校！");
        		return ;
        	}
        	
        	var year = $("#yearSelect option:selected").text().trim();
        	var month = $("#monthSelect option:selected").text().trim();
        	var day = $("#daySelect option:selected").text().trim();
        	var birthday = year + "-" + month + "-" + day;
        	
        	var data = {
        		"sign": sign,
        		"phone": phone,
        		"schoolId": schoolId,
        		"sex": $("input[name=sexRadio]:checked").val(),
        		"entryYear": $("#entrySelect option:selected").text().trim(),
        		"birthday": birthday
        	};
        	
        	 ajaxServices.updatePersonalInfo(data, function() {
        		 showMessage("保存成功！");
        		 reloadPage();
        	 });
    	}
    	else{
    		var sign = $("#signInput").val().trim();
        	if ("" == sign) {
        		showMessage("请填写个性签名！");
        		return ;
        	}
        	
        	var phone = $("#phoneInput").val().trim();
        	if ("" == phone) {
        		showMessage("请填写联系电话！");
        		return ;
        	}
        	
        	var schoolId = $("#univSelect option:selected").attr("data-id");
        	if (!schoolId || "" == schoolId) {
        		showMessage("请选择学校！");
        		return ;
        	}
        	
        	var birthday = $("#yearSelect option:selected").text().trim();
        	
        	var data = {
        		"sign": sign,
        		"phone": phone,
        		"schoolId": schoolId,
        		"birthday": birthday
        	};
        	
        	 ajaxServices.updatePersonalInfo(data, function() {
        		 showMessage("保存成功！");
        		 reloadPage();
        	 });
    	}
    });
    
    /**
     * 银行账户信息
     */
    $("#updateBankAccountBtn").on("click",function(){
    	var bankAccount = $("#bankAccountInput").val().trim();
    	if ("" == bankAccount) {
    		showMessage("请填写银行账户！");
    		return ;
    	}
    	var bankName = $("#bankNameInput").val().trim();
    	if ("" == bankName) {
    		showMessage("请填写开户行名称！");
    		return ;
    	}
    	var bankUserName = $("#bankUserNameInput").val().trim();
    	if ("" == bankUserName) {
    		showMessage("请填写开户人姓名！");
    		return ;
    	}
    	
    	var data = {
    			"bankAccount" : bankAccount,
    			"bankUserName" : bankUserName,
    			"bankName" : bankName
    	};
    	
    	ajaxServices.updateBankAccountInfo(data, function(){
    		showMessage("保存成功！");
    		reloadPage();
    	});
    });
    
    /**
     * 修改密码
     */
    $("#updatePasswdBtn").on("click",function(){
    	var oldPasswd = $("#oldPasswdInput").val().trim();
    	var newPasswd = $("#newPasswdInput").val().trim();
    	var newPasswdComfirm = $("#newPasswdComfirmInput").val().trim();
    	
    	if("" == oldPasswd){
    		showMessage("请填写旧密码！");
    		return ;
    	}
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
    			"id" : userId,
    			"oldPasswd" : oldPasswd,
    			"newPasswd" : newPasswd
    	};
    	
    	ajaxServices.updatePasswd(data,function(){
    		showMessage("保存成功！");
    		reloadPage();
    	},function(){
    		showMessage("原密码输入有误！");
    	});
    	
    });
    
    $("#redPacketInfo").on("click", function() {
		$(".page-mask").show();
		$("#myRedPacket").show();
	});
    
    /**
     * 初始化用户信息
     */
    $(document).ready(function(){
    	if (birthdayData && birthdayData != "") {
        	var birthArr = $.trim(birthdayData).split("-");
        	$($("#yearSelect").children("option:contains('" +  birthArr[0]
        			+ "')")[0]).attr('selected', 'selected');
        	$($("#monthSelect").children("option:contains('" +  birthArr[1]
        		+ "')")[0]).attr('selected', 'selected');
        	$($("#daySelect").children("option:contains('" +  birthArr[2]
        		+ "')")[0]).attr('selected', 'selected');
        };
        
        if (entryYearData && entryYearData != "") {
        	$($("#entrySelect").children("option:contains('" + entryYearData + "')")[0])
        		.attr('selected', 'selected');
        };
        
        if (sexData && sexData != "") {
        	if (sexData == "1") {
        		$("#sex1").attr("checked", "checked");
        	}
        };
    });
    
    /**
     * 学校下拉框处理
     */
    var cityArray = {};
    var $provinceSelect = $("#provinceSelect");
    var $citySelect = $("#citySelect");
    var $univSelect = $("#univSelect");

    var defaultOpt = "<option data-spec='true'>请选择</option>";
    $provinceSelect.html(defaultOpt);
    $citySelect.html(defaultOpt);
    $univSelect.html(defaultOpt);

    ajaxServices.getUnivArray({}, function(data){
        cityArray = data;
        var provinceOpt = "<option data-spec='true'>请选择</option>";
        $.each(cityArray, function (provKey, cityArr) {
            provinceOpt = provinceOpt + "<option>" + provKey + "</option>";
        });
        $provinceSelect.html(provinceOpt);
        
        if(schoolModel){
            var prov = schoolModel.province;
            var univ = schoolModel.univ;
            var school = schoolModel.school;

            $($provinceSelect.children("option:contains('" + prov + "')")[0])
                .attr('selected', 'selected');
            $provinceSelect.trigger('change');
            $($citySelect.children("option:contains('" + univ + "')")[0])
                .attr('selected', 'selected');
            $citySelect.trigger('change');
            
            
        }
    });

    $provinceSelect.on('change', function(){
        var prov = $(this).children("option:selected").val();
        if(prov == "请选择") {
            $citySelect.html(defaultOpt);
            $univSelect.html(defaultOpt);
            return ;
        }

        var cityOpt = "<option data-spec='true'>请选择</option>";
        $.each(cityArray[prov], function (idx, cityVal) {
            cityOpt = cityOpt + "<option>"
                + cityVal + "</option>";
        });
        
        $citySelect.html(cityOpt);
        $univSelect.html(defaultOpt);
    });
    
    var isFirstProfile = true;
    
    $citySelect.on('change', function(){
        var city = $(this).children("option:selected").val();
        if(city == "请选择") {
            $univSelect.html(defaultOpt);
            return ;
        }

        ajaxServices.getSchool4Univ({"univ": city}, function(data){
            var schoolOpt = "<option data-id='' data-spec='true'>请选择</option>";
            $.each(data, function (idx, schoolCol) {
            	schoolOpt = schoolOpt + "<option data-id='" + schoolCol.id + "'>" 
            		+ schoolCol.school + "</option>";
            });
            $univSelect.html(schoolOpt);
            var school = schoolModel.school;
            if (isFirstProfile && school != ""){
            	$($univSelect.children("option:contains('" + school + "')")[0])
            		.attr('selected', 'selected');
            }
            isFirstProfile = false;
        });
    });
    
    /**
     * 申请提现
     */
    $("#requestMoney").on("click", function() {
    	if($("#bankAccountInput").val().trim() == "" || $("#bankNameInput").val().trim() == "" || $("#bankUserNameInput").val().trim() == ""){
    		showMessage("请在编辑资料中补充账户信息！");
    	}
    	else if($("#phoneInput").val().trim() == ""){
    		showMessage("请在编辑资料中补充联系电话！");
    	}
    	else{
    		$(".page-mask").show();
    		$("#accountConfirmBlock").show();
    	}

    });
    $("#applyWithdrawalBtn").on("click",function(){
    		ajaxServices.saveAction({"type": 2}, function() {
    		showMessage("您已成功提交提现申请，预计3～7日到账");
    	});
    });
    
    /**
     * 站内信
     */
    var bindLetterEvent = function() {
    	$(".c-body .letter").unbind("click");
		$(".c-body .letter").on('click', function() {
	    	$("#letter-title").text($(this).find(".letter-title").text());
	    	$("#letter-content").html($(this).attr("data-content"));
	    	$(".page-mask").show();
	    	$("#letterBlock").show();
	    });
    };
    bindLetterEvent();
    
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
    		var $cbody = $(this).parent().siblings(".c-body");
    		var $pageInfo = $(this).siblings("#pageInfo");
    		var dataTmpl = $pageInfo.attr('data-tmpl');
    		var $dataTmpl = $("#" + dataTmpl);
    		ajaxServices[$pageInfo.attr('data-ajax')]({"userId": userId, "pageNum": pageNum - 1, 
    			"pageSize": $pageInfo.attr("data-page-size")}, function(data) {
    				if (canEdit == 1 && "myProj-tmpl" == dataTmpl) {
    					$.each(data, function(idx, item) {
    						item.canEdit = 1;
    					});
    				}
    				
    				$cbody.html($dataTmpl.tmpl({"dataList": data.elements}));
    				$this.siblings("#pageNum").text(data.pageNum);
    				$this.siblings("#pageCount").text(data.pageCount);
    				drawCenterLine();
    				
    				if ("letter-tmpl" == dataTmpl) {
    					bindLetterEvent();
    				}
    		});
    	});
    	
    	$(pageItem).find("#next").on("click", function() {
    		var pageNum = parseInt($(this).siblings("#pageNum").text());
    		var pageCount = parseInt($(this).siblings("#pageCount").text());
    		if (pageNum == pageCount) {
    			return ;
    		}
    		
    		var $this = $(this);
    		var $cbody = $this.parent().siblings(".c-body");
    		var $pageInfo = $this.siblings("#pageInfo");
    		var dataTmpl = $pageInfo.attr('data-tmpl');
    		var $dataTmpl = $("#" + dataTmpl);
    		ajaxServices[$pageInfo.attr('data-ajax')]({"userId": userId, "pageNum": pageNum + 1, 
    			"pageSize": $pageInfo.attr("data-page-size")}, function(data) {
    				if (canEdit == 1 && "myProj-tmpl" == dataTmpl) {
    					$.each(data, function(idx, item) {
    						item.canEdit = 1;
    					});
    				}
    				
    				$cbody.html($dataTmpl.tmpl({"dataList": data.elements}));
    				$this.siblings("#pageNum").text(data.pageNum);
    				$this.siblings("#pageCount").text(data.pageCount);
    				drawCenterLine();
    				
    				if ("letter-tmpl" == dataTmpl) {
    					bindLetterEvent();
    				}
    		});
    	});
    });
})(jQuery, window);