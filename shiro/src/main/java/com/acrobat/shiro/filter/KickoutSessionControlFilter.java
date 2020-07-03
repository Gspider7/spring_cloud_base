package com.acrobat.shiro.filter;

import com.acrobat.shiro.constant.SessionAttributeKeys;
import com.acrobat.shiro.dao.RedisSessionDao;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * 限制账号只能在一个地方登录，防止同一个账号创建多个session
 *
 * @author xutao
 * @date 2018-12-05 13:39
 */
public class KickoutSessionControlFilter extends AccessControlFilter {

    private String kickoutUrl = "/unauthorized/offline";                // 踢出后跳转的地址
    private boolean kickoutAfter = false;                               // 踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
    private int maxSession = 1;                                         // 同一个帐号最大会话数，默认1

    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    @Autowired
    private RedisSessionDao redisSessionDao;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public KickoutSessionControlFilter(SessionManager sessionManager, CacheManager cacheManager) {
        this.sessionManager = sessionManager;
        this.cache = cacheManager.getCache("user_session_ids");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        String kickoutKey = SessionAttributeKeys.KEY_KICKOUT;

        // 如果没有登录，直接进行之后的流程
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            return true;
        }
        Session session = subject.getSession();
        String username = (String) subject.getPrincipal();
        Serializable sessionId = session.getId();

        // 将用户当前登录的所有sessionId保存到队列
        Deque<Serializable> deque = cache.get(username);
        if (deque == null) {
            deque = new LinkedList<>();
            cache.put(username, deque);
        }
        if (!deque.contains(sessionId) && session.getAttribute(kickoutKey) == null) {
            deque.push(sessionId);
            cache.put(username, deque);
        }

        // 如果队列里的sessionId数超出最大会话数，开始踢人
        while (deque.size() > maxSession) {
            Serializable kickoutSessionId = kickoutAfter ? deque.removeFirst() : deque.removeLast();
            cache.put(username, deque);

            Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
            if (kickoutSession != null) {
                // 设置会话的kickout属性表示该session已被踢出
                kickoutSession.setAttribute(kickoutKey, true);
            }
        }

        // 如果被踢出了
        Boolean kickoutFlag = (Boolean) session.getAttribute(kickoutKey);
        if (kickoutFlag != null && kickoutFlag) {
            subject.logout();                       // 执行登出，内存中的session将被删除
//            session.removeAttribute(kickoutKey);
            saveRequest(request);

            // 执行返回
            HttpServletRequest httpRequest = WebUtils.toHttp(request);
            String requestType = httpRequest.getHeader("X-Requested-With");
            // 如果是ajax请求
            if ("XMLHttpRequest".equals(requestType)) {
                HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                httpServletResponse.sendError(HttpStatus.FORBIDDEN.value());
                return false;
            } else {
                // 执行重定向
                WebUtils.issueRedirect(request, response, kickoutUrl);
                return false;
            }
        }

        return true;
    }


}
