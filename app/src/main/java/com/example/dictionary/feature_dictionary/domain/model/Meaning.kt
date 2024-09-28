package com.example.dictionary.feature_dictionary.domain.model

import com.example.dictionary.feature_dictionary.data.remote.dto.DefinitionDto

data class Meaning(
    /* Note: we here used List<Definition> instead of List<DefinitionDto>, because we are in domain layer */
    val definitions: List<Definition>,
    val partOfSpeech: String
)
