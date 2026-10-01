package com.kleannara.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

@Component
@Aspect
public class XSSProcessingAspect {

    // @ControllerAdvice를 사용하여 모든 @RequestMapping 또는 특정 경로에 대한 처리
    @Around("execution(* com.kleannara.controller..*(..))") // 모든 컨트롤러 메서드를 대상으로
    public Object processXSS(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메서드 실행
        Object result = joinPoint.proceed();

        // 반환값이 String일 경우 HTML 디코딩 처리
        if (result instanceof String) {
            result = HtmlUtils.htmlUnescape((String) result); // HTML 디코딩 (이스케이프 해제)
        }

        // 결과 반환
        return result;
    }
}