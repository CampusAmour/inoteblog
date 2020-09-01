package com.campusamour.inoteblog.queryentity;

public class BlogToTag {
    private Long id;
    private Long blogId;
    private Long tagId;

    public BlogToTag() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return "BlogToTag{" +
                "id=" + id +
                ", blogId=" + blogId +
                ", tagId=" + tagId +
                '}';
    }
}
