package com.fundin.utils.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyUtils {
	
	private final static Logger LOG = LoggerFactory.getLogger(
			PropertyUtils.class.getName());
	
	private PropertyUtils() {
		
	}
	
	public static void readProperty(String filePath, LineHandler handler) {
		InputStream is = PropertyUtils.class.getClassLoader().
				getResourceAsStream(filePath);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = null;
			while (null != (line = br.readLine())) {
				handler.handleLine(line);
			}
		} catch (Exception e) {
			LOG.error("readProperty error ! filePath : " + filePath, e);
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					LOG.error("readProperty error ! filePath : " + filePath, e);
				}
			}
		}
	}
	
	public interface LineHandler {
		/**
		 * 处理文件的每一行文本
		 */
		public void handleLine(String line);
	}
	
}
