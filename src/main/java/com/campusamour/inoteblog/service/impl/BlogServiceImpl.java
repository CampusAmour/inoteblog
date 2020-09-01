package com.campusamour.inoteblog.service.impl;

import com.campusamour.inoteblog.handle.NotFoundBlogException;
import com.campusamour.inoteblog.mapper.BlogMapper;
import com.campusamour.inoteblog.mapper.TypeMapper;
import com.campusamour.inoteblog.model.Blog;
import com.campusamour.inoteblog.queryentity.IndexPageBlog;
import com.campusamour.inoteblog.queryentity.PostPageBlog;
import com.campusamour.inoteblog.service.BlogService;
import com.campusamour.inoteblog.util.MarkdownUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private TypeMapper typeMapper;

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
    public Blog searchBlogById(Long id) {
        return blogMapper.queryBlogById(id);
    }

    @Transactional
    @Override
    public Blog searchBlogMarkdownById(Long id) {
        Blog blog = blogMapper.queryBlogById(id);
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
    public Blog searchBlogByTitle(Long id, String title) {
        return blogMapper.queryBlogByTitle(id, title);
    }

    @Transactional
    @Override
    public List<Blog> selectAllBlogs () {
        List<Blog> tempBlogList = blogMapper.queryAllBlogs();
        List<Blog> blogList = new ArrayList<>();
        for (Blog blog : tempBlogList) {
            blog.setType(typeMapper.queryTypeById(blog.getTypeId()));
            System.out.println(blog.getTypeId());
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
    public List<Blog> selectBlogsByRecommendAndViewsTop(Integer size) {
        return blogMapper.queryBlogsByRecommendAndViewsTop(size);
    }

    @Override
    public List<IndexPageBlog> selectIndexPageBlogs(String sqlString) {
        return blogMapper.queryIndexPageBlogs(sqlString);

    }

    @Override
    public List<Blog> selectBlogsbyBlogIds(List<Long> blogIds) {
        return blogMapper.queryBlogsbyBlogIds(blogIds);
    }

    @Override
    public List<IndexPageBlog> selectQueryBlogs(String query) {
        return blogMapper.queryBlogsByQuery(query);
    }

    @Override
    public List<PostPageBlog> selectPostPageBlogs() {
        return  blogMapper.queryPostPageBlogs();
    }

    @Override
    public Long searchCurrentMaxBlogId() {
        return blogMapper.queryCurrentMaxBlogId();
    }

    @Override
    public List<Long> selectAllRecommendAndPublishedBlogId() {
        return blogMapper.queryAllRecommendAndPublishedBlogId();
    }
}
