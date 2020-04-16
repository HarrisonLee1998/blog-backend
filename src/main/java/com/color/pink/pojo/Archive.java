package com.color.pink.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

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

    private LocalDateTime createDate;

    private Integer viewTimes;

    @Length(max = 1 << 16)
    private String markdown;

    @Length(max = 1 << 16)
    private String html;

    private Integer articleNums;
}