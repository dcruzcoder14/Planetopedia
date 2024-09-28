package com.example.planetopedia

import androidx.lifecycle.LiveData
import androidx.room.*

// Define a DAO (Data Access Object) interface for managing the Planet entities in the database
@Dao
interface PlanetDao {
    // Get all planets from the database as LiveData
    @Query("SELECT * FROM planet")
    fun getAll(): LiveData<List<Planet>>

    // Insert a planet into the database
    @Insert
    fun insert(planet: Planet)

    // Inserts a list of planets into the database. If a planet with the same ID already exists,
    // the existing team will be replaced.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(planet: List<Planet>)

    // Update a planet in the database
    @Update
    fun update(planet: Planet)

    // Delete a planet from the database
    @Delete
    fun delete(planet: Planet)

    // Get a planet by name (case-insensitive) as LiveData
    @Query("SELECT * FROM planet WHERE UPPER(name) = UPPER(:name)")
    fun getPlanetByName(name: String): LiveData<Planet?>

    // Check if there is a duplicate planet name, excluding the planet with the given ID
    @Query("SELECT EXISTS(SELECT * FROM planet WHERE id != :planetId AND name = :name)")
    fun isDuplicateName(planetId: Int, name: String): LiveData<Boolean>
}
