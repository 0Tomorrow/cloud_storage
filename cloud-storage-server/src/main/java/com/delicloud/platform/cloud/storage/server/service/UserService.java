package com.delicloud.platform.cloud.storage.server.service;

import com.delicloud.platform.cloud.storage.server.entity.TUserInfo;
import com.delicloud.platform.cloud.storage.server.repository.UserRepo;
import com.delicloud.platform.cloud.storage.server.repository.cache.UserTokenCache;
import com.delicloud.platform.cloud.storage.server.util.UserUtil;
import com.delicloud.platform.common.lang.exception.PlatformException;
import com.delicloud.platform.common.lang.util.StringEncoder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserTokenCache userTokenCache;

    @Autowired
    IndexService indexService;

    public String login(Long account, String password) {

        if (StringUtils.isBlank(account + "")) {
            throw new PlatformException("用户名为空");
        }
        if (StringUtils.isBlank(password + "")) {
            throw new PlatformException("密码为空");
        }
        String msg;
        if ((msg = UserUtil.verifyParam("account", account)) != null) {
            throw new PlatformException(msg);
        }
        if ((msg = UserUtil.verifyParam("password", password)) != null) {
            throw new PlatformException(msg);
        }

        TUserInfo tUserInfo = userRepo.findFirstByAccountAndPassword(account, password);
        if (tUserInfo == null) {
            throw new PlatformException("用户名或密码错误");
        }
        return getToken(tUserInfo);
    }

    private String getToken(TUserInfo tUserInfo) {
        Long account = tUserInfo.getUpdateBy();
        String s = String.valueOf(account) + ":" + System.currentTimeMillis();
        String md5 =  StringEncoder.encodeByMD5(s);
        userTokenCache.putUserToken(tUserInfo.getAccount() + "", md5);
        return md5;
    }

    public void register(Long account, String password) {
        if (account == null) {
            throw new PlatformException("用户名为空");
        }
        if (StringUtils.isBlank(password + "")) {
            throw new PlatformException("密码为空");
        }
        String msg;
        if ((msg = UserUtil.verifyParam("account", account)) != null) {
            throw new PlatformException(msg);
        }
        if ((msg = UserUtil.verifyParam("password", password)) != null) {
            throw new PlatformException(msg);
        }

        TUserInfo tUserInfo = new TUserInfo();
        tUserInfo.setAccount(account);
        tUserInfo.setPassword(password);
        userRepo.save(tUserInfo);
        indexService.createRootIndex(account);
    }
}
