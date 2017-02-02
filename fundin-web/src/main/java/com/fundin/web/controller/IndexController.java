package com.fundin.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fundin.domain.dto.ProjView;
import com.fundin.domain.entity.FundinUser;
import com.fundin.service.common.Constants;
import com.fundin.service.component.BannerHelper;
import com.fundin.service.impl.ProjService;
import com.fundin.service.impl.RecommendService;
import com.fundin.service.impl.UserService;
import com.fundin.utils.common.DateUtil;
import com.fundin.utils.common.ImageUtils;
import com.fundin.utils.common.ThreadPoolUtils;
import com.fundin.web.common.LoginContext;
import com.fundin.web.common.Resp;
import com.fundin.web.common.UEditorResult;

@Controller
public class IndexController {

	private static final Logger LOG = LoggerFactory.getLogger(
			IndexController.class);
	
	@Resource
	private ProjService projService;
	@Resource
	private UserService userService;
	@Resource
	private RecommendService recommendService;
	
	private static int default_init_pagesize = 10;
	
	@RequestMapping(value = {"", "/", "/index"})
	public String index(ModelMap model, HttpServletRequest request) {
		model.put(Constants.Attrs.NAV_HEAD, "index");
		model.put(Constants.Attrs.PAGE_TITLE, "Fundin.cn - 国内第一校园众筹社交网站");
		model.put("page_size", default_init_pagesize);
		
		try {
			model.put("projViewList", projService.getProjViewList(
					null, "new", null, null, 1, default_init_pagesize));
			model.put("bannerList", BannerHelper.getBannerList());
			
			List<ProjView> hotList = projService.getProjViewList(
					null, "hot", null, null, 0, 0);
			if (hotList.size() > 5) {
				hotList = hotList.subList(0, 5);
			}
			model.put("hotList", hotList);
			
			if(LoginContext.getLoginContext().isLoggedin()){
				FundinUser user = userService.queryInfo(LoginContext.getLoginContext().getUserId());
				List<FundinUser> recommList = recommendService.getRecommendUserList(user);
				if(recommList != null){
					model.put("recommList", recommList);
				}
			}
		} catch (Exception ex) {
			LOG.error("index ex !", ex);
		}
		return "index";
	}
	
	@RequestMapping(value = "/search")
	public String search(
			@RequestParam(value = "subject", required = false) Integer subject, 
			@RequestParam(value = "require", required = false) String require, 
			@RequestParam(value = "searchWord", required = false) String searchWord,
			@RequestParam(value = "location", required = false) String location,
			ModelMap model, HttpServletRequest request) {
		model.put(Constants.Attrs.NAV_HEAD, "browse");
		model.put(Constants.Attrs.PAGE_TITLE, "浏览项目 - Fundin.cn");
		
		model.put(Constants.Attrs.NAV_SUBJECT, 0);
		model.put(Constants.Attrs.NAV_REQUIRE, "new");
		model.put(Constants.Attrs.NAV_LOCATION, "national");
		if (null != subject) {
			model.put(Constants.Attrs.NAV_SUBJECT, subject);
		}
		if (StringUtils.isNotBlank(require)) {
			model.put(Constants.Attrs.NAV_REQUIRE, require);
		}
		if (StringUtils.isNotBlank(location)) {
			model.put(Constants.Attrs.NAV_LOCATION, location);
		}
		if (StringUtils.isNotBlank(searchWord)) {
			model.put(Constants.Attrs.NAV_SEARCH, searchWord);
		}
		
		try {
			model.put("projViewList", projService.getProjViewList(
					subject, require, searchWord, location, 0, 0));
		} catch (Exception ex) {
			LOG.error("search ex !", ex);
		}
		
		return "search";
	}
	
	@RequestMapping(value = "/getProjViewListByPage", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getProjViewListByPage(
			@RequestParam(value = "pageNum", required = true) Integer pageNum, 
			@RequestParam(value = "pageSize", required = true) Integer pageSize) {
		try {
			return Resp.succ(projService.getProjViewList(null, "new", null,null, pageNum, pageSize));
		} catch (Exception ex) {
			LOG.error("getProjViewListByPage ex !", ex);
		}
		return Resp.fail(null);
	}
	
	@RequestMapping(value = "/help/help")
	public String help(ModelMap model) {
		model.put(Constants.Attrs.NAV_HEAD, "help");
		model.put(Constants.Attrs.PAGE_TITLE, "服务攻略 - Fundin.cn");
		return "help";
	}
	
	@RequestMapping(value = "/help/state")
	public String state(ModelMap model) {
		model.put(Constants.Attrs.PAGE_TITLE, "用户协议 - Fundin.cn");
		return "state";
	}
	
	@Value("${uploadPath}")
	private String uploadPath;
	
	private static String thumbnailPath;
	@Value("${thumbnailPath}")
	public void setThumbnailPath(String thumbnailPath) {
		IndexController.thumbnailPath = thumbnailPath;
	}

	private static String middlePath;
	@Value("${middlePath}")
	public void setMiddlePath(String middlePath) {
		IndexController.middlePath = middlePath;
	}
	
	public String getSuffix(MultipartFile file) {
		String originalName = file.getOriginalFilename();
		return originalName.substring(originalName.lastIndexOf('.'), originalName.length());
	}
	
	private String generateImgName() {
		return (int)(Math.random() * 1000000) 
				+ DateUtil.dateFormat(new Date(), DateUtil.DATEFORMAT_TRIM) 
				+ ".jpg";
	}
	
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST, 
			produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object uploadImage(@RequestParam("imgFile") MultipartFile imgFile) {
		try {
			String newImgName = generateImgName();
			File targetFile = compressImage(imgFile, newImgName);
			addThumbnailTask(targetFile, newImgName);
			return Resp.succ(Constants.FUNDIN_IMG_PATH + newImgName);
		} catch (Exception ex) {
			LOG.error("uploadImage ex !", ex);
		}
		return Resp.fail(null);
	}

	@RequestMapping(value = "/uploadImage4UEditor", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String uploadImage4UEditor(@RequestParam("upfile") MultipartFile imgFile) {
		try {
			String newImgName = generateImgName();
			File targetFile = compressImage(imgFile, newImgName);
			addThumbnailTask(targetFile, newImgName);
			return UEditorResult.success(Constants.FUNDIN_IMG_PATH + newImgName);
		} catch (Exception ex) {
			LOG.error("uploadImage4UEditor ex !", ex);
		}
		return UEditorResult.failed();
	}
	
	private static long big_img_size = 1024 * 1024L;
	private float obtainCprQuality(MultipartFile imgFile) {
		long imgSize = imgFile.getSize();
		if (imgSize >= big_img_size) {
			return 0.2f;
		} else {
			return 0.5f;
		}
	}
	
	private File compressImage(MultipartFile imgFile, String newImgName) throws IOException {
		File targetFile = new File(uploadPath, newImgName);
		ImageUtils.compressQuality(imgFile.getInputStream(), targetFile, 
				obtainCprQuality(imgFile));
		return targetFile;
	}
	
	private void addThumbnailTask(File sourceImage, String newImgName) {
		ThreadPoolUtils.getExecutor().execute(
				new GenThumbnailTask(sourceImage, newImgName));
	}
	
	private static class GenThumbnailTask implements Runnable {

		private File sourceImage;
		private String imageName;
		
		public GenThumbnailTask(File sourceImage, String imageName) {
			this.sourceImage = sourceImage;
			this.imageName = imageName;
		}
		
		@Override
		public void run() {
			try {
				ImageUtils.resizeImage(sourceImage, 
						new File(thumbnailPath, imageName), 60, 60, false);
				ImageUtils.resizeImage(sourceImage, 
						new File(middlePath, imageName), 400, 4000, true);
			} catch (IOException e) {
				LOG.error("GenThumbnailTask error !", e);
			}
		}
		
	}
	
}
