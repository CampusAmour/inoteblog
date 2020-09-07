package com.campusamour.inoteblog.mapper;

import com.campusamour.inoteblog.model.Blog;
import com.campusamour.inoteblog.queryentity.BlogQuery;
import com.campusamour.inoteblog.queryentity.IndexPageBlog;
import com.campusamour.inoteblog.queryentity.PostPageBlog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BlogMapper {
    int insertBlog(Blog blog);

    Blog queryBlogById(@Param("sqlString") String sqlString, Long id);

    Blog queryBlogByTitle(@Param("sqlString") String sqlString);   // 查找除了与id项title相等以外的title博客

    Long queryRandomPublishedBlogId();

    List<Blog> queryAllBlogs();

    List<Blog> queryBlogsByRecommendAndViewsTop(Integer size);

    // sql注入
    List<IndexPageBlog> queryIndexPageBlogs(@Param("sqlString") String sqlString);

    List<Blog> queryBlogsbyBlogIds(List<Long> blogIds);

    List<IndexPageBlog> queryBlogsByQuery(String query);

    List<PostPageBlog> queryPostPageBlogs();

    int updateBlogById(Blog blog);

    void deleteBlogById(Long id);

    Long queryBlogTypeId(Long blogId);

    String queryBlogTagIds(Long blogId);

    int increaseBlogViewById(Long blogId);

    List<String>  queryBlogAllYears();

    List<Blog> queryBlogsByYear(String year);

    Long queryBlogCounts();

    Long queryCurrentMaxBlogId();

    List<Long> queryAllRecommendAndPublishedBlogId();

    void updateBlogPublishedById(Long blogId, Date updateTime);

    Boolean queryBlogPublishedByBlogId(Long blogId);

    List<Blog> queryBlogsByBlogQueryEntity(@Param("sqlString") String sqlString);

}
