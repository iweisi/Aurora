package me.aurora.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @author 郑杰
 * @date 2018/10/25 11:26:38
 */
@Data
public class DepartmentDTO implements Serializable {

    private Long id;

    private String name;

    private Timestamp createTime;

    private Integer pid;

    private String rolesSelect;

    public interface New{};
    public interface Update{};
}
