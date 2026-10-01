package com.kleannara.batch;

import com.kleannara.controller.AdminCommon;
import com.kleannara.model.AdminLogModel;
import com.kleannara.model.AdminModel;
import com.kleannara.service.AdminAuthService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile("batch")
public class UpdateLoginJob implements Job {

    @Autowired
    private AdminAuthService adminService;

    @Override
    public void execute(JobExecutionContext context) {
        AdminLogModel adminLog = new AdminLogModel();
        adminLog.setType("U");
        adminLog.setId("시스템");
        adminLog.setReferer("");
        adminLog.setAction("");
        adminLog.setServiceName("로그인 특정 일수 경과 배치");
        adminLog.setIp("127.0.0.1");

        try {
            List<AdminModel> getLoginDisableList = adminService.getLoginDisableList();
            String targetId = getLoginDisableList.stream()
                    .map(AdminModel::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            if (getLoginDisableList != null && getLoginDisableList.size() > 0) {
                int updatedRows = adminService.updateLoginStatus(getLoginDisableList);
                adminLog.setMemo("로그인 특정 일수 경과 배치 실행, 결과(아이디): " + targetId);
            } else {
                adminLog.setMemo("로그인 특정 일수 경과 배치, 결과 없음");
            }
        } catch (Exception ex) {
            adminLog.setMemo("로그인 특정 일수 경과 배치 오류: " + ex.getMessage());
            ex.printStackTrace();
        }

        try {
            adminService.insertAdminLog(adminLog);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
