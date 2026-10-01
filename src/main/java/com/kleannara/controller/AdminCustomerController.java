package com.kleannara.controller;

import com.google.gson.Gson;
import com.kleannara.model.*;
import com.kleannara.paging.paging.PagingResponse;
import com.kleannara.service.AdminFaqService;
import com.kleannara.service.AdminVocService;
import com.kleannara.service.EmailService;
import com.kleannara.util.CommonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminCustomerController extends AdminCommon {
    @Autowired
    AdminFaqService faqService;
    @Autowired
    AdminVocService vocService;

    @Autowired
    EmailService emailService;

    @Value("${email.link1}")
    private String link1;

    @Value("${email.link2}")
    private String link2;

    /**
     * faq 시작
     */
    //faq리스트화면
    @RequestMapping("/AdminCustomer")
    public String adminMain(Model model){
        return "redirect:/AdminCustomer/faqList";
    }

    //faq리스트화면
    @RequestMapping("/AdminCustomer/faqList")
    public String faqList(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){
        params.setRecordSize(100);
        PagingResponse<FaqModel> response = faqService.getFaqList(params);
        model.addAttribute("response", response);
        return AuthCheck(6, "admin/content/faqList", request, model);
    }

    //faq추가 및 업데이트
    @RequestMapping("/AdminCustomer/insertFaq")
    public String insertFaq(@ModelAttribute("params") final FaqModel params, HttpServletRequest request, Model model){

        //db
        int insertedId = params.getIdx();
        if(insertedId == 0)
            insertedId = faqService.insertFaq(params);
        else
        	params.setShow_order(0);
            insertedId = faqService.updateFaq(params);

        if(insertedId > 0){
            MessageModel message = new MessageModel("해당 내용이 등록 되었습니다", "/AdminCustomer/faqList", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            MessageModel message = new MessageModel("등록에 실패했습니다.", "/AdminCustomer/faqList", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    //faq전체순서변경
    @RequestMapping("/AdminCustomer/updateFaqOrder") //ajax 일때만 []
    public String updateFaqOrder(HttpServletRequest request, HttpServletResponse response, Model model){

        String[] idx_arr = (String[])request.getParameterValues("idx[]");
        int order = 1;
        int result = 0;
        for (String idx : idx_arr) {
            FaqModel tempParams = new FaqModel();
            tempParams.setIdx(Integer.parseInt(idx));
            tempParams.setShow_order(order);
            order++;

            result += faqService.updateFaq(tempParams);
        }

        String resultStr = "해당 내용이 등록 되었습니다.";
        if(result <= 0)
            resultStr = "수정에 실패했습니다.";

        MessageModel message = new MessageModel(resultStr, "/AdminCustomer/faqList", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    //ajax 컬럼 업데이트
    @RequestMapping("/AdminCustomer/updateFaqValue") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> updateFaqValue(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        String idx = (String)params.get("idx");
        String value = (String)params.get("value");
        String type = (String)params.get("type");

        int result = 0;
        FaqModel tempParams = new FaqModel();
        tempParams.setIdx(Integer.parseInt(idx));
        if("del_yn".equals(type))
            tempParams.setDel_yn(value);

        result = faqService.updateFaq(tempParams);

        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<String, Object>();

        if(result <= 0)
            data.put("result", "fail");
        else
            data.put("result", "success");

        return data;
    }

    //faq상세화면
    @RequestMapping("/AdminCustomer/faqView")
    public String faqView(@ModelAttribute("params") final FaqModel params, HttpServletRequest request, Model model){
        FaqModel data = faqService.getFaqOne(params);
        if(data == null)
            data = new FaqModel();
        model.addAttribute("data", data);
        return AuthCheck(6, "admin/content/faqView", request, model);
    }

    /**
     * voc 시작
     */
    //voc리스트화면
    @RequestMapping("/AdminCustomer/vocList")
    public String vocList(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){
        if(params.getType() == null || "".equals(params.getType())) {
            params.setType("2");
        }
        //원본 타입
        String origType = params.getType();
        String origReqType = params.getReq_type();
        if(origReqType != null) {
            int reqTypeInt = Integer.parseInt(origReqType);
            if(reqTypeInt > 4) {
                reqTypeInt = reqTypeInt - 4;
                params.setReq_type(reqTypeInt + "");
                params.setType("3");
            }
            else {
                params.setType("2");
            }
        }
        PagingResponse<VocModel> response = vocService.getVocList(params);
        model.addAttribute("response", response);

        //필터 파라미터 원복
        params.setReq_type(origReqType);
        params.setType(origType);

        return AuthCheck(6, "admin/content/vocList", request, model);
    }

    //voc 과거건
    @RequestMapping("/AdminCustomer/asisVocList")
    public String asisVocList(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){
        //원본 타입
        PagingResponse<VocAsisModel> response = vocService.getVocAsisList(params);
        model.addAttribute("response", response);

        return AuthCheck(6, "admin/content/vocAsisList", request, model);
    }

    //voc추가 및 업데이트
    @RequestMapping("/AdminCustomer/insertVoc")
    public String insertVoc(@ModelAttribute("params") final VocModel params, HttpServletRequest request, Model model){

        //db
        int insertedId = params.getIdx();
        if(insertedId == 0)
            insertedId = vocService.insertVoc(params);
        else
            insertedId = vocService.updateVoc(params);

        //응답완료 - 이메일전송
        if("3".equals(params.getDm_status())){

            VocModel tempRow = vocService.getVocOne(params);

            MailModel mailParams = new MailModel();
            mailParams.setTitle("[깨끗한나라] 고객님의 문의사항에 답변드립니다.");
            mailParams.setAddress(tempRow.getEmail());
            mailParams.setTemplate("email/voc.html");
            Map<String, Object> variables = new HashMap<>();
            variables.put("link1", link1);
            variables.put("link2", link2);
            variables.put("name", tempRow.getName());
            variables.put("question", tempRow.getContent());
            variables.put("answer", params.getDm_text());
            mailParams.setVariables(variables);
            mailParams.setFrom("knhp@kleannara.com");
            try {
                emailService.sendEmail(mailParams);
            }
            catch (Exception e){
            }
            finally {
            }
        }

        if(insertedId > 0){
            MessageModel message = new MessageModel("해당 내용이 등록 되었습니다", "/AdminCustomer/vocList?type=2", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            MessageModel message = new MessageModel("등록에 실패했습니다.", "/AdminCustomer/vocList?type=2", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    //ajax 컬럼 업데이트
    @RequestMapping("/AdminCustomer/updateVocValue") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> updateVocValue(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){
        String idxs = (String)params.get("idx");
        String[] idx_arr = {};
        if(idxs != null )
            idx_arr = idxs.split(",");
        String value = (String)params.get("value");
        String type = (String)params.get("type");

        int result = 0;
        for (String idx : idx_arr) {
            VocModel tempParams = new VocModel();
            tempParams.setIdx(Integer.parseInt(idx));
            if ("del_yn".equals(type))
                tempParams.setDel_yn(value);

            result = vocService.updateVoc(tempParams);
        }

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
        		adminLog.setServiceTarget(idxs);

        		this.setAdminLog(request, adminLog);
        	}
        }

        return data;
    }

    //voc상세화면
    @RequestMapping("/AdminCustomer/vocView")
    public String vocView(@ModelAttribute("params") final VocModel params, HttpServletRequest request, Model model){
        VocModel data = vocService.getVocOne(params);
        if(data == null)
            data = new VocModel();

        VocFileModel tempParam = new VocFileModel();
        tempParam.setP_idx(params.getIdx());
        List<VocFileModel> files = vocService.getVocFileList(tempParam);
        model.addAttribute("data", data);
        model.addAttribute("files", files);
        return AuthCheck(6, "admin/content/vocView", request, model);
    }

    // asis voc상세화면
    @RequestMapping("/AdminCustomer/asisVocView")
    public String asisVocView(@ModelAttribute("params") final VocAsisModel params, HttpServletRequest request, Model model){
        VocAsisModel data = vocService.getVocAsisOne(params);
        if(data == null)
            data = new VocAsisModel();

        model.addAttribute("data", data);
        return AuthCheck(6, "admin/content/vocAsisView", request, model);
    }

    @RequestMapping("/Test/mail")
    @ResponseBody
    public String testmail(Model model){

        MailModel params = new MailModel();
        params.setTitle("[깨끗한나라] 고객님의 문의사항에 답변드립니다.");
        params.setAddress("seobyj@naver.com");
        params.setTemplate("email/voc.html");
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "이름");
        variables.put("question", "질문<br>질문");
        variables.put("answer", "답변<br>답변");
        params.setVariables(variables);
        params.setFrom("knhp@kleannara.com");
        String result = "";
        try {
            emailService.sendEmail(params);
        }
        catch (Exception e){
            result += e.toString();
        }
        finally {
            result += "finally";
            return result;
        }
    }
}
