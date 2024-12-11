package com.fylora.wordle.data

import com.fylora.wordle.data.entity.GameStats

interface StatisticsService {
    suspend fun saveGameStats(gameStats: GameStats)
    suspend fun loadGameStats(): GameStats
}