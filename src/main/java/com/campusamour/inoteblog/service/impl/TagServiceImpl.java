package com.campusamour.inoteblog.service.impl;

import com.campusamour.inoteblog.mapper.TagMapper;
import com.campusamour.inoteblog.model.Tag;
import com.campusamour.inoteblog.model.Type;
import com.campusamour.inoteblog.service.TagService;
import com.campusamour.inoteblog.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public int saveTag(Tag tag) {
        tag.setBlogNums(Long.valueOf("0"));
        return tagMapper.insertTag(tag);
    }

    @Override
    public Tag searchTagById(Long id) {
        return tagMapper.queryTagById(id);
    }

    @Override
    public Tag searchTagByName(String name) {
        return tagMapper.queryTagByName(name);
    }

    @Transactional
    @Override
    public List<Tag> selectAllTags() {
        List<Tag> tagList = tagMapper.queryAllTags();
        return tagList;
    }

    private static List<Long> convertStrToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (ids != null && !"".equals(ids)) {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                list.add(new Long(id));
            }
        }
//        for (Long aLong : list) {
//            System.out.println(aLong);
//        }
        return list;
    }

    @Override
    public List<Tag> selectTagsByBlogNumsTopInRedis(Integer size) {
        String name = "tags";
        Long setSize = redisTemplate.opsForZSet().size(name);
        if (setSize != null && setSize >= size) {
            List<Tag> tags = new ArrayList<>(redisTemplate.opsForZSet().range(name, 0, size));
            return tags;
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public boolean saveTagsByBlogNumsTopInRedis(List<Tag> tags) {
        String name = "tags";
        for (Tag tag : tags) {
            redisTemplate.opsForZSet().add(name, tag, -1*tag.getBlogNums());
        }
        redisTemplate.expire(name, Duration.ofSeconds(DateUtil.getSecondsNextEarlyMorning()));
        return true;
    }

    @Override
    public List<Tag> selectTagsByBlogNumsTopOrAll(Integer size, String sqlString) {
        if (sqlString == null) {
            sqlString = "limit " + size;
        }
        return tagMapper.queryTagsByBlogNumsTopOrALL(sqlString);
    }

    @Override
    public List<Tag> selectTagsByIds(String ids) {
        List<Tag> tagList = tagMapper.queryTagsByIds(TagServiceImpl.convertStrToList(ids));
        return tagList;
    }

    @Transactional
    @Override
    public int updateTag(Tag tag) {
        return tagMapper.updateTagById(tag);
    }

    @Transactional
    @Override
    public void removeTagById(Long id) {
        tagMapper.deleteTagById(id);
    }

    @Override
    public int increaseTagBlogNums(String typeIds) {
        List<Long> typeList = convertStrToList(typeIds);
        return tagMapper.increaseTagBlogNums(typeList);
    }

    @Override
    public int decreaseTagBlogNums(String typeIds) {
        List<Long> typeList = convertStrToList(typeIds);
        return tagMapper.decreaseTagBlogNums(typeList);
    }

    @Override
    public List<Long> searchAllBlogToTagByTagId(Long tagId) {
        return tagMapper.queryAllBlogToTagByTagId(tagId);
    }

    @Override
    public void addBlogToTag(Long blogId, String tagIds) {
        List<Long> tagList = convertStrToList(tagIds);
        for (Long tagId : tagList) {
            tagMapper.insertBlogToTag(blogId, tagId);
        }
    }

    @Override
    public int removeBlogToTagByBlogId(Long blogId) {
        return tagMapper.deleteBlogToTagByBlogId(blogId);
    }
}
