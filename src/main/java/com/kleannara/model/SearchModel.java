package com.kleannara.model;

import java.lang.reflect.Field;

import com.kleannara.paging.paging.Pagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchModel {
    private int page;                 // 현재 페이지 번호
    private int recordSize;           // 페이지당 출력할 데이터 개수
    private int pageSize;             // 화면 하단에 출력할 페이지 사이즈
    private String keyword;           // 검색 키워드
    private String searchType;        // 검색 유형
    private Pagination pagination;    // 페이지네이션 정보
    private String lang;              // 언어
    //페이지별 필터값
    private String type;
    private String search_type;     //필터링 타입 구분값
    private String req_type;
    private String prod_type;
    private String status;
    private String sub_type;
    private String vocCategoryCode;
    private String vocStatus;

    //voc
    private String email;
    private String password;
    private int p_idx;
        
    //front
    private String del_yn;
    private String show_yn;
    private String main_yn;
    private String top_yn;
    private String now_yn;
    private String main_type;   //1:공지 2:매체 or 홍보
    private String show_pc;
    private String show_mo;
    private int limit;
    
    private String sYear;			// 지속가능경영보고서 > 연도 구분 추가 - 2024/05/27 강지선

    public SearchModel() {
        this.page = 1;
        this.recordSize = 10;
        this.pageSize = 10;
    }
    
    public String toStringAdminLog(String method) {
    	StringBuilder str = new StringBuilder();

    	switch (method) {
			case "vocList":
			case "adminList":

				Field[] fields = SearchModel.class.getDeclaredFields();
		        for (Field field : fields) {
		        	
		        	// 수집제외
		        	if ("type".equals(field.getName()) || "page".equals(field.getName()) || "recordSize".equals(field.getName()) || "pageSize".equals(field.getName()) || "p_idx".equals(field.getName()) || "limit".equals(field.getName())) {
		        		continue;
		        	}
		        	
		            try {
		                field.setAccessible(true);  // private 필드에 접근 가능하게 설정
		                Object value = field.get(this);  // 해당 필드의 값 가져오기

		                // String 또는 int 타입 변수만 처리
		                if (value != null &&  (field.getType().equals(String.class) || field.getType().equals(int.class))) {

		                    // int 타입일 경우 값은 String으로 변환
		                    if (field.getType().equals(int.class)) {
		                        value = Integer.toString((int) value);
		                    }

		                    // 값이 null이 아니거나 빈 문자열이 아닐 경우만 처리
		                    if (!(value instanceof String && ((String) value).isEmpty())) {
		                        str.append(field.getName()).append("=").append(value).append("&");
		                    }
		                }
		            } catch (IllegalAccessException e) {
		                e.printStackTrace();
		            }
		        }
		        
		        // 마지막 쉼표와 공백을 제거
                if (str.length() > 0 && str.charAt(str.length() - 2) == '&') {
                    str.delete(str.length() - 2, str.length());
                    
                    str = str.insert(0, "조건: ");
                }

				break;

			default:
				break;
		}
    	
    	return str.toString();
    }
}
