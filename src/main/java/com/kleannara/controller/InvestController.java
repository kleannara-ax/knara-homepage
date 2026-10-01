package com.kleannara.controller;

import com.google.gson.JsonParser;
import com.kleannara.model.ReportModel;
import com.kleannara.model.SearchModel;
import com.kleannara.paging.paging.PagingResponse;
import com.kleannara.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class InvestController extends FrontCommon {

    private final AdminReportService reportService;

    @Value("#{environment['config.file.dir']}")
    private String fileDir;

    @RequestMapping(value = {"/Invest"})
    public String main(Model model){
        return "redirect:/Invest/management";
    }

    //이사회
    @RequestMapping("/Invest/management")
    public String management(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/invest_management");
    }

    //재무상태표
    @RequestMapping("/Invest/finance")
    public String finance(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/invest_finance");
    }

    //손익계산서
    @RequestMapping("/Invest/profit")
    public String profit(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/invest_profit");
    }

    //현금흐름표
    @RequestMapping("/Invest/cashFlow")
    public String cashFlow(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/invest_cashFlow");
    }

    //실시간주가
    @RequestMapping("/Invest/stockPrice")
    public String stockPrice(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/invest_stockPrice");
    }

    //주식현황
    @RequestMapping("/Invest/shareholder")
    public String shareholder(HttpServletRequest request, Model model){
        getGnbInfo(model);
        return getViewPath(request, "front/content/invest_shareholder");
    }

    //영업보고서, 감사보고서, IR자료, 공시자료, 전자공고
    @RequestMapping("/Invest/reportList")
    public String reportList(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){
        if(params.getType() == null || "".equals(params.getType())) {
            params.setType("3");
        }

        params.setTop_yn("Y");
        params.setShow_yn("Y");
        PagingResponse<ReportModel> response = reportService.getReportList(params);
        model.addAttribute("response", response);

        String viewName = "front/content/invest_reportList";
        //전자공고, 공시자료
        if("2".equals(params.getType()) || "6".equals(params.getType()))
            viewName = "front/content/invest_report2List";

        getGnbInfo(model);
        return getViewPath(request, viewName);
    }

    @RequestMapping("/Invest/reportBatch")
    @ResponseBody
    public String reportBatch(@ModelAttribute("params") final SearchModel params, HttpServletRequest request, Model model){

        //http 통신을 하기위한 객체 선언 실시
        URL url = null;
        HttpURLConnection conn = null;

        //http 통신 요청 후 응답 받은 데이터를 담기 위한 변수
        String responseData = "";
        int newInsert = 0;
        BufferedReader br = null;
        StringBuffer sb = null;
        JSONObject jsonObj = null;

        try {
            url = new URL("https://asp.koscom.co.kr/listservice/getDisInfo?code=004540&auth_key=vhAHopuizhHJRwQNfb3ApohjQJb7C0YA&gubun=K&count=30"); // 호출할 외부 API 를 입력한다.

            conn = (HttpURLConnection) url.openConnection(); // header에 데이터 통신 방법을 지정한다.
            //conn.setRequestMethod("GET");
            //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("GET");

            //http 요청 실시
            conn.connect();

            //http 요청 후 응답 받은 데이터를 버퍼에 쌓는다
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            sb = new StringBuffer();
            while ((responseData = br.readLine()) != null) {
                sb.append(responseData); //StringBuffer에 응답받은 데이터 순차적으로 저장 실시
            }

            //메소드 호출 완료 시 반환하는 변수에 버퍼 데이터 삽입 실시
            responseData = sb.toString();
            JSONParser jsonParser = new JSONParser();
            jsonObj = (JSONObject)jsonParser.parse(responseData);

            //http 요청 응답 코드 확인 실시
            String responseCode = String.valueOf(conn.getResponseCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            //http 요청 및 응답 완료 후 BufferedReader를 닫아줍니다
            try {
                if (br != null) {
                    br.close();
                }
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(jsonObj != null){
            if((boolean) jsonObj.get("resultOk")){
                JSONArray jsonArray = (JSONArray)jsonObj.get("dataList");
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject tempJsonOjb = (JSONObject)jsonArray.get(i);
                    ReportModel tempParam = new ReportModel();
                    tempParam.setAcpt_no((String)tempJsonOjb.get("acptNo"));
                    tempParam.setTitle((String)tempJsonOjb.get("formKorNm"));
                    tempParam.setContent((String)tempJsonOjb.get("disclsViewerLink"));
                    tempParam.setType("6");
                    tempParam.setShow_yn("Y");
                    try{
                        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmm");
                        tempParam.setReg_date(transFormat.parse((String)tempJsonOjb.get("disclsPublDdtm")));
                    }
                    catch (ParseException e){
                        tempParam.setReg_date(null);
                    }

                    ReportModel tempResult = reportService.getReportOne(tempParam);

                    if(tempResult == null) {
                        reportService.insertReport(tempParam);
                        newInsert++;
                    }
                    else
                        reportService.updateReport(tempParam);
                }
            }
        }

        return LocalDate.now() + " 신규추가 : " + newInsert;
    }

    //리포트 상세
    @RequestMapping("/Invest/reportView")
    public String reportView(@ModelAttribute("params") final ReportModel params, HttpServletRequest request, Model model){
        ReportModel data = reportService.getReportOne(params);
        model.addAttribute("data", data);

        //이전,다음
        int prev_idx=0;
        String prev_title="";
        int next_idx=0;
        String next_title="";
        List<ReportModel> nextList = reportService.getReportNext(params);
        for(ReportModel row : nextList){
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

        getGnbInfo(model);
        return getViewPath(request, "front/content/invest_reportView");
    }

    @RequestMapping("/Invest/attach/{idx}")
    public ResponseEntity<Object> downloadAttach(@PathVariable int idx) throws MalformedURLException {

        ReportModel tempParams = new ReportModel();
        tempParams.setIdx(idx);
        ReportModel data = reportService.getReportOne(tempParams);

        String savedPath = fileDir + "/report/"+data.getFile_path();
        UrlResource resource = new UrlResource("file:" +  savedPath);
        String encodedFileName = UriUtils.encode(data.getFile_name(), StandardCharsets.UTF_8);

        // 파일 다운로드 대화상자가 뜨도록 하는 헤더를 설정해주는 것
        // Content-Disposition 헤더에 attachment; filename="업로드 파일명" 값을 준다.
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition).body(resource);
    }
}
