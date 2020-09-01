package com.campusamour.inoteblog.web;

import com.campusamour.inoteblog.model.Comment;
import com.campusamour.inoteblog.model.User;
import com.campusamour.inoteblog.queryentity.CommentToReply;
import com.campusamour.inoteblog.service.BlogService;
import com.campusamour.inoteblog.service.CommentService;
import com.campusamour.inoteblog.service.impl.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.selectBlogCommentsByBlogId(blogId));
        return "blog :: commentList";
    }

    // 新增评论或子评论
    @PostMapping("/comments")
    public String postComment(Comment comment, HttpSession session) {
        System.out.println("parentComment.id: " + comment.getParentComment().getId());
        Long blogId = comment.getBlog().getId();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setUploaderComment(true);
            comment.setNickname(user.getNickname());
        } else {
            comment.setUploaderComment(false);
        }

        comment.setBlogId(blogId);
        comment.setBlog(blogService.searchBlogById(blogId));
        comment.setAvatar(avatar);
        if (comment.getParentComment().getId() != null) {
            comment.setParentCommentId(comment.getParentComment().getId());
            comment.setParentCommentNickname(comment.getParentComment().getNickname());
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }

    // 删除评论
    @GetMapping("/comment/{id}/delete")
    public String delete(@PathVariable("id") Long commentId, Model model){
        // 删两个表t_comment, t_comment_reply
        Long blogId = commentService.removeCommentAndReplyById(commentId);
        /*model.addAttribute("blog", detailedBlog);
        model.addAttribute("comments", comments);*/
        return "redirect:/blog/" + blogId;
    }

}
