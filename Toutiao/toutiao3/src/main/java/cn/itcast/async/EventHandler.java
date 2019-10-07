package cn.itcast.async;

import java.util.List;

public interface EventHandler {
    void doHandler(EventModel model);
    List<EventType> getSupportEventTypes();
}
