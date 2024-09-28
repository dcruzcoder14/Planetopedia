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
import com.example.planetopedia.databinding.FragmentSelectPlanetToDeleteBinding
import java.util.*

// This is the SelectPlanetToDeleteFragment, responsible for displaying the list
// of planets so that the user can select one to delete.
class SelectPlanetToDeleteFragment : Fragment() {

    // Declare the ViewModel, adapter, and binding variables.
    private lateinit var viewModel: PlanetViewModel
    private lateinit var adapter: PlanetAdapter
    private var _binding: FragmentSelectPlanetToDeleteBinding? = null
    private val binding get() = _binding!!

    // Inflate the layout for this fragment.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectPlanetToDeleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    // This method is called when the view is created.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel.
        viewModel = ViewModelProvider(this)[PlanetViewModel::class.java]

        // Initialize the adapter with a click listener to navigate to the DeletePlanetFragment
        // with the selected planet.
        adapter = PlanetAdapter { planet ->
            val action =
                SelectPlanetToDeleteFragmentDirections.actionSelectPlanetToDeleteFragmentToDeletePlanetFragment(
                    planet
                )
            findNavController().navigate(action)
        }

        // Setup the RecyclerView with the adapter and a LinearLayoutManager.
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SelectPlanetToDeleteFragment.adapter
        }

        // Observe the list of planets from the ViewModel and update the adapter accordingly.
        viewModel.allPlanets.observe(viewLifecycleOwner) { planets ->
            adapter.setPlanets(planets)
        }
    }

    // Clean up the binding reference when the view is destroyed.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Define the PlanetAdapter class, which is responsible for displaying the list of planets.
    inner class PlanetAdapter(
        private val onPlanetClick: (Planet) -> Unit
    ) :
        RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder>() {

        // Declare a list of planets to be displayed.
        private var planets: List<Planet> = emptyList()

        // Update the list of planets and notify the adapter of the changes.
        fun setPlanets(planets: List<Planet>) {
            this.planets = planets
            notifyDataSetChanged()
        }

        // Create a new ViewHolder for each item in the list.
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanetViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.planet_item, parent, false)
            return PlanetViewHolder(view)
        }

        // Bind the ViewHolder with the planet data at the specified position.
        override fun onBindViewHolder(holder: PlanetViewHolder, position: Int) {
            val planet = planets[position]
            holder.bind(planet)
        }

        // Get the number of items in the list.
        override fun getItemCount(): Int = planets.size

        // Define the PlanetViewHolder class, responsible for displaying an individual planet item.
        inner class PlanetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val planetName: TextView = itemView.findViewById(R.id.planet_name)
            private val distanceFromSun: TextView = itemView.findViewById(R.id.distance_from_sun)
            private val gravity: TextView = itemView.findViewById(R.id.gravity)
            private val planetImage: ImageView = itemView.findViewById(R.id.planet_image)

            // Bind the ViewHolder with the given planet data.
            @SuppressLint("SetTextI18n")
            fun bind(planet: Planet) {
                // Set the click listener for the item, calling the onPlanetClick function passed
                // to the adapter when the item is clicked.
                itemView.setOnClickListener { onPlanetClick(planet) }

                // Set the TextViews and ImageView with the planet data.
                planetName.text = planet.name
                distanceFromSun.text = "Distance from Sun: ${planet.distanceFromSun} AU"
                gravity.text = "Gravity: ${planet.gravity} m/s²"

                planetImage.setImageResource(getImageResource(planet.name))
            }
        }

        // Get the image resource ID for the given planet name.
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
