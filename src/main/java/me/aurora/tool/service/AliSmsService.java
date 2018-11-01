package me.aurora.tool.service;

import me.aurora.common.config.api.ResponseEntity;
import me.aurora.tool.domain.AliSmsConfig;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author 郑杰
 * @date 2018/10/29 16:59:00
 */
@CacheConfig(cacheNames = "aliSms")
public interface AliSmsService {

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    AliSmsConfig findById(long id);

    @CachePut(key = "#p0.getId()")
    AliSmsConfig updateConfig(AliSmsConfig aliSmsConfig, AliSmsConfig old);

    ResponseEntity send(AliSmsConfig aliSmsConfig, String phone, String code) throws Exception;
}
