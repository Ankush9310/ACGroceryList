package com.ankushchaudhary.acgrocerylist.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query(" SELECT * FROM item ORDER BY name ASC ")
    fun getItem(): Flow<List<Item>>

    @Query (" SELECT * FROM item WHERE id = :id ")
    fun getItem(int: Int): Flow<Item>

    /*
        Specify the onConflict strategy as IGNORE, when the user tries to add
        an existing Item into the database Room ignore the conflict.
     */

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

}