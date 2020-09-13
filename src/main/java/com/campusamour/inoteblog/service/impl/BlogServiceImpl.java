package com.campusamour.inoteblog.service.impl;

import com.campusamour.inoteblog.handle.NotFoundBlogException;
import com.campusamour.inoteblog.mapper.BlogMapper;
import com.campusamour.inoteblog.mapper.TypeMapper;
import com.campusamour.inoteblog.model.*;
import com.campusamour.inoteblog.queryentity.IndexPageBlog;
import com.campusamour.inoteblog.queryentity.PostPageBlog;
import com.campusamour.inoteblog.service.BlogService;
import com.campusamour.inoteblog.util.DateUtil;
import com.campusamour.inoteblog.util.MarkdownUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public int saveBlog(Blog blog) {
        Date time = new Date();
        blog.setUpdateTime(time);
        blog.setCreateTime(time);
        blog.setViews(0);
        blog.setCommentCount(0);
        return blogMapper.insertBlog(blog);

    }

    @Override
    public Blog searchBlogById(String sqlString, Long id) {
        return blogMapper.queryBlogById(sqlString, id);
    }

    @Transactional
    @Override
    public Blog searchBlogMarkdownById(Long id) {
        Blog blog = blogMapper.queryBlogById("and b.published", id);
        if (blog == null) {
            throw new NotFoundBlogException("该博客不存在哟，如有兴趣请联系作者~");
        }
        blogMapper.increaseBlogViewById(id);
        Blog temp_blog = new Blog();
        BeanUtils.copyProperties(blog, temp_blog);
        String content = temp_blog.getContent();
        temp_blog.setContent(MarkdownUtil.markdownToHtmlExtensions(content));
        return temp_blog;
    }

    @Override
    public Blog searchRandomPublishedBlog() {
        Long blogId = blogMapper.queryRandomPublishedBlogId();
        return searchBlogMarkdownById(blogId);
    }

    @Override
    public Blog searchBlogByTitle(String sqlString) {
        return blogMapper.queryBlogByTitle(sqlString);
    }

    @Transactional
    @Override
    public List<Blog> selectAllBlogs () {
        List<Blog> tempBlogList = blogMapper.queryAllBlogs();
        List<Blog> blogList = new ArrayList<>();
        for (Blog blog : tempBlogList) {
            blog.setType(typeMapper.queryTypeById(blog.getTypeId()));
            // System.out.println(blog.getTypeId());
            blogList.add(blog);
        }
        return blogList;
    }

    @Transactional
    @Override
    public int updateBlog(Blog blog) {
        Date time = new Date();
        blog.setUpdateTime(time);
        return blogMapper.updateBlogById(blog);
    }

    @Transactional
    @Override
    public void removeBlogById(Long id) {
        blogMapper.deleteBlogById(id);
    }

    @Override
    public Long searchBlogTypeId(Long blogId) {
        return blogMapper.queryBlogTypeId(blogId);
    }

    @Override
    public String searchBlogTagIds(Long blogId) {
        return blogMapper.queryBlogTagIds(blogId);
    }

    @Override
    public Map<String, List<Blog>> archiveBlogGroupByYear() {
        List<String> years = blogMapper.queryBlogAllYears();
        Map<String, List<Blog>> resultMap = new LinkedHashMap<>();
        for (String year : years) {
            resultMap.put(year, blogMapper.queryBlogsByYear(year));
        }
        return resultMap;
    }

    @Override
    public Long searchBlogCounts() {
        return blogMapper.queryBlogCounts();
    }

    @Override
    public List<Blog> selectBlogsByRecommendAndViewsTopOrRandomRecommendInRedis(Integer size, String name) {
        Long setSize = redisTemplate.opsForZSet().size(name);
        if (setSize != null && setSize >= size) {
            List<Blog> recommendBlogs = new ArrayList<>(redisTemplate.opsForZSet().range(name, 0, size));
            return recommendBlogs;
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public boolean saveBlogsByRecommendAndViewsTopOrRandomRecommendInRedis(List<Blog> blogs, String name) {
        for (Blog blog : blogs) {
            redisTemplate.opsForZSet().add(name, blog, -1*blog.getViews());
        }
        redisTemplate.expire(name, Duration.ofSeconds(DateUtil.getSecondsNextEarlyMorning()));
        return true;
    }

    @Override
    public List<Blog> selectBlogsByRecommendAndViewsTop(Integer size) {
        return blogMapper.queryBlogsByRecommendAndViewsTop(size);
    }

    @Override
    public List<IndexPageBlog> selectIndexPageBlogs(String sqlString) {
        return blogMapper.queryIndexPageBlogs(sqlString);
    }

    @Override
    public List<Blog> selectBlogsByBlogIds(List<Long> blogIds) {
        return blogMapper.queryBlogsbyBlogIds(blogIds);
    }

    @Override
    public List<IndexPageBlog> selectQueryBlogs(String query) {
        return blogMapper.queryBlogsByQuery(query);
    }

    @Override
    public List<PostPageBlog> selectPostPageBlogs() {
        return blogMapper.queryPostPageBlogs();
    }

    @Override
    public Long searchCurrentMaxBlogId() {
        return blogMapper.queryCurrentMaxBlogId();
    }

    @Override
    public List<Long> selectAllRecommendAndPublishedBlogId() {
        return blogMapper.queryAllRecommendAndPublishedBlogId();
    }

    /*
    @Transactional
    @Override
    public String saveCurrentBlogInRedis(Blog blog) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");

        redisTemplate.opsForHash().put(uuid, "id", blog.getId());
        redisTemplate.opsForHash().put(uuid, "title", blog.getTitle());
        redisTemplate.opsForHash().put(uuid, "content", blog.getContent());
        redisTemplate.opsForHash().put(uuid, "firstImage", blog.getFirstImage());
        redisTemplate.opsForHash().put(uuid, "flag", blog.getFlag());
        redisTemplate.opsForHash().put(uuid, "description", blog.getDescription());
        redisTemplate.opsForHash().put(uuid, "tagIds", blog.getTagIds());
        redisTemplate.opsForHash().put(uuid, "typeId", blog.getTypeId());
        redisTemplate.opsForHash().put(uuid, "userId", blog.getUserId());
        redisTemplate.opsForHash().put(uuid, "appreciation", blog.isAppreciation());
        redisTemplate.opsForHash().put(uuid, "shareStatement", blog.isShareStatement());
        redisTemplate.opsForHash().put(uuid, "commentable", blog.isCommentable());
        redisTemplate.opsForHash().put(uuid, "published", blog.isPublished());
        redisTemplate.opsForHash().put(uuid, "recommend", blog.isRecommend());
        redisTemplate.expire(uuid, Duration.ofSeconds(30));
        return uuid;
    }

    @Transactional
    @Override
    public Blog getCurrentBlogInRedis(String uuid) {
        Map<Object, Object> resMap = redisTemplate.opsForHash().entries(uuid);
        if (resMap == null) {
            throw new NotFoundBlogException();
        }
        Blog blog = new Blog();
        blog.setId((Long) resMap.get("id"));
        blog.setTitle((String) resMap.get("title"));
        blog.setContent((String) resMap.get("content"));
        blog.setFirstImage((String) resMap.get("firstImage"));
        blog.setFlag((String) resMap.get("flag"));
        blog.setDescription((String) resMap.get("description"));
        blog.setTagIds((String) resMap.get("tagIds"));
        blog.setTypeId((Long) resMap.get("typeId"));
        blog.setUserId((Long) resMap.get("userId"));
        blog.setAppreciation((Boolean) resMap.get("appreciation"));
        blog.setShareStatement((Boolean) resMap.get("shareStatement"));
        blog.setCommentable((Boolean) resMap.get("commentable"));
        blog.setPublished((Boolean) resMap.get("published"));
        blog.setRecommend((Boolean) resMap.get("recommend"));

        // redisTemplate.opsForHash().delete(uuid);
        return blog;
    }
    */

    @Transactional
    @Override
    public void publishBlogById(Long blogId) {
        blogMapper.updateBlogPublishedById(blogId, new Date());
    }

    @Override
    public Boolean searchBlogPublishedByBlogId(Long blogId) {
        return blogMapper.queryBlogPublishedByBlogId(blogId);
    }

    @Override
    public List<Blog> searchBlogsByBlogQueryEntity(String sqlString) {
        return blogMapper.queryBlogsByBlogQueryEntity(sqlString);
    }

}
