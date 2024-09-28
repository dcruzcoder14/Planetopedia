package com.example.planetopedia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// The PlanetViewModel class inherits from AndroidViewModel and is responsible for
// managing the data for the UI and communicating with the PlanetRepository.
class PlanetViewModel(application: Application) : AndroidViewModel(application) {

    // An instance of the PlanetRepository, which is responsible for
    // performing database operations through the DAO.
    private val repository: PlanetRepository

    // A LiveData object containing a list of all planets in the database.
    // LiveData allows the UI to automatically update when the data changes.
    val allPlanets: LiveData<List<Planet>>

    init {
        // Get the PlanetDao instance from the PlanetDatabase.
        val planetDao = PlanetDatabase.getDatabase(application).planetDao()

        // Initialize the repository with the PlanetDao.
        repository = PlanetRepository(planetDao)

        // Retrieve all planets from the repository.
        allPlanets = repository.allPlanets
    }

    // Insert a new planet into the database.
    // The viewModelScope.launch(Dispatchers.IO) is used to perform the database
    // operation in a background thread, preventing the UI from freezing.
    fun insert(planet: Planet) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(planet)
    }

    // Update an existing planet in the database.
    // The viewModelScope.launch(Dispatchers.IO) is used to perform the database
    // operation in a background thread, preventing the UI from freezing.
    fun update(planet: Planet) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(planet)
    }

    // Delete a planet from the database.
    // The viewModelScope.launch(Dispatchers.IO) is used to perform the database
    // operation in a background thread, preventing the UI from freezing.
    fun delete(planet: Planet) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(planet)
    }

    fun getPlanetByName(name: String): LiveData<Planet?> {
        return repository.getPlanetByName(name)
    }

    fun isDuplicateName(planetId: Int, name: String): LiveData<Boolean> {
        // Call the isDuplicateName function on the repository with the given planetId and name
        // and return the result as a LiveData object
        return repository.isDuplicateName(planetId, name)
    }

}

