package com.conton.impress.service.impl;

import com.conton.base.service.impl.BaseServiceImpl;
import com.conton.impress.mapper.DiaryCommentMapper;
import com.conton.impress.mapper.DiaryContentMapper;
import com.conton.impress.mapper.DiaryMapper;
import com.conton.impress.model.Diary;
import com.conton.impress.model.DiaryComment;
import com.conton.impress.model.DiaryContent;
import com.conton.impress.model.VO.DiaryCommentVO;
import com.conton.impress.model.VO.DiaryDetailVO;
import com.conton.impress.model.VO.DiaryVO;
import com.conton.impress.service.DiaryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

    @Override
    public DiaryDetailVO getDiaryDetailVObyId(long id) {
        Diary diary = getById(id);

        DiaryDetailVO diaryDetailVO = new DiaryDetailVO();
        BeanUtils.copyProperties(diary, diaryDetailVO);

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
}
