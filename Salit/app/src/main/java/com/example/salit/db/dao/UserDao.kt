package com.example.salit.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.salit.db.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email LIKE :email")
    fun getUser(email: String): User?

    @Insert
    fun insertAll(vararg user: User)
}