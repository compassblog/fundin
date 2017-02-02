package com.fundin.service.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fundin.dao.mybatis.FundinMessageDao;
import com.fundin.dao.mybatis.FundinPayRecordDao;
import com.fundin.domain.entity.FundinMessage;
import com.fundin.domain.entity.FundinProj;
import com.fundin.service.impl.ProjService;
import com.google.common.collect.Maps;

@Component
public class MessageHandler {

	private static String TYPE_ATTENTION = "attention";
	private static String TYPE_COMMENT = "comment";
	private static String TYPE_CONTENT = "content";
	private static String TYPE_PROGRESS = "progress";
	private static String TYPE_LETTER = "letter";
	private static String TYPE_REDPACKET = "redpacket";
	
	@Resource
	private FundinMessageDao messageDao;
	@Resource
	private FundinPayRecordDao payRecordDao;
	@Resource
	private ProjService projService;
	
	public void createAttentionMessage(Long userId) {
		FundinMessage message = new FundinMessage();
		message.setType(TYPE_ATTENTION);
		message.setUserId(userId);
		messageDao.insert(message);
	}
	
	public void createCommentMessage(Long replyUserId, Long projId) {
		FundinMessage message = new FundinMessage();
		message.setType(TYPE_COMMENT);
		message.setProjId(projId);
		
		FundinProj proj = projService.getProj(projId);
		message.setProjTitle(proj.getTitle());
		if (null == replyUserId) {
			message.setUserId(proj.getUserId());
		} else {
			message.setUserId(replyUserId);
		}
		messageDao.insert(message);
	}
	
	public void createLetterMessage(List<Long> userIdList) {
		List<FundinMessage> messageList = new ArrayList<FundinMessage>();
		for (Long userId : userIdList) {
			FundinMessage message = new FundinMessage();
			message.setType(TYPE_LETTER);
			message.setUserId(userId);
			messageList.add(message);
		}
		messageDao.batchInsert(messageList);
	}
	
	public void createContentMessage(Long projId) {
		FundinProj proj = projService.getProj(projId);
		List<FundinMessage> messageList = new ArrayList<FundinMessage>();
		
		List<Long> payUserIdList = payRecordDao.queryUserIdList(projId);
		for (Long userId : payUserIdList) {
			FundinMessage message = new FundinMessage();
			message.setType(TYPE_CONTENT);
			message.setUserId(userId);
			message.setProjId(projId);
			message.setProjTitle(proj.getTitle());
			messageList.add(message);
		}

		if (messageList.size() > 0) {
			messageDao.batchInsert(messageList);
		}
	}
	
	public void createProgressMessage(Long projId) {
		FundinProj proj = projService.getProj(projId);
		List<FundinMessage> messageList = new ArrayList<FundinMessage>();
		
		List<Long> payUserIdList = payRecordDao.queryUserIdList(projId);
		for (Long userId : payUserIdList) {
			FundinMessage message = new FundinMessage();
			message.setType(TYPE_PROGRESS);
			message.setUserId(userId);
			message.setProjId(projId);
			message.setProjTitle(proj.getTitle());
			messageList.add(message);
		}
		
		if (messageList.size() > 0) {
			messageDao.batchInsert(messageList);
		}
	}
	
	public void createRedPacketMessage(Long userId){
		FundinMessage message = new FundinMessage();
		message.setType(TYPE_REDPACKET);
		message.setUserId(userId);
		messageDao.insert(message);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void updateMessageStatus(Long userId, Long projId, String type) {
		messageDao.updateStatus(userId, projId, type);
	}
	
	public int getMessageCount(Long userId) {
		return messageDao.getMessageCount(userId);
	}
	
	public List<MessageInfo> getMessageList(Long userId) {
		List<FundinMessage> messageList = messageDao.getMessageList(userId);
		List<MessageInfo> messageInfos = new ArrayList<MessageInfo>();
		
		Map<String, MessageInfo> singleMap = Maps.newHashMap();
		Map<Long, MessageInfo> commentMap = Maps.newHashMap();
		Map<Long, MessageInfo> contentMap = Maps.newHashMap();
		Map<Long, MessageInfo> progressMap = Maps.newHashMap();
		
		for (FundinMessage message : messageList) {
			if (TYPE_ATTENTION.equals(message.getType())) {
				MessageInfo info = singleMap.get(TYPE_ATTENTION);
				if (null == info) {
					info = new MessageInfo();
					info.setType(TYPE_ATTENTION);
					info.setCount(0);
					singleMap.put(TYPE_ATTENTION, info);
				}
				info.setCount(info.getCount() + 1);
			} else if (TYPE_LETTER.equals(message.getType())) {
				MessageInfo info = singleMap.get(TYPE_LETTER);
				if (null == info) {
					info = new MessageInfo();
					info.setType(TYPE_LETTER);
					info.setCount(0);
					singleMap.put(TYPE_LETTER, info);
				}
				info.setCount(info.getCount() + 1);
			} else if (TYPE_COMMENT.equals(message.getType())) {
				MessageInfo info = commentMap.get(message.getProjId());
				if (null == info) {
					info = new MessageInfo();
					info.setType(TYPE_COMMENT);
					info.setProjId(message.getProjId());
					info.setProjTitle(message.getProjTitle());
					info.setCount(0);
					commentMap.put(message.getProjId(), info);
				}
				info.setCount(info.getCount() + 1);
			} else if (TYPE_CONTENT.equals(message.getType())) {
				MessageInfo info = contentMap.get(message.getProjId());
				if (null == info) {
					info = new MessageInfo();
					info.setType(TYPE_CONTENT);
					info.setProjId(message.getProjId());
					info.setProjTitle(message.getProjTitle());
					info.setCount(0);
					contentMap.put(message.getProjId(), info);
				}
				info.setCount(info.getCount() + 1);
			} else if (TYPE_PROGRESS.equals(message.getType())) {
				MessageInfo info = progressMap.get(message.getProjId());
				if (null == info) {
					info = new MessageInfo();
					info.setType(TYPE_PROGRESS);
					info.setProjId(message.getProjId());
					info.setProjTitle(message.getProjTitle());
					info.setCount(0);
					progressMap.put(message.getProjId(), info);
				}
				info.setCount(info.getCount() + 1);
			} else if (TYPE_REDPACKET.equals(message.getType())) {
				MessageInfo info = progressMap.get(message.getProjId());
				if (null == info) {
					info = new MessageInfo();
					info.setType(TYPE_REDPACKET);
					info.setCount(0);
					progressMap.put(message.getProjId(), info);
				}
				info.setCount(info.getCount() + 1);
			}
		}
		
		int infoSize = 0;
		if (singleMap.get(TYPE_ATTENTION) != null) {
			messageInfos.add(singleMap.get(TYPE_ATTENTION));
			infoSize ++;
		}
		if (singleMap.get(TYPE_LETTER) != null) {
			messageInfos.add(singleMap.get(TYPE_LETTER));
			infoSize ++;
		}
		List<MessageInfo> tempAll = new ArrayList<MessageInfo>();
		tempAll.addAll(commentMap.values());
		tempAll.addAll(contentMap.values());
		tempAll.addAll(progressMap.values());
		for (MessageInfo info : tempAll) {
			messageInfos.add(info);
			infoSize ++;
			if (infoSize == 10) {
				break ;
			}
		}
		
		return messageInfos;
	}
	
	public static class MessageInfo {
		private String type;
		private Long projId;
		private String projTitle;
		private Integer count;
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Long getProjId() {
			return projId;
		}
		public void setProjId(Long projId) {
			this.projId = projId;
		}
		public String getProjTitle() {
			return projTitle;
		}
		public void setProjTitle(String projTitle) {
			this.projTitle = projTitle;
		}
		public Integer getCount() {
			return count;
		}
		public void setCount(Integer count) {
			this.count = count;
		}
	}
	
}