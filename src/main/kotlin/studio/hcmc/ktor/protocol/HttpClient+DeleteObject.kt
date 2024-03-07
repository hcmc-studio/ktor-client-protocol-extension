package studio.hcmc.ktor.protocol

import io.ktor.client.*
import io.ktor.client.request.*
import studio.hcmc.kotlin.protocol.io.Response

suspend inline fun HttpClient.deleteObject(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Response.Empty {
    return delete(urlString, block)
        .interceptError()
}