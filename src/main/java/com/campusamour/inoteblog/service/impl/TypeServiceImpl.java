package com.campusamour.inoteblog.service.impl;

import com.campusamour.inoteblog.mapper.TypeMapper;
import com.campusamour.inoteblog.model.Type;
import com.campusamour.inoteblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeMapper typeMapper;

    @Transactional
    @Override
    public int saveType(Type type) {
        type.setBlogNums(Long.valueOf("0"));
        return typeMapper.insertType(type);
    }

    @Override
    public Type searchTypeById(Long id) {
        return typeMapper.queryTypeById(id);
    }

    @Override
    public Type searchTypeByName(String name) {
        return typeMapper.queryTypeByName(name);
    }

    @Transactional
    @Override
    public List<Type> selectAllTypes() {
        List<Type> typeList = typeMapper.queryAllTypes();
        return typeList;
    }

    @Transactional
    @Override
    public int updateType(Type type) {
        return typeMapper.updateTypeById(type);
    }

    @Transactional
    @Override
    public void removeTypeById(Long id) {
        typeMapper.deleteTypeById(id);
    }

    @Override
    public List<Type> selectTypesByBlogNumsTopOrAll(Integer size, String sqlString) {
        if (sqlString == null) {
            sqlString = "limit " + size;
        }
        return typeMapper.queryTypesByBlogNumsTopOrALL(sqlString);
    }

    @Override
    public int increaseTypeBlogNums(Long typeId) {
        return typeMapper.updateIncTypeBlogNums(typeId);
    }

    @Override
    public int decreaseTypeBlogNums(Long typeId) {
        return typeMapper.updateDecTypeBlogNums(typeId);
    }
}
