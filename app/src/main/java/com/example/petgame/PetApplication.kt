package com.example.petgame;

import android.app.Application
import com.example.petgame.data.Model.PetRoomDatabase

class PetApplication : Application() {
    // Using by lazy so the database is only created when needed
    // rather than when the application starts
    val database: PetRoomDatabase by lazy { PetRoomDatabase.getDatabase(this) }
}
