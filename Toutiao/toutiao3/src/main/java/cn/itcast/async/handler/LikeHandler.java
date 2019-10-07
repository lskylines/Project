package cn.itcast.async.handler;

import cn.itcast.Utils.ZixunUtils;
import cn.itcast.async.EventHandler;
import cn.itcast.async.EventModel;
import cn.itcast.async.EventType;
import cn.itcast.model.Message;
import cn.itcast.model.User;
import cn.itcast.service.MessageService;
import cn.itcast.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
public class LikeHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(LikeHandler.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandler(EventModel model) {
        logger.info("点赞后进入doHandler");
        Message message = new Message();
        message.setFromId(model.getActorId());
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());

        User user = userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName() + "赞了你的评论");
        message.setHasRead(0);
        message.setConversationId(user.getId() + "_" + model.getEntityOwnerId());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
