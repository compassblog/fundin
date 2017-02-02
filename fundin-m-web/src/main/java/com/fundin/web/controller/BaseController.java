package com.fundin.web.controller;

import org.springframework.ui.ModelMap;

import com.fundin.service.common.Constants;

public class BaseController {

	protected void addPageTitle(ModelMap model, String pageTitle) {
		model.put(Constants.Attrs.PAGE_TITLE, pageTitle);
	}
	
	protected void addNavHead(ModelMap model, String navHead) {
		model.put(Constants.Attrs.NAV_HEAD, navHead);
	}
	
	protected void addBarTitle(ModelMap model, String barTitle) {
		model.put(Constants.Attrs.BAR_TITLE, barTitle);
	}
	
}
