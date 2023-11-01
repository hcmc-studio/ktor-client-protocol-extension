package studio.hcmc.ktor.protocol

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*
import studio.hcmc.kotlin.protocol.io.Response

suspend inline fun HttpResponse.bodyAsEmpty(): Result<Response.Empty> {
    return bodyAsResponse()
}

suspend inline fun <reified R> HttpResponse.bodyAsObject(): Result<Response.Object<R>> {
    return bodyAsResponse()
}

suspend inline fun <reified R> HttpResponse.bodyAsArray(): Result<Response.Array<R>> {
    return bodyAsResponse()
}

suspend inline fun <reified R : Response<*>> HttpResponse.bodyAsResponse(): Result<R> {
    return if (status.isSuccess()) {
        Result.success(body())
    } else {
        try {
            Result.failure(body<Response.Error>().result)
        } catch (e: Throwable) {
            Result.failure(IOException("Response is not instance of Response.Error", e))
        }
    }
}