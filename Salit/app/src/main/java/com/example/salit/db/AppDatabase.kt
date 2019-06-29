package com.example.salit.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.salit.db.dao.SaleDao
import com.example.salit.db.models.*
import java.util.concurrent.Executors
import android.support.annotation.NonNull
import com.example.salit.db.dao.CategoryDao
import com.example.salit.db.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Database(
    entities = [Image::class, Location::class, Sale::class, Category::class, User::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun SaleDao(): SaleDao
    abstract fun CategoryDao(): CategoryDao
    abstract fun UserDao(): UserDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AppDatabase"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        GlobalScope.launch(Dispatchers.IO) {
                            val database = getDatabase(context)
                            val categoryDao = database.CategoryDao()
                            val saleDao = database.SaleDao()
                            val userDao = database.UserDao()
                            categoryDao.insertAll(Category("All categories"))
                            categoryDao.insertAll(Category("Fashion"))
                            categoryDao.insertAll(Category("Technology"))
                            categoryDao.insertAll(Category("Sport"))
                            categoryDao.insertAll(Category("Others"))
                            userDao.insertAll(User("user1@gmail.com"))
                            userDao.insertAll(User("user2@gmail.com"))
                            userDao.insertAll(User("user3@gmail.com"))

                        }
                    }
                }).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

