package com.bangkit.emergenz.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit.emergenz.data.local.model.CachedArticle

@Dao
interface ArticleDao {
    @Query("SELECT * FROM cache_table WHERE id = :id")
    fun getCachedDataById(id: Int): LiveData<CachedArticle?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCachedData(cachedData: CachedArticle)
}
