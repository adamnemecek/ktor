package org.jetbrains.ktor.servlet

import org.jetbrains.ktor.host.*
import org.jetbrains.ktor.http.*
import org.jetbrains.ktor.response.*
import javax.servlet.http.*

class ServletApplicationResponse(call: ServletApplicationCall,
                                 val servletResponse: HttpServletResponse,
                                 private val pushImpl: (ResponsePushBuilder) -> Boolean
) : BaseApplicationResponse(call) {
    override fun setStatus(statusCode: HttpStatusCode) {
        servletResponse.status = statusCode.value
    }

    override val headers: ResponseHeaders = object : ResponseHeaders() {
        override fun hostAppendHeader(name: String, value: String) {
            servletResponse.addHeader(name, value)
        }

        override fun getHostHeaderNames(): List<String> = servletResponse.headerNames.toList()
        override fun getHostHeaderValues(name: String): List<String> = servletResponse.getHeaders(name).toList()
    }

    override fun push(builder: ResponsePushBuilder) {
        if (!pushImpl(builder)) {
            super.push(builder)
        }
    }
}
