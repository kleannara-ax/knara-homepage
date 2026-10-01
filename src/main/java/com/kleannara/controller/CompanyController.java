package com.kleannara.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CompanyController extends FrontCommon {

    @RequestMapping(value = {"/Company"})
    public String main(Model model){
        getGnbInfo(model);
        return "redirect:/Company/company";
    }

    //회사소개
    @RequestMapping("/Company/company")
    public String company(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/company");
    }

    //연혁개요
    @RequestMapping("/Company/history")
    public String history(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/company_history");
    }

    //디지털역사관 소개
    @RequestMapping("/Company/digital")
    public String digital(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/company_digital");
    }

    //공장소개
    @RequestMapping("/Company/factory")
    public String factory(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/company_factory");
    }

    //계열사소개
    @RequestMapping("/Company/subsidiary")
    public String subsidiary(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/company_subsidiary");
    }

    //오시는길
    @RequestMapping("/Company/come")
    public String come(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/company_come");
    }
}
