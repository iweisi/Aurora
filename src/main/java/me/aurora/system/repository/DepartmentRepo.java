package me.aurora.system.repository;

import me.aurora.system.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author 郑杰
 * @date 2018/10/25 11:36:41
 */
public interface DepartmentRepo extends JpaRepository<Department,Long>, JpaSpecificationExecutor {

    /**
     * 根据PID查询
     * @param parseInt
     * @return
     */
    List<Department> findByPid(int parseInt);
}
