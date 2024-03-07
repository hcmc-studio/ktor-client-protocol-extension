package studio.hcmc.ktor.protocol

import io.ktor.client.*
import io.ktor.client.request.*
import studio.hcmc.kotlin.protocol.io.Response

suspend inline fun <reified T> HttpClient.putObject(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    return put(urlString, block)
        .interceptError<Response.Object<T>>()
        .result
}