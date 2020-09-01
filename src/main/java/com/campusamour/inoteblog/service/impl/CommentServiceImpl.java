package com.campusamour.inoteblog.service.impl;

import com.campusamour.inoteblog.mapper.CommentMapper;
import com.campusamour.inoteblog.model.Comment;
import com.campusamour.inoteblog.queryentity.CommentToReply;
import com.campusamour.inoteblog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    private ArrayList<Long> subCommentIdPool = new ArrayList<>(); // 公共的存放子评论的池子

    @Transactional
    @Override
    public void saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        boolean flag = false;
        if (parentCommentId != 0) {
            String parentCommentNickname = (String)redisTemplate.opsForValue().get("commentId" + parentCommentId);
            if (parentCommentNickname == null) {
                flag = true;
                parentCommentNickname = commentMapper.queryCommentsNickNameById(parentCommentId);
                redisTemplate.opsForValue().set("commentId" + parentCommentId, parentCommentNickname, Duration.ofSeconds(60));
            }
            comment.setParentCommentNickname(parentCommentNickname);
            // comment.setParentComment(commentMapper.findByParentCommentId(parentCommentId));
        } else {
            comment.setParentCommentId(null);
            comment.setParentComment(null);
            comment.setParentCommentNickname(null);
        }
        comment.setCreateTime(new Date());

        commentMapper.insertComment(comment);   // 先将comment插入后得到自增id值

        if (flag) { // 若此条为回复信息，则构建t_comment_reply表元素
            commentMapper.insertCommentReplies(parentCommentId, commentMapper.queryNextCommentIdValue());
        }
    }

    @Override
    public List<CommentToReply> selectCommentToReplyByCommentId(Long commentId) {
        return commentMapper.queryCommentToReplyByCommentId(commentId);
    }

    @Transactional
    @Override
    public Long removeCommentAndReplyById(Long commentId) {
        Comment deleteComment = new Comment();
        deleteComment.setId(commentId);
        findChildrenComments(deleteComment, true);

        // 根据commentId获取blogId
        Long blogId = commentMapper.queryBlogIdByCommentId(commentId);

        // 删除t_comment_reply表中的项
        if (subCommentIdPool.size() > 0) {
            commentMapper.deleteCommentToReplyByReplyIds(subCommentIdPool);
        }
        subCommentIdPool.add(commentId);
        // 删除t_comment表中的项
        commentMapper.deleteCommentAndReplyByCommentIds(subCommentIdPool);

        subCommentIdPool.clear();

        return blogId;
    }

    @Override
    public List<Comment> selectBlogCommentsByBlogId(Long blogId) {
        List<Comment> commentList = commentMapper.queryBlogCommentsByBlogIdAndParentCommentIsNull(blogId);
        return requireTotalCommentsCombination(commentList);
    }



    private List<Comment> requireTotalCommentsCombination(List<Comment> comments) {
        if (comments == null) {
            return null;
        }
        List<Comment> commentLists = new ArrayList<>();
        for (Comment comment : comments) {
            Comment tempComment = new Comment();
            BeanUtils.copyProperties(comment, tempComment);
            commentLists.add(tempComment);
        }
        combineChildrenComments(commentLists);
        return commentLists;
    }

    private void combineChildrenComments(List<Comment> comments) {
        for (Comment comment : comments) {
            findChildrenComments(comment, false);
        }
    }

    private void findChildrenComments(Comment comment, boolean isDelete) {
        List<CommentToReply> commentToReplyList = commentMapper.queryCommentToReplyByCommentId(comment.getId());
        if (commentToReplyList.size() != 0) {
            subCommentIdPool.clear(); // 清空公共存放子评论的池子
            for (CommentToReply commentToReply : commentToReplyList) {
                recursiveRequireSubComments(commentToReply);    // 递归搜寻所有子评论
            }
            if (subCommentIdPool.size() > 0 && isDelete == false) {
                List<Comment> commentReplies = commentMapper.queryCommentRepliesByReplyIds(subCommentIdPool);
                comment.setReplyComments(commentReplies);
            }
            if (isDelete == false)
                subCommentIdPool.clear();
        } else {
            if (isDelete == false)
                comment.setReplyComments(new ArrayList<>());
        }
    }

    private void recursiveRequireSubComments(CommentToReply commentToReply)
    {
        if (commentToReply != null) {
            subCommentIdPool.add(commentToReply.getReplyId());
            List<CommentToReply> subCommentToReplyList = commentMapper.queryCommentToReplyByCommentId(commentToReply.getReplyId());
            for (CommentToReply cToReply : subCommentToReplyList) {
                recursiveRequireSubComments(cToReply);
            }
        }
    }

}
