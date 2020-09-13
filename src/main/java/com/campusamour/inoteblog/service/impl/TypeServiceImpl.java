package com.campusamour.inoteblog.service.impl;

import com.campusamour.inoteblog.mapper.TypeMapper;
import com.campusamour.inoteblog.model.Type;
import com.campusamour.inoteblog.service.TypeService;
import com.campusamour.inoteblog.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private RedisTemplate redisTemplate;

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
    public List<Type> selectTypesByBlogNumsTopInRedis(Integer size) {
        String name = "types";
        Long setSize = redisTemplate.opsForZSet().size(name);
        if (setSize != null && setSize >= size) {
            // Set<Type> types = redisTemplate.opsForZSet().range(name, 0, size);
            List<Type> types = new ArrayList<>(redisTemplate.opsForZSet().range(name, 0, size));
            return types;
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public boolean saveTypesByBlogNumsTopInRedis(List<Type> types) {
        String name = "types";
        for (Type type : types) {
            redisTemplate.opsForZSet().add(name, type, -1*type.getBlogNums());
        }
        redisTemplate.expire(name, Duration.ofSeconds(DateUtil.getSecondsNextEarlyMorning()));
        return true;
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
