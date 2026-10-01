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
public class PopupModel {
    private int idx;
    private String show_pc;
    private String show_mo;
    private String title;
    private String content;
    private String lang;
    private int pop_x;
    private int pop_y;
    private int pop_w;
    private int pop_h;
    private String del_yn;
    private Date start_date;
    private Date end_date;
    private Date reg_date;
    private Date mod_date;
    //화면표기용
    private String date1;
    private String hour1;
    private String min1;
    private String date2;
    private String hour2;
    private String min2;
    //결과값
    private String showing_yn;
    private String cookie_yn;
}
