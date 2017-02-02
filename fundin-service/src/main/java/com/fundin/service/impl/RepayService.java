//package com.fundin.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.springframework.stereotype.Component;
//
//import com.fundin.dao.mybatis.FundinPayRecordDao;
//import com.fundin.dao.mybatis.FundinUserDao;
//import com.fundin.domain.entity.FundinUser;
//import com.fundin.domain.query.Pagination;
//import com.fundin.service.common.RedisUtil;
//
//@Component
//public class RepayService {
//
//	@Resource
//	private FundinPayRecordDao payRecordDao; 
//	@Resource
//	private FundinUserDao userDao;
//	@Resource
//	private RedisUtil redisUtil;
//	@Resource
//	private UserService userService;
//	
//	public Pagination<FundinUser> getRepayUserList(Long projId, 
//			int pageNum, int pageSize) {
//		Pagination<FundinUser> pageQuery = Pagination.getInstance(
//				payRecordDao.queryRepayUserCount(projId), pageNum, pageSize);
//		pageQuery.addQueryParams("projId", projId);
//		
//		List<Long> userIds = payRecordDao.queryUserIdListByProjId(pageQuery);
//		List<FundinUser> users = new ArrayList<FundinUser>();
//		for (Long id : userIds) {
//			FundinUser user = userDao.queryNameAndHeadImg(id);
//			users.add(user);
//		}
//		pageQuery.setElements(users);
//		return pageQuery;
//	} 
//	
//}
