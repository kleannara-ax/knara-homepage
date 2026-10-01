package com.kleannara.controller;

import com.google.gson.Gson;
import com.kleannara.model.*;
import com.kleannara.paging.paging.PagingResponse;
import com.kleannara.service.AdminMainService;
import com.kleannara.service.AdminNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AdminMainController extends AdminCommon {

    @Autowired
    AdminMainService mainService;

    @Autowired
    AdminNewsService newsService;

    //대시보드
    @RequestMapping("/Admin")
    public String main(Model model){
        return "redirect:/Admin/dashboard";
    }

    //대시보드
    @RequestMapping("/Admin/dashboard")
    public String dashboard(Model model){
        return "admin/content/dashboard";
    }

    //배너리스트화면
    @RequestMapping("/AdminMain")
    public String adminMain(HttpServletRequest request, Model model){
        return "redirect:/AdminMain/bannerList";
    }

    //배너리스트화면
    @RequestMapping("/AdminMain/bannerList")
    public String bannerList(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){
        PagingResponse<BannerModel> response = mainService.getBannerList(params);
        model.addAttribute("response", response);
        return AuthCheck(1, "admin/content/bannerList", request, model);
    }

    //배너추가 및 업데이트
    @RequestMapping("/AdminMain/insertBanner")
    public String insertBanner(@ModelAttribute("params") final BannerModel params, HttpServletRequest request, Model model){

        //이미지 업로드
        FileModel fileModel = null;
        FileModel fileModel2 = null;
        FileModel fileModel3 = null;
        try {
            //초기정보 파라미터 셋팅
            params.setFile_name(params.getPre_file_name());
            params.setFile_path(params.getPre_file_path());
            params.setFile_name_ta(params.getPre_file_name_ta());
            params.setFile_path_ta(params.getPre_file_path_ta());
            params.setFile_name_mo(params.getPre_file_name_mo());
            params.setFile_path_mo(params.getPre_file_path_mo());
            //저장
            fileModel = fileService.saveFile(params.getFile(), "banner");
            fileModel2 = fileService.saveFile(params.getFile_ta(), "banner");
            fileModel3 = fileService.saveFile(params.getFile_mo(), "banner");
            //업로드 파일 셋팅
            if(fileModel != null) {
                params.setFile_name(fileModel.getFileOrig());
                params.setFile_path(fileModel.getFileName());
            }
            if(fileModel2 != null) {
                params.setFile_name_ta(fileModel2.getFileOrig());
                params.setFile_path_ta(fileModel2.getFileName());
            }
            if(fileModel3 != null) {
                params.setFile_name_mo(fileModel3.getFileOrig());
                params.setFile_path_mo(fileModel3.getFileName());
            }
        } catch (IOException e){
            MessageModel message = new MessageModel("이미지 업로드 실패", "/AdminMain/bannerView?"+"idx="+params.getIdx(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        //db
        int insertedId = params.getIdx();
        if(insertedId == 0)
            insertedId = mainService.insertBanner(params);
        else
            insertedId = mainService.updateBanner(params);

        if(insertedId > 0){
            MessageModel message = new MessageModel("해당 내용이 등록 되었습니다", "/AdminMain/bannerList", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            MessageModel message = new MessageModel("등록에 실패했습니다.", "/AdminMain/bannerList", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    //배너전체순서변경
    @RequestMapping("/AdminMain/updateBannerOrder") //ajax 일때만 []
    public String updateBannerOrder(HttpServletRequest request, HttpServletResponse response, Model model){

        String[] idx_arr = (String[])request.getParameterValues("idx[]");
        int order = 1;
        int result = 0;
        for (String idx : idx_arr) {
            BannerModel tempParams = new BannerModel();
            tempParams.setIdx(Integer.parseInt(idx));
            tempParams.setShow_order(order);
            order++;

            result += mainService.updateBanner(tempParams);
        }

        String resultStr = "해당 내용이 등록 되었습니다.";
        if(result <= 0)
            resultStr = "수정에 실패했습니다.";

        MessageModel message = new MessageModel(resultStr, "/AdminMain/bannerList", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    //ajax 컬럼 업데이트
    @RequestMapping("/AdminMain/updateBannerValue") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> updateBannerValue(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        String idx = (String)params.get("idx");
        String value = (String)params.get("value");
        String type = (String)params.get("type");

        int result = 0;
        BannerModel tempParams = new BannerModel();
        tempParams.setIdx(Integer.parseInt(idx));
        if("show_yn".equals(type))
            tempParams.setShow_yn(value);
        else if("del_yn".equals(type))
            tempParams.setDel_yn(value);

        result = mainService.updateBanner(tempParams);

        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<String, Object>();

        if(result <= 0)
            data.put("result", "fail");
        else
            data.put("result", "success");

        return data;
    }

    //배너상세화면
    @RequestMapping("/AdminMain/bannerView")
    public String bannerView(@ModelAttribute("params") final BannerModel params, HttpServletRequest request, Model model){
        BannerModel data = mainService.getBannerOne(params);
        if(data == null)
            data = new BannerModel();
        model.addAttribute("data", data);
        return AuthCheck(1, "admin/content/bannerView", request, model);
    }

    //우리들소식
    @RequestMapping("/AdminMain/newsOrderList")
    public String newsOrderList(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){

        //news List
        params.setMain_type("2");
        PagingResponse<NewsModel> response = newsService.getNewsList(params);
        model.addAttribute("response", response);

        //order List
        SearchModel tempParams = new SearchModel();
        tempParams.setMain_yn("Y");
        tempParams.setMain_type("2");
        tempParams.setLimit(5);
        //params.setPage(1);
        PagingResponse<NewsModel> response2 = newsService.getNewsList(tempParams);
        model.addAttribute("response2", response2);

        return AuthCheck(1, "admin/content/newsOrderList", request, model);
    }

    //배너전체순서변경
    @RequestMapping("/AdminMain/updateNewsOrder") //ajax 일때만 []
    public String updateNewsOrder(HttpServletRequest request, HttpServletResponse response, Model model){

        //기존 order 삭제
        NewsModel delParams = new NewsModel();
        delParams.setMain_yn("N");
        delParams.setMain_type("2");
        newsService.updateNewsOrder(delParams);

        //신규 order 등록
        String[] idx_arr = (String[])request.getParameterValues("idx[]");
        int order = 1;
        int result = 0;
        for (String idx : idx_arr) {
            NewsModel tempParams = new NewsModel();
            tempParams.setIdx(Integer.parseInt(idx));
            tempParams.setShow_order(order);
            tempParams.setMain_yn("Y");
            order++;

            result += newsService.updateNews(tempParams);
        }

        String resultStr = "해당 내용이 등록 되었습니다.";
        if(result <= 0)
            resultStr = "수정에 실패했습니다.";

        MessageModel message = new MessageModel(resultStr, "/AdminMain/newsOrderList", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    //팝업시작
    //팝업리스트화면
    @RequestMapping("/AdminMain/popupList")
    public String popupList(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){
        PagingResponse<PopupModel> response = mainService.getPopupList(params);
        model.addAttribute("response", response);
        return AuthCheck(1, "admin/content/popupList", request, model);
    }

    //팝업추가 및 업데이트
    @RequestMapping("/AdminMain/insertPopup")
    public String insertPopup(@ModelAttribute("params") final PopupModel params, HttpServletRequest request, Model model){
        //db
        int insertedId = params.getIdx();
        String start_date_str = params.getDate1()+" "+params.getHour1()+":"+params.getMin1()+":00";
        String end_date_str = params.getDate2()+" "+params.getHour2()+":"+params.getMin2()+":00";
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            params.setStart_date(transFormat.parse(start_date_str));
            params.setEnd_date(transFormat.parse(end_date_str));
        }
        catch (ParseException e){
        }

        if(params.getShow_pc() == null)
            params.setShow_pc("N");
        if(params.getShow_mo() == null)
            params.setShow_mo("N");
        if(insertedId == 0)
            insertedId = mainService.insertPopup(params);
        else
            insertedId = mainService.updatePopup(params);

        if(insertedId > 0){
            MessageModel message = new MessageModel("해당 내용이 등록 되었습니다", "/AdminMain/popupList", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            MessageModel message = new MessageModel("등록에 실패했습니다.", "/AdminMain/popupList", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    //ajax 컬럼 업데이트
    @RequestMapping("/AdminMain/updatePopupValue") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> updatePopupValue(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        String idx = (String)params.get("idx");
        String value = (String)params.get("value");
        String type = (String)params.get("type");

        int result = 0;
        PopupModel tempParams = new PopupModel();
        tempParams.setIdx(Integer.parseInt(idx));
        if("del_yn".equals(type))
            tempParams.setDel_yn(value);

        result = mainService.updatePopup(tempParams);

        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<String, Object>();

        if(result <= 0)
            data.put("result", "fail");
        else
            data.put("result", "success");

        return data;
    }

    //팝업상세화면
    @RequestMapping("/AdminMain/popupView")
    public String popupView(@ModelAttribute("params") final PopupModel params, HttpServletRequest request, Model model){
        PopupModel data = mainService.getPopupOne(params);
        if(data == null)
            data = new PopupModel();
        model.addAttribute("data", data);
        return AuthCheck(1, "admin/content/popupView", request, model);
    }
}
