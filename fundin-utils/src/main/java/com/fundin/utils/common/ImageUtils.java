package com.fundin.utils.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.coobird.thumbnailator.Thumbnails;

public class ImageUtils {

	/**
	 * 生成缩略图
	 * @param sourceImage
	 * @param destImage
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	public static void resizeImage(File sourceImage, File destImage, 
			int width, int height, boolean keepAspectRatio) throws IOException {
		Thumbnails.of(sourceImage).size(width, height)
			.keepAspectRatio(keepAspectRatio).toFile(destImage);
	}
	
	/**
	 * 压缩图片质量
	 * @param inputStream
	 * @param outFile
	 * @param quality
	 * @throws IOException
	 */
	public static void compressQuality(InputStream inputStream, File outFile, 
			float quality) throws IOException {
		Thumbnails.of(inputStream).scale(1f).outputQuality(quality)
			.outputFormat("jpg").toFile(outFile);
	}
	
	public static void main(String[] args) throws Exception {
		InputStream in = new BufferedInputStream(new FileInputStream(
				"C:\\Users\\Administrator.CDYJY-20160612O\\Desktop\\x.png"));
		compressQuality(in, new File(
				"C:\\Users\\Administrator.CDYJY-20160612O\\Desktop\\new-x.jpg"), 0.5f);
		resizeImage(new File("C:\\Users\\Administrator.CDYJY-20160612O\\Desktop\\x.png"), 
					new File("C:\\Users\\Administrator.CDYJY-20160612O\\Desktop\\new-x.jpg"), 
					400, 400, true);
	}
	
}
