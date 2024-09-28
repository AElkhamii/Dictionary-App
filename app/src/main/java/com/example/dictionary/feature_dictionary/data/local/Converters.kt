package com.example.dictionary.feature_dictionary.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.dictionary.feature_dictionary.data.util.JsonParser
import com.example.dictionary.feature_dictionary.domain.model.Meaning
import com.google.gson.reflect.TypeToken

/* note: Whenever there is an variable in the entity that is not a primitive data type such as string, int, ...etc. we have to create a converter to handel this variable */

/* This class will provide the converter functions that are responsible for switch between json string and List<Meaning> */
/* @ProvidedTypeConverter used to provide instance of Converters class, because by default tab converter can not have constructor arguments like we use here
 * but in our case we need that JsonParser here, so we need to provide our own converter which will later do, just for room to know that it should not create that class on its own. */
/* At the end go back to the Database class and pass this type converter to the database to let data base know how to handel the conversion process */
@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {
    @TypeConverter
    fun fromMeaningsJson(json: String): List<Meaning>{
        return jsonParser.fromJson<ArrayList<Meaning>>(
            json,
            object: TypeToken<ArrayList<Meaning>>(){}.type
        )?: emptyList()
    }

    @TypeConverter
    fun toMeaningsJson(meanings: List<Meaning>): String{
        return jsonParser.toJson(
            meanings,
            object: TypeToken<ArrayList<Meaning>>(){}.type
        )?: "[]"
    }
}