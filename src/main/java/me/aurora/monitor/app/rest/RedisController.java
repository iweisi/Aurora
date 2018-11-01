package me.aurora.monitor.app.rest;

import lombok.extern.slf4j.Slf4j;
import me.aurora.common.annotation.Log;
import me.aurora.common.config.api.ResponseEntity;
import me.aurora.monitor.service.RedisService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.Set;

/**
 * @author 郑杰
 * @date 2018/09/22 10:48:11
 */
@Slf4j
@RestController
@RequestMapping("redis")
public class RedisController {

    private static final String INTEGER_PREFIX = "(integer)";

    @Autowired
    private RedisService redisService;

    @GetMapping("terminal")
    @RequiresPermissions (value={"admin", "redis:all","redis:terminal"}, logical= Logical.OR)
    public ModelAndView terminal(Model model) {
        String osName = System.getProperty("os.name");
        model.addAttribute("osName", osName);
        return new ModelAndView("/system/redis/terminal");
    }

    @Log("执行Redis keys")
    @GetMapping("keys")
    public ResponseEntity keys(String arg) {
        log.warn("REST request to keys Redis : {}" +arg);
        try {
            Set<String> set = this.redisService.getKeys(arg);
            return ResponseEntity.ok(set);
        } catch (Exception e) {
            return ResponseEntity.error(e.getMessage());
        }
    }

    @Log("执行Redis get")
    @GetMapping("get")
    public ResponseEntity get(String arg) {
        log.warn("REST request to get Redis : {}" +arg);
        try {
            String result = this.redisService.get(arg);
            return ResponseEntity.ok(result == null ? "" : result);
        } catch (Exception e) {
            return ResponseEntity.error(e.getMessage());
        }
    }

    @Log("执行Redis set")
    @GetMapping("set")
    public ResponseEntity set(String arg) {
        log.warn("REST request to set Redis : {}" +arg);
        try {
            String[] args = arg.split(",");
            if (args.length == 1){
                return ResponseEntity.error("(error) ERR wrong number of arguments for 'set' command");
            } else if(args.length != 2){
                return ResponseEntity.error("(error) ERR syntax error");
            }
            String result = this.redisService.set(args[0], args[1]);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.error(e.getMessage());
        }
    }

    @Log("执行Redis del")
    @GetMapping("del")
    public ResponseEntity del(String arg) {
        log.warn("REST request to del Redis : {}" +arg);
        try {
            String[] args = arg.split(",");
            Long result = this.redisService.del(args);
            return ResponseEntity.ok(INTEGER_PREFIX + result);
        } catch (Exception e) {
            return ResponseEntity.error(e.getMessage());
        }
    }

    @Log("执行Redis exists")
    @GetMapping("exists")
    public ResponseEntity exists(String arg) {
        log.warn("REST request to exists Redis : {}" +arg);
        try {
            int count = 0;
            String[] arr = arg.split(",");
            for (String key : arr) {
                if (this.redisService.exists(key)){
                    count++;
                }
            }
            return ResponseEntity.ok(INTEGER_PREFIX + count);
        } catch (Exception e) {
            return ResponseEntity.error(e.getMessage());
        }
    }

    @Log("执行Redis pttl")
    @GetMapping("pttl")
    public ResponseEntity pttl(String arg) {
        log.warn("REST request to pttl Redis : {}" +arg);
        try {
            return ResponseEntity.ok(INTEGER_PREFIX + this.redisService.pttl(arg));
        } catch (Exception e) {
            return ResponseEntity.error(e.getMessage());
        }
    }

    @Log("执行Redis pexpire")
    @GetMapping("pexpire")
    public ResponseEntity pexpire(String arg) {
        log.warn("REST request to pexpire Redis : {}" +arg);
        try {
            String[] arr = arg.split(",");
            if (arr.length != 2 || !isValidLong(arr[1])) {
                return ResponseEntity.error("(error) ERR wrong number of arguments for 'pexpire' command");
            }
            return ResponseEntity.ok(INTEGER_PREFIX + this.redisService.pexpire(arr[0], Long.valueOf(arr[1])));
        } catch (Exception e) {
            return ResponseEntity.error(e.getMessage());
        }
    }

    @Log("执行flushdb")
    @GetMapping("flushdb")
    public ResponseEntity flushdb() {
        log.warn("REST request to flushdb Redis : {}");
        try {
            return ResponseEntity.ok(INTEGER_PREFIX + this.redisService.flushdb());
        } catch (Exception e) {
            return ResponseEntity.error(e.getMessage());
        }
    }

    @Log("执行flushall")
    @GetMapping("flushall")
    public ResponseEntity flushall() {
        log.warn("REST request to flushall Redis : {}");
        try {
            return ResponseEntity.ok(INTEGER_PREFIX + this.redisService.flushall());
        } catch (Exception e) {
            return ResponseEntity.error(e.getMessage());
        }
    }

    private static boolean isValidLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
