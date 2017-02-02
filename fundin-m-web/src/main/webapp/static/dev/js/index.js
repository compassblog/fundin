'use strict';
define(["Zepto"], function($) {
	$("#select-subject, #select-require, #select-location")
		.on("change", function() {
			var subject = $("#select-subject").find("option:selected").val();
			var require = $("#select-require").find("option:selected").val();
			var location = $("#select-location").find("option:selected").val();
			window.location.href = "/index?subject=" 
				+ subject + "&require=" + require + "&location=" + location;
		});
})