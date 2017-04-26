package com.conton.impress.web.timer;

import com.conton.impress.model.Diary;
import com.conton.impress.service.DiaryService;
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
}
