package com.cotton.impress.web.controller;

import com.cotton.base.common.RestResponse;
import com.cotton.base.util.DistanceUtil;
import com.cotton.impress.model.*;
import com.cotton.impress.model.VO.DiaryCommentVO;
import com.cotton.impress.model.VO.DiaryDetailVO;
import com.cotton.impress.model.VO.DiaryExVO;
import com.cotton.impress.model.VO.DiaryVO;
import com.cotton.impress.service.*;
import com.cotton.impress.web.PermissionContext;
import com.github.pagehelper.PageInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日记相关
 */
@Controller
@RequestMapping("/diary")
public class DiaryController extends ImpressBaseController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberFriendService memberFriendService;
    @Autowired
    private DiaryService diaryService;
    @Autowired
    private DiaryCommentService diaryCommentService;
    @Autowired
    private DiaryRecordService diaryRecordService;

    @Autowired
    private CacheService cacheService;

    @Value("${dtX}")
    private int dtX;
    @Value("${dtY}")
    private int dtY;
    @Value("${shareUrl}")
    private String shareUrl;

    /**
     * 左右印象
     *
     * @param lbsX
     * @param lbsY
     * @param bFirst
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/aboutDiarys")
    @ResponseBody
    public RestResponse<List<DiaryExVO>> aboutDiaries(@RequestParam(required = true) String lbsX,
                                                      @RequestParam(required = true) String lbsY,
                                                      @RequestParam(defaultValue = "false") boolean bFirst,
                                                      @RequestParam(defaultValue = "1") int pageNum,
                                                      @RequestParam(defaultValue = "20") int pageSize) {
        RestResponse<List<DiaryExVO>> restResponse = new RestResponse<List<DiaryExVO>>();

        if (!checkLocation(Double.valueOf(lbsX), Double.valueOf(lbsY))) {
            restResponse.setCode("error");
            restResponse.setMessage("坐标位置读取错误！");

            return restResponse;
        }

        Member member = PermissionContext.getMember();

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("lbX", Double.valueOf(lbsX));
        condition.put("lbY", Double.valueOf(lbsY));

        //访问权限条件
        buildAccessRight(member, condition);

        //伪造管理员数据
        if(bFirst){

            //查看半径十公里内有多少条日记
            condition.put("radius", "10000");
            PageInfo<DiaryVO> diaryPageInfo = diaryService.queryAboutDiaryList(pageNum, pageSize, condition);

            int adminSize = 3;
            if(diaryPageInfo != null && diaryPageInfo.getList() != null && diaryPageInfo.getList().size()<5){
                adminSize = 5;
            }

            List<DiaryVO> diaryVOList = diaryService.getAdminByRand(adminSize);
            forgePosition(diaryVOList, Double.valueOf(lbsX), Double.valueOf(lbsY));

            condition.remove("radius");
        }

        //查询左右印象
        PageInfo<DiaryVO> diaryPageInfo = diaryService.queryAboutDiaryList(pageNum, pageSize, condition);

        if (diaryPageInfo != null) {

            for (DiaryVO diaryVO : diaryPageInfo.getList()) {

                //如果数据库中影响力字段没有值，去获取作者的影响力
                if (diaryVO.getInfluence() == null || diaryVO.getInfluence().equals("0")) {

                    diaryVO.setInfluence(cacheService.getInfluence(diaryVO.getMemberId()));
                }
            }

            //转换ExVO数据
            List<DiaryExVO> diaryVOList = new LinkedList<DiaryExVO>();

            for (DiaryVO diary : diaryPageInfo.getList()) {
                DiaryExVO diaryExVO = getDiaryExVO(member, diary);
                diaryVOList.add(diaryExVO);
            }

            restResponse.setCode(RestResponse.OK);
            restResponse.setData(diaryVOList);
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("读取日记失败！");
        }
        return restResponse;
    }

    private void buildAccessRight(Member member, Map<String, Object> condition) {

        //能够浏览到的日记的权限条件： 1  所有人可以看 2 我自己的日记 3 朋友不可见 不是好友 4 朋友可见 是好友

        MemberFriend memberFriendModel = new MemberFriend();
        memberFriendModel.setMemberId(member.getId());
        memberFriendModel.setStatus("normal");

        List<MemberFriend> memberFriendList = memberFriendService.queryList(memberFriendModel);

        if (memberFriendList != null && !memberFriendList.isEmpty()) {

            List<Long> friendIdList = new LinkedList<Long>();
            for (MemberFriend f : memberFriendList) {
                friendIdList.add(f.getFriendMemberId());
            }
            condition.put("friendIdList", friendIdList);

        }

        condition.put("currentMemberId",member.getId());
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
    public RestResponse<List<DiaryExVO>> sunDiaries(@RequestParam(required = true) String lbsX,
                                                    @RequestParam(required = true) String lbsY,
                                                    @RequestParam(defaultValue = "all") String type,
                                                    @RequestParam(defaultValue = "1") int pageNum,
                                                    @RequestParam(defaultValue = "20") int pageSize) {
        RestResponse<List<DiaryExVO>> restResponse = new RestResponse<List<DiaryExVO>>();

        if (!checkLocation(Double.valueOf(lbsX), Double.valueOf(lbsY))) {
            restResponse.setCode("error");
            restResponse.setMessage("坐标位置读取错误！");

            return restResponse;
        }

        Member member = PermissionContext.getMember();

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("lbX", Double.valueOf(lbsX));
        condition.put("lbY", Double.valueOf(lbsY));

        buildAccessRight(member,condition);

        if(type.equals("all")){
            condition.put("weight", "true");
        }

        PageInfo<DiaryVO> diaryPageInfo = diaryService.querySunDiaryList(pageNum, pageSize, condition);

        if (diaryPageInfo != null && diaryPageInfo.getList() != null && !diaryPageInfo.getList().isEmpty()) {


            for (DiaryVO d : diaryPageInfo.getList()) {
                if (d.getInfluence() == null || d.getInfluence().equals("0")) {

                    d.setInfluence(cacheService.getInfluence(d.getMemberId()));
                }
            }

            //转换ExVO数据
            List<DiaryExVO> diaryVOList = new LinkedList<DiaryExVO>();

            for (DiaryVO diary : diaryPageInfo.getList()) {
                DiaryExVO diaryVO = getDiaryExVO(member, diary);
                diaryVOList.add(diaryVO);
            }

            restResponse.setCode(RestResponse.OK);
            restResponse.setData(diaryVOList);
        } else {
            restResponse.setCode(RestResponse.OK);
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
        List<DiaryRecord> diaryRecordList = memberService.getAllBrowseMe(member.getId());

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

        Map<String, Integer> pointMap = new HashMap<String, Integer>();

        for (Point point : pointList) {

            //转换坐标
            if (point.getLbsX() != null && point.getLbsY() != null) {
                String x = String.valueOf(Math.floor(point.getLbsX() / dtX) * dtX);
                String y = String.valueOf(Math.floor(point.getLbsY() / dtY) * dtY);
                String key = x + "-" + y;
                if (pointMap.containsKey(key)) {

                    pointMap.put(key, pointMap.get(key) + 1);
                } else {
                    pointMap.put(key, 1);
                }
            }
        }

        List<Point> pointResult = new LinkedList<Point>();

        for (Map.Entry<String, Integer> entry : pointMap.entrySet()) {

            String[] xy = entry.getKey().split("-");
            Point point = new Point(Double.valueOf(xy[0]), Double.valueOf(xy[1]), entry.getValue());
            pointResult.add(point);

        }

        map.put("pointList", pointResult);

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


    /**
     * 我的印象
     *
     * @return
     */
    @RequestMapping(value = "/myDiarys")
    @ResponseBody
    public RestResponse<List<DiaryVO>> myDiaries() {
        RestResponse<List<DiaryVO>> restResponse = new RestResponse<List<DiaryVO>>();

        Member member = PermissionContext.getMember();

        Diary model = new Diary();
        model.setMemberId(member.getId());
        model.setStatus("normal");

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
    public RestResponse<Map<String, Object>> myFriendDetail(@RequestParam(required = true) String lbsX,
                                                            @RequestParam(required = true) String lbsY,
                                                            @RequestParam(required = true) long friendId) {
        RestResponse<Map<String, Object>> restResponse = new RestResponse<Map<String, Object>>();

        if (!checkLocation(Double.valueOf(lbsX), Double.valueOf(lbsY))) {
            restResponse.setCode("error");
            restResponse.setMessage("坐标位置读取错误！");

            return restResponse;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        Diary model = new Diary();
        model.setMemberId(friendId);
        model.setStatus("normal");
        //获取好友个人信息
        Member member = memberService.getById(friendId);
        if (member != null) {
            map.put("memberInfo", member);

            //获取该好友的影响力
            String influence = cacheService.getInfluence(friendId);

            //获取好友日志
            List<Diary> diaryList = diaryService.queryList(model);

            if (diaryList != null) {
                List<DiaryExVO> diaryExVOList = new LinkedList<DiaryExVO>();

                for (Diary diary : diaryList) {
                    DiaryVO diaryVO = new DiaryVO();
                    BeanUtils.copyProperties(diary, diaryVO);

                    if (diaryVO.getInfluence() == null) {
                        diaryVO.setInfluence(influence);
                    }

                    //计算当前坐标位置
                    double distance = DistanceUtil.getTwopointsDistance(diaryVO.getLbsX().toString(), diaryVO.getLbsY().toString(), lbsX, lbsY);
                    diaryVO.setDistance((int) distance);

                    //转换成exVO
                    DiaryExVO diaryExVO = getDiaryExVO(member, diaryVO);
                    diaryExVOList.add(diaryExVO);
                }
                map.put("diaryList", diaryExVOList);
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
    public RestResponse<List<DiaryExVO>> friendUpDiaries(@RequestParam(defaultValue = "1") int pageNum,
                                                         @RequestParam(defaultValue = "20") int pageSize,
                                                         @RequestParam(required = true) String lbsX,
                                                         @RequestParam(required = true) String lbsY,
                                                         @RequestParam(required = true) long friendId) {
        RestResponse<List<DiaryExVO>> restResponse = new RestResponse<List<DiaryExVO>>();

        if (!checkLocation(Double.valueOf(lbsX), Double.valueOf(lbsY))) {
            restResponse.setCode("error");
            restResponse.setMessage("坐标位置读取错误！");

            return restResponse;
        }

        Member member = PermissionContext.getMember();

        //获取用户赞过的日志列表
        List<DiaryExVO> diaryExVOList = getFriendUpOrBrowseDiaries(pageNum, pageSize, lbsX, lbsY, friendId, member, "up");

        if (diaryExVOList != null) {
            restResponse.setCode(RestResponse.OK);
            restResponse.setData(diaryExVOList);
        } else {
            restResponse.setCode(RestResponse.OK);
            restResponse.setData(new LinkedList<DiaryExVO>());
        }

        return restResponse;
    }

    private List<DiaryExVO> getFriendUpOrBrowseDiaries(@RequestParam(defaultValue = "1") int pageNum,
                                                       @RequestParam(defaultValue = "20") int pageSize,
                                                       @RequestParam(defaultValue = "30.278992") String lbsX,
                                                       @RequestParam(defaultValue = "120.167536") String lbsY,
                                                       @RequestParam(required = true) long friendId,
                                                       @RequestParam(required = true) Member member,
                                                       @RequestParam(defaultValue = "up") String type) {


        DiaryRecord diaryRecord = new DiaryRecord();
        diaryRecord.setMemberId(friendId);
        diaryRecord.setSelector("diary");
        diaryRecord.setStatus("normal");
        diaryRecord.setCategory(type);
        List<DiaryRecord> diaryRecordList = diaryRecordService.queryList(diaryRecord);


        Example example = new Example(Diary.class);
        Example.Criteria criteria = example.createCriteria();

        if (diaryRecordList != null && !diaryRecordList.isEmpty()) {
            List<Long> diaryIdList = new LinkedList<Long>();
            for (DiaryRecord record : diaryRecordList) {
                diaryIdList.add(record.getDiaryId());
            }
            criteria.andIn("id", diaryIdList);
            criteria.andEqualTo("status", "normal");

            PageInfo<Diary> diaryPageInfo = diaryService.query(pageNum, pageSize, example);


            if (diaryPageInfo != null) {
                List<DiaryExVO> diaryVOList = new LinkedList<DiaryExVO>();

                for (Diary diary : diaryPageInfo.getList()) {
                    DiaryVO diaryVO = new DiaryVO();
                    BeanUtils.copyProperties(diary, diaryVO);
                    DiaryExVO diaryExVO = getDiaryExVO(member, diaryVO);

                    //计算当前坐标位置
                    double distance = DistanceUtil.getTwopointsDistance(diaryExVO.getLbsX().toString(), diaryExVO.getLbsY().toString(), lbsX, lbsY);
                    diaryExVO.setDistance((int) distance);

                    diaryVOList.add(diaryExVO);
                }

                return diaryVOList;
            } else {
                return null;
            }
        } else {
            return null;
        }
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
    public RestResponse<List<DiaryExVO>> friendBrowseDiaries(@RequestParam(defaultValue = "1") int pageNum,
                                                             @RequestParam(defaultValue = "20") int pageSize,
                                                             @RequestParam(required = true) String lbsX,
                                                             @RequestParam(required = true) String lbsY,
                                                             @RequestParam(required = true) long friendId) {
        RestResponse<List<DiaryExVO>> restResponse = new RestResponse<List<DiaryExVO>>();

        if (!checkLocation(Double.valueOf(lbsX), Double.valueOf(lbsY))) {
            restResponse.setCode("error");
            restResponse.setMessage("坐标位置读取错误！");

            return restResponse;
        }

        Member member = PermissionContext.getMember();

        //获取用户浏览过的日志列表
        List<DiaryExVO> diaryExVOList = getFriendUpOrBrowseDiaries(pageNum, pageSize, lbsX, lbsY, friendId, member, "browse");

        if (diaryExVOList != null) {
            restResponse.setCode(RestResponse.OK);
            restResponse.setData(diaryExVOList);
        } else {
            restResponse.setCode(RestResponse.OK);
            restResponse.setData(new LinkedList<DiaryExVO>());
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
    public RestResponse<DiaryDetailVO> getDiaryDetail(@RequestParam(defaultValue = "ios") String format,
                                                      @RequestParam(required = true) long diaryId) {
        RestResponse<DiaryDetailVO> restResponse = new RestResponse<DiaryDetailVO>();

        Member member = PermissionContext.getMember();

        DiaryDetailVO diaryDetailVO = diaryService.getDiaryDetailVObyId(member.getId(), diaryId);

        if (diaryDetailVO != null) {

            buildDiaryExVOInfo(member.getId(), diaryDetailVO);

            if (diaryDetailVO.getDiaryCommentVOList() != null && !diaryDetailVO.getDiaryCommentVOList().isEmpty()) {

                for (DiaryCommentVO diaryCommentVO : diaryDetailVO.getDiaryCommentVOList()) {
                    buildCommentVOInfo(member.getId(), diaryCommentVO);

                    //处理回复的悄悄话
                    if(diaryCommentVO.getReplayList() != null && !diaryCommentVO.getReplayList().isEmpty()) {

                        List<DiaryCommentVO> whisperList = new LinkedList<DiaryCommentVO>();

                        for(DiaryCommentVO child : diaryCommentVO.getReplayList()){
                            if(child.getIsWhisper() == 1
                                    && member.getId() != child.getCommentUserId()
                                    && member.getId() != diaryCommentVO.getCommentUserId()){
                                whisperList.add(child);
                            }
                        }

                        //删除别人的悄悄话内容
                        diaryCommentVO.getReplayList().removeAll(whisperList);
                    }
                }
            }

            if (format.equals("android")) {

                diaryDetailVO.setContent(iosToAndroid(diaryDetailVO.getContent()));
            } else {
                diaryDetailVO.setContent(androidToIos(diaryDetailVO.getContent()));
            }

            //设置分享url
            diaryDetailVO.setShareUrl(shareUrl);
            diaryDetailVO.setFirstShare(member.getFirstShare());

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
            restResponse.setMessage("举报日记失败！");
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
    public RestResponse<Void> addDiary(@RequestParam(defaultValue = "ios") String format,
                                       @RequestParam(required = true) String publishTime, @RequestParam(required = true) String tag,
                                       String brief, String firstImage, String contentHeight,
                                       @RequestParam(required = true) Integer anonymous, @RequestParam(required = true) String accessRight,
                                       @RequestParam(required = true) double lbsX, @RequestParam(required = true) double lbsY,
                                       @RequestParam(required = true) String content) {
        RestResponse<Void> restResponse = new RestResponse<Void>();

        if (!checkLocation(Double.valueOf(lbsX), Double.valueOf(lbsY))) {
            restResponse.setCode("error");
            restResponse.setMessage("坐标位置读取错误！");

            return restResponse;
        }


        Member member = PermissionContext.getMember();

        if (format.equals("android")) {

            content = androidToIos(content);
        }
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
     * 删除日记
     *
     * @param diaryId
     * @return
     */
    @RequestMapping(value = "/deleteDiary")
    @ResponseBody
    public RestResponse<Void> deleteDiary(@RequestParam(required = true) long diaryId) {
        RestResponse<Void> restResponse = new RestResponse<Void>();

        Member member = PermissionContext.getMember();

        Diary diary = diaryService.getById(diaryId);

        if (diary != null) {

            //条件判断，只能删除我的日记里的
            if (member.getId() == diary.getMemberId()) {

                diary.setStatus("delete");
                diaryService.update(diary);
                restResponse.setCode(RestResponse.OK);
            } else {
                restResponse.setCode("error");
                restResponse.setMessage("没有删除权限！");
            }
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("该日记不存在！");
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
    public RestResponse<Void> editDiary(@RequestParam(defaultValue = "ios") String format,
                                        @RequestParam(required = true) long diaryId, String publishTime, String tag, String brief, String firstImage,
                                        String contentHeight, Integer anonymous, String accessRight,
                                        double lbsX, double lbsY, String content) {

        RestResponse<Void> restResponse = new RestResponse<Void>();

        Diary diary = diaryService.getById(diaryId);

        if (diary == null) {
            restResponse.setCode("error");
            restResponse.setMessage("无效的日记id！");
            return restResponse;
        }

        if (format.equals("android") && content != null) {

            content = androidToIos(content);
        }
        diary.setPublishTime(publishTime);
        diary.setTag(tag);
        diary.setBrief(brief);
        diary.setFirstImage(firstImage);
        diary.setContentHeight(contentHeight);
        diary.setAnonymous(anonymous);
        diary.setAccuse(accessRight);
        diary.setAccessRight(accessRight);

        if (checkLocation(Double.valueOf(lbsX), Double.valueOf(lbsY))) {
            diary.setLbsX(lbsX);
            diary.setLbsY(lbsY);
        }

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

        Diary diary = diaryService.getById(diaryId);

        if (diary == null) {
            restResponse.setCode("error");
            restResponse.setMessage("diaryId 不存在！");
            return restResponse;
        }

        DiaryComment parentComment = null;
        if (parentId != null) {
            parentComment = diaryCommentService.getById(parentId);

            if (parentComment == null) {
                restResponse.setCode("error");
                restResponse.setMessage("parentId 不存在！");
                return restResponse;
            }
        }

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

            //发消息
            Message message = new Message();
            message.setDiaryId(diaryId);
            message.setStatus("normal");
            message.setCategory("comment");
            message.setFromMemberId(member.getId());
            message.setToMemberId(diary.getMemberId());
            message.setProcessStatus("unprocessed");
            message.setUpdateAt(new Date());
            messageService.insert(message);

            if (parentComment != null) {
                Message message2 = new Message();
                message.setDiaryId(diaryId);
                message2.setStatus("normal");
                message2.setCategory("comment");
                message2.setFromMemberId(member.getId());
                message2.setToMemberId(parentComment.getCommentUserId());
                message2.setProcessStatus("unprocessed");
                message2.setUpdateAt(new Date());
                messageService.insert(message2);
            }

            restResponse.setCode(RestResponse.OK);
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("评论失败！");
        }
        return restResponse;
    }

    /**
     * 举报评论
     *
     * @param commentId
     * @return
     */
    @RequestMapping(value = "/accuseComment")
    @ResponseBody
    public RestResponse<Void> accuseComment(@RequestParam(required = true) long commentId) {
        RestResponse<Void> restResponse = new RestResponse<Void>();

        DiaryComment diaryComment = diaryCommentService.getById(commentId);

        if (diaryComment != null) {
            diaryComment.setAccuse("accuse");
            diaryCommentService.update(diaryComment);
            restResponse.setCode(RestResponse.OK);
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("举报评论失败！");
        }
        return restResponse;
    }

    /**
     * 删除评论
     *
     * @param commentId
     * @return
     */
    @RequestMapping(value = "/deleteComment")
    @ResponseBody
    public RestResponse<Void> deleteComment(@RequestParam(required = true) long commentId) {
        RestResponse<Void> restResponse = new RestResponse<Void>();

        Member member = PermissionContext.getMember();

        DiaryComment diaryComment = diaryCommentService.getById(commentId);

        if (diaryComment != null) {

            //条件判断，只能删除我的评论和我日记里的评论
            Diary diary = diaryService.getById(diaryComment.getDiaryId());
            if (member.getId() == diaryComment.getCommentUserId() ||
                    member.getId() == diary.getMemberId()) {

                diaryComment.setStatus("delete");
                diaryCommentService.update(diaryComment);
                restResponse.setCode(RestResponse.OK);
            } else {
                restResponse.setCode("error");
                restResponse.setMessage("没有删除权限！");
            }
        } else {
            restResponse.setCode("error");
            restResponse.setMessage("该评论不存在！");
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

        if(!checkLocation(Double.valueOf(lbsX),Double.valueOf(lbsY))){
            restResponse.setCode("error");
            restResponse.setMessage("坐标位置读取错误！");

            return  restResponse;
        }

        Member member = PermissionContext.getMember();

        Diary diary = diaryService.getById(diaryId);

        if (diary == null) {
            restResponse.setCode("error");
            restResponse.setMessage("diaryId错误！");
            return restResponse;
        }
        DiaryRecord diaryRecord = new DiaryRecord();
        diaryRecord.setMemberId(member.getId());
        diaryRecord.setDiaryId(diaryId);
        diaryRecord.setDiaryMemberId(diary.getMemberId());
        diaryRecord.setSelector(type);
        diaryRecord.setCategory(category);
        diaryRecord.setLbsX(lbsX);
        diaryRecord.setLbsY(lbsY);
        diaryRecord.setStatus("normal");

        DiaryComment diaryComment = null;
        if (type.equals("comment")) {
            diaryComment = diaryCommentService.getById(commentId);
            if (diaryComment == null) {
                restResponse.setCode("error");
                restResponse.setMessage("commentId错误！");
                return restResponse;
            }
            diaryRecord.setCommentId(commentId);
            diaryRecord.setCommentMemberId(diaryComment.getCommentUserId());
        }

        if (diaryRecordService.addDiaryRecord(diaryRecord)) {

            //如果是点赞的 发送消息
            if (category.equals("up")) {
                Message message = new Message();
                message.setDiaryId(diaryId);
                message.setStatus("normal");
                message.setCategory("up");
                message.setFromMemberId(member.getId());
                message.setProcessStatus("unprocessed");
                message.setUpdateAt(new Date());

                if (type.equals("diary")) {
                    message.setToMemberId(diary.getMemberId());

                } else {
                    message.setToMemberId(diaryComment.getCommentUserId());
                }
                messageService.insert(message);
            }
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

        private Integer num;


        public Point(Double lbsY, Double lbsX) {
            this.lbsY = lbsY;
            this.lbsX = lbsX;
            this.num = 1;
        }

        public Point(Double lbsX, Double lbsY, Integer num) {
            this.lbsX = lbsX;
            this.lbsY = lbsY;
            this.num = num;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
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

    private String androidToIos(String context) {

        String result = context.replaceAll("<br>", "\n");
        result = result.replaceAll("<img src=\"", "|");
        result = result.replaceAll("\" alt=\"image\">", "|");
        return result;
    }


    private String iosToAndroid(String context) {

        if (context == null || context.isEmpty()) {
            return null;
        }

        String result = context.replaceAll("\\n", "<br>");

        Pattern p = Pattern.compile("\\|", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(result);
        StringBuffer buf = new StringBuffer();
        int i = 0;
        while (m.find()) {
            i++;
            if (i % 2 == 0) {
                m.appendReplacement(buf, "\" alt=\"image\">");
            } else {
                m.appendReplacement(buf, "<img src=\"");
            }
        }
        m.appendTail(buf);

        return buf.toString();
    }

    @NotNull
    private DiaryExVO getDiaryExVO(Member member, DiaryVO diary) {
        DiaryExVO diaryExVO = new DiaryExVO();
        BeanUtils.copyProperties(diary, diaryExVO);
        buildDiaryExVOInfo(member.getId(), diaryExVO);
        return diaryExVO;
    }


    private void forgePosition(List<DiaryVO> diaryVOList, Double lbsX, Double lbsY) {

        for (DiaryVO diaryVO : diaryVOList) {
            Random random = new Random();

            if (random.nextBoolean()) {
                diaryVO.setLbsX(lbsX + random.nextDouble() / 10);
            } else {
                diaryVO.setLbsX(lbsX - random.nextDouble() / 10);
            }

            if (random.nextBoolean()) {
                diaryVO.setLbsY(lbsY + random.nextDouble() / 10);
            } else {
                diaryVO.setLbsY(lbsY - random.nextDouble() / 10);
            }

            //更新管理员坐标位置
            Diary diary = new Diary();
            diary.setId(diaryVO.getId());
            diary.setLbsX(diaryVO.getLbsX());
            diary.setLbsY(diaryVO.getLbsY());
            diaryService.update(diary);
        }

    }

    private boolean checkLocation(double lbsX, double lbsY) {
        if (lbsX < - 90 || lbsX > 90 || lbsX == 0) {
            return false;
        }

        if (lbsY < -180 || lbsY > 180 || lbsY == 0) {
            return false;
        }

        return true;
    }
}
