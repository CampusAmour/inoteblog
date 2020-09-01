package com.campusamour.inoteblog.queryentity;

import com.campusamour.inoteblog.model.Type;

import java.util.Date;

public class PostPageBlog {
    private Long id;
    private String title;
    private boolean recommend;
    private boolean published;
    private Date updateTime;
    //Type
    private Type type;

    public PostPageBlog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PostPageBlog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", recommend=" + recommend +
                ", published=" + published +
                ", updateTime=" + updateTime +
                ", type=" + type +
                '}';
    }
}
