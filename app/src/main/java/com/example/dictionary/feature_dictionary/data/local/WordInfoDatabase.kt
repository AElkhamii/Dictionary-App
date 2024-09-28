package com.example.dictionary.feature_dictionary.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dictionary.feature_dictionary.data.local.entity.WordInfoEntity

/* Create the database */
@Database(
    entities = [WordInfoEntity::class],
    version = 1
)
/* pass this type converter to the database to let data base know how to handel the conversion process */
@TypeConverters(Converters::class)
abstract class WordInfoDatabase: RoomDatabase() {

    /* Instance from Data Access Object */
    abstract val dao: WordInfoDao
}