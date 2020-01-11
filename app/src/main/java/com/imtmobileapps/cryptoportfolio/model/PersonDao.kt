package com.imtmobileapps.cryptoportfolio.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: Person):Long

    @Query(value = "SELECT * FROM person WHERE person_id = :personId")
    suspend fun getPerson(personId: Int):Person

    @Query(value = "DELETE FROM person")
    suspend fun deletePerson()

}