package ir.khebrati.audiosense.unit.data.source.remote

import ir.khebrati.audiosense.data.source.remote.HeadphoneFetcherImpl
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class HeadphoneFetcherTest {
    @Test
    fun `Test fetching headphones list`(){
        val fetch = HeadphoneFetcherImpl()
        runBlocking {
            fetch.fetchAllFromServer()
        }
    }
}
