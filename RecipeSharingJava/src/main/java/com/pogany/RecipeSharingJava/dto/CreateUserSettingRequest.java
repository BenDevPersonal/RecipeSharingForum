package com.pogany.RecipeSharingJava.dto;

public class CreateUserSettingRequest {
    private Integer userId;
    private boolean showCountryOnProfile;
    private boolean showAllergyOnProfile;
    private boolean autoFilterAllergy;

    public CreateUserSettingRequest() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isShowCountryOnProfile() {
        return showCountryOnProfile;
    }

    public void setShowCountryOnProfile(boolean showCountryOnProfile) {
        this.showCountryOnProfile = showCountryOnProfile;
    }

    public boolean isShowAllergyOnProfile() {
        return showAllergyOnProfile;
    }

    public void setShowAllergyOnProfile(boolean showAllergyOnProfile) {
        this.showAllergyOnProfile = showAllergyOnProfile;
    }

    public boolean isAutoFilterAllergy() {
        return autoFilterAllergy;
    }

    public void setAutoFilterAllergy(boolean autoFilterAllergy) {
        this.autoFilterAllergy = autoFilterAllergy;
    }
}
