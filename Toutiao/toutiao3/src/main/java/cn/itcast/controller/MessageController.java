package cn.itcast.controller;

import cn.itcast.Utils.ToutiaoUtil;
import cn.itcast.model.*;
import cn.itcast.service.MessageService;
import cn.itcast.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @create 2019-07-06 12:34
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    private MessageService messageService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;


    //获取站内信列表页
    @RequestMapping(path="/msg/list", method = RequestMethod.GET)
    public String conversationList(Model model){
        try{
            int localUserId = hostHolder.get().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            for(Message message: conversationList){
                ViewObject vo = new ViewObject();
                vo.set("conversation", message);
                int targetId = localUserId==message.getFromId() ? message.getToId(): message.getFromId();
                User user = userService.selectById(targetId);
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getName());
                vo.set("targetId", targetId);
                vo.set("totalCount", message.getId());
                vo.set("unread", messageService.getConversationTotalCount(localUserId, message.getConversationId()));
                vo.set("sessionCount", messageService.getSessionCount(message.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
        }catch(Exception e){
            e.printStackTrace();
            logger.error("获取站内信列表异常" + e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path="/msg/detail", method = RequestMethod.GET)
    public String conversationDetail(Model model, @RequestParam("conversationId")String conversationId){
        try{
            List<ViewObject> messages = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, ToutiaoUtil.MessagePageNum);
            for(Message message : conversationList){
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                User user = userService.selectById(message.getFromId());
                if(user==null){
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userId", user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
        }catch(Exception e) {
            logger.info("获取站内消息失败" + e.getMessage());
        }
        return "letterDetail";
    }

    //站内信列表删除
    @RequestMapping(path="/msg/Deletelist/{conversationId}", method = {RequestMethod.GET})
    public String Deletelist(@PathVariable("conversationId")String conversationId){
        try{
            messageService.DeleteByConversationId(conversationId);
            logger.info("删除成功" + conversationId);
        }catch(Exception e){
            logger.error("站内信列表删除异常" + e.getMessage());
        }
        return "redirect:/msg/list";
    }



    @RequestMapping(path="/addMessage", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String addMessage(Model model,@RequestParam("fromId")int fromId,
                             @RequestParam("toId")int toId,
                             @RequestParam("content")String content){
        Message message = new Message();
        message.setContent(content);
        message.setFromId(fromId);
        message.setToId(toId);
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setConversationId(fromId<toId? String.format("%d_%d", fromId, toId): String.format("%d_%d", toId, fromId));
        messageService.addMessage(message);
        return ToutiaoUtil.getJson(message.getId());
    }

}
