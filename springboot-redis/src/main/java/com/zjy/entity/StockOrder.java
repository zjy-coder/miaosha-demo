package com.zjy.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Date 2020-07-21 12:22
 * @Created by zjy
 */
@Data
@Accessors(chain = true)
public class StockOrder implements Serializable {
    private Integer id;
    private Integer sid;
    private String name;
    private Date date;

}
