package com.kleannara.controller;

import com.kleannara.model.BrandCategoryModel;
import com.kleannara.model.BrandModel;
import com.kleannara.model.MessageModel;
import com.kleannara.service.AdminBrandService;
import com.kleannara.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Controller
public class FrontCommon {

    @Autowired
    FileService fileService;

    @Autowired
    AdminBrandService brandService;

    // 사용자에게 메시지를 전달하고, 페이지를 리다이렉트 한다.
    public String showMessageAndRedirect(final MessageModel params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }

    // 브랜드 정보 gnb설정
    public Model getGnbInfo(Model model) {

        //용지카테고리
        BrandCategoryModel tempParams = new BrandCategoryModel();
        tempParams.setBrand_idx(1);
        tempParams.setShow_yn("Y");
        List<BrandCategoryModel> list1 = brandService.getBrandCategoryList(tempParams);
        model.addAttribute("paperList", list1);
        
        //PS사업부
        BrandModel params = new BrandModel();
        params.setType("2");    //ps
        //params.setShow_yn("Y"); 브랜드 카테고리는 show yn 없음
        List<BrandModel> list2 = brandService.getBrandList(params);
        
        //HL사업부
        model.addAttribute("brandList2", list2);
        params.setType("3");
        params.setShow_yn("Y");
        List<BrandModel> list3 = brandService.getBrandList(params);
        model.addAttribute("brandList3", list3);

        return model;
    }

    public String getViewPath(HttpServletRequest request, String path){
        Cookie langCookie = WebUtils.getCookie(request, "lang");
        String lang = null;
        if(langCookie != null)
            lang = langCookie.getValue();
        if(lang == null || "".equals(lang))
            lang = "ko";

        String returnPath = path.replace("front", "front/"+lang);
        return returnPath;
    }

    public String getCookie(HttpServletRequest request, String name, String defaultValue){
        Cookie langCookie = WebUtils.getCookie(request, name);
        String value = null;
        if(langCookie != null)
            value = langCookie.getValue();

        if(value == null || "".equals(value))
            value = defaultValue;

        return value;
    }
}
