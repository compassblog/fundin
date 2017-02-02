package com.fundin.service.common.excel;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Map;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtil {

	private static final Logger LOG = LoggerFactory.getLogger(
			ExcelUtil.class);
	
	public static final String TEMPLATE_PATH = "template/";
	
	public static void exportExcel(OutputStream os, String templateName, 
			Map<String, Object> dataMap) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dataMap.put("dateFormater", dateFormat);
        
        XLSTransformer transformer = new XLSTransformer();
        InputStream is = null;
        try {
            is = ExcelUtil.class.getClassLoader().getResourceAsStream(
            		TEMPLATE_PATH + templateName);
            Workbook workbook = transformer.transformXLS(is, dataMap);
            workbook.write(os);
            os.flush();
        } catch(Exception e) {
        	LOG.error("ExcelUtil exportExcel error!", e);
        } finally {
	        IOUtils.closeQuietly(is);
	        IOUtils.closeQuietly(os);
        }
	}
	
}
