package com.kleannara.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FaqModel {
    private int idx;
    private String type;
    private String title;
    private String content;
    private String top_yn;
    private String red_member;
    private int show_order;
    private Date reg_date;
    private Date mod_date;
    private String del_yn;
}