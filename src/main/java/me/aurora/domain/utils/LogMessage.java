package me.aurora.domain.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 郑杰
 * @date 2018/10/30 19:55:46
 */
@Data
@AllArgsConstructor
public class LogMessage {

    private String body;
    private String timestamp;
    private String threadName;
    private String className;
    private String level;
}
