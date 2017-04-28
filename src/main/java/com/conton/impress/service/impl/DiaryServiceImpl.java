package com.conton.impress.service.impl;

import com.conton.base.service.impl.BaseServiceImpl;
import com.conton.impress.mapper.DiaryCommentMapper;
import com.conton.impress.mapper.DiaryContentMapper;
import com.conton.impress.mapper.DiaryMapper;
import com.conton.impress.mapper.MemberMapper;
import com.conton.impress.model.*;
import com.conton.impress.model.VO.DiaryCommentVO;
import com.conton.impress.model.VO.DiaryDetailVO;
import com.conton.impress.model.VO.DiaryExVO;
import com.conton.impress.model.VO.DiaryVO;
import com.conton.impress.service.DiaryRecordService;
import com.conton.impress.service.DiaryService;
import com.conton.impress.service.MemberFriendService;
import com.conton.impress.service.MemberService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DiaryServiceImpl extends BaseServiceImpl<Diary> implements DiaryService {

    @Autowired
    private DiaryContentMapper diaryContentMapper;
    @Autowired
    private DiaryCommentMapper diaryCommentMapper;
    @Autowired
    private DiaryMapper diaryMapper;
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberFriendService memberFriendService;
    @Autowired
    private DiaryRecordService diaryRecordService;

    @Override
    public DiaryDetailVO getDiaryDetailVObyId(long id) {
        Diary diary = getById(id);

        DiaryVO diaryVO = new DiaryVO();
        BeanUtils.copyProperties(diary,diaryVO);
        //获取用户信息
        Member member = memberService.getById(diaryVO.getMemberId());

        if(member!= null){
            diaryVO.setHeadPortrait(member.getHeadPortrait());
            diaryVO.setFriendName(member.getName());
        }

        //处理影响力
        if(diaryVO.getInfluence() == null || diaryVO.getInfluence().equals("0")){
            diaryVO.setInfluence(memberService.getInfluence(diaryVO.getMemberId()));
        }

        DiaryExVO diaryExVO = convertDiaryVO2DiaryExVO(diaryVO.getMemberId(),diaryVO);

        DiaryDetailVO diaryDetailVO = new DiaryDetailVO();
        BeanUtils.copyProperties(diaryExVO, diaryDetailVO);


        //日记正文
        DiaryContent model = new DiaryContent();
        model.setDiaryId(id);
        List<DiaryContent> diaryContentList = diaryContentMapper.select(model);

        if (diaryContentList != null && diaryContentList.size() > 0) {
            diaryDetailVO.setContent(diaryContentList.get(0).getContent());
        }

        //日记评论

        DiaryComment model2 = new DiaryComment();
        model2.setDiaryId(id);
        List<DiaryComment> diaryCommentList = diaryCommentMapper.select(model2);

        //查找评论
        if (diaryContentList != null && diaryCommentList.size() > 0) {
            List<DiaryCommentVO> diaryCommentVOList = new LinkedList<DiaryCommentVO>();
            for (DiaryComment diaryComment : diaryCommentList) {
                DiaryCommentVO diaryCommentVO = new DiaryCommentVO();
                BeanUtils.copyProperties(diaryComment, diaryCommentVO);

                //查找子评论
                if (diaryComment.getParentId() == null) {
                    DiaryComment model3 = new DiaryComment();
                    model3.setParentId(diaryComment.getId());
                    List<DiaryComment> childCommentList = diaryCommentMapper.select(model3);
                    if (childCommentList != null) {
                        List<DiaryCommentVO> childCommentVOList = new LinkedList<DiaryCommentVO>();
                        for(DiaryComment diaryComment1 :childCommentList){
                            DiaryCommentVO childVO = new DiaryCommentVO();
                            BeanUtils.copyProperties(diaryComment1, childVO);
                            childCommentVOList.add(childVO);
                        }

                        diaryCommentVO.setReplayList(childCommentVOList);
                    }
                    diaryCommentVOList.add(diaryCommentVO);
                }
            }
            diaryDetailVO.setDiaryCommentVOList(diaryCommentVOList);
        }
        return diaryDetailVO;
    }

    @Override
    public boolean editDiary(Diary diary, String content) {

        mapper.updateByPrimaryKeySelective(diary);
        DiaryContent model = new DiaryContent();
        model.setDiaryId(diary.getId());
        model.setStatus("normal");
        DiaryContent diaryContent = diaryContentMapper.selectOne(model);
        diaryContent.setContent(content);

        diaryContentMapper.updateByPrimaryKeySelective(diaryContent);

        return true;
    }

    @Override
    public boolean addDiary(long memberId, String sex, String publishTime, String tag, String brief, String firstImage, String contentHeight, Integer anonymous, String accessRight, double lbsX, double lbsY, String content) {

        Diary diary = new Diary();
        diary.setMemberId(memberId);
        diary.setSex(sex);
        diary.setPublishTime(publishTime);
        diary.setTag(tag);
        diary.setBrief(brief);
        diary.setFirstImage(firstImage);
        diary.setContentHeight(contentHeight);
        diary.setAnonymous(anonymous);
        diary.setAccessRight(accessRight);
        diary.setLbsX(lbsX);
        diary.setLbsY(lbsY);
        diary.setStatus("normal");
        diary.setWeight(0);
        diary.setUpCount(0);
        diary.setDownCount(0);
        diary.setCommentCount(0);
        diary.setBrowseCount(0);
        diary.setCreatedAt(new Date());
        mapper.insert(diary);

        DiaryContent diaryContent = new DiaryContent();
        diaryContent.setDiaryId(diary.getId());
        diaryContent.setContent(content);
        diaryContent.setStatus("normal");
        diaryContent.setCreatedAt(diary.getCreatedAt());
        diaryContentMapper.insert(diaryContent);

        return true;
    }

    @Override
    public PageInfo<DiaryVO> queryAboutDiaryList(int pageNum, int pageSize, Map<String, Object> map) {
        if (pageSize > 0) {
            PageHelper.startPage(pageNum, pageSize);
        }
        return new PageInfo<DiaryVO>(diaryMapper.selectAboutDiaryList(map));
    }

    @Override
    public PageInfo<DiaryVO> querySunDiaryList(int pageNum, int pageSize, Map<String, Object> map) {
        if (pageSize > 0) {
            PageHelper.startPage(pageNum, pageSize);
        }
        return new PageInfo<DiaryVO>(diaryMapper.selectSunDiaryList(map));
    }


    @NotNull
    @Override
    public DiaryExVO convertDiaryVO2DiaryExVO(Long currentMemberId, DiaryVO diaryVO) {

        DiaryExVO diaryExVO = new DiaryExVO();

        //转换普通数据
        BeanUtils.copyProperties(diaryVO, diaryExVO);



        //查看是否是好友日记
        MemberFriend memberFriendModel = new MemberFriend();
        memberFriendModel.setMemberId(currentMemberId);
        memberFriendModel.setFriendMemberId(diaryVO.getMemberId());

        List<MemberFriend> memberFriendList = memberFriendService.queryList(memberFriendModel);

        if(memberFriendList != null && !memberFriendList.isEmpty()){
            diaryExVO.setbFriendDiary(true);
        }else {
            diaryExVO.setbFriendDiary(false);
        }

        //查看是否阅读/点赞/踩
        DiaryRecord diaryRecordModel = new DiaryRecord();
        diaryRecordModel.setDiaryId(diaryVO.getId());
        diaryRecordModel.setMemberId(currentMemberId);
        diaryRecordModel.setSelector("diary");
        diaryRecordModel.setStatus("normal");
        List<DiaryRecord> diaryRecordList = diaryRecordService.queryList(diaryRecordModel);

        if(diaryRecordList != null){
            for(DiaryRecord d : diaryRecordList){
                if(d.getCategory().equals("up")){
                    diaryExVO.setUpStatus(true);

                }else if(d.getCategory().equals("down")){
                    diaryExVO.setUpStatus(true);

                }else if(d.getCategory().equals("browse")){
                    diaryExVO.setUpStatus(true);
                }
            }
        }


        return diaryExVO;
    }

    @Override
    public void resetWeight() {
        diaryMapper.resetWeight();
    }


}
