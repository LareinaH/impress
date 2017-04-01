package com.conton.impress.web.controller;

import com.conton.base.common.RestResponse;
import com.conton.impress.model.Diary;
import com.conton.impress.model.DiaryComment;
import com.conton.impress.model.DiaryRecord;
import com.conton.impress.model.Member;
import com.conton.impress.model.VO.DiaryDetailVO;
import com.conton.impress.model.VO.DiaryVO;
import com.conton.impress.service.DiaryCommentService;
import com.conton.impress.service.DiaryRecordService;
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
    @Autowired
    private DiaryCommentService diaryCommentService;
    @Autowired
    private DiaryRecordService diaryRecordService;

    /**
     * 左右印象
     * @param lbsX
     * @param lbsY
     * @param radius
     * @param pageNum
     * @param pageSize
     * @return
     */
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

    /**
     * 日记详情
     * @param diaryId
     * @return
     */
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

    /**
     * 添加日记
     * @param publishTime 发布时间
     * @param tag 标签【default：默认， event：事件， mood：心情 ，thing：物件，view： 景色】
     * @param brief 日记摘要（选填）
     * @param firstImage 日记首图（选填）
     * @param anonymous 是否匿名【0：不匿名，1：匿名】
     * @param accessRight 访问权限【all：所有人可见，excludeFriend：朋友不可见，friend：朋友可见，oneself：仅自己可见】
     * @param lbsX 经度
     * @param lbsY 纬度
     * @param content 内容
     * @return
     */
    @RequestMapping(value = "/addDiary", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<Void> addDiary(@RequestParam(required = true) String publishTime, @RequestParam(required = true) String tag,
                                       String brief,String firstImage,
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

    /**
     * 添加评论
     * @param diaryId 日记id
     * @param parentId 如果是评论了其他人的评论 评论的id
     * @param isWhisper 是否是悄悄话
     * @param commentText 评论内容
     * @return
     */
    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<Void> addComment(Long diaryId,Long parentId, int isWhisper,String commentText) {
        RestResponse<Void> restResponse = new RestResponse<Void>();

        Member member = PermissionContext.getMember();

        DiaryComment diaryComment = new DiaryComment();
        diaryComment.setDiaryId(diaryId);
        diaryComment.setParentId(parentId);
        diaryComment.setCommentUserId(member.getId());
        diaryComment.setIsWhisper(isWhisper);
        diaryComment.setCommentText(commentText);
        diaryComment.setStatus("normal");
        if (diaryCommentService.addCommentService(diaryComment)) {
            restResponse.setCode(RestResponse.OK);
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("评论失败！");
        }
        return restResponse;
    }

    /**
     * 日记操作记录（日记的赞/踩/浏览）
     * @param diaryId
     * @param type 类型【diary：日记， comment：评论】
     * @param commentId 评论id 当type=comment时有效
     * @param category 分类【up：赞，down：踩，browse：浏览】
     * @return
     */
    @RequestMapping(value = "/addDiaryRecord")
    @ResponseBody
    public RestResponse<Void> addDiaryRecord(Long diaryId,String type, Long commentId,String category) {
        RestResponse<Void> restResponse = new RestResponse<Void>();

        Member member = PermissionContext.getMember();

        DiaryRecord diaryRecord = new DiaryRecord();
        diaryRecord.setMemberId(member.getId());
        diaryRecord.setDiaryId(diaryId);
        diaryRecord.setCommentId(commentId);
        diaryRecord.setSelector(type);
        diaryRecord.setCategory(category);
        diaryRecord.setStatus("normal");

        if (diaryRecordService.addDiaryRecord(diaryRecord)) {
            restResponse.setCode(RestResponse.OK);
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("评论失败！");
        }
        return restResponse;
    }
}
