package com.delicloud.platform.cloud.storage.server.repository;

import com.delicloud.platform.cloud.storage.server.bo.SliceInfo;
import com.delicloud.platform.cloud.storage.server.entity.TUserInfo;
import com.delicloud.platform.common.data.repository.MyRepository;
import org.hibernate.annotations.OnDelete;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepo extends MyRepository<TUserInfo, Long> {
    TUserInfo findFirstByAccountAndPassword(Long account, String password);

    @Transactional
    void deleteAllByAccount(Long account);

//    @Transactional
//    @Query(nativeQuery = true, value = "delete from t_user_info where id in(select id from (SELECT id from t_user_info where account = ?1) t)")
//    void deleteAllByAccount(Long account);
}
