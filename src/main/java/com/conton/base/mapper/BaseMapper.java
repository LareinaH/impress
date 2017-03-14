package com.conton.base.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import com.conton.base.model.BaseModel;

public interface BaseMapper<ModelType extends BaseModel> extends Mapper<ModelType>, MySqlMapper<ModelType> {
}
