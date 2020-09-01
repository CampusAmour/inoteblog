package com.campusamour.inoteblog.queryentity;

public class CommentToReply {
    private Long id;
    private Long commentId;
    private Long replyId;

    public CommentToReply() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    @Override
    public String toString() {
        return "CommentToReply{" +
                "id=" + id +
                ", commentId=" + commentId +
                ", replyId=" + replyId +
                '}';
    }
}
