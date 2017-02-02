package com.fundin.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fundin.service.common.Constants;
import com.fundin.service.impl.OperService;

@Controller
public class OperController {

	private static final Logger LOG = LoggerFactory.getLogger(
			OperController.class);
	
	@Resource
	private OperService operService;
	
	@RequestMapping("/export/action")
	@ResponseBody
	public void exportAction(HttpServletResponse response, 
			@RequestParam(value = "token", required = true) String token) {
		if (!Constants.DEFAULT_TOKEN.equalsIgnoreCase(token)) {
			return ;
		}
		String templateName = "action_info.xls";
		processExportResponse(response, templateName);
		try {
			operService.exportAction(response.getOutputStream(), templateName);
		} catch (Exception e) {
			LOG.error("exportAction error!", e);
		}
	}
	
	@RequestMapping("/export/pay")
	@ResponseBody
	public void exportPay(HttpServletResponse response, 
			@RequestParam(value = "token", required = true) String token) {
		if (!Constants.DEFAULT_TOKEN.equalsIgnoreCase(token)) {
			return ;
		}
		String templateName = "pay_info.xls";
		processExportResponse(response, templateName);
		try {
			operService.exportPay(response.getOutputStream(), templateName);
		} catch (Exception e) {
			LOG.error("exportPay error!", e);
		}
	}
	
	@RequestMapping("/export/user")
	@ResponseBody
	public void exportUser(HttpServletResponse response, 
			@RequestParam(value = "token", required = true) String token) {
		if (!Constants.DEFAULT_TOKEN.equalsIgnoreCase(token)) {
			return ;
		}
		String templateName = "user_info.xls";
		processExportResponse(response, templateName);
		try {
			operService.exportUser(response.getOutputStream(), templateName);
		} catch (Exception e) {
			LOG.error("exportUser error!", e);
		}
	}
	
	@RequestMapping("/export/userCount")
	@ResponseBody
	public void exportUserCount(HttpServletResponse response, 
			@RequestParam(value = "token", required = true) String token) {
		if (!Constants.DEFAULT_TOKEN.equalsIgnoreCase(token)) {
			return ;
		}
		String templateName = "userCount_info.xls";
		processExportResponse(response, templateName);
		try {
			operService.exportUserCount(response.getOutputStream(), templateName);
		} catch (Exception e) {
			LOG.error("exportUserCount error!", e);
		}
	}

	private void processExportResponse(HttpServletResponse response, String exportName) {
		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-Disposition", "attachment;filename=\""
				+ exportName + "\"");
	}
	
//	@RequestMapping("/oper/sendEmailToAll")
//	@ResponseBody
//	public void sendEmailToAll(HttpServletResponse response, 
//			@RequestParam(value = "token", required = true) String token) {
//		if (!Constants.DEFAULT_TOKEN.equalsIgnoreCase(token)) {
//			return ;
//		}
//		try {
//			operService.sendEmailToAll();
//		} catch (Exception e) {
//			LOG.error("sendEmailToAll error!", e);
//		}
//	}
	
}
