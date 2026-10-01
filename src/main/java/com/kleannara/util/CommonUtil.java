package com.kleannara.util;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class CommonUtil {
    private static final Set<String> ALLOWED_IP_PREFIXES = new HashSet<>();

    static {
        // 허용된 IP 대역 (C 클래스 기준)
        ALLOWED_IP_PREFIXES.add("192.168.1.");    // 192.168.1.X 대역 허용
        ALLOWED_IP_PREFIXES.add("123.143.13.");   // 123.143.13.X 대역 허용
        ALLOWED_IP_PREFIXES.add("127.0.0.");      // 127.0.0.X (로컬 루프백)
        ALLOWED_IP_PREFIXES.add("210.107.10.");   // 210.107.10.X 대역 허용
        ALLOWED_IP_PREFIXES.add("192.1.7.");      // 192.1.7.X 대역 허용
        ALLOWED_IP_PREFIXES.add("192.1.17.");     // 192.1.17.X 대역 허용
        ALLOWED_IP_PREFIXES.add("192.1.107.");    // 192.1.107.X 대역 허용
        ALLOWED_IP_PREFIXES.add("192.1.117.");    // 192.1.117.X 대역 허용
        ALLOWED_IP_PREFIXES.add("115.90.230.");   // 115.90.230.X 대역 허용
        ALLOWED_IP_PREFIXES.add("210.182.40.");   // 210.182.40.X 대역 허용
        ALLOWED_IP_PREFIXES.add("112.216.68.");   // 112.216.68.X 대역 허용
    }

    /**
     * 주어진 IP가 허용된 IP 목록에 있는지 확인
     *
     * @param ipAddress 요청한 클라이언트 IP 주소
     * @return 허용된 IP이면 true, 아니면 false
     */
    public static boolean isAllowedIp(String ipAddress) {
        for (String prefix : ALLOWED_IP_PREFIXES) {
            if (ipAddress.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
    public static String getRemoteIP(HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");

        //proxy 환경일 경우
        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0) {
        	ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0) {
        	ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0) {
        	ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0) {
            ip = request.getRemoteAddr() ;
        }
        
        // IPv6 localhost -> IPv4 localhost로 변환
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e) {
                ip = "127.0.0.1"; // fallback
            }
        }


        return ip;
    }

    public static String getDeviceType(HttpServletRequest request)
    {
        //디바이스 화인
        String deviceStr = "pc";
        Device device = DeviceUtils.getCurrentDevice(request);
        if(device.isTablet())
            deviceStr = "ta";
        else if(device.isMobile())
            deviceStr = "mo";

        return deviceStr;
    }
    
    public static String getServiceName() {
    	 // Map<String, String> 타입으로 메뉴맵을 생성
        Map<String, String> serviceMap = new HashMap<>();

        //AdminAuthController
        serviceMap.put("adminList", "관리자 검색");
        serviceMap.put("insertAdmin", "관리자 등록/수정");
        serviceMap.put("deleteAdmin", "관리자 삭제");
        serviceMap.put("adminView", "관리자 상세조회");
        
        //AdminReportController
        serviceMap.put("vocList", "게시물 검색");
        serviceMap.put("insertVoc", "게시물 답변");
        serviceMap.put("updateVocValue", "게시물 삭제");
        serviceMap.put("vocView", "게시물 조회");
        serviceMap.put("fileDownloadLog", "게시물 파일다운");
        
        //AdminLoginController
        serviceMap.put("loginProc", "로그인");
        serviceMap.put("logoutProc", "로그아웃");

        serviceMap.put("ajaxChangeAdminPassword", "비밀번호 수동 변경");
        serviceMap.put("updatePasswordBatch", "비밀번호 변경 배치");
        serviceMap.put("updateLoginBatch", "로그인 특정 일수 경과 배치");

        // 현재 메서드 이름을 가져와서 해당 서비스명 반환
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();

        // null 체크 후, 반환 값 처리
        if (methodName != null && serviceMap.containsKey(methodName)) {
            return serviceMap.get(methodName);
        } else {
            return "defaultService"; // 기본값 반환 또는 예외를 던질 수 있음
        }
    }
}
