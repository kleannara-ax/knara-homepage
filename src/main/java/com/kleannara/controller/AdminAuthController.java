package com.kleannara.controller;

import com.google.gson.Gson;
import com.kleannara.config.CustomPasswordEncoding;
import com.kleannara.model.AdminLogModel;
import com.kleannara.model.AdminModel;
import com.kleannara.model.BannerModel;
import com.kleannara.model.MessageModel;
import com.kleannara.model.SearchModel;
import com.kleannara.paging.paging.PagingResponse;
import com.kleannara.service.AdminAuthService;
import com.kleannara.util.CommonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
public class AdminAuthController extends AdminCommon {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private CustomPasswordEncoding customPasswordEncoder;

    /**
     * admin 시작
     */
    //admin리스트화면
    @RequestMapping("/AdminAuth")
    public String adminMain(Model model){
        return "redirect:/AdminAuth/adminList";
    }

    //admin리스트화면
    @RequestMapping("/AdminAuth/adminList")
    public String adminList(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){
        PagingResponse<AdminModel> response = adminService.getAdminList(params);
        model.addAttribute("response", response);
        
        if (true) {
    		AdminLogModel adminLog = new AdminLogModel();
    		adminLog.setType("R");
    		adminLog.setMemo(params.toStringAdminLog("adminList"));
    		
    		this.setAdminLog(request, adminLog);
    	}
        
        return AuthCheck(7, "admin/content/adminList", request, model);
    }

    //admin추가 및 업데이트
    @RequestMapping("/AdminAuth/insertAdmin")
    public String insertAdmin(@ModelAttribute("params") final AdminModel params, HttpServletRequest request, Model model){

        //db
        int insertedId = params.getIdx();
        boolean passwordPatternCheck = isValidPassword(params.getPassword());

        if(!passwordPatternCheck){

            MessageModel message = new MessageModel("잘못된 접근입니다.", "/AdminAuth/adminList", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        params.setPassword(customPasswordEncoder.sha256Encoding(params.getPassword()));
        if(params.getAuth_menu1() == null) params.setAuth_menu1("N");
        if(params.getAuth_menu2() == null) params.setAuth_menu2("N");
        if(params.getAuth_menu3() == null) params.setAuth_menu3("N");
        if(params.getAuth_menu4() == null) params.setAuth_menu4("N");
        if(params.getAuth_menu5() == null) params.setAuth_menu5("N");
        if(params.getAuth_menu6() == null) params.setAuth_menu6("N");
        if(params.getAuth_menu7() == null) params.setAuth_menu7("N");
        
        AdminLogModel adminLog = new AdminLogModel();
        adminLog.setType("C");
        adminLog.setServiceTarget(String.valueOf(params.getId()));
        adminLog.setMemo("[관리자] " + " 상태: 생성, " + params.toStringAdminLog("insertAdmin"));
        
        if(insertedId == 0) {
            insertedId = adminService.insertAdmin(params);
        }
        else {
//            params.setPassword(null);
            insertedId = adminService.updateAdmin(params);
            
        	adminLog.setType("U");
            adminLog.setMemo("[관리자] " + " 상태: 비밀번호 변경, " + params.toStringAdminLog("insertAdmin"));
        }

        if(insertedId > 0){
        	this.setAdminLog(request, adminLog);
        	
            MessageModel message = new MessageModel("해당 내용이 등록 되었습니다", "/AdminAuth/adminList", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            MessageModel message = new MessageModel("등록에 실패했습니다.", "/AdminAuth/adminList", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    //admin삭제
    @RequestMapping("/AdminAuth/deleteAdmin")
	@ResponseBody
    public Map<String, Object> deleteAdmin(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

    	String idx = (String)params.get("idx");
        String value = (String)params.get("value");
        String type = (String)params.get("type");
        String id = (String)params.get("id");
    	
        int result = 0;
        AdminModel tempParams = new AdminModel();
        
        tempParams.setIdx(Integer.parseInt(idx));
        if("show_yn".equals(type))
            tempParams.setDel_yn(value);
        else if("del_yn".equals(type))
            tempParams.setDel_yn(value);
        
        result = adminService.updateAdmin(tempParams);
        
        Map<String, Object> data = new HashMap<String, Object>();

        if(result <= 0)
            data.put("result", "fail");
        else {
        	data.put("result", "success");
        	
        	if (true) {
        		AdminLogModel adminLog = new AdminLogModel();
        		adminLog.setType("D");
        		adminLog.setServiceTarget(id);
        		
        		this.setAdminLog(request, adminLog);
        	}
        }

        return data;
    }
    
    //admin상세화면
    @RequestMapping("/AdminAuth/adminView")
    public String adminView(@ModelAttribute("params") final AdminModel params, HttpServletRequest request, Model model){
        AdminModel data = new AdminModel();
        if(params.getIdx() != 0) {
            data = adminService.getAdminOne(params);
            
            if (true) {
        		AdminLogModel adminLog = new AdminLogModel();
        		adminLog.setType("R");
        		adminLog.setServiceTarget(String.valueOf(data.getId()));

        		this.setAdminLog(request, adminLog);
        	}
        }

        String authCheck = "";

        HttpSession session = request.getSession();
        Map<String, String> response = new HashMap<>();

        String id = (String)session.getAttribute("admin_id");

        int adminStatus = adminService.getAdminStatus(id);

        if(adminStatus == 1){
            authCheck = "19hfd8ws!#uyhf87yg1";
        }

        model.addAttribute("data", data);
        model.addAttribute("auth", authCheck);
        
        return AuthCheck(7, "admin/content/adminView", request, model);
    }

    @RequestMapping("/AdminAuth/changePassword")
    public String checkPasswordChanged(Model model){
        return "admin/content/changePassword";
    }
    @PostMapping("/AdminAuth/checkPasswordChanged")
    public ResponseEntity<Map<String, String>> checkPasswordChanged(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, String> response = new HashMap<>();

        String id = (String)session.getAttribute("admin_id");

        AdminModel tempParams = new AdminModel();
        tempParams.setId(id);

        String passwordChange = adminService.getAdminPasswordCheck(tempParams);
        session.setAttribute("passwordChange", passwordChange);
        if ("N".equals(passwordChange)) {
            response.put("status", "change_required");
        } else {
            response.put("status", "ok");
        }

        return ResponseEntity.ok(response);
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
