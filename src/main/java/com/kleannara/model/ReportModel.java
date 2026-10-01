package com.kleannara.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportModel {
    private int idx;
    private String type;
    private String title;
    private String file_path;
    private String file_name;
    private String show_yn;
    private String reg_member;
    private String download_cnt;
    private String top_yn;
    private String content;
    private String sYear;
    private String read_cnt;
    private Date reg_date;
    private Date mod_date;
    private String del_yn;
    
    //업데이트용
    private MultipartFile file;
    private String pre_file_path;
    private String pre_file_name;
    
    //출력용
    private String reg_member_;
    private String reg_AdminID;
    private String reg_AdminName;
    
    //프론트
    private String prev_idx;
    private String prev_title;
    private String next_idx;
    private String next_title;
    
    //공시지가 키
    private String acpt_no;
    
    //페이지이동
    private int page;
    private String keyword;
    private String searchType;
    
}
