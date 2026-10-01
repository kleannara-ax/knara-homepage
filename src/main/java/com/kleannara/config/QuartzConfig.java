package com.kleannara.config;

import com.kleannara.batch.UpdateLoginJob;
import com.kleannara.batch.UpdatePasswordJob;
import org.quartz.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("batch")
public class QuartzConfig {

    // ---------------------------
    // 비밀번호 변경 배치 (매일 9시)
    // ---------------------------
    @Bean
    @ConditionalOnMissingBean(name = "updatePasswordJobDetail")
    public JobDetail updatePasswordJobDetail() {
        return JobBuilder.newJob(UpdatePasswordJob.class)
                .withIdentity("updatePasswordJob")
                .storeDurably()
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(name = "updatePasswordJobTrigger")
    public Trigger updatePasswordJobTrigger(JobDetail updatePasswordJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(updatePasswordJobDetail)
                .withIdentity("updatePasswordTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 9 * * ?"))
                // 테스트용 매 1분마다
                // .withSchedule(CronScheduleBuilder.cronSchedule("0 */1 * * * ?"))
                .build();
    }

    // ---------------------------
    // 로그인 배치 (매일 0시)
    // ---------------------------
    @Bean
    @ConditionalOnMissingBean(name = "updateLoginJobDetail")
    public JobDetail updateLoginJobDetail() {
        return JobBuilder.newJob(UpdateLoginJob.class)
                .withIdentity("updateLoginJob")
                .storeDurably()
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(name = "updateLoginJobTrigger")
    public Trigger updateLoginJobTrigger(JobDetail updateLoginJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(updateLoginJobDetail)
                .withIdentity("updateLoginTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))
                // 테스트용 매 1분마다
//                 .withSchedule(CronScheduleBuilder.cronSchedule("0 */1 * * * ?"))
                .build();
    }

    // ---------------------------
    // 확인용 로그
    // ---------------------------
    @Bean
    public CommandLineRunner logScheduler(org.quartz.Scheduler scheduler) {
        return args -> {
            System.out.println("### Quartz Scheduler: "
                    + scheduler.getSchedulerName()
                    + " / InstanceId: "
                    + scheduler.getSchedulerInstanceId());
        };
    }
}
