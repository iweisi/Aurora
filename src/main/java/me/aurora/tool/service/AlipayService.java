package me.aurora.tool.service;

import me.aurora.tool.domain.AlipayConfig;
import me.aurora.tool.domain.vo.TradeVo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author zhengjie
 * @date 2018/09/30 14:12:41
 */
@CacheConfig(cacheNames = "alipay")
public interface AlipayService {

    /**
     * 处理来自PC的交易请求
     * @param alipay
     * @param trade
     * @return
     * @throws Exception
     */
    String toPayAsPC(AlipayConfig alipay, TradeVo trade) throws Exception;

    /**
     * 处理来自手机网页的交易请求
     * @param alipay
     * @param trade
     * @return
     * @throws Exception
     */
    String toPayAsWeb(AlipayConfig alipay, TradeVo trade) throws Exception;

    /**
     * 查询配置
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    AlipayConfig findById(Long id);

    /**
     * 更新配置
     * @param alipayConfig
     * @return
     */
    @CachePut(key = "#p0.getId()")
    AlipayConfig updateConfig(AlipayConfig alipayConfig);
}
