package com.campusamour.inoteblog.mapper;

import com.campusamour.inoteblog.model.User;

public interface UserMapper {
    User searchByUsernameAndPassword(String username, String password);
}
