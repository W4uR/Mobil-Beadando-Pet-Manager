package com.example.petgame.data.Model

import java.text.NumberFormat

data class PetWithStats(
    val pet: Pet,
    val foodBowlPercentage: Int,
    val waterBowlPercentage: Int
)
fun PetWithStats.getFormatedFoodPercentage(): String = "$foodBowlPercentage%"
fun PetWithStats.getFormatedWaterPercentage(): String ="$waterBowlPercentage%"
fun PetWithStats.getFormatedHappiness(): String{
    return ((foodBowlPercentage+waterBowlPercentage)/2).toString()+"%"
}