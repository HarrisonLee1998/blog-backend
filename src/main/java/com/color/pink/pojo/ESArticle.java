package com.color.pink.pojo;

import com.color.pink.util.HTMLUtils;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * @author HarrisonLee
 * @date 2020/4/17 0:33
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ESArticle {
    private String id;

    @NotBlank(message = "文章标题不能为空")
    @Length(max = 64)
    private String title;

    private Integer star;

    private Integer viewTimes;

    @NotNull
    private Integer is_open;

    private Integer is_delete;

    private String post_date;

    private String last_update_date;

    @NotBlank(message = "分类长度不能为空")
    @Length(max = 32)
    private String archive_title;

    @NotNull
    private Set<String> tags;

    @NotNull
    private Integer is_reward;

    @NotBlank(message = "markdown内容不能为空")
    @Length(max = 1<<16)
    private String markdown;

    @NotBlank(message = "html内容不能为空")
    @Length(max = 1<<16)
    private String html;

    private String pure_txt;

    public ESArticle(Article article) throws IOException {
        var htmlUtils = new HTMLUtils();
        this.pure_txt = htmlUtils.handleParse(article.getHtml());
        var f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.post_date = f.format(article.getPostDate());
        this.last_update_date = f.format(article.getLastUpdateDate());

        // 原样复制
        this.id = article.getId();
        this.archive_title = article.getArchiveTitle();
        this.title = article.getTitle();
        this.markdown = article.getMarkdown();
        this.html = article.getHtml();
        this.is_open = article.getIsOpen();
        this.is_delete = article.getIsDelete();
        this.star = article.getStar();
        this.is_reward = article.getIsReward();
        this.viewTimes = article.getViewTimes();
        this.tags = article.getTags();
    }
}
