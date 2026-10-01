package com.kleannara.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RecruitController extends FrontCommon {

    @RequestMapping(value = {"/Recruit"})
    public String main(Model model){
        return "redirect:/Recruit/talent";
    }

    //인재상
    @RequestMapping("/Recruit/talent")
    public String talent(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/recruit_talent");
    }

    //인사제도
    @RequestMapping("/Recruit/system")
    public String system(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/recruit_system");
    }

    //채용정보
    @RequestMapping("/Recruit/info")
    public String info(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/recruit_info");
    }

    //직무소개
    @RequestMapping("/Recruit/job")
    public String job(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/recruit_job");
    }
}
