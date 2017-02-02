package com.fundin.web.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fundin.service.component.MessageHandler;
import com.fundin.service.impl.ActionService;
import com.fundin.service.impl.ProjService;
import com.fundin.service.impl.UserService;
import com.fundin.web.common.LoginContext;
import com.fundin.web.common.Resp;

@Controller
public class ActionController {

	private static final Logger LOG = LoggerFactory.getLogger(
			ActionController.class);
	
	@Resource
	private ActionService actionService;
	@Resource
	private UserService userService;
	@Resource
	private ProjService projService;
	@Resource
	private MessageHandler messageHandler;
	
	@RequestMapping(value = "/saveAction", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object saveAction(
			@RequestParam(value = "type", required = true) Integer type, 
			@RequestParam(value = "content", required = false) String content) {
		try {
			if (actionService.saveAction(
					LoginContext.getLoginContext().getUserId(), type, content)) {
				return Resp.succ(null);
			}
		} catch (Exception ex) {
			LOG.error("saveAction ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/saveUserAction", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object saveUserAction(
			@RequestParam(value = "type", required = true) Integer type, 
			@RequestParam(value = "projId", required = true) Long projId) {
		try {
			if (actionService.saveUserAction(
					LoginContext.getLoginContext().getUserId(), projId, type)) {
				return Resp.succ(null);
			}
		} catch (Exception ex) {
			LOG.error("saveUserAction ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/addNewAttention", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object addNewAttention(
			@RequestParam(value = "userId", required = true) Long userId) {
		try {
			if (actionService.addNewAttention(userId, 
					LoginContext.getLoginContext().getUserId())) {
				return Resp.succ(null);
			}
		} catch (Exception ex) {
			LOG.error("addNewAttention ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/cancleAttention", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object cancleAttention(
			@RequestParam(value = "userId", required = true) Long userId) {
		try {
			actionService.cancleAttention(userId, LoginContext.getLoginContext().getUserId());
			return Resp.succ(null);
		} catch (Exception ex) {
			LOG.error("cancleAttention ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/getLetterList", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getLetterList(
			@RequestParam(value = "userId", required = true) Long userId, 
			@RequestParam(value = "pageNum", required = true) Integer pageNum, 
			@RequestParam(value = "pageSize", required = true) Integer pageSize) {
		try {
			return Resp.succ(userService.getLetterList(userId, pageNum, pageSize));
		} catch (Exception ex) {
			LOG.error("getLetterList ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/getMyProjList", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getMyProjList(
			@RequestParam(value = "userId", required = true) Long userId, 
			@RequestParam(value = "pageNum", required = true) Integer pageNum, 
			@RequestParam(value = "pageSize", required = true) Integer pageSize) {
		try {
			return Resp.succ(projService.getMyProjList(userId, pageNum, pageSize));
		} catch (Exception ex) {
			LOG.error("getMyProjList ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/getMySupportList", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getMySupportList(
			@RequestParam(value = "userId", required = true) Long userId, 
			@RequestParam(value = "pageNum", required = true) Integer pageNum, 
			@RequestParam(value = "pageSize", required = true) Integer pageSize) {
		try {
			return Resp.succ(projService.getMySupportList(userId, pageNum, pageSize));
		} catch (Exception ex) {
			LOG.error("getMySupportList ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/getMyFollowList", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getMyFollowList(
			@RequestParam(value = "userId", required = true) Long userId, 
			@RequestParam(value = "pageNum", required = true) Integer pageNum, 
			@RequestParam(value = "pageSize", required = true) Integer pageSize) {
		try {
			return Resp.succ(projService.getMyFollowList(userId, pageNum, pageSize));
		} catch (Exception ex) {
			LOG.error("getMyFollowList ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/getAttentionList", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getAttentionList(
			@RequestParam(value = "userId", required = true) Long userId, 
			@RequestParam(value = "pageNum", required = true) Integer pageNum, 
			@RequestParam(value = "pageSize", required = true) Integer pageSize) {
		try {
			return Resp.succ(actionService.getByAttentionUserId(userId, pageNum, pageSize));
		} catch (Exception ex) {
			LOG.error("getAttentionList ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/getBeAttentionList", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getBeAttentionList(
			@RequestParam(value = "userId", required = true) Long userId, 
			@RequestParam(value = "pageNum", required = true) Integer pageNum, 
			@RequestParam(value = "pageSize", required = true) Integer pageSize) {
		try {
			return Resp.succ(actionService.getAttentionByUserId(userId, pageNum, pageSize));
		} catch (Exception ex) {
			LOG.error("getBeAttentionList ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/updateMessageStatus", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updateMessageStatus(
			@RequestParam(value = "type", required = false) String type, 
			@RequestParam(value = "projId", required = false) Long projId) {
		try {
			messageHandler.updateMessageStatus(
					LoginContext.getLoginContext().getUserId(), projId, type);
			return Resp.succ(null);
		} catch (Exception ex) {
			LOG.error("updateMessageStatus ex !", ex);
		}
		return Resp.fail(null);
	}
	
}