package studio.hcmc.ktor.protocol

import io.ktor.client.*
import io.ktor.client.request.*
import studio.hcmc.kotlin.protocol.io.Response

suspend inline fun <reified T> HttpClient.postObject(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    return post(urlString, block)
        .interceptError<Response.Object<T>>()
        .result
}