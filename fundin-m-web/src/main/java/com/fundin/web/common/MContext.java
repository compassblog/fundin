package com.fundin.web.common;

public class MContext {

	private static ThreadLocal<MContext> mHolder = new ThreadLocal<MContext>();

	private boolean isLoggedin = false;
	private Long userId;
	
	public static MContext createMContext() {
		MContext mContext = getInstance();
		setMContext(mContext);
		return mContext;
	}
	
	public static MContext getInstance() {
		return new MContext();
	}

	public static void setMContext(MContext mContext) {
		mHolder.set(mContext);
	}

	public static MContext getMContext() {
		return mHolder.get();
	}

	public static void remove() {
		mHolder.remove();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isLoggedin() {
		return isLoggedin;
	}

	public void setLoggedin(boolean isLoggedin) {
		this.isLoggedin = isLoggedin;
	}
	
}
