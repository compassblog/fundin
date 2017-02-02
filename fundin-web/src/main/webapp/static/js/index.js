(function($, window){
    
	$('.banner').unslider({
        speed: 1000,               //  The speed to animate each slide (in milliseconds)
        delay: 3000,              //  The delay between slide animations (in milliseconds)
        complete: function() {},  //  A function that gets called after every slide animation
        keys: true,               //  Enable keyboard (left, right) arrow shortcuts
        dots: true,               //  Display dot navigation
        fluid: false              //  Support responsive design. May break non-responsive designs
    });

    $('.ck-slide').ckSlide({
		autoPlay: true,	//	默认为不自动播放，需要时请以此设置
		dir: 'x',	//	默认效果淡隐淡出，x为水平移动，y为垂直滚动
		interval:4500	//	默认间隔4500毫秒
	});
    
	var pageNum = 2;
	
    $(".load-more").on('click', function(){
    	ajaxServices.getProjViewListByPage({"pageNum": pageNum++, "pageSize": pageSize}, 
    			function(data) {
    		$.each(data, function(idx, projView) {
    			projView.raisedRatio = parseInt(parseFloat(projView.raisedRatio) * 100);
    		});
    		$(".load-more").before($("#proj-list-tmpl").tmpl({"projViewList": data}));
    		
    		if (data.length < pageSize) {
    			$(".load-more").remove();
    		}
    	});
    });
    
})(jQuery, window);