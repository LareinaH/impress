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
import com.conton.impress.service.MemberService;
import com.conton.impress.web.PermissionContext;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * 日记相关
 */
@Controller
@RequestMapping("/diary")
public class DiaryController extends ImpressBaseComtroller {

    @Autowired
    private MemberService memberService;
    @Autowired
    private DiaryService diaryService;
    @Autowired
    private DiaryCommentService diaryCommentService;
    @Autowired
    private DiaryRecordService diaryRecordService;

    /**
     * 左右印象
     *
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
     * 日出印象
     *
     * @param type     all-全部  friend-好友
     * @param lbsX
     * @param lbsY
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/sunDiarys")
    @ResponseBody
    public RestResponse<List<DiaryVO>> sunDiarys(@RequestParam(required = true) String type,
                                                 @RequestParam(required = true) String lbsX,
                                                 @RequestParam(required = true) String lbsY,
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
     * 世界印象
     *
     * @return
     */
    @RequestMapping(value = "/wordImpress")
    @ResponseBody
    public RestResponse<Map<String, Object>> wordImpress() {
        RestResponse<Map<String, Object>> restResponse = new RestResponse<Map<String, Object>>();

        Member member = PermissionContext.getMember();

        Map<String, Object> map = new HashMap<String, Object>();
        restResponse.setCode(RestResponse.OK);
        restResponse.setData(map);

        //0 查找所有看过我的人 包括二级浏览者
        List<DiaryRecord> diaryRecordList = getAllBrowseMe(member.getId());

        if (diaryRecordList == null || diaryRecordList.isEmpty()) {
            map.put("pointList", null);
            map.put("influence", 0);
            map.put("cover", 0);
        }

        //1 获取看过我的位置
        List<Point> pointList = new LinkedList<Point>();
        Set<Long> memberIdSet = new HashSet<Long>();
        for (DiaryRecord diaryRecord : diaryRecordList) {
            pointList.add(new Point(diaryRecord.getLbsX(), diaryRecord.getLbsY()));
            memberIdSet.add(diaryRecord.getMemberId());
        }

        map.put("pointList", pointList);

        //2 计算影响人数
        map.put("influencePerson", memberIdSet.size());

        //3 计算影响力
        map.put("influence", memberIdSet.size() * 8);

        //4 计算覆盖范围
        float cover = (float) (memberIdSet.size() * 8 * 10) / 149500000;
        DecimalFormat df = new DecimalFormat("0.0000");//格式化小数
        String coverString = df.format(cover);

        map.put("cover", coverString);


        return restResponse;
    }

    private List<DiaryRecord> getAllBrowseMe(Long memberId) {

        DiaryRecord model = new DiaryRecord();
        model.setDiaryMemberId(memberId);
        model.setSelector("diary");
        model.setCategory("browse");
        model.setStatus("normal");

        List<DiaryRecord> diaryRecordList = diaryRecordService.queryList(model);

        if (diaryRecordList != null && !diaryRecordList.isEmpty()) {

            Set<Long> memberIdSet = new HashSet<Long>();
            for (DiaryRecord diaryRecord : diaryRecordList) {
                memberIdSet.add(diaryRecord.getMemberId());
            }

            Example example = new Example(DiaryRecord.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("diaryMemberId", new ArrayList<Long>(memberIdSet));
            criteria.andEqualTo("selector", "diary");
            criteria.andEqualTo("category", "browse");
            criteria.andEqualTo("status", "normal");

            List<DiaryRecord> subDiaryRecordList = diaryRecordService.queryList(example);

            diaryRecordList.addAll(subDiaryRecordList);
        }

        return diaryRecordList;
    }

    /**
     * 我的印象
     *
     * @return
     */
    @RequestMapping(value = "/myDiarys")
    @ResponseBody
    public RestResponse<List<DiaryVO>> myDiarys() {
        RestResponse<List<DiaryVO>> restResponse = new RestResponse<List<DiaryVO>>();

        Member member = PermissionContext.getMember();

        Diary model = new Diary();
        model.setMemberId(member.getId());

        List<Diary> diaryList = diaryService.queryList(model);

        if (diaryList != null) {
            List<DiaryVO> diaryVOList = new LinkedList<DiaryVO>();

            for (Diary diary : diaryList) {
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
     * 我的好友详情
     *
     * @return
     */
    @RequestMapping(value = "/myFriendDetail")
    @ResponseBody
    public RestResponse<Map<String, Object>> myFriendDetail(@RequestParam(required = true) long friendId) {
        RestResponse<Map<String, Object>> restResponse = new RestResponse<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();

        Diary model = new Diary();
        model.setMemberId(friendId);
        //获取好友个人信息
        Member member = memberService.getById(friendId);
        if (member != null) {
            map.put("memberInfo", member);

            //获取好友日志
            List<Diary> diaryList = diaryService.queryList(model);

            if (diaryList != null) {
                List<DiaryVO> diaryVOList = new LinkedList<DiaryVO>();

                for (Diary diary : diaryList) {
                    DiaryVO diaryVO = new DiaryVO();
                    BeanUtils.copyProperties(diary, diaryVO);
                    diaryVOList.add(diaryVO);
                }
                map.put("diaryList", diaryVOList);
            }
            restResponse.setCode(RestResponse.OK);
            restResponse.setData(map);
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("获取好友信息失败！");
        }
        return restResponse;
    }


    /**
     * 他赞过的日记
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/friendUpDiarys")
    @ResponseBody
    public RestResponse<List<DiaryVO>> friendUpDiarys(@RequestParam(defaultValue = "1") int pageNum,
                                                      @RequestParam(defaultValue = "20") int pageSize,
                                                      @RequestParam(required = true) long friendId) {
        RestResponse<List<DiaryVO>> restResponse = new RestResponse<List<DiaryVO>>();

        //获取用户赞过的日志列表

        DiaryRecord diaryRecord = new DiaryRecord();
        diaryRecord.setMemberId(friendId);
        diaryRecord.setSelector("diary");
        diaryRecord.setStatus("normal");
        diaryRecord.setCategory("up");
        List<DiaryRecord> diaryRecordList = diaryRecordService.queryList(diaryRecord);


        Example example = new Example(Diary.class);
        Example.Criteria criteria = example.createCriteria();

        if (diaryRecordList != null && !diaryRecordList.isEmpty()) {
            List<Long> diaryIdList = new LinkedList<Long>();
            for (DiaryRecord record : diaryRecordList) {
                diaryIdList.add(record.getDiaryId());
            }
            criteria.andIn("Id", diaryIdList);
        }

        PageInfo<Diary> diaryPageInfo = diaryService.query(pageNum, pageSize, example);


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
     * 他浏览过的日记
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/friendBrowseDiarys")
    @ResponseBody
    public RestResponse<List<DiaryVO>> friendBrowseDiarys(@RequestParam(defaultValue = "1") int pageNum,
                                                          @RequestParam(defaultValue = "20") int pageSize,
                                                          @RequestParam(required = true) long friendId) {
        RestResponse<List<DiaryVO>> restResponse = new RestResponse<List<DiaryVO>>();


        DiaryRecord diaryRecord = new DiaryRecord();
        diaryRecord.setMemberId(friendId);
        diaryRecord.setSelector("diary");
        diaryRecord.setStatus("normal");
        diaryRecord.setCategory("browse");
        List<DiaryRecord> diaryRecordList = diaryRecordService.queryList(diaryRecord);

        Example example = new Example(Diary.class);
        Example.Criteria criteria = example.createCriteria();

        if (diaryRecordList != null && !diaryRecordList.isEmpty()) {
            List<Long> diaryIdList = new LinkedList<Long>();
            for (DiaryRecord record : diaryRecordList) {
                diaryIdList.add(record.getDiaryId());
            }
            criteria.andIn("Id", diaryIdList);
        }

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
     *
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
     * 举报日记
     *
     * @param diaryId
     * @return
     */
    @RequestMapping(value = "/accuse")
    @ResponseBody
    public RestResponse<Void> accuse(@RequestParam(required = true) long diaryId) {
        RestResponse<Void> restResponse = new RestResponse<Void>();

        Diary diary = diaryService.getById(diaryId);

        if (diary != null) {
            diary.setAccuse("accuse");
            diaryService.update(diary);
            restResponse.setCode(RestResponse.OK);
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("举报日记是吧！");
        }
        return restResponse;
    }

    /**
     * 添加日记
     *
     * @param publishTime 发布时间
     * @param tag         标签【default：默认， event：事件， mood：心情 ，thing：物件，view： 景色】
     * @param brief       日记摘要（选填）
     * @param firstImage  日记首图（选填）
     * @param anonymous   是否匿名【0：不匿名，1：匿名】
     * @param accessRight 访问权限【all：所有人可见，excludeFriend：朋友不可见，friend：朋友可见，oneself：仅自己可见】
     * @param lbsX        经度
     * @param lbsY        纬度
     * @param content     内容
     * @return
     */
    @RequestMapping(value = "/addDiary", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<Void> addDiary(@RequestParam(required = true) String publishTime, @RequestParam(required = true) String tag,
                                       String brief, String firstImage, String contentHeight,
                                       @RequestParam(required = true) Integer anonymous, @RequestParam(required = true) String accessRight,
                                       @RequestParam(required = true) double lbsX, @RequestParam(required = true) double lbsY,
                                       @RequestParam(required = true) String content) {
        RestResponse<Void> restResponse = new RestResponse<Void>();


        Member member = PermissionContext.getMember();
        if (diaryService.addDiary(member.getId(), member.getSex(), publishTime, tag, brief, firstImage, contentHeight, anonymous,
                accessRight, lbsX, lbsY, content)) {
            restResponse.setCode(RestResponse.OK);
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("添加日记失败！");
        }
        return restResponse;
    }

    /**
     * 修改日记
     *
     * @param diaryId     日记ID
     * @param publishTime 发布时间
     * @param tag         标签【default：默认， event：事件， mood：心情 ，thing：物件，view： 景色】
     * @param brief       日记摘要（选填）
     * @param firstImage  日记首图（选填）
     * @param anonymous   是否匿名【0：不匿名，1：匿名】
     * @param accessRight 访问权限【all：所有人可见，excludeFriend：朋友不可见，friend：朋友可见，oneself：仅自己可见】
     * @param lbsX        经度
     * @param lbsY        纬度
     * @param content     内容
     * @return
     */
    @RequestMapping(value = "/editDiary", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<Void> editDiary(@RequestParam(required = true) long diaryId, String publishTime, String tag, String brief, String firstImage,
                                        String contentHeight, Integer anonymous, String accessRight,
                                        double lbsX, double lbsY, String content) {

        RestResponse<Void> restResponse = new RestResponse<Void>();

        Diary diary = diaryService.getById(diaryId);

        if (diary == null) {
            restResponse.setCode("error");
            restResponse.setMessage("无效的日记id！");
            return restResponse;
        }

        diary.setPublishTime(publishTime);
        diary.setTag(tag);
        diary.setBrief(brief);
        diary.setFirstImage(firstImage);
        diary.setContentHeight(contentHeight);
        diary.setAnonymous(anonymous);
        diary.setAccuse(accessRight);
        diary.setAccessRight(accessRight);
        diary.setLbsX(lbsX);
        diary.setLbsY(lbsY);


        if (diaryService.editDiary(diary, content)) {
            restResponse.setCode(RestResponse.OK);
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("添加日记失败！");
        }
        return restResponse;
    }

    /**
     * 添加评论
     *
     * @param diaryId     日记id
     * @param parentId    如果是评论了其他人的评论 评论的id
     * @param isWhisper   是否是悄悄话 【0：不是，1：是】
     * @param commentText 评论内容
     * @param image       评论图片
     * @return
     */
    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<Void> addComment(@RequestParam(required = true) Long diaryId, Long parentId,
                                         @RequestParam(defaultValue = "1") int isWhisper,
                                         @RequestParam(required = true) String commentText,
                                         String image) {
        RestResponse<Void> restResponse = new RestResponse<Void>();

        Member member = PermissionContext.getMember();

        DiaryComment diaryComment = new DiaryComment();
        diaryComment.setDiaryId(diaryId);
        diaryComment.setParentId(parentId);
        diaryComment.setCommentUserId(member.getId());
        diaryComment.setIsWhisper(isWhisper);
        diaryComment.setCommentText(commentText);
        diaryComment.setImage(image);
        diaryComment.setStatus("normal");
        diaryComment.setCreatedAt(new Date());
        if (diaryCommentService.addComment(diaryComment)) {
            restResponse.setCode(RestResponse.OK);
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("评论失败！");
        }
        return restResponse;
    }

    /**
     * 日记操作记录（日记的赞/踩/浏览）
     *
     * @param diaryId
     * @param type      类型【diary：日记， comment：评论】
     * @param commentId 评论id 当type=comment时有效
     * @param category  分类【up：赞，down：踩，browse：浏览】
     * @param lbsX      经度
     * @param lbsY      纬度
     * @return
     */
    @RequestMapping(value = "/addDiaryRecord")
    @ResponseBody
    public RestResponse<Void> addDiaryRecord(@RequestParam(required = true) Long diaryId,
                                             @RequestParam(required = true) String type, Long commentId,
                                             @RequestParam(required = true) String category,
                                             @RequestParam(required = true) double lbsX, @RequestParam(required = true) double lbsY) {
        RestResponse<Void> restResponse = new RestResponse<Void>();

        Member member = PermissionContext.getMember();

        DiaryRecord diaryRecord = new DiaryRecord();
        diaryRecord.setMemberId(member.getId());
        diaryRecord.setDiaryId(diaryId);
        diaryRecord.setCommentId(commentId);
        diaryRecord.setSelector(type);
        diaryRecord.setCategory(category);
        diaryRecord.setLbsX(lbsX);
        diaryRecord.setLbsY(lbsY);
        diaryRecord.setStatus("normal");

        if (diaryRecordService.addDiaryRecord(diaryRecord)) {
            restResponse.setCode(RestResponse.OK);
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("评论失败！");
        }
        return restResponse;
    }

    class Point {
        /**
         * 经度
         */
        private Double lbsX;

        /**
         * 纬度
         */
        private Double lbsY;

        public Point(Double lbsY, Double lbsX) {
            this.lbsY = lbsY;
            this.lbsX = lbsX;
        }

        public Double getLbsX() {
            return lbsX;
        }

        public void setLbsX(Double lbsX) {
            this.lbsX = lbsX;
        }

        public Double getLbsY() {
            return lbsY;
        }

        public void setLbsY(Double lbsY) {
            this.lbsY = lbsY;
        }
    }

}
