package com.imtmobileapps.cryptoportfolio.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.imtmobileapps.cryptoportfolio.util.BigDecimalDoubleTypeConverter

@Database(entities = arrayOf(CryptoValue::class, Person::class, TotalValues::class), version = 21)
@TypeConverters(BigDecimalDoubleTypeConverter::class)
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