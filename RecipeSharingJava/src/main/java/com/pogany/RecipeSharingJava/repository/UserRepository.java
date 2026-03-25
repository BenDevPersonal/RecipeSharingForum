package com.pogany.RecipeSharingJava.repository;

import com.pogany.RecipeSharingJava.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {

}
