package com.example.dictionary.feature_dictionary.data.repository

import com.example.dictionary.core.util.Resource
import com.example.dictionary.feature_dictionary.data.local.WordInfoDao
import com.example.dictionary.feature_dictionary.data.remote.DictionaryApi
import com.example.dictionary.feature_dictionary.domain.model.WordInfo
import com.example.dictionary.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class WordInfoRepositoryImpl(
    private val api: DictionaryApi,
    private val dao: WordInfoDao
): WordInfoRepository {

    /* This is where the cashing logic belongs, because that is what the repository is used for and what is the job of a repository.
     * It is used to take all different data sources you have in our case that's api and database and then it will take that and decide
     * which data should be forwarded to the viewmodel into the UI  */

    /* When it comes to cashing you should usually stick to single source of truth principle. what that means is all data that we ever get and display
     * in our UI comes from the database, so we will never get data directly from the api and directly display it in UI. */

    /* What we are going to do is we get the data from the api and insert it into the database to then show it in our UI from the database. whenever you deal with cashing you will use single source of truth */

    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        /** 1.First emit loading before starting our data base request **/
        emit(Resource.Loading())

        /** 2.Read the current word from our data base [the cashed ones] and emit this with loading status again **/
        /* Get the word info from the database and map wordInfoEntity object which came from DB request into WordInfo object from domain layer */
        /* We would like to emit these to our UI so we can directly display these even if we are still loading and waiting from our api */
        val wordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        /* This now will notify the viewmodel that there is actually word in first to display in our UI and the next step make the api request */
        emit(Resource.Loading(data = wordInfos))

        /** 3.Initiate the api call and when we get the result we will replace th items in our dat base with what we got from the api **/
        try {
            /* Get Word info from API request */
            val remoteWordInfo = api.getWordInfo(word)
            /* Delete the existing word info that have the same word from the data base */
            dao.deleteWordInfos(remoteWordInfo.map{it.word})
            dao.insertWordInfos(remoteWordInfo.map { it.toWordInfoEntity() })
        }
        /** 4.If we have errors we will simply emit error **/
        /* Invalid response exception */
        catch (e: HttpException){
            emit(Resource.Error(
                message = "Something Went Wrong!",
                data = wordInfos
            ))
        }
        /* Internet connection exception or something in the parsing went wrong */
        catch (e: IOException){
            emit(Resource.Error(
                message = "Couldn't reach server, check your internet connection",
                data = wordInfos
            ))
        }

        /** 5.if we have no errors we simply emits success resource with the word we got from our data base **/
        val newWordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Success(data = newWordInfos))
    }
}