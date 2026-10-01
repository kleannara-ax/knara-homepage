package com.kleannara.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Setter
@Getter
@Alias("CustomerModel")
public class CustomerModel {

    private int idx;
    private String category;
    private String title;
    private String version;
    private String prev_version;
    private String contents;
    private Date regist_date;
}
