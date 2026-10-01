package com.kleannara.controller;

import com.google.gson.Gson;
import com.kleannara.config.CustomPasswordEncoding;
import com.kleannara.model.AdminLogModel;
import com.kleannara.model.AdminModel;
import com.kleannara.model.MessageModel;
import com.kleannara.service.AdminLoginService;
import com.kleannara.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
public class AdminLoginController extends AdminCommon{

    @Autowired
    AdminLoginService adminLoginService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AdminCommon adminCommon;

    @Autowired
    private CustomPasswordEncoding customPasswordEncoder;

    @RequestMapping("/AdminLogin/login")
    public String login(HttpServletRequest request, Model model){

        if (CommonUtil.isAllowedIp(CommonUtil.getRemoteIP(request))) {
            return "admin/content/login";
        }
        else {
            return "Error_403";
        }
    }

    @RequestMapping("/AdminLogin/loginProc")
    public String loginProc(@ModelAttribute("params") final AdminModel params, HttpServletRequest request, Model model) {
        AdminModel admin = adminLoginService.getAdminOne(params);
        boolean loginResult = false;

        if(admin==null){
            loginResult = false;
        }
        else if(admin.getLogin_allow() == 1){
            MessageModel message = new MessageModel("확인이 필요한 계정입니다.\n관리자에게 문의하세요", "/AdminLogin/login", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            String paramPW = params.getPassword();
            String adminPW = admin.getPassword();
            try{
                loginResult = customPasswordEncoder.sha256Matching(paramPW, adminPW);
            }
            catch (Exception e){
                loginResult = false;
            }

            if(loginResult) {
                loginResult = true;
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(7200);
                session.setAttribute("admin_name", admin.getName());
                session.setAttribute("admin_id", admin.getId());
                session.setAttribute("admin_idx", admin.getIdx());
                session.setAttribute("auth_menu1", admin.getAuth_menu1());
                session.setAttribute("auth_menu2", admin.getAuth_menu2());
                session.setAttribute("auth_menu3", admin.getAuth_menu3());
                session.setAttribute("auth_menu4", admin.getAuth_menu4());
                session.setAttribute("auth_menu5", admin.getAuth_menu5());
                session.setAttribute("auth_menu6", admin.getAuth_menu6());
                session.setAttribute("auth_menu7", admin.getAuth_menu7());
            }
            else
                loginResult = false;
        }

        if(loginResult){
            adminLoginService.updateLoginDate(admin);
        }


        if(loginResult){

        	AdminLogModel adminLog = new AdminLogModel();
        	adminLog.setType("I");
        	adminLog.setMemo("로그인");
        	
        	this.setAdminLog(request, adminLog);
        	
            return "redirect:/Admin/dashboard";
        }
        else {
            MessageModel message = new MessageModel("로그인 정보를 확인하세요.", "/AdminLogin/login", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    @RequestMapping("/AdminLogin/logoutProc")
    public String logoutProc(@ModelAttribute("params") final AdminModel params, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        
        AdminLogModel adminLog = new AdminLogModel();
    	adminLog.setType("O");
    	adminLog.setMemo("로그아웃");
    	
    	this.setAdminLog(request, adminLog);
    	
        session.setAttribute("admin_name", null);
        session.setAttribute("admin_id", null);
        session.setAttribute("admin_idx", null);
        session.setAttribute("auth_menu1", null);
        session.setAttribute("auth_menu2", null);
        session.setAttribute("auth_menu3", null);
        session.setAttribute("auth_menu4", null);
        session.setAttribute("auth_menu5", null);
        session.setAttribute("auth_menu6", null);
        session.setAttribute("auth_menu7", null);

        MessageModel message = new MessageModel("로그아웃 성공.", "/AdminLogin/login", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    //ajax 컬럼 업데이트
    @RequestMapping("/AdminLogin/ajaxChangeAdminPassword") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> ajaxChangeAdminPassword(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("admin_id");
        String password = (String)params.get("old_pw");
        String password_new = (String)params.get("new_pw");

        Map<String, Object> data = new HashMap<String, Object>();
        AdminModel tempParams = new AdminModel();
        tempParams.setId(id);

        boolean passwordPatternCheck = isValidPassword(password_new);

        if(!passwordPatternCheck){
            data.put("result", "false");
            data.put("msg", "잘못된 비밀번호 변경 접근입니다.");
            return data;
        }

        AdminModel admin = adminLoginService.getAdminOne(tempParams);
        if(admin == null){
            data.put("result", "false");
            data.put("msg", "관리자 정보가 올바르지 않습니다.");
        }
        else {
            String adminPW = admin.getPassword();
            boolean matchingResult = false;
            try{
                matchingResult = customPasswordEncoder.sha256Matching(password, adminPW);
            }
            catch (Exception e){
                matchingResult = false;
            }

            if(!matchingResult) {
                data.put("result", "false");
                data.put("msg", "기존 비밀번호를 확인해 주세요.");
            }
            else{
                admin.setPassword(customPasswordEncoder.sha256Encoding(password_new));
                int result = adminLoginService.updateAdminPassword(admin);

                AdminLogModel adminLog = new AdminLogModel();
                adminLog.setType("U");
                adminLog.setId(id);
                adminLog.setServiceTarget(id);
                adminLog.setMemo("비밀번호 본인 변경");
//                adminLog.setServiceName(CommonUtil.getServiceName());
//                adminLog.setIp(CommonUtil.getRemoteIP(request));

                adminCommon.setAdminLog(request, adminLog);
                if(result > 0) {
                    data.put("result", "success");
                }
            }
        }

        return data;
    }



    @RequestMapping("/AdminLogin/testPW") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> testPW(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){
        String pw1 = (String)params.get("pw1");
        String pw2 = (String)params.get("pw2");
        String pw1_ = customPasswordEncoder.sha256Encoding(pw1);
        String pw2_ = customPasswordEncoder.sha256Encoding(pw2);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("pw1", pw1_);
        data.put("pw2", pw2_);
        data.put("matching1", customPasswordEncoder.sha256Matching(pw1, pw1_));
        data.put("matching2", customPasswordEncoder.sha256Matching(pw2, pw2_));
        return data;
    }

    private boolean isValidPassword(String password) {
        // 1. 8글자 이상인지 확인
        if (password.length() < 8) {
            return false;
        }

        // 2. 8~9글자: 숫자, 영문, 특수문자 포함해야 함
        if (password.length() <= 9) {
            return Pattern.compile("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])").matcher(password).find();
        }

        // 3. 10글자 이상: 숫자, 영문 포함해야 함
        return Pattern.compile("(?=.*[0-9])(?=.*[a-zA-Z])").matcher(password).find();
    }

}
