package com.example.dictionary.feature_dictionary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dictionary.feature_dictionary.domain.model.Meaning
import com.example.dictionary.feature_dictionary.domain.model.WordInfo

/* note: Whenever there is an variable in the entity that is not a primitive data type such as string, int, ...etc.
 * we have to create a converter to handel this variable
 * Example: val meanings: List<Meaning>, we need converter to convert it from list to string and vis verse. */

@Entity
data class WordInfoEntity(
    @PrimaryKey val id: Int? = null,
    /* Problem: we  */
    val meanings: List<Meaning>,
    val phonetic: String?,
    val word: String
){
    fun toWordInfo():WordInfo{
        return WordInfo(
            meanings = meanings,
            phonetic = phonetic,
            word = word
        )
    }
}