package com.example.planetopedia

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planetopedia.databinding.FragmentSelectPlanetToUpdateBinding
import java.util.*

// The SelectPlanetToUpdateFragment class displays a list of planets and allows users to select a planet to update
class SelectPlanetToUpdateFragment : Fragment() {

    // Declare the PlanetViewModel and PlanetAdapter as lateinit variables
    private lateinit var viewModel: PlanetViewModel
    private lateinit var adapter: PlanetAdapter

    // Declare the binding variable for this fragment
    private var _binding: FragmentSelectPlanetToUpdateBinding? = null
    private val binding get() = _binding!!

    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectPlanetToUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Initialize the ViewModel, adapter, and RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this)[PlanetViewModel::class.java]

        // Create the adapter for the RecyclerView, passing in a lambda function to handle planet item clicks
        adapter = PlanetAdapter { planet ->
            // When a planet item is clicked, navigate to UpdatePlanetFragment with the selected planet
            val action =
                SelectPlanetToUpdateFragmentDirections.actionSelectPlanetToUpdateFragmentToUpdatePlanetFragment(
                    planet
                )
            findNavController().navigate(action)
        }

        // Set up the RecyclerView with the adapter and a LinearLayoutManager
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SelectPlanetToUpdateFragment.adapter
        }

        // Observe the LiveData for all planets and update the adapter when the data changes
        viewModel.allPlanets.observe(viewLifecycleOwner) { planets ->
            adapter.setPlanets(planets)
        }
    }

    // Clean up the binding variable when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // The adapter for the RecyclerView, responsible for creating and managing planet item views
    inner class PlanetAdapter(
        private val onPlanetClick: (Planet) -> Unit
    ) :
        RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder>() {

        // The list of planets to display
        private var planets: List<Planet> = emptyList()

        // Update the list of planets and refresh the adapter
        fun setPlanets(planets: List<Planet>) {
            this.planets = planets
            notifyDataSetChanged()
        }

        // Create a new ViewHolder for the planet item layout
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanetViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.planet_item, parent, false)
            return PlanetViewHolder(view)
        }

        // Bind a planet to a ViewHolder, updating the view to display the planet's data
        override fun onBindViewHolder(holder: PlanetViewHolder, position: Int) {
            val planet = planets[position]
            holder.bind(planet)
        }

        // Return the number of planets in the list
        override fun getItemCount(): Int = planets.size

        // The ViewHolder for a planet item, responsible for managing the
        // views that display a planet's data
        inner class PlanetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val planetName: TextView = itemView.findViewById(R.id.planet_name)
            private val distanceFromSun: TextView = itemView.findViewById(R.id.distance_from_sun)
            private val gravity: TextView = itemView.findViewById(R.id.gravity)
            private val planetImage: ImageView = itemView.findViewById(R.id.planet_image)

            // Bind a planet to the ViewHolder, updating the views to display the planet's data
            @SuppressLint("SetTextI18n")
            fun bind(planet: Planet) {
                // Set a click listener on the item view to handle clicks on the planet item
                itemView.setOnClickListener { onPlanetClick(planet) }

                // Set the planet name, distance from Sun, and gravity text fields
                planetName.text = planet.name
                distanceFromSun.text = "Distance from Sun: ${planet.distanceFromSun} AU"
                gravity.text = "Gravity: ${planet.gravity} m/s²"

                // Set the planet image
                planetImage.setImageResource(getImageResource(planet.name))
            }
        }

        // Get the resource ID for a planet image based on the planet name
        private fun getImageResource(imageName: String): Int {
            return when (imageName.lowercase(Locale.ROOT)) {
                "mercury" -> R.drawable.mercury
                "venus" -> R.drawable.venus
                "earth" -> R.drawable.earth
                "mars" -> R.drawable.mars
                "jupiter" -> R.drawable.jupiter
                "saturn" -> R.drawable.saturn
                "uranus" -> R.drawable.uranus
                "neptune" -> R.drawable.neptune
                else -> R.drawable.mercury // Use the default image for unknown planet names
            }
        }
    }
}