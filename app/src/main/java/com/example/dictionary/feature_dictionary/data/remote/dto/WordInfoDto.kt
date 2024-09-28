package com.example.dictionary.feature_dictionary.data.remote.dto

import com.example.dictionary.feature_dictionary.data.local.entity.WordInfoEntity


data class WordInfoDto(
    val meanings: List<MeaningDto>,
    val phonetic: String,
    val phonetics: List<PhoneticDto>,
//    val sourceUrls: List<String>,
    val word: String
){
    /* Mapper */
    fun toWordInfoEntity(): WordInfoEntity{
        return WordInfoEntity(
            meanings = meanings.map { it.toMeaning() },
            phonetic = phonetic,
            word = word
            /* we may use it in the future */
//            phonetics = phonetics
            /* will not be used at all */
//            sourceUrls = sourceUrls
        )
    }
}