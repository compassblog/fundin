'use strict';
define(["Zepto", "service", "utils"], function($, service, utils) {
	$(".message_cell").on('click',function() {
    	var data = {};
    	data.type = $(this).attr('data-type');
    	if ($(this).attr('data-projId') != "") {
    		data.projId = $(this).attr('data-projId');
    	}
    	
    	var $this = $(this);
    	var count = $(this).attr('data-count');
    	service.updateMessageStatus(data, function(data) {
    		$this.remove();
    	});
    });
})