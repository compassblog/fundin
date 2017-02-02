package com.fundin.service.impl;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fundin.dao.mybatis.FundinPayRecordDao;
import com.fundin.dao.mybatis.FundinUserActionDao;
import com.fundin.dao.mybatis.FundinUserDao;
import com.fundin.domain.export.ExportEntity.ExportAction;
import com.fundin.domain.export.ExportEntity.ExportPay;
import com.fundin.domain.export.ExportEntity.ExportUser;
import com.fundin.domain.export.ExportEntity.ExportUserCount;
import com.fundin.domain.type.UserActionEnum;
import com.fundin.service.common.excel.ExcelUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class OperService {

	@Resource
	private FundinUserActionDao userActionDao;
	@Resource
	private FundinPayRecordDao payRecordDao;
	@Resource
	private FundinUserDao userDao;
	@Resource
	private UserService userService;
	
	public void exportAction(OutputStream os, String templateName) {
		Map<String, Object> dataMap = Maps.newHashMap();
		
		List<ExportAction> followList = userActionDao.exportUserActionByType(
				UserActionEnum.USER_ACTION_FOLLOW.getCode());
		dataMap.put("followList", mergeUserName(followList));
		
		List<ExportAction> goodList = userActionDao.exportUserActionByType(
				UserActionEnum.USER_ACTION_GOOD.getCode());
		dataMap.put("goodList", mergeUserName(goodList));
		
		ExcelUtil.exportExcel(os, templateName, dataMap);
	}
	
	private List<ExportAction> mergeUserName(List<ExportAction> actionList) {
		List<ExportAction> newList = Lists.newArrayList();
		HashMultimap<String, String> mergeMap = HashMultimap.create();
		for (ExportAction action : actionList) {
			mergeMap.put(action.getProjName(), action.getUserName());
		}
		for (Map.Entry<String, Collection<String>> entry : mergeMap.asMap().entrySet()) {
			ExportAction mergeAction = new ExportAction();
			mergeAction.setProjName(entry.getKey());
			mergeAction.setUserName(StringUtils.join(entry.getValue().iterator(), ","));
			newList.add(mergeAction);
		}
		return newList;
	}
	
	public void exportPay(OutputStream os, String templateName) {
		Map<String, Object> dataMap = Maps.newHashMap();
		List<ExportPay> payList = payRecordDao.exportPay();
		dataMap.put("payList", mergeUserName2(payList));
		ExcelUtil.exportExcel(os, templateName, dataMap);
	}

	private List<ExportPay> mergeUserName2(List<ExportPay> payList) {
		List<ExportPay> newList = Lists.newArrayList();
		HashMultimap<String, String> mergeMap = HashMultimap.create();
		for (ExportPay pay : payList) {
			mergeMap.put(pay.getProjName() + "\t" + pay.getAmount(), pay.getUserName());
		}
		for (Map.Entry<String, Collection<String>> entry : mergeMap.asMap().entrySet()) {
			ExportPay mergePay = new ExportPay();
			String arr[] = StringUtils.split(entry.getKey(), "\t");
			mergePay.setProjName(arr[0]);
			mergePay.setAmount(Integer.valueOf(arr[1]));
			mergePay.setUserName(StringUtils.join(entry.getValue().iterator(), ","));
			newList.add(mergePay);
		}
		return newList;
	}
	
	public void exportUser(OutputStream os, String templateName) {
		Map<String, Object> dataMap = Maps.newHashMap();
		List<ExportUser> userList = userDao.exportUser();
		dataMap.put("userList", userList);
		ExcelUtil.exportExcel(os, templateName, dataMap);
	}
	
	public void exportUserCount(OutputStream os, String templateName) throws Exception {
		Map<String, Object> dataMap = Maps.newHashMap();
		List<Date> dateList = userDao.exportUserRegTime();
		dataMap.put("countList", calcUserCount(dateList));
		ExcelUtil.exportExcel(os, templateName, dataMap);
	}

	private List<ExportUserCount> calcUserCount(List<Date> dateList) throws Exception {
		List<ExportUserCount> countList = Lists.newArrayList();
		Map<Date, Integer> countMap = Maps.newTreeMap();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		for (Date date : dateList) {
			Date newDate = df.parse(df.format(date));
			Integer count = countMap.get(newDate);
			if (null == count) {
				countMap.put(newDate, Integer.valueOf(1));
			} else {
				countMap.put(newDate, count + 1);
			}
		}
		
		int totalCount = 0;
		for (Map.Entry<Date, Integer> entry : countMap.entrySet()) {
			ExportUserCount userCount = new ExportUserCount();
			userCount.setRegTime(entry.getKey());
			userCount.setIncr(entry.getValue());
			totalCount += entry.getValue();
			userCount.setTotal(totalCount);
			countList.add(userCount);
		}
		return countList;
	}
	
//	public void sendEmailToAll() {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				Random r = new Random();
//				List<String> emails = userDao.getAllEmail();
//				for (String email : emails) {
//					try {
//						userService.sendWelcomeEmail(email);
//						Thread.sleep(1000 * 60 * (r.nextInt(10) + 1));
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}).start();
//	}

}
