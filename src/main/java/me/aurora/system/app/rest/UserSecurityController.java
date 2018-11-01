package me.aurora.system.app.rest;

import lombok.extern.slf4j.Slf4j;
import me.aurora.common.annotation.Log;
import me.aurora.system.domain.Department;
import me.aurora.common.config.api.ResponseEntity;
import me.aurora.system.domain.Role;
import me.aurora.system.domain.User;
import me.aurora.system.service.DepartmentService;
import me.aurora.system.service.MenuService;
import me.aurora.monitor.service.SysLogService;
import me.aurora.system.service.UserService;
import me.aurora.common.utils.EncryptHelper;
import me.aurora.common.utils.HttpContextUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * 用户登录、注册、修改密码
 * @author 郑杰
 * @date 2018/08/20 13:50:06
 */
@Slf4j
@RestController
public class UserSecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private DepartmentService departmentService;

    @Log("登录")
    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestParam String username, @RequestParam String password, @RequestParam Boolean rememberMe){
        log.warn("REST request to login User : {}"+username);
        password = EncryptHelper.encrypt(password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            /**
             * 登录成功后将锁屏清掉
             */
            HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
            request.getSession(true).setAttribute("lock",null);
            return ResponseEntity.ok();
        } catch (UnknownAccountException e) {
            return ResponseEntity.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return ResponseEntity.error(e.getMessage());
        } catch (LockedAccountException e) {
            return ResponseEntity.error(e.getMessage());
        } catch (AuthenticationException e) {
            return ResponseEntity.error("认证失败！");
        }
    }

    @GetMapping("/index")
    public ModelAndView index() {
        log.warn("REST request to index User : {}");
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        user = userService.findById(user.getId());

        //获取部门信息，获取部门的角色菜单
        Department department = departmentService.findById(user.getDepartment().getId());

        Set<Role> roleSet = user.getRoles();
        roleSet.addAll(department.getRoles());

        // 获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        request.setAttribute("user", user);
        request.setAttribute("menus",menuService.findMenusByUserRols(roleSet));
        return new ModelAndView("/index");
    }
}
