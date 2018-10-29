package me.aurora.app.rest.utils;

import lombok.extern.slf4j.Slf4j;
import me.aurora.annotation.Log;
import me.aurora.domain.ResponseEntity;
import me.aurora.domain.utils.AliSmsConfig;
import me.aurora.service.AliSmsService;
import me.aurora.util.HttpContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 郑杰
 * @date 2018/10/29 16:58:17
 */
@Slf4j
@RestController
@RequestMapping("aliSms")
public class AliSmsController {

    @Autowired
    private AliSmsService aliSmsService;

    @GetMapping(value = "/index")
    public ModelAndView index(){
        AliSmsConfig aliDayuConfig = aliSmsService.findById(1L);
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        request.setAttribute("aliSms",aliDayuConfig);
        if(aliDayuConfig == null){
            request.setAttribute("aliSms",new AliSmsConfig());
        }
        return new ModelAndView("/utils/aliSms/index");
    }

    @Log("配置阿里短信")
    @PostMapping(value = "/config")
    public ResponseEntity emailConfig(@RequestBody @Validated(AliSmsConfig.Update.class) AliSmsConfig aliSmsConfig){
        log.warn("REST request to aliSmsConfig AliSmsConfig : {}" +aliSmsConfig);
        aliSmsConfig.setId(1L);
        aliSmsService.updateConfig(aliSmsConfig,aliSmsService.findById(1L));
        return ResponseEntity.ok();
    }

    @Log("发送短信")
    @PostMapping(value = "/send")
    public ResponseEntity send(@RequestParam String phone,@RequestParam String code) throws Exception {
        log.warn("REST request to send SMS : {}" +phone);
        return aliSmsService.send(aliSmsService.findById(1L),phone,code);
    }
}
