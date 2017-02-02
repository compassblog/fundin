package com.fundin.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fundin.dao.mybatis.FundinActionDao;
import com.fundin.dao.mybatis.FundinPayRecordDao;
import com.fundin.dao.mybatis.FundinProjDao;
import com.fundin.dao.mybatis.FundinProjProgressDao;
import com.fundin.dao.mybatis.FundinUserActionDao;
import com.fundin.dao.mybatis.FundinUserDao;
import com.fundin.domain.dto.ProjInfo;
import com.fundin.domain.dto.ProjView;
import com.fundin.domain.entity.FundinPayRecord;
import com.fundin.domain.entity.FundinProj;
import com.fundin.domain.entity.FundinProjProgress;
import com.fundin.domain.entity.FundinSchool;
import com.fundin.domain.entity.FundinUser;
import com.fundin.domain.entity.FundinUserAction;
import com.fundin.domain.query.Pagination;
import com.fundin.domain.query.QueryInfo;
import com.fundin.domain.type.ProjStatusEnum;
import com.fundin.domain.type.RedPacketTypeEnum;
import com.fundin.domain.type.UserActionEnum;
import com.fundin.service.common.Constants;
import com.fundin.service.common.DateConverter;
import com.fundin.service.common.ImagePathConventer;
import com.fundin.service.common.RedisConstants;
import com.fundin.service.common.RedisUtil;
import com.fundin.service.common.RedisUtil.DbQueryExecutor;
import com.fundin.service.component.LetterHandler;
import com.fundin.service.component.MessageHandler;
import com.fundin.utils.common.Clock;
import com.fundin.utils.common.DateUtil;
import com.fundin.utils.mapper.BeanMapper;

@Component
public class ProjService {

	@Resource
	private FundinProjDao projDao;
	@Resource
	private FundinProjProgressDao progressDao;
	@Resource
	private FundinPayRecordDao payRecordDao;
	@Resource
	private FundinActionDao actionDao;
	@Resource
	private FundinUserActionDao userActionDao;
	@Resource
	private FundinUserDao userDao;
	@Resource
	private RedisUtil redisUtil;
	@Resource
	private ActionService actionService;
	@Resource
	private UserService userService;
	@Resource
	private LetterHandler letterHandler;
	@Resource
	private MessageHandler messageHandler;
	@Resource
	private RedPacketService redPacketService;
	
	@Transactional(rollbackFor = Exception.class)
	public Long startNew(FundinProj proj) {
		proj.setStatus(ProjStatusEnum.AUDITING.getCode());
		FundinProj newProj = addOrUpdateProj(proj);
		if (null == newProj) {
			return null;
		}
		return newProj.getId();
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Long saveDraft(FundinProj proj) {
		proj.setStatus(ProjStatusEnum.STARTING.getCode());
		
		FundinProj newProj = addOrUpdateProj(proj);
		if (null == newProj) {
			return null;
		}
		return newProj.getId();
	}
	
	private FundinProj addOrUpdateProj(FundinProj proj) {
		if (null != proj.getDays()) {
			proj.setStartDate(Clock.DEFAULT.getCurrentDate());
			proj.setEndDate(DateUtil.getSameTimeOfNextDays(proj.getStartDate(), proj.getDays()));
		}

		if (null == proj.getId()) {
			if (Constants.INSERT_SUCCESS != projDao.startNew(proj)) {
				return null;
			}
		} else {
			proj.setUpdateDate(Clock.DEFAULT.getCurrentDate());
			if (Constants.UPDATE_SUCCESS != projDao.updateInfo(proj)) {
				return null;
			}
		}
		return proj;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void updateProjStatus(Long projId, Integer status) {
		projDao.updateStatus(projId, status);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void auditRet(Long projId, Integer ret) {
		if (ret == 1) {
			updateProjStatus(projId, ProjStatusEnum.FUNDING.getCode());
			
			FundinProjProgress progress = new FundinProjProgress();
			progress.setProjId(projId);
			progress.setUserId(0L);
			progress.setProgressDesc("项目通过审核，开始众筹");
			addNewProgress(progress);
			
			FundinProj proj = getProj(projId);
			Long amountCount = redPacketService.checkAmount(proj.getUserId(),
					RedPacketTypeEnum.REDPACKET_TYPE_F.getCode());
			if (projDao.getMyPassProCount(proj.getUserId()) == 1 && proj.getTotalAmount() >= 1000 && amountCount == 0)
				redPacketService.createRedPacket(proj.getUserId(), RedPacketTypeEnum.REDPACKET_TYPE_F.getCode(),
						Constants.REDPACKET_AMOUNT_25, 4);
		} else {
			updateProjStatus(projId, ProjStatusEnum.STARTING.getCode());
		}
		letterHandler.createAuditLetter(projId, ret);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public boolean saveEdit(FundinProj proj) {
		proj.setUpdateDate(Clock.DEFAULT.getCurrentDate());
		if (Constants.UPDATE_SUCCESS != projDao.saveEdit(proj)) {
			return false;
		}
		messageHandler.createContentMessage(proj.getId());
		return true;
	}
	
	public FundinProj getProj(final Long projId) {
		return redisUtil.getByCache(RedisConstants.PROJ_ID_KEY + projId, FundinProj.class, 
			new DbQueryExecutor<FundinProj>(){
				@Override
				public FundinProj getFromDb() {
					return projDao.getProj(projId);
				}
					
		}, RedisConstants.PROJ_ID_KEY_EXPIRY);
	}
	
	public ProjInfo getProjInfo(FundinProj proj, Long loginUserId) {
		ProjInfo info = new ProjInfo();
		info.setRestTime(DateConverter.convertDate4Future(
				proj.getEndDate(), Clock.DEFAULT.getCurrentDate()));
		
		FundinSchool school = userService.getSchoolByUserId(proj.getUserId());
		if (null != school) {
			info.setProvince(school.getProvince());
		}
		
		if (null != loginUserId) {
			List<FundinUserAction> followList = actionService.getUserActionList(
					proj.getId(), UserActionEnum.USER_ACTION_FOLLOW.getCode());
			for (FundinUserAction follow : followList) {
				if (loginUserId.equals(follow.getUserId())) {
					info.setHasFollow(true);
					break ;
				}
			}
			info.setFollowNum(followList.size());
			
			List<FundinUserAction> goodList = actionService.getUserActionList(
					proj.getId(), UserActionEnum.USER_ACTION_GOOD.getCode());
			for (FundinUserAction good : goodList) {
				if (loginUserId.equals(good.getUserId())) {
					info.setHasGood(true);
					break ;
				}
			}
			info.setGoodNum(goodList.size());
			
			if (hasSupport(loginUserId, proj.getId())) {
				info.setHasSupport(true);
			}
		}
		return info;
	}
	
	public ProjInfo getProjInfoForM(FundinProj proj, Long loginUserId) {
		ProjInfo info = new ProjInfo();
		info.setRestTime(DateConverter.convertDate4Future(
				proj.getEndDate(), Clock.DEFAULT.getCurrentDate()));
		
		List<FundinUserAction> followList = actionService.getUserActionList(
				proj.getId(), UserActionEnum.USER_ACTION_FOLLOW.getCode());
		info.setFollowNum(followList.size());
		if (null != loginUserId) {
			for (FundinUserAction follow : followList) {
				if (loginUserId.equals(follow.getUserId())) {
					info.setHasFollow(true);
					break ;
				}
			}
			if (hasSupport(loginUserId, proj.getId())) {
				info.setHasSupport(true);
			}
		}
		return info;
	}

	public boolean hasSupport(Long userId, Long projId) {
		FundinPayRecord payRecord = payRecordDao.queryOne(userId, projId);
		if (null != payRecord) {
			return true;
		}
		return false;
	}
	
	public FundinProj getStartingProj(Long userId) {
		FundinProj fundinProj = projDao.getStartingProj(userId);
		return fundinProj;
	}
	
	public List<FundinProj> getProjList(Integer subject, String require,
			String searchWord, String location, int pageNum, int pageSize) {
		if (StringUtils.isNotBlank(searchWord)) {
			searchWord = "%" + searchWord + "%";
		}
		
		QueryInfo query = null;
		if (pageNum > 0 && pageSize > 0) {
			query = QueryInfo.getInstance(pageNum, pageSize);
		}
		
		return projDao.getProjList(subject, require, searchWord, location, query);
	}
	
	public List<ProjView> getActivityProjBySubject(int subject, int pageNum, int pageSize) {
		Pagination<FundinProj> pageQuery = Pagination.getInstance(
				projDao.getActivityProjCount(subject), pageNum, pageSize);
		pageQuery.addQueryParams("subject", subject);
		
		List<FundinProj> projs = projDao.getActivityProj(pageQuery);
		pageQuery.setElements(projs);
		
		List<ProjView> projViewList = new ArrayList<ProjView>();
		
		for (FundinProj proj : projs) {
			ProjView view = BeanMapper.map(proj, ProjView.class);
			
			FundinUser user = userService.queryInfo(view.getUserId());
			view.setUserName(user.getName());
			view.setUserHeadImg(user.getHeadImg());
			view.setTime(DateConverter.convertDate4Past(
					view.getStartDate(), Clock.DEFAULT.getCurrentDate()));
			view.setRestTime(DateConverter.convertDate4Future(
					proj.getEndDate(), Clock.DEFAULT.getCurrentDate()));
			projViewList.add(view);
		}

		return projViewList;
	}
	
	public List<ProjView> getProjViewList(Integer subject, String require,
			String searchWord, String location, int pageNum, int pageSize) {
		if ("hot".equals(require)) {
			pageNum = 0;
			pageSize = 0;
		}
		
		List<FundinProj> projList = getProjList(subject, require, 
				searchWord, location, pageNum, pageSize);
		List<ProjView> projViewList = new ArrayList<ProjView>();
		
		for (FundinProj proj : projList) {
			ProjView view = BeanMapper.map(proj, ProjView.class);
			
			FundinUser user = userService.queryInfo(view.getUserId());
			view.setUserName(user.getName());
			view.setUserHeadImg(user.getHeadImg());
			view.setTime(DateConverter.convertDate4Past(
					view.getStartDate(), Clock.DEFAULT.getCurrentDate()));
			view.setRestTime(DateConverter.convertDate4Future(
					proj.getEndDate(), Clock.DEFAULT.getCurrentDate()));
			projViewList.add(view);
		}
		
		if ("hot".equals(require)) {
			for (ProjView view : projViewList) {
				calcHotWeight(view);
			}
			Collections.sort(projViewList, hotComparator);
		}
		
		return projViewList;
	}
	
	private static final Comparator<ProjView> hotComparator = 
			new Comparator<ProjView>(){
				@Override
				public int compare(ProjView o1, ProjView o2) {
					return o2.getWeight() - o1.getWeight();
				}
			};
	
	private void calcHotWeight(ProjView item) {
		if (item.getStatus() > ProjStatusEnum.FUNDING.getCode()) {
			item.setWeight(0);
			return ;
		}
		
		double weight = 0.8 * item.getRaisedNum() 
				+ 0.5 * item.getRaisedRatio().doubleValue();
		item.setWeight((int) weight);
	}

	public Pagination<FundinProj> getMyProjList(Long userId, int pageNum, int pageSize) {
		Pagination<FundinProj> pageQuery = Pagination.getInstance(
				projDao.getMyProCount(userId), pageNum, pageSize);
		pageQuery.addQueryParams("userId", userId);
		pageQuery.setElements(projDao.getMyProList(pageQuery));
		return pageQuery;
	}

	public Pagination<FundinProj> getMyFollowList(Long userId, int pageNum, int pageSize) {
		Pagination<FundinProj> pageQuery = Pagination.getInstance(
				userActionDao.getProIdCount(
				userId, UserActionEnum.USER_ACTION_FOLLOW.getCode()), 
				pageNum, pageSize);
		pageQuery.addQueryParams("userId", userId);
		pageQuery.addQueryParams("type", UserActionEnum.USER_ACTION_FOLLOW.getCode());
		List<Long> proIdList = userActionDao.getProIdList(pageQuery);
		
		List<FundinProj> projList = new ArrayList<FundinProj>();
		if (CollectionUtils.isNotEmpty(proIdList)) {
			for (Long proId : proIdList) {
				projList.add(getProj(proId));
			}
		}
		pageQuery.setElements(projList);
		return pageQuery;
	}

	public Pagination<FundinProj> getMySupportList(Long userId, int pageNum, int pageSize) {
		Pagination<FundinProj> pageQuery = Pagination.getInstance(
				payRecordDao.queryProIdCount(userId), pageNum, pageSize);
		pageQuery.addQueryParams("userId", userId);
		List<Long> proIdList = payRecordDao.queryProIdList(pageQuery);
		
		List<FundinProj> projList = new ArrayList<FundinProj>();
		if (CollectionUtils.isNotEmpty(proIdList)) {
			for (Long proId : proIdList) {
				projList.add(getProj(proId));
			}
		}
		pageQuery.setElements(projList);
		return pageQuery;
	}
	
	public List<FundinProjProgress> getProgressList(Long projId) {
		List<FundinProjProgress> progressList = progressDao.getProgressList(projId);
		for (FundinProjProgress progress : progressList) {
			progress.setTimeStr(DateUtil.dateFormat(
					progress.getTime(), DateUtil.DATEFORMAT_SECOND));
		}
		return progressList;
	}
	
	public List<ProjView> getMyProjViewList(Long userId, int pageNum, int pageSize){
		Pagination<FundinProj> pageQuery = Pagination.getInstance(
				projDao.getMyProCount(userId), pageNum, pageSize);
		pageQuery.addQueryParams("userId", userId);
		List<FundinProj> lProj = projDao.getMyProList(pageQuery);
		List<ProjView> projViewList = new ArrayList<ProjView>();
		
		for (FundinProj proj : lProj) {
			ProjView view = BeanMapper.map(proj, ProjView.class);
			view.setCoverImg(ImagePathConventer.convert2Small(view.getCoverImg()));
			
			FundinUser user = userService.queryInfo(view.getUserId());
			view.setUserName(user.getName());
			view.setUserHeadImg(ImagePathConventer.convert2Small(user.getHeadImg()));
			view.setTime(DateConverter.convertDate4Past(
					view.getStartDate(), Clock.DEFAULT.getCurrentDate()));
			view.setRestTime(DateConverter.convertDate4Future(
					view.getEndDate(), Clock.DEFAULT.getCurrentDate()));
			projViewList.add(view);
		}
		
		return projViewList;
	}
	
	public List<ProjView> getMyFollowProjViewList(Long userId, int pageNum, int pageSize){
		Pagination<FundinProj> pageQuery = Pagination.getInstance(
				userActionDao.getProIdCount(
				userId, UserActionEnum.USER_ACTION_FOLLOW.getCode()), 
				pageNum, pageSize);
		pageQuery.addQueryParams("userId", userId);
		pageQuery.addQueryParams("type", UserActionEnum.USER_ACTION_FOLLOW.getCode());
		List<Long> proIdList = userActionDao.getProIdList(pageQuery);
		
		List<FundinProj> projList = new ArrayList<FundinProj>();
		if (CollectionUtils.isNotEmpty(proIdList)) {
			for (Long proId : proIdList) {
				projList.add(getProj(proId));
			}
		}
		List<ProjView> projViewList = new ArrayList<ProjView>();
		for (FundinProj proj : projList) {
			ProjView view = BeanMapper.map(proj, ProjView.class);
			view.setCoverImg(ImagePathConventer.convert2Small(view.getCoverImg()));
			
			FundinUser user = userService.queryInfo(view.getUserId());
			view.setUserName(user.getName());
			view.setUserHeadImg(ImagePathConventer.convert2Small(user.getHeadImg()));
			view.setTime(DateConverter.convertDate4Past(
					view.getStartDate(), Clock.DEFAULT.getCurrentDate()));
			view.setRestTime(DateConverter.convertDate4Future(
					view.getEndDate(), Clock.DEFAULT.getCurrentDate()));
			projViewList.add(view);
		}
		
		return projViewList;
	}
	
	public List<ProjView> getMySupportProjViewList(Long userId, int pageNum, int pageSize){
		Pagination<FundinProj> pageQuery = Pagination.getInstance(
				payRecordDao.queryProIdCount(userId), pageNum, pageSize);
		pageQuery.addQueryParams("userId", userId);
		List<Long> proIdList = payRecordDao.queryProIdList(pageQuery);
		
		List<FundinProj> projList = new ArrayList<FundinProj>();
		if (CollectionUtils.isNotEmpty(proIdList)) {
			for (Long proId : proIdList) {
				projList.add(getProj(proId));
			}
		}
		List<ProjView> projViewList = new ArrayList<ProjView>();
		for (FundinProj proj : projList) {
			ProjView view = BeanMapper.map(proj, ProjView.class);
			view.setCoverImg(ImagePathConventer.convert2Small(view.getCoverImg()));
			
			FundinUser user = userService.queryInfo(view.getUserId());
			view.setUserName(user.getName());
			view.setUserHeadImg(ImagePathConventer.convert2Small(user.getHeadImg()));
			view.setTime(DateConverter.convertDate4Past(
					view.getStartDate(), Clock.DEFAULT.getCurrentDate()));
			view.setRestTime(DateConverter.convertDate4Future(
					view.getEndDate(), Clock.DEFAULT.getCurrentDate()));
			projViewList.add(view);
		}
		
		return projViewList;
	}

	@Transactional(rollbackFor = Exception.class)
	public FundinProjProgress addNewProgress(FundinProjProgress progress) {
		progress.setTime(Clock.DEFAULT.getCurrentDate());
		if (Constants.INSERT_SUCCESS != progressDao.addNewProgress(progress)) {
			return null;
		}
		messageHandler.createProgressMessage(progress.getProjId());
		
		progress.setTimeStr(DateUtil.dateFormat(
				progress.getTime(), DateUtil.DATEFORMAT_SECOND));
		return progress;
	}

	public int findMyPro(Long userId, Long projId) {
		return projDao.findMyPro(userId, projId);
	}
	
	public Pagination<FundinUser> getRepayUserList(Long projId, int pageNum, int pageSize) {
		Pagination<FundinUser> pageQuery = Pagination.getInstance(payRecordDao.queryRepayUserCount(projId), pageNum,
				pageSize);
		pageQuery.addQueryParams("projId", projId);

		List<Long> userIds = payRecordDao.queryUserIdListByProjId(pageQuery);
		List<FundinUser> users = new ArrayList<FundinUser>();
		for (Long id : userIds) {
			FundinUser user = userDao.queryNameAndHeadImg(id);
			user.setSupportAmount(payRecordDao.getSupportAmount(id, projId));
			users.add(user);
		}
		pageQuery.setElements(users);
		return pageQuery;
	}
	
	/**
	 **		明信片比赛
	 					**/
	
//	public FundinProj getPostStartPro(Long userId, Integer subject) {
//		List<FundinProj> postStartPros = projDao.getPostStartPro(userId, subject);
//		if (CollectionUtils.isNotEmpty(postStartPros)) {
//			return postStartPros.get(0);
//		}
//		return null;
//	}
//	
//	@Transactional(rollbackFor = Exception.class)
//	public Long savePostDraft(FundinProj proj) {
//		proj.setStatus(ProjStatusEnum.STARTING.getCode());
//		
//		FundinProj newProj = addOrUpdatePost(proj);
//		if (null == newProj) {
//			return null;
//		}
//		return newProj.getId();
//	}
//	
//	@Transactional(rollbackFor = Exception.class)
//	public Long startNewPost(FundinProj proj) {
//		proj.setStatus(ProjStatusEnum.AUDITING.getCode());
//		FundinProj newProj = addOrUpdatePost(proj);
//		if (null == newProj) {
//			return null;
//		}
//		return newProj.getId();
//	}
//	
//	private FundinProj addOrUpdatePost(FundinProj proj) {
//		if (null == proj.getId()) {
//			proj.setStartDate(Clock.DEFAULT.getCurrentDate());
//			proj.setEndDate(DateUtil.stringToDate("20160531235959", DateUtil.DATEFORMAT_TRIM));
//			proj.setDays(DateUtil.calcDays(proj.getEndDate(), proj.getStartDate()));
//			proj.setTotalAmount(99);
//			if (Constants.INSERT_SUCCESS != projDao.startNew(proj)) {
//				return null;
//			}
//			
//			List<FundinRepay> repayList = Lists.newArrayList();
//			FundinRepay repay = new FundinRepay();
//			repay.setProjId(proj.getId());
//			repay.setAmount(3);
//			repay.setImage("");
//			repay.setContent("众筹成功后，该设计作品将印制成明信片免费发送给您");
//			repayList.add(repay);
//			repayService.insertOrUpdateRepay(repayList);
//		} else {
//			proj.setUpdateDate(Clock.DEFAULT.getCurrentDate());
//			proj.setStartDate(Clock.DEFAULT.getCurrentDate());
//			proj.setEndDate(DateUtil.stringToDate("20160531235959", DateUtil.DATEFORMAT_TRIM));
//			proj.setDays(DateUtil.calcDays(proj.getEndDate(), proj.getStartDate()));
//			proj.setTotalAmount(99);
//			if (Constants.UPDATE_SUCCESS != projDao.updateInfo(proj)) {
//				return null;
//			}
//		}
//		
//		return proj;
//	}
//	
//	@Transactional(rollbackFor = Exception.class)
//	public boolean savePostEdit(FundinProj proj) {
//		proj.setUpdateDate(Clock.DEFAULT.getCurrentDate());
//		if (Constants.UPDATE_SUCCESS != projDao.saveEdit(proj)) {
//			return false;
//		}
//		
//		messageHandler.createContentMessage(proj.getId());
//		return true;
//	}
	
}
