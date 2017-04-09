package com.conton.impress.mapper;

import com.conton.base.mapper.BaseMapper;
import com.conton.impress.model.Message;
import com.conton.impress.model.VO.MessageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MessageMapper extends BaseMapper<Message> {
    List<MessageVO> query(@Param("map")  Map<String,Object> map);
}