package studio.hcmc.ktor.protocol

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import studio.hcmc.kotlin.protocol.DataTransferObject

@KtorDsl
suspend inline fun HttpClient.get(url: String, vararg parameters: Pair<String, Any?>, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse {
    return get(urlString = url) {
        for ((name, value) in parameters) {
            if (value is Iterable<*>) {
                val values = value.map { if (it is String) it else it.toString() }
                this.url.parameters.appendAll(name, values)
            } else if (value is String) {
                this.url.parameters.append(name, value)
            } else if (value != null) {
                this.url.parameters.append(name, value.toString())
            }
        }

        accept(ContentType.Application.Json)
        block()
    }
}

@KtorDsl
suspend inline fun <reified T : DataTransferObject> HttpClient.post(url: String, dto: T, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse {
    return post(urlString = url) {
        setBody(dto)
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
        block()
    }
}

@KtorDsl
suspend inline fun <reified T : DataTransferObject> HttpClient.put(url: String, dto: T, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse {
    return put(urlString = url) {
        setBody(dto)
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
        block()
    }
}

@KtorDsl
suspend inline fun <reified T : DataTransferObject> HttpClient.patch(url: String, dto: T, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse {
    return patch(urlString = url) {
        setBody(dto)
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
        block()
    }
}