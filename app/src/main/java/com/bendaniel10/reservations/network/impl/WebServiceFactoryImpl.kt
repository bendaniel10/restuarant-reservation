package com.bendaniel10.reservations.network.impl

import com.bendaniel10.reservations.network.WebServiceBuilder
import com.bendaniel10.reservations.network.WebServiceFactory
import com.bendaniel10.reservations.network.service.ApiWebService

open class WebServiceFactoryImpl(val webServiceBuilder: WebServiceBuilder) : WebServiceFactory {


    override fun buildPublicService(endPoint: String, port: String?): ApiWebService {

        var baseUrl = endPoint

        if (!endPoint.endsWith("/")) {
            baseUrl = endPoint.plus("/")
        }

        val builder = webServiceBuilder
                .clearInterceptors()
                .withEndpoint(baseUrl)
                .withPort(port)

        return builder.build(ApiWebService::class.java, logNetworkRequests = true) as ApiWebService

    }

}