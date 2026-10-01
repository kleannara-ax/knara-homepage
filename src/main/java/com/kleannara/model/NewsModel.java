package com.kleannara.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsModel {
    private int idx;
    private String type;
    private String sub_type;
    private String title;
    private String open_date;
    private String content;
    private String content_ta;
    private String content_mo;
    private String content_top;
    private String top_yn;
    private String main_yn;
    private String news_url;
    private String thumb_path;
    private String thumb_name;
    private String file_path;
    private String file_name;
    private String show_yn;
    private String reg_member;
    private String youtube_url;
    private String attach_path;
    private String attach_name;
    private String tag;
    private Date reg_date;
    private Date mod_date;
    private String del_yn;
    private int read_cnt;
    private int show_order;
    //업데이트용
    private MultipartFile thumb;
    private MultipartFile file;
    private MultipartFile attach;
    private String pre_thumb_path;
    private String pre_thumb_name;
    private String pre_file_path;
    private String pre_file_name;
    private String pre_attach_path;
    private String pre_attach_name;
    //출력용
    private String reg_member_;
    //프론트
    private String prev_idx;
    private String prev_title;
    private String next_idx;
    private String next_title;
    private String target;  //pc, ta, mo
    //페이지이동
    private int page;
    private String keyword;
    private String searchType;
    //검색용. 1=1, 2=2,3
    private String main_type;
}
