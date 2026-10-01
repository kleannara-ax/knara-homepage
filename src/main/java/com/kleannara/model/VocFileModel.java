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
public class VocFileModel {
    private int idx;
    private int p_idx;
    private String file_name;
    private String file_path;
    private Date reg_date;
    private String del_yn;
}
