package studio.hcmc.ktor.protocol

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import studio.hcmc.kotlin.crypto.RSA
import studio.hcmc.kotlin.crypto.encrypt
import studio.hcmc.kotlin.protocol.DataTransferObject
import java.net.URL as JavaUrl

fun HttpClient(
    url: JavaUrl,
    json: Json = Json,
    defaultRequestConfig: DefaultRequest.DefaultRequestBuilder.() -> Unit = { this.url(url.toString()) },
    contentNegotiationConfig: ContentNegotiation.Config.() -> Unit = { json(json) },
    loggingConfig: Logging.Config.() -> Unit = { logger = Logger.DEFAULT; level = LogLevel.ALL },
    clientConfig: HttpClientConfig<CIOEngineConfig>.() -> Unit = {}
): HttpClient {
    return HttpClient(CIO) {
        defaultRequest {
            defaultRequestConfig()
        }

        install(ContentNegotiation) {
            contentNegotiationConfig()
        }

        install(Logging) {
            loggingConfig()
        }

        clientConfig()
    }
}

fun HttpRequestBuilder.applyParameters(vararg parameters: Pair<String, Any?>) {
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
}

suspend inline fun HttpClient.get(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    return get {
        url(urlString)
        accept(ContentType.Application.Json)
        block()
    }
}

suspend inline fun HttpClient.get(
    urlString: String,
    vararg parameters: Pair<String, Any?>,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    return get {
        url(urlString)
        applyParameters(*parameters)
        accept(ContentType.Application.Json)
        block()
    }
}

suspend inline fun HttpClient.post(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    return post {
        url(urlString)
        accept(ContentType.Application.Json)
        block()
    }
}

suspend inline fun <reified T : DataTransferObject> HttpClient.post(
    urlString: String,
    dto: T,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    return post {
        url(urlString)
        setBody(dto)
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
        block()
    }
}

//suspend inline fun <reified T : DataTransferObject> HttpClient.post(
//    urlString: String,
//    dto: T,
//    publicKey: String,
//    keySize: Int,
//    block: HttpRequestBuilder.() -> Unit = {}
//): HttpResponse {
//    val encryptedBody = RSA.encrypt(Json.encodeToString(dto), publicKey, keySize)
//    val encryptedDTO =
//    post(urlString, encryptedDTO, block)
//
//}

suspend inline fun HttpClient.put(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    return put {
        url(urlString)
        accept(ContentType.Application.Json)
        block()
    }
}

suspend inline fun <reified T : DataTransferObject> HttpClient.put(
    urlString: String,
    dto: T,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    return put {
        url(urlString)
        setBody(dto)
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
        block()
    }
}

suspend inline fun HttpClient.patch(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    return patch {
        url(urlString)
        accept(ContentType.Application.Json)
        block()
    }
}

suspend inline fun <reified T : DataTransferObject> HttpClient.patch(
    urlString: String,
    dto: T,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    return patch {
        url(urlString)
        setBody(dto)
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
        block()
    }
}

suspend inline fun HttpClient.delete(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    return delete {
        url(urlString)
        accept(ContentType.Application.Json)
        block()
    }
}

suspend inline fun HttpClient.delete(
    urlString: String,
    vararg parameters: Pair<String, Any?>,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    return delete {
        url(urlString)
        applyParameters(*parameters)
        accept(ContentType.Application.Json)
        block()
    }
}

suspend inline fun HttpClient.head(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    return head {
        url(urlString)
        accept(ContentType.Application.Json)
        block()
    }
}

suspend inline fun HttpClient.head(
    urlString: String,
    vararg parameters: Pair<String, Any?>,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    return head {
        url(urlString)
        applyParameters(*parameters)
        accept(ContentType.Application.Json)
        block()
    }
}