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
public class ProductModel {
    private int idx;
    private String title_ko;
    private String title_en;
    private String title_zh;
    private String content_ko;
    private String content_en;
    private String content_zh;
    private String content_ko_s;
    private String content_en_s;
    private String content_zh_s;
    private String show_yn;
    private String reg_admin;
    private int show_order;
    private String size_ko;
    private String size_en;
    private String size_zh;
    private String color_ko;
    private String color_en;
    private String color_zh;
    private String purpose_ko;
    private String purpose_en;
    private String purpose_zh;
    private String thumb_path;
    private String thumb_name;
    private String image_path;
    private String image_name;
    private Date reg_date;
    private Date mod_date;
    private String del_yn;
    private int category_idx;
    //업데이트용
    private MultipartFile image;
    private MultipartFile thumb;
    private String pre_image_path;
    private String pre_image_name;
    private String pre_thumb_path;
    private String pre_thumb_name;
    //파라미터전달용
    private String brand_type;
    private String brand_idx;
    private String category_title;
}
