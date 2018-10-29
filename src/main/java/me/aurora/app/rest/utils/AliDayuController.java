package me.aurora.app.rest.utils;

import lombok.extern.slf4j.Slf4j;
import me.aurora.annotation.Log;
import me.aurora.domain.ResponseEntity;
import me.aurora.domain.utils.AliDayuConfig;
import me.aurora.service.AliDayuService;
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
@RequestMapping("aliDayu")
public class AliDayuController {

    @Autowired
    private AliDayuService aliDayuService;

    @GetMapping(value = "/index")
    public ModelAndView index(){
        AliDayuConfig aliDayuConfig = aliDayuService.findById(1L);
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        request.setAttribute("aliDayu",aliDayuConfig);
        if(aliDayuConfig == null){
            request.setAttribute("aliDayu",new AliDayuConfig());
        }
        return new ModelAndView("/utils/aliDayu/index");
    }

    @Log("配置阿里大鱼")
    @PostMapping(value = "/config")
    public ResponseEntity emailConfig(@RequestBody @Validated(AliDayuConfig.Update.class) AliDayuConfig aliDayuConfig){
        log.warn("REST request to aliDayu AliDayuConfig : {}" +aliDayuConfig);
        aliDayuConfig.setId(1L);
        aliDayuService.updateConfig(aliDayuConfig,aliDayuService.findById(1L));
        return ResponseEntity.ok();
    }

    @Log("发送短信")
    @PostMapping(value = "send")
    public ResponseEntity send(@RequestParam String phone,@RequestParam String code) throws Exception {
        log.warn("REST request to send SMS : {}" +phone);
        return aliDayuService.send(aliDayuService.findById(1L),phone,code);
    }
}
