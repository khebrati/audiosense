package ir.khebrati.audiosense.data.source.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import ir.khebrati.audiosense.db.AudiosenseDb
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun createSqlDriver(): SqlDriver {
    return SqlDriverFactory().createDriver()
}

class SqlDriverFactory : KoinComponent {
    private val context: Context by inject()

    fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = AudiosenseDb.Schema,
            context = context,
            name = "audiosense.db"
        )
    }
}

