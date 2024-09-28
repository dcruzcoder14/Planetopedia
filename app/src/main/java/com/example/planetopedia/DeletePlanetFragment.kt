package com.example.planetopedia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.planetopedia.databinding.FragmentDeletePlanetBinding

// This is the DeletePlanetFragment, responsible for displaying a confirmation
// dialog to delete the selected planet and navigating back to the planet list
class DeletePlanetFragment : Fragment() {

    // Declare the ViewModel and binding variables, as well as the navigation arguments
    private lateinit var viewModel: PlanetViewModel
    private val args: DeletePlanetFragmentArgs by navArgs()
    private var _binding: FragmentDeletePlanetBinding? = null
    private val binding get() = _binding!!

    // Inflate the layout for this fragment.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeletePlanetBinding.inflate(inflater, container, false)
        return binding.root
    }

    // This method is called when the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel.
        viewModel = ViewModelProvider(this).get(PlanetViewModel::class.java)

        // Get the planet passed as an argument from the previous fragment
        val planet = args.planet

        // Show a confirmation dialog to the user before deleting the planet.
        // We use the context of the current fragment to create the AlertDialog
        context?.let {
            AlertDialog.Builder(it)
                .setTitle("Delete Planet") // Set the title of the dialog.
                .setMessage("Are you sure you want to delete ${planet.name}?") // Set the message of the dialog
                .setPositiveButton("Yes") { _, _ -> // Set the positive button (Yes) and its click listener
                    viewModel.delete(planet) // Delete the planet using the ViewModel
                    // Navigate back to the planet list after the planet is deleted
                    findNavController().navigate(R.id.action_deletePlanetFragment_to_planetsListFragment)
                }
                .setNegativeButton("No") { _, _ -> // Set the negative button (No) and its click listener
                    // Navigate back to the planet list without deleting the planet
                    findNavController().navigate(R.id.action_deletePlanetFragment_to_planetsListFragment)
                }
                .show() // Display the AlertDialog
        }
    }

    // This method is called when the view is destroyed
    // It's used to clean up the binding reference to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
