package com.example.planetopedia

object BootstrapData {
    private val planets = listOf(
        Planet(id= 1, name = "Jupiter", distanceFromSun = 6000.00, gravity = 850.00, imageName = "jupiter"),
        Planet(id= 2, name = "Mars", distanceFromSun = 2524.29, gravity = 913.92, imageName = "mars")
    )

    fun insertPlanets(database: PlanetDatabase) {
        database.planetDao().insertAll(planets)
    }
}
