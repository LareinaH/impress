package com.conton.impress.mapper;

import com.conton.base.mapper.BaseMapper;
import com.conton.impress.model.Diary;
import com.conton.impress.model.VO.DiaryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DiaryMapper extends BaseMapper<Diary> {

    List<DiaryVO> selectAboutDiaryList(@Param("map")Map<String, Object> map);

    List<DiaryVO> selectSunDiaryList(@Param("map")Map<String, Object> map);

}