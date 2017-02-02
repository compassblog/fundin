package com.fundin.dao.mybatis.provider;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SqlBuilder;

import com.fundin.domain.query.QueryInfo;

@SuppressWarnings("deprecation")
public class FundinProjProvider extends SqlBuilder {
	
	public String getProjList(Map<String, Object> params) {
		Integer subject = (Integer) params.get("subject");
		String require = (String) params.get("require");
		String searchWord = (String) params.get("searchWord");
		String location = (String) params.get("location");
		QueryInfo query = (QueryInfo) params.get("query");
		
		BEGIN();
		SELECT(" fundin_proj.id as id, fundin_proj.user_id as userId, fundin_proj.subject as subject, fundin_proj.title as title, fundin_proj.intro as intro, "
			+ "cover_img as coverImg, video as video, content as content, fundin_proj.total_amount as totalAmount, "
			+ "fundin_proj.raised_amount as raisedAmount, fundin_proj.raised_ratio as raisedRatio, fundin_proj.raised_num as raisedNum, "
			+ "fundin_proj.days as days, fundin_proj.repay_days as repayDays, fundin_proj.repay_way as repayWay, start_date as startDate, "
			+ "end_date as endDate, fundin_proj.status as status ");
		FROM(" fundin_proj ");
		
		if (null != subject) {
			WHERE(" subject = #{subject} ");
		}
		
		if (StringUtils.isNotBlank(searchWord)) {
			WHERE(" title LIKE #{searchWord}");
		}
		
		if ("finished".equals(require)) {
			WHERE(" status >= 3 ");
		} 
		else {
			if ("endsoon".equals(require)){
				WHERE(" now() BETWEEN DATE_SUB(end_date, INTERVAL 3 DAY) and end_date ");
			}
			else{
				WHERE(" status >= 2 ");
			}
		}
		if( !("national".equals(location)) && location != null){
			JOIN(" fundin_user on fundin_proj.user_id = fundin_user.id ");
			JOIN(" fundin_school on fundin_user.school_id = fundin_school.id ");
			WHERE(" fundin_school.province = #{location}");
		}
		ORDER_BY("start_date desc");
		
		String sql = SQL();
		if (null != query) {
			sql = sql + " LIMIT " + query.getStartIndex() + ", " + query.getPageSize();
		}
		return sql;
	}
	
}
