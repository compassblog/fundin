package com.fundin.web.controller;

import java.io.StringWriter;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fundin.domain.entity.FundinUser;
import com.fundin.domain.type.RedPacketTypeEnum;
import com.fundin.service.common.Constants;
import com.fundin.service.common.PasswdUtil;
import com.fundin.service.common.mail.MailChecker;
import com.fundin.service.component.SchoolHelper;
import com.fundin.service.impl.ActionService;
import com.fundin.service.impl.ProjService;
import com.fundin.service.impl.RedPacketService;
import com.fundin.service.impl.UserService;
import com.fundin.utils.common.ConstellationUtils;
import com.fundin.utils.security.SymmetricEncryptionUtils;
import com.fundin.web.common.HttpHelper;
import com.fundin.web.common.LoginContext;
import com.fundin.web.common.Resp;

@Controller
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(
			UserController.class);
	
	@Resource
	private UserService userService;
	@Resource
	private ProjService projService;
	@Resource
	private ActionService actionService;
	@Resource
	private SchoolHelper schoolHelper;
	@Resource
	private RedPacketService redPacketService;

	@RequestMapping(value = "/register", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object register(@RequestParam(value = "regName", required = true) String name,
			@RequestParam(value = "regEmail", required = true) String email,
			@RequestParam(value = "regPasswd", required = true) String passwd,
			@RequestParam(value = "regType", required = true) Integer type,
			@RequestParam(value = "invPhone", required=false) String invPhone,
			HttpServletResponse response) {
		try {
			if (StringUtils.isBlank(name.replaceAll("[^\u4e00-\u9fa5]", ""))) {
				return Resp.fail("请填写真实姓名或社团名称！");
			}
			if (userService.queryIdByEmail(email) != null) {
				return Resp.fail("该邮箱已经注册过！");
			}
			if (!MailChecker.checkMailExist(email)) {
				return Resp.fail("该邮箱不存在！");
			}
			
			Long invUserId = userService.queryIdByPhone(invPhone);
			FundinUser user = userService.register(
					name, email, null, passwd, type, invUserId);
			if (null != user) {
				HttpHelper.setCookie(response, null, Constants.LOGIN_COOKIE_NAME, 
						SymmetricEncryptionUtils.encryptByAES(user.getId().toString(), SymmetricEncryptionUtils.ENCRYPT_KEY_COOKIE));
				HttpHelper.setCookie(response, null, Constants.FIRST_LOGIN_KEY, "1");
				userService.sendWelcomeEmail(email);
				
				if (StringUtils.isNotBlank(invPhone) && invUserId != null) {
					Long amountCount = redPacketService.checkAmount(invUserId,
							RedPacketTypeEnum.REDPACKET_TYPE_Z.getCode());
					if (amountCount < 100)
						redPacketService.createRedPacket(invUserId, RedPacketTypeEnum.REDPACKET_TYPE_Z.getCode(),
								Constants.REDPACKET_AMOUNT_5, 10);
					userService.updateInviteCount(invUserId);
				}
				return Resp.succ(null);
			}
		} catch (Exception ex) {
			LOG.error("register ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object login(@RequestParam(value = "loginEmail", required = true) String email, 
			@RequestParam(value = "loginPasswd", required = true) String passwd, 
			@RequestParam(value = "rememberMe", required = false) String rememberMe,
			HttpServletResponse response) {
		try {
			FundinUser user = userService.login(email, passwd);
			if (null != user) {
				HttpHelper.setCookie(response, rememberMe, Constants.LOGIN_COOKIE_NAME, 
						SymmetricEncryptionUtils.encryptByAES(user.getId().toString(), SymmetricEncryptionUtils.ENCRYPT_KEY_COOKIE));
				return Resp.succ(null);
			}
		} catch (Exception ex) {
			LOG.error("login ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/forgetPasswd", method = RequestMethod.POST,
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object forgetPasswd(@RequestParam(value = "email", required = true) String email,
			HttpServletResponse response){
		try {
			Long id = userService.queryIdByEmail(email);
			if (id != null) {
				String key = SymmetricEncryptionUtils.encryptByAES(id.toString(), SymmetricEncryptionUtils.ENCRYPT_KEY_RESET);
				userService.sendForgetEmail(email, key);
				return Resp.succ(null);
			}
		} catch (Exception ex) {
			LOG.error("forgetPasswd ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public String reset(@RequestParam(value = "key", required = true) String key,
			ModelMap model,HttpServletRequest request){
		try{
			model.put(Constants.Attrs.NAV_HEAD, "resetPage");
			model.put(Constants.Attrs.PAGE_TITLE, "Fundin.cn-密码重置");
			model.put("key", key);
			return "resetPage";
		} catch(Exception ex){
			LOG.error("resetPage ex !", ex);
		}
		return null;
	}
	
	@RequestMapping(value = "/resetPasswd", method = RequestMethod.POST,
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object resetPasswd(@RequestParam(value = "key", required = true) String key,
			@RequestParam(value = "newPasswd", required = true) String newPasswd,
			HttpServletResponse response){
		try {
			Long userId = Long.parseLong(SymmetricEncryptionUtils.decryptByAES(key, SymmetricEncryptionUtils.ENCRYPT_KEY_RESET));
			if (userId != null) {
				FundinUser userWithNewPasswd = new FundinUser();
				String salt = PasswdUtil.generateSalt();
				String securePasswd = PasswdUtil.encryptPasswd(salt, newPasswd);
				userWithNewPasswd.setId(userId);
				userWithNewPasswd.setSalt(salt);
				userWithNewPasswd.setPasswd(securePasswd);
				if (userService.updatePasswd(userWithNewPasswd)) {
					return Resp.succ(null);
				}
			}
		} catch (Exception ex) {
			LOG.error("resetPasswd ex !", ex);
		}
		return Resp.fail(null);
	}
	
	
	@RequestMapping(value = "/logout")
	@ResponseBody
	public void logout(HttpServletResponse response) {
		try {
			HttpHelper.deleteCookie(response, Constants.LOGIN_COOKIE_NAME);
			response.sendRedirect("/");
		} catch (Exception ex) {
			LOG.error("logout ex !", ex);
		}
	}
	
	@RequestMapping(value = "/user/updatePersonalInfo", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updatePersonalInfo(FundinUser user) {
		try {
			user.setId(LoginContext.getLoginContext().getUserId());
			if (userService.updatePersonalInfo(user)) {
				return Resp.succ(null);
			}
		} catch (Exception ex) {
			LOG.error("updatePersonalInfo ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/user/updateBankAccountInfo", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updateBankAccountInfo(FundinUser user) {
		try {
			user.setId(LoginContext.getLoginContext().getUserId());
			if (userService.updateBankAccountInfo(user)) {
				return Resp.succ(null);
			}
		} catch (Exception ex) {
			LOG.error("updateBankAccountInfo ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/user/updatePasswd", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updatePasswd(@RequestParam(value = "id", required = true) Long id, 
			@RequestParam(value = "oldPasswd", required = true) String oldPasswd, 
			@RequestParam(value = "newPasswd", required = true) String newPasswd,
			HttpServletResponse response) {
		try {
			FundinUser user = userService.queryInfo(id);
			String email = user.getEmail();
			FundinUser userWithOldPasswd = userService.login(email, oldPasswd);
			
			if (null != userWithOldPasswd) {
				FundinUser userWithNewPasswd = new FundinUser();
				String salt = PasswdUtil.generateSalt();
				String securePasswd = PasswdUtil.encryptPasswd(salt, newPasswd);
				userWithNewPasswd.setId(id);
				userWithNewPasswd.setSalt(salt);
				userWithNewPasswd.setPasswd(securePasswd);
				if (userService.updatePasswd(userWithNewPasswd)) {
					return Resp.succ(null);
				}
				
			}
		} catch (Exception ex) {
			LOG.error("updatePasswd ex !", ex);
		}
		return Resp.fail(null);
	}
	
	
	
	@RequestMapping(value = "/user/updateSomeInfo", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updateSomeInfo(FundinUser user) {
		try {
			user.setId(LoginContext.getLoginContext().getUserId());
			if (userService.updateSomeInfo(user)) {
				return Resp.succ(null);
			}
		} catch (Exception ex) {
			LOG.error("updateSomeInfo ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/user/getUnivArray", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getUnivArray() {
		try {
			return Resp.succ(schoolHelper.getUnivArray());
		} catch (Exception ex) {
			LOG.error("getUnivArray ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/user/getSchool4Univ", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getSchool4Univ(
			@RequestParam(value = "univ", required = true) String univ) {
		try {
			return Resp.succ(schoolHelper.getSchool4Univ(univ));
		} catch (Exception ex) {
			LOG.error("getSchool4Univ ex !", ex);
		}
		return Resp.fail(null);
	}
	
	private static final int default_user_size = 10;
	private static final int default_proj_size = 5;
	
	@RequestMapping(value = "/user/{userId}")
	public String userPage(@PathVariable("userId") Long userId, ModelMap model, 
			HttpServletRequest request) {
		FundinUser user = userService.queryInfo(userId);
		if (null == user) {
			return "redirect:/static/html/404.html";
		}
		
		try {
			model.put(Constants.Attrs.PAGE_TITLE, user.getName() + " - Fundin.cn");
			model.put("user", user);
			
			if (userId.equals(LoginContext.getLoginContext().getUserId())) {
				model.put("canEdit", true);
			} else {
				model.put("canEdit", false);
				model.put("hasAttention", actionService.hasAttention(
						userId, LoginContext.getLoginContext().getUserId()));
			}
			
			if (null != user.getSchoolId()) {
				model.put("schoolModel", schoolHelper.getById(user.getSchoolId()));
			}
			
			model.put("constellation", getConstellation(user));
			model.put("person", getPerson(user));
			
			model.put("attentionList", actionService.getByAttentionUserId(userId, 1, default_user_size));
			model.put("beAttentionList", actionService.getAttentionByUserId(userId, 1, default_user_size));
			
			model.put("redPacketList", redPacketService.getMyRedPacketInfo(userId));
			model.put("letterList", userService.getLetterList(userId, 1, default_proj_size));
			
			model.put("myProjList", projService.getMyProjList(userId, 1, default_proj_size));
			model.put("myFollowList", projService.getMyFollowList(userId, 1, default_proj_size));
			model.put("mySupportList", projService.getMySupportList(userId, 1, default_proj_size));
		} catch (Exception ex) {
			LOG.error("userPage ex !", ex);
		}
		
		return "user" + user.getType();
	}
	
	private Object getConstellation(FundinUser user) {
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
			LOG.error("getConstellation error !", e);
		}
		
		return null;
	}
	
	private Object getPerson(FundinUser user) {
		if (user.getId().equals(LoginContext.getLoginContext().getUserId())) {
			return "我";
		}
		
		if (user.getType() == 0) {
			if (null == user.getSex()) {
				return "TA";
			} else if (0 == user.getSex()) {
				return "他";
			} else {
				return "她";
			}
		} else {
			return "TA";
		}
	}

	public String getVmContent(String vmPath, Map<String, Object> vmParams) {
		StringWriter writer = new StringWriter();
		
		try {
			VelocityEngine ve = getVelocityEngine();
			Template t = ve.getTemplate(vmPath, Constants.DEFAULT_ENCODING);
			VelocityContext context = getVelocityContext(vmParams);
			t.merge(context, writer);
		} catch(Exception e) {
			LOG.error("getVmContent error, vmPath: {}", vmPath, e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
		
		return writer.toString();
	}
	
	private VelocityEngine getVelocityEngine() throws Exception{
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(Velocity.INPUT_ENCODING, Constants.DEFAULT_ENCODING);
		ve.setProperty(Velocity.OUTPUT_ENCODING, Constants.DEFAULT_ENCODING);
		ve.setProperty("resource.loader", "class");
		ve.setProperty("class.resource.loader.class", 
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		ve.init();
		return ve;
	}

	private VelocityContext getVelocityContext(Map<String, Object> vmParams){
		VelocityContext context = new VelocityContext();
		if(vmParams != null && vmParams.size() > 0){
			for(Map.Entry<String, Object> param : vmParams.entrySet()){
				context.put(param.getKey(), param.getValue());
			}
		}
		return context;
	}
	
	@RequestMapping(value = "/initUS", method = RequestMethod.GET, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object initUS(
			@RequestParam(value = "code", required = true) String code) {
		if (!"fundin2015".equals(code)) {
			return null;
		}
		try {
			schoolHelper.initUS();
			return Resp.succ("初始导入成功");
		} catch (Exception ex) {
			LOG.error("initUS ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/user/hasPersonalInfo", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object hasPersonalInfo() {
		try {
			FundinUser user = userService.queryInfo(LoginContext.getLoginContext().getUserId());
			if (user.getType() == 0 && (StringUtils.isBlank(user.getSign()) 
					|| StringUtils.isBlank(user.getPhone()) 
					|| null == user.getSex() || null == user.getSchoolId() 
					|| StringUtils.isBlank(user.getBirthday()) 
					|| StringUtils.isBlank(user.getEntryYear()))) {
				return Resp.succ(0);
			}
			if (user.getType() == 1 && (StringUtils.isBlank(user.getSign()) 
					|| StringUtils.isBlank(user.getPhone()) 
					|| null == user.getSchoolId() 
					|| StringUtils.isBlank(user.getBirthday()))) {
				return Resp.succ(0);
			}
			return Resp.succ(1);
		} catch (Exception ex) {
			LOG.error("hasPersonalInfo ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/user/hasBankAccountInfo", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object hasBankAccountInfo() {
		try {
			FundinUser user = userService.queryInfo(LoginContext.getLoginContext().getUserId());
			if (StringUtils.isBlank(user.getBankName()) || StringUtils.isBlank(user.getBankAccount()) || 
					StringUtils.isBlank(user.getBankUserName())) {
				return Resp.succ(0);
			}
			return Resp.succ(1);
		} catch (Exception ex) {
			LOG.error("hasBankAccountInfo ex !", ex);
		}
		return Resp.fail(null);
	}
	
}
