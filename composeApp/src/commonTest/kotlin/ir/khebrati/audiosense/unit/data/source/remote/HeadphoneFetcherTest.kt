package ir.khebrati.audiosense.unit.data.source.remote

import ir.khebrati.audiosense.data.source.remote.HeadphoneFetcherImpl
import ir.khebrati.audiosense.data.source.remote.TokenManager
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class HeadphoneFetcherTest {
    @Test
    fun test_fetching_headphones_list(){
        val fetch = HeadphoneFetcherImpl(TokenManager())
        runBlocking {
            fetch.fetchAllFromServer()
        }
    }
}
