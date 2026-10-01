package com.kleannara.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VocModel {
    private int idx;
    private String type;
    private String req_type;
    private String prod_type;
    private String status;
    private String name;
    private String email;
    private String mobile;
    private String title;
    private String content;
    private String file_name;
    private String file_path;
    private String reg_ip;
    private Date reg_date;
    private Date mod_date;
    private String dm_lang;
    private String dm_status;
    private String dm_text;
    private String dm_id;
    private String dm_name;
    private Date dm_date;
    private String dm_ip;
    private String emailYN;
    private String del_yn;
    private String password;
    private String address;
    private String address_detail;
    private String zipcode;
    
 // 관리자 응답 내용 History 기능 추가 - 2024/06/13 강지선
    private int p_idx;
    private List<VocModel> vocAnswers;
    
    //업데이트용
    private MultipartFile file;
    private MultipartFile[] files;
    private String pre_file_path;
    private String pre_file_name;
    
    //외부 voc 시스템용
    private String alertCds;
    private String vocSeq;
    private String score1;
    private String score2;
    private String score3;
    private String score4;
    private String score5;
    
    public String toStringAdminLog(String method) {
    	String str = "";
    	
    	switch (method) {
			case "insertVoc":
				str = "답변: " + this.dm_text.replace("\r\n", "<br />").replace("\n", "<br />");
				break;
			case "vocView":
				//str = "게시물 조회 / " + "idx=" + this.p_idx;
				break;
			case "fileDownloadLog":
				str = "파일: " + this.file_name;
				break;

			default:
				break;
		}
    	
    	return str;
    }
}
