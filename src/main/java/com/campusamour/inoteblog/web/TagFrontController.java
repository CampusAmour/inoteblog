package com.campusamour.inoteblog.web;

import com.campusamour.inoteblog.model.Blog;
import com.campusamour.inoteblog.model.Tag;
import com.campusamour.inoteblog.queryentity.IndexPageBlog;
import com.campusamour.inoteblog.service.BlogService;
import com.campusamour.inoteblog.service.TagService;
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
public class TagFrontController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tag/{id}")
    public String tag(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @PathVariable Long id, Model model) {
        List<Tag> tags = tagService.selectTagsByBlogNumsTopOrAll(id.intValue(), "");
        if (id == 0) {
            id = tags.get(0).getId();
        }
        List<Long> blogIds = tagService.searchAllBlogToTagByTagId(id);

        // 使用PageHelper实现分页
        if (blogIds.size() > 0) {
            String orderBy = "update_time desc";
            PageHelper.startPage(pageNum, 6, orderBy);
            List<Blog> blogList = blogService.selectBlogsbyBlogIds(blogIds);
            if (blogList.size() > 0) {
                for (Blog blog : blogList) {
                    String tagIds = blog.getTagIds();
                    if (tagIds != null && !"".equals(tagIds)) {
                        blog.setTags(tagService.selectTagsByIds(tagIds));
                    }
                }
                PageInfo<Blog> pageInfo = new PageInfo<>(blogList);
                model.addAttribute("pageInfo", pageInfo);
            } else {
                PageInfo<IndexPageBlog> pageInfo = new PageInfo<>();
                model.addAttribute("pageInfo", pageInfo);
            }
        } else {
            PageInfo<IndexPageBlog> pageInfo = new PageInfo<>();
            model.addAttribute("pageInfo", pageInfo);
        }
        model.addAttribute("tags", tags);
        model.addAttribute("activeTagId", id);
        model.addAttribute("message", "该标签下还没有文章哟，敬请期待！");
        return "tag";
    }
}
