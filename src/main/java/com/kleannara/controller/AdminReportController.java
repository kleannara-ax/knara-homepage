package com.kleannara.controller;

import com.google.gson.Gson;
import com.kleannara.model.*;
import com.kleannara.paging.paging.PagingResponse;
import com.kleannara.service.AdminReportService;
import com.kleannara.service.AdminVocService;
import com.kleannara.service.EmailService;
import com.kleannara.util.CommonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class AdminReportController extends AdminCommon {

    private static final Logger logger = LoggerFactory.getLogger("AdminReportController");
	
    @Autowired
    AdminReportService reportService;

    @Autowired
    AdminVocService vocService;

    @Autowired
    EmailService emailService;

    @Value("${email.link1}")
    private String link1;

    @Value("${email.link2}")
    private String link2;

    /**
     * 리포트
     */
    //리포트리스트화면
    @RequestMapping("/AdminReport")
    public String adminMain(Model model){
        return "redirect:/AdminReport/vocList";
    }

    //리포트리스트화면
    @RequestMapping("/AdminReport/reportList")
    public String reportList(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){
        if(params.getType() == null || "".equals(params.getType())) {
            params.setType("1");
        }
        params.setSYear("ALL");
        PagingResponse<ReportModel> response = reportService.getReportList(params);
        model.addAttribute("response", response);
        return AuthCheck(3, "admin/content/reportList", request, model);
    }

    //리포트추가 및 업데이트
    @RequestMapping("/AdminReport/insertReport")
    public String insertReport(@ModelAttribute("params") final ReportModel params, HttpServletRequest request, Model model){

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
            MessageModel message = new MessageModel("이미지 업로드 실패", "/AdminReport/reportView?type="+params.getType()+"&idx="+params.getIdx(), RequestMethod.GET, null);
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
            MessageModel message = new MessageModel("해당 내용이 등록 되었습니다", "/AdminReport/reportList?type="+params.getType(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            MessageModel message = new MessageModel("등록에 실패했습니다.", "/AdminReport/reportList?type="+params.getType(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    //ajax 컬럼 업데이트
    @RequestMapping("/AdminReport/updateReportValue") //ajax 일때만 []
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

    //리포트상세화면
    @RequestMapping("/AdminReport/reportView")
    public String reportView(@ModelAttribute("params") final ReportModel params, HttpServletRequest request, Model model){
        ReportModel data = null;
        if(params.getIdx() != 0)
            data = reportService.getReportOne(params);
        if(data == null)
            data = new ReportModel();
        model.addAttribute("data", data);
        return AuthCheck(3, "admin/content/reportView", request, model);
    }

    /**
     * voc
     */
    //voc리스트화면
    @RequestMapping("/AdminReport/vocList")
    public String vocList(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){
        if(params.getType() == null || "".equals(params.getType())) {
            params.setType("1");
        }

        PagingResponse<VocModel> response = vocService.getVocList(params);
        model.addAttribute("response", response);
        
        if (true) {
    		AdminLogModel adminLog = new AdminLogModel();
    		adminLog.setType("R");
    		adminLog.setMemo(params.toStringAdminLog("vocList"));
    		
    		this.setAdminLog(request, adminLog);
    	}
        
        return AuthCheck(3, "admin/content/vocList", request, model);
    }

    //voc추가 및 업데이트
    @RequestMapping("/AdminReport/insertVoc")
    public String insertVoc(@ModelAttribute("params") final VocModel params, HttpServletRequest request, Model model){

        //db
        HttpSession session = request.getSession();
        String adminId = session.getAttribute("admin_id")+"";
        String adminName = session.getAttribute("admin_name")+"";
        params.setDm_id(adminId);
        params.setDm_name(adminName);
        params.setDm_ip(CommonUtil.getRemoteIP(request));	        //사용자 ip
        
        int insertedId = 0; 
        
        if (params.getIdx() == 0) {
        	insertedId = vocService.insertVoc(params);
        } else {
            // p_idx 할당
        	int pIDX = params.getIdx();
            params.setP_idx(pIDX);
            insertedId = pIDX;
            
            vocService.updateVoc(params);        	
        }
        
        // 관리자 응답 내용 History 기능 추가 - 2024/06/13 강지선
        int answersID = vocService.insertVocAnswers(params);

        // insertVocAnswers 성공 & 이메일 발송 여부 확인해서 발송
        if (answersID > 0) {
        	if ("Y".equals(params.getEmailYN())) {
        		params.setIdx(insertedId);
                VocModel tempRow = vocService.getVocOne(params);

                if(!("".equals(tempRow.getEmail()) || null == tempRow.getEmail())) {
                    MailModel mailParams = new MailModel();
                    mailParams.setTitle("[깨끗한나라] 고객님의 문의사항에 답변드립니다.");
                    mailParams.setAddress(tempRow.getEmail());
                    mailParams.setTemplate("email/voc.html");
                    
                    Map<String, Object> variables = new HashMap<>();
                    variables.put("link1", link1);
                    variables.put("link2", link2);
                    variables.put("name", tempRow.getName());
//                    variables.put("question", tempRow.getContent());
//                    variables.put("answer", params.getDm_text());
                    
                    // 본문의 줄바꿈 문자를 <br> 치환 - 2024/06/24 강지선
                    String questionWithLineBreaks = tempRow.getContent().replace("\r\n", "<br />").replace("\n", "<br />");
                    variables.put("question", questionWithLineBreaks);
                    
                    String answerWithLineBreaks = params.getDm_text().replace("\r\n", "<br />").replace("\n", "<br />");
                    variables.put("answer", answerWithLineBreaks);
                    
                    mailParams.setVariables(variables);
                    mailParams.setFrom("knhp@kleannara.com");
                    
                    try {
                        emailService.sendEmail(mailParams);
                    } catch (Exception e) {
                    } finally {
                    }
                }
            }        	
        }

        if(insertedId > 0){
        	if (true) {
        		AdminLogModel adminLog = new AdminLogModel();
        		adminLog.setType("C");
        		adminLog.setServiceTarget(String.valueOf(insertedId));
        		adminLog.setMemo(params.toStringAdminLog("insertVoc"));
        		
        		this.setAdminLog(request, adminLog);
        	}
        	
            MessageModel message = new MessageModel("해당 내용이 등록 되었습니다", "/AdminReport/vocList", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            MessageModel message = new MessageModel("등록에 실패했습니다.", "/AdminReport/vocList", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    //ajax 컬럼 업데이트
    @RequestMapping("/AdminReport/updateVocValue") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> updateVocValue(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        String idx = (String)params.get("idx");
        String value = (String)params.get("value");
        String type = (String)params.get("type");

        int result = 0;
        VocModel tempParams = new VocModel();
        tempParams.setIdx(Integer.parseInt(idx));
        if("del_yn".equals(type))
            tempParams.setDel_yn(value);

        result = vocService.updateVoc(tempParams);

        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<String, Object>();

        if(result <= 0) {
        	data.put("result", "fail");
        }
        else {
        	data.put("result", "success");
        	
        	if (true) {
        		AdminLogModel adminLog = new AdminLogModel();
        		adminLog.setType("D");
        		adminLog.setServiceTarget(String.valueOf(idx));

        		this.setAdminLog(request, adminLog);
        	}
        }

        return data;
    }

    //voc상세화면
    @RequestMapping("/AdminReport/vocView")
    public String vocView(@ModelAttribute("params") final VocModel params, HttpServletRequest request, Model model){
        VocModel data = vocService.getVocOne(params);
        if(data == null) {
            data = new VocModel();
            data.setType("1");
        }
        
        VocFileModel tempParam = new VocFileModel();
        tempParam.setP_idx(params.getIdx());
        List<VocFileModel> files = vocService.getVocFileList(tempParam);
        
        // 답변 목록 조회 - 2024/06/13 강지선
        params.setP_idx(data.getIdx());
        List<VocModel> vocAnswers = vocService.getVocAnswers(params);
        
        // 조회 로그 기록
        if (true) {
        	AdminLogModel adminLog = new AdminLogModel();
        	adminLog.setType("R");
    		adminLog.setServiceTarget(String.valueOf(params.getIdx()));
        	adminLog.setMemo(params.toStringAdminLog("vocView"));
        	
        	this.setAdminLog(request, adminLog);
        }
        
        model.addAttribute("data", data);
        model.addAttribute("files", files);
        model.addAttribute("vocAnswers", vocAnswers);
        return AuthCheck(3, "admin/content/vocView", request, model);
    }
    
    //ajax 파일다운로드 로깅
    @RequestMapping("/AdminReport/fileDownloadLog")
    @ResponseBody
    public Map<String, Object> fileDownloadLog(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        String idx = (String)params.get("idx");
        String fileName = (String)params.get("fileName");

        int result = 0;
        VocModel tempParams = new VocModel();
        tempParams.setIdx(Integer.parseInt(idx));
        tempParams.setFile_name(fileName);

        // 조회 로그 기록
        if (true) {
        	AdminLogModel adminLog = new AdminLogModel();
        	adminLog.setType("R");
    		adminLog.setServiceTarget(idx);
        	adminLog.setMemo(tempParams.toStringAdminLog("fileDownloadLog"));
        	
        	result = this.setAdminLog(request, adminLog);
        }

        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<String, Object>();

        if(result <= 0)
            data.put("result", "fail");
        else
            data.put("result", "success");

        return data;
    }
}
