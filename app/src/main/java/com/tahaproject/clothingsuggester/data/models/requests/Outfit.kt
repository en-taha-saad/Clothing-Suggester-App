package com.tahaproject.clothingsuggester.data.models.requests

data class Outfit(
    val id: String,
    val imageResource: Int,
    val temperature: IntRange,
)