package com.fundin.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.fundin.dao.mybatis.FundinLetterDao;
import com.fundin.dao.mybatis.FundinUserDao;
import com.fundin.domain.dto.InviteSort;
import com.fundin.domain.entity.FundinLetter;
import com.fundin.domain.entity.FundinSchool;
import com.fundin.domain.entity.FundinUser;
import com.fundin.domain.query.Pagination;
import com.fundin.service.common.Constants;
import com.fundin.service.common.ImagePathConventer;
import com.fundin.service.common.PasswdUtil;
import com.fundin.service.common.RedisConstants;
import com.fundin.service.common.RedisUtil;
import com.fundin.service.common.RedisUtil.DbQueryExecutor;
import com.fundin.service.common.mail.MailConstants;
import com.fundin.service.common.mail.MailHelper;
import com.fundin.service.common.mail.MailSenderInfo;
import com.fundin.service.component.SchoolHelper;
import com.fundin.utils.common.Clock;
import com.fundin.utils.common.DateUtil;

@Component
public class UserService {

	@Resource
	private FundinUserDao userDao;
	@Resource
	private FundinLetterDao letterDao;
	@Resource
	private RedisUtil redisUtil;
	@Resource
	private SchoolHelper schoolHelper;
	@Resource
	private ActionService actionService;
	
	private static final String DEFAULT_HEAD_IMG = "/static/img/default-header.png";
	
	@Transactional(rollbackFor = Exception.class)
	public FundinUser register(String name, String email, String phone, 
			String passwd, Integer type, Long invUserId) {
		FundinUser user = new FundinUser();
		String salt = PasswdUtil.generateSalt();
		String securePasswd = PasswdUtil.encryptPasswd(salt, passwd);
		
		user.setName(name);
		user.setEmail(email);
		user.setPhone(phone);
		user.setType(type);
		user.setSalt(salt);
		user.setPasswd(securePasswd);
		user.setHeadImg(DEFAULT_HEAD_IMG);
		user.setRegTime(Clock.DEFAULT.getCurrentDate());
		user.setInviteUserId(invUserId);
		if (Constants.INSERT_SUCCESS == userDao.register(user)) {
			return user;
		}
		return null;
	}

	public FundinUser login(String email, String passwd) {
		FundinUser user = userDao.queryLogin(email);
		if (null != user) {
			String securePasswd = PasswdUtil.encryptPasswd(user.getSalt(), passwd);
			if (securePasswd.equals(user.getPasswd())) {
				return user;
			}
		}
		return null;
	}
	
	public FundinUser loginByPhone(String phone, String passwd) {
		FundinUser user = userDao.queryLoginByPhone(phone);
		if (null != user) {
			String securePasswd = PasswdUtil.encryptPasswd(user.getSalt(), passwd);
			if (securePasswd.equals(user.getPasswd())) {
				return user;
			}
		}
		return null;
	}
	
	public FundinUser loginByWx(String userinfo) {
		JSONObject userinfoObj = JSONObject.parseObject(userinfo);
		
		FundinUser user = userDao.queryByOpenid(userinfoObj.getString("openid"));
		if (user != null) {
			return user;
		}
		
		user = new FundinUser();
		user.setName(userinfoObj.getString("nickname"));
		user.setType(0);
		
		String sex = userinfoObj.getString("sex");
		if ("2".equals(sex)) {
			user.setSex(1);
		} else {
			user.setSex(0);
		}
		
		user.setHeadImg(userinfoObj.getString("headimgurl"));
		user.setRegTime(Clock.DEFAULT.getCurrentDate());
		if (Constants.INSERT_SUCCESS == userDao.register(user)) {
			return user;
		}
		
		return null;
	}

	public FundinUser queryInfo(final Long id) {
		return redisUtil.getByCache(RedisConstants.USER_ID_KEY + id, FundinUser.class, 
			new DbQueryExecutor<FundinUser>(){
				@Override
				public FundinUser getFromDb() {
					return userDao.queryInfo(id);
				}
					
		}, RedisConstants.USER_ID_KEY_EXPIRY);
	}
	
	public Long queryIdByEmail(String email){
		return userDao.queryIdByEmail(email);
	}
	
	public void sendForgetEmail(String email, String key){
		String link = "http://www.fundin.cn/reset?key=" + key;
		
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setValidate(true);
		mailInfo.setUserName(MailConstants.MAIL_USER_NAME);
		mailInfo.setPassword(MailConstants.MAIL_USER_PASSWORD);
		mailInfo.setFromAddress(MailConstants.MAIL_USER_NAME);
		mailInfo.setToAddress(email);
		mailInfo.setSubject("密码找回 － Fundin.cn");
		mailInfo.setContent("你好，点击下面的链接重置你的Fundin.cn账号密码：" + link);
		MailHelper.asyncSendMail(mailInfo);
	}
	
	public void sendWelcomeEmail(String email){
		String link = "http://www.sojump.com/jq/8026576.aspx";
		
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setValidate(true);
		mailInfo.setUserName(MailConstants.MAIL_USER_NAME);
		mailInfo.setPassword(MailConstants.MAIL_USER_PASSWORD);
		mailInfo.setFromAddress(MailConstants.MAIL_USER_NAME);
		mailInfo.setToAddress(email);
		mailInfo.setSubject("Fundin--欢迎新用户");
		mailInfo.setContent("您好，欢迎您注册Fundin.cn，请访问链接" + link 
				+ "，写下您宝贵的意见吧，我们将不胜感激！");
		MailHelper.asyncSendMail(mailInfo);
	}
	
	public boolean updatePersonalInfo(FundinUser user) {
		user.setUpdateTime(Clock.DEFAULT.getCurrentDate());
		if (Constants.UPDATE_SUCCESS == userDao.updatePersonalInfo(user)){
			return true;
		}
		return false;
	}
	
	public boolean updateBankAccountInfo(FundinUser user) {
		user.setUpdateTime(Clock.DEFAULT.getCurrentDate());
		if (Constants.UPDATE_SUCCESS == userDao.updateBankAccountInfo(user)){
			return true;
		}
		return false;
	}
	
	public boolean updatePasswd(FundinUser user) {
		user.setUpdateTime(Clock.DEFAULT.getCurrentDate());
		if (Constants.UPDATE_SUCCESS == userDao.updatePasswd(user)){
			return true;
		}
		return false;
	}

	public Pagination<FundinLetter> getLetterList(Long userId, int pageNum, int pageSize) {
		Pagination<FundinLetter> pageQuery = Pagination.getInstance(
				letterDao.getLetterCount(userId), pageNum, pageSize);
		pageQuery.addQueryParams("userId", userId);
		List<FundinLetter> letters =  letterDao.getLetterList(pageQuery);
		
		for (FundinLetter letter : letters) {
			letter.setTimeStr(DateUtil.dateFormat(
					letter.getTime(), DateUtil.DATEFORMAT_SECOND));
		}
		pageQuery.setElements(letters);
		return pageQuery;
	}

	public boolean updateSomeInfo(FundinUser user) {
		if (Constants.UPDATE_SUCCESS == userDao.updateSomeInfo(user)){
			return true;
		}
		return false;
	}

	public FundinSchool getSchoolByUserId(Long userId) {
		return userDao.getSchoolByUserId(userId);
	}
	
	public Long queryIdByPhone(String phone){
		return userDao.queryIdByPhone(phone);
	}
	
	public boolean updateInviteCount(Long userId) {
		if (Constants.UPDATE_SUCCESS == userDao.updateInviteCount(userId))
			return true;
		return false;
	}
	
	public List<InviteSort> getInviteNo1(Integer inviteCount){
		return userDao.getInviteNo1(inviteCount);
	}
	
	public List<InviteSort> getInviteSort() {
		return userDao.getInviteSort();
	}
	
	public List<FundinUser> getInvitedUserListByUserId(Long userId, int pageNum, int pageSize) {
		List<FundinUser> userList = userDao.getInviteUserList(userId, pageNum - 1, pageSize);
		for (FundinUser user : userList) {
			user.setHeadImg(ImagePathConventer.convert2Small(user.getHeadImg()));
			user.setConstellation(actionService.getConstellation(user));
			user.setSchoolModel(schoolHelper.getById(user.getSchoolId()));
		}
		return userList;
	}
}
