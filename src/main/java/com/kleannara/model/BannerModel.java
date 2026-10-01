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
public class BannerModel {
    private int idx;
    private String type;
    private String title;
    private String file_path;
    private String file_name;
    private String file_path_ta;
    private String file_name_ta;
    private String file_path_mo;
    private String file_name_mo;
    private String pre_file_path;
    private String pre_file_name;
    private String pre_file_path_ta;
    private String pre_file_name_ta;
    private String pre_file_path_mo;
    private String pre_file_name_mo;
    private String link_title;
    private String link_url;
    private String video_url;
    private String show_yn;
    private String del_yn;
    private Date reg_date;
    private Date mod_date;
    private MultipartFile file;
    private MultipartFile file_ta;
    private MultipartFile file_mo;
    private int show_order;
}
