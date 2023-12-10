package com.example.petgame.data.RDS// com.example.petgame.data.RDS.DogApiService.kt
import retrofit2.http.GET

interface DogApiService {
    @GET("breeds/image/random")
    suspend fun getRandomDogImageUrl(): DogApiResponse
}
