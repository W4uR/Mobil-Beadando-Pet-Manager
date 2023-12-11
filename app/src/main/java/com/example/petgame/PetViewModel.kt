package com.example.petgame

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.petgame.data.Model.Pet
import com.example.petgame.data.PetRepository
import com.example.petgame.data.Model.PetRoomDatabase
import com.example.petgame.data.Model.PetWithStats
import com.example.petgame.data.RDS.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.util.Date

class PetViewModel(application: Application) : AndroidViewModel(application) {

    val getPets: LiveData<List<Pet>>
    val getPetsWithStats: LiveData<List<PetWithStats>>
    private val repository: PetRepository
    val randomDogImage : MutableLiveData<Bitmap?>


    init {
        val petDao = PetRoomDatabase.getDatabase(application).petDao()
        val dogApiService = RetrofitClient.instance
        repository = PetRepository(petDao,dogApiService)
        randomDogImage = repository.dogImageBitmap
        getPets = repository.getPets
        getPetsWithStats = repository.petsWithStats
    }

    fun addPet(pet: Pet){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(pet)
        }
    }

    fun retrivePet(id: Int): LiveData<Pet> {
        return repository.getPet(id);
    }

    fun retrivePetWithStats(id: Int): LiveData<PetWithStats> {
        return repository.getPetWithStats(id);
    }


    fun isEntryValid(petName: String): Boolean {
        if (petName.isBlank()) {
            return false
        }
        return true
    }

    fun getRandomDog(context:Context){
        viewModelScope.launch {
            repository.fetchNewDogImage(context)
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PetViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PetViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}