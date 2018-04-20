package com.scut.weixinserver.repo;

import com.scut.weixinserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUserId(String userId);
    int deleteUserByUserId(String userId);
}
