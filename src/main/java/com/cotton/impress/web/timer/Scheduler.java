package com.cotton.impress.web.timer;

import com.cotton.impress.service.DiaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class Scheduler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DiaryService diaryService;

    /**
     * 清空日出印象权重 - 每天凌晨触发
     */
    @Scheduled(cron="0 0 0 * * ?")
    public void sunDiaryScheduler() {

        logger.info("清空日出印象权重。开始……");

        diaryService.resetWeight();

        logger.info("清空日出印象权重。结束。");
    }


    /**
     * 发送极光消息 - 每天晚上触发
     */
    @Scheduled(cron="0 0 20 * * ?")
    public void senJPush() {

        logger.info("发送极光消息。开始……");

        diaryService.resetWeight();

        logger.info("发送极光消息。结束。");
    }
}
