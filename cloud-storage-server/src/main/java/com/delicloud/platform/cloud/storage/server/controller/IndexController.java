package com.delicloud.platform.cloud.storage.server.controller;

import com.delicloud.platform.cloud.storage.core.bo.IndexInfo;
import com.delicloud.platform.cloud.storage.core.bo.IndexInfoResp;
import com.delicloud.platform.cloud.storage.server.bo.IndexReq;
import com.delicloud.platform.cloud.storage.server.service.IndexService;
import com.delicloud.platform.common.lang.bo.RespBase;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    IndexService indexService;

    @ApiOperation(value = "通过path查询该路径下的所有文件夹", response = RespBase.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public RespBase<List<IndexInfoResp>> show(Long updateBy, String path) {
        List<IndexInfoResp> list = indexService.findIndex(updateBy, path);
        return new RespBase<>(list);
    }

    @ApiOperation(value = "创建文件夹", response = RespBase.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespBase createIndex(@RequestBody IndexInfo indexInfo) {
        indexService.createIndex(indexInfo);
        return RespBase.OK_RESP_BASE;
    }

    @ApiOperation(value = "删除文件夹", response = RespBase.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespBase deleteIndex(@RequestBody IndexReq indexReq) {
        String id = indexReq.getId();
        boolean sure = indexReq.isSure();
        indexService.deleteIndex(id, sure);
        return RespBase.OK_RESP_BASE;
    }
}
