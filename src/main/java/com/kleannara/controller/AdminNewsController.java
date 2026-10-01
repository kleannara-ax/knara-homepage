package com.kleannara.controller;

import com.google.gson.Gson;
import com.kleannara.model.FileModel;
import com.kleannara.model.NewsModel;
import com.kleannara.model.MessageModel;
import com.kleannara.model.SearchModel;
import com.kleannara.paging.paging.PagingResponse;
import com.kleannara.service.AdminNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AdminNewsController extends AdminCommon {

    @Autowired
    AdminNewsService newsService;

    //뉴스리스트화면
    @RequestMapping("/AdminNews")
    public String adminMain(Model model){
        return "redirect:/AdminNews/newsList?type=1";
    }

    //뉴스리스트화면
    @RequestMapping("/AdminNews/newsList")
    public String newsList(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){
        if(params.getType() == null || "".equals(params.getType())) {
            params.setType("1");
        }
        PagingResponse<NewsModel> response = newsService.getNewsList(params);
        model.addAttribute("response", response);
        return AuthCheck(5, "admin/content/newsList", request, model);
    }

    //뉴스추가 및 업데이트
    @RequestMapping("/AdminNews/insertNews")
    public String insertNews(@ModelAttribute("params") final NewsModel params, HttpServletRequest request, Model model){
        //이미지 업로드
        FileModel fileModel1 = null;
        FileModel fileModel2 = null;
        FileModel fileModel3 = null;
        try {
            //초기정보 파라미터 셋팅
            params.setThumb_name(params.getPre_thumb_name());
            params.setThumb_path(params.getPre_thumb_path());
            params.setFile_name(params.getPre_file_name());
            params.setFile_path(params.getPre_file_path());
            params.setAttach_name(params.getPre_attach_name());
            params.setAttach_path(params.getPre_attach_path());
            //저장
            if(params.getThumb() != null)
                fileModel1 = fileService.saveFile(params.getThumb(), "news");
            if(params.getFile() != null)
                fileModel2 = fileService.saveFile(params.getFile(), "news");
            if(params.getAttach() != null)
                fileModel3 = fileService.saveFile(params.getAttach(), "news");
            //업로드 파일 셋팅
            if(fileModel1 != null) {
                params.setThumb_name(fileModel1.getFileOrig());
                params.setThumb_path(fileModel1.getFileName());
            }
            if(fileModel2 != null) {
                params.setFile_name(fileModel2.getFileOrig());
                params.setFile_path(fileModel2.getFileName());
            }
            if(fileModel3 != null) {
                params.setAttach_name(fileModel3.getFileOrig());
                params.setAttach_path(fileModel3.getFileName());
            }
        } catch (IOException e){
            MessageModel message = new MessageModel("이미지 업로드 실패", "/AdminBrand/brandView?"+"idx="+params.getIdx(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        //db
        HttpSession session = request.getSession();
        String idx = session.getAttribute("admin_idx")+"";
        params.setReg_member(idx);
        int insertedId = params.getIdx();
        if(insertedId == 0)
            insertedId = newsService.insertNews(params);
        else
            insertedId = newsService.updateNews(params);

        if(insertedId > 0){
            String title = "공지사항이";
            if("2".equals(params.getType()))
                title = "언론보도가";
            else if("3".equals(params.getType()))
                title = "홍보자료가";
            MessageModel message = new MessageModel("해당 내용이 등록 되었습니다", "/AdminNews/newsList?type="+params.getType(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            MessageModel message = new MessageModel("등록에 실패했습니다.", "/AdminNews/newsList?type="+params.getType(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    //ajax 컬럼 업데이트
    @RequestMapping("/AdminNews/updateNewsValue") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> updateNewsValue(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        String idx = (String)params.get("idx");
        String value = (String)params.get("value");
        String type = (String)params.get("type");

        int result = 0;
        NewsModel tempParams = new NewsModel();
        tempParams.setIdx(Integer.parseInt(idx));
        if("del_yn".equals(type))
            tempParams.setDel_yn(value);

        result = newsService.updateNews(tempParams);

        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<String, Object>();

        if(result <= 0)
            data.put("result", "fail");
        else
            data.put("result", "success");

        return data;
    }

    //뉴스상세화면
    @RequestMapping("/AdminNews/newsView")
    public String newsView(@ModelAttribute("params") final NewsModel params, HttpServletRequest request, Model model){
        NewsModel data = newsService.getNewsOne(params);
        if(data == null)
            data = new NewsModel();
        model.addAttribute("data", data);
        return AuthCheck(5, "admin/content/newsView", request, model);
    }
}
