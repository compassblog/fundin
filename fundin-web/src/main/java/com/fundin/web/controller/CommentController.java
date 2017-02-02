package com.fundin.web.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fundin.domain.dto.CommentView;
import com.fundin.domain.entity.FundinComment;
import com.fundin.domain.entity.FundinUser;
import com.fundin.service.impl.CommentService;
import com.fundin.service.impl.UserService;
import com.fundin.utils.mapper.BeanMapper;
import com.fundin.web.common.LoginContext;
import com.fundin.web.common.Resp;

@Controller
public class CommentController {

	private static final Logger LOG = LoggerFactory.getLogger(
			CommentController.class);
	
	@Resource
	private CommentService commentService;
	@Resource
	private UserService userService;
	
	@RequestMapping(value = "/comment/newComment", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object newComment(
			@RequestParam(value = "projId", required = true) Long projId,
			@RequestParam(value = "replyToId", required = false) Long replyToId,
			@RequestParam(value = "replyUserId", required = false) Long replyUserId,
			@RequestParam(value = "msg", required = true) String msg) {
		try {
			FundinComment comment = commentService.newComment(LoginContext.getLoginContext().getUserId(), 
					projId, replyToId, replyUserId, msg);
			if (null != comment) {
				CommentView commentView = BeanMapper.map(comment, CommentView.class);
				commentView.setUserName(LoginContext.getLoginContext().getUserName());
				commentView.setUserHeadImg(LoginContext.getLoginContext().getUserHeadImg());
				
				if (null != commentView.getReplyUserId()) {
					FundinUser replyUser = userService.queryInfo(commentView.getReplyUserId());
					commentView.setReplyUserName(replyUser.getName());
				}
				
				return Resp.succ(commentView);
			}
		} catch (Exception ex) {
			LOG.error("newComment ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/comment/delComment", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object delComment(
			@RequestParam(value = "commentId", required = true) Long commentId) {
		try {
			commentService.delComment(commentId);
			return Resp.succ(null);
		} catch (Exception ex) {
			LOG.error("delComment ex !", ex);
		}
		return Resp.fail(null);
	}
	
}