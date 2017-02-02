package com.fundin.dao.mybatis;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.fundin.dao.mybatis.provider.FundinUserProvider;
import com.fundin.domain.dto.InviteSort;
import com.fundin.domain.entity.FundinSchool;
import com.fundin.domain.entity.FundinUser;
import com.fundin.domain.export.ExportEntity.ExportUser;

public interface FundinUserDao {
	
	@Insert("insert into fundin_user (name, email, phone, salt, passwd, type, "
			+ "head_img, reg_time, invite_user_id) "
			+ "values (#{name}, #{email}, #{phone}, #{salt}, #{passwd}, #{type}, "
			+ "#{headImg}, #{regTime}, #{inviteUserId})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int register(FundinUser user);
	
	@Select("select id as id, name as name, email as email, salt as salt, "
			+ "passwd as passwd from fundin_user where email = #{email}")
	public FundinUser queryLogin(@Param("email") String email);
	
	@Select("select id as id, name as name, phone as phone, salt as salt, "
			+ "passwd as passwd from fundin_user where phone = #{phone}")
	public FundinUser queryLoginByPhone(@Param("phone") String phone);
	
	@Select("select id as id, name as name, openid as openid "
			+ "from fundin_user where openid = #{openid}")
	public FundinUser queryByOpenid(@Param("openid") String openid);
	
	@Select("select id as id, name as name, type as type, email as email, phone as phone, "
			+ "bank_account as bankAccount, head_img as headImg, sex as sex, birthday as birthday, "
			+ "sign as sign, school_id as schoolId, entry_year as entryYear, bank_name as bankName, "
			+ "bank_user_name as bankUserName, raised_amount as raisedAmount,"
			+ "withdrawal_amount as withdrawalAmount, deposit_amount as depositAmount,"
			+ "transaction_amount as transactionAmount from fundin_user where id = #{id}")
	public FundinUser queryInfo(@Param("id") Long id);
	
	@UpdateProvider(type = FundinUserProvider.class, method = "updateSomeInfo")
	public int updateSomeInfo(@Param("user") FundinUser user);
	
	@Update("update fundin_user set phone = #{phone}, sign = #{sign}, sex = #{sex}, "
			+ "school_id = #{schoolId}, birthday = #{birthday}, entry_year = #{entryYear}, "
			+ "update_time = #{updateTime} where id = #{id}")
	public int updatePersonalInfo(FundinUser user);
	
	@Update("update fundin_user set bank_name = #{bankName}, bank_account = #{bankAccount}, bank_user_name = #{bankUserName}, "
			+ "update_time = #{updateTime} where id = #{id}")
	public int updateBankAccountInfo(FundinUser user);
	
	@Update("update fundin_user set passwd = #{passwd}, salt = #{salt}, "
			+ "update_time = #{updateTime} where id = #{id}")
	public int updatePasswd(FundinUser user);
	
	@Update("update fundin_user set raised_amount = raised_amount + #{raisedAmount}, "
			+ "withdrawal_amount = withdrawal_amount + #{withdrawalAmount}, deposit_amount = deposit_amount + #{depositAmount},  "
			+ "transaction_amount = transaction_amount + #{transactionAmount}, update_time = now() where id = #{userId}")
	public int updateAmountInfo(@Param("userId") Long userId, @Param("raisedAmount") int raisedAmount, 
			@Param("withdrawalAmount") int withdrawalAmount, @Param("depositAmount") int depositAmount, 
			@Param("transactionAmount") int transactionAmount);
	
	@Select("select id as id, name as name, head_img as headImg from fundin_user where school_id = #{school_id}")
	public List<FundinUser> querySameSchool(Long school_id);
	
	@Select("select head_img from fundin_user")
	public List<String> getAllHeadImg();
	
	@Select("select email from fundin_user")
	public List<String> getAllEmail();
	
	@Select("select id from fundin_user where email = #{email}")
	public Long queryIdByEmail(String email);

	@Select("select id as id, name as name, head_img as headImg "
			+ "from fundin_user where id = #{id}")
	public FundinUser queryNameAndHeadImg(@Param("id") Long id);

	@Select("select B.id as id, B.province as province, B.univ as univ, B.school as school "
			+ "from fundin_user A join fundin_school B on A.school_id = B.id "
			+ "where A.id = #{userId}")
	public FundinSchool getSchoolByUserId(Long userId);
	
	@Select("select A.name as name, A.type as type, A.email as email, A.phone as phone, "
			+ "A.sex as sex, A.birthday as birthday, A.sign as sign, A.entry_year as entryYear, "
			+ "A.reg_time as regTime, B.province as province, B.univ as univ, B.school as school "
			+ "from fundin_user A left join fundin_school B on A.school_id = B.id")
	public List<ExportUser> exportUser();
	
	@Select("select reg_time from fundin_user")
	public List<Date> exportUserRegTime();

	@Select("select id from fundin_user where phone = #{phone}")
	public Long queryIdByPhone(String phone);
	
	@Update("update fundin_user set invite_count = invite_count+1 where id=#{userId}")
	public int updateInviteCount(@Param("userId") Long userId);
	
	@Select("select id,name,invite_count as inviteCount from fundin_user where invite_count = "
			+ "(select max(invite_count) from fundin_user) and invite_count>=#{inviteCount}")
	public List<InviteSort> getInviteNo1(Integer inviteCount);
	
	@Select("select id,name,invite_count as inviteCount from fundin_user order by invite_count desc limit 0,10")
	public List<InviteSort> getInviteSort();
	
	@Select("select id,name,type,head_img headImg,sex,birthday,school_id schoolId "
			+ "from fundin_user where invite_user_id=#{userId} limit #{pageNum},#{pageSize}")
	public List<FundinUser> getInviteUserList(@Param("userId") Long userId, @Param("pageNum") int pageNum,
			@Param("pageSize") int pageSize);
}
