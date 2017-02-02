package com.fundin.utils.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class QRUtils {
	
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	   
	private static final String Default_Format = "png";
	private static final String Default_CHARACTER_SET = "utf-8";
	
	public static BitMatrix createQRCode(String content, int width, int height) {  
	    Map<EncodeHintType, Object> hints = Maps.newHashMap();
	    hints.put(EncodeHintType.CHARACTER_SET, Default_CHARACTER_SET);
	    try {
	        return new MultiFormatWriter().encode(
	        		content, BarcodeFormat.QR_CODE, width, height, hints);
	    } catch (Exception e) {
	        throw new RuntimeException("createQRCode error !");
	    }
	}
	
	private static BufferedImage toBufferedImage(BitMatrix matrix) {  
	    int width = matrix.getWidth();
	    int height = matrix.getHeight();
	    BufferedImage image = new BufferedImage(
	    		width, height, BufferedImage.TYPE_INT_RGB); 
	    for (int x = 0; x < width; x++) {
		    for (int y = 0; y < height; y++) {
		    	image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
		    }
	    }
	    return image;
    }
	
	public static void writeToFile(BitMatrix matrix, File file) 
			throws Exception {  
		BufferedImage image = toBufferedImage(matrix);
		if (! ImageIO.write(image, Default_Format, file)) {
			throw new RuntimeException("writeToFile error !");
		}
	}  
   
     
   public static void writeToStream(BitMatrix matrix, OutputStream output) 
		   throws Exception {  
	   BufferedImage image = toBufferedImage(matrix);
	   if (! ImageIO.write(image, Default_Format, output)) {
		   throw new RuntimeException("writeToStream error !");
	   }
   }
   
}
