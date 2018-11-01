package me.aurora.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * 角色
 * @author 郑杰
 * @date 2018/08/20 20:10:15
 */
@Data
public class RoleDTO implements Serializable {

    private Long id;

    private String name;

    private String remark;

    private Set<PermissionDTO> permissions;

    private Timestamp createDateTime;

    private Timestamp updateDateTime;
}
