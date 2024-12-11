package com.fylora.wordle.ui

class MapCharacterToStateUseCase {

    operator fun invoke(
        guess: Word,
        word: String
    ): List<Character> {
        val wordCharCounts = mutableMapOf<Char, Int>()

        word.forEach { char ->
            wordCharCounts[char] = wordCharCounts.getOrDefault(char, 0) + 1
        }

        val result = guess.characters.mapIndexed { index, character ->
            if (word[index] == character.char) {
                wordCharCounts[character.char] = wordCharCounts[character.char]!! - 1
                Character(character.char, LetterType.CorrectSpot)
            } else {
                character
            }
        }.toMutableList()

        result.forEachIndexed { index, character ->
            if (character.type == LetterType.NotSubmitted) {
                if (wordCharCounts.getOrDefault(character.char, 0) > 0) {
                    wordCharCounts[character.char] = wordCharCounts[character.char]!! - 1
                    result[index] = Character(character.char, LetterType.WrongSpot)
                } else {
                    result[index] = Character(character.char, LetterType.NotInWord)
                }
            }
        }

        return result
    }
}
