package com.bendaniel10.reservations.network.service

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming

interface ApiWebService {

    @Streaming
    @GET("customer-list.json")
    fun fetchCustomersJSONFile(): Observable<ResponseBody>

    @Streaming
    @GET("table-map.json")
    fun fetchTablesJSONFile(): Observable<ResponseBody>

}