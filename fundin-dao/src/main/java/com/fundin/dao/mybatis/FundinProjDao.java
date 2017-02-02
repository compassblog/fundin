package com.fundin.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.fundin.dao.mybatis.provider.FundinProjProvider;
import com.fundin.domain.entity.FundinProj;
import com.fundin.domain.query.Pagination;
import com.fundin.domain.query.QueryInfo;

public interface FundinProjDao {
	
	@Insert("insert into fundin_proj (user_id, subject, title, intro, cover_img, video, "
			+ "content, total_amount, days, repay_days, repay_way, start_date, end_date, status,"
			+ "repay_content, repay_image) values (#{userId}, #{subject}, #{title}, #{intro},"
			+ " #{coverImg}, #{video}, #{content}, #{totalAmount}, #{days}, #{repayDays}, #{repayWay}, "
			+ "#{startDate}, #{endDate}, #{status}, #{repayContent}, #{repayImage})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int startNew(FundinProj proj);
	
	@Select("select id as id, user_id as userId, subject as subject, title as title, intro as intro, "
			+ "cover_img as coverImg, video as video, content as content, "
			+ "total_amount as totalAmount, raised_amount as raisedAmount, raised_ratio as raisedRatio, "
			+ "raised_num as raisedNum, days as days, repay_days as repayDays, repay_way as repayWay, "
			+ "start_date as startDate, end_date as endDate, status as status, repay_content as repayContent, "
			+ "repay_image as repayImage from fundin_proj where id = #{projId}")
	public FundinProj getProj(@Param("projId") Long projId);
	
	@Select("select id as id, user_id as userId, subject as subject, title as title, intro as intro, "
			+ "cover_img as coverImg, video as video, content as content, "
			+ "total_amount as totalAmount, raised_amount as raisedAmount, raised_ratio as raisedRatio, "
			+ "raised_num as raisedNum, days as days, repay_days as repayDays, repay_way as repayWay, "
			+ "start_date as startDate, end_date as endDate, status as status, repay_content as repayContent,"
			+ "repay_image as repayImage from fundin_proj where user_id = #{userId} and status = 0 "
			+ "and subject <= 4 order by start_date limit 0,1")
	public FundinProj getStartingProj(@Param("userId") Long userId);
	
	@SelectProvider(type = FundinProjProvider.class, method = "getProjList")
	public List<FundinProj> getProjList(@Param("subject") Integer subject, @Param("require") String require,
			@Param("searchWord") String searchWord,@Param("location") String location, @Param("query") QueryInfo query);
	
	@Select("select count(*) from fundin_proj where status > 0 and user_id = ${userId}")
	public int getMyProCount(@Param("userId") Long userId);
	
	@Select("SELECT COUNT(0) FROM fundin_proj WHERE user_id=#{userId} AND status>1")
	public int getMyPassProCount(@Param("userId") Long userId);
	
	@Select("select id as id, user_id as userId, subject as subject, title as title, "
			+ "intro as intro, cover_img as coverImg, total_amount as totalAmount, "
			+ "raised_amount as raisedAmount, days as days, status as status, "
			+ "start_date as startDate, end_date as endDate from fundin_proj "
			+ "where status > 0 and user_id = ${queryParams.userId} "
			+ "limit #{startIndex}, #{pageSize}")
	public List<FundinProj> getMyProList(Pagination<FundinProj> pageQuery);

	@Update("update fundin_proj set subject = #{subject}, title = #{title}, intro = #{intro}, "
			+ "cover_img = #{coverImg}, video = #{video}, content = #{content}, "
			+ "total_amount = #{totalAmount}, days = #{days}, repay_days = #{repayDays}, "
			+ "repay_way = #{repayWay}, start_date = #{startDate}, end_date = #{endDate}, "
			+ "update_date = #{updateDate}, status = #{status}, repay_content = #{repayContent},"
			+ "repay_image = #{repayImage} where id = #{id}")
	public int updateInfo(FundinProj proj);
	
	@Update("update fundin_proj set intro = #{intro}, cover_img = #{coverImg}, "
			+ "video = #{video}, content = #{content}, update_date = #{updateDate}, "
			+ "repay_way = #{repayWay}, repay_content = #{repayContent},"
			+ " repay_image = #{repayImage} where id = #{id}")
	public int saveEdit(FundinProj proj);
	
	@Update("update fundin_proj set raised_amount = #{raisedAmount}, raised_ratio = #{raisedRatio}, "
			+ "raised_num = #{raisedNum} where id = #{id}")
	public int updateRaised(FundinProj proj);
	
	@Update("update fundin_proj set status = #{status} where id = #{projId}")
	public int updateStatus(@Param("projId") Long projId, @Param("status") Integer status);

	@Select("select id as id, user_id as userId, subject as subject, title as title, intro as intro, "
			+ "cover_img as coverImg, video as video, content as content, "
			+ "total_amount as totalAmount, raised_amount as raisedAmount, raised_ratio as raisedRatio, "
			+ "raised_num as raisedNum, days as days, repay_days as repayDays, repay_way as repayWay, "
			+ "start_date as startDate, end_date as endDate, status as status "
			+ "from fundin_proj where status = 2 and end_date < now()")
	public List<FundinProj> getEndProj();
	
	@Select("select cover_img from fundin_proj")
	public List<String> getAllCoverImg();
	
	@Select("select content from fundin_proj")
	public List<String> getAllContent();
	

	@Select("select fundin_proj.id as id, fundin_proj.user_id as userId, fundin_proj.subject as subject, fundin_proj.title as title, fundin_proj.intro as intro, "
			+ "cover_img as coverImg, video as video, content as content, fundin_proj.total_amount as totalAmount, "
			+ "fundin_proj.raised_amount as raisedAmount, fundin_proj.raised_ratio as raisedRatio, fundin_proj.raised_num as raisedNum, "
			+ "fundin_proj.days as days, fundin_proj.repay_days as repayDays, fundin_proj.repay_way as repayWay, start_date as startDate, "
			+ "end_date as endDate, fundin_proj.status as status "
			+ "from fundin_proj where status >= 2 and subject = #{queryParams.subject} order by id desc")
	public List<FundinProj> getActivityProj(Pagination<FundinProj> pageQuery);
	
	@Select("select count(*) from fundin_proj where status >= 2 and subject = ${subject}")
	public int getActivityProjCount(@Param("subject") Integer subject);

	@Select("select id as id, user_id as userId, subject as subject, title as title, intro as intro, "
			+ "cover_img as coverImg, video as video, content as content, "
			+ "total_amount as totalAmount, raised_amount as raisedAmount, raised_ratio as raisedRatio, "
			+ "raised_num as raisedNum, days as days, repay_days as repayDays, repay_way as repayWay, "
			+ "start_date as startDate, end_date as endDate, status as status "
			+ "from fundin_proj where user_id = #{userId} and status = 0 and subject = #{subject}")
	public List<FundinProj> getPostStartPro(@Param("userId") Long userId, @Param("subject") Integer subject);
	
	@Select("select count(0) from fundin_proj where id=#{projId} and user_id=#{userId}")
	public int findMyPro(@Param("userId") Long userId, @Param("projId") Long projId);
}
