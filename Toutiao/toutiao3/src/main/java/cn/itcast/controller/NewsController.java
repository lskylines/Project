package cn.itcast.controller;

import cn.itcast.Utils.ToutiaoUtil;
import cn.itcast.dao.NewsDAO;
import cn.itcast.model.*;
import cn.itcast.service.CommentService;
import cn.itcast.service.LikeService;
import cn.itcast.service.NewsService;
import cn.itcast.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @create 2019-06-29 10:23
 */
@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private NewsService newsService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CommentService commentService;

    //分享资讯
    @RequestMapping(path = "/news/addNews", method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String content){
        try{
            News news  = new News();
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setImage(image);
            news.setContent(content);
            if(hostHolder.get()!=null){
                //登陆，使用hostHolder获取当前用户id
                news.setUserId(hostHolder.get().getId());
            }else{
                //匿名用户
                news.setUserId(3);
            }
            newsService.addNews(news);
            return ToutiaoUtil.getJson(0, "资讯发布成功");
        }catch (Exception e){
            logger.info("资讯发布异常" + e.getMessage());
            return ToutiaoUtil.getJson(1, "发布失败");
        }
    }


    //资讯详情页
    @RequestMapping(path="/news/{newsId}", method = {RequestMethod.GET})
    public String detail(@PathVariable("newsId")int newsId, Model model){
        News news = newsService.selectById(newsId);
        if(news!=null){
            int localUserId = hostHolder.get()!=null ? hostHolder.get().getId(): 0;
            if(localUserId!=0){
                model.addAttribute("like", likeService.getLikeStatus(localUserId, news.getId(), EntityType.ENTITY_NEWS));
            }else{
                model.addAttribute("like" , 0);
            }

            List<ViewObject> vos = new ArrayList<>();
            List<Comment> commentList = commentService.selectByEntityIdAndEntityType(news.getId(), EntityType.ENTITY_NEWS);
            for(Comment comment: commentList){
                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("user", userService.selectById(comment.getUserId()));
                vos.add(vo);
            }
            model.addAttribute("comments", vos);
        }
        model.addAttribute("news", news);
        model.addAttribute("owner", userService.selectById(news.getUserId()));
        return "detail";
    }

}
