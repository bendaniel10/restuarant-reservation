package com.bendaniel10.reservations.network

import okhttp3.Interceptor

interface WebServiceBuilder {

    fun clearInterceptors(): WebServiceBuilder

    fun addInterceptor(interceptor: Interceptor): WebServiceBuilder

    fun addInterceptors(vararg interceptorArray: Interceptor): WebServiceBuilder

    fun withEndpoint(endpoint: String): WebServiceBuilder

    fun withPort(port: String?): WebServiceBuilder

    fun build(clazz: Class<*>, logNetworkRequests: Boolean): Any

}