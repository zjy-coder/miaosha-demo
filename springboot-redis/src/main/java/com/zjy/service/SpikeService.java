package com.zjy.service;

/**
 * @Date 2020-07-21 11:37
 * @Created by zjy
 */
public interface SpikeService {
    //秒杀业务
    Integer kill(Integer id);

    Integer optimismKill(Integer id);
}
