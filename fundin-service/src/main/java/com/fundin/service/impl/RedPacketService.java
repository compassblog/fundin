package com.fundin.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fundin.dao.mybatis.FundinPayRecordDao;
import com.fundin.dao.mybatis.FundinRedPacketDao;
import com.fundin.domain.dto.RedPacketInfo;
import com.fundin.domain.entity.FundinProj;
import com.fundin.domain.entity.FundinRedPacket;
import com.fundin.domain.type.RedPacketStatusEnum;
import com.fundin.domain.type.RedPacketTypeEnum;
import com.fundin.service.component.MessageHandler;
import com.fundin.utils.common.DateUtil;

@Component
public class RedPacketService {

	@Resource
	private FundinRedPacketDao fundinRedPacketDao;
	@Resource
	private MessageHandler messageHandler;
	@Resource
	private ProjService projService;
	@Resource
	private FundinPayRecordDao payRecordDao;

	/**
	 * 紅包創建
	 * 
	 * @param userId
	 * @param type
	 * @param amount
	 * @param count
	 */
	@Transactional(rollbackFor = Exception.class)
	public void createRedPacket(Long userId, Integer type, Long amount, Integer count) {
		Date now = new Date();
		FundinRedPacket redPacket = new FundinRedPacket();
		redPacket.setUserId(userId);
		redPacket.setType(type);
		redPacket.setAmount(amount);
		redPacket.setStatus(RedPacketStatusEnum.REDPACKET_UNUSED.getCode());
		redPacket.setCreateTime(now);
		redPacket.setBeginTime(DateUtil.stringToDate("2016-09-01", DateUtil.DATEFORMAT_DAY));
		redPacket.setEndTime(DateUtil.stringToDate("2017-01-01", DateUtil.DATEFORMAT_DAY));
		for (int i = 0; i < count; i++) {
			fundinRedPacketDao.createRedPacket(redPacket);
		}
		messageHandler.createRedPacketMessage(userId);
	}

	public Long checkAmount(Long userId, Integer type) {
		return fundinRedPacketDao.checkAmount(userId, type);
	}

	public List<RedPacketInfo> getMyRedPacketInfo(Long userId) {
		List<RedPacketInfo> list = fundinRedPacketDao.getMyRedPacketInfo(userId);
		for (RedPacketInfo redPacketInfo : list) {
			if (redPacketInfo.getType() == RedPacketTypeEnum.REDPACKET_TYPE_F.getCode())
				redPacketInfo.setTypeName(RedPacketTypeEnum.REDPACKET_TYPE_F.getDesc());
			else
				redPacketInfo.setTypeName(RedPacketTypeEnum.REDPACKET_TYPE_Z.getDesc());
			redPacketInfo.setBeginTimeStr(DateUtil.dateFormat(redPacketInfo.getBeginTime(), DateUtil.DATEFORMAT_DAY));
			redPacketInfo.setEndTimeStr(DateUtil.dateFormat(redPacketInfo.getEndTime(), DateUtil.DATEFORMAT_DAY));
		}
		return list;
	}

	public List<RedPacketInfo> getMyRedPacket(Long userId, Long projId) {
		List<RedPacketInfo> list = getMyRedPacketInfo(userId);
		List<Integer> types = fundinRedPacketDao.getUsedRedPacket(userId, projId);

		FundinProj proj = projService.getProj(projId);
		if (proj.getTotalAmount() < 1000) {
			for (RedPacketInfo redPacketInfo : list) {
				redPacketInfo.setCanUse(false);
			}
		} else {
			boolean canUse = true;
			for (RedPacketInfo redPacketInfo : list) {
				for (Integer type : types) {
					if (type == redPacketInfo.getType()) {
						canUse = false;
						break;
					}
				}
				redPacketInfo.setCanUse(canUse);
				if (redPacketInfo.getCanUse()) {
					// 发起红包 必须是自己发起的项目
					if (redPacketInfo.getType() == RedPacketTypeEnum.REDPACKET_TYPE_F.getCode()) {
						if (projService.findMyPro(userId, projId) == 0)
							redPacketInfo.setCanUse(false);
					} else {
						// 支持红包： 项目接受红包数额不超过总众筹金额80%
						int redPacketAmount = payRecordDao.getRedPacketAmount(projId);
						int totalAmount = proj.getTotalAmount();
						if ((float) redPacketAmount / (float) totalAmount >= 0.8) {
							redPacketInfo.setCanUse(false);
						}
					}
				}
			}
		}
		return list;
	}

	public void updateInfoById(Long id, Long projId, Integer status, Date now) {
		fundinRedPacketDao.updateInfoById(id, projId, status, now);
	}
	
	public void updateInfoByAmount(Long userId,Long projId,Integer amount, Integer status, Date now) {
		fundinRedPacketDao.updateInfoByAmount(userId, projId, amount, status, now);
	}
}