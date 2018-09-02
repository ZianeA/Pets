package com.example.android.pets;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Pet {

    private String name;
    private String breed;
    private int gender;
    private int weight;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GENDER_MALE, GENDER_FEMALE, GENDER_UNKNOWN})
    public @interface Gender {
    }

    public static final int GENDER_MALE = 0;
    public static final int GENDER_FEMALE = 1;
    public static final int GENDER_UNKNOWN = 2;

    /**
     * @param name   the name of the pet
     * @param breed  the breed of the pet
     * @param gender the gender of the pet (Male, Female, Unknown)
     * @param weight the weight of the pet in kilograms
     */
    public Pet(String name, String breed, @Gender int gender, int weight) {
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.weight = weight;
    }

    public Pet() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Gender
    public int getGender() {
        return gender;
    }

    public void setGender(@Gender int gender) {
        this.gender = gender;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
