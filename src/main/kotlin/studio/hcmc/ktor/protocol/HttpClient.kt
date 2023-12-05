package studio.hcmc.ktor.protocol

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import studio.hcmc.kotlin.protocol.io.Response

suspend inline fun <reified T> HttpClient.getObject(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    return get(urlString, block).body<Response.Object<T>>().result
}

suspend inline fun <reified T> HttpClient.getArray(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): List<T> {
    return get(urlString, block).body<Response.Array<T>>().result
}

suspend inline fun <reified T> HttpClient.postObject(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    return post(urlString, block).body<Response.Object<T>>().result
}

suspend inline fun <reified T> HttpClient.putObject(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    return put(urlString, block).body<Response.Object<T>>().result
}

suspend inline fun <reified T> HttpClient.patchObject(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    return patch(urlString, block).body<Response.Object<T>>().result
}