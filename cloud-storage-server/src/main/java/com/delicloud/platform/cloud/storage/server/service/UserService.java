package com.delicloud.platform.cloud.storage.server.service;

import com.delicloud.platform.cloud.storage.server.bo.req.UserReq;
import com.delicloud.platform.cloud.storage.server.entity.TUserInfo;
import com.delicloud.platform.cloud.storage.server.repository.UserRepo;
import com.delicloud.platform.cloud.storage.server.repository.cache.UserTokenCache;
import com.delicloud.platform.common.lang.exception.PlatformException;
import com.delicloud.platform.common.lang.util.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserTokenCache userTokenCache;

    @Autowired
    IndexService indexService;

    public String login(UserReq userReq) {
        if (!userReq.verify()) {
            throw new PlatformException("参数格式不对");
        }
        Long account = Long.parseLong(userReq.getAccount());
        String password = userReq.getPassword();

        TUserInfo tUserInfo = userRepo.findFirstByAccountAndPassword(account, password);
        if (tUserInfo == null) {
            throw new PlatformException("用户名或密码错误");
        }
        return getToken(tUserInfo);
    }

    private String getToken(TUserInfo tUserInfo) {
        String s = tUserInfo.getId() + ":" + System.currentTimeMillis();
        String token = StringEncoder.encodeByMD5(s);
        userTokenCache.putUserToken(tUserInfo.getId().toString(), token);
        return new String(Base64.getEncoder().encode((tUserInfo.getId() + ":" + token).getBytes()));
    }

    public void register(UserReq userReq) {
        if (!userReq.verify()) {
            throw new PlatformException("参数格式不对");
        }
        Long account = Long.parseLong(userReq.getAccount());
        String password = userReq.getPassword();

        if (userRepo.findFirstByAccount(account) != null) {
            throw new PlatformException("用户已存在");
        }

        TUserInfo tUserInfo = new TUserInfo();
        tUserInfo.setAccount(account);
        tUserInfo.setPassword(password);
        userRepo.save(tUserInfo);
        indexService.createRootIndex(account);
    }
}
