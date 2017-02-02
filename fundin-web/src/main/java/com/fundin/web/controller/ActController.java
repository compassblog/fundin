package com.fundin.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.fundin.service.impl.CommentService;
import com.fundin.service.impl.ProjService;
import com.fundin.service.impl.UserService;

@Controller
public class ActController{
	
//	private static final Logger LOG = LoggerFactory.getLogger(
//			ActionController.class);
//	private static int default_init_pagesize = 10;
	
	@Resource
	private ProjService projService;
	@Resource
	private CommentService commentService;
	@Resource
	private UserService userService;

//	@RequestMapping(value = "/getActProj", method = RequestMethod.POST, 
//			produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public Object getActProj(
//			@RequestParam(value = "subject", required = true) Integer subject, 
//			@RequestParam(value = "pageNum", required = true) Integer pageNum, 
//			@RequestParam(value = "pageSize", required = true) Integer pageSize) {
//		try {
//			return Resp.succ(projService.getActivityProjBySubject(subject, pageNum, pageSize));
//		} catch (Exception ex) {
//			LOG.error("getActProj ex !", ex);
//		}
//		return Resp.fail(null);
//	}
	
//	@RequestMapping(value = "/postcard")
//	public String postcard(
//			ModelMap model, HttpServletRequest request) {
//		model.put(Constants.Attrs.PAGE_TITLE, "众趣明信片设计大赛 - Fundin.cn");
//		model.put("page_size", default_init_pagesize);
//		
//		try {
//			model.put("projViewList", projService.getActivityProjBySubject(
//					10, 1, default_init_pagesize));
//		} catch (Exception ex) {
//			LOG.error("postcard ex !", ex);
//		}
//		
//		return "/act/postcard";
//		return "/act/postAward";
//	}

//	@RequestMapping(value = "/act/startPost")
//	public String startPost(ModelMap model, 
//			@RequestParam(value = "token", required = false) String token, 
//			@RequestParam(value = "proId", required = false) Long proId) {
//		model.put(Constants.Attrs.PAGE_TITLE, "众趣明信片设计大赛 - 创建项目");
//		
//		if (Constants.DEFAULT_TOKEN.equals(token)) {
//			FundinProj proj = projService.getProj(proId);
//			if (null != proj && 
//					proj.getStatus() == ProjStatusEnum.AUDITING.getCode()) {
//				model.put("proj", proj);
//				model.put("repayList", repayService.getRepayList(proj.getId()));
//			}
//		} else {
//			FundinProj proj = projService.getPostStartPro(
//					LoginContext.getLoginContext().getUserId(), 10);
//			if (null != proj) {
//				model.put("proj", proj);
//				model.put("repayList", repayService.getRepayList(proj.getId()));
//			}
//		}
//		
//		return "/act/startPost";
//	}
//	
//	@RequestMapping(value = "/act/savePostDraft", method = RequestMethod.POST, 
//			produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public Object savePostDraft(FundinProj proj) {
//		try {
//			proj.setUserId(LoginContext.getLoginContext().getUserId());
//			Long projId = projService.savePostDraft(proj);
//			if (null != projId) {
//				return Resp.succ(projId);
//			}
//		} catch (Exception ex) {
//			LOG.error("saveDraft ex !", ex);
//		}
//		return Resp.fail(null);
//	}
//	
//	@RequestMapping(value = "/act/startNewPost", method = RequestMethod.POST, 
//			produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public Object startNewPost(FundinProj proj) {
//		try {
//			proj.setUserId(LoginContext.getLoginContext().getUserId());
//			Long projId = projService.startNewPost(proj);
//			if (null != projId) {
//				return Resp.succ(projId);
//			}
//		} catch (Exception ex) {
//			LOG.error("startNew ex !", ex);
//		}
//		return Resp.fail(null);
//	}
//	
//	@RequestMapping(value = "/act/savePostEdit", method = RequestMethod.POST, 
//			produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public Object savePostEdit(FundinProj proj) {
//		try {
//			proj.setUserId(LoginContext.getLoginContext().getUserId());
//			if (projService.savePostEdit(proj)) {
//				return Resp.succ(null);
//			}
//		} catch (Exception ex) {
//			LOG.error("saveEdit ex !", ex);
//		}
//		return Resp.fail(null);
//	}
//	
//	@RequestMapping(value = "/postEdit/{projId}")
//	public String projEdit(@PathVariable("projId") Long projId, 
//			ModelMap model, HttpServletRequest request) {
//		try {
//			FundinProj proj = projService.getProj(projId);
//			if (!LoginContext.getLoginContext().isLoggedin() || 
//					!LoginContext.getLoginContext().getUserId().equals(proj.getUserId())) {
//				return "redirect:/static/html/404.html";
//			}
//			
//			model.put(Constants.Attrs.PAGE_TITLE, proj.getTitle() + " - Fundin.cn" );
//			model.put("proj", proj);
//			
//			model.put("fixed", false);
//			if (proj.getStatus() > 2 || proj.getRaisedRatio().floatValue() > 0.5) {
//				model.put("fixed", true);
//			}
//			
//			model.put("progressList", projService.getProgressList(projId));
//		} catch (Exception ex) {
//			LOG.error("projEdit ex !", ex);
//		}
//		return "/act/postEdit";
//	}
	
}

