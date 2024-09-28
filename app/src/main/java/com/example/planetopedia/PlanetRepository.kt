package com.example.planetopedia

import androidx.lifecycle.LiveData

// Define a repository class for managing the planets, which uses the PlanetDao to perform database operations
// It is a best practice to use a repository as a single source of truth for the data, separating the data access logic from the ViewModel
class PlanetRepository(private val planetDao: PlanetDao) {
    // Get all planets as LiveData from the DAO
    val allPlanets: LiveData<List<Planet>> = planetDao.getAll()

    // Insert a planet using the DAO
    fun insert(planet: Planet) {
        planetDao.insert(planet)
    }

    // Update a planet using the DAO
    fun update(planet: Planet) {
        planetDao.update(planet)
    }

    // Delete a planet using the DAO
    fun delete(planet: Planet) {
        planetDao.delete(planet)
    }

    // Get a planet by name using the DAO
    fun getPlanetByName(name: String): LiveData<Planet?> {
        return planetDao.getPlanetByName(name)
    }

    // Check if a duplicate planet name exists, excluding the given planet ID, using the DAO
    fun isDuplicateName(planetId: Int, name: String): LiveData<Boolean> {
        return planetDao.isDuplicateName(planetId, name)
    }
}

