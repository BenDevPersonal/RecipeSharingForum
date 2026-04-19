package com.pogany.RecipeSharingJava.repository;

import com.pogany.RecipeSharingJava.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistRepository extends JpaRepository<Blacklist,Integer> {
}
