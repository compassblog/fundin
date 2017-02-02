package com.fundin.service.component;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.fundin.dao.mybatis.FundinLetterDao;
import com.fundin.dao.mybatis.FundinPayRecordDao;
import com.fundin.domain.entity.FundinLetter;
import com.fundin.domain.entity.FundinProj;
import com.fundin.service.impl.ProjService;

@Component("letterHandler")
public class LetterHandler {
	
	@Resource
	private FundinLetterDao letterDao;
	@Resource
	private ProjService projService;
	@Resource
	private FundinPayRecordDao payRecordDao;
	@Resource
	private MessageHandler messageHandler;
	
	public void createAuditLetter(Long projId, Integer ret) {
		FundinProj proj = projService.getProj(projId);
		FundinLetter letter = new FundinLetter();
		
		letter.setProjId(projId);
		letter.setUserId(proj.getUserId());
		if (ret == 1) {
			letter.setTitle("您发起的项目" + proj.getTitle() + "审核通过");
			letter.setContent("您申请发起的项目<a target='_blank' href='/proj/" 
					+ projId + "'>" + proj.getTitle() + "</a>已经审核通过，众筹正式开始！");
		} else {
			letter.setTitle("您发起的项目" + proj.getTitle() + "审核未通过");
			letter.setContent("您申请发起的项目" + proj.getTitle() + 
					"未通过审核，请点击发起项目进行修改后重新申请发起");
		}
		letterDao.insert(letter);
		
		List<Long> userIdList = new ArrayList<Long>();
		userIdList.add(proj.getUserId());
		messageHandler.createLetterMessage(userIdList);
	}
	
	public void createFundLetter(Long projId, Integer ret) {
		List<FundinLetter> letterList = new ArrayList<FundinLetter>();
		List<Long> userIdList = new ArrayList<Long>();
		FundinProj proj = projService.getProj(projId);
		
		FundinLetter origletter = new FundinLetter();
		origletter.setProjId(projId);
		origletter.setUserId(proj.getUserId());
		if (ret == 1) {
			origletter.setTitle("您发起的项目" + proj.getTitle() + "众筹成功");
			origletter.setContent("恭喜，您申请发起的项目<a target='_blank' href='/proj/" 
					+ projId + "'>" + proj.getTitle() + "</a>已经众筹成功，"
					+ "请联系网站工作人员申请提现，并注意在" + proj.getRepayDays() 
					+ "天后发放相应的回报给您的支持者们。");
		} else {
			origletter.setTitle("您发起的项目" + proj.getTitle() + "众筹失败");
			origletter.setContent("很遗憾，您申请发起的项目<a target='_blank' href='/proj/" 
					+ projId + "'>" + proj.getTitle() + "</a>众筹失败，"
					+ "已经众筹到的资金将会返回给您的支持者。");
		}
		letterList.add(origletter);
		userIdList.add(proj.getUserId());
		
		List<Long> payUserIdList = payRecordDao.queryUserIdList(projId);
		for (Long userId : payUserIdList) {
			FundinLetter letter = new FundinLetter();
			letter.setProjId(projId);
			letter.setUserId(userId);
			if (ret == 1) {
				letter.setTitle("您支持的项目" + proj.getTitle() + "众筹成功");
				letter.setContent("恭喜，您支持的项目<a target='_blank' href='/proj/" 
						+ projId + "'>" + proj.getTitle() + "</a>已经众筹成功，"
						+ "请持续关注项目的进展，发起人将于" + proj.getRepayDays() 
						+ "天后发放相应的回报给您。");
			} else {
				letter.setTitle("您支持的项目" + proj.getTitle() + "众筹失败");
				letter.setContent("很遗憾，您支持的项目<a target='_blank' href='/proj/" 
						+ projId + "'>" + proj.getTitle() + "</a>众筹失败，"
						+ "你支持该项目的资金将会通过微信原路退回给您，请注意查收。");
			}
			letterList.add(letter);
			userIdList.add(userId);
		}
		
		letterDao.batchInsert(letterList);
		messageHandler.createLetterMessage(userIdList);
	}
	
}
