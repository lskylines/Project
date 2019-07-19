package cn.itcast.service;

import cn.itcast.dao.MessageDAO;
import cn.itcast.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * @create 2019-07-06 12:33
 */
@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;
    public int addMessage(Message message){
        return messageDAO.addMessage(message);
    }


    //站内信列表页
    public List<Message> getConversationList(int userId, int offset, int limit){
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public int getConversationTotalCount(int userId, String conversationId){
        return messageDAO.selectCount(userId, conversationId);
    }


    //站内信详情页
    public List<Message> getConversationDetail(String conversationId, int offset,int limit){
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    //查询站内列表页会话数量
    public int getSessionCount(String conversationId){
        return messageDAO.getSessionCount(conversationId);
    }

    //站内信列表删除
    public void DeleteByConversationId(String conversationId){
        messageDAO.DeleteByConversationId(conversationId);
    }
}
