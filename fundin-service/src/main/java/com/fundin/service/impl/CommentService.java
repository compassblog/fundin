package com.fundin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fundin.dao.mybatis.FundinCommentDao;
import com.fundin.domain.dto.CommentView;
import com.fundin.domain.entity.FundinComment;
import com.fundin.domain.entity.FundinUser;
import com.fundin.service.common.Constants;
import com.fundin.service.common.DateConverter;
import com.fundin.service.component.MessageHandler;
import com.fundin.utils.common.Clock;
import com.fundin.utils.mapper.BeanMapper;

@Component
public class CommentService {

	@Resource
	private FundinCommentDao commentDao;
	@Resource
	private UserService userService;
	@Resource
	private MessageHandler messageHandler;
	
	@Transactional(rollbackFor = Exception.class)
	public FundinComment newComment(Long userId, Long projId, Long replyToId,
			Long replyUserId, String msg) {
		FundinComment comment = new FundinComment();
		comment.setUserId(userId);
		comment.setProjId(projId);
		comment.setReplyToId(replyToId);
		comment.setReplyUserId(replyUserId);
		comment.setMsg(msg);
		comment.setCreateTime(Clock.DEFAULT.getCurrentDate());
		
		if (Constants.INSERT_SUCCESS != commentDao.newComment(comment)) {
			return null;
		}
		messageHandler.createCommentMessage(replyUserId, projId);
		return comment;
	}

	public List<FundinComment> getCommentList(Long projId) {
		return commentDao.getCommentList(projId);
	}

	public List<CommentView> getCommentViewList(Long projId) {
		List<FundinComment> commentList = getCommentList(projId);
		List<CommentView> commentViewList = new ArrayList<CommentView>();
		
		for (FundinComment comment : commentList) {
			CommentView view = BeanMapper.map(comment, CommentView.class);
			
			FundinUser user = userService.queryInfo(view.getUserId());
			view.setUserName(user.getName());
			view.setUserHeadImg(user.getHeadImg());
			if (null != view.getReplyUserId()) {
				FundinUser replyUser = userService.queryInfo(view.getReplyUserId());
				view.setReplyUserName(replyUser.getName());
			}
			
			view.setTime(DateConverter.convertDate4Past(
					view.getCreateTime(), Clock.DEFAULT.getCurrentDate()));
			commentViewList.add(view);
		}
		
		return commentViewList;
	}

	public void delComment(Long commentId) {
		commentDao.delComment(commentId);
	}
	
}
