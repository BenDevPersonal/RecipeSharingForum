package com.pogany.RecipeSharingJava.repository;

import com.pogany.RecipeSharingJava.entity.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSettingRepository extends JpaRepository<UserSetting,Integer> {
    Optional<UserSetting> findByUser_Id(Integer userId);
}
