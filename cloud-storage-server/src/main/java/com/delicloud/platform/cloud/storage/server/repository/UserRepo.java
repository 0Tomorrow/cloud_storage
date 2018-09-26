package com.delicloud.platform.cloud.storage.server.repository;

import com.delicloud.platform.cloud.storage.server.bo.SliceInfo;
import com.delicloud.platform.cloud.storage.server.entity.TUserInfo;
import com.delicloud.platform.common.data.repository.MyRepository;
import org.springframework.cache.annotation.CachePut;

public interface UserRepo extends MyRepository<TUserInfo, Long> {
    TUserInfo findFirstByAccountAndPassword(Long account, String password);

}
