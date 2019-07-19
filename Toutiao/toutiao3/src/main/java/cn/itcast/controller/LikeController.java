package cn.itcast.controller;

import cn.itcast.Utils.ToutiaoUtil;
import cn.itcast.model.EntityType;
import cn.itcast.model.HostHolder;
import cn.itcast.model.News;
import cn.itcast.service.LikeService;
import cn.itcast.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author
 * @create 2019-07-07 10:48
 */
@Controller
public class LikeController {
    public static final Logger logger = LoggerFactory.getLogger(LikeController.class);
    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private NewsService newsService;


    @RequestMapping(path="/like", method = {RequestMethod.POST, RequestMethod.GET,})
    @ResponseBody
    @Transactional
    public String like(@RequestParam("newsId") int newsId){
        int userId = hostHolder.get().getId();
        long likeCount = likeService.like(userId,newsId, EntityType.ENTITY_NEWS);
        newsService.UpdateLikeCount(newsId, (int)likeCount);
        return ToutiaoUtil.getJson(0, String.valueOf(likeCount));
    }

    @RequestMapping(path="/dislike", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @Transactional
    public String disLike(@RequestParam("newsId")int newsId){
        int userId = hostHolder.get().getId();
        long dislikeCount = likeService.disLike(userId, newsId, EntityType.ENTITY_NEWS);
        newsService.UpdateLikeCount(newsId,(int)dislikeCount);
        logger.info("disLikeCount" + dislikeCount);
        return ToutiaoUtil.getJson(0, String.valueOf(dislikeCount));
    }

}
