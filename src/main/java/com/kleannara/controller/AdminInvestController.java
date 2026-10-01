package com.kleannara.controller;

import com.google.gson.Gson;
import com.kleannara.model.*;
import com.kleannara.paging.paging.PagingResponse;
import com.kleannara.service.AdminReportService;
import com.kleannara.service.AdminVocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AdminInvestController extends AdminCommon {

    @Autowired
    AdminReportService reportService;

    /**
     * 투자정보
     */
    //투자정보리스트화면
    @RequestMapping("/AdminInvest")
    public String adminMain(Model model){
        return "redirect:/AdminInvest/reportList";
    }

    //투자정보리스트화면
    @RequestMapping("/AdminInvest/reportList")
    public String reportList(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){
        if(params.getType() == null || "".equals(params.getType())) {
            params.setType("1");
        }
        PagingResponse<ReportModel> response = reportService.getReportList(params);
        model.addAttribute("response", response);
        return AuthCheck(4, "admin/content/reportList", request, model);
    }

    //투자정보추가 및 업데이트
    @RequestMapping("/AdminInvest/insertReport")
    public String insertReport(@ModelAttribute("params") final ReportModel params, HttpServletRequest request, Model model){

        if(params.getTop_yn() == null)
            params.setTop_yn("N");

        //이미지 업로드
        FileModel fileModel1 = null;
        try {
            //초기정보 파라미터 셋팅
            params.setFile_name(params.getPre_file_name());
            params.setFile_path(params.getPre_file_path());
            //저장
            fileModel1 = fileService.saveFile(params.getFile(), "report");
            //업로드 파일 셋팅
            if(fileModel1 != null) {
                params.setFile_name(fileModel1.getFileOrig());
                params.setFile_path(fileModel1.getFileName());
            }
        } catch (IOException e){
            MessageModel message = new MessageModel("이미지 업로드 실패", "/AdminInvest/reportView?type="+params.getType()+"&idx="+params.getIdx(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        //db
        HttpSession session = request.getSession();
        String idx = session.getAttribute("admin_idx")+"";
        params.setReg_member(idx);
        int insertedId = params.getIdx();
        if(insertedId == 0)
            insertedId = reportService.insertReport(params);
        else
            insertedId = reportService.updateReport(params);

        if(insertedId > 0){
            MessageModel message = new MessageModel("해당 내용이 등록 되었습니다", "/AdminInvest/reportList?type="+params.getType(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            MessageModel message = new MessageModel("등록에 실패했습니다.", "/AdminInvest/reportList?type="+params.getType(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    //ajax 컬럼 업데이트
    @RequestMapping("/AdminInvest/updateReportValue") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> updateReportValue(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        String idx = (String)params.get("idx");
        String value = (String)params.get("value");
        String type = (String)params.get("type");

        int result = 0;
        ReportModel tempParams = new ReportModel();
        tempParams.setIdx(Integer.parseInt(idx));
        if("del_yn".equals(type))
            tempParams.setDel_yn(value);

        result = reportService.updateReport(tempParams);

        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<String, Object>();

        if(result <= 0)
            data.put("result", "fail");
        else
            data.put("result", "success");

        return data;
    }

    //투자정보상세화면
    @RequestMapping("/AdminInvest/reportView")
    public String reportView(@ModelAttribute("params") final ReportModel params, HttpServletRequest request, Model model){
        ReportModel data = null;
        if(params.getIdx() != 0)
            data = reportService.getReportOne(params);
        if(data == null)
            data = new ReportModel();
        model.addAttribute("data", data);
        return AuthCheck(4, "admin/content/reportView", request, model);
    }
}
