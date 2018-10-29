package me.aurora.repository;

import me.aurora.domain.utils.AliSmsConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 郑杰
 * @date 2018/10/29 16:59:52
 */
public interface AliSmsRepo extends JpaRepository<AliSmsConfig,Long> {
}
