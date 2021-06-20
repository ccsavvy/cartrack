package com.jetpack.compose.cartrack.entities

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("users")
    fun getRepository(): Single<Repository>
}