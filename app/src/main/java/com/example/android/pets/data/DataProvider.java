package com.example.android.pets.data;

import com.example.android.pets.Pet;

import java.util.ArrayList;
import java.util.List;

public final class DataProvider {

    public static List<Pet> pets;

    static  {
        pets = new ArrayList<>(4);
        pets.add(new Pet("Luci", "German Shepherd", Pet.GENDER_MALE, 20));
        pets.add(new Pet("Tommy", "Bulldog", Pet.GENDER_MALE, 25));
        pets.add(new Pet("Binx", "Poodle", Pet.GENDER_FEMALE, 8));
        pets.add(new Pet("Flat", "Pug", Pet.GENDER_UNKNOWN, 13));
    }
}
