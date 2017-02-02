(function($, window){

	$(document).ready(function(){
		/**
		 * nav导航效果
		 */
		$(".nav-left li").hover(
	        function(){
	            $(".nav-left li.active").attr('class', 'non-active');
	        },
	        function(){
	            $(".nav-left li.non-active").attr('class', 'active');
	        }
	    );
		
		/**
		 * 搜索功能
		 */
		var search = function() {
			var searchWord = $("#search-input").val().trim();
			if ("" == searchWord) {
				return ;
			}
			
			location.href = "/search?searchWord=" + searchWord;
		};
		$("#search-input").on('keydown', function(event) {
			if(event.keyCode == 13){
				search();
			}
		});
		$("#btn-search").on('click', function() {
			search();
		});
		
		/**
		 * 页面重载
		 */
		window.reloadPage = function() {
			setTimeout(function() {location.reload();}, 1000);
		};
		
		var putToMid = function(window, elem) {
			var window = $(window).width();
			var offset = ((window - $(elem).width()) / 2).toFixed(2);
			var offPercent = ((offset / window).toFixed(2)) * 100;
			$(elem).css("left", offPercent + "%");
		};
		
		/**
		 * 信息提示
		 */
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
		
		/**
		 * 确认取消对话框
		 */
		window.showOperation = function(opHint, callback, params) {
			$(".page-mask-light").show();
			$("#operation-content").text(opHint);
			
			putToMid(window, $(".operation-block"));
			$(".operation-block").show();
			
			$("#op-confirm").unbind("click");
			$("#op-confirm").on("click", function() {
				typeof callback == "function" && callback(params);
				$(".operation-block").hide();
				$(".page-mask-light").hide();
			});
		};
		
		$("#op-cancle").on("click", function() {
			$(".operation-block").hide();
			$(".page-mask-light").hide();
		});
		
		/**
		 * 返回顶部功能
		 */
	    $(window).on("scroll", function(){
	        if ($(window).scrollTop() > 30){
	            $(".top-ctrl").fadeIn(500);
	        } else {
	            $(".top-ctrl").fadeOut(500);
	        }
	    });
	    $(".top-ctrl").on("click", function(){
	        $('body,html').animate({scrollTop:0}, 200);
	        return false;
	    });

	    /**
		 * 意见反馈功能
		 */
	    $(".feedback").on("click", function () {
	        $(".page-mask").show();
	        $(".feedback-block").show();
	    });

	    $(".header i").on("click", function () {
	        closeFeedback();
	    });
	    var closeFeedback = function(){
	        $(".feedback-input").val("");
	        $(".feedback-block").hide();
	        $(".page-mask").hide();
	    };
	    
	    $("#feedback-btn").on("click", function () {
	        var feedback = $(".feedback-input").val().trim();
	        if ("" == feedback) {
	        	return ;
	        }
	        ajaxServices.saveAction({"type": 1, "content": feedback}, 
	        		function() {
			        	closeFeedback();
			        	showMessage("提交成功，谢谢您的反馈！");
			        });
	    });
	    
	    /**
	     * 微信关注
	     */
	    $(".qrcode").mouseover(function(){
	    	$(".qrcode-img").show();
	    });
	    $(".qrcode").mouseout(function(){
	    	$(".qrcode-img").hide();
	    });
	    
	    /**
	     * 忘记密码
	     */
	    $("#forget-pw").on("click",function(){
	    	var email = $("#login-email").val().trim();
	    	if("" == email){
	    		showMessage("请输入你的邮箱");
	    		return;
	    	}
	    	ajaxServices.forgetPasswd({"email":email},function(){
	    		showMessage("重置密码邮件已发送至"+email+", 请注意查收"  , 2000);
	    	},function(){
	    		showMessage("该邮箱没有注册，请核查邮箱是否填写正确" ,  2000)
	    	});
	    	
	    });
	    
	    /**
	     * 登录注册弹窗功能
	     */
	    window.showLogin = function(){
	        $(".page-mask").show();
	        $(".login-block").show();
	    };
	    var closeLogin = function(){
	        $("#login-val-form")[0].reset();
	        $("label.error").remove();
	        $(".login-block").hide();
	        $(".page-mask").hide();
	    };
	    
	    var showReg = function(){
	        $(".page-mask").show();
	        $(".reg-block").show();
	    };
	    var closeReg = function(){
	        $("#reg-val-form")[0].reset();
	        $("label.error").remove();
	        $(".reg-block").hide();
	        $(".page-mask").hide();
	    };
	    
	    /**
	     * 判断是否登录状态，展示登录窗
	     */
	    if ("false" == $("#loginStatus").val()) {
	    	$("#login-val-form").validate({
		        rules: {
		            loginEmail: {required: true, email: true},
		            loginPasswd: {required: true, minlength: 6}
		        },
		        messages: {
		            loginEmail: {required: "请输入Email地址", email: "请输入正确的email地址"},
		            loginPasswd: {required: "请输入密码", minlength: "密码不能小于6个字符"}
		        },
		        submitHandler: function (form) {
		            $("#btn-login").attr("disabled", true);
		            ajaxServices.login($(form).serialize(), 
		            	function(){
		                	location.reload();
			            }, function(){
			            	$("#btn-login").attr("disabled", false);
			            	showMessage("邮箱或密码错误");
			            });
		        }
		    });
	    	
	    	$("#reg-val-form").validate({
		        rules: {
		            regName : {required: true},
		            regEmail: {required: true, email: true},
		            regPasswd: {required: true, minlength: 6},
		            confirmPasswd: {required: true, minlength: 6}
		        },
		        messages: {
		            regName : {required: "请输入真实姓名或社团名称"},
		            regEmail: {required: "请输入Email地址", email: "请输入正确的email地址"},
		            regPasswd: {required: "请输入密码", minlength: "密码不能小于6个字符"},
		            confirmPasswd: {required: "请确认密码", minlength: "密码不能小于6个字符"}
		        },
		        submitHandler: function (form) {
		            $("#btn-reg").attr("disabled", true);
		            if ($("input[name=regPasswd]").val().trim() != 
		            		$("input[name=confirmPasswd]").val().trim()) {
		            	$("#btn-reg").attr("disabled", false);
		            	showMessage("两次输入的密码不一致");
		            	return ;
		            }
		            
		            ajaxServices.register($(form).serialize(), 
		            	function(){
			            	closeReg();
			            	showMessage("注册成功， 欢迎您来到Fundin！");
			            	reloadPage();
			            }, function(msg, data){
			            	$("#btn-reg").attr("disabled", false);
			            	showMessage(msg);
			            });
		        }
		    });
	    	
	    	$("#close-login").on("click", function() {
				closeLogin();
			});
			$("#close-reg").on("click", function() {
				closeReg();
			});
			
	    	$("#login-hint-a").on("click", function(){
		        closeReg();
		        showLogin();
		    });
		    $("#reg-hint-a").on("click", function(){
		        closeLogin();
		        showReg();
		    });
	    };
	    
	    /**
	     * 消息
	     */
	    if ($(".nav-message").length > 0) {
	    	$(".nav-message").on("click", function() {
				$(".messageDown").toggle();
			});
		    
		    $(".messageDown").css({
		    	"top": "48px", 
				"left": $(".nav-message").offset().left - 150});
			$(".messageDown .nav-message-link").on('click', function() {
		    	var data = {};
		    	data.type = $(this).attr('data-type');
		    	if ($(this).attr('data-projId') != "") {
		    		data.projId = $(this).attr('data-projId');
		    	}
		    	
		    	var $this = $(this);
		    	var count = $(this).attr('data-count');
		    	ajaxServices.updateMessageStatus(data, function(data) {
		    		$this.remove();
		    		$("#messageCount").text(parseInt($("#messageCount").text()) - count);
		    	});
		    });
			
			$("#messageAll").unbind('click');
			$("#messageAll").on('click', function() {
				ajaxServices.updateMessageStatus({}, function(data) {
					$(".messageDown").html("");
		    		$("#messageCount").text("0");
		    	});
			});
	    };
	    
		/**
		 * 登录
		 */
		$("#link-login").on("click", function() {
			showLogin();
		});
		
		/**
		 * 注册
		 */
		$("#link-register").on("click", function(){
			showReg();
		});
		
		/**
		 * 第一次登录
		 */
		if ($("#firstLoginArea").length > 0) {
			$("#firstLoginArea").css({
				"top": "16px", 
				"left": $("ul.nav-left").offset().left + $("ul.nav-left").width() + 8});
			
			var timer = setInterval(function(){
				$("#firstLoginArea").animate({"opacity": 0.4}, 800).animate({"opacity": 1}, 800);
			}, 1600);
			
			$("#closeFirst").on("click", function() {
				$("#firstLoginArea").remove();
				clearInterval(timer);
			});
		};
	});
	
})(jQuery, window);