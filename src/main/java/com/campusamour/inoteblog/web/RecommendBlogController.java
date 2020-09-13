package com.campusamour.inoteblog.web;

import com.campusamour.inoteblog.model.Blog;
import com.campusamour.inoteblog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.campusamour.inoteblog.util.DateUtil.requireRandomByDate;

@Controller
public class RecommendBlogController {
    @Autowired
    private BlogService blogService;

    @Value("${random-recommend-size}")
    private Integer randomSize;

    @GetMapping("/recommend/blog")
    public String recommendBlogByDate(Model model) {
        List<Blog> randomRecommendBlogs = null;

        randomRecommendBlogs = blogService.selectBlogsByRecommendAndViewsTopOrRandomRecommendInRedis(randomSize, "randomRecommendBlogs");
        if (randomRecommendBlogs == null) {
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
            chooseBlogIds.add(blogIds.get((middle + 1) % blogSize));
            chooseBlogIds.add(blogIds.get((middle + 2) % blogSize));
            randomRecommendBlogs = blogService.selectBlogsByBlogIds(chooseBlogIds);
            blogService.saveBlogsByRecommendAndViewsTopOrRandomRecommendInRedis(randomRecommendBlogs, "randomRecommendBlogs");
        }
        model.addAttribute("randomRecommendBlogs", randomRecommendBlogs);
        return "_fragments :: randomRecommendBlogList";
    }

    @GetMapping("/recommend")
    public String recommendBlog() {
        Blog blog = blogService.searchRandomPublishedBlog();
        return "redirect:/blog/" + blog.getId();
    }
}
