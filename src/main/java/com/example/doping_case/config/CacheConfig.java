package com.example.doping_case.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // Spring, uygulama başlarken bu isimlerde önbellekler oluşturacak.
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager(
            "tests", 
            "testsAsInfo", 
            "students"
        );
        return cacheManager;
    }
}