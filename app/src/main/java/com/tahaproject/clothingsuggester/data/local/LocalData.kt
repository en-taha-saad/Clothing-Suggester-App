package com.tahaproject.clothingsuggester.data.local

import com.tahaproject.clothingsuggester.data.models.requests.Outfit


interface LocalData {
    fun saveSuggestedOutfit(id: String, lastTimeWorn: Long)

    fun getSuggestedOutfit(temperature: Double): Outfit?

    fun isOutfitWornInLastTwoDays(id: String): Boolean
}