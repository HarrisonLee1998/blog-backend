package com.color.pink.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HarrisonLee
 * @date 2020/4/5 1:56
 */
@Setter
@Getter
@ToString
public class ResponseUtil implements Serializable {

    private static final long serialVersionUID = -5372450875750675775L;

    private HttpStatus status;

    // other data
    private Map<String, Object>map;

    public ResponseUtil(HttpStatus status){
        this.status = status;
        this.map = new HashMap<>();
    }


    public ResponseUtil put(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public ResponseUtil put(Map<String, Object>map){
        this.map.putAll(map);
        return this;
    }

    public static ResponseUtil factory(HttpStatus status){
        return new ResponseUtil(status);
    }
}
