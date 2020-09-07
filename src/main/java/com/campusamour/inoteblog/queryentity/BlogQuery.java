package com.campusamour.inoteblog.queryentity;

public class BlogQuery {
    private String queryTitle;
    private Long typeId;
    private Boolean recommend;

    public BlogQuery() {
    }

    public String getQueryTitle() {
        return queryTitle;
    }

    public void setQueryTitle(String queryTitle) {
        this.queryTitle = queryTitle;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    @Override
    public String toString() {
        return "BlogQuery{" +
                "queryTitle='" + queryTitle + '\'' +
                ", typeId=" + typeId +
                ", recommend=" + recommend +
                '}';
    }
}
