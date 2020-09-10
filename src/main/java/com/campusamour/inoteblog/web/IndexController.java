package com.campusamour.inoteblog.web;

import com.campusamour.inoteblog.model.Blog;
import com.campusamour.inoteblog.model.Tag;
import com.campusamour.inoteblog.queryentity.IndexPageBlog;
import com.campusamour.inoteblog.service.BlogService;
import com.campusamour.inoteblog.service.TagService;
import com.campusamour.inoteblog.service.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping(value="/")
    public String index(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, Model model) {
        // 使用PageHelper实现分页
        String orderBy = "update_time desc";
        PageHelper.startPage(pageNum, 6, orderBy);
        List<IndexPageBlog> blogList = blogService.selectIndexPageBlogs("");
        for (IndexPageBlog blog : blogList) {
            String tagIds = blog.getTagIds();
            if (tagIds != null && !"".equals(tagIds)) {
                blog.setTags(tagService.selectTagsByIds(tagIds));
            }
        }
        PageInfo<IndexPageBlog> pageInfo = new PageInfo<>(blogList);
        // System.out.println("total: " + pageInfo.getTotal());
        model.addAttribute("pageInfo", pageInfo);

        // 加载热门分类、标签、博客
        model.addAttribute("types", typeService.selectTypesByBlogNumsTopOrAll(7, null));
        model.addAttribute("tags", tagService.selectTagsByBlogNumsTopOrAll(7, null));
        model.addAttribute("recommendBlogs", blogService.selectBlogsByRecommendAndViewsTop(7));

        return "index";
    }

    /*@GetMapping(value="/search")
    public String search(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @RequestParam String query, Model model) {
        // 使用PageHelper实现分页
        String orderBy = "update_time desc";
        PageHelper.startPage(pageNum, 1, orderBy);
        List<IndexPageBlog> blogList = blogService.selectQueryBlogs(query);
        PageInfo<IndexPageBlog> pageInfo = new PageInfo<>(blogList);
        System.out.println("total: " + pageInfo.getTotal());
        model.addAttribute("pageInfo", pageInfo);
        return "search";
    }*/

    @GetMapping(value="/search")
    public String search(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @RequestParam String query, Model model) {
        // 使用PageHelper实现分页
        String orderBy = "update_time desc";
        PageHelper.startPage(pageNum, 6, orderBy);
        List<IndexPageBlog> blogList = blogService.selectQueryBlogs(query);
        PageInfo<IndexPageBlog> pageInfo = new PageInfo<>(blogList);
        // System.out.println("total: " + pageInfo.getTotal());
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping(value="/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        Blog blog = blogService.searchBlogMarkdownById(id);
        List<Tag> tagList;
        if ("".equals(blog.getTagIds())) {
            tagList = null;
        } else {
            tagList = tagService.selectTagsByIds(blog.getTagIds());
        }
        blog.setTags(tagList);
        model.addAttribute("blog", blog);
        return "blog";
    }
}
