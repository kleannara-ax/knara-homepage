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
public class VocAsisModel {
    private int idx;
    private String vocID;
    private String vocName;
    private String vocEmail;
    private String vocPhone;
    private String vocCategoryCode;
    private String vocTitle;
    private String vocContent;
    private String vocFilePath;
    private String vocStatus;
    private String vocAdminMemo;
    private String vocAdminDate;
    private String vocType;
    private String vocSite;
    private String vocNation;
    private String fowardingId;
    private String regAdminId;
    private String regName;
    private Date regDate;
    private String regIP;
}
