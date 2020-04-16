package com.color.pink.util;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author HarrisonLee
 * @date 2020/4/14 18:34
 */
@Component
@Setter
@Getter
@ToString
@ConfigurationProperties(prefix = "qiniu")
public class Qiniu {
    private String Host;
    private String AccessKey;
    private String SecretKey;
    private String Bucket;
}
