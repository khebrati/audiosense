package ir.khebrati.audiosense.data.source

import androidx.room.Room
import androidx.room.RoomDatabase
import ir.khebrati.audiosense.data.source.local.AppDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask


fun getDatabaseBuilder() : RoomDatabase.Builder<AppDatabase> {
    val dbPath = documentDirectory() + "/audiosense.db"
    return Room.databaseBuilder(
        name = dbPath
    )
}
@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory() : String{
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}