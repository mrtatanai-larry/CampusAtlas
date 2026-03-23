package com.techiteasy.campusatlas.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmark_lists")
    fun getAllLists(): Flow<List<BookmarkList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: BookmarkList)

    @Update
    suspend fun updateList(list: BookmarkList)

    @Delete
    suspend fun deleteList(list: BookmarkList)
}
