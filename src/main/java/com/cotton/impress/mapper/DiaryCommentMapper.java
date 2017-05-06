package com.cotton.impress.mapper;

import com.cotton.base.mapper.BaseMapper;
import com.cotton.impress.model.DiaryComment;
import com.cotton.impress.model.VO.DiaryCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DiaryCommentMapper extends BaseMapper<DiaryComment> {

    List<DiaryCommentVO> selectDiaryCommentVOList(@Param("map")Map<String, Object> map);
}