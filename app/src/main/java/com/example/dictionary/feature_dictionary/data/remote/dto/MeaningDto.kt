package com.example.dictionary.feature_dictionary.data.remote.dto

import com.example.dictionary.feature_dictionary.domain.model.Meaning

data class MeaningDto(
    val antonyms: List<String>?,
    val definitions: List<DefinitionDto>,
    val partOfSpeech: String,
    val synonyms: List<String>?
){
    /* Mapper */
    fun toMeaning():Meaning{
        return Meaning(
            /* Since the domain layer use List<Definition> instead of List<DefinitionDto>, we have to map List<DefinitionDto> to List<Definition>
             * we can do that by using map scope function and map each definitions in List<DefinitionDto> to List<Definition> by using toDefinition() mapper*/
            definitions = definitions.map{it.toDefinition()},
            partOfSpeech = partOfSpeech
            /* We do not need those attributes in the domain layer or in the UI layer*/
//            antonyms = antonyms,
//            synonyms = synonyms
        )
    }
}