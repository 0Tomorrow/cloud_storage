package com.delicloud.platform.cloud.storage.server.controller;

import com.delicloud.platform.cloud.storage.server.bo.UserReq;
import com.delicloud.platform.cloud.storage.server.service.UserService;
import com.delicloud.platform.common.lang.bo.RespBase;
import com.delicloud.platform.common.lang.exception.PlatformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RespBase login(@RequestBody UserReq userReq) {
        String token = userService.login(userReq);
        return new RespBase<>(token);
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public RespBase register(@RequestBody UserReq userReq) {
        userService.register(userReq);
        return RespBase.OK_RESP_BASE;
    }
}
