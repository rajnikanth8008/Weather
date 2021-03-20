package com.rk.weather.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rk.weather.data.db.entity.BookmarkEntity

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookmark(bookmarkEntity: BookmarkEntity)

    @Query("SELECT * FROM Bookmark")
    fun getBookmarkList(): LiveData<MutableList<BookmarkEntity>>

    @Delete
    fun deleteBookmark(bookmarkEntity: BookmarkEntity)
}