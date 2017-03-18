package com.conton.impress.web.controller;

import com.conton.base.common.RestResponse;
import com.conton.impress.model.Diary;
import com.conton.impress.model.VO.DiaryVO;
import com.conton.impress.model.VO.MemberVO;
import com.conton.impress.service.DiaryService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;

/**
 * 日记相关
 */
@Controller
@RequestMapping("/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @RequestMapping(value = "/aboutDiarys")
    @ResponseBody
    public RestResponse<List<DiaryVO>> register(@RequestParam(required = true) String lbsX,
                                                @RequestParam(required = true) String lbsY,
                                                @RequestParam(required = true) String radius,
                                                @RequestParam(defaultValue = "1") int pageNum,
                                                @RequestParam(defaultValue = "20") int pageSize) {
        RestResponse<List<DiaryVO>> restResponse = new RestResponse<List<DiaryVO>>();

        PageInfo<Diary> diaryPageInfo = diaryService.query(0, Integer.valueOf(pageSize));

        if (diaryPageInfo != null) {
            List<DiaryVO> diaryVOList = new LinkedList<DiaryVO>();

            for (Diary diary : diaryPageInfo.getList()) {
                DiaryVO diaryVO = new DiaryVO();
                BeanUtils.copyProperties(diary, diaryVO);
                diaryVOList.add(diaryVO);
            }

            restResponse.setCode(RestResponse.OK);
            restResponse.setData(diaryVOList);
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("读取日记失败！");
        }
        return restResponse;
    }


}
