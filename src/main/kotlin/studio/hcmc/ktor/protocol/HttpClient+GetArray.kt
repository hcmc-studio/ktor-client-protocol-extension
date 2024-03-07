package studio.hcmc.ktor.protocol

import io.ktor.client.*
import io.ktor.client.request.*
import studio.hcmc.kotlin.protocol.io.Response

suspend inline fun <reified T> HttpClient.getArray(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): List<T> {
    return get(urlString, block)
        .interceptError<Response.Array<T>>()
        .result
}