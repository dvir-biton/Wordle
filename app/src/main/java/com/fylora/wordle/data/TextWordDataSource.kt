package com.fylora.wordle.data

import android.content.Context
import com.fylora.wordle.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class TextWordDataSource(
    private val context: Context
): WordDataSource {
    override suspend fun getRandomWord(): String {
        return withContext(Dispatchers.IO) {
            val inputStream = context.resources.openRawResource(R.raw.valid_words)
            lateinit var selectedWord: String
            var count = 0

            inputStream.bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    count++
                    if (Random.nextInt(count) == 0) {
                        selectedWord = line
                    }
                }
            }

            selectedWord
        }
    }

    override suspend fun doesWordExist(word: String): Boolean {
        return withContext(Dispatchers.IO) {
            val inputStream = context.resources.openRawResource(R.raw.valid_words)

            inputStream.bufferedReader().useLines { lines ->
                lines.any { line ->
                    word.lowercase() == line
                }
            }
        }
    }
}