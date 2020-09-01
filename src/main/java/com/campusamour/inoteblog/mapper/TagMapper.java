package com.campusamour.inoteblog.mapper;

import com.campusamour.inoteblog.model.Tag;
import com.campusamour.inoteblog.queryentity.BlogToTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagMapper {
    int insertTag(Tag tag);

    Tag queryTagById(Long id);

    Tag queryTagByName(String name);

    List<Tag> queryAllTags();

    List<Tag> queryTagsByIds(List<Long> ids);

    List<Tag> queryTagsByBlogNumsTopOrALL(@Param("sqlString") String sqlString);

    int updateTagById(Tag tag);

    void deleteTagById(Long id);

    int increaseTagBlogNums(List<Long> typeIds);

    int decreaseTagBlogNums(List<Long>  typeIds);

    List<Long> queryAllBlogToTagByTagId(Long tagId);


    int insertBlogToTag(Long blogId, Long tagId);   // t_blog_tag

    int deleteBlogToTagByBlogId(Long blogId);   // t_blog_tag

}
