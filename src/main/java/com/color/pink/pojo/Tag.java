package com.color.pink.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode
@JsonIgnoreProperties(value = { "handler" })
public class Tag implements Serializable {
    private String id;

    private String title;

    private LocalDateTime createDate;

    private Integer viewTimes;

    private Integer articleNums;

    public Integer getArticleNums() {
        return articleNums;
    }

    public void setArticleNums(Integer articleNums) {
        this.articleNums = articleNums;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Integer getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(Integer viewTimes) {
        this.viewTimes = viewTimes;
    }
}