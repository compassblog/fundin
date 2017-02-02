package com.fundin.service.job;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.fundin.dao.mybatis.FundinProjDao;
import com.fundin.dao.mybatis.FundinUserDao;
import com.fundin.service.component.BannerHelper;
import com.google.common.collect.Sets;

public class CleanImageFileJob {

	private final static Logger LOG = LoggerFactory.getLogger(
			CleanImageFileJob.class.getName());
	
	@Resource
	private FundinUserDao userDao;
	@Resource
	private FundinProjDao projDao;
	@Value("${uploadPath}")
	private String uploadPath;
	
	public void execute() {
		LOG.info("start clean image file job...");
		Set<String> imageInUse = getImageInUse();
		File[] allImageFile = getAllImageFile();
		removeUnusedImage(allImageFile, imageInUse);
		LOG.info("end clean image file job...");
	}

	private void removeUnusedImage(File[] allImageFile, Set<String> imageInUse) {
		for (File imageFile : allImageFile) {
			if (! imageInUse.contains(imageFile.getName())) {
				imageFile.delete();
			}
		}
	}

	private static Date calcDate;
	private static void resetCalcDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR, -6);
		calcDate = calendar.getTime();
	}
	
	private static FilenameFilter dateFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			Date createTime = new Date(new File(dir, name).lastModified());
			if (createTime.before(calcDate)) {
				return true;
			}
			return false;
		}
	};
	
	private File[] getAllImageFile() {
		File uploadDirectory = new File(uploadPath);
		resetCalcDate();
		return uploadDirectory.listFiles(dateFilter);
	}

	private static Pattern IMAGE_PATH_PATTERN = Pattern.compile(
			"http://img.fundin.cn/fundin/image/(\\d*.jpg)");
	
	private Set<String> getImageInUse() {
		List<String> imageList = userDao.getAllHeadImg();
		imageList.addAll(projDao.getAllCoverImg());
		imageList.addAll(projDao.getAllContent());
		imageList.addAll(BannerHelper.getBannerImageList());
		
		Set<String> imageInUse = Sets.newHashSet();
		for (String imagePath : imageList) {
			if (StringUtils.isNotBlank(imagePath)) {
				Matcher matcher = IMAGE_PATH_PATTERN.matcher(imagePath);
				while (matcher.find()) {
					imageInUse.add(matcher.group(1));
				}
			}
		}
		return imageInUse;
	}
	
}