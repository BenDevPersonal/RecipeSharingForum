package com.pogany.RecipeSharingJava.repository;

import com.pogany.RecipeSharingJava.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Integer> {

}
