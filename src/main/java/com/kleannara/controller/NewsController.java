package com.kleannara.controller;

import com.kleannara.util.CommonUtil;
import com.kleannara.model.NewsModel;
import com.kleannara.model.SearchModel;
import com.kleannara.paging.paging.PagingResponse;
import com.kleannara.service.AdminNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NewsController extends FrontCommon {

    @Value("#{environment['config.file.dir']}")
    private String fileDir;

    private final AdminNewsService newsService;

    @RequestMapping(value = {"/News"})
    public String main(Model model){
        return "redirect:/News/pressList";
    }

    @RequestMapping("/News/noticeList")
    public String noticeList(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){

        NewsModel top = newsService.getNewsTopOne();
        model.addAttribute("top", top);

        if(params.getType() == null || "".equals(params.getType())) {
            params.setType("1");
        }
        params.setShow_yn("Y");
        params.setRecordSize(12);
        PagingResponse<NewsModel> response = newsService.getNewsList(params);
        model.addAttribute("response", response);

        getGnbInfo(model);
        return getViewPath(request, "front/content/news_noticeList");
    }

    @RequestMapping("/News/pressList")
    public String pressList(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){

        params.setType("2");
        params.setShow_yn("Y");
        params.setRecordSize(12);
        PagingResponse<NewsModel> response = newsService.getNewsList(params);
        model.addAttribute("response", response);

        getGnbInfo(model);
        return getViewPath(request, "front/content/news_pressList");
    }

    @RequestMapping("/News/adList")
    public String adList(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){

        params.setType("3");
        params.setShow_yn("Y");
        params.setRecordSize(12);
        PagingResponse<NewsModel> response = newsService.getNewsList(params);
        model.addAttribute("response", response);

        getGnbInfo(model);
        return getViewPath(request, "front/content/news_adList");
    }

    @RequestMapping("/News/noticeView")
    public String noticeView(@ModelAttribute("params") final NewsModel params, HttpServletRequest request, Model model){

        NewsModel data = newsService.getNewsOne(params);
        model.addAttribute("data", data);

        //readCount
        data.setRead_cnt(data.getRead_cnt() + 1);
        newsService.updateNews(data);

        //이전,다음
        int prev_idx=0;
        String prev_title="";
        int next_idx=0;
        String next_title="";
        List<NewsModel> nextList = newsService.getNewsNext(params);
        for(NewsModel row : nextList){
            if(row.getIdx() < data.getIdx()){
                prev_idx = row.getIdx();
                prev_title = row.getTitle();
            }
            else if(row.getIdx() > data.getIdx()){
                next_idx = row.getIdx();
                next_title = row.getTitle();
            }
        }
        params.setPrev_idx(prev_idx+"");
        params.setPrev_title(prev_title);
        params.setNext_idx(next_idx+"");
        params.setNext_title(next_title);

        params.setTarget(CommonUtil.getDeviceType(request));

        getGnbInfo(model);
        return getViewPath(request, "front/content/news_noticeView");
    }

    @RequestMapping("/News/attach/{idx}")
    public ResponseEntity<Object> downloadAttach(@PathVariable int idx) throws MalformedURLException {

        NewsModel tempParams = new NewsModel();
        tempParams.setIdx(idx);
        NewsModel data = newsService.getNewsOne(tempParams);

        String savedPath = fileDir + "/news/"+data.getAttach_path();
        UrlResource resource = new UrlResource("file:" +  savedPath);
        String encodedFileName = UriUtils.encode(data.getAttach_name(), StandardCharsets.UTF_8);

        // 파일 다운로드 대화상자가 뜨도록 하는 헤더를 설정해주는 것
        // Content-Disposition 헤더에 attachment; filename="업로드 파일명" 값을 준다.
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition).body(resource);
    }
}
