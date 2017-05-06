package com.cotton.impress.mapper;

import com.cotton.base.mapper.BaseMapper;
import com.cotton.impress.model.Message;
import com.cotton.impress.model.VO.MessageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MessageMapper extends BaseMapper<Message> {
    List<MessageVO> query(@Param("map")  Map<String,Object> map);
}