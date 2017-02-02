package com.fundin.service.common;

import org.apache.commons.lang3.StringUtils;

public class ImagePathConventer {

	public static String convert2Small(String orig) {
		if (StringUtils.isBlank(orig)) {
			return orig;
		}
		if (orig.startsWith("/static")) {
			return "/static/dist/image/default-header.png";
		}
		return StringUtils.replace(orig, "image", "simg");
	}
	
	public static String convert2Middle(String orig) {
		if (StringUtils.isBlank(orig)) {
			return orig;
		}
		if (orig.startsWith("/static")) {
			return "/static/dist/image/default-header.png";
		}
		return StringUtils.replace(orig, "image", "mimg");
	}

	public static String convertContent(String orig) {
		if (StringUtils.isBlank(orig)) {
			return orig;
		}
		return StringUtils.replace(orig, "/fundin/image", "/fundin/mimg");
	}
	
}
