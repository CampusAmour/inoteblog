package com.campusamour.inoteblog.service;

import com.campusamour.inoteblog.model.User;

public interface UserService {
    User checkUser(String username, String password);

    // Boolean searchUserInRedis(String username);
}
