package com.fundin.utils.https;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class SSLConnectionManager {
	
	private static SSLConnectionSocketFactory connectionFactory;
	
	static {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
					null, new TrustStrategy() {
			    public boolean isTrusted(X509Certificate[] chain, String authType) 
			    		throws CertificateException {
			         return true;
			    }
		    }).build();
			connectionFactory = new SSLConnectionSocketFactory(sslContext);
		} catch (Exception e) {
			throw new RuntimeException("init SSLConnectionSocketFactory error !", e);
		}
	}
	
	public static CloseableHttpClient createSSLClientDefault(){
		try {
			return HttpClients.custom().setSSLSocketFactory(connectionFactory).build();
        } catch (Exception e) {
        	throw new RuntimeException("createSSLClientDefault error !", e);
        }
	}
	
}
