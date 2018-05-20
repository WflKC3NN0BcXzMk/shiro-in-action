package cn.mycloudx.shiroweb.controller;

import cn.mycloudx.shiroweb.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanghao
 * @date 2018/05/19
 */
@RestController
@Slf4j
public class UserController {

    @PostMapping(value = "/subLogin", consumes = "application/x-www-form-urlencoded")
    public String login(UserVO user) {
        log.info("user={}", user);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUser(), user.getPassword(), true);
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            return e.getMessage();
        }
        return "登录成功";
    }

    @GetMapping("/testRole")
    public String testRole() {
        return "testRole success";
    }


    @GetMapping("/testRole1")
    public String testRole1() {
        return "testRole success";
    }



    @GetMapping("/testPerms")
    public String testPermission() {
        return "testPermission success";
    }



    @GetMapping("/testPerms1")
    public String testPerms2() {
        return "testPermission success";
    }


}
