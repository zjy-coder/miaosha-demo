package com.zjy.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.zjy.service.SpikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @Date 2020-07-21 10:09
 * @Created by zjy
 */
@RestController
@RequestMapping("/spike")
public class SpikeController {

    @Autowired
    private SpikeService spikeService;

    //创建令牌桶实例
    private RateLimiter rateLimiter =RateLimiter.create(20);


    /**
     *
     * @param id 商品id
     * @return
     */
    //秒杀接口--悲观锁实现秒杀
    @RequestMapping("/pessimisticKill")
    public String pessimisticKill(Integer id) {
        try {
            synchronized (this) {
                //根据秒杀商品id 执行秒杀业务
                Integer kill = spikeService.kill(id);
                return "生成的订单号：" + kill;
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     *
     * @param id 商品id
     * @return
     */
    //乐观锁实现秒杀--加版本号,限流
    @RequestMapping("/optimismKill")
    public String optimismKill(Integer id) {
        if(!rateLimiter.tryAcquire(3, TimeUnit.SECONDS)){ //当请求在3s内未拿到令牌直接返回网络繁忙
            return "网络繁忙，请稍后重试......";
        }
        //根据秒杀商品id 执行秒杀业务
        Integer kill = spikeService.optimismKill(id);
        return "生成的订单号：" + kill;
    }


    /**
     *
     * @param id 商品id
     * @param userId 用户id（这里简单传递，真实场景后台应自己获取）
     * @return
     */
    //乐观锁实现秒杀--加版本号,限流，单用户频率限制
    @RequestMapping("/optimismKillLimit")
    public String optimismKillLimit(Integer id,Integer userId){
        //加入令牌桶的限流措施
        if(!rateLimiter.tryAcquire(3, TimeUnit.SECONDS)){ //当请求在3s内未拿到令牌直接返回网络繁忙
            return "网络繁忙，请稍后重试......";
        }
        //先进行单个用户频率限制判断
        //根据秒杀商品id 执行秒杀业务

        Integer kill = spikeService.optimismKill(id);
        return "生成的订单号：" + kill;
    }

}
