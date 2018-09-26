package com.delicloud.platform.cloud.storage.server.controller;

import com.delicloud.platform.cloud.storage.core.bo.FileInfo;
import com.delicloud.platform.cloud.storage.server.aop.Token;
import com.delicloud.platform.cloud.storage.server.bo.FileReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Token
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public void test() {
    }
}
