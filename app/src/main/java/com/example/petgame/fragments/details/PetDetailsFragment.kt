package com.example.petgame.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.petgame.PetViewModel
import com.example.petgame.data.Model.Pet
import com.example.petgame.databinding.FragmentPetDetailsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PetDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PetDetailsFragment : Fragment() {
    private val navigationArgs: PetDetailsFragmentArgs by navArgs()
    lateinit var pet: Pet

    private val viewModel: PetViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, PetViewModel.Factory(activity.application))
            .get(PetViewModel::class.java)
    }

    private var _binding: FragmentPetDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPetDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.petId
        // Retrieve the item details using the itemId.
        // Attach an observer on the data (instead of polling for changes) and only update the
        // the UI when the data actually changes.
        viewModel.retrivePet(id).observe(this.viewLifecycleOwner) { selectedItem ->
            pet = selectedItem
            bind(pet)
        }
    }

    /**
     * Binds views with the passed in item data.
     */
    private fun bind(pet: Pet) {
        binding.apply {
            petName.text = pet.petName
            petImage.setImageBitmap(pet.petImage)
            //TODO: add onlcick for bowl filling
        }
    }

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}