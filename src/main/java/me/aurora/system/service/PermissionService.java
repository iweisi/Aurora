package me.aurora.system.service;

import me.aurora.system.domain.Permission;
import me.aurora.system.domain.Role;
import me.aurora.system.repository.spec.PermissionSpec;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 郑杰
 * @date 2018/08/23 17:30:58
 */
@CacheConfig(cacheNames = "permission")
public interface PermissionService {

    /**
     * 查询所有权限
     * @param permissionSpec
     * @param pageable
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    Map getPermissionInfo(PermissionSpec permissionSpec, Pageable pageable);

    /**
     * 新增权限
     * @param permission
     * @return
     */
    @CacheEvict(allEntries = true)
    void insert(Permission permission);

    /**
     * 根据id查询权限
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    Permission findById(Long id);

    /**
     * 更新权限
     *
     * @param old
     * @param permission
     * @return
     */
    @CacheEvict(allEntries = true)
    void update(Permission old, Permission permission);

    /**
     * 删除权限
     * @param permission
     * @return
     */
    @CacheEvict(allEntries = true)
    void delete(Permission permission);

    /**
     * 查询所有的权限
     * @return
     */
    @Cacheable(key = "'getAll'")
    List<Permission> getAll();

    /**
     * 构建树形结构数据
     * @param permissions
     * @return
     */
    @Cacheable(key = "'buildPermissionTree'")
    List<Map<String, Object>> buildPermissionTree(List<Permission> permissions);

    /**
     * 根据上级类目ID查询权限
     * @param pid
     * @param isTop
     * @return
     */
    List<Permission> findByPid(int pid,Boolean isTop);

    /**
     * 根据角色获取所有权限
     * @param roles
     * @return
     */
    List<Permission> findByRoles(Set<Role> roles);

    /**
     * 构建顶级权限树
     * @param byPid
     * @return
     */
    List<Map<String, Object>> buildTopPermissionTree(List<Permission> byPid);
}
