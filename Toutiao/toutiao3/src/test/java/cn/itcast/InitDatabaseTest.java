package cn.itcast;

import cn.itcast.dao.CommentDAO;
import cn.itcast.dao.MessageDAO;
import cn.itcast.dao.NewsDAO;
import cn.itcast.dao.UserDAO;
import cn.itcast.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;
import java.util.UUID;


/**
 * @author
 * @create 2019-06-09 16:12
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication3.class)
public class InitDatabaseTest {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private NewsDAO newsDAO;

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private MessageDAO messageDAO;

    @Test
    public void initData(){

        Random random = new Random();
        //for (int i = 1; i < 10; i++) {
            /*User user = new User();
            user.setName(String.format("User%d", i+1));
            user.setPassword("Password");
            user.setSalt(UUID.randomUUID().toString().substring(0, 5));
            user.setHeadUrl(String.format("/headers/%dt.png", random.nextInt(1000)));
            userDAO.addUser(user);*/
            //Assert.assertNotNull(userDAO.selectById(i));
            //Assert.assertNotNull(userDAO.selectByName("User"+String.valueOf(i+1)));
            /*News news = new News();
            news.setCommentCount(i+1);
            news.setLikeCount(i+2);
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i+20));
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600* 5 * i);
            news.setCreatedDate(date);
            news.setUserId(29);
            news.setTitle(String.format("Title%d", i+1));
            news.setImage(String.format("newsImage/%d.png", random.nextInt(20)));
            newsDAO.addNews(news);*/


            /*Comment comment = new Comment();
            comment.setContent(String.format("今日首款头条%d", random.nextInt(1000)));
            Date date = new Date();
            date.setTime(date.getTime() + 3600 * 5 * i);
            comment.setCreatedDate(date);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setStatus(0);
            comment.setUserId(29);
            comment.setEntityId(i + 10);
            commentDAO.addComment(comment);*/
        //}
       /* userDAO.deleteById(2);
        Assert.assertNull(userDAO.selectById(2));*/
        Message message = new Message();
        message.setFromId(1);
        message.setToId(2);
        message.setConversationId("1_2");
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setContent("hello world");
       messageDAO.addMessage(message);
    }



}
