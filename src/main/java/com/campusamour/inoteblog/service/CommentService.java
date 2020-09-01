package com.campusamour.inoteblog.service;

import com.campusamour.inoteblog.model.Comment;
import com.campusamour.inoteblog.queryentity.CommentToReply;

import java.util.List;

public interface CommentService {
    List<Comment> selectBlogCommentsByBlogId(Long blogId);

    void saveComment(Comment comment);

    List<CommentToReply> selectCommentToReplyByCommentId(Long commentId);

    Long removeCommentAndReplyById(Long commentId);
}
