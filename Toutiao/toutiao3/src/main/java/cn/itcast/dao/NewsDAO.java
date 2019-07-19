package cn.itcast.dao;

import cn.itcast.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author
 * @create 2019-06-23 0:06
 */
@Mapper
public interface NewsDAO {
    String TABLE_NAME = " news ";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date, user_id";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;
    @Select({"SELECT", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE id=#{id}"})
    News selectById(@Param("id") int id);

    @Insert({"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS, ") VALUES(#{title}, #{link},#{image}, #{likeCount}, #{commentCount}, #{createdDate}, #{userId})"})
    int addNews(News news);

    @Update({"UPDATE ", TABLE_NAME, " SET comment_count=#{commentCount} WHERE id=#{id}"})
    void updateCommentCount(News news);



    @Select({"SELECT", SELECT_FIELDS, " FROM ", TABLE_NAME, " ORDER BY created_date DESC"})
    List<News> getAllNews();

    //评论添加成功后更新评论数量
    @Update({"UPDATE ", TABLE_NAME, " SET comment_count=#{commentCount} WHERE id=#{id}"})
    int UpdateCommentCount(@Param("commentCount") int commentCount, @Param("id") int id);

    //根据UID查看
    @Select({"SELECT * FROM", TABLE_NAME, " WHERE user_id=#{userId} ORDER BY  created_date DESC"})
    public List<News> findByUserId(int userId);

    //更新点赞数
    @Update({"UPDATE ", TABLE_NAME, " SET like_count=#{likeCount} WHERE id=#{id}"})
    public int UpdateLikeCount(@Param("id")int id, @Param("likeCount")int likeCount);

    List<News> selectByUserIdAndOffset(@Param("userId") int userId,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

}
