package com.fundin.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fundin.service.common.Constants;
import com.fundin.utils.security.SymmetricEncryptionUtils;
import com.fundin.web.common.HttpHelper;
import com.fundin.web.common.MContext;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	private final static Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class.getName());
	
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception {
		MContext mContext = MContext.createMContext();
		
		try {
			String cookieVal = HttpHelper.getCookie(request, Constants.LOGIN_COOKIE_NAME);
			if (StringUtils.isNotBlank(cookieVal)) {
				mContext.setLoggedin(true);
				Long userId = Long.parseLong(SymmetricEncryptionUtils.decryptByAES(
						cookieVal, SymmetricEncryptionUtils.ENCRYPT_KEY_COOKIE));
				mContext.setUserId(userId);
			}
		} catch (Exception ex) {
			LOG.error("Login error !", ex);
			response.getWriter().print("Login error !");
			return false;
		}
		return true;
	}

	@Override
	public void afterCompletion(
			HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		MContext.remove();
	}
	
}
