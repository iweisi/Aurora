package me.aurora.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import me.aurora.system.domain.Permission;
import me.aurora.system.domain.Role;
import me.aurora.system.repository.RoleRepo;
import me.aurora.system.repository.spec.RoleSpec;
import me.aurora.system.service.RoleService;
import me.aurora.system.service.dto.RoleDTO;
import me.aurora.system.service.mapper.RoleMapper;
import me.aurora.common.utils.PageUtil;
import me.aurora.common.config.exception.AuroraException;
import me.aurora.common.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author 郑杰
 * @date 2018/08/23 17:27:03
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Map<String, Object>> buildRoleTree(List<Role> roles) {
        List<Map<String,Object>> maps = new LinkedList<>();
        roles.forEach(role -> {
            if (role!=null){
                Map<String,Object> map = new HashMap<>(16);
                map.put("id",role.getId());
                map.put("name",role.getName());
                maps.add(map);
            }
        });
        return maps;
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepo.findAll();
    }

    @Override
    public Map getRoleInfo(RoleSpec roleSpec, Pageable pageable) {
        Page<Role> roles = roleRepo.findAll(roleSpec,pageable);
        Page<RoleDTO> roleDTOS = roles.map(roleMapper::toDto);
        return PageUtil.buildPage(roleDTOS.getContent(),roles.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(Role role, String permissions) {
        if(roleRepo.findByName(role.getName())!=null){
            throw new AuroraException(HttpStatus.HTTP_BAD_REQUEST,"角色已存在");
        }
        if(StrUtil.isEmpty(permissions)){
            throw new AuroraException(HttpStatus.HTTP_NOT_FOUND,"请至少为其分配一个权限");
        }
        role.setPermissions(getPermissions(permissions));
        roleRepo.save(role);
    }

    @Override
    public Role findById(Long id) {
        Optional<Role> role = roleRepo.findById(id);
        ValidationUtil.isNull(role,"id:"+id+"is not find");
        return role.get();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Role old,Role role, String permissions) {
        if(StrUtil.isEmpty(permissions)){
            throw new AuroraException(HttpStatus.HTTP_NOT_FOUND,"请至少为其分配一个权限");
        }
        Role role1 = roleRepo.findByName(role.getName());
        if(role1!=null && !role.getId().equals(role1.getId())){
            throw new AuroraException(HttpStatus.HTTP_BAD_REQUEST,"角色已存在");
        }
        old.setName(role.getName());
        old.setRemark(role.getRemark());
        old.setPermissions(getPermissions(permissions));
        roleRepo.save(old);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Role role) {
        try {
            roleRepo.delete(role);
        }catch (Exception e){
            throw new AuroraException(HttpStatus.HTTP_INTERNAL_ERROR,"删除失败，请检查角色是否被使用");
        }
    }

    @Override
    public String getPermissions(Set<Permission> permissions) {
        String str = "";
        for (Permission permission : permissions) {
            str += permission.getId()+",";
        }
        return StrUtil.hasEmpty(str)?"":str.substring(0,str.length() - 1);
    }

    /**
     * 获取权限集合
     * @param permissions
     * @return
     */
    public Set<Permission> getPermissions(String permissions){

        Set<Permission> permissionSet = new HashSet<>();
        for(String permissionId:permissions.split(",")){
            Permission permission = new Permission();
            permission.setId(Long.parseLong(permissionId));
            permissionSet.add(permission);
        }
        return permissionSet;
    }

    @Override
    public String getRoles(Set<Role> roles) {
        String str = "";
        for (Role role : roles) {
            str += role.getId()+",";
        }
        return StrUtil.hasEmpty(str)?"":str.substring(0,str.length() - 1);
    }
}
