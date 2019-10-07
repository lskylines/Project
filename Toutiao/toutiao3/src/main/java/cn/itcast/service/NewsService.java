package cn.itcast.service;

import cn.itcast.dao.NewsDAO;
import cn.itcast.model.News;
import cn.itcast.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author
 * @create 2019-06-28 20:11
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;
    @Autowired
    private SensitiveService sensitiveService;

    public List<News> selectByUserIdAndOffset(int userId, int offset, int limit){
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public int addNews(News news){
        //html文本过滤
        news.setTitle(HtmlUtils.htmlEscape(news.getTitle()));
        news.setContent(HtmlUtils.htmlEscape(news.getContent()));
        //敏感词过滤     亮点
        news.setTitle(sensitiveService.filter(news.getTitle()));
        news.setContent(sensitiveService.filter(news.getContent()));
        return newsDAO.addNews(news) >0 ? news.getId() : 0;
    }

    //Undo
    public List<News> getAllNews(){
        return newsDAO.getAllNews();
    }


    public News selectById(int newsId){
        return newsDAO.selectById(newsId);
    }

    public int updateCommentCount(int commentCount, int newsId){
        return newsDAO.UpdateCommentCount(commentCount, newsId);
    }

    public List<News> findByUserId(int userId){
        return newsDAO.findByUserId(userId);
    }

    //更新点赞数
    public int UpdateLikeCount(int id, int likeCount){
        return newsDAO.UpdateLikeCount(id, likeCount);
    }
}
