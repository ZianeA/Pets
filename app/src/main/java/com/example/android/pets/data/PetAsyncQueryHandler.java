package com.example.android.pets.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

public class PetAsyncQueryHandler extends AsyncQueryHandler {

    public PetAsyncQueryHandler(ContentResolver cr) {
        super(cr);
    }
}
