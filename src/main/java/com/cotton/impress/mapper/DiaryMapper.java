package com.cotton.impress.mapper;

import com.cotton.base.mapper.BaseMapper;
import com.cotton.impress.model.Diary;
import com.cotton.impress.model.VO.DiaryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DiaryMapper extends BaseMapper<Diary> {

    List<DiaryVO> selectAboutDiaryList(@Param("map")Map<String, Object> map);

    List<DiaryVO> selectSunDiaryList(@Param("map")Map<String, Object> map);

    void resetWeight();

    List<DiaryVO> getAdminByRand(@Param("pageSize")int pageSize);

}