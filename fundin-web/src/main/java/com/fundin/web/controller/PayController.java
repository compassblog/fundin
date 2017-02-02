package com.fundin.web.controller;

import java.io.BufferedReader;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fundin.service.impl.WXPayService;
import com.fundin.web.common.LoginContext;
import com.fundin.web.common.Resp;

@Controller
public class PayController {

	private static final Logger LOG = LoggerFactory.getLogger(
			PayController.class);
	
	@Resource
	private WXPayService wxPayService;
	
	@RequestMapping(value = "/pay/createNewWXPay", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object createNewWXPay(@RequestParam(value = "projId", required = true) Long projId,
			@RequestParam(value = "amount", required = true) Integer amount,
			@RequestParam(value = "rAmount", required = true) Integer rAmount) {
		try {
			return Resp.succ(
					wxPayService.createNewWXPay(projId, LoginContext.getLoginContext().getUserId(), amount, rAmount));
		} catch (Exception ex) {
			LOG.error("createNewWXPay ex !", ex);
		}
		return Resp.fail(null);
	}

	@RequestMapping(value = "/pay/createNewPay", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object createNewPay(
			@RequestParam(value = "projId", required = true) Long projId,
			@RequestParam(value = "rAmount", required = true) Integer rAmount,
			@RequestParam(value = "redIds", required = true) String redIds) {
		try {
			return Resp.succ(
					wxPayService.createNewPay(projId, LoginContext.getLoginContext().getUserId(), rAmount, redIds));
		} catch (Exception ex) {
			LOG.error("createNewWXPay ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/pay/getWXPayQRCode")
	@ResponseBody
	public void getWXPayQRCode(
			@RequestParam(value = "wxPayId", required = true) Long wxPayId, 
			HttpServletResponse response) {
		try {
			response.setContentType("image/png");
			wxPayService.getWXPayQRCode(wxPayId, response.getOutputStream());
		} catch (Exception ex) {
			LOG.error("getWXPayQRCode ex !", ex);
		}
	}
	
	@RequestMapping(value = "/pay/wxPayNotify", method = RequestMethod.POST, 
			produces = "application/xml;charset=UTF-8")
	@ResponseBody
	public String wxPayNotify(HttpServletRequest req) {
		try {
			return wxPayService.wxPayNotify(readContent(req));
		} catch (Exception ex) {
			LOG.error("wxPayNotify ex !", ex);
		}
		return null;
	}
	
	private String readContent(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = req.getReader();
			String line = null;
			while (null != (line = br.readLine())) {
				sb.append(line);
			}
		} catch (Exception e) {
			throw new RuntimeException("wxPayNotify readContent error !", e);
		} finally {
			IOUtils.closeQuietly(br);
		}
		return sb.toString();
	}
	
	@RequestMapping(value = "/pay/hasWXPayed", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object hasWXPayed(
			@RequestParam(value = "wxPayId", required = true) Long wxPayId, 
			HttpServletResponse response) {
		try {
			if (wxPayService.hasWXPayed(wxPayId)) {
				return Resp.succ(1);
			} else {
				return Resp.succ(0);
			}
		} catch (Exception ex) {
			LOG.error("hasWXPayed ex !", ex);
		}
		return Resp.fail(null);
	}
	
}
