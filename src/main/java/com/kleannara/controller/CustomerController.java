package com.kleannara.controller;

import com.kleannara.service.CustomerService;
import com.kleannara.util.CommonUtil;
import com.kleannara.model.*;
import com.kleannara.paging.paging.PagingResponse;
import com.kleannara.service.AdminFaqService;
import com.kleannara.service.AdminVocService;
import lombok.RequiredArgsConstructor;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerController extends FrontCommon {

    private static final Logger logger = LoggerFactory.getLogger("CustomerController");
    private final AdminFaqService faqService;
    private final AdminVocService vocService;
    private final CustomerService customerService;

    @Value("${voc.url_voc}")
    private String exVocUrl;
    @Value("${voc.url_attach}")
    private String exAttachUrl;
    @Value("${voc.url_survey}")
    private String exSurveyUrl;
    @Value("${voc.auth}")
    private String exAuth;
    @Value("${voc.user}")
    private String exUserKey;

    @RequestMapping(value = {"/Customer"})
    public String main(Model model){
        return "redirect:/Customer/customer";
    }

    @RequestMapping("/Customer/customer")
    public String customer(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){
        params.setLimit(5);
        params.setTop_yn("Y");
        PagingResponse<FaqModel> response = faqService.getFaqList(params);
        model.addAttribute("response", response);

        getGnbInfo(model);
        return getViewPath(request, "front/content/customer");
    }

    @RequestMapping("/Customer/faq")
    public String faq(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){
        PagingResponse<FaqModel> response = faqService.getFaqList(params);
        model.addAttribute("response", response);

        getGnbInfo(model);
        return getViewPath(request, "front/content/customer_faq");
    }

    @RequestMapping("/Customer/voc")
    public String voc(@ModelAttribute("params") final VocModel params, HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/customer_vocView");
    }

    //부조리신고센터 등록
    @RequestMapping("/Customer/insertVoc")
    public String insertVoc(@ModelAttribute("params") final VocModel params, HttpServletRequest request, Model model){

        JSONObject jsonObj = null;

        try{
            String serverUrl = exVocUrl;
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = this.makeRestTemplate();

            HttpHeaders restHeaders = new HttpHeaders();
            restHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            restHeaders.set("Authorization", exAuth);
            restHeaders.set("X-UserAuthKey", exUserKey);

            MultiValueMap<String, Object> restBody = new LinkedMultiValueMap<>();
            if("1".equals(params.getReq_type()))
                restBody.add("vocCd", "14");
            else if("2".equals(params.getReq_type()))
                restBody.add("vocCd", "15");
            else if("3".equals(params.getReq_type()))
                restBody.add("vocCd", "11");
            else if("4".equals(params.getReq_type()))
                restBody.add("vocCd", "16");
            restBody.add("title", params.getTitle());
            restBody.add("question", params.getContent());
            restBody.add("vocAgree", "Y");
            restBody.add("userNm", params.getName());
            restBody.add("mobile", params.getMobile());
            restBody.add("email", params.getEmail());

            MultipartFile[] files = params.getFiles();
            for(MultipartFile file : files) {
                ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                    // 기존 ByteArrayResource의 getFilename 메서드 override
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                };
                restBody.add("files", fileResource);
            }

            HttpEntity<MultiValueMap<String, Object>> restRequest = new HttpEntity<>(restBody, restHeaders);
            String result = restTemplate.postForObject(new URI(serverUrl), restRequest, String.class);
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(result);
            jsonObj = (JSONObject) obj;
            logger.info("voc result:"+result);
        }
        catch(HttpClientErrorException | HttpServerErrorException e) {
            logger.error(e.toString());
        }
        catch(Exception e) {
            logger.error(e.toString());
        }
        finally {
            if(jsonObj != null && jsonObj.get("vocSeq") != null){
                MessageModel message = new MessageModel("해당 내용이 등록 되었습니다", "/Customer/voc?type="+params.getType(), RequestMethod.GET, null);
                return showMessageAndRedirect(message, model);
            }
            else {
                MessageModel message = new MessageModel("등록에 실패했습니다.", "/Customer/voc?type="+params.getType(), RequestMethod.GET, null);
                return showMessageAndRedirect(message, model);
            }
        }

        /*
        //사용자 ip
        params.setReg_ip(CommonUtil.getRemoteIP(request));

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
                MessageModel message = new MessageModel("이미지 업로드 실패", "/Customer/voc?type="+params.getType(), RequestMethod.GET, null);
                return showMessageAndRedirect(message, model);
            }
        }

        if(insertedId > 0){
            MessageModel message = new MessageModel("해당 내용이 등록 되었습니다", "/Customer/voc?type="+params.getType(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            MessageModel message = new MessageModel("등록에 실패했습니다.", "/Customer/voc?type="+params.getType(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
         */
    }

//    @RequestMapping("/Company/privacy")
//    public String privacy(HttpServletRequest request, Model model){
//        getGnbInfo(model);
//        List<CustomerModel> customerModelList = informationHTMLText("privacy", "");
//        CustomerModel customerModel = new CustomerModel();
//        if(customerModelList == null){
//            return "Error_400";
//        }
//        else{
//            customerModel = customerModelList.get(0);
//            model.addAttribute("customerModelList", customerModelList);
//            model.addAttribute("customerModel", customerModel);
//            return getViewPath(request, "front/content/customer_privacy");
//        }
//
//
//    }

    @GetMapping("/Company/privacy")
    public String getPrivacyPolicy(@RequestParam(value = "version", required = false) String version,
                                   @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                   HttpServletRequest request,
                                   Model model) {

        List<CustomerModel> customerModelList = informationHTMLText("privacy", "");

        // version 값이 없거나, 일치하는 버전이 없으면 최신 버전(첫 번째 데이터) 사용
        CustomerModel selectedPrivacy = customerModelList.stream()
                .filter(policy -> version != null && version.equals(policy.getVersion()))
                .findFirst()
                .orElse(customerModelList.get(0)); // 기본값: 최신 버전 (리스트의 첫 번째 항목)

        model.addAttribute("customerModel", selectedPrivacy);
        model.addAttribute("customerModelList", customerModelList);
        return getViewPath(request, "front/content/customer_privacy");
    }

    @RequestMapping("/Company/privacy/sample")
    public String privacySample(HttpServletRequest request, Model model){
        getGnbInfo(model);

        return getViewPath(request, "front/content/customer_privacy_sample");
    }


    @RequestMapping("/Company/terms")
    public String terms(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/customer_terms");
    }

    @RequestMapping("/Company/email")
    public String email(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/customer_email");
    }

    @RequestMapping("/Customer/exVocView")
    public String exVocView(HttpServletRequest request, Model model){
        return getViewPath(request, "front/content/ex_voc_view");
    }

    @RequestMapping("/Customer/insertExVoc")
    public String insertExVoc(@ModelAttribute("params") final VocModel params, HttpServletRequest request, Model model){

        JSONObject jsonObj = null;

        try{
            String serverUrl = exVocUrl;
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = this.makeRestTemplate();

            HttpHeaders restHeaders = new HttpHeaders();
            restHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            restHeaders.set("Authorization", exAuth);
            restHeaders.set("X-UserAuthKey", exUserKey);

            MultiValueMap<String, Object> restBody = new LinkedMultiValueMap<>();
            if("1".equals(params.getReq_type()))
                restBody.add("vocCd", "14");
            else if("2".equals(params.getReq_type()))
                restBody.add("vocCd", "16");
            else if("3".equals(params.getReq_type()))
                restBody.add("vocCd", "11");
            else if("4".equals(params.getReq_type()))
                restBody.add("vocCd", "17");
            restBody.add("title", params.getTitle());
            restBody.add("question", params.getContent());
            restBody.add("vocAgree", "Y");
            restBody.add("userNm", params.getName());
            restBody.add("mobile", params.getMobile());
            restBody.add("email", params.getEmail());
            restBody.add("alertCds", params.getAlertCds());

            MultipartFile[] files = params.getFiles();
            for(MultipartFile file : files) {
                ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                    // 기존 ByteArrayResource의 getFilename 메서드 override
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                };
                restBody.add("files", fileResource);
            }

            HttpEntity<MultiValueMap<String, Object>> restRequest = new HttpEntity<>(restBody, restHeaders);
            String result = restTemplate.postForObject(new URI(serverUrl), restRequest, String.class);
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(result);
            jsonObj = (JSONObject) obj;
            logger.debug("voc result:"+result);
        }
        catch(HttpClientErrorException | HttpServerErrorException e) {
            logger.error(e.toString());
        }
        catch(Exception e) {
            logger.error(e.toString());
        }
        finally {
            if(jsonObj != null && jsonObj.get("vocSeq") != null){
                return "redirect:/Customer/exVocResult";
            }
            else {
                MessageModel message = new MessageModel("등록에 실패했습니다.", "/Customer/exVocView", RequestMethod.GET, null);
                return showMessageAndRedirect(message, model);
            }
        }
    }

    @RequestMapping("/Customer/exVocResult")
    public String exVocResult(HttpServletRequest request, Model model){
        return getViewPath(request, "front/content/ex_voc_result");
    }

    @RequestMapping("/Customer/exAttachView/{vocSeq}")
    public String exAttachView(HttpServletRequest request, Model model, @PathVariable("vocSeq") String vocSeq){
        model.addAttribute("vocSeq", vocSeq);
        return getViewPath(request, "front/content/ex_attach_view");
    }

    @RequestMapping("/Customer/insertExAttach")
    public String insertExAttach(@ModelAttribute("params") final VocModel params, HttpServletRequest request, Model model){

        if(params.getVocSeq() == null || "".equals(params.getVocSeq())){
            MessageModel message = new MessageModel("정상적인 접근이 아닙니다.", "/Customer/exAttachView/error", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        JSONObject jsonObj = null;

        try{
            String serverUrl = exAttachUrl;
            serverUrl = serverUrl.replace("$s", params.getVocSeq());
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = this.makeRestTemplate();

            HttpHeaders restHeaders = new HttpHeaders();
            restHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            restHeaders.set("Authorization", exAuth);
            restHeaders.set("X-UserAuthKey", exUserKey);

            MultiValueMap<String, Object> restBody = new LinkedMultiValueMap<>();
            restBody.add("vocSeq", params.getVocSeq());
            restBody.add("evdRqTt", params.getContent());

            MultipartFile[] files = params.getFiles();
            for(MultipartFile file : files) {
                ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                    // 기존 ByteArrayResource의 getFilename 메서드 override
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                };
                restBody.add("files", fileResource);
            }

            HttpEntity<MultiValueMap<String, Object>> restRequest = new HttpEntity<>(restBody, restHeaders);
            String result = restTemplate.postForObject(new URI(serverUrl), restRequest, String.class);
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(result);
            jsonObj = (JSONObject) obj;
            logger.debug("voc result:"+result);
        }
        catch(HttpClientErrorException | HttpServerErrorException e) {
            logger.error(e.toString());
        }
        catch(Exception e) {
            logger.error(e.toString());
        }
        finally {
            if(jsonObj != null && jsonObj.get("vocSeq") != null){
                return "redirect:/Customer/exAttachResult";
            }
            else {
                MessageModel message = new MessageModel("등록에 실패했습니다.", "/Customer/exAttachView/"+params.getVocSeq(), RequestMethod.GET, null);
                return showMessageAndRedirect(message, model);
            }
        }
    }

    @RequestMapping("/Customer/exAttachResult")
    public String exAttachResult(HttpServletRequest request, Model model){
        return getViewPath(request, "front/content/ex_attach_result");
    }

    @RequestMapping("/Customer/exSurveyView/{vocSeq}")
    public String exSurveyView(HttpServletRequest request, Model model, @PathVariable("vocSeq") String vocSeq){
        model.addAttribute("vocSeq", vocSeq);
        model.addAttribute("error", request.getParameter("error"));
        return getViewPath(request, "front/content/ex_survey_view");
    }

    @RequestMapping("/Customer/insertExSurvey")
    public String insertExSurvey(@ModelAttribute("params") final VocModel params, HttpServletRequest request, Model model){



        if(params.getVocSeq() == null || "".equals(params.getVocSeq())){
            MessageModel message = new MessageModel("정상적인 접근이 아닙니다.", "/Customer/exSurveyView?error=Y", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        JSONObject jsonObj = null;

        try{
            String serverUrl = exSurveyUrl;
            serverUrl = serverUrl.replace("$s", params.getVocSeq());
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = this.makeRestTemplate();

            HttpHeaders restHeaders = new HttpHeaders();
            restHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            restHeaders.set("Authorization", exAuth);
            restHeaders.set("X-UserAuthKey", exUserKey);

            MultiValueMap<String, Object> restBody = new LinkedMultiValueMap<>();
            restBody.add("vocSeq", params.getVocSeq());

            for(int i = 1; i <= 5; i++) {
                if(i==1) restBody.add("scores", params.getScore1());
                if(i==2) restBody.add("scores", params.getScore2());
                if(i==3) restBody.add("scores", params.getScore3());
                if(i==4) restBody.add("scores", params.getScore4());
                if(i==5) restBody.add("scores", params.getScore5());
                restBody.add("orderNos", i+"");
            }

            HttpEntity<MultiValueMap<String, Object>> restRequest = new HttpEntity<>(restBody, restHeaders);
            String result = restTemplate.postForObject(new URI(serverUrl), restRequest, String.class);
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(result);
            jsonObj = (JSONObject) obj;
            logger.debug("voc result:"+result);
        }
        catch(HttpClientErrorException | HttpServerErrorException e) {
            logger.error(e.toString());
        }
        catch(Exception e) {
            logger.error(e.toString());
        }
        finally {
            if(jsonObj != null && jsonObj.get("vocSeq") != null){
                return "redirect:/Customer/exSurveyResult";
            }
            else {
                MessageModel message = new MessageModel("등록에 실패했습니다.", "/Customer/exSurveyView/"+params.getVocSeq(), RequestMethod.GET, null);
                return showMessageAndRedirect(message, model);
            }
        }
    }

    @RequestMapping("/Customer/exSurveyResult")
    public String exSurveyResult(HttpServletRequest request, Model model){
        return getViewPath(request, "front/content/ex_survey_result");
    }

    private RestTemplate makeRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        requestFactory.setConnectTimeout(3 * 1000);

        requestFactory.setReadTimeout(3 * 1000);

        return new RestTemplate(requestFactory);
    }


    private List<CustomerModel> informationHTMLText(String category, String version){

        try {
            List<CustomerModel> customerMode =  customerService.getContents(category, version);
            return customerMode;
        }
        catch(Exception e){
            logger.error(e.toString());
            return null;
        }
    }

}