package com.conton.impress.service;

import com.conton.base.service.BaseService;
import com.conton.impress.model.DiaryRecord;
import com.conton.impress.model.Member;

import java.util.List;

public interface MemberService extends BaseService<Member>{


    String getInfluence(Long memberId);

    List<DiaryRecord> getAllBrowseMe(Long memberId);
}
