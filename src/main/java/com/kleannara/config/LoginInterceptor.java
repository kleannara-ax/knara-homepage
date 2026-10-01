package com.kleannara.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("==================== BEGIN ====================");

        String requestUrl = request.getRequestURI();

        //오픈 ipcheck
        /*
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getHeader("X-FORWARDED-FOR");
        if (ip == null)
            ip = req.getRemoteAddr();
        if(!requestUrl.contains("/Main/open") && !requestUrl.contains("/open.png")){
            if(!ip.contains("59.9.131.234") && !ip.contains("121.134.129.198") && !ip.contains("0:0:0:0:0:0:0:1") && !ip.contains("218.153.139.6")
                    && !ip.contains("58.120.171.30") && !ip.contains("218.153.203.114") && !ip.contains("125.178.6.91")){
                response.sendRedirect("/Main/open");
                return false; // 미인증 사용자는 다음으로 진행하지 않고 끝
            }
        }
        */

        if(requestUrl.contains("/AdminLogin"))
            return HandlerInterceptor.super.preHandle(request, response, handler);

        if(requestUrl.contains("/Admin")) {
            //새션없음
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("admin_name") == null) {
                // 로그인으로 redirect
                response.sendRedirect("/AdminLogin/login");
                return false; // 미인증 사용자는 다음으로 진행하지 않고 끝
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.debug("==================== END ======================");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

}
