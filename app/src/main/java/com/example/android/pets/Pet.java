package com.example.android.pets;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Pet implements Parcelable{

    private String name;
    private String breed;
    private int gender;
    private int weight;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(breed);
        parcel.writeInt(gender);
        parcel.writeInt(weight);
    }

    public static final Parcelable.Creator<Pet> CREATOR
            = new Parcelable.Creator<Pet>() {
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }

        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };

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

    public Pet(Parcel in) {
        name = in.readString();
        breed = in.readString();
        gender = in.readInt();
        weight = in.readInt();
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
