package me.aurora.monitor.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 郑杰
 * @date 2018/08/23 9:59:57
 */
@Data
public class SysLogDTO implements Serializable {

    private Long id;

    private String username;

    private String operation;

    private Integer time;

    private String method;

    private String params;

    private String ip;

    private String location;

    private Timestamp createTime;
}
