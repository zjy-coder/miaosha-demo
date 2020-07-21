package com.zjy.mapper;

import com.zjy.entity.Stock;

/**
 * @Date 2020-07-21 14:39
 * @Created by zjy
 */
public interface SpikeOptimismMapper {
    Stock checkStock(Integer id);

    Integer updateStock(Stock stock);

}
