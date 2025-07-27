package ir.khebrati.audiosense.data.source

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.khebrati.audiosense.data.source.local.AppDatabase

fun getDatabaseBuilder(ctx: Lazy<Context>) : RoomDatabase.Builder<AppDatabase>{
    val appContext = ctx.value.applicationContext
    val dbFile = appContext.getDatabasePath("audiosense.db")
    return Room.databaseBuilder(
        context = appContext,
        name = dbFile.absolutePath
    )
}