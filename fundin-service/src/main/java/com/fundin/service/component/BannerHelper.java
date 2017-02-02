package com.fundin.service.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fundin.utils.common.PropertyUtils;
import com.fundin.utils.common.PropertyUtils.LineHandler;
import com.google.common.collect.Lists;

public class BannerHelper {

	public static class BannerInfo {
		private String title;
		private String url;
		private String image;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
	}
	
	public static List<BannerInfo> bannerList = new ArrayList<BannerInfo>();
	
	static {
		PropertyUtils.readProperty("/conf/banner.properties", new LineHandler() {
			@Override
			public void handleLine(String line) {
				String[] arr = StringUtils.split(line, "\t");
				if (arr.length == 3) {
					BannerInfo banner = new BannerInfo();
					banner.setTitle(arr[0]);
					banner.setImage(arr[1]);
					banner.setUrl(arr[2]);
					bannerList.add(banner);
				}
			}
		});
	}
	
	public static List<BannerInfo> getBannerList() {
		return bannerList;
	}
	
	public static List<String> getBannerImageList() {
		List<String> imageList = Lists.newArrayList();
		for (BannerInfo banner : bannerList) {
			imageList.add(banner.getImage());
		}
		return imageList;
	}
	
}
