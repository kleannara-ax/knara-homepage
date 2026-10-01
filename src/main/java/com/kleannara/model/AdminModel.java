package com.kleannara.model;

import lombok.*;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminModel {
    private int idx;
    private String name;
    private String id;
    private String password;
    private String passwordChangeFlg;
    private Date passwordChangeDate;
    private Date reg_date;
    private Date mod_date;
    private String del_yn;
    private String auth_menu1;
    private String auth_menu2;
    private String auth_menu3;
    private String auth_menu4;
    private String auth_menu5;
    private String auth_menu6;
    private String auth_menu7;
    private int status;
    private String last_login_date;
    private int login_allow;

    // toStringAdminLog 메소드에서 this를 사용하여 멤버 변수 접근
    public String toStringAdminLog(String method) {
        StringBuilder str = new StringBuilder();

        // Map<String, String> 타입으로 메뉴맵을 생성
        Map<String, String> menuMap = new HashMap<>();
        menuMap.put("auth_menu1", "Main 관리");
        menuMap.put("auth_menu2", "사업소개 관리");
        menuMap.put("auth_menu3", "ESG경영 관리");
        menuMap.put("auth_menu4", "투자정보 관리");
        menuMap.put("auth_menu5", "뉴스룸 관리");
        menuMap.put("auth_menu6", "고객센터 관리");
        menuMap.put("auth_menu7", "관리자권한 관리");

        // auth_menu1부터 auth_menu7까지 배열로 관리
        String[] authMenus = {this.auth_menu1, this.auth_menu2, this.auth_menu3, this.auth_menu4, this.auth_menu5, this.auth_menu6, this.auth_menu7};
        String[] menuKeys = {"auth_menu1", "auth_menu2", "auth_menu3", "auth_menu4", "auth_menu5", "auth_menu6", "auth_menu7"};

        switch (method) {
            case "insertAdmin":
                str.append("권한: ");

                // 설정된 권환만 로그 기록
                IntStream.range(0, authMenus.length)
                        .filter(i -> "Y".equals(authMenus[i]))
                        .forEach(i -> str.append(menuMap.get(menuKeys[i])).append(", "));

                // 마지막 쉼표와 공백을 제거
                if (str.length() > 0 && str.charAt(str.length() - 2) == ',') {
                    str.delete(str.length() - 2, str.length());
                }

                break;
            case "deleteAdmin":
            	break;
            case "adminView":
            	break;
            default:
                break;
        }

        return str.toString();
    }
}
