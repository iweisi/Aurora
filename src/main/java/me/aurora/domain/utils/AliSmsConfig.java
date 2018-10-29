package me.aurora.domain.utils;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author 郑杰
 * @date 2018/10/29 16:53:13
 */
@Data
@Entity
@Table(name = "zj_alisms_config")
public class AliSmsConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * （必填）阿里短信后台查看
     */
    @NotBlank(groups = Update.class)
    private String accessKeyId;

    /**
     * （必填）阿里短信后台查看
     */
    @NotBlank(groups = Update.class)
    private String accessKeySecret;

    /**
     * （必填）短信签名-可在短信控制台中找到
     */
    @NotBlank(groups = Update.class)
    private String signName;

    /**
     * （必填）短信模板-可在短信控制台中找到
     */
    @NotBlank(groups = Update.class)
    private String templateCode;

    public interface Update {}
}
