package com.color.pink.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(value = { "handler" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Article implements Serializable {
    private String id;

    @NotBlank(message = "文章标题不能为空")
    @Length(max = 128)
    private String title;

    private Integer star;

    private Integer viewTimes;

    @NotNull
    private Integer isOpen;

    private Integer isDelete;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @NotBlank(message = "分类长度不能为空")
    @Length(max = 32)
    private String archiveTitle;

    @NotNull
    private Set<String>tags;

    @NotNull
    private Integer isReward;

    @NotBlank(message = "markdown内容不能为空")
    @Length(max = 1<<16)
    private String markdown;

    @NotBlank(message = "html内容不能为空")
    @Length(max = 1<<16)
    private String html;
}