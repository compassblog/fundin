(function($, window){
	
	$("#subject a").on("click", function() {
		$(this).attr("class", "chosen").siblings().removeClass("chosen");
		refreshSearch();
	});
	
	$("#require a").on("click", function() {
		$(this).attr("class", "chosen").siblings().removeClass("chosen");
		refreshSearch();
	});
	
	$("#location a").on("click", function() {
		$(this).attr("class", "chosen").siblings().removeClass("chosen");
		refreshSearch();
	});
	
	var refreshSearch = function() {
		location.href = "/search?subject=" + $("#subject a.chosen").attr("data-key") + 
				"&require=" + $("#require a.chosen").attr("data-key") +
				"&location=" + $("#location a.chosen").attr("data-key");
	};
	
})(jQuery, window);