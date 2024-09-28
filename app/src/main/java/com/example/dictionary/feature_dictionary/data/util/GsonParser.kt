package com.example.dictionary.feature_dictionary.data.util

import com.google.gson.Gson
import java.lang.reflect.Type

/* We will use Gson Converter library here by implementing JsonParser interface */
/* note: we can use here any other library such as Moshi parser, kotlin serialization or jackson*/
/* At the end you will need to add this Room.addTypeConverter(GsonParser(Gson())) while you create Room instance to tell room how to convert this attribute */
class GsonParser(
    private val gson: Gson
): JsonParser {
    override fun <T> fromJson(json: String, type: Type): T? {
        return gson.fromJson(json,type)
    }

    override fun <T> toJson(obj: T, type: Type): String? {
        return gson.toJson(obj,type)
    }
}