package com.fundin.web.common;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

public class HttpHelper {

	/**
	 * 设置Cookie.
	 * @param response
	 * @param cookieName
	 * @param cookieVal
	 * @return
	 */
	public static void setCookie(HttpServletResponse response, 
			String rememberMe, String cookieName, String cookieVal) {
		Cookie cookie = new Cookie(cookieName, cookieVal);
		if ("on".equalsIgnoreCase(rememberMe)) {
			cookie.setMaxAge(60 * 60 * 24 * 7);
		}
		response.addCookie(cookie);
	}
	
	/**
	 * 删除Cookie.
	 * @param response
	 * @param cookieName
	 * @return
	 */
	public static void deleteCookie(HttpServletResponse response, 
			String cookieName) {
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
	/**
	 * 获取Cookie.
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * 获取多个Cookie.
	 * 
	 * @param request
	 * @param cookieMap
	 */
	public static void getCookies(HttpServletRequest request, 
			Map<String, String> cookieMap) {
		if (MapUtils.isEmpty(cookieMap)) {
			return;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				String cookieName = cookie.getName();
				if (cookieMap.containsKey(cookieName)) {
					cookieMap.put(cookieName, cookie.getValue());
				}
			}
		}
	}

	/**
	 * 获取IP信息.
	 * @param req
	 * @return
	 */
	public static String getIp(HttpServletRequest req) {
		String ip = req.getHeader("X-Forwarded-For");
		if (StringUtils.isNotBlank(ip)) {
			String[] ips = StringUtils.split(ip, ',');
			if (ips != null) {
				for (String tmpip : ips) {
					if (StringUtils.isBlank(tmpip)) {
						continue;
					}
					tmpip = tmpip.trim();
					if (isIPAddr(tmpip) && !tmpip.startsWith("10.") && 
							!tmpip.startsWith("192.168.") && !"127.0.0.1".equals(tmpip)) {
						return tmpip.trim();
					}
				}
			}
		}
		ip = req.getHeader("x-real-ip");
		if (isIPAddr(ip)) {
			return ip;
		}
		ip = req.getRemoteAddr();
		if (ip.indexOf('.') == -1) {
			ip = "127.0.0.1";
		}
		return ip;
		// return "61.139.2.16";
	}

	private static boolean isIPAddr(String addr) {
		if (StringUtils.isEmpty(addr)) {
			return false;
		}
		String[] ips = StringUtils.split(addr, '.');
		if (ips.length != 4) {
			return false;
		}
		try {
			int ipa = Integer.parseInt(ips[0]);
			int ipb = Integer.parseInt(ips[1]);
			int ipc = Integer.parseInt(ips[2]);
			int ipd = Integer.parseInt(ips[3]);
			return (ipa >= 0) && (ipa <= 255) && (ipb >= 0) && (ipb <= 255) && (ipc >= 0) && (ipc <= 255) && (ipd >= 0)
					&& (ipd <= 255);
		} catch (Exception e) {
			// ignore
		}
		return false;
	}

}