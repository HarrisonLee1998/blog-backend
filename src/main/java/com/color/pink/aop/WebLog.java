package com.color.pink.aop;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author HarrisonLee
 * @date 2020/4/5 1:16
 */

@NoArgsConstructor
@Setter
@Getter
public class WebLog {
    /**
     * 操作描述
     */
    private String description;

    /**
     * 操作时间
     */
    private LocalDateTime startTime;

    /**
     * 消耗时间
     */
    private Integer spendTime;

    /**
     * URI
     */
    private String uri;

    /**
     * 请求类型
     */
    private String method;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 请求参数
     */
    private Object parameter;

    /**
     * 请求返回的结果
     */
    private Object result;
}
