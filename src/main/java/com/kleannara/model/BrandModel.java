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
public class BrandModel {
    private int idx;
    private String type;
    private String title_ko;
    private String title_en;
    private String title_zh;
    private String explain_ko;
    private String explain_en;
    private String explain_zh;
    private int show_order;
    private String image_path;
    private String image_name;
    private String logo_path;
    private String logo_name;
    private String thumb_path;
    private String thumb_name;
    private String insta_url;
    private String shop_url;
    private String sub_copy_ko;
    private String sub_copy_en;
    private String sub_copy_zh;
    private String headline_ko;
    private String headline_en;
    private String headline_zh;
    private String story_ko;
    private String story_en;
    private String story_zh;
    private String show_yn;
    private String reg_admin;
    private Date reg_date;
    private Date mod_date;
    private String del_yn;
    //업데이트용
    private MultipartFile image;
    private MultipartFile logo;
    private MultipartFile thumb;
    private String pre_image_path;
    private String pre_image_name;
    private String pre_logo_path;
    private String pre_logo_name;
    private String pre_thumb_path;
    private String pre_thumb_name;
    //프론트
    private String page_type;
}
