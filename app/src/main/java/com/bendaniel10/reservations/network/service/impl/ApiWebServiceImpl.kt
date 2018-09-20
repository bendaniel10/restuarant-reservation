package com.bendaniel10.reservations.network.service.impl

import com.bendaniel10.reservations.network.WebServiceFactory
import com.bendaniel10.reservations.network.service.ApiWebService
import io.reactivex.Observable
import okhttp3.ResponseBody

class ApiWebServiceImpl(private val webServiceFactory: WebServiceFactory) : ApiWebService {

    override fun fetchCustomersJSONFile(): Observable<ResponseBody> {

        val apiWebService = webServiceFactory.buildPublicService()

        return apiWebService.fetchCustomersJSONFile()

    }

    override fun fetchTablesJSONFile(): Observable<ResponseBody> {

        val apiWebService = webServiceFactory.buildPublicService()

        return apiWebService.fetchTablesJSONFile()

    }


}