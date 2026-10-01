package com.kleannara.controller;

import com.kleannara.model.AdminLogModel;
import com.kleannara.model.MessageModel;
import com.kleannara.service.AdminAuthService;
import com.kleannara.service.FileService;
import com.kleannara.util.CommonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.UUID;

@Controller
public class AdminCommon {

    @Autowired
    FileService fileService;
    
    @Autowired
    AdminAuthService adminService;

    // 사용자에게 메시지를 전달하고, 페이지를 리다이렉트 한다.
    public String showMessageAndRedirect(final MessageModel params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }

    public String AuthCheck(int menuType, String redirectUrl, HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        String auth = (String)session.getAttribute("auth_menu"+menuType);
        if(!"Y".equals(auth)){
            MessageModel message = new MessageModel("접근 권한이 없습니다.", "/Admin", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }else {
            return redirectUrl;
        }
    }
    
    // 관리자 로그 기록
    public int setAdminLog(HttpServletRequest request, AdminLogModel adminLog) {
    	
    	HttpSession session = request.getSession(false);
    	String action = request.getRequestURL().toString();
    	String referer = request.getHeader("Referer").toString();

    	adminLog.setId((String) session.getAttribute("admin_id"));
    	adminLog.setReferer(referer);
    	adminLog.setAction(action);
    	adminLog.setServiceName(CommonUtil.getServiceName());
    	adminLog.setIp(CommonUtil.getRemoteIP(request));

    	return adminService.insertAdminLog(adminLog);
    }
}
