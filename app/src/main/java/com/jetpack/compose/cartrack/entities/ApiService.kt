package com.jetpack.compose.cartrack.entities

import com.jetpack.compose.cartrack.R
import com.jetpack.compose.cartrack.application.Cartrack
import io.reactivex.rxjava3.core.Single
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiService {

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com"
    }

    private val hostname = "jsonplaceholder.typicode.com"
    private val pinning: CertificatePinner = CertificatePinner.Builder().add(
        hostname, "sha256/"
                + Cartrack.context.resources.openRawResource(R.raw.cartrack_cert)
    ).build()

    private fun getOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder().certificatePinner(pinning)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        val httpClient = client.build()
        httpClient.dispatcher().maxRequestsPerHost = 30
        return httpClient
    }

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(getOkHttpClient())
        .build()
        .create(Api::class.java)

    fun getCarTrackUsers(): Single<List<CarTrackUsers>> {
        return api.getRepository()
    }
}