package com.color.pink.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(value = { "handler" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Archive implements Serializable {
    private Integer order;

    @NotBlank(message = "分类名称不能为空")
    @Length(max = 32)
    private String title;

    private String imgUrl;

    private int articleNums;

    private Set<Article> articles;
}