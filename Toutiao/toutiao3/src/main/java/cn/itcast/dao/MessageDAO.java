package cn.itcast.dao;

import cn.itcast.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author
 * @create 2019-07-06 12:25
 */
@Mapper
public interface MessageDAO {
    //站内信DAO层
    String TABLE_NAME = "message";
    String INSERT_FIELDS = "from_id, to_id, content, created_date, has_read,conversation_id";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS, ") ", "VALUES(#{fromId},#{toId}, #{content}, #{createdDate}, #{hasRead}, #{conversationId})"})
    int addMessage(Message message);


    //查询站内信未读数量
    @Select({"SELECT COUNT(id) FROM ", TABLE_NAME, " WHERE has_read=0 AND to_id=#{userId} AND conversation_id=#{conversationId}"})
    public int selectCount(@Param("userId")int userId, @Param("conversationId")String conversationId);


    //站内信详情页
    @Select({"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE conversation_id=#{conversationId} ORDER BY id desc LIMIT #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId")String conversationId,
                                               @Param("offset")int offset,
                                               @Param("limit")int limit);


    //查询站内列表页会话数量
    @Select({"SELECT COUNT(id) FROM", TABLE_NAME, " WHERE conversation_id=#{conversationId}"})
    public int getSessionCount(@Param("conversationId")String conversationId);


    //删除站内信列表
    @Delete({"DELETE FROM ", TABLE_NAME, " WHERE conversation_id=#{conversationId}"})
    public void DeleteByConversationId(@Param("conversationId")String conversationId);

    //SELECT *,COUNT(id) as cnt from (SELECT * FROM message where from_id=1 or to_id=3 order by id desc) tt group by conversation_id order by id desc;
    @Select({"SELECT", SELECT_FIELDS," ,COUNT(id) as id FROM (SELECT * FROM message WHERE  from_id=#{userId} OR to_id=#{userId} ORDER BY id desc) tt GROUP BY conversation_id ORDER BY id DESC LIMIT #{offset},#{limit}"})
    List<Message> getConversationList(@Param("userId")int userId,
                                      @Param("offset")int offset,
                                      @Param("limit")int limit);
}
