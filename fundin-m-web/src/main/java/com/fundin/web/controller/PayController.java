package com.fundin.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fundin.domain.entity.FundinProj;
import com.fundin.pay.wx.WXPayConstants;
import com.fundin.service.common.ImagePathConventer;
import com.fundin.service.impl.ProjService;
import com.fundin.service.impl.RedPacketService;
import com.fundin.service.impl.WXPayService;
import com.fundin.web.common.HttpHelper;
import com.fundin.web.common.MContext;
import com.fundin.web.common.Resp;
import com.fundin.web.wechat.WXAuthToken;
import com.fundin.web.wechat.WXJsApiConfig;

@Controller
public class PayController extends BaseController {

	private static final Logger LOG = LoggerFactory.getLogger(PayController.class);
	
	@Resource
	private ProjService projService;
	@Resource
	private WXPayService wxPayService;
	@Resource
	private RedPacketService redPacketService;
	
	@RequestMapping(value = "/wx/getJsApiConfig", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getJsApiConfig(
			@RequestParam(value = "url", required = true) String url) {
		try {
			return Resp.succ(WXJsApiConfig.getJsApiConfig(
					url, WXJsApiConfig.JS_API_LIST));
		} catch (Exception e) {
			LOG.error("getJsApiConfig error !", e);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/pay/{projId}")
	public String pay(@PathVariable("projId") Long projId, 
			@RequestParam(value = "code", required = true) String code, 
			@RequestParam(value = "state", required = true) Integer state, 
			ModelMap model, HttpServletRequest request) {
		try {
			if (! MContext.getMContext().isLoggedin()) {
				return "redirect:/m/login?returnUrl=http://m.fundin.cn/proj/" + projId;
			}
			
			String openid = WXAuthToken.getOpenidByCode(code);
			if (StringUtils.isBlank(openid)) {
				return "redirect:/m/login?returnUrl=http://m.fundin.cn/proj/" + projId;
			}
			model.put("openid", openid);
			
			FundinProj proj = projService.getProj(projId);
			addPageTitle(model,  proj.getTitle() + " - Fundin.cn");
			proj.setCoverImg(ImagePathConventer.convert2Middle(proj.getCoverImg()));
			model.put("proj", proj);
			model.put("redPacketList", redPacketService.getMyRedPacket(MContext.getMContext().getUserId(), projId));

			model.put("appId", WXPayConstants.WX_APP_ID);
		} catch (Exception ex) {
			LOG.error("pay ex !", ex);
		}
		return "pay";
	}
	
	@RequestMapping(value = "/pay/createNewWXPayForM", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object createNewWXPay(
			@RequestParam(value = "projId", required = true) Long projId, 
			@RequestParam(value = "openid", required = true) String openid,
			@RequestParam(value = "rAmount", required = true) Integer rAmount,
			@RequestParam(value = "amount", required = true) Integer amount,
			HttpServletRequest request) {
		try {
			if (projService.hasSupport(MContext.getMContext().getUserId(), projId)) {
				return Resp.fail("您已支持过该项目");
			}
			String prepay_id = wxPayService.createNewWXPayForM(
					projId, MContext.getMContext().getUserId(), openid, HttpHelper.getIp(request),amount,rAmount);
			if (StringUtils.isBlank(prepay_id)) {
				return Resp.fail("生成支付订单失败");
			}
			return Resp.succ(WXAuthToken.obtainWXPay(prepay_id));
		} catch (Exception ex) {
			LOG.error("createNewWXPayForM ex !", ex);
		}
		return Resp.fail("生成支付订单失败");
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
					wxPayService.createNewPay(projId, MContext.getMContext().getUserId(), rAmount, redIds));
		} catch (Exception ex) {
			LOG.error("createNewWXPay ex !", ex);
		}
		return Resp.fail(null);
	}
}
