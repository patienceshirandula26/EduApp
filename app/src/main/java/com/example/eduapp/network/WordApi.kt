package com.example.eduapp.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path

data class DictionaryEntry(
    @SerializedName("word") val word: String?,
    @SerializedName("meanings") val meanings: List<Meaning>?
)

data class Meaning(
    @SerializedName("definitions") val definitions: List<Definition>?
)

data class Definition(
    @SerializedName("definition") val definition: String?
)

interface WordApi {
    @GET("api/v2/entries/en/{word}")
    suspend fun lookup(@Path("word") word: String): List<DictionaryEntry>

    companion object {
        const val BASE_URL = "https://api.dictionaryapi.dev/"
    }
}
