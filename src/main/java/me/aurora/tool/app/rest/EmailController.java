package me.aurora.tool.app.rest;

import lombok.extern.slf4j.Slf4j;
import me.aurora.common.annotation.Log;
import me.aurora.common.config.api.ResponseEntity;
import me.aurora.tool.domain.EmailConfig;
import me.aurora.tool.domain.vo.EmailVo;
import me.aurora.tool.service.EmailService;
import me.aurora.common.utils.HttpContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 发送邮件
 * @author 郑杰
 * @date 2018/09/28 6:55:53
 */
@Slf4j
@RestController
@RequestMapping("email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/index")
    public ModelAndView index(){
        EmailConfig emailConfig = emailService.findById(1L);
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        request.setAttribute("email",emailConfig);
        if(emailConfig == null){
            request.setAttribute("email",new EmailConfig());
        }
        return new ModelAndView("/utils/email/index");
    }

    @Log("配置邮件")
    @PostMapping(value = "/config")
    public ResponseEntity emailConfig(@RequestBody @Validated(EmailConfig.Update.class) EmailConfig emailConfig){
        log.warn("REST request to emailConfig EmailConfig : {}" +emailConfig);
        emailConfig.setId(1L);
        emailService.updateConfig(emailConfig,emailService.findById(1L));
        return ResponseEntity.ok();
    }

    @Log("发送邮件")
    @PostMapping(value = "send")
    public ResponseEntity send(@Valid @RequestBody EmailVo emailVo) throws Exception {
        log.warn("REST request to send Email : {}" +emailVo);
        emailService.send(emailVo,emailService.findById(1L));
        return ResponseEntity.ok();
    }
}
