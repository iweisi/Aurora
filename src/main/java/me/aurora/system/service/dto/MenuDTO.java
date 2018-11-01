package me.aurora.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 郑杰
 * @date 2018/08/23 11:54:10
 */
@Data
public class MenuDTO implements Serializable {

    private Long id;

    private Long soft;

    private String ico;

    private String name;

    private String url;

    private Integer pid;

    private Boolean iframe;

    private Integer level;

    private String rolesSelect;

    private Integer levelNum;

    private Timestamp createTime;

    private Timestamp updateTime;
}
