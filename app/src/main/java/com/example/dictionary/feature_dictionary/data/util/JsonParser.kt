package com.example.dictionary.feature_dictionary.data.util

import java.lang.reflect.Type

/* Create a generic interface that are suitable with any converter library that you are going to use */
/* Note: This interface makes it easier to change the convection library that we use without changing every single line related to this conversion in the code manually */
interface JsonParser {
    fun <T> fromJson(json: String, type: Type): T?

    fun<T> toJson(obj: T, type: Type): String?
}