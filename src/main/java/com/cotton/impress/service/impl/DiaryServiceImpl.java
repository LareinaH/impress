package com.cotton.impress.service.impl;

import com.cotton.base.service.impl.BaseServiceImpl;
import com.cotton.impress.mapper.DiaryCommentMapper;
import com.cotton.impress.mapper.DiaryContentMapper;
import com.cotton.impress.mapper.DiaryMapper;
import com.cotton.impress.model.*;
import com.cotton.impress.model.VO.DiaryCommentVO;
import com.cotton.impress.model.VO.DiaryDetailVO;
import com.cotton.impress.model.VO.DiaryVO;
import com.cotton.impress.service.DiaryRecordService;
import com.cotton.impress.service.DiaryService;
import com.cotton.impress.service.MemberFriendService;
import com.cotton.impress.service.MemberService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public DiaryDetailVO getDiaryDetailVObyId(long currentUserID,long id) {
        Diary diary = getById(id);

        if(diary == null || !diary.getStatus().equals("normal")){
            return null;
        }

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

        DiaryDetailVO diaryDetailVO = new DiaryDetailVO();
        BeanUtils.copyProperties(diaryVO, diaryDetailVO);


        //日记正文
        DiaryContent model = new DiaryContent();
        model.setDiaryId(id);
        List<DiaryContent> diaryContentList = diaryContentMapper.select(model);

        if (diaryContentList != null && diaryContentList.size() > 0) {
            diaryDetailVO.setContent(diaryContentList.get(0).getContent());
        }

        //日记评论

        Map<String,Object> condition = new HashMap<String, Object>();
        condition.put("diaryId",id);
        condition.put("status","normal");

        List<DiaryCommentVO> diaryCommentVOList = diaryCommentMapper.selectDiaryCommentVOList(condition);

        List<DiaryCommentVO> newDiaryCommentVOList = new LinkedList<DiaryCommentVO>();

        //查找评论
        if (diaryCommentVOList != null && diaryCommentVOList.size() > 0) {

            //获取当前用户的所有好友
            MemberFriend memberFriendModel = new MemberFriend();
            memberFriendModel.setMemberId(currentUserID);
            memberFriendModel.setStatus("normal");
            List<MemberFriend> memberFriendList = memberFriendService.queryList(memberFriendModel);

            List<Long> friendIdList = new LinkedList<Long>();

            if(memberFriendList != null && !memberFriendList.isEmpty()) {

                for(MemberFriend memberFriend : memberFriendList){
                    friendIdList.add(memberFriend.getFriendMemberId());
                }

            }

            for (DiaryCommentVO diaryCommentVO : diaryCommentVOList) {

                //查找子评论
                if (diaryCommentVO.getParentId() == null) {

                    condition.put("parentId",diaryCommentVO.getId());
                    List<DiaryCommentVO> childCommentVOList = diaryCommentMapper.selectDiaryCommentVOList(condition);
                    if (childCommentVOList != null) {

                        //遍历回复
                        for (DiaryCommentVO diaryCommentVOChild : childCommentVOList){
                            //存在好友关系
                            if(friendIdList.indexOf(diaryCommentVOChild.getCommentUserId()) >= 0 ||
                                    diaryCommentVOChild.getCommentUserId() == currentUserID){

                                diaryCommentVOChild.setbFriendComment(true);

                            }
                            
                            //如果是 日记主人评论
                            if(diary.getMemberId() == diaryCommentVOChild.getCommentUserId()){
                                diaryCommentVOChild.setCommentUserName("日记主人");
                                diaryCommentVOChild.setCommentUserHeadPortrait(null);
                            }
                        }

                        diaryCommentVO.setReplayList(childCommentVOList);
                    }

                    //存在好友关系
                    if(friendIdList.indexOf(diaryCommentVO.getCommentUserId()) >= 0 ||
                            diaryCommentVO.getCommentUserId() == currentUserID){

                        diaryCommentVO.setbFriendComment(true);

                    }

                    //如果是 日记主人评论
                    if(diary.getMemberId() == diaryCommentVO.getCommentUserId()){
                        diaryCommentVO.setCommentUserName("日记主人");
                        diaryCommentVO.setCommentUserHeadPortrait(null);
                    }
                    newDiaryCommentVOList.add(diaryCommentVO);
                }
            }
        }

        diaryDetailVO.setDiaryCommentVOList(newDiaryCommentVOList);
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

    /*

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

    */

    @Override
    public List<DiaryVO> getAdminByRand(int pageSize) {
        return diaryMapper.getAdminByRand(pageSize);
    }

    @Override
    public void resetWeight() {
        diaryMapper.resetWeight();
    }


}
