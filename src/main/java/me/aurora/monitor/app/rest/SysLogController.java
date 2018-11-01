package me.aurora.monitor.app.rest;

import me.aurora.monitor.repository.spec.LogSpec;
import me.aurora.monitor.service.SysLogService;
import me.aurora.system.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;

/**
 * @author 郑杰
 * @date 2018/08/23 9:57:14
 */
@RestController
@RequestMapping("sysLog")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 去日志页面
     * @return
     */
    @GetMapping(value = "/index")
    public ModelAndView index(){
        return new ModelAndView("/system/log/index");
    }

    /**
     * 去实时日志页面
     * @param model
     * @return
     */
    @GetMapping(value = "/msg")
    public ModelAndView logMsgIndex(Model model){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String osName = System.getProperty("os.name");
        model.addAttribute("osName", osName);
        model.addAttribute("userName", user.getUsername());
        return new ModelAndView("/system/logMessage/index");
    }

    /**
     * 查询所有日志
     * @param username
     * @param method
     * @param operation
     * @param location
     * @param page
     * @param limit
     * @return
     */
    @RequiresPermissions(value={"admin", "log:all"}, logical= Logical.OR)
    @GetMapping(value = "/getLogInfo")
    public Map getLogInfo(@RequestParam(value = "username",required = false) String username,
                          @RequestParam(value = "method",required = false) String method,
                          @RequestParam(value = "operation",required = false) String operation,
                          @RequestParam(value = "location",required = false) String location,
                          @RequestParam(value = "page",defaultValue = "1")Integer page,
                          @RequestParam(value = "limit",defaultValue = "10")Integer limit){
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(page-1,limit,sort);
        return sysLogService.getLogInfo(new LogSpec(username,method,operation,location),pageable);
    }
}
