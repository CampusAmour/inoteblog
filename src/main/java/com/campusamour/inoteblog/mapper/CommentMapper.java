package com.campusamour.inoteblog.mapper;

import com.campusamour.inoteblog.model.Comment;
import com.campusamour.inoteblog.queryentity.CommentToReply;

import java.util.List;

public interface CommentMapper {
    List<Comment> queryBlogCommentsByBlogIdAndParentCommentIsNull(Long blogId);

    int insertComment(Comment comment);

    int insertCommentReplies(Long commentId, Long replyId); // com.campusamour.inoteblog.queryentity.CommentToReply

    Long queryNextCommentIdValue();

    Comment findByParentCommentId(Long parentCommentId);

    String queryCommentsNickNameById(Long parentCommentId);

    List<CommentToReply> queryCommentToReplyByCommentId(Long commentId);

    List<Comment> queryCommentRepliesByReplyIds(List<Long> replyIds);

    Long queryBlogIdByCommentId(Long commentId);

    void deleteCommentAndReplyByCommentIds(List<Long> commentId);

    void deleteCommentToReplyByReplyIds(List<Long> replyIds);

}