package com.color.pink.util;

import lombok.*;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;

/**
 * @author HarrisonLee
 * @date 2020/4/17 23:06
 */

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class PageHelper implements Serializable {

    // 当前页码
    @Min(1)
    private int pageNo;

    // 每页数量
    private int pageSize;

    // 总的元素个数
    private int total;

    // 总页数
    private int pages;

    // 是否是第一页
    private boolean isFirst;

    // 是否是最后一页
    private boolean isLast;

    // 是否有上一页
    private boolean hasPrevious;

    // 是否有下一页
    private boolean hashNext;

    // 数据
    private List<Object>list;
}
