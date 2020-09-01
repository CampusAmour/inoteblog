package com.campusamour.inoteblog.service.impl;

import com.campusamour.inoteblog.mapper.UserMapper;
import com.campusamour.inoteblog.model.User;
import com.campusamour.inoteblog.service.UserService;
import com.campusamour.inoteblog.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public User checkUser(String username, String password) {
        User user = userMapper.searchByUsernameAndPassword(username, MD5Util.code(password));
        return user;
    }
}
