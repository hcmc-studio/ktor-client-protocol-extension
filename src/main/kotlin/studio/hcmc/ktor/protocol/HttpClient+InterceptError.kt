package studio.hcmc.ktor.protocol

import io.ktor.client.call.*
import io.ktor.client.statement.*
import studio.hcmc.kotlin.protocol.io.Response

suspend inline fun <reified T> HttpResponse.interceptError(): T {
    try {
        return body<T>()
    } catch (e: Throwable) {
        val errorResponse: Response.Error
        try {
            errorResponse = body<Response.Error>()
        } catch (_: Throwable) {
            throw e
        }

        throw errorResponse.result
    }
}