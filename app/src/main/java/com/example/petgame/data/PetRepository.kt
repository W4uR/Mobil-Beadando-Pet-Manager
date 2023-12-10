package com.example.petgame.data

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.Coil
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.petgame.R
import com.example.petgame.data.Model.Pet
import com.example.petgame.data.Model.PetDao
import com.example.petgame.data.RDS.DogApiService

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


    fun getPet(id: Int): LiveData<Pet> {
        return petDao.getPet(id);
    }

    suspend fun insert(pet: Pet){
        petDao.insert(pet)
    }


}