package com.campusamour.inoteblog.web.admin;

import com.campusamour.inoteblog.model.Blog;
import com.campusamour.inoteblog.model.Tag;
import com.campusamour.inoteblog.model.Type;
import com.campusamour.inoteblog.model.User;
import com.campusamour.inoteblog.queryentity.PostPageBlog;
import com.campusamour.inoteblog.service.BlogService;
import com.campusamour.inoteblog.service.TagService;
import com.campusamour.inoteblog.service.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, Model model) {
        // 使用PageHelper实现分页
        String orderBy = "update_time desc";
        PageHelper.startPage(pageNum, 10, orderBy);
        List<PostPageBlog> blogList = blogService.selectPostPageBlogs();
        PageInfo<PostPageBlog> pageInfo = new PageInfo<>(blogList);
        model.addAttribute("pageInfo", pageInfo);

        // 加载所有types
        model.addAttribute("types", typeService.selectAllTypes());
        return "admin/blogs-admin";
    }

    // 未实现
    @GetMapping("/blogs/search")
    public String searchBlogs(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, Model model) {
        // 使用PageHelper实现分页
        String orderBy = "id desc";
        PageHelper.startPage(pageNum, 10, orderBy);
        List<Blog> blogList = blogService.selectAllBlogs();
        PageInfo<Blog> pageInfo = new PageInfo<>(blogList);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/blogs-admin :: blogList";
    }

    @GetMapping("/blogs/write")
    public String writeBlogs(Model model) {
        Blog blog = new Blog();
        Date time = new Date();
        blog.setUpdateTime(time);
        blog.setCreateTime(time);
        model.addAttribute("blog", blog);

        model.addAttribute("types", typeService.selectAllTypes());
        model.addAttribute("tags", tagService.selectAllTags());
        return "admin/blogs-input";
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/blogs")
    public String submitBlog(Blog blog, Long originalTypeId, String originalTagIds, RedirectAttributes attributes, HttpSession session) {
        if (blog.getFlag() != null && !"".equals(blog.getFlag()) && !",".equals(blog.getFlag())) {
            String flag = blog.getFlag();
            String[] temp_flag = flag.split(",");
            blog.setFlag(temp_flag[1]);
        } else {
            blog.setFlag("原创");
        }
        if (blog.getId() == null) {  // 第一次提交博客
            blog.setUserId(((User) session.getAttribute("user")).getId());
        }
        // 将原type和原tags进行删除
        if (originalTypeId != null)
        {
            Long typeId = blogService.searchBlogTypeId(blog.getId()); // blogId
            typeService.decreaseTypeBlogNums(typeId);
        }
        if (!"".equals(originalTagIds) && originalTagIds != null) {
            String tagIds = blogService.searchBlogTagIds(blog.getId()); // blogId
            if (tagIds != null && !"".equals(tagIds))
                tagService.decreaseTagBlogNums(tagIds);
        }
        Blog temp_blog = blogService.searchBlogByTitle(blog.getId(), blog.getTitle());
        if (temp_blog != null) {
            attributes.addFlashAttribute("message", "博客标题已存在");
            return "redirect:/admin/blogs/write";
        }
        Long typeId = blog.getTypeId();
        blog.setType(typeService.searchTypeById(typeId));

        String tagIds = blog.getTagIds();
        if ("".equals(tagIds) || tagIds == null) {  // 没有选择tag时，tagIds为空
            blog.setTags(new ArrayList<Tag>());
        } else {
            blog.setTags(tagService.selectTagsByIds(tagIds));
        }
        if (blog.getId() == null) {
            int b = blogService.saveBlog(blog);
            int t1 = typeService.increaseTypeBlogNums(typeId);
            int t2 = tagService.increaseTagBlogNums(tagIds);

            // t_blog_tag表
            if (!"".equals(tagIds) && tagIds != null) {
                Long blogId = blogService.searchCurrentMaxBlogId();
                tagService.addBlogToTag(blogId, tagIds);
            }

            if (b == 0 || t1 == 0 || t2 == 0) {
                attributes.addFlashAttribute("message", "新增博客失败");
            } else {
                attributes.addFlashAttribute("message", "新增博客成功");
            }
        } else {
            int b = blogService.updateBlog(blog);
            int t1 = typeService.increaseTypeBlogNums(typeId);
            int t2 = 1;

            // t_blog_tag表
            tagService.removeBlogToTagByBlogId(blog.getId());
            if (!"".equals(tagIds) && tagIds != null) { // tagIds不为空时，才访问数据库增加tag对应的blog_num
                t2 = tagService.increaseTagBlogNums(tagIds);
                // t_blog_tag表
                tagService.addBlogToTag(blog.getId(), tagIds);
            }

            if (b == 0 || t1 == 0 || t2 == 0) {
                attributes.addFlashAttribute("message", "修改博客失败");
            } else {
                attributes.addFlashAttribute("message", "修改博客成功");
            }
        }
        return "redirect:/admin/blogs";
    }

    @Transactional
    @GetMapping("/blogs/{id}/edit")
    public String editBlog(@PathVariable Long id, Model model) {
        model.addAttribute("types", typeService.selectAllTypes());
        model.addAttribute("tags", tagService.selectAllTags());

        model.addAttribute("originalTypeId", blogService.searchBlogTypeId(id));
        model.addAttribute("originalTagIds", blogService.searchBlogTagIds(id));

        Blog blog = blogService.searchBlogById(id);
        blog.listToString();
        model.addAttribute("blog", blog);
        return "admin/blogs-input";
    }

    @Transactional
    @GetMapping("/blogs/{id}/delete")
    public String deleteBlog(@PathVariable Long id, RedirectAttributes attributes, Model model) {
        Long typeId = blogService.searchBlogTypeId(id); // blogId
        int t1 = typeService.decreaseTypeBlogNums(typeId);

        String tagIds = blogService.searchBlogTagIds(id);   // blogId
        int t2 = 1;
        if (tagIds != null && !"".equals(tagIds))
            tagService.decreaseTagBlogNums(tagIds);
        if (t1 == 0 || t2 == 0) {
            attributes.addFlashAttribute("message", "删除操作失败");
            return "redirect:/admin/blogs";
        }
        blogService.removeBlogById(id);
        attributes.addFlashAttribute("message", "删除操作成功");
        return "redirect:/admin/blogs";
    }

    // test
/*    @GetMapping("/test/blog/{size}")
    public @ResponseBody List<Blog> test1(@PathVariable Integer size) {
        List<Blog> blogList = blogService.selectBlogsByRecommendAndViewsTop(size);
        for (Blog blog : blogList) {
            System.out.println(blog);
        }
        return blogList;
    }

    @GetMapping("/test/tag/{size}")
    public @ResponseBody List<Tag> test2(@PathVariable Integer size) {
        List<Tag> tagList = tagService.selectTagsByBlogNumsTop(size);
        for (Tag tag : tagList) {
            System.out.println(tag);
        }
        return tagList;
    }

    @GetMapping("/test/type/{size}")
    public @ResponseBody List<Type> test3(@PathVariable Integer size) {
        List<Type> typeList = typeService.selectTypesByBlogNumsTop(size);
        for (Type type : typeList) {
            System.out.println(type);
        }
        return typeList;
    }*/
}
