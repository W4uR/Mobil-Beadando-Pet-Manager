package com.example.petgame.fragments.add

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.petgame.PetViewModel
import com.example.petgame.data.Model.Pet
import com.example.petgame.databinding.FragmentPetAddBinding
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/**
 * A simple [Fragment] subclass.
 * Use the [PetAddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PetAddFragment : Fragment() {
    private val viewModel: PetViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, PetViewModel.Factory(activity.application))
            .get(PetViewModel::class.java)
    }


    lateinit var pet: Pet

    private var _binding: FragmentPetAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPetAddBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.randomDogImage.observe(viewLifecycleOwner,Observer { newValue ->
            binding.petImage.setImageBitmap(newValue)
        })

        binding.randomizeButton.setOnClickListener {
                viewModel.getRandomDog(requireContext())
        }
        binding.saveAction.setOnClickListener {

            insertPetToDatabase()
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.petName.text.toString()
        )
    }

    private fun insertPetToDatabase(){
        if (isEntryValid()){
            viewModel.addPet(Pet(id = 0, petName = binding.petName.text.toString(), petImage = binding.petImage.drawable.toBitmap(), lastFed = LocalDateTime.now(), lastDrink = LocalDateTime.now()))
            this.findNavController().navigate(PetAddFragmentDirections.actionPetAddFragmentToPetListFragment())
        }else{
            Toast.makeText(requireContext(), "The pet needs a name!", Toast.LENGTH_SHORT).show()
        }
    }
}