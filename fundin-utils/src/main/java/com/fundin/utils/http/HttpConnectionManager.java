package com.fundin.utils.http;

import java.util.concurrent.TimeUnit;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpConnectionManager {

	private static PoolingHttpClientConnectionManager connectionManager;

	/**
	 * 最大连接数
	 */
	private final static int MAX_TOTAL_CONNECTIONS = 200;
	/**
	 * 每个路由最大连接数
	 */
	private final static int MAX_ROUTE_CONNECTIONS = 20;
	/**
	 * 关闭空闲连接等待时间
	 */
	private final static int CLOSE_IDLE_TIMEOUT = 5000;

	static {
		connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		connectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
		connectionManager.closeIdleConnections(CLOSE_IDLE_TIMEOUT, TimeUnit.MILLISECONDS);
	}

	public static CloseableHttpClient getHttpClient() {
		return HttpClients.custom().setConnectionManager(connectionManager).build();
	}

}
