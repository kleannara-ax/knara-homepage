//package com.kleannara.job;
//
//import com.kleannara.controller.AdminCommon;
//import com.kleannara.model.AdminLogModel;
//import com.kleannara.model.AdminModel;
//import com.kleannara.service.AdminAuthService;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.beans.factory.annotation.Autowired;
//import com.kleannara.service.AdminLoginService;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class BatchScheduler {
//
//    @Autowired
//    private AdminLoginService adminLoginService;
//
//    @Autowired
//    AdminAuthService adminService;
//
//
//    @Scheduled(cron = "0 0 9 * * ?") // 매일 오전 9시 실행
////    @Scheduled(cron = "0 */1 * * * ?") // 테스트용 매 1분마다 실행
//    public void updatePasswordBatch() {
////        System.out.println("---- 스케줄러 시작 ----");
//        AdminLogModel adminLog = new AdminLogModel();
//        AdminCommon adminCommon = new AdminCommon();
//        adminLog.setType("U");
//        adminLog.setId("시스템");
//        adminLog.setReferer("");
//        adminLog.setAction("");
//        adminLog.setServiceName("비밀번호 변경 배치");
//        adminLog.setIp("127.0.0.1");
//
//        try{
//            int updatedRows = adminLoginService.updateExpiredPasswords();
//            if(updatedRows > 0){
//                adminLog.setMemo("비밀번호 변경 플래그 업데이트 실행, 결과: " + updatedRows + "개 변경");
//
//            }
//            else{
//                adminLog.setMemo("비밀번호 변경 플래그 업데이트 실행, 결과 없음");
//            }
//        }
//        catch(Exception ex){
//            adminLog.setMemo("비밀번호 변경 플래그 업데이트 오류");
//        }
//        try {
//            adminService.insertAdminLog(adminLog);
//        }
//        catch(Exception ex){
//            ex.printStackTrace();
//        }
//    }
//
//    @Scheduled(cron = "0 0 0 * * ?") // 매일 오전 0시 실행
//    public void updateLoginBatch() {
//        AdminLogModel adminLog = new AdminLogModel();
//        AdminCommon adminCommon = new AdminCommon();
//        adminLog.setType("U");
//        adminLog.setId("시스템");
//        adminLog.setReferer("");
//        adminLog.setAction("");
//        adminLog.setServiceName("로그인 특정 일수 경과 배치");
//        adminLog.setIp("127.0.0.1");
//        try{
//            // 90일 이전 대상
//            List<AdminModel> getLoginDisableList = adminService.getLoginDisableList();
//            String targetId = getLoginDisableList.stream()
//                    .map(AdminModel::getId)
//                    .map(String::valueOf)
//                    .collect(Collectors.joining(","));
//            if(getLoginDisableList != null && getLoginDisableList.size() > 0){
//                int updatedRows =  adminService.updateLoginStatus(getLoginDisableList);
//                adminLog.setMemo("로그인 특정 일수 경과 배치 실행, 결과(아이디): " + targetId);
//            }
//            else{
//                adminLog.setMemo("로그인 특정 일수 경과 배치, 결과 없음");
//            }
//        }
//        catch(Exception ex){
//            adminLog.setMemo("로그인 특정 일수 경과 배치 오류: " + ex.getMessage());
//            ex.printStackTrace();
//        }
//        try {
//            adminService.insertAdminLog(adminLog);
//        }
//        catch(Exception ex){
//            ex.printStackTrace();
//        }
//    }
//}
