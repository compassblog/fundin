package com.fundin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.fundin.dao.mybatis.FundinUserDao;
import com.fundin.domain.entity.FundinSchool;
import com.fundin.domain.entity.FundinUser;
import com.fundin.service.component.SchoolHelper;

@Component
public class RecommendService {

	@Resource
	private UserService userService;
	
	@Resource
	private ActionService actionService;
	
	@Resource
	private FundinUserDao fundinUserDao;
	
	@Resource
	private SchoolHelper schoolHelper;
	
	static int MAX_RECOMMEND_NUMS = 15;
	
	public List<FundinUser> getRecommendUserList(FundinUser user){
		List<FundinUser> recommendList = new ArrayList<FundinUser>();
		if (null == user.getSchoolId()) {
			return recommendList;
		}
		
		List<FundinUser> sameSchoolList = fundinUserDao.querySameSchool(user.getSchoolId());
		if(sameSchoolList != null){
			//delete user self and user's followings
			for(int i = sameSchoolList.size()-1; i>=0 ; i--){
				FundinUser u = sameSchoolList.get(i);
				if(u.getId() != user.getId() && !actionService.hasAttention(u.getId(),user.getId())){
					recommendList.add(u);
				}
			}
		}
		
		if(recommendList.size() >= MAX_RECOMMEND_NUMS){
			recommendList = recommendList.subList(0, MAX_RECOMMEND_NUMS);
			return recommendList;
		}
		
		//get user's univ's schools
		List<FundinSchool> schoolsList = schoolHelper.getSchool4Univ(schoolHelper.getById(user.getSchoolId()).getUniv());
		for(FundinSchool school : schoolsList){
			if(school.getId().equals(user.getSchoolId()))
				continue;
			List<FundinUser> sameSchoolUserList = fundinUserDao.querySameSchool(school.getId());
			for(FundinUser u : sameSchoolUserList){
				if(u.getId() != user.getId() && !actionService.hasAttention(u.getId(),user.getId())){
					recommendList.add(u);
					if(recommendList.size() == MAX_RECOMMEND_NUMS){
						return recommendList;
					}
				}
			}
		}
		
		return recommendList;
	}
}

