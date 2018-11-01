package me.aurora.tool.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 郑杰
 * @date 2018/09/20 14:08:31
 */
@Data
public class PictureDto implements Serializable {

    private Long id;

    private String filename;

    private String url;

    private String size;

    private String height;

    private String width;

    private String userName;

    private Timestamp createTime;
}
