package com.fundin.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.fundin.utils.vm.VmStringTool;
import com.fundin.web.common.MContext;

public class AttributeInterceptor extends HandlerInterceptorAdapter {

	@Value("${m.static.dir:dist}")
	private String staticDir;
	
	@Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		if ((modelAndView == null) || (modelAndView.getView() instanceof RedirectView) 
				|| (modelAndView.getViewName().startsWith(UrlBasedViewResolver.REDIRECT_URL_PREFIX))) {
			return;
		}
		
		addVmAttribute(modelAndView);
		addVmToolAttribute(modelAndView);
	}

	private void addVmToolAttribute(ModelAndView modelAndView) {
		modelAndView.addObject("stringTool", new VmStringTool());
	}

	private void addVmAttribute(ModelAndView modelAndView) {
		modelAndView.addObject("dir", staticDir);
		modelAndView.addObject("loginStatus", MContext.getMContext().isLoggedin());
		modelAndView.addObject("loginUserId", MContext.getMContext().getUserId());
	}
	
}
