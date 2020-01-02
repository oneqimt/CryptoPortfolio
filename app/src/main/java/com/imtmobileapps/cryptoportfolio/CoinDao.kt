package com.imtmobileapps.cryptoportfolio

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.imtmobileapps.cryptoportfolio.model.CryptoValue

@Dao
interface CoinDao {

    // returns the primary keys
    // suspend - on a background thread
    @Insert
    suspend fun insertAll(vararg dogs: CryptoValue):List<Long>

    @Query(value = "SELECT * FROM cryptovalue")
    suspend fun getPersonCoins(): List<CryptoValue>

    @Query(value = "SELECT * FROM cryptovalue WHERE uuid = :cryptoId")
    suspend fun getCoin(cryptoId: Int):CryptoValue

    @Query(value = "DELETE FROM cryptovalue")
    suspend fun deleteAllCoins()

}