package com.fundin.service.job;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fundin.dao.mybatis.FundinProjDao;
import com.fundin.dao.mybatis.FundinUserDao;
import com.fundin.domain.entity.FundinProj;
import com.fundin.domain.type.ProjStatusEnum;
import com.fundin.service.component.LetterHandler;
import com.fundin.service.impl.WXPayService;

public class UpdateProjStatusJob {

	private final static Logger LOG = LoggerFactory.getLogger(
			UpdateProjStatusJob.class.getName());
	
	@Resource
	private WXPayService wxPayService;
	@Resource
	private FundinProjDao projDao;
	@Resource
	private FundinUserDao userDao;
	@Resource
	private LetterHandler letterHandler;
	
	public void execute() {
		LOG.warn("start update proj status job...");
		
		List<FundinProj> endProjList = projDao.getEndProj();
		if (CollectionUtils.isEmpty(endProjList)) {
			return ;
		}
		
		for (FundinProj endProj : endProjList) {
			Integer raisedAmount = endProj.getRaisedAmount();
			if (raisedAmount >= endProj.getTotalAmount()) {
				projDao.updateStatus(endProj.getId(), ProjStatusEnum.FUND_SUCC.getCode());
				letterHandler.createFundLetter(endProj.getId(), 1);
				
				int transactionAmount = (int) (raisedAmount * 0.01);
				int depositAmount = (int) (raisedAmount * 0.1);
				int withdrawalAmount = raisedAmount - transactionAmount - depositAmount;
				userDao.updateAmountInfo(endProj.getUserId(), raisedAmount, 
						withdrawalAmount, depositAmount, transactionAmount);
			} else {
				wxPayService.wxRefund(endProj.getId());
				projDao.updateStatus(endProj.getId(), ProjStatusEnum.FUND_FAIL.getCode());
				letterHandler.createFundLetter(endProj.getId(), 0);
			}
		}
		
		LOG.warn("end update proj status job...");
	}
	
}