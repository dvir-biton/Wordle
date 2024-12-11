package com.fylora.wordle.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.fylora.wordle.data.entity.GameStats
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object PreferencesKeys {
    val WINS = intPreferencesKey("wins")
    val LOSSES = intPreferencesKey("losses")

    fun winsAtGuessKey(index: Int) = intPreferencesKey("wins_at_guess_$index")
}

val Context.dataStore by preferencesDataStore(name = "wordle_stats")

class DataStoreStatisticsService(private val context: Context) : StatisticsService {

    override suspend fun saveGameStats(gameStats: GameStats) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.WINS] = gameStats.wins
            preferences[PreferencesKeys.LOSSES] = gameStats.loses
            gameStats.winsAtGuess.forEach { (index, count) ->
                preferences[PreferencesKeys.winsAtGuessKey(index)] = count
            }
        }
    }

    override suspend fun loadGameStats(): GameStats {
        val preferences = context.dataStore.data.map { preferences ->
            GameStats(
                wins = preferences[PreferencesKeys.WINS] ?: 0,
                loses = preferences[PreferencesKeys.LOSSES] ?: 0,
                winsAtGuess = (1..6).associateWith { index ->
                    preferences[PreferencesKeys.winsAtGuessKey(index)] ?: 0
                }.toMutableMap()
            )
        }
        return preferences.first()
    }
}