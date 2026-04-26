package com.pogany.RecipeSharingJava.repository;

import com.pogany.RecipeSharingJava.entity.Follow;
import com.pogany.RecipeSharingJava.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Integer> {
    Optional<Follow> findByFollowingUserAndFollowedUser(User followingUser, User followedUser);
    boolean existsByFollowingUserAndFollowedUser(User followingUser, User followedUser);
    long countByFollowedUserId(Integer followedUserId);
    List<Follow> findAllByFollowedUser(User followedUser);
}
