package studio.hcmc.ktor.protocol

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import studio.hcmc.kotlin.protocol.io.Response

suspend inline fun <reified T> HttpClient.getObject(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    return get(urlString, block).interceptError<Response.Object<T>>().result
}

suspend inline fun <reified T> HttpClient.getArray(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): List<T> {
    return get(urlString, block).interceptError<Response.Array<T>>().result
}

suspend inline fun <reified T> HttpClient.postObject(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    return post(urlString, block).interceptError<Response.Object<T>>().result
}

suspend inline fun <reified T> HttpClient.putObject(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    return put(urlString, block).interceptError<Response.Object<T>>().result
}

suspend inline fun <reified T> HttpClient.patchObject(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    return patch(urlString, block).interceptError<Response.Object<T>>().result
}

suspend inline fun HttpClient.deleteObject(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Response.Empty {
    return delete(urlString, block).interceptError()
}

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