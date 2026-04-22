package com.pogany.RecipeSharingJava.repository;

import com.pogany.RecipeSharingJava.entity.Blacklist;
import com.pogany.RecipeSharingJava.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistRepository extends JpaRepository<Blacklist,Integer> {
    boolean existsByBlacklistingUserAndBlacklistedUser(User blacklistingUser, User blacklistedUser);
    Optional<Blacklist> findByBlacklistingUserAndBlacklistedUser(
            User blacklistingUser,
            User blacklistedUser
    );
}
