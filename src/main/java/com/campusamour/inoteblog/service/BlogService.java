package com.campusamour.inoteblog.service;

import com.campusamour.inoteblog.model.Blog;
import com.campusamour.inoteblog.queryentity.IndexPageBlog;
import com.campusamour.inoteblog.queryentity.PostPageBlog;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog searchBlogById(Long id);

    Blog searchBlogMarkdownById(Long id);

    int saveBlog(Blog blog);

    Blog searchBlogByTitle(Long id, String title);

    Blog searchRandomPublishedBlog();

    List<Blog> selectAllBlogs();

    List<Blog> selectBlogsByRecommendAndViewsTop(Integer size);

    List<IndexPageBlog> selectIndexPageBlogs(String sqlString);

    List<Blog> selectBlogsbyBlogIds(List<Long> blogIds);

    List<IndexPageBlog> selectQueryBlogs(String query); // 按title或内容搜索

    List<PostPageBlog> selectPostPageBlogs();

    int updateBlog(Blog blog);

    void removeBlogById(Long id);

    Long searchBlogTypeId(Long id);

    Long searchBlogCounts();

    String searchBlogTagIds(Long blogId);

    Map<String, List<Blog>> archiveBlogGroupByYear();

    Long searchCurrentMaxBlogId();

    List<Long> selectAllRecommendAndPublishedBlogId();

}
