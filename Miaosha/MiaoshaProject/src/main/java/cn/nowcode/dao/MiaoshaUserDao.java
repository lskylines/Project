package cn.nowcode.dao;

import cn.nowcode.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author
 * @create 2019-07-13 22:45
 */
@Mapper
public interface MiaoshaUserDao {
    String TABLE_NAME = "miaosha_user";
    String INSERT_FIELDS = " nickname,password,salt,head,register_date,last_login_date,login_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;
    @Select({"SELECT", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE id=#{id}"})
    public MiaoshaUser getById(@Param("id") long id);

}
