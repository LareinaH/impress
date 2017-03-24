package com.conton.impress.service.impl;

import com.conton.base.service.impl.BaseServiceImpl;
import com.conton.impress.mapper.DiaryCommentMapper;
import com.conton.impress.mapper.DiaryContentMapper;
import com.conton.impress.model.Diary;
import com.conton.impress.model.DiaryComment;
import com.conton.impress.model.DiaryContent;
import com.conton.impress.model.VO.DiaryCommentVO;
import com.conton.impress.model.VO.DiaryDetailVO;
import com.conton.impress.service.DiaryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class DiaryServiceImpl extends BaseServiceImpl<Diary> implements DiaryService {

    @Autowired
    private DiaryContentMapper diaryContentMapper;
    @Autowired
    private DiaryCommentMapper diaryCommentMapper;

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
        if(diaryContentList != null && diaryCommentList.size()>0){
            List<DiaryCommentVO> diaryCommentVOList = new LinkedList<DiaryCommentVO>();
            for(DiaryComment diaryComment : diaryCommentList){
                DiaryCommentVO diaryCommentVO = new DiaryCommentVO();
                BeanUtils.copyProperties(diaryComment,diaryCommentVO);
                if(diaryComment.getParentId() != null){
                    DiaryComment parentComment = diaryCommentMapper.selectByPrimaryKey(diaryComment.getParentId());
                    if(parentComment!= null){
                        DiaryCommentVO parentVO = new DiaryCommentVO();
                        BeanUtils.copyProperties(parentComment,parentVO);
                        diaryCommentVO.setParentDiaryComment(parentVO);
                    }
                }
                diaryCommentVOList.add(diaryCommentVO);

            }
            diaryDetailVO.setDiaryCommentVOList(diaryCommentVOList);
        }
        return diaryDetailVO;
    }
}
