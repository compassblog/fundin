package com.fundin.web.common;

public class LoginContext {

	private final static ThreadLocal<LoginContext> holder = 
			new ThreadLocal<LoginContext>();

	private boolean isLoggedin = false;
	private Long userId;
	private String userName;
	private Integer userType;
	private String userHeadImg;
	
	public static LoginContext getInstance() {
		return new LoginContext();
	}

	public static void setLoginContext(LoginContext context) {
		holder.set(context);
	}

	public static LoginContext getLoginContext() {
		return holder.get();
	}

	public static void remove() {
		holder.remove();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isLoggedin() {
		return isLoggedin;
	}

	public void setLoggedin(boolean isLoggedin) {
		this.isLoggedin = isLoggedin;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getUserHeadImg() {
		return userHeadImg;
	}

	public void setUserHeadImg(String userHeadImg) {
		this.userHeadImg = userHeadImg;
	}
	
}
