package com.pogany.RecipeSharingJava.dto;

public class UserSettingDto {
    private Integer id;
    private boolean showCountryOnProfile;
    private boolean showAllergyOnProfile;
    private boolean autoFilterAllergy;

    public UserSettingDto() {
    }

    public UserSettingDto(Integer id, boolean showCountryOnProfile, boolean showAllergyOnProfile, boolean autoFilterAllergy) {
        this.id = id;
        this.showCountryOnProfile = showCountryOnProfile;
        this.showAllergyOnProfile = showAllergyOnProfile;
        this.autoFilterAllergy = autoFilterAllergy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
