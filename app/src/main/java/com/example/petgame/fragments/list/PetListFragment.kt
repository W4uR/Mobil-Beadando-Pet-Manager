package com.example.petgame.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petgame.PetViewModel
import com.example.petgame.databinding.FragmentPetListBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PetListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PetListFragment : Fragment() {
    private val viewModel: PetViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, PetViewModel.Factory(activity.application))
            .get(PetViewModel::class.java)
    }

    private var _binding: FragmentPetListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPetListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemListAdapter {
            val action =
                PetListFragmentDirections.actionPetListFragmentToPetDetailsFragment(it.pet.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes.
        viewModel.getPetsWithStats.observe(this.viewLifecycleOwner) { pets ->
            pets.let {
                adapter.submitList(it)
            }
        }
        binding.floatingActionButton.setOnClickListener {
            val action = PetListFragmentDirections.actionPetListFragmentToPetAddFragment()
            this.findNavController().navigate(action)
        }
    }
}