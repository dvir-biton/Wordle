package com.fylora.wordle.data

interface WordDataSource {
    suspend fun getRandomWord(): String
    suspend fun doesWordExist(word: String): Boolean
}