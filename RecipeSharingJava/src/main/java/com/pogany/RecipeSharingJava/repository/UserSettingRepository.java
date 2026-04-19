package com.pogany.RecipeSharingJava.repository;

import com.pogany.RecipeSharingJava.entity.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingRepository extends JpaRepository<UserSetting,Integer> {

}
