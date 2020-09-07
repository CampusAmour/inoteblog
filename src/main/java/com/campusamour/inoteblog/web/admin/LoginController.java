package com.campusamour.inoteblog.web.admin;

import com.campusamour.inoteblog.model.Blog;
import com.campusamour.inoteblog.model.Tag;
import com.campusamour.inoteblog.model.User;
import com.campusamour.inoteblog.service.BlogService;
import com.campusamour.inoteblog.service.TagService;
import com.campusamour.inoteblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private TagService tagService;

    @GetMapping
    public String loginPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println(request.getSession().getAttribute("user") );
        if (request.getSession().getAttribute("user") != null) {
            return "admin/login-index";
        } else {
            return "admin/login";
        }
        /*System.out.println("###################");
        System.out.println(request.getSession().getAttribute("user") == null);
        return "admin/login";*/
    }

    @GetMapping(value = {"/back", "/login"})
    public String back(HttpSession session, RedirectAttributes attributes) {
        if (session.getAttribute("user") != null) {
            session.setAttribute("welcome", false);
            return "admin/login-index";
        } else {
            attributes.addFlashAttribute("message", "抱歉，请重新登录");
            return "redirect:/admin";
        }
    }

    @PostMapping(value = "/login")
    public String login(@RequestParam String username, @RequestParam String password, @RequestParam String verification, HttpSession session, RedirectAttributes attributes) {
        if (username == null || password == null || verification == null) {
            attributes.addFlashAttribute("message", "用户名或密码或验证码不能为空!");
            return "redirect:/admin";
        }
        User user = userService.checkUser(username, password);
        if (user != null) {
            System.out.println("verification: "+ verification);
            System.out.println("session.kaptcha: " + session.getAttribute("kaptcha"));
            System.out.println(verification.toLowerCase());
            if (session.getAttribute("kaptcha").equals(verification.toLowerCase())) {
                session.setAttribute("user", user);
                Blog blog = randomRecommendBlog();
                session.setAttribute("blog", blog);
                session.setAttribute("welcome", true);
                return "admin/login-index";
            } else {
                attributes.addFlashAttribute("message", "验证码有误，请重新登录!");
                return "redirect:/admin";
            }

        } else {
            attributes.addFlashAttribute("message", "用户名或密码错误!");
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        // session.setAttribute("user", null);
        return "redirect:/admin";
    }

    private Blog randomRecommendBlog() {
        Blog blog = blogService.searchRandomPublishedBlog();
        List<Tag> tagList;
        if ("".equals(blog.getTagIds())) {
            tagList = null;
        } else {
            tagList = tagService.selectTagsByIds(blog.getTagIds());
        }
        blog.setTags(tagList);
        return blog;
    }
}
