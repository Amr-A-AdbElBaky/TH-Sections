package com.example.thmanyah.features.sections.data.source.remote

import com.example.thmanyah.BuildConfig
import com.example.thmanyah.features.sections.data.model.HomeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SectionsApi {
    @GET(BuildConfig.HOME_SECRIONS_END_POINT)
    suspend fun getHomeSections(@Query("page") page: Int,
                                @Query("name") name: String?): HomeResponse
}