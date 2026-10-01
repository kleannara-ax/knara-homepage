package com.kleannara.controller;

import com.kleannara.util.CommonUtil;
import com.kleannara.model.BannerModel;
import com.kleannara.model.NewsModel;
import com.kleannara.model.PopupModel;
import com.kleannara.model.SearchModel;
import com.kleannara.paging.paging.PagingResponse;
import com.kleannara.service.AdminMainService;
import com.kleannara.service.AdminNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MainController extends FrontCommon {

    private final AdminNewsService newsService;
    private final AdminMainService mainService;

    //대시보드
    @RequestMapping(value = {"/", "/Main"})
    public String main(HttpServletRequest request, Model model, HttpServletResponse response){
        String lang = request.getParameter("lang");
        if(lang == null || "".equals(lang))
            lang = "ko";
        Cookie genderCookie = new Cookie("lang", lang);
        genderCookie.setMaxAge(60*60*24*365);
        response.addCookie(genderCookie);

        return "redirect:/Main/main";
    }

    //배너리스트화면
    @RequestMapping("/Main/main")
    public String index(HttpServletRequest request, Model model){

        //배너
        SearchModel params = new SearchModel();
        params.setShow_yn("Y");
        params.setLimit(5);
        PagingResponse<BannerModel> response = mainService.getBannerList(params);
        model.addAttribute("response", response);

        //공지사항
        params = new SearchModel();
        params.setMain_yn("Y");
        params.setMain_type("1");
        params.setShow_yn("Y");
        params.setLimit(12);
        PagingResponse<NewsModel> response1 = newsService.getNewsList(params);
        model.addAttribute("response1", response1);

        //매체, 홍보
        params = new SearchModel();
        params.setMain_yn("Y");
        params.setMain_type("2");
        params.setShow_yn("Y");
        params.setLimit(5);
        PagingResponse<NewsModel> response2 = newsService.getNewsList(params);
        model.addAttribute("response2", response2);

        //팝업
        String lang = getCookie(request, "lang", "ko");
        if("ko".equals(lang))
            lang = "1";
        else if("en".equals(lang))
            lang = "2";
        else if("zh".equals(lang))
            lang = "3";
        params = new SearchModel();
        params.setShow_yn("Y");
        params.setNow_yn("Y");
        params.setLang(lang);
        if("pc".equals(CommonUtil.getDeviceType(request)))
            params.setShow_pc("Y");
        else
            params.setShow_mo("Y");
        PagingResponse<PopupModel> response3 = mainService.getPopupList(params);
        int popupCnt = 0;
        for (PopupModel popup : response3.getList()) {
            int idx = popup.getIdx();
            Cookie popupName = WebUtils.getCookie(request, "popup"+idx);
            if(popupName != null)
                popup.setCookie_yn("Y");
            else
                popupCnt++;
        }
        model.addAttribute("response3", response3);
        model.addAttribute("popup_cnt", popupCnt);

        getGnbInfo(model);
        return getViewPath(request, "front/content/main");
    }

    //임시오픈
    @RequestMapping("/Main/open")
    public String open(HttpServletRequest request, Model model){

        return getViewPath(request, "front/content/open");
    }

    /**
     * 봇 크롤링 막기
     */
    @RequestMapping(value = "/robots.txt")
    @ResponseBody
    public void robotsBlock(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.getWriter().write("User-agent: *\nAllow: /\n");
        } catch (IOException e) {
        }
    }
}
