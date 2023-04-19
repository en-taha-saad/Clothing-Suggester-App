package com.tahaproject.clothingsuggester.data.local

import android.content.SharedPreferences
import com.tahaproject.clothingsuggester.R
import com.tahaproject.clothingsuggester.data.models.requests.Outfit

class LocalDataImpl(
    private val sharedPreferences: SharedPreferences
) : LocalData {

    override fun saveSuggestedOutfit(id: String, lastTimeWorn: Long) {
        sharedPreferences.edit().putLong(id, lastTimeWorn).apply()
    }

    override fun getSuggestedOutfit(temperature: Double): Outfit? {
        val validOutfits = OUTFITS.filter { includeValidOutfit(it, temperature) }
        if (validOutfits.isEmpty()) return null
        val suggestedOutfit = validOutfits.random()
        saveSuggestedOutfit(suggestedOutfit.id, System.currentTimeMillis())
        return suggestedOutfit
    }

    private fun includeValidOutfit(outfit: Outfit, temperature: Double): Boolean {
        return !isOutfitWornInLastTwoDays(outfit.id) && outfit.temperature.contains(temperature.toInt())
    }

    override fun isOutfitWornInLastTwoDays(id: String): Boolean {
        val lastTimeWorn = sharedPreferences.getLong(id, 0)
        val twoDaysAgo = System.currentTimeMillis() - TWO_DAYS
        return lastTimeWorn > twoDaysAgo
    }

    companion object {
        const val TWO_DAYS = 2 * 24 * 60 * 60 * 1000

        val OUTFITS = listOf(
            Outfit(
                id = R.drawable.cold_1.toString(),
                imageResource = R.drawable.cold_1,
                temperature = -10..20
            ),
            Outfit(
                id = R.drawable.cold_2.toString(),
                imageResource = R.drawable.cold_2,
                temperature = -10..20
            ),
            Outfit(
                id = R.drawable.cold_3.toString(),
                imageResource = R.drawable.cold_3,
                temperature = -10..20
            ),
            Outfit(
                id = R.drawable.cold_4.toString(),
                imageResource = R.drawable.cold_4,
                temperature = -10..20
            ),
            Outfit(
                id = R.drawable.cold_5.toString(),
                imageResource = R.drawable.cold_5,
                temperature = -10..20
            ),
            Outfit(
                id = R.drawable.mid_1.toString(),
                imageResource = R.drawable.mid_1,
                temperature = 20..30
            ),
            Outfit(
                id = R.drawable.mid_2.toString(),
                imageResource = R.drawable.mid_2,
                temperature = 20..30
            ),
            Outfit(
                id = R.drawable.mid_3.toString(),
                imageResource = R.drawable.mid_3,
                temperature = 20..30
            ),
            Outfit(
                id = R.drawable.mid_4.toString(),
                imageResource = R.drawable.mid_4,
                temperature = 20..30
            ),
            Outfit(
                id = R.drawable.mid_5.toString(),
                imageResource = R.drawable.mid_5,
                temperature = 20..30
            ),
            Outfit(
                id = R.drawable.hot_1.toString(),
                imageResource = R.drawable.hot_1,
                temperature = 30..40
            ),
            Outfit(
                id = R.drawable.hot_2.toString(),
                imageResource = R.drawable.hot_2,
                temperature = 30..40
            ),
            Outfit(
                id = R.drawable.hot_3.toString(),
                imageResource = R.drawable.hot_3,
                temperature = 30..40
            ),
            Outfit(
                id = R.drawable.hot_4.toString(),
                imageResource = R.drawable.hot_4,
                temperature = 30..40
            ),
            Outfit(
                id = R.drawable.hot_5.toString(),
                imageResource = R.drawable.hot_5,
                temperature = 30..40
            ),
        )
    }

}