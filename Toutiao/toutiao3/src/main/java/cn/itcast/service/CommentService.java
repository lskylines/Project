package cn.itcast.service;

import cn.itcast.dao.CommentDAO;
import cn.itcast.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author
 * @create 2019-06-30 13:54
 */
@Service
public class CommentService {
    @Autowired
    private CommentDAO commentDAO;
    @Autowired
    private SensitiveService sensitiveService;

    //根据newsId，EnityType查询评论数据
    public List<Comment> selectByEntityIdAndEntityType(int entityId,int entityType){
        return commentDAO.selectByEntityIdAndEntityType(entityId, entityType);
    }

    public int addComment(Comment comment){
        //html文本过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        //敏感词过滤
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDAO.addComment(comment) > 0 ? comment.getId() : 0;
    }

    //查询某一资讯评论数量
    public int selectCommentCount(int entityId, int entityType){
        return commentDAO.selectCommentCount(entityId, entityType);
    }
}
