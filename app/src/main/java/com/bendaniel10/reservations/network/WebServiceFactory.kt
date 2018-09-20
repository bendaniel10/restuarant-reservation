package com.bendaniel10.reservations.network

import com.bendaniel10.reservations.app.Constants
import com.bendaniel10.reservations.network.service.ApiWebService

interface WebServiceFactory {

    fun buildPublicService(endPoint: String = Constants.REST_END_POINT, port: String? = null): ApiWebService

}