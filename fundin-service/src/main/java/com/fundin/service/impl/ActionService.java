package com.fundin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fundin.dao.mybatis.FundinActionDao;
import com.fundin.dao.mybatis.FundinAttentionDao;
import com.fundin.dao.mybatis.FundinUserActionDao;
import com.fundin.domain.entity.FundinAction;
import com.fundin.domain.entity.FundinAttention;
import com.fundin.domain.entity.FundinUser;
import com.fundin.domain.entity.FundinUserAction;
import com.fundin.domain.query.Pagination;
import com.fundin.service.common.Constants;
import com.fundin.service.common.ImagePathConventer;
import com.fundin.service.component.MessageHandler;
import com.fundin.service.component.SchoolHelper;
import com.fundin.utils.common.Clock;
import com.fundin.utils.common.ConstellationUtils;

@Component
public class ActionService {

	@Resource
	private UserService userService;
	@Resource
	private FundinActionDao actionDao;
	@Resource
	private FundinUserActionDao userActionDao;
	@Resource
	private FundinAttentionDao attentionDao;
	@Resource
	private MessageHandler messageHandler;
	@Resource
	private SchoolHelper schoolHelper;
	
	@Transactional(rollbackFor = Exception.class)
	public boolean saveAction(Long userId, Integer type, String content) {
		FundinAction action = new FundinAction();
		action.setUserId(userId);
		action.setType(type);
		action.setContent(content);
		action.setTime(Clock.DEFAULT.getCurrentDate());
		
		if (Constants.INSERT_SUCCESS == actionDao.saveAction(action)) {
			return true;
		}
		return false;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public boolean saveUserAction(Long userId, Long projId, Integer type) {
		FundinUserAction action = new FundinUserAction();
		action.setUserId(userId);
		action.setProjId(projId);
		action.setType(type);
		action.setTime(Clock.DEFAULT.getCurrentDate());
		
		if (Constants.INSERT_SUCCESS == userActionDao.saveUserAction(action)) {
			return true;
		}
		return false;
	}

	public List<FundinUserAction> getUserActionList(Long projId, Integer type) {
		return userActionDao.getUserActionList(projId, type);
	}
	
	public Pagination<FundinAttention> getAttentionByUserId(Long userId, int pageNum, int pageSize) {
		Pagination<FundinAttention> pageQuery = Pagination.getInstance(
				attentionDao.getByUserIdCount(userId), pageNum, pageSize);
		pageQuery.addQueryParams("userId", userId);
		
		List<FundinAttention> attentions = attentionDao.getByUserId(pageQuery);
		for (FundinAttention attention : attentions) {
			FundinUser user = userService.queryInfo(attention.getAttentionUserId());
			attention.setUserName(user.getName());
			attention.setUserHeadImg(user.getHeadImg());
		}
		pageQuery.setElements(attentions);
		return pageQuery;
	}

	public Pagination<FundinAttention> getByAttentionUserId(Long attentionUserId, 
			int pageNum, int pageSize) {
		Pagination<FundinAttention> pageQuery = Pagination.getInstance(
				attentionDao.getByAttentionUserIdCount(attentionUserId), pageNum, pageSize);
		pageQuery.addQueryParams("attentionUserId", attentionUserId);
		
		List<FundinAttention> attentions = attentionDao.getByAttentionUserId(pageQuery);
		for (FundinAttention attention : attentions) {
			FundinUser user = userService.queryInfo(attention.getUserId());
			attention.setUserName(user.getName());
			attention.setUserHeadImg(user.getHeadImg());
		}
		pageQuery.setElements(attentions);
		return pageQuery;
	}
	
	public List<FundinUser> getAttentionUserListByUserId(Long userId, int pageNum, int pageSize){
		Pagination<FundinAttention> pageQuery = Pagination.getInstance(
				attentionDao.getByUserIdCount(userId), pageNum, pageSize);
		pageQuery.addQueryParams("userId", userId);
		
		List<FundinAttention> attentions = attentionDao.getByUserId(pageQuery);
		List<FundinUser> userList = new ArrayList<FundinUser>();
		for (FundinAttention attention : attentions) {
			FundinUser user = userService.queryInfo(attention.getAttentionUserId());
			user.setHeadImg(ImagePathConventer.convert2Small(user.getHeadImg()));
			user.setConstellation(getConstellation(user));
			user.setSchoolModel(schoolHelper.getById(user.getSchoolId()));
			userList.add(user);
		}
		return userList;
	}
	
	public List<FundinUser> getBeAttentionUserListByUserId(Long attentionUserId, int pageNum, int pageSize){
		Pagination<FundinAttention> pageQuery = Pagination.getInstance(
				attentionDao.getByAttentionUserIdCount(attentionUserId), pageNum, pageSize);
		pageQuery.addQueryParams("attentionUserId", attentionUserId);
		
		List<FundinAttention> attentions = attentionDao.getByAttentionUserId(pageQuery);
		List<FundinUser> userList = new ArrayList<FundinUser>();
		for (FundinAttention attention : attentions) {
			FundinUser user = userService.queryInfo(attention.getUserId());
			user.setHeadImg(ImagePathConventer.convert2Small(user.getHeadImg()));
			user.setConstellation(getConstellation(user));
			user.setSchoolModel(schoolHelper.getById(user.getSchoolId()));
			userList.add(user);
		}
		return userList;
	}

	String getConstellation(FundinUser user) {
		if (StringUtils.isBlank(user.getBirthday())) {
			return null;
		}
		try {
			String[] birthArr = StringUtils.split(user.getBirthday(), "-");
			if (birthArr.length == 3) {
				return ConstellationUtils.date2Constellation(
						Integer.valueOf(birthArr[1]), Integer.valueOf(birthArr[2]));
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public boolean addNewAttention(Long userId, Long attentionUserId) {
		FundinAttention attention = new FundinAttention();
		attention.setUserId(userId);
		attention.setAttentionUserId(attentionUserId);
		attention.setTime(Clock.DEFAULT.getCurrentDate());
		if (Constants.INSERT_SUCCESS != attentionDao.addNewAttention(attention)) {
			return false;
		}
		
		messageHandler.createAttentionMessage(userId);
		return true;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void cancleAttention(Long userId, Long attentionUserId) {
		FundinAttention attention = new FundinAttention();
		attention.setUserId(userId);
		attention.setAttentionUserId(attentionUserId);
		attentionDao.cancleAttention(attention);
	}

	public boolean hasAttention(Long userId, Long attentionUserId) {
		FundinAttention attention = attentionDao.hasAttention(userId, attentionUserId);
		if (null == attention) {
			return false;
		}
		return true;
	}
	
}
