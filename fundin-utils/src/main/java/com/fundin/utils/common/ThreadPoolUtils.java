package com.fundin.utils.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtils {

	private static ExecutorService executor;

	/**
	 * 线程池维护线程的核心(最少)数量
	 */
	private final static int CORE_POOL_SIZE = 100;
	/**
	 * 线程池维护线程的最大数量
	 */
	private final static int MAXIMUM_POOL_SIZE = 200;
	/**
	 * 线程池维护线程所允许的空闲时间
	 */
	private final static int KEEP_ALIVE_TIME = 60;
	/**
	 * 线程池所使用的缓冲队列的最大长度
	 */
	private static final int MaxQueueSize = 500;

	static {
		executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(MaxQueueSize), new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public static ExecutorService getExecutor() {
		return executor;
	}

}
