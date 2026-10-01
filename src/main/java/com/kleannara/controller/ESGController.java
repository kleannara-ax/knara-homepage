package com.kleannara.controller;

import com.google.gson.Gson;
import com.kleannara.config.CustomPasswordEncoding;
import com.kleannara.service.EmailService;
import com.kleannara.util.CommonUtil;
import com.kleannara.model.*;
import com.kleannara.paging.paging.PagingResponse;
import com.kleannara.service.AdminReportService;
import com.kleannara.service.AdminVocService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class ESGController extends FrontCommon {
    private static final Logger logger = LoggerFactory.getLogger("ESGController");
    private final AdminVocService vocService;
    private final AdminReportService reportService;

    @Autowired
    EmailService emailService;

    @Autowired
    private CustomPasswordEncoding customPasswordEncoder;

    @Value("${email.link1}")
    private String link1;

    @Value("${email.link2}")
    private String link2;

    @RequestMapping(value = {"/ESG"})
    public String main(Model model){
        return "redirect:/ESG/environment";
    }

    //환경경영
    @RequestMapping("/ESG/environment")
    public String environment(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/esg_environment");
    }

    //사회적책임경영
    @RequestMapping("/ESG/social")
    public String social(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/esg_social");
    }

    //지배구조
    @RequestMapping("/ESG/governance")
    public String governance(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/esg_governance");
    }

    //윤리경영소개 및 정책
    @RequestMapping("/ESG/ethics")
    public String ethics(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/esg_ethics");
    }

    //부조리신고센터
    @RequestMapping("/ESG/voc")
    public String voc(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/esg_voc");
    }

    //부조리신고센터 등록
    @RequestMapping("/ESG/vocView")
    public String vocView(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/esg_vocView");
    }

    //부조리신고센터 등록
    @RequestMapping("/ESG/insertVoc")
    public String insertVoc(@ModelAttribute("params") final VocModel params, HttpServletRequest request, Model model){

        //부조리신고센터 타입
        params.setType("1");
        //사용자 ip
        params.setReg_ip(CommonUtil.getRemoteIP(request));

        if(params.getTitle() == null || "".equals(params.getTitle())){
            MessageModel message = new MessageModel("등록에 실패했습니다.[필수정보 누락]", "/ESG/vocView", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        String pw = params.getPassword();
        if(params.getPassword() != null && !"".equals(params.getPassword()))
            params.setPassword(customPasswordEncoder.sha256Encoding(pw));

        //db
        int insertedId = params.getIdx();
        if(insertedId == 0)
            insertedId = vocService.insertVoc(params);
        else
            insertedId = vocService.updateVoc(params);

        MultipartFile[] files = params.getFiles();
        for(MultipartFile file : files){
            //이미지 업로드
            FileModel fileModel = null;
            VocFileModel vocFileModel = new VocFileModel();
            try {
                //저장
                fileModel = fileService.saveFile(file, "voc");
                //업로드 파일 셋팅
                if(fileModel != null) {
                    vocFileModel.setFile_name(fileModel.getFileOrig());
                    vocFileModel.setFile_path(fileModel.getFileName());
                    vocFileModel.setP_idx(params.getIdx());

                    int tempInsertedId = vocService.insertVocFile(vocFileModel);
                }
            } catch (IOException e){
                MessageModel message = new MessageModel("이미지 업로드 실패", "/ESG/vocView", RequestMethod.GET, null);
                return showMessageAndRedirect(message, model);
            }
        }

        if(insertedId > 0){
            MessageModel message = new MessageModel("해당 내용이 등록 되었습니다", "/ESG/voc", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            MessageModel message = new MessageModel("등록에 실패했습니다.", "/ESG/vocView", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    //ajax 컬럼 업데이트
    @RequestMapping("/ESG/vocEmailToEmployment") //ajax 일때만 []
    @ResponseBody
    public Map<String, Object> vocEmailToEmployment(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){

        String name = (String)params.get("name");
        String question = (String)params.get("question");

        if(null == question || "".equals(question)){
            return null;
        }

        /*
        성현도 책임 진실경영팀
        01033043501  /  02-2270-9261
        hdsoung@kleannara.com

        권기빈 선임 진실경영팀
        01031738965  /  02-2270-9262
        gbkwon@kleannara.com

        박종희 사원 진실경영팀
        01087741509  /  02-2270-9239
        parkjh@kleannara.com

        윤서준 사원 진실경영팀	=> 2024/10/07 퇴사
        01063485819  /  02-2270-9233
        sjyoon@kleannara.com
         */
        String[] emails = new String[]{"csmoon@kleannara.com", "parkjh@kleannara.com"};	// sjyoon@kleannara.com	=> 2024/10/07 퇴사
//      String[] emails = new String[]{"hdsoung@kleannara.com", "gbkwon@kleannara.com", "parkjh@kleannara.com", "sjyoon@kleannara.com"};
//       String[] emails = new String[]{"seobyj@naver.com", "seobyjj@gmail.com"};

        for(String email : emails){
            MailModel mailParams = new MailModel();
            mailParams.setTitle("[깨끗한나라] 고객님의 문의사항이 접수됐습니다.");
            mailParams.setAddress(email);
            mailParams.setTemplate("email/voc2.html");
            Map<String, Object> variables = new HashMap<>();
            variables.put("link1", link1);
            variables.put("link2", link2);
            variables.put("name", name);
            variables.put("question", question);
            mailParams.setVariables(variables);
            mailParams.setFrom("knhp@kleannara.com");
            try {
                emailService.sendEmail(mailParams);
            } catch (Exception e) {
                logger.error(e.toString());
            } finally {
                logger.info("email:"+email);
            }
        }

        return null;
    }

    //부조리신고센터 리스트
    @RequestMapping("/ESG/vocList_OLD")
    public String vocList_OLD(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){

        params.setType("1");
        PagingResponse<VocModel> response = vocService.getVocList(params);
        //비밀번호 맞는 항목만 출력
        List<VocModel> new_list = new ArrayList<>();
        for(int i = 0; i < response.getList().size(); i++){
            VocModel tempModel = response.getList().get(i);

            try {
                if (customPasswordEncoder.sha256Matching(params.getPassword(), tempModel.getPassword()))
                    new_list.add(tempModel);
            }
            catch(Exception e){}
        }

        PagingResponse<VocModel> response2 = new PagingResponse<>(new_list, response.getPagination());
        model.addAttribute("response", response2);

        getGnbInfo(model);
        return getViewPath(request, "front/content/esg_vocList");
    }

    //부조리신고센터 > 답변 리스트 포함
    @RequestMapping("/ESG/vocList")
    public String vocList(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){
    	
    	VocModel answerParams = new VocModel();
    	
        params.setType("1");
        PagingResponse<VocModel> response = vocService.getVocList(params);
        
        //비밀번호 맞는 항목만 출력
        List<VocModel> new_list = new ArrayList<>();
        for(int i = 0; i < response.getList().size(); i++){
            VocModel tempModel = response.getList().get(i);
            
//            String questionWithLineBreaks = tempModel.getContent().replace("\r\n", "<br />").replace("\n", "<br />");
//            tempModel.setContent(questionWithLineBreaks);
            
            try {
                if (customPasswordEncoder.sha256Matching(params.getPassword(), tempModel.getPassword())) {
                	// voc_answers 조회 - 2024/06/14 강지선
                    int pIdx = tempModel.getIdx();
                    answerParams.setP_idx(pIdx);
                    List<VocModel> vocAnswers = vocService.getVocAnswers(answerParams);
                    
//                    for(int j = 0; j < vocAnswers.size() ; j++) {
//                        VocModel tempAnswer = vocAnswers.get(j);
//                        
//                        String answerWithLineBreaks = tempAnswer.getDm_text().replace("\r\n", "<br />").replace("\n", "<br />");
//                        tempAnswer.setContent(answerWithLineBreaks);
//                    	
//                        vocAnswers.set(i, tempAnswer);
//                    }
                    
                    // 답변 목록을 return Object에 포함
                    tempModel.setVocAnswers(vocAnswers);
                    
                    new_list.add(tempModel);                	
                }
            }
            catch(Exception e){}
        }

        PagingResponse<VocModel> response2 = new PagingResponse<>(new_list, response.getPagination());
        model.addAttribute("response", response2);

        getGnbInfo(model);
        return getViewPath(request, "front/content/esg_vocList");
    }
    
    //부조리신고센터 나의항목
    @RequestMapping("/ESG/myVoc")
    public String myVoc(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/esg_myVoc");
    }

    //동반성장
    @RequestMapping("/ESG/accompany")
    public String accompany(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/esg_accompany");
    }

    //고객만족경영
    @RequestMapping("/ESG/customer")
    public String customer(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/esg_customer");
    }

    //지속가능경영보고서
    @RequestMapping("/ESG/sustainable")
    public String sustainable(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){
        params.setType("1");
        params.setShow_yn("Y");
        PagingResponse<ReportModel> response = reportService.getReportList(params);
        model.addAttribute("response", response);

        getGnbInfo(model);
        return getViewPath(request, "front/content/esg_sustainable");
    }
    
    // 지속가능경영보고서 > 연도별 리스트
    @RequestMapping("/ESG/sustainableSub") //ajax 일때만 []
    @ResponseBody
    public PagingResponse<ReportModel> sustainableSub(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){

        params.setType("1");
        params.setShow_yn("Y");
        
        PagingResponse<ReportModel> response = reportService.getReportList(params);

        return response;
    }
}
