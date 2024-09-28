package com.example.planetopedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.planetopedia.databinding.FragmentAddPlanetBinding
import java.util.*

// An extension function on LiveData that allows an observer to be
// registered just once and then automatically unregistered after it
// receives its first value.
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    // Register a new observer with the LiveData instance that wraps the
    // provided Observer instance and removes itself after onChanged is
    // called once.
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            // Call the provided Observer's onChanged method with the value
            // passed to this observer's onChanged method.
            observer.onChanged(t)

            // Remove this observer from the LiveData instance.
            removeObserver(this)
        }
    })
}

class AddPlanetFragment : Fragment() {

    private lateinit var viewModel: PlanetViewModel

    // Inflate the layout for this fragment using view binding
    private var _binding: FragmentAddPlanetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlanetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this).get(PlanetViewModel::class.java)

        // Handle the add button click
        binding.addButton.setOnClickListener {
            // Get the values of the planet name, distance from the sun, and gravity fields
            val name = view.findViewById<EditText>(R.id.planet_name).text.toString()
            val distance = view.findViewById<EditText>(R.id.distance_from_sun).text.toString()
            val gravity = view.findViewById<EditText>(R.id.gravity).text.toString()

            // Add a list of valid planet names
            val validPlanetNames = listOf("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune")

            // Check if all fields are filled and if the planet name is valid
            if (name.isNotEmpty() && distance.isNotEmpty() && gravity.isNotEmpty() && name in validPlanetNames) {
                // Get the LiveData for the planet with the given name
                viewModel.getPlanetByName(name).observeOnce(viewLifecycleOwner) { existingPlanet ->
                    // Check if the planet already exists in the database
                    if (existingPlanet != null) {
                        // If the planet already exists, show an error message to the user
                        Toast.makeText(context, "Planet already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        // If the planet does not exist, generate the image name for the planet
                        val imageName = getImageName(name)

                        // Create a new planet object with the input values
                        val planet = Planet(0, name, distance.toDouble(), gravity.toDouble(),
                            imageName ?: "unknown")

                        // Insert the new planet into the database
                        viewModel.insert(planet)

                        // Show a success message to the user
                        Toast.makeText(context, "Planet added", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // If any field is empty or the planet name is not valid, show an error message to the user
                Toast.makeText(context, "Please fill in all fields and enter a valid planet name", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun getImageName(name: String): String? {
        val lowercaseName = name.lowercase(Locale.getDefault())
        return if (lowercaseName in listOf("mercury", "venus", "earth", "mars", "jupiter", "saturn", "uranus", "neptune")) {
            name.uppercase(Locale.getDefault())
        } else {
            null
        }
    }


}

