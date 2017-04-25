package com.conton.impress.service.impl;

import com.conton.base.service.impl.BaseServiceImpl;
import com.conton.impress.model.DiaryRecord;
import com.conton.impress.model.Member;
import com.conton.impress.service.DiaryRecordService;
import com.conton.impress.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MemberServiceImpl extends BaseServiceImpl<Member> implements MemberService{

    @Autowired
    private DiaryRecordService diaryRecordService;

    @Override
    public String getInfluence(Long memberId){

        List<DiaryRecord> diaryRecordList = getAllBrowseMe(memberId);

        Set<Long> memberIdSet = new HashSet<Long>();
        for (DiaryRecord diaryRecord : diaryRecordList) {
            memberIdSet.add(diaryRecord.getMemberId());
        }
        return String.valueOf(memberIdSet.size() * 8);
    }


    @Override
    public List<DiaryRecord> getAllBrowseMe(Long memberId) {

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
}
