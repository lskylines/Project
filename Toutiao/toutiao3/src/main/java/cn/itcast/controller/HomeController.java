package cn.itcast.controller;

import cn.itcast.Utils.ToutiaoUtil;
import cn.itcast.model.*;
import cn.itcast.service.LikeService;
import cn.itcast.service.NewsService;
import cn.itcast.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @create 2019-06-23 0:03
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;

   /* public List<ViewObject> getUserViews(int userId, int offset, int limit){
        List<ViewObject> vos = new ArrayList<>();
        List<News> news  = newsService.selectByUserIdAndOffset(userId, offset, limit);
        for(News n: news){
            ViewObject vo = new ViewObject();
            vo.set("news", n);
            vo.set("user", userService.selectById(n.getUserId()));
            vos.add(vo);
        }
        return vos;
    }*/



    public List<ViewObject> selectViews(){
        List<ViewObject> vos = new ArrayList<>();
        List<News> news  = newsService.getAllNews();
        for(News n: news){
            ViewObject vo = new ViewObject();
            vo.set("news", n);
            vo.set("user", userService.selectById(n.getUserId()));
            vos.add(vo);
        }
        return vos;
    }



    public List<User> getAllUsers(List<News> news){
        List<User> users  = new ArrayList<>();
        for(News n:news){
            User user = userService.selectById(n.getUserId());
            users.add(user);
        }
        return users;
    }


    public List<NewsUser> getViews(List<News> list){
        List<NewsUser> vos = new ArrayList<>();
        int localUserId = hostHolder.get() != null ? hostHolder.get().getId(): 0;
        for(News n: list){
            NewsUser nu = new NewsUser();
            nu.setId(n.getId());
            nu.setTitle(n.getTitle());
            nu.setContent(n.getContent());
            nu.setImage(n.getImage());
            nu.setLikeCount(n.getLikeCount());
            nu.setCommentCount(n.getCommentCount());
            nu.setCreatedDate(n.getCreatedDate());
            nu.setUserId(n.getUserId());
            User user = userService.selectById(nu.getUserId());
            nu.setName(user.getName());
            nu.setHeadUrl(user.getHeadUrl());

            if (localUserId != 0) {
                nu.setLike(likeService.getLikeStatus(localUserId, n.getId(), EntityType.ENTITY_NEWS));
            } else {
                nu.setLike(0);
            }

            vos.add(nu);
        }
        return vos;
    }


    @RequestMapping(path={"/{pageNo}", "/index/{pageNo}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String home(Model model, @PathVariable(value = "pageNo")int pageNo){
        PageHelper.startPage(pageNo, ToutiaoUtil.TotTiaoPageNum);
        List<News> list = newsService.getAllNews();
        PageInfo pageInfo = new PageInfo(list,  5);
        pageInfo.setList(getViews(pageInfo.getList()));
        model.addAttribute("pageInfo", pageInfo);
        return "home";
    }

    //用户首页
    @RequestMapping(path="/user/{userId}/{pageNum}", method = {RequestMethod.GET})
    public String userIndex(Model model, @PathVariable(value = "userId") int userId, @PathVariable(value = "pageNum") int pageNum){
        PageHelper.startPage(pageNum, ToutiaoUtil.CommentPageNum);
        List<News> list = newsService.findByUserId(userId);
        PageInfo pageInfo = new PageInfo(list, 5);
        pageInfo.setList(getViews(pageInfo.getList()));
        model.addAttribute("pageInfo", pageInfo);
        return "UserIndex";
    }


}
