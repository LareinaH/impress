package com.cotton.base.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cotton.base.mapper.BaseMapper;
import com.cotton.base.model.BaseModel;
import com.cotton.base.service.BaseService;

public class BaseServiceImpl<ModelType extends BaseModel> implements BaseService<ModelType> {
	@Autowired
	protected BaseMapper<ModelType> mapper;

	@Override
	public boolean insert(ModelType model) {
		model.setCreatedAt(new Date());
		return mapper.insertSelective(model) > 0;
	}

	@Override
	public boolean update(ModelType model) {
		return mapper.updateByPrimaryKeySelective(model) > 0;
	}

	@Override
	public boolean delete(ModelType model) {
		return mapper.delete(model) > 0;
	}

	@Override
	public boolean deleteById(Long id) {
		return mapper.deleteByPrimaryKey(id) > 0;
	}

	@Override
	public ModelType getById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public long count(ModelType model) {
		return mapper.selectCount(model);
	}

	@Override
	public long count(Object condition) {
		return mapper.selectCountByExample(condition);
	}

	@Override
	public PageInfo<ModelType> query(int pageNum, int pageSize) {
		if (pageSize > 0) {
			PageHelper.startPage(pageNum, pageSize);
			PageHelper.orderBy("createdAt DESC");
		}
		return new PageInfo<ModelType>(mapper.selectAll());
	}

	@Override
	public PageInfo<ModelType> query(int pageNum, int pageSize, Object condition) {
		if (pageSize > 0) {
			PageHelper.startPage(pageNum, pageSize);
			PageHelper.orderBy("createdAt DESC");
		}
		return new PageInfo<ModelType>(mapper.selectByExample(condition));
	}

	@Override
	public PageInfo<ModelType> query(int pageNum, int pageSize, ModelType model) {
		if (pageSize > 0) {
			PageHelper.startPage(pageNum, pageSize);
			PageHelper.orderBy("createdAt DESC");
		}
		return new PageInfo<ModelType>(mapper.select(model));
	}

	@Override
	public List<ModelType> queryList() {
		return mapper.selectAll();
	}

	@Override
	public List<ModelType> queryList(ModelType model) {
		return mapper.select(model);
	}

	@Override
	public List<ModelType> queryList(Object condition) {
		return mapper.selectByExample(condition);
	}

	@Override
	public ModelType selectOne(ModelType model) {
		return mapper.selectOne(model);
	}
}
