package com.campusamour.inoteblog.service;

import com.campusamour.inoteblog.model.Type;
import java.util.List;

public interface TypeService {
    int saveType(Type type);

    Type searchTypeById(Long id);

    Type searchTypeByName(String name);

    List<Type> selectAllTypes();

    List<Type> selectTypesByBlogNumsTopOrAll(Integer size, String sqlString);

    int updateType(Type type);

    void removeTypeById(Long id);

    int increaseTypeBlogNums(Long typeId); // update

    int decreaseTypeBlogNums(Long typeId); // update

}
