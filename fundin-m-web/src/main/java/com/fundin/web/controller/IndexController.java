package com.fundin.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fundin.domain.dto.CommentView;
import com.fundin.domain.dto.ProjInfo;
import com.fundin.domain.dto.ProjView;
import com.fundin.domain.entity.FundinProj;
import com.fundin.domain.entity.FundinUser;
import com.fundin.domain.type.ProjLocationEnum;
import com.fundin.domain.type.ProjRequireEnum;
import com.fundin.domain.type.ProjStatusEnum;
import com.fundin.domain.type.ProjSubjectEnum;
import com.fundin.domain.type.RedPacketTypeEnum;
import com.fundin.pay.wx.WXPayConstants;
import com.fundin.service.common.Constants;
import com.fundin.service.common.ImagePathConventer;
import com.fundin.service.component.MessageHandler;
import com.fundin.service.component.SchoolHelper;
import com.fundin.service.impl.ActionService;
import com.fundin.service.impl.CommentService;
import com.fundin.service.impl.ProjService;
import com.fundin.service.impl.RedPacketService;
import com.fundin.service.impl.UserService;
import com.fundin.utils.common.ConstellationUtils;
import com.fundin.utils.common.DateUtil;
import com.fundin.utils.common.ImageUtils;
import com.fundin.utils.common.StringUtil;
import com.fundin.utils.common.ThreadPoolUtils;
import com.fundin.utils.security.SymmetricEncryptionUtils;
import com.fundin.web.common.HttpHelper;
import com.fundin.web.common.MContext;
import com.fundin.web.common.Resp;
import com.fundin.web.wechat.Vcode;
import com.fundin.web.wechat.WXAuthToken;

@Controller
public class IndexController extends BaseController {

	private static final Logger LOG = LoggerFactory.getLogger(IndexController.class); 
	
	@Resource
	private ProjService projService;
	@Resource
	private UserService userService;
	@Resource
	private CommentService commentService;
	@Resource
	private ActionService actionService;
	@Resource
	private SchoolHelper schoolHelper;
	@Resource
	private MessageHandler messageHandler;
	
	@Value("${uploadPath}")
	private String uploadPath;
	
	private static String thumbnailPath;
	@Value("${thumbnailPath}")
	public void setThumbnailPath(String thumbnailPath) {
		IndexController.thumbnailPath = thumbnailPath;
	}

	private static String middlePath;
	@Value("${middlePath}")
	public void setMiddlePath(String middlePath) {
		IndexController.middlePath = middlePath;
	}
	
	@Resource
	private RedPacketService redPacketService;

	
	@RequestMapping(value = {"", "/", "/index"})
	public String index(
			@RequestParam(value = "subject", required = false) Integer subject, 
			@RequestParam(value = "require", required = false) String require, 
			@RequestParam(value = "location", required = false) String location,
			ModelMap model, HttpServletRequest request) {
		addNavHead(model, "index");
		addPageTitle(model, "Fundin.cn - 国内第一校园众筹社交网站");
		
		model.put("ProjSubjectEnum", ProjSubjectEnum.class);
		model.put("ProjRequireEnum", ProjRequireEnum.class);
		model.put("ProjLocationEnum", ProjLocationEnum.class);
		
		model.put(Constants.Attrs.NAV_SUBJECT, "");
		model.put(Constants.Attrs.NAV_REQUIRE, "new");
		model.put(Constants.Attrs.NAV_LOCATION, "national");
		if (null != subject) {
			model.put(Constants.Attrs.NAV_SUBJECT, subject);
		}
		if (StringUtils.isNotBlank(require)) {
			model.put(Constants.Attrs.NAV_REQUIRE, require);
		}
		if (StringUtils.isNotBlank(location)) {
			model.put(Constants.Attrs.NAV_LOCATION, location);
		}
		
		try {
			List<ProjView> viewList = projService.getProjViewList(subject, require, null, location, 0, 0);
			convertImagePath(viewList);
			model.put("projViewList", viewList);
		} catch (Exception ex) {
			LOG.error("index ex !", ex);
		}
		return "index";
	}
	
	private void convertImagePath(List<ProjView> viewList) {
		if (CollectionUtils.isEmpty(viewList)) {
			return ;
		}
		for (ProjView view : viewList) {
			view.setCoverImg(ImagePathConventer.convert2Small(view.getCoverImg()));
			view.setUserHeadImg(ImagePathConventer.convert2Small(view.getUserHeadImg()));
		}
	}

	@RequestMapping(value = "/m/login")
	public String loginPage(
			@RequestParam(value = "returnUrl", required = false) String returnUrl, 
			ModelMap model, HttpServletRequest request) throws Exception {
		model.put("appId", WXPayConstants.WX_APP_ID);
		return "login";
	}
	
	@RequestMapping(value = "/loginByWx")
	public String loginByWx(
			@RequestParam(value = "code", required = true) String code, 
			@RequestParam(value = "state", required = true) String state, 
			HttpServletResponse response) {
		try {
			String userinfo = WXAuthToken.getUserInfoByCode(code);
			if (StringUtils.isBlank(userinfo)) {
				return "redirect:/";
			}
			
			FundinUser user = userService.loginByWx(userinfo);
			LOG.warn("user : {}", user);
			if (null != user) {
				HttpHelper.setCookie(response, "on", Constants.LOGIN_COOKIE_NAME, 
						SymmetricEncryptionUtils.encryptByAES(user.getId().toString(), SymmetricEncryptionUtils.ENCRYPT_KEY_COOKIE));
				return "redirect:/me";
			}
		} catch (Exception ex) {
			LOG.error("loginByWx ex !", ex);
		}
		return "redirect:/";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object login(@RequestParam(value = "phone", required = true) String phone, 
			@RequestParam(value = "passwd", required = true) String passwd, 
			@RequestParam(value = "rememberMe", required = false) String rememberMe,
			HttpServletResponse response) {
		try {
			FundinUser user = userService.loginByPhone(phone, passwd);
			if (null != user) {
				HttpHelper.setCookie(response, rememberMe, Constants.LOGIN_COOKIE_NAME, 
						SymmetricEncryptionUtils.encryptByAES(user.getId().toString(), SymmetricEncryptionUtils.ENCRYPT_KEY_COOKIE));
				return Resp.succ(null);
			}
		} catch (Exception ex) {
			LOG.error("login ex !", ex);
		}
		return Resp.fail("手机号或密码错误");
	}
	
	@RequestMapping(value = "/m/register")
	public String registerPage(ModelMap model, HttpServletRequest request) {
		model.put("page-token", StringUtil.generateRandomString(8));
		return "register";
	}
	
	@RequestMapping(value = "/sendVcode", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object sendVcode(
			@RequestParam(value = "phone", required = true) String phone, 
			@RequestParam(value = "token", required = true) String token) {
		Vcode.createCode(token, phone);
		return Resp.succ(null);
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object register(
			@RequestParam(value = "regName", required = true) String name,
			@RequestParam(value = "regPhone", required = true) String phone,
			@RequestParam(value = "vcode", required = true) String vcode,
			@RequestParam(value = "regPasswd", required = true) String passwd,
			@RequestParam(value = "regType", required = true) Integer type,
			@RequestParam(value = "invPhone", required=false) String invPhone,
			@RequestParam(value = "token", required = true) String token, 
			HttpServletResponse response) {
		try {
			if (StringUtils.isBlank(name.replaceAll("[^\u4e00-\u9fa5]", ""))) {
				return Resp.fail("请填写真实姓名或社团名称！");
			}
			if (userService.queryIdByPhone(phone) != null) {
				return Resp.fail("该手机号已经注册过！");
			}
			if (! Vcode.checkCode(token, vcode)) {
				return Resp.fail("手机验证码错误！");
			}
			
			Long invUserId = userService.queryIdByPhone(invPhone);
			FundinUser user = userService.register(
					name, null, phone, passwd, type, invUserId);
			if (null != user) {
				HttpHelper.setCookie(response, null, Constants.LOGIN_COOKIE_NAME, 
						SymmetricEncryptionUtils.encryptByAES(user.getId().toString(), SymmetricEncryptionUtils.ENCRYPT_KEY_COOKIE));
				
				if (invPhone != null && invUserId != null) {
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
		return Resp.fail("注册失败");
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
	
	@RequestMapping(value = "/me")
	public String me(ModelMap model, HttpServletRequest request) {
		if (! MContext.getMContext().isLoggedin()) {
			return "redirect:/m/login?returnUrl=/me";
		}
		FundinUser user = userService.queryInfo(MContext.getMContext().getUserId());
		if (null == user) {
			return "redirect:/";
		}
		try {
			addNavHead(model, "me");
			addPageTitle(model, user.getName() + " - Fundin.cn");
			user.setHeadImg(ImagePathConventer.convert2Small(user.getHeadImg()));
			model.put("user", user);
			
			model.put("me", true);
			model.put("person", "我");
			model.put("messageCount", messageHandler.getMessageCount(user.getId()));
			
			if (null != user.getSchoolId()) {
				model.put("schoolModel", schoolHelper.getById(user.getSchoolId()));
			}
			model.put("constellation", getConstellation(user));
		} catch (Exception ex) {
			LOG.error("me ex !", ex);
		}
		return "me";
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
	
	@RequestMapping(value = "/profile")
	public String profile(ModelMap model, HttpServletRequest request) {
		if (! MContext.getMContext().isLoggedin()) {
			return "redirect:/m/login?returnUrl=/profile";
		}
		FundinUser user = userService.queryInfo(MContext.getMContext().getUserId());
		if (null == user) {
			return "redirect:/";
		}
		try {
			addPageTitle(model, user.getName() + " - 资料 - Fundin.cn");
			user.setHeadImg(ImagePathConventer.convert2Small(user.getHeadImg()));
			model.put("user", user);
			model.put("person", "TA");
			
			if (null != user.getSchoolId()) {
				model.put("schoolModel", schoolHelper.getById(user.getSchoolId()));
			}
		} catch (Exception ex) {
			LOG.error("me ex !", ex);
		}
		return "profile";
	}
	
	@RequestMapping(value = "/message")
	public String message(ModelMap model, HttpServletRequest request) {
		if (! MContext.getMContext().isLoggedin()) {
			return "redirect:/m/login?returnUrl=/profile";
		}
		FundinUser user = userService.queryInfo(MContext.getMContext().getUserId());
		if (null == user) {
			return "redirect:/";
		}
		try {
			addPageTitle(model, user.getName() + " - 消息 - Fundin.cn");
			model.put("messages", messageHandler.getMessageList(user.getId()));
			model.put("userid", user.getId());
		} catch (Exception ex) {
			LOG.error("message ex !", ex);
		}
		return "message";
	}
	
	@RequestMapping(value = "/account")
	public String account(ModelMap model, HttpServletRequest request) {
		if (! MContext.getMContext().isLoggedin()) {
			return "redirect:/m/login?returnUrl=/profile";
		}
		FundinUser user = userService.queryInfo(MContext.getMContext().getUserId());
		if (null == user) {
			return "redirect:/";
		}
		try {
			addPageTitle(model, user.getName() + " - 账户 - Fundin.cn");
			model.put("user", user);
			
		} catch (Exception ex) {
			LOG.error("account ex !", ex);
		}
		return "account";
	}
	
	@RequestMapping(value = "/user/updateSomeInfo", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updateSomeInfo(FundinUser user) {
		try {
			user.setId(MContext.getMContext().getUserId());
			if (userService.updateSomeInfo(user)) {
				return Resp.succ(null);
			}
		} catch (Exception ex) {
			LOG.error("updateSomeInfo ex !", ex);
		}
		return Resp.fail("保存失败");
	}
	
	private static class GenThumbnailTask implements Runnable {

		private File sourceImage;
		private String imageName;
		
		public GenThumbnailTask(File sourceImage, String imageName) {
			this.sourceImage = sourceImage;
			this.imageName = imageName;
		}
		
		@Override
		public void run() {
			try {
				ImageUtils.resizeImage(sourceImage, 
						new File(thumbnailPath, imageName), 60, 60, false);
				ImageUtils.resizeImage(sourceImage, 
						new File(middlePath, imageName), 400, 4000, true);
			} catch (IOException e) {
				LOG.error("GenThumbnailTask error !", e);
			}
		}
		
	}
	
	@RequestMapping(value = "/user/getUnivArray", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getUnivArray() {
		try {
			return Resp.succ(schoolHelper.getUnivArray());
		} catch (Exception ex) {
			LOG.error("getUnivArray ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/user/getSchool4Univ", produces = "application/json;charset=UTF-8")
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
	
	@RequestMapping(value = "/user/{userId}")
	public String user(@PathVariable("userId") Long userId, 
			ModelMap model, HttpServletRequest request) {
		if (MContext.getMContext().isLoggedin() && 
				userId.equals(MContext.getMContext().getUserId())) {
			return "redirect:/me";
		}
		FundinUser user = userService.queryInfo(userId);
		if (null == user) {
			return "redirect:/";
		}
		try {
			addPageTitle(model, user.getName() + " - Fundin.cn");
			user.setHeadImg(ImagePathConventer.convert2Small(user.getHeadImg()));
			model.put("user", user);
			model.put("person", "TA");
			
			if (null != user.getSchoolId()) {
				model.put("schoolModel", schoolHelper.getById(user.getSchoolId()));
			}
			model.put("constellation", getConstellation(user));
		} catch (Exception ex) {
			LOG.error("user ex !", ex);
		}
		return "me";
	}
	
	@RequestMapping(value = "/myAtten/{userId}")
	public String myAtten(@PathVariable("userId") Long userId, 
			ModelMap model, HttpServletRequest request) {
		FundinUser user = userService.queryInfo(userId);
		if (null == user) {
			return "redirect:/";
		}
		addBarTitle(model, user.getName() + "关注的人");
		model.put("userList", actionService.getBeAttentionUserListByUserId(userId, 1, 100));
		return "userListPage";
	}
	
	@RequestMapping(value = "/beAtten/{userId}")
	public String beAtten(@PathVariable("userId") Long userId, 
			ModelMap model, HttpServletRequest request) {
		FundinUser user = userService.queryInfo(userId);
		if (null == user) {
			return "redirect:/";
		}
		addBarTitle(model, "关注" + user.getName() + "的人");
		model.put("userList", actionService.getAttentionUserListByUserId(userId, 1, 100));
		return "userListPage";
	}
	
	@RequestMapping(value = "/invite/{userId}")
	public String Invite(@PathVariable("userId") Long userId, 
			ModelMap model) {
		FundinUser user = userService.queryInfo(userId);
		if (null == user) {
			return "redirect:/";
		}
		addBarTitle(model, user.getName() + "邀请的人");
		model.put("userList", userService.getInvitedUserListByUserId(userId, 1, 100));
		return "userListPage";
	}
	
	@RequestMapping(value = "/letter/{userId}")
	public String letter(@PathVariable("userId") Long userId, 
			ModelMap model, HttpServletRequest request) {
		model.put("letterList", userService.getLetterList(userId, 1, 100));
		return "letterPage";
	}
	
	@RequestMapping(value = "/redpacket/{userId}")
	public String redpacket(@PathVariable("userId") Long userId, ModelMap model) {
		model.put("redPacketList", redPacketService.getMyRedPacketInfo(userId));
		return "redpacketPage";
	}
	
	@RequestMapping(value = "/proj/{projId}")
	public String project(@PathVariable("projId") Long projId, 
			ModelMap model, HttpServletRequest request) {
		try {
			FundinProj proj = projService.getProj(projId);
			if (null == proj || proj.getStatus() < ProjStatusEnum.FUNDING.getCode()) {
				return "redirect:/";
			}
			
			addPageTitle(model,  proj.getTitle() + " - Fundin.cn");
			proj.setCoverImg(ImagePathConventer.convert2Middle(proj.getCoverImg()));
			proj.setContent(ImagePathConventer.convertContent(proj.getContent()));
			proj.setRepayImage(ImagePathConventer.convert2Small(proj.getRepayImage()));
			model.put("proj", proj);
			
			ProjInfo projInfo = projService.getProjInfoForM(proj, MContext.getMContext().getUserId());
			model.put("projInfo", projInfo);
			
			FundinUser user = userService.queryInfo(proj.getUserId());
			user.setHeadImg(ImagePathConventer.convert2Small(user.getHeadImg()));
			model.put("user", user);
			
			List<CommentView> commentList = commentService.getCommentViewList(projId);
			convertCommentView(commentList);
			model.put("commentViewList", commentList);
			
			model.put("appId", WXPayConstants.WX_APP_ID);
		} catch (Exception ex) {
			LOG.error("project ex !", ex);
		}
		return "project";
	}
	
	@RequestMapping(value = "/myPro/{userId}")
	public String myProj(@PathVariable("userId") Long userId, 
			ModelMap model, HttpServletRequest request) {
		FundinUser user = userService.queryInfo(userId);
		if (null == user) {
			return "redirect:/";
		}
		addBarTitle(model, user.getName() + "发起的项目");
		model.put("projViewList", projService.getMyProjViewList(userId, 1, 100));
		return "proListPage";
	}
	
	@RequestMapping(value = "/supportPro/{userId}")
	public String supportPro(@PathVariable("userId") Long userId, 
			ModelMap model, HttpServletRequest request) {
		FundinUser user = userService.queryInfo(userId);
		if (null == user) {
			return "redirect:/";
		}
		addBarTitle(model, user.getName() + "支持的项目");
		model.put("projViewList", projService.getMySupportProjViewList(userId, 1, 100));
		return "proListPage";
	}
	
	@RequestMapping(value = "/followPro/{userId}")
	public String myFollowProj(@PathVariable("userId") Long userId, 
			ModelMap model, HttpServletRequest request) {
		FundinUser user = userService.queryInfo(userId);
		if (null == user) {
			return "redirect:/";
		}
		addBarTitle(model, user.getName() + "关注的项目");
		model.put("projViewList", projService.getMyFollowProjViewList(userId, 1, 100));
		return "proListPage";
	}

	private void convertCommentView(List<CommentView> commentList) {
		if (CollectionUtils.isEmpty(commentList)) {
			return ;
		}
		for (CommentView view : commentList) {
			view.setUserHeadImg(ImagePathConventer.convert2Small(view.getUserHeadImg()));
		}
	}

	@RequestMapping(value = "/saveUserAction", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object saveUserAction(
			@RequestParam(value = "type", required = true) Integer type, 
			@RequestParam(value = "projId", required = true) Long projId) {
		try {
			if (actionService.saveUserAction(
					MContext.getMContext().getUserId(), projId, type)) {
				return Resp.succ(null);
			}
		} catch (Exception ex) {
			LOG.error("saveUserAction ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/startup")
	public String startup(ModelMap model) {
		if (! MContext.getMContext().isLoggedin()) {
			return "redirect:/m/login?returnUrl=/startup";
		}
		model.put(Constants.Attrs.NAV_HEAD, "startup");
		model.put(Constants.Attrs.PAGE_TITLE, "发起项目 - Fundin.cn");
		
		FundinUser user = userService.queryInfo(MContext.getMContext().getUserId());
		model.put("user", user);
		FundinProj proj = projService.getStartingProj(
				MContext.getMContext().getUserId());
		if (null != proj) {
			model.put("proj", proj);
		}
		return "startup";
	}
	
	@RequestMapping(value = "/proj/saveDraft", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object saveDraft(FundinProj proj) {
		try {
			proj.setUserId(MContext.getMContext().getUserId());
			Long projId = projService.saveDraft(proj);
			if (null != projId) {
				return Resp.succ(projId);
			}
		} catch (Exception ex) {
			LOG.error("saveDraft ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/proj/startNew", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object startNew(FundinProj proj) {
		try {
			proj.setUserId(MContext.getMContext().getUserId());
			Long projId = projService.startNew(proj);
			if (null != projId) {
				return Resp.succ(projId);
			}
		} catch (Exception ex) {
			LOG.error("startNew ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/user/hasPersonalInfo", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object hasPersonalInfo() {
		try {
			FundinUser user = userService.queryInfo(MContext.getMContext().getUserId());
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
	
	@RequestMapping(value = "/updateMessageStatus", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object updateMessageStatus(
			@RequestParam(value = "type", required = false) String type, 
			@RequestParam(value = "projId", required = false) Long projId) {
		try {
			messageHandler.updateMessageStatus(
					MContext.getMContext().getUserId(), projId, type);
			return Resp.succ(null);
		} catch (Exception ex) {
			LOG.error("updateMessageStatus ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/uploadImage", 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object uploadImage(@RequestParam("imgFile") MultipartFile imgFile) {
		try {
			String newImgName = generateImgName();
			File targetFile = compressImage(imgFile, newImgName);
			addThumbnailTask(targetFile, newImgName);
			return Resp.succ(Constants.FUNDIN_IMG_PATH + newImgName);
		} catch (Exception ex) {
			LOG.error("uploadImage ex !", ex);
		}
		return Resp.fail(null);
	}
	
	private String generateImgName() {
		return (int)(Math.random() * 1000000) 
				+ DateUtil.dateFormat(new Date(), DateUtil.DATEFORMAT_TRIM) 
				+ ".jpg";
	}
	
	private static long big_img_size = 1024 * 1024L;
	private float obtainCprQuality(MultipartFile imgFile) {
		long imgSize = imgFile.getSize();
		if (imgSize >= big_img_size) {
			return 0.2f;
		} else {
			return 0.5f;
		}
	}
	
	private File compressImage(MultipartFile imgFile, String newImgName) throws IOException {
		File targetFile = new File(uploadPath, newImgName);
		ImageUtils.compressQuality(imgFile.getInputStream(), targetFile, 
				obtainCprQuality(imgFile));
		return targetFile;
	}
	
	private void addThumbnailTask(File sourceImage, String newImgName) {
		ThreadPoolUtils.getExecutor().execute(
				new GenThumbnailTask(sourceImage, newImgName));
	}
	
}
