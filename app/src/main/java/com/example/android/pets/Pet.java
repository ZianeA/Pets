package com.example.android.pets;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.InverseMethod;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import com.example.android.pets.BR;
import com.google.common.primitives.Ints;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class Pet extends BaseObservable implements Parcelable  {

    private int id;
    private String name;
    private String breed;
    private int gender;
    private int weight;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(id);
        out.writeString(name);
        out.writeString(breed);
        out.writeInt(gender);
        out.writeInt(weight);
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
    public Pet(int id, String name, String breed, @Gender int gender, int weight) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.weight = weight;
    }

    public Pet() {

    }

    public Pet(Parcel in) {
        id = in.readInt();
        name = in.readString();
        breed = in.readString();
        gender = in.readInt();
        weight = in.readInt();
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
        notifyPropertyChanged(BR.breed);
    }

    @Gender @Bindable
    public int getGender() {
        return gender;
    }

    public void setGender(@Gender int gender) {
        this.gender = gender;
        notifyPropertyChanged(BR.gender);
    }

    @Bindable
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
        notifyPropertyChanged(BR.weight);
    }

    @InverseMethod("convertIntToString")
    public static int convertStringToInt(String value) {
        Integer parsed = Ints.tryParse(value);
        return parsed == null ? 0 : parsed;
    }

    public static String convertIntToString(int value) {
        return String.valueOf(value);
    }
}
