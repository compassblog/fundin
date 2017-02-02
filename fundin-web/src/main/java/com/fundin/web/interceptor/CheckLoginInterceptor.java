package com.fundin.web.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fundin.domain.entity.FundinUser;
import com.fundin.service.common.Constants;
import com.fundin.service.component.MessageHandler;
import com.fundin.service.impl.UserService;
import com.fundin.utils.security.SymmetricEncryptionUtils;
import com.fundin.web.common.HttpHelper;
import com.fundin.web.common.LoginContext;

public class CheckLoginInterceptor extends HandlerInterceptorAdapter {

	private final static Logger LOG = LoggerFactory.getLogger(
			CheckLoginInterceptor.class.getName());
	
	@Resource
	private UserService userService;
	@Resource
	private MessageHandler messageHandler;
	
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception {
		LoginContext context = initContext();
		
		/**
		 *	首次登录标记
		 */
		String firstLogin = HttpHelper.getCookie(request, Constants.FIRST_LOGIN_KEY);
		if (StringUtils.isNotBlank(firstLogin)) {
			request.setAttribute(Constants.Attrs.LOGIN_FIRST, true);
			HttpHelper.deleteCookie(response, Constants.FIRST_LOGIN_KEY);
		}
		
		request.setAttribute(Constants.Attrs.LOGIN_STATUS, false);
		try {
			String cookieVal = HttpHelper.getCookie(request, Constants.LOGIN_COOKIE_NAME);
			if (StringUtils.isNotBlank(cookieVal)) {
				context.setLoggedin(true);
				request.setAttribute(Constants.Attrs.LOGIN_STATUS, true);
				
				Long userId = Long.parseLong(SymmetricEncryptionUtils.decryptByAES(cookieVal, SymmetricEncryptionUtils.ENCRYPT_KEY_COOKIE));
				context.setUserId(userId);
				request.setAttribute(Constants.Attrs.LOGIN_USERID, userId);
				
				FundinUser user = userService.queryInfo(userId);
				if (null != user) {
					context.setUserName(user.getName());
					context.setUserType(user.getType());
					context.setUserHeadImg(user.getHeadImg());
					
					request.setAttribute(Constants.Attrs.LOGIN_USERNAME, user.getName());
					request.setAttribute(Constants.Attrs.LOGIN_USERTYPE, user.getType());
					request.setAttribute(Constants.Attrs.LOGIN_USERHEADIMG, user.getHeadImg());
				}
				
				request.setAttribute(Constants.Attrs.MESSAGE_COUNT, 
						messageHandler.getMessageCount(userId));
				request.setAttribute(Constants.Attrs.MESSAGE_LIST, 
						messageHandler.getMessageList(userId));
			}
		} catch (Exception ex) {
			LOG.error("CheckLogin error !", ex);
			response.getWriter().print("CheckLogin error !");
			return false;
		}
		return true;
	}

	private LoginContext initContext() {
		LoginContext context = LoginContext.getInstance();
		LoginContext.setLoginContext(context);
		return context;
	}
	
}
