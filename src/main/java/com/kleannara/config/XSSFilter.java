package com.kleannara.config;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XSSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // XSS 필터링을 적용한 요청 객체를 체인으로 전달
        XSSRequestWrapper wrappedRequest = new XSSRequestWrapper(req);

        // 응답에 대해서도 XSS 방지 처리 (필요에 따라)
        XSSResponseWrapper wrappedResponse = new XSSResponseWrapper(res);

        // 필터 체인 진행
        chain.doFilter(wrappedRequest, wrappedResponse);
    }

    @Override
    public void destroy() {
        // 필터 종료
    }
}