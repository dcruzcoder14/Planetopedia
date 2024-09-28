package com.example.planetopedia

import android.os.Parcelable // Importing the Parcelable interface from the Android SDK
import androidx.room.Entity // Importing the Entity annotation from the Room library
import androidx.room.PrimaryKey // Importing the PrimaryKey annotation from the Room library
import kotlinx.android.parcel.Parcelize // Importing the Parcelize annotation from the Kotlin Android Extensions library

// Annotating the Planet class with the Parcelize and Entity annotations
@Parcelize
@Entity(tableName = "planet")
data class Planet(
    // Defining the primary key for the Planet table, which will auto-generate a unique ID for each record
    @PrimaryKey(autoGenerate = true) val id: Int,
    // Defining properties for the Planet class, including the planet's name, distance from the sun, gravity, and image name
    val name: String,
    val distanceFromSun: Double,
    val gravity: Double,
    val imageName: String
) : Parcelable // Implementing the Parcelable interface to allow instances of the Planet class to be passed between activities using intents
