/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package com.fundin.pay.wx;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * This example demonstrates how to create secure connections with a custom SSL
 * context.
 */
public class WXCustomSSL {

	private static SSLConnectionSocketFactory sslsf;
	
	static {
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
	        FileInputStream instream = new FileInputStream(new File(WXPayConstants.WX_KEY_STORE_PATH));
	        try {
	            keyStore.load(instream, WXPayConstants.WX_MCH_ID.toCharArray());
	        } finally {
	            instream.close();
	        }

	        // Trust own CA and all self-signed certs
	        SSLContext sslcontext = SSLContexts.custom()
	                .loadKeyMaterial(keyStore, WXPayConstants.WX_MCH_ID.toCharArray())
	                .build();
	        // Allow TLSv1 protocol only
	        sslsf = new SSLConnectionSocketFactory(
	                sslcontext,
	                new String[] { "TLSv1" },
	                null,
	                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		} catch (Exception e) {
			throw new RuntimeException("init WXCustomSSL SSLConnectionSocketFactory error !", e);
		}
	}
	
    private static final String DEFAULT_ENCODING = "utf-8";
    
    public static String post(String postUrl, String postBody) {
    	CloseableHttpClient httpsClient = HttpClients.custom()
                 .setSSLSocketFactory(sslsf).build();
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
			throw new RuntimeException("WXCustomSSL post error !", e);
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
