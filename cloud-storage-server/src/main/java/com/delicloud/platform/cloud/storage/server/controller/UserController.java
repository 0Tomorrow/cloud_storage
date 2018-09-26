package com.delicloud.platform.cloud.storage.server.controller;

import com.delicloud.platform.cloud.storage.server.service.UserService;
import com.delicloud.platform.common.lang.bo.RespBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RespBase<String> login(Long account, String password) {
        String token = userService.login(account, password);
        return new RespBase<>(token);
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public RespBase register(
            @RequestParam(value = "account", required = false) String accountString, @RequestParam String password) {
        Long account = Long.parseLong(accountString);
        userService.register(account, password);
        return RespBase.OK_RESP_BASE;
    }
}
