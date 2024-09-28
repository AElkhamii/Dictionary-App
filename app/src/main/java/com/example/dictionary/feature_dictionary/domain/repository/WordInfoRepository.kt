package com.example.dictionary.feature_dictionary.domain.repository

import com.example.dictionary.core.util.Resource
import com.example.dictionary.feature_dictionary.domain.model.WordInfo
import kotlinx.coroutines.flow.Flow

interface WordInfoRepository {

    /* We actually use a flow here to emit multiple values over a period of time
     * So we can first emit our loading status then we can get our actual word wordinfo from the cash
     * We can then make the request, when we get the response we can emit another list of wordinfos
     * that we actually got from the api and so on*/
    fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>>
}