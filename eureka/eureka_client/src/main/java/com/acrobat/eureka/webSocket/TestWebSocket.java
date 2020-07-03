package com.acrobat.eureka.webSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * webSocket服务实例
 * @author xutao
 * @date 2018-11-23 09:58
 */
/* webSocket实例交给springBoot管理，虽然默认是单例的，springBoot还是会为每个webSocket连接创建一个实例
 * 所以可以用一个静态Set将所有实例保存起来 */
@Component
@ServerEndpoint("/webSocket/test")
public class TestWebSocket {

    private static final Logger logger = LoggerFactory.getLogger(TestWebSocket.class);

    // 记录当前在线连接数
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    // 静态线程安全Set，存放当前实例
    private static CopyOnWriteArraySet<TestWebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    // 连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);

        int nowCount = onlineCount.addAndGet(1);
        logger.debug("有新连接加入！当前在线人数为{}", nowCount);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);

        int nowCount = onlineCount.addAndGet(-1);
        logger.debug("有一连接关闭！当前在线人数为", nowCount);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.debug("收到来自客户端的webSocket消息:{}", message);

        try {
            // 返回消息
            sendMessage("收到客户端消息: " + message);

            // 群发消息到所有webSocket客户端
            for (TestWebSocket item : webSocketSet) {
                item.sendMessage(message);
            }
        } catch (IOException e) {
            logger.warn("发送webSocket消息失败", e);
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("webSocket发生错误", error);
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     */
    public void sendMessage(String message) throws IOException {

        this.session.getBasicRemote().sendText(message);
    }


}
