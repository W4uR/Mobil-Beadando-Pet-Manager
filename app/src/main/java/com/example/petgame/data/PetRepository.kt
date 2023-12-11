package com.example.petgame.data

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import coil.Coil
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.petgame.R
import com.example.petgame.data.Model.Pet
import com.example.petgame.data.Model.PetDao
import com.example.petgame.data.Model.PetWithStats
import com.example.petgame.data.RDS.DogApiService
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.LocalDateTime

class PetRepository(private var petDao: PetDao,private var dogApiService: DogApiService){
    private val _dogImageBitmap = MutableLiveData<Bitmap?>()
    val dogImageBitmap get() = _dogImageBitmap

    suspend fun fetchNewDogImage(context: Context) {
        try {
            val imageUrl = dogApiService.getRandomDogImageUrl().message

            if(imageUrl!=null) {
                loadImage(context, imageUrl)
            }
        } catch (e: Exception) {
            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun loadImage(context: Context, imageUrl: String){
        try {
            val loading = ImageLoader(context)
            val request = ImageRequest.Builder(context).data(imageUrl).build()
            val result = (loading.execute(request) as SuccessResult).drawable
            _dogImageBitmap.postValue((result as BitmapDrawable).bitmap)
        } catch (e: Exception) {
            Toast.makeText(context,"Couldn't find another dog :( try again",Toast.LENGTH_SHORT).show()
        }
    }

    val  getPets : LiveData<List<Pet>> = petDao.getPets()
    val petsWithStats: LiveData<List<PetWithStats>> = getPets.map { pets ->
        pets.map { PetWithStats(it, calcHunger(it.lastFed),calcThirst(it.lastDrink)) }
    }


    fun getPet(id: Int): LiveData<Pet> {
        return petDao.getPet(id);
    }

    fun getPetWithStats(id:Int) :LiveData<PetWithStats>{
        return petToPetWithStats(getPet(id))
    }

    suspend fun insert(pet: Pet){
        petDao.insert(pet)
    }


    fun petToPetWithStats(pet:LiveData<Pet>):LiveData<PetWithStats>{
        return pet.map { PetWithStats(it, calcHunger(it.lastFed),calcThirst(it.lastDrink))}
    }

    fun calcHunger(lastFed: LocalDateTime) : Int{
        val now = LocalDateTime.now()
        val hours: Float = Duration.between(lastFed, now).toMinutes().toFloat()
        return interpolate(15f,hours)
    }
    fun calcThirst(lastDrink: LocalDateTime) : Int{
        val now = LocalDateTime.now()
        val hours: Float = Duration.between(lastDrink, now).toMinutes().toFloat()
        return interpolate(7f,hours)
    }

    fun interpolate(maxHour:Float, input: Float): Int {
        return when {
            input <= 0 -> 100
            input >= maxHour -> 0
            else -> {
                val startValue = 100
                val endValue = 0
                // Linear interpolation formula
                val interpolatedValue = startValue + (endValue - startValue) * (input / maxHour)
                return interpolatedValue.toInt()
            }
        }
    }

}