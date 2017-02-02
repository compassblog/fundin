package com.fundin.utils.https;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class SSLUtils {

	private static final String DEFAULT_ENCODING = "utf-8";
	
	public static String post(String postUrl, String postBody) {
		CloseableHttpClient httpsClient = SSLConnectionManager.createSSLClientDefault();
		String result = null;
		CloseableHttpResponse response = null;
		
		try {
			HttpPost post = new HttpPost(postUrl);
			StringEntity postEntity = new StringEntity(postBody, DEFAULT_ENCODING);
			postEntity.setContentType("application/x-www-form-urlencoded");
			post.setEntity(postEntity);
			
			response = httpsClient.execute(post);
			
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				result = EntityUtils.toString(entity, DEFAULT_ENCODING);
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			throw new RuntimeException("SSLUtils post error !", e);
		} finally {
			closeResponse(response);
		}
		
		return result;
	}
	
	private static void closeResponse(CloseableHttpResponse response) {
		try {
			if (response != null) {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
