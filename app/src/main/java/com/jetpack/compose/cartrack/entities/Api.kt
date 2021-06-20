package com.jetpack.compose.cartrack.entities

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface Api {
    @GET("users")
    fun getRepository(): Single<List<CarTrackUsers>>
}