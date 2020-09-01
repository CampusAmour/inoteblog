package com.campusamour.inoteblog.mapper;

import com.campusamour.inoteblog.model.Type;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TypeMapper {
    int insertType(Type type);

    Type queryTypeById(Long id);

    Type queryTypeByName(String name);

    List<Type> queryAllTypes();

    List<Type> queryTypesByBlogNumsTopOrALL(@Param("sqlString") String sqlString);

    int updateTypeById(Type type);

    void deleteTypeById(Long id);

    int updateIncTypeBlogNums(Long typeId);

    int updateDecTypeBlogNums(Long typeId);

}
