package com.campusamour.inoteblog.web;

import com.campusamour.inoteblog.model.Blog;
import com.campusamour.inoteblog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.campusamour.inoteblog.util.RandomUtil.requireRandomByDate;

@Controller
public class RecommendBlogController {
    @Autowired
    private BlogService blogService;

    @GetMapping("/recommend/blog")
    public String recommendBlogByDate(Model model) {
        Map<String, Integer> mapResult = requireRandomByDate();
        List<Long> blogIds = blogService.selectAllRecommendAndPublishedBlogId();
        Integer blogSize = blogIds.size();
        Integer middle = mapResult.get("year") * mapResult.get("month") / mapResult.get("dayOfMonth") % blogSize - mapResult.get("dayOfWeek");
        if (middle < 1) {
            middle = (middle + blogSize) % blogSize;
        }
        List<Long> chooseBlogIds = new ArrayList<>();
        chooseBlogIds.add(blogIds.get(middle - 1));
        chooseBlogIds.add(blogIds.get(middle));
        chooseBlogIds.add(blogIds.get((middle+1)%blogSize));
        chooseBlogIds.add(blogIds.get((middle+2)%blogSize));
        List<Blog> recommendBlogs = blogService.selectBlogsbyBlogIds(chooseBlogIds);
        model.addAttribute("recommendBlogs", recommendBlogs);
        return "_fragments :: recommendBlogList";
    }

    @GetMapping("/recommend")
    public String recommendBlog() {
        Blog blog = blogService.searchRandomPublishedBlog();
        return "redirect:/blog/" + blog.getId();
    }
}
