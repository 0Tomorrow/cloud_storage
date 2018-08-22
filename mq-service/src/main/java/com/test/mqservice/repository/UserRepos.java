package com.test.mqservice.repository;

import com.test.mqcore.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepos extends JpaRepository<UserInfo, Integer>, JpaSpecificationExecutor<Integer> {

}
