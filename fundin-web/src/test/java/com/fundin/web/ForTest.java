package com.fundin.web;

import java.io.File;

import com.fundin.utils.common.ImageUtils;

public class ForTest {

	public static void main(String[] args) throws Exception {
		File sourceDir = new File("C:\\Users\\XXX\\Desktop\\image");
		File destDir = new File("C:\\Users\\XXX\\Desktop\\middle-image");
		for (File imageFile : sourceDir.listFiles()) {
			ImageUtils.resizeImage(imageFile, new File(destDir, imageFile.getName()),
					400, 4000, true);
		}
	}
	

}
