package me.aurora.monitor.app.rest;


import lombok.extern.slf4j.Slf4j;
import me.aurora.common.annotation.Log;
import me.aurora.common.config.api.ResponseEntity;
import me.aurora.monitor.service.SessionService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author 郑杰
 * @date 2018/09/21 15:49:14
 */
@Slf4j
@RestController
@RequestMapping("/online")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @GetMapping("/index")
    public ModelAndView online() {
        return new ModelAndView("/system/online/index");
    }

    @GetMapping(value = "/getOnlineInfo")
    public Map getOnlineInfo(){
        return sessionService.getOnlineInfo();
    }

    @Log("踢出用户")
    @RequiresPermissions(value={"admin", "user:all","user:logout"}, logical= Logical.OR)
    @DeleteMapping("/forceLogout")
    public ResponseEntity forceLogout(String id) {
        log.warn("REST request to forceLogout User : {}" +id);
        sessionService.forceLogout(id);
        return ResponseEntity.ok();
    }
}
