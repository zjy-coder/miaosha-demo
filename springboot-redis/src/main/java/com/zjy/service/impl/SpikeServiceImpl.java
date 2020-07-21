package com.zjy.service.impl;

import com.zjy.entity.Stock;
import com.zjy.entity.StockOrder;
import com.zjy.mapper.SpikeMapper;
import com.zjy.mapper.SpikeOptimismMapper;
import com.zjy.service.SpikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Date 2020-07-21 11:37
 * @Created by zjy
 */
@Service
@Slf4j
@Transactional
public class SpikeServiceImpl implements SpikeService {

    @Autowired
    private SpikeMapper spikeMapper; //悲观锁mapper

    @Autowired
    private SpikeOptimismMapper optimismMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Integer kill(Integer id) {
        //检查库存
        Stock stock = spikeMapper.checkStock(id);
        if(stock.getStock().equals(stock.getSales())){
            throw new RuntimeException("库存不足");
        }
        //扣除库存
        stock.setSales(stock.getSales()+1);
        spikeMapper.updateStock(stock);
        //创建订单
        StockOrder stockOrder =new StockOrder();
        stockOrder.setDate(new Date()).setName(stock.getName()).setSid(stock.getId());
        spikeMapper.createOrder(stockOrder);
        return stockOrder.getId();
    }

    @Override
    public Integer optimismKill(Integer id) {
        //限时抢购
        if(!stringRedisTemplate.hasKey("miaosha:"+id)){
            throw new RuntimeException("该商品未在抢购时段");
        }
        //检查库存
        Stock stock = optimismMapper.checkStock(id);
        if(stock.getStock().equals(stock.getSales())){
            throw new RuntimeException("库存不足");
        }
        Integer num = optimismMapper.updateStock(stock);
        if(num == 0){//库存不足
            throw new RuntimeException("抢购失败，请重试");
        }
        StockOrder stockOrder =new StockOrder();
        stockOrder.setDate(new Date()).setName(stock.getName()).setSid(stock.getId());
        spikeMapper.createOrder(stockOrder);
        return stockOrder.getId();
    }


}
