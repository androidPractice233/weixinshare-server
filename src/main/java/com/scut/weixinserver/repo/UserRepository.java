package com.scut.weixinserver.repo;

import com.scut.weixinserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUserId(String userId);
    User findUserByUserName(String userName);
    int deleteUserByUserId(String userId);
    List<User> getUsersByUserIdIn(List userIds);
}
