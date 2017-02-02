//package com.fundin.dao.mybatis;
//
//import java.util.List;
//
//import org.apache.ibatis.annotations.Delete;
//import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.Select;
//import org.apache.ibatis.annotations.Update;
//
//import com.fundin.domain.entity.FundinRepay;
//
//public interface FundinRepayDao {
//	
//	@Select("insert into fundin_repay (proj_id, content, image) VALUES(#{projId},#{content},#{image})")
//	public void addRePay(FundinRepay repay);
//	
//	@Select("select id as id, proj_id as projId, content as content, "
//			+ "image as image, support_num as supportNum from fundin_repay "
//			+ "where proj_id = #{projId}")
//	public List<FundinRepay> getRepayByProjId(@Param("projId") Long projId);
//	
//	@Update("update fundin_repay set support_num = support_num + 1 where id = #{repayId}")
//	public int updateRepaySupport(@Param("repayId") Long repayId);
//	
//	@Update("update fundin_repay set content = #{content}, "
//			+ "image = #{image} where id = #{id}")
//	public int updateInfo(FundinRepay repay);
//	
//	@Select("select id as id, proj_id as projId, content as content, "
//			+ "image as image, support_num as supportNum from fundin_repay "
//			+ "where id = #{repayId}")
//	public FundinRepay getRepay(@Param("repayId") Long repayId);
//	
//	@Delete("delete from fundin_repay where id = #{repayId}")
//	public void deleteRepay(@Param("repayId") Long repayId);
//	
//	@Select("select image from fundin_repay")
//	public List<String> getAllImage();
//	
//}
