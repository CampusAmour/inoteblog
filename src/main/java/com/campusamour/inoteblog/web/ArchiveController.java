package com.campusamour.inoteblog.web;

import com.campusamour.inoteblog.model.Blog;
import com.campusamour.inoteblog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;


@Controller
public class ArchiveController {
    @Autowired
    private BlogService blogService;

    @GetMapping(value="/archive")
    public String archive(Model model) {
        Map<String, List<Blog>> resultMap = blogService.archiveBlogGroupByYear();
        model.addAttribute("archiveMap", resultMap);
        model.addAttribute("blogCounts", blogService.searchBlogCounts());
        return "archive";
    }
}
