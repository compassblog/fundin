'use strict';
define(["Zepto", "service", "utils"], function($, service, utils) {
	
	window.showDialog = function(dialogId) {
		var $dialog = $("#" + dialogId);
		$dialog.css("display", "block");
		$dialog.find("input[type=text]").select();
	};
	
	window.cancleDialog = function(cancle_btn) {
		$(cancle_btn).parent().parent().parent().css("display", "none");
	};
	
	var updateSomeInfo = function(data, conf_btn){
		service.updateSomeInfo(data, function() {
			$(conf_btn).parent().parent().parent().css("display", "none");
			utils.showToastMsg("已保存", null, null, function(){window.location.reload();});
		}, function(msg, data) {
			utils.showToastMsg(msg, "warn");
		});
	};
	
	var userId = $("#userid").val();
	
	$("#btn-sign").tap(function(){
		updateSomeInfo({"sign": $("#sign-text").val()}, this);
	});
	
	$("#btn-sex").tap(function(){
		updateSomeInfo({"sex": $("select[name=sex-select] option:selected").val()}, this);
	});
	
	$("#btn-phone").tap(function(){
		updateSomeInfo({"phone": $("#phone-text").val()}, this);
	});
	
	$("#btn-bankAccount").tap(function(){
		updateSomeInfo({"bankAccount": $("#bankAccount-text").val()}, this);
	});
	
	$("#btn-bankName").tap(function(){
		updateSomeInfo({"bankName": $("#bankName-text").val()}, this);
	});
	
	$("#btn-bankUserName").tap(function(){
		updateSomeInfo({"bankUserName": $("#bankUserName-text").val()}, this);
	});
	
	var findSelected = function(select, text) {
		var $options = $(select).children("option");
		$.each($options, function(idx, option){
			if ($(option).text().indexOf(text) > -1) {
				$(option).attr('selected', 'selected');
				return false;
			}
		});
	};
	
	$("#icon-input").on("change", function(){
	    var files = !!this.files ? this.files : [];
	    if (!files.length || !window.FileReader) return;
	    if (/^image/.test( files[0].type)){
	        var reader = new FileReader();
	        reader.readAsDataURL(files[0]);
	        reader.onloadend = function(){
	       $("#icon-preview").css("background-image", "url("+this.result+")");
	        }
	    }
	}); 
	
	$("#btn-icon").tap(function(){
		var fileObj = $("#icon-input").get(0).files[0];
		console.log(fileObj);
		var fd = new FormData();
		fd.append("imgFile",fileObj);
		$.ajax({
			type : 'post',
			url : '/uploadImage',
			data : fd,
			contentType:false,
			processData:false,
			success : function(resp){
				console.log('resp:' + resp.data);
				updateSomeInfo({"headImg": resp.data}, this);
			},
			error : function(){
				alert('Upload failed.');
				return false;
			}
		}) 
	});

	
	/**
	 * 生日数据
	 */
	if (birthdayData && birthdayData != "") {
    	var birthArr = $.trim(birthdayData).split("-");
    	findSelected($("#yearSelect"), birthArr[0]);
    	findSelected($("#monthSelect"), birthArr[1]);
    	findSelected($("#daySelect"), birthArr[2]);
    };
    
    $("#btn-birthday").tap(function(){
    	var year = $("#yearSelect option:selected").text();
    	var month = $("#monthSelect option:selected").text();
    	var day = $("#daySelect option:selected").text();
    	var birthday;
    	if (month != null && day != null) {
    		birthday = year + "-" + month + "-" + day;
    	} else {
    		birthday = year;
    	}
		updateSomeInfo({"birthday": birthday}, this);
	});
	
    /**
     * 学校数据
     */
    var cityArray = {};
    var $provinceSelect = $("#provinceSelect");
    var $citySelect = $("#citySelect");
    var $univSelect = $("#univSelect");

    var defaultOpt = "<option data-spec='true'>请选择</option>";
    $provinceSelect.html(defaultOpt);
    $citySelect.html(defaultOpt);
    $univSelect.html(defaultOpt);

    $provinceSelect.on('change', function(){
        var prov = $(this).children("option:selected").val();
        if (prov == "请选择") {
            $citySelect.html(defaultOpt);
            $univSelect.html(defaultOpt);
            return ;
        }

        var cityOpt = "<option data-spec='true'>请选择</option>";
        $.each(cityArray[prov], function (idx, cityVal) {
            cityOpt = cityOpt + "<option>" + cityVal + "</option>";
        });
        
        $citySelect.html(cityOpt);
        $univSelect.html(defaultOpt);
    });
    
    var isFirstProfile = true;
    
    $citySelect.on('change', function(){
        var city = $(this).children("option:selected").val();
        if (city == "请选择") {
            $univSelect.html(defaultOpt);
            return ;
        }

        service.getSchool4Univ({"univ": city}, function(data){
            var schoolOpt = "<option data-id='' data-spec='true'>请选择</option>";
            $.each(data, function (idx, schoolCol) {
            	schoolOpt = schoolOpt + "<option data-id='" + schoolCol.id + "'>" 
            		+ schoolCol.school + "</option>";
            });
            $univSelect.html(schoolOpt);
            var school = schoolModel.school;
            if (isFirstProfile && school != ""){
            	findSelected($univSelect, school);
            }
            isFirstProfile = false;
        });
    });
    
    service.getUnivArray({}, function(data){
        cityArray = data;
        var provinceOpt = "<option data-spec='true'>请选择</option>";
        $.each(cityArray, function (provKey, cityArr) {
            provinceOpt = provinceOpt + "<option>" + provKey + "</option>";
        });
        $provinceSelect.html(provinceOpt);
        
        if (schoolModel) {
            var prov = schoolModel.province;
            var univ = schoolModel.univ;
            var school = schoolModel.school;

            findSelected($provinceSelect, prov);
            $provinceSelect.trigger('change');
            findSelected($citySelect, univ);
            $citySelect.trigger('change');
        }
    });
    
    $("#btn-school").tap(function(){
    	var schoolId = $univSelect.children("option:selected").attr("data-id");
    	if (! schoolId || "" == schoolId) {
    		return ;
    	}
		updateSomeInfo({"schoolId": schoolId}, this);
	});
    
    /**
     * 入学年份数据
     */
    if (entryYearData && entryYearData != "") {
    	findSelected($("#entrySelect"), entryYearData);
    };
    
    $("#btn-entryYear").tap(function(){
		updateSomeInfo({"entryYear": $("select[name=entry-select] option:selected").text()}, this);
	});
    
    $("#btn-withdraw").tap(function(){
    	cancleDialog(this);
	});
    
    /**
     * 消息查看处理
     */
    
})