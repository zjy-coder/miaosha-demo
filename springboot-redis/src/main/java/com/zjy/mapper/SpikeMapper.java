package com.zjy.mapper;

import com.zjy.entity.Stock;
import com.zjy.entity.StockOrder;
import org.apache.ibatis.annotations.Param;

/**
 * @Date 2020-07-21 11:40
 * @Created by zjy
 */
public interface SpikeMapper {
    Stock checkStock(@Param("id") Integer id);

    void updateStock(Stock stock);

    void createOrder(StockOrder stockOrder);
}
