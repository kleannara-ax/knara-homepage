
package com.kleannara.controller;

import com.kleannara.model.BrandCategoryModel;
import com.kleannara.model.BrandModel;
import com.kleannara.model.ProductModel;
import com.kleannara.service.AdminBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BrandController extends FrontCommon {

    private final AdminBrandService brandService;

    @RequestMapping(value = {"/Brand"})
    public String main(Model model){
        return "redirect:/Brand/brandMain";
    }

    @RequestMapping("/Brand/brandMain")
    public String brandMain(@ModelAttribute("params") final BrandModel params, HttpServletRequest request, Model model){

        //용지카테고리
        BrandCategoryModel tempParams = new BrandCategoryModel();
        tempParams.setBrand_idx(1);
        tempParams.setShow_yn("Y");
        List<BrandCategoryModel> list1 = brandService.getBrandCategoryList(tempParams);
        model.addAttribute("list1", list1);
        
        //PS사업부
        params.setType("2");
        params.setShow_yn("Y");
        List<BrandModel> list2 = brandService.getBrandList(params);
        model.addAttribute("list2", list2);
        
        //HL사업부
        params.setType("3");
        params.setShow_yn("Y");
        List<BrandModel> list3 = brandService.getBrandList(params);
        model.addAttribute("list3", list3);

        //페이지 타입
        if(params.getPage_type() == null) {
            //java.util.Random random = new java.util.Random();
            //int tmp = random.nextInt(2) + 1;
            //params.setPage_type(tmp+"");
            params.setPage_type("2");
        }

        getGnbInfo(model);
        return getViewPath(request, "front/content/brandMain");
    }

    @RequestMapping("/Brand/paperList")
    public String paperList(@ModelAttribute("params") final ProductModel params, HttpServletRequest request, Model model){

        BrandCategoryModel tempParams = new BrandCategoryModel();
        tempParams.setIdx(params.getCategory_idx());
        BrandCategoryModel data = brandService.getBrandCategoryOne(tempParams);
        model.addAttribute("data", data);

        params.setShow_yn("Y");
        List<ProductModel> list = brandService.getProductList(params);
        model.addAttribute("list", list);

        getGnbInfo(model);
        return getViewPath(request, "front/content/paperList");
    }

    @RequestMapping("/Brand/brandList")
    public String brandList(@ModelAttribute("params") final BrandCategoryModel params, HttpServletRequest request, Model model){

        //브랜드정보
        BrandModel tempParams = new BrandModel();
        tempParams.setIdx(params.getBrand_idx());
        tempParams.setShow_yn("Y");
        BrandModel brand = brandService.getBrandOne(tempParams);

        //카테고리 리스트
        //params.setShow_yn("Y"); 브랜드 카테고리는 show yn 없음
        List<BrandCategoryModel> list = brandService.getBrandCategoryList(params);

        //제품 리스트
        List<List<ProductModel>> list2 = new ArrayList<>();
        for(BrandCategoryModel cate : list) {
            ProductModel tempParams2 = new ProductModel();
            tempParams2.setBrand_idx(params.getBrand_idx() + "");
            tempParams2.setShow_yn("Y");
            tempParams2.setCategory_idx(cate.getIdx());
            List<ProductModel> tempList = brandService.getProductList(tempParams2);
            list2.add(tempList);
        }

        model.addAttribute("list", list);
        model.addAttribute("list2", list2);
        model.addAttribute("data", brand);

        getGnbInfo(model);
        return getViewPath(request, "front/content/brandList");
    }
}
