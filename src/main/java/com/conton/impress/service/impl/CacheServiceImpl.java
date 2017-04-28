package com.conton.impress.service.impl;

import com.conton.impress.service.CacheService;
import com.conton.impress.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017-04-26.
 */
@Service
public class CacheServiceImpl implements CacheService{

    @Autowired
    private MemberService memberService;

    //这里的单引号不能少，否则会报错，被识别是一个对象;
    public static final String CACHE_KEY = "'demoInfo'";


    /**
     * value属性表示使用哪个缓存策略，缓存策略在ehcache.xml
     */
    public static final String DEMO_CACHE_NAME = "demo";

    @Cacheable(value=DEMO_CACHE_NAME,key="'demoInfo_'+#memberId")
    @Override
    public String getInfluence(Long memberId) {
        System.err.println("没有走缓存！"+memberId);
        return memberService.getInfluence(memberId);
    }

}
