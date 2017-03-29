package com.conton.impress.web.controller;

import com.conton.base.common.RestResponse;
import com.conton.impress.model.Diary;
import com.conton.impress.model.Member;
import com.conton.impress.model.VO.DiaryDetailVO;
import com.conton.impress.model.VO.DiaryVO;
import com.conton.impress.service.DiaryService;
import com.conton.impress.web.PermissionContext;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    public RestResponse<List<DiaryVO>> aboutDiarys(@RequestParam(required = true) String lbsX,
                                                   @RequestParam(required = true) String lbsY,
                                                   @RequestParam(required = true) String radius,
                                                   @RequestParam(defaultValue = "1") int pageNum,
                                                   @RequestParam(defaultValue = "20") int pageSize) {
        RestResponse<List<DiaryVO>> restResponse = new RestResponse<List<DiaryVO>>();

        PageInfo<Diary> diaryPageInfo = diaryService.query(pageNum, pageSize);

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

    @RequestMapping(value = "/getDiaryDetail")
    @ResponseBody
    public RestResponse<DiaryDetailVO> getDiaryDetail(@RequestParam(required = true) long diaryId) {
        RestResponse<DiaryDetailVO> restResponse = new RestResponse<DiaryDetailVO>();

        DiaryDetailVO diaryDetailVO = diaryService.getDiaryDetailVObyId(diaryId);

        if (diaryDetailVO != null) {

            restResponse.setCode(RestResponse.OK);
            restResponse.setData(diaryDetailVO);
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("读取日记详情失败！");
        }
        return restResponse;
    }

    @RequestMapping(value = "/addDiary", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<Void> addDiary(@RequestParam(required = true) String publishTime, @RequestParam(required = true) String tag,
                                       @RequestParam(required = true) String brief, @RequestParam(required = true) String firstImage,
                                       @RequestParam(required = true) Integer anonymous, @RequestParam(required = true) String accessRight,
                                       @RequestParam(required = true) double lbsX, @RequestParam(required = true) double lbsY,
                                       @RequestParam(required = true) String content) {
        RestResponse<Void> restResponse = new RestResponse<Void>();


        Member member = PermissionContext.getMember();
        if (diaryService.addDiary(member.getId(), publishTime, tag, brief, firstImage, anonymous,
                accessRight, lbsX, lbsY, content)) {
            restResponse.setCode(RestResponse.OK);
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("添加日记失败！");
        }
        return restResponse;
    }

    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<Void> addComment(Long diaryId,Long parentId, boolean isWhisper,String commentText) {
        RestResponse<Void> restResponse = new RestResponse<Void>();

        Member member = PermissionContext.getMember();
        if (true) {
            restResponse.setCode(RestResponse.OK);
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("评论失败！");
        }
        return restResponse;
    }


}
