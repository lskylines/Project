package cn.itcast.controller;

import cn.itcast.model.Comment;
import cn.itcast.model.EntityType;
import cn.itcast.model.HostHolder;
import cn.itcast.service.CommentService;
import cn.itcast.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author
 * @create 2019-06-30 14:46
 */
@Controller
@RequestMapping(path="/comment")
public class CommentController {
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private CommentService commentService;
    @Autowired
    NewsService newsService;


    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @RequestMapping(path="/addComment", method = {RequestMethod.POST})
    @Transactional
    public String addComment(Model model, @RequestParam("newsId") int newsId,
                             @RequestParam("content") String content){
        try{
            Comment comment = new Comment();
            comment.setEntityId(newsId);
            comment.setUserId(hostHolder.get().getId());
            comment.setStatus(0);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreatedDate(new Date());
            comment.setContent(content);
            commentService.addComment(comment);
            //评论添加成功后，news表CommentCount数量更新
            int commentCount = commentService.selectCommentCount(newsId, EntityType.ENTITY_NEWS);
            newsService.updateCommentCount(commentCount, newsId);
        }catch(Exception e){
            logger.error("添加评论异常" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);

    }
}
