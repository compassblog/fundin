package com.fundin.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.fundin.domain.entity.FundinProjProgress;
import com.fundin.domain.type.ProjStatusEnum;
import com.fundin.service.common.Constants;
import com.fundin.service.impl.CommentService;
import com.fundin.service.impl.ProjService;
import com.fundin.service.impl.RedPacketService;
import com.fundin.service.impl.UserService;
import com.fundin.web.common.LoginContext;
import com.fundin.web.common.Resp;

@Controller
public class ProjController {

	private static final Logger LOG = LoggerFactory.getLogger(
			ProjController.class);
	
	@Resource
	private ProjService projService;
	@Resource
	private CommentService commentService;
	@Resource
	private UserService userService;
	@Resource
	private RedPacketService redPacketService;
	
	@RequestMapping(value = "/proj/startup")
	public String startup(ModelMap model) {
		model.put(Constants.Attrs.NAV_HEAD, "startup");
		model.put(Constants.Attrs.PAGE_TITLE, "发起项目 - Fundin.cn");
		
		FundinProj proj = projService.getStartingProj(
				LoginContext.getLoginContext().getUserId());
		if (null != proj) {
			model.put("proj", proj);
		}
		return "startup";
	}
	
	@RequestMapping(value = "/proj/saveDraft", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object saveDraft(FundinProj proj) {
		try {
			proj.setUserId(LoginContext.getLoginContext().getUserId());
			Long projId = projService.saveDraft(proj);
			if (null != projId) {
				return Resp.succ(projId);
			}
		} catch (Exception ex) {
			LOG.error("saveDraft ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/proj/startNew", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object startNew(FundinProj proj) {
		try {
			proj.setUserId(LoginContext.getLoginContext().getUserId());
			Long projId = projService.startNew(proj);
			if (null != projId) {
				return Resp.succ(projId);
			}
		} catch (Exception ex) {
			LOG.error("startNew ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/proj/saveEdit", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object saveEdit(FundinProj proj) {
		try {
			proj.setUserId(LoginContext.getLoginContext().getUserId());
			if (projService.saveEdit(proj)) {
				return Resp.succ(null);
			}
		} catch (Exception ex) {
			LOG.error("saveEdit ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/proj/auditRet", method = RequestMethod.GET, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object auditRet(
			@RequestParam(value = "projId", required = true) Long projId, 
			@RequestParam(value = "ret", required = true) Integer ret, 
			@RequestParam(value = "token", required = true) String token, 
			HttpServletResponse response) {
		try {
			if (!Constants.DEFAULT_TOKEN.equals(token)) {
				response.getWriter().write("error token.");
				return null;
			}
			
			projService.auditRet(projId, ret);
			return Resp.succ(null);
		} catch (Exception ex) {
			LOG.error("auditRet ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/projEdit/{projId}")
	public String projEdit(@PathVariable("projId") Long projId, 
			@RequestParam(value = "token", required = false) String token, 
			ModelMap model, HttpServletRequest request) {
		try {
			FundinProj proj = projService.getProj(projId);
			if (!Constants.DEFAULT_TOKEN.equals(token) && 
					(!LoginContext.getLoginContext().isLoggedin() || 
					!LoginContext.getLoginContext().getUserId().equals(
							proj.getUserId()))) {
				return "redirect:/static/html/404.html";
			}
			
			model.put(Constants.Attrs.PAGE_TITLE, proj.getTitle() + " - Fundin.cn" );
			model.put("proj", proj);
			
			model.put("fixed", false);
			if (proj.getStatus() > 2 || proj.getRaisedRatio().floatValue() > 0.5) {
				model.put("fixed", true);
			}
			
			model.put("progressList", projService.getProgressList(projId));
		} catch (Exception ex) {
			LOG.error("projEdit ex !", ex);
		}
		return "projEdit";
	}
	
	@RequestMapping(value = "/proj/{projId}")
	public String projPage(@PathVariable("projId") Long projId, 
			@RequestParam(value = "token", required = false) String token, 
			ModelMap model, HttpServletRequest request) {
		try {
			FundinProj proj = projService.getProj(projId);
			if (null == proj) {
				return "redirect:/static/html/404.html";
			}
			if (ProjStatusEnum.AUDITING.getCode() == proj.getStatus() && 
					!Constants.DEFAULT_TOKEN.equals(token)) {
				return "redirect:/static/html/404.html";
			}
			
			model.put(Constants.Attrs.PAGE_TITLE,  proj.getTitle() + " - Fundin.cn");
			model.put("proj", proj);
			
			model.put("canEdit", false);
			if (proj.getUserId().equals(LoginContext.getLoginContext().getUserId())) {
				model.put("canEdit", true);
			}
			
			model.put("projInfo", projService.getProjInfo(proj, LoginContext.getLoginContext().getUserId()));
			model.put("progressList", projService.getProgressList(projId));
			
			model.put("user", userService.queryInfo(proj.getUserId()));
			model.put("redPacketList", redPacketService.getMyRedPacket(LoginContext.getLoginContext().getUserId(), projId));
			model.put("repayUserList", projService.getRepayUserList(projId, 1, 10));
			model.put("commentViewList", commentService.getCommentViewList(projId));
		} catch (Exception ex) {
			LOG.error("projPage ex !", ex);
		}
		return "projPage";
	}
	
	@RequestMapping(value = "/proj/addNewProgress", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object addNewProgress(FundinProjProgress progress) {
		try {
			progress.setUserId(LoginContext.getLoginContext().getUserId());
			FundinProjProgress newProgress = projService.addNewProgress(progress);
			if (null != newProgress) {
				return Resp.succ(newProgress);
			}
		} catch (Exception ex) {
			LOG.error("addNewProgress ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/getRepayUserList", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getRepayUserList(
			@RequestParam(value = "projId", required = true) Long projId, 
			@RequestParam(value = "pageNum", required = true) Integer pageNum, 
			@RequestParam(value = "pageSize", required = true) Integer pageSize) {
		try {
			return Resp.succ(projService.getRepayUserList(projId, pageNum, pageSize));
		} catch (Exception ex) {
			LOG.error("getRepayUserList ex !", ex);
		}
		return Resp.fail(null);
	}
	
}
