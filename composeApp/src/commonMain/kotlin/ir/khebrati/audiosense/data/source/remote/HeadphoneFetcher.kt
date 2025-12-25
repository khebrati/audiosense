package ir.khebrati.audiosense.data.source.remote

import ir.khebrati.audiosense.data.source.remote.entity.RemoteHeadphone

interface HeadphoneFetcher {
    suspend fun fetchAllFromServer() : List<RemoteHeadphone>
}
