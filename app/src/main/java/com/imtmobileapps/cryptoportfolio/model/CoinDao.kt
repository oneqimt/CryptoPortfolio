package com.imtmobileapps.cryptoportfolio.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoinDao {

    // returns the primary keys
    // suspend - on a background thread
    @Insert
    suspend fun insertAll(vararg coins: CryptoValue):List<Long>


    @Query(value = "SELECT * FROM cryptovalue")
    suspend fun getPersonCoins(): List<CryptoValue>

    @Query(value = "SELECT * FROM cryptovalue WHERE uuid = :cryptoId")
    suspend fun getCoin(cryptoId: Int):CryptoValue

    @Query(value = "DELETE FROM cryptovalue")
    suspend fun deleteAllCoins()



}