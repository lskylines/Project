package cn.itcast.dao;

import cn.itcast.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @author
 * @create 2019-06-30 13:39
 */
@Mapper
public interface CommentDAO {
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = " content, user_id, entity_id, entity_type, created_date, status";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS, ") VALUES(#{content}, #{userId}, #{entityId}, #{entityType}, #{createdDate}, #{status})"})
    int addComment(Comment comment);

    @Select({"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE entity_id=#{entityId} AND entity_type=#{entityType} AND status=0 ORDER BY created_date DESC"})
    List<Comment> selectByEntityIdAndEntityType(@Param("entityId") int entityId,@Param("entityType") int entityType);

    @Select({"SELECT COUNT(*) FROM" ,TABLE_NAME, " WHERE entity_id=#{entityId} AND entity_type=#{entityType} AND status=0"})
    int selectCommentCount(@Param("entityId") int entityId,
                           @Param("entityType") int entityType);
}
