package me.aurora.tool.repository;

import me.aurora.tool.domain.QiniuConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 郑杰
 * @date 2018/10/02 10:06:26
 */
public interface QiNiuConfigRepo extends JpaRepository<QiniuConfig,Long> {
}
