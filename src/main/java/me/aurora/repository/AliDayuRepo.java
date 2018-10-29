package me.aurora.repository;

import me.aurora.domain.utils.AliDayuConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 郑杰
 * @date 2018/10/29 16:59:52
 */
public interface AliDayuRepo extends JpaRepository<AliDayuConfig,Long> {
}
