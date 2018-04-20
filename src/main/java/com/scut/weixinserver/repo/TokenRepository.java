package com.scut.weixinserver.repo;

import com.scut.weixinserver.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findTokenByUserId(String userId);
    int deleteTokenByUserId(String userId);
}
