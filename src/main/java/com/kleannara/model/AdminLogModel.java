package com.kleannara.model;

import lombok.*;
import org.springframework.lang.Nullable;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminLogModel {
    private int idx;
    private String id;
    private String type;	// C, R, U, D, I(LOGIN), O(LOGOUT)
    private String serviceName;
    private String serviceTarget;
    private String referer;
    private String action;
    private String memo;
    private String ip;
    private Date reg_date;
}
