package com.campusamour.inoteblog.web;

import com.campusamour.inoteblog.model.Type;
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
public class TypeFrontController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private TagService tagService;

    @GetMapping("/type/{id}")
    public String type(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @PathVariable Long id, Model model) {
        List<Type> types = typeService.selectTypesByBlogNumsTopOrAll(id.intValue(), "");
        if (id == 0) {
            id = types.get(0).getId();
        }
        // 使用PageHelper实现分页
        String orderBy = "update_time desc";
        PageHelper.startPage(pageNum, 6, orderBy);
        List<IndexPageBlog> blogList = blogService.selectIndexPageBlogs(" and b.type_id=" + id);
        for (IndexPageBlog blog : blogList) {
            String tagIds = blog.getTagIds();
            if (tagIds != null && !"".equals(tagIds)) {
                blog.setTags(tagService.selectTagsByIds(tagIds));
            }
        }
        PageInfo<IndexPageBlog> pageInfo = new PageInfo<>(blogList);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("types", types);
        model.addAttribute("activeTypeId", id);
        model.addAttribute("message", "该分类下还没有文章哟，敬请期待！");
        return "type";
    }
}