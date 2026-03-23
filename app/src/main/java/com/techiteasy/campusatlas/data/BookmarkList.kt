package com.techiteasy.campusatlas.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_lists")
data class BookmarkList(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val iconType: String = "default" // "favorites", "starred", "custom"
)
