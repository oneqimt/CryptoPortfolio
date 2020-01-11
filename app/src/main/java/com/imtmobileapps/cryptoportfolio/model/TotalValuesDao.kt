package com.imtmobileapps.cryptoportfolio.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TotalValuesDao {

    @Query(value = "SELECT * FROM totalvalues WHERE person_id = :personId")
    suspend fun getTotalValues(personId: Int):TotalValues

    @Query(value = "DELETE FROM totalvalues")
    suspend fun deleteAllTotalValues()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTotalValues(totalValues: TotalValues):Long


}