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
import com.example.planetopedia.databinding.FragmentUpdatePlanetBinding
import java.util.*

class PlanetsListFragment : Fragment() {

    private lateinit var viewModel: PlanetViewModel
    private lateinit var adapter: PlanetAdapter

    // Declare the binding variable for this fragment
    private var _binding: PlanetsListFragmentDirections? = null
    private val binding get() = _binding!!


    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_planets_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this)[PlanetViewModel::class.java]

        // Create the adapter for the RecyclerView, passing in a lambda function to handle planet item clicks
        // Set up the adapter for the RecyclerView
        adapter = PlanetAdapter()

        // Set the adapter on the RecyclerView
        view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PlanetsListFragment.adapter
        }

        // Observe the LiveData for all planets and update the adapter when it changes
        viewModel.allPlanets.observe(viewLifecycleOwner) { planets ->
            adapter.setPlanets(planets)
        }

    }

    // The adapter for the RecyclerView
    inner class PlanetAdapter :
        RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder>() {

        private var planets: List<Planet> = emptyList()

        // Set the list of planets and update the adapter
        @SuppressLint("NotifyDataSetChanged")
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

        // Bind a planet to a ViewHolder
        override fun onBindViewHolder(holder: PlanetViewHolder, position: Int) {
            val planet = planets[position]
            holder.bind(planet)
        }

        // Return the number of planets in the list
        override fun getItemCount(): Int = planets.size

        // The ViewHolder for a planet item
        inner class PlanetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val planetName: TextView = itemView.findViewById(R.id.planet_name)
            private val distanceFromSun: TextView = itemView.findViewById(R.id.distance_from_sun)
            private val gravity: TextView = itemView.findViewById(R.id.gravity)
            private val planetImage: ImageView = itemView.findViewById(R.id.planet_image)

            // Bind a planet to the ViewHolder
            @SuppressLint("SetTextI18n")
            fun bind(planet: Planet) {
                // Set the planet name, distance from Sun, and gravity text fields
                planetName.text = planet.name
                distanceFromSun.text = "Distance from Sun: ${planet.distanceFromSun} AU"
                gravity.text = "Gravity: ${planet.gravity} m/s²"

                // Set the planet image
                planetImage.setImageResource(getImageResource(planet.name))
            }
        }
    }


    // Get the resource ID for a planet image based on the planet name
    private fun getImageResource(imageName: String?): Int {
        return when (imageName?.lowercase(Locale.ROOT)) {
            "mercury" -> R.drawable.mercury
            "venus" -> R.drawable.venus
            "earth" -> R.drawable.earth
            "mars" -> R.drawable.mars
            "jupiter" -> R.drawable.jupiter
            "saturn" -> R.drawable.saturn
            "uranus" -> R.drawable.uranus
            "neptune" -> R.drawable.neptune
            // Add other planets' images if available
            else -> R.drawable.mercury // Use a default image for unknown planet names
        }
    }


}