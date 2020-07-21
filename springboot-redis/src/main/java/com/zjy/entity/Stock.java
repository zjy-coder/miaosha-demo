package com.zjy.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Date 2020-07-21 11:40
 * @Created by zjy
 */
@Data
public class Stock implements Serializable {
    private Integer id;
    private String name;
    private Integer stock;
    private Integer sales;
    private Integer version;
}
