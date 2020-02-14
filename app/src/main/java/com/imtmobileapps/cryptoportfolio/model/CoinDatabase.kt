package com.imtmobileapps.cryptoportfolio.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(CryptoValue::class, Person::class, TotalValues::class), version = 15)
abstract class CoinDatabase :RoomDatabase(){

    abstract fun coinDao() : CoinDao
    abstract fun totalValuesDao() : TotalValuesDao
    abstract fun personDao() : PersonDao

    //SINGLETON
    companion object {
        @Volatile private var instance : CoinDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{
                instance = it
            }

        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, CoinDatabase::class.java, "coindatabase"
        ).fallbackToDestructiveMigration().build()
    }

}