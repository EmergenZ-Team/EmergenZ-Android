package com.bangkit.emergenz.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bangkit.emergenz.data.dao.ArticleDao
import com.bangkit.emergenz.data.local.model.CachedArticle

@Database(entities = [CachedArticle::class], version = 1)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile
        private var instance: ArticleDatabase? = null

        fun getInstance(context: Context): ArticleDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): ArticleDatabase {
            return Room.databaseBuilder(context.applicationContext, ArticleDatabase::class.java, "app_database")
                .build()
        }
    }
}