package com.fundin.service.component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fundin.dao.mybatis.FundinSchoolDao;
import com.fundin.domain.entity.FundinSchool;
import com.fundin.utils.common.PropertyUtils;
import com.fundin.utils.common.PropertyUtils.LineHandler;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

@Component
public class SchoolHelper {

	@Resource
	private FundinSchoolDao schoolDao;
	
	private static Map<Long, FundinSchool> schoolMap = Maps.newHashMap();
	private static Multimap<String, FundinSchool> schoolInfos = ArrayListMultimap.create();
	private static Multimap<String, String> univInfos = ArrayListMultimap.create();
	
	@PostConstruct
	public void init() {
		List<FundinSchool> schoolList = schoolDao.queryAll();
		
		for (FundinSchool school : schoolList) {
			schoolMap.put(school.getId(), school);
			schoolInfos.put(school.getUniv(), school);
			if (!univInfos.containsEntry(school.getProvince(), school.getUniv())) {
				univInfos.put(school.getProvince(), school.getUniv());
			}
		}
	}
	
	public Map<String, Collection<String>> getUnivArray() {
		return univInfos.asMap();
	}
	
	public FundinSchool getById(Long schoolId) {
		return schoolMap.get(schoolId);
	}
	
	public List<FundinSchool> getSchool4Univ(String univ) {
		return (List<FundinSchool>) schoolInfos.get(univ);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void initUS() {
		PropertyUtils.readProperty("all.csv", new LineHandler() {
			@Override
			public void handleLine(String line) {
				String[] arr = StringUtils.split(line, ",");
				FundinSchool s = new FundinSchool();
				s.setProvince(arr[0]);
				s.setUniv(arr[1]);
				s.setSchool(arr[2]);
				schoolDao.insert(s);
			}
		});
	}
	
}
