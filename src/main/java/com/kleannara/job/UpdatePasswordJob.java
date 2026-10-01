package com.kleannara.batch;

import com.kleannara.controller.AdminCommon;
import com.kleannara.model.AdminLogModel;
import com.kleannara.model.AdminModel;
import com.kleannara.service.AdminAuthService;
import com.kleannara.service.AdminLoginService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UpdatePasswordJob implements Job {

    @Autowired
    private AdminLoginService adminLoginService;

    @Autowired
    private AdminAuthService adminService;

    @Override
    @Profile("batch")
    public void execute(JobExecutionContext context) {
        AdminLogModel adminLog = new AdminLogModel();
        adminLog.setType("U");
        adminLog.setId("시스템");
        adminLog.setReferer("");
        adminLog.setAction("");
        adminLog.setServiceName("비밀번호 변경 배치");
        adminLog.setIp("127.0.0.1");

        try {
            int updatedRows = adminLoginService.updateExpiredPasswords();
            if (updatedRows > 0) {
                adminLog.setMemo("비밀번호 변경 플래그 업데이트 실행, 결과: " + updatedRows + "개 변경");
            } else {
                adminLog.setMemo("비밀번호 변경 플래그 업데이트 실행, 결과 없음");
            }
        } catch (Exception ex) {
            adminLog.setMemo("비밀번호 변경 플래그 업데이트 오류");
            ex.printStackTrace();
        }

        try {
            adminService.insertAdminLog(adminLog);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
