package com.kleannara.controller;

import com.google.gson.Gson;
import com.kleannara.model.*;
import com.kleannara.service.AdminBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminBrandController extends AdminCommon {
    @Autowired
    AdminBrandService brandService;

    //브랜드리스트화면
    @RequestMapping("/AdminBrand")
    public String adminMain(Model model){
        return "redirect:/AdminBrand/brandList";
    }

    /**
     * 브랜드 시작
     */
    //브랜드리스트화면
    @RequestMapping("/AdminBrand/brandList")
    public String brandList(@ModelAttribute("params") final BrandModel params, HttpServletRequest request, Model model){
        //용지는 브랜드관리 없음
        if(params.getType() == null || "".equals(params.getType())) {
            params.setType("1");
            return AuthCheck(2, "redirect:/AdminBrand/brandCategoryList?brand_idx=1&brand_type=1", request, model);
        }

        List<BrandModel> list = brandService.getBrandList(params);
        model.addAttribute("list", list);
        return AuthCheck(2, "admin/content/brandList", request, model);
    }

    //브랜드추가 및 업데이트
    @RequestMapping("/AdminBrand/insertBrand")
    public String insertBrand(@ModelAttribute("params") final BrandModel params, HttpServletRequest request, Model model){

        //이미지 업로드
        FileModel fileModel1 = null;
        FileModel fileModel2 = null;
        FileModel fileModel3 = null;
        try {
            //초기정보 파라미터 셋팅
            params.setImage_name(params.getPre_image_name());
            params.setImage_path(params.getPre_image_path());
            params.setThumb_name(params.getPre_thumb_name());
            params.setThumb_path(params.getPre_thumb_path());
            params.setLogo_name(params.getPre_logo_name());
            params.setLogo_path(params.getPre_logo_path());
            //저장
            fileModel1 = fileService.saveFile(params.getImage(), "brand");
            fileModel2 = fileService.saveFile(params.getThumb(), "brand");
            fileModel3 = fileService.saveFile(params.getLogo(), "brand");
            //업로드 파일 셋팅
            if(fileModel1 != null) {
                params.setImage_name(fileModel1.getFileOrig());
                params.setImage_path(fileModel1.getFileName());
            }
            if(fileModel2 != null) {
                params.setThumb_name(fileModel2.getFileOrig());
                params.setThumb_path(fileModel2.getFileName());
            }
            if(fileModel3 != null) {
                params.setLogo_name(fileModel3.getFileOrig());
                params.setLogo_path(fileModel3.getFileName());
            }
        } catch (IOException e){
            MessageModel message = new MessageModel("이미지 업로드 실패", "/AdminBrand/brandView?type="+params.getType()+"&idx="+params.getIdx(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        //db
        HttpSession session = request.getSession();
        String idx = session.getAttribute("admin_idx")+"";
        params.setReg_admin(idx);
        int insertedId = params.getIdx();
        if(insertedId == 0)
            insertedId = brandService.insertBrand(params);
        else
            insertedId = brandService.updateBrand(params);

        if(insertedId > 0){
            MessageModel message = new MessageModel("해당 내용이 등록 되었습니다", "/AdminBrand/brandList?type="+params.getType(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            MessageModel message = new MessageModel("등록에 실패했습니다.", "/AdminBrand/brandList?type="+params.getType(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    //ajax 컬럼 업데이트
    @RequestMapping("/AdminBrand/updateBrandValue") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> updateBrandValue(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        String idx = (String)params.get("idx");
        String value = (String)params.get("value");
        String type = (String)params.get("type");

        int result = 0;
        BrandModel tempParams = new BrandModel();
        tempParams.setIdx(Integer.parseInt(idx));
        if("show_yn".equals(type))
            tempParams.setShow_yn(value);
        else if("del_yn".equals(type))
            tempParams.setDel_yn(value);

        result = brandService.updateBrand(tempParams);

        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<String, Object>();

        if(result <= 0)
            data.put("result", "fail");
        else
            data.put("result", "success");

        return data;
    }

    //브랜드상세화면
    @RequestMapping("/AdminBrand/brandView")
    public String brandView(@ModelAttribute("params") final BrandModel params, HttpServletRequest request, Model model){
        BrandModel data = brandService.getBrandOne(params);
        if(data == null)
            data = new BrandModel();
        model.addAttribute("data", data);
        return AuthCheck(2, "admin/content/brandView", request, model);
    }

    //ajax 컬럼 업데이트
    @RequestMapping("/AdminBrand/ajaxUpdateBrandOrder") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> ajaxUpdateBrandOrder(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        String idxs[] = (String[])request.getParameterValues("idx[]");

        int result = 0;
        for(int i = 0; i < idxs.length; i++){
            BrandModel tempParams = new BrandModel();
            tempParams.setIdx(Integer.parseInt(idxs[i]));
            tempParams.setShow_order(i+1);

            result += brandService.updateBrand(tempParams);
        }

        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<String, Object>();

        if(result < idxs.length)
            data.put("result", "fail");
        else
            data.put("result", "success");

        return data;
    }

    /**
     * 브랜드 카테고리 시작
     */
    //브랜드 카테고리 리스트화면
    @RequestMapping("/AdminBrand/brandCategoryList")
    public String brandCategoryList(@ModelAttribute("params") final BrandCategoryModel params, HttpServletRequest request, Model model){

        //용지타입
        String returnPage = "admin/content/brandCategoryList";
        if("1".equals(params.getBrand_type()))
            returnPage = "admin/content/paperCategoryList";

        BrandModel tempParams = new BrandModel();
        tempParams.setIdx(params.getBrand_idx());
        BrandModel brand = brandService.getBrandOne(tempParams);

        List<BrandCategoryModel> list = brandService.getBrandCategoryList(params);

        model.addAttribute("list", list);
        model.addAttribute("brand", brand);

        return AuthCheck(2, returnPage, request, model);
    }

    //브랜드카테고리추가 및 업데이트
    @RequestMapping("/AdminBrand/insertBrandCategory")
    public String insertBrandCategory(@ModelAttribute("params") final BrandCategoryModel params, HttpServletRequest request, Model model){
        //이미지 업로드
        FileModel fileModel1 = null;
        FileModel fileModel2 = null;
        try {
            //초기정보 파라미터 셋팅
            params.setImage_name(params.getPre_image_name());
            params.setImage_path(params.getPre_image_path());
            params.setThumb_name(params.getPre_thumb_name());
            params.setThumb_path(params.getPre_thumb_path());
            //저장
            fileModel1 = fileService.saveFile(params.getImage(), "category");
            fileModel2 = fileService.saveFile(params.getThumb(), "category");
            //업로드 파일 셋팅
            if(fileModel1 != null) {
                params.setImage_name(fileModel1.getFileOrig());
                params.setImage_path(fileModel1.getFileName());
            }
            if(fileModel2 != null) {
                params.setThumb_name(fileModel2.getFileOrig());
                params.setThumb_path(fileModel2.getFileName());
            }
        } catch (IOException e){
            MessageModel message = new MessageModel("이미지 업로드 실패", "/AdminBrand/brandCategoryView?brand_type="+params.getBrand_type()+"&brand_idx="+params.getBrand_idx()+"&idx="+params.getIdx(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        //db
        HttpSession session = request.getSession();
        String idx = session.getAttribute("admin_idx")+"";
        params.setReg_admin(idx);
        int insertedId = params.getIdx();
        if(insertedId == 0)
            insertedId = brandService.insertBrandCategory(params);
        else
            insertedId = brandService.updateBrandCategory(params);

        if(insertedId > 0){
            MessageModel message = new MessageModel("해당 내용이 등록 되었습니다", "/AdminBrand/brandCategoryList?brand_type="+params.getBrand_type()+"&brand_idx="+params.getBrand_idx(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            MessageModel message = new MessageModel("등록에 실패했습니다.", "/AdminBrand/brandCategoryList?brand_type="+params.getBrand_type()+"&brand_idx="+params.getBrand_idx(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    //브랜드카테고리전체순서변경
    @RequestMapping("/AdminBrand/updateBrandCategoryOrder") //ajax 일때만 []
    public String updateBrandCategoryOrder(HttpServletRequest request, HttpServletResponse response, Model model){

        String[] idx_arr = (String[])request.getParameterValues("idx[]");
        int order = 1;
        int result = 0;
        for (String idx : idx_arr) {
            BrandCategoryModel tempParams = new BrandCategoryModel();
            tempParams.setIdx(Integer.parseInt(idx));
            tempParams.setShow_order(order);
            order++;

            result += brandService.updateBrandCategory(tempParams);
        }

        String resultStr = "해당 내용이 등록 되었습니다.";
        if(result <= 0)
            resultStr = "수정에 실패했습니다.";

        MessageModel message = new MessageModel(resultStr, "/AdminBrand/brandCategoryList", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    //ajax 컬럼 추가
    @RequestMapping("/AdminBrand/ajaxInsertBrandCategory")
    @ResponseBody
    public Map<String, Object> ajaxInsertBrandCategory(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        int brand_idx = Integer.parseInt((String)params.get("brand_idx"));
        BrandCategoryModel tempParams = new BrandCategoryModel();
        tempParams.setBrand_idx(brand_idx);
        HttpSession session = request.getSession();
        String idx = session.getAttribute("admin_idx")+"";
        tempParams.setReg_admin(idx);
        int result = brandService.insertBrandCategory(tempParams);

        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<String, Object>();

        if(result <= 0)
            data.put("result", "fail");
        else {
            data.put("result", "success");
            data.put("idx", tempParams.getIdx());
        }

        return data;
    }

    //ajax 컬럼 업데이트
    @RequestMapping("/AdminBrand/ajaxUpdateBrandCategory") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> ajaxUpdateBrandCategory(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        String idxs[] = (String[])request.getParameterValues("idx[]");
        String title_kos[] = (String[])request.getParameterValues("title_ko[]");
        String title_ens[] = (String[])request.getParameterValues("title_en[]");
        String title_zhs[] = (String[])request.getParameterValues("title_zh[]");

        int result = 0;
        for(int i = 0; i < idxs.length; i++){
            BrandCategoryModel tempParams = new BrandCategoryModel();
            tempParams.setIdx(Integer.parseInt(idxs[i]));
            tempParams.setTitle_ko(title_kos[i]);
            tempParams.setTitle_en(title_ens[i]);
            tempParams.setTitle_zh(title_zhs[i]);
            tempParams.setShow_order(i+1);

            result += brandService.updateBrandCategory(tempParams);
        }

        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<String, Object>();

        if(result < idxs.length)
            data.put("result", "fail");
        else
            data.put("result", "success");

        return data;
    }

    @RequestMapping("/AdminBrand/ajaxDeleteBrandCategory") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> ajaxDeleteBrandCategory(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        String idxs = (String)params.get("idx");
        String[] idx_arr = {};
        if(idxs != null )
            idx_arr = idxs.split(",");
        String value = (String)params.get("value");
        String type = (String)params.get("type");
        Boolean isChild = false;
        Map<String, Object> data = new HashMap<String, Object>();

        for (String idx : idx_arr) {
            ProductModel tempParams = new ProductModel();
            tempParams.setCategory_idx(Integer.parseInt(idx));
            tempParams.setDel_yn("N");

            List<ProductModel> tempList = brandService.getProductList(tempParams);
            if(tempList.size() > 0){
                isChild = true;
                break;
            }
        }
        if(isChild) {
            data.put("result", "child");
            return data;
        }

        int result = 0;
        for (String idx : idx_arr) {
            BrandCategoryModel tempParams = new BrandCategoryModel();
            tempParams.setIdx(Integer.parseInt(idx));
            if ("del_yn".equals(type))
                tempParams.setDel_yn(value);

            result = brandService.updateBrandCategory(tempParams);
        }

        Gson gson = new Gson();
        if(result <= 0)
            data.put("result", "fail");
        else
            data.put("result", "success");

        return data;
    }

    //브랜드카테고리상세화면
    @RequestMapping("/AdminBrand/brandCategoryView")
    public String brandCategoryView(@ModelAttribute("params") final BrandCategoryModel params, HttpServletRequest request, Model model){

        //용지타입
        String returnPage = "admin/content/brandCategoryView";
        if("1".equals(params.getBrand_type()))
            returnPage = "admin/content/paperCategoryView";

        BrandCategoryModel data = brandService.getBrandCategoryOne(params);
        if(data == null)
            data = new BrandCategoryModel();
        model.addAttribute("data", data);
        return AuthCheck(2, returnPage, request, model);
    }

    @RequestMapping("/AdminBrand/updateBrandCategoryValue") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> updateBrandCategoryValue(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        String idx = (String)params.get("idx");
        String value = (String)params.get("value");
        String type = (String)params.get("type");

        int result = 0;
        BrandCategoryModel tempParams = new BrandCategoryModel();
        tempParams.setIdx(Integer.parseInt(idx));
        if("show_yn".equals(type))
            tempParams.setShow_yn(value);
        else if("del_yn".equals(type))
            tempParams.setDel_yn(value);

        result = brandService.updateBrandCategory(tempParams);

        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<String, Object>();

        if(result <= 0)
            data.put("result", "fail");
        else
            data.put("result", "success");

        return data;
    }

    /**
     * 제품
     */
    //ajax 컬럼 추가
    @RequestMapping("/AdminBrand/ajaxProductList")
    @ResponseBody
    public Map<String, Object> ajaxProductList(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        int category_idx = Integer.parseInt((String)params.get("category_idx"));
        ProductModel tempParams = new ProductModel();
        tempParams.setCategory_idx(category_idx);
        List<ProductModel> list = brandService.getProductList(tempParams);

        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("result", "success");
        data.put("list", list);

        return data;
    }

    //제품추가 및 업데이트
    @RequestMapping("/AdminBrand/insertProduct")
    public String insertProduct(@ModelAttribute("params") final ProductModel params, HttpServletRequest request, Model model){

        //이미지 업로드
        FileModel fileModel1 = null;
        FileModel fileModel2 = null;
        try {
            //초기정보 파라미터 셋팅
            params.setImage_name(params.getPre_image_name());
            params.setImage_path(params.getPre_image_path());
            params.setThumb_name(params.getPre_thumb_name());
            params.setThumb_path(params.getPre_thumb_path());
            //저장
            fileModel1 = fileService.saveFile(params.getImage(), "product");
            fileModel2 = fileService.saveFile(params.getThumb(), "product");
            //업로드 파일 셋팅
            if(fileModel1 != null) {
                params.setImage_name(fileModel1.getFileOrig());
                params.setImage_path(fileModel1.getFileName());
            }
            if(fileModel2 != null) {
                params.setThumb_name(fileModel2.getFileOrig());
                params.setThumb_path(fileModel2.getFileName());
            }
        } catch (IOException e){
            MessageModel message = new MessageModel("이미지 업로드 실패", "/AdminBrand/brandCategoryView?brand_type="+params.getBrand_type()+"&brand_idx="+params.getBrand_idx()+"&idx="+params.getIdx(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        //db
        HttpSession session = request.getSession();
        String idx = session.getAttribute("admin_idx")+"";
        params.setReg_admin(idx);
        int insertedId = params.getIdx();
        if(insertedId == 0)
            insertedId = brandService.insertProduct(params);
        else
            insertedId = brandService.updateProduct(params);

        if(insertedId > 0){
            MessageModel message = new MessageModel("해당 내용이 등록 되었습니다", "/AdminBrand/brandCategoryList?brand_type="+params.getBrand_type()+"&brand_idx="+params.getBrand_idx(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            MessageModel message = new MessageModel("등록에 실패했습니다.", "/AdminBrand/brandCategoryList?brand_type="+params.getBrand_type()+"&brand_idx="+params.getBrand_idx(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    //ajax 컬럼 업데이트
    @RequestMapping("/AdminBrand/updateProductValue") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> updateProductValue(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        String idx = (String)params.get("idx");
        String value = (String)params.get("value");
        String type = (String)params.get("type");

        int result = 0;
        ProductModel tempParams = new ProductModel();
        tempParams.setIdx(Integer.parseInt(idx));
        if("show_yn".equals(type))
            tempParams.setShow_yn(value);
        else if("del_yn".equals(type))
            tempParams.setDel_yn(value);

        result = brandService.updateProduct(tempParams);

        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<String, Object>();

        if(result <= 0)
            data.put("result", "fail");
        else
            data.put("result", "success");

        return data;
    }

    //제품상세화면
    @RequestMapping("/AdminBrand/productView")
    public String productView(@ModelAttribute("params") final ProductModel params, HttpServletRequest request, Model model){
        ProductModel data = brandService.getProductOne(params);
        if(data == null)
            data = new ProductModel();
        model.addAttribute("data", data);
        return AuthCheck(2, "admin/content/productView", request, model);
    }

    //ajax 컬럼 업데이트
    @RequestMapping("/AdminBrand/ajaxUpdateProductOrder") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> ajaxUpdateProductOrder(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        String idxs[] = (String[])request.getParameterValues("idx[]");

        int result = 0;
        for(int i = 0; i < idxs.length; i++){
            ProductModel tempParams = new ProductModel();
            tempParams.setIdx(Integer.parseInt(idxs[i]));
            tempParams.setShow_order(i+1);

            result += brandService.updateProduct(tempParams);
        }

        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<String, Object>();

        if(result < idxs.length)
            data.put("result", "fail");
        else
            data.put("result", "success");

        return data;
    }
}
