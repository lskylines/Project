package cn.itcast.dao;

import cn.itcast.model.LoginTikcet;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author
 * @create 2019-06-28 20:05
 */
@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME = "login_ticket";
    String INSERT_FIELDS = " user_id, ticket, expired, status ";
    String SELECT_FIELDS  = " id, "+INSERT_FIELDS;

    @Insert({"INSERT INTO ", TABLE_NAME,"(", INSERT_FIELDS,  ") VALUES(#{userId}, #{ticket},#{expired}, #{status})"})
    public int addTicket(LoginTikcet ticket);

    @Select({"SELECT * FROM ", TABLE_NAME, " WHERE ticket=#{ticket}"})
    public LoginTikcet selectByTicket(String ticket);

    @Update({"UPDATE ", TABLE_NAME, " SET status=1 WHERE ticket=#{ticket}"})
    public int logout(String ticket);
}
