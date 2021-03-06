package com.campusamour.inoteblog.model;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Type {
    private Long id;

    @NotBlank(message = "分类名称不能为空")
    private String name;

    private Long blogNums;

    private List<Blog> blogs = new ArrayList<>();

    public Type() {
    }

    public Long getBlogNums() {
        return blogNums;
    }

    public void setBlogNums(Long blogNums) {
        this.blogNums = blogNums;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }



    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
