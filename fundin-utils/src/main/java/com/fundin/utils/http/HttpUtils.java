package com.fundin.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {

	private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

	/*
	 * 发送Http请求并返回响应结果
	 */
	public static String sendRequest(RequestBuilder requestBuilder) {
		CloseableHttpClient client = HttpConnectionManager.getHttpClient();
		CloseableHttpResponse response = null;
		String result = null;
		try {
			response = client.execute(requestBuilder.buildRequest());
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			throw new HttpClientException(e.getMessage(), e);
		} finally {
			closeResponse(response);
		}
		return result;
	}

	/*
	 * 关闭CloseableHttpResponse
	 */
	private static void closeResponse(CloseableHttpResponse response) {
		try {
			if (response != null) {
				response.close();
			}
		} catch (Exception e) {
			LOG.error("closeResponse error!", e);
		}
	}

	public interface RequestBuilder {
		/*
		 * 组装用于发送请求的HttpRequestBase
		 */
		public HttpRequestBase buildRequest() throws Exception;
	}

}