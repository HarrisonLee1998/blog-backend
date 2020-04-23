package com.color.pink.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Archive implements Serializable {
    private Integer order;

    @NotBlank(message = "分类名称不能为空")
    @Length(max = 32)
    private String title;

    private String imgUrl;

    private Set<Article> articles;
}