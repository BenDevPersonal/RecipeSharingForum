package com.pogany.RecipeSharingJava.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_setting")
public class UserSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "show_country_on_profile", nullable = false)
    private Boolean showCountryOnProfile;

    @NotNull
    @Column(name = "show_allergy_on_profile", nullable = false)
    private Boolean showAllergyOnProfile;

    @NotNull
    @Column(name = "auto_filter_allergy", nullable = false)
    private Boolean autoFilterAllergy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getShowCountryOnProfile() {
        return showCountryOnProfile;
    }

    public void setShowCountryOnProfile(Boolean showCountryOnProfile) {
        this.showCountryOnProfile = showCountryOnProfile;
    }

    public Boolean getShowAllergyOnProfile() {
        return showAllergyOnProfile;
    }

    public void setShowAllergyOnProfile(Boolean showAllergyOnProfile) {
        this.showAllergyOnProfile = showAllergyOnProfile;
    }

    public Boolean getAutoFilterAllergy() {
        return autoFilterAllergy;
    }

    public void setAutoFilterAllergy(Boolean autoFilterAllergy) {
        this.autoFilterAllergy = autoFilterAllergy;
    }
}