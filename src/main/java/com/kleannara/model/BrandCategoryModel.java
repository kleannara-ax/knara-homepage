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
public class BrandCategoryModel {
    private int idx;
    private String title_ko;
    private String title_en;
    private String title_zh;
    private String content_ko;
    private String content_en;
    private String content_zh;
    private String show_yn;
    private String reg_admin;
    private String image_path;
    private String image_name;
    private String thumb_path;
    private String thumb_name;
    private int show_order;
    private Date reg_date;
    private Date mod_date;
    private String del_yn;
    private int brand_idx;
    //업데이트용
    private MultipartFile image;
    private String pre_image_path;
    private String pre_image_name;
    private MultipartFile thumb;
    private String pre_thumb_path;
    private String pre_thumb_name;
    //파라미터전달용
    private String brand_type;
    private String type;
    private String category_idx;
}
