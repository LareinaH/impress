package com.cotton.impress.service;

import com.cotton.base.service.BaseService;
import com.cotton.impress.model.DiaryRecord;
import com.cotton.impress.model.Member;

import java.util.List;

public interface MemberService extends BaseService<Member>{


    String getInfluence(Long memberId);

    List<DiaryRecord> getAllBrowseMe(Long memberId);
}
