package me.aurora.monitor.app.rest;

import me.aurora.common.annotation.Log;
import me.aurora.common.config.api.ResponseEntity;
import me.aurora.monitor.service.SysLogService;
import me.aurora.monitor.service.WebCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * pv、ip统计、swagger访问
 * @author 郑杰
 * @date 2018/11/01 20:21:36
 */
@RestController
public class WebController {


    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private WebCountService webCountService;

    @Log("访问swagger")
    @GetMapping(value = "/swagger/index")
    public ModelAndView index(){
        return new ModelAndView("/system/api/index");
    }

    /**
     * 访问量计数
     * @return
     */
    @GetMapping(value = "/pageviews/plusCount")
    public ResponseEntity plusCount(){
        webCountService.save();
        return ResponseEntity.ok();
    }

    /**
     * 访问量查询
     * @return
     */
    @GetMapping(value = "/pageviews/get")
    public Map getCount(){
        Map map = new HashMap();
        map.put("pv",webCountService.findByDate(LocalDate.now().toString()).getCounts());
        map.put("weekPv",webCountService.getWeekPv());
        map.put("ip",sysLogService.getIp());
        map.put("weekIp",sysLogService.getWeekIP());
        return map;
    }
}
