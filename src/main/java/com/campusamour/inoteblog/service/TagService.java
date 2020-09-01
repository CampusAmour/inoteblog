package com.campusamour.inoteblog.service;

import com.campusamour.inoteblog.model.Tag;

import java.util.List;

public interface TagService {
    int saveTag(Tag tag);

    Tag searchTagById(Long id);

    Tag searchTagByName(String name);

    List<Tag> selectAllTags();

    List<Tag> selectTagsByIds(String ids);

    List<Tag> selectTagsByBlogNumsTopOrAll(Integer size, String sqlString);

    int updateTag(Tag tag);

    void removeTagById(Long id);

    int increaseTagBlogNums(String typeIds);

    int decreaseTagBlogNums(String typeIds);

    List<Long> searchAllBlogToTagByTagId(Long tagId);

    void addBlogToTag(Long blogId, String tagIds);

    int removeBlogToTagByBlogId(Long blogId);
}