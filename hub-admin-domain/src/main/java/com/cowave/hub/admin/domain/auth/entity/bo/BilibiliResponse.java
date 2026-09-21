package com.cowave.hub.admin.domain.auth.entity.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class BilibiliResponse<T> {

    /**
     * 返回码，0表示成功
     */
    private Integer code;

    /**
     * 响应数据
     */
    private T data;
}
