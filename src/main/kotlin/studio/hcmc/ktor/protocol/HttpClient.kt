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
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import studio.hcmc.kotlin.crypto.RSA
import studio.hcmc.kotlin.crypto.encrypt
import studio.hcmc.kotlin.protocol.io.DataTransferObject
import studio.hcmc.kotlin.protocol.io.EncryptedDataTransferObject
import java.net.URL as JavaUrl

private val defaultJsonKey = AttributeKey<Json>("defaultJson")

val HttpClient.defaultJson: Json get() = attributes[defaultJsonKey]

fun HttpClient(
    url: JavaUrl,
    jsonSupplier: () -> Json = { Json },
    clientConfig: HttpClientConfig<CIOEngineConfig>.() -> Unit = {}
): HttpClient {
    val json = jsonSupplier()
    val client = HttpClient(CIO) {
        defaultRequest {
            url(url.toString())
        }

        install(ContentNegotiation) {
            json(json)
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        clientConfig()
    }

    client.attributes.put(defaultJsonKey, json)

    return client
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

inline fun <reified T : DataTransferObject> HttpClient.encryptDto(
    dto: T,
    publicKey: String,
    keySize: Int
): EncryptedDataTransferObject {
    val jsonString = defaultJson.encodeToString(dto)
    val encryptedString = RSA.encrypt(jsonString, publicKey, keySize)

    return EncryptedDataTransferObject(publicKey, encryptedString)
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

suspend inline fun <reified T : DataTransferObject> HttpClient.post(
    urlString: String,
    dto: T,
    publicKey: String,
    keySize: Int,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    return post(urlString, encryptDto(dto, publicKey, keySize), block)
}

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

suspend inline fun <reified T : DataTransferObject> HttpClient.put(
    urlString: String,
    dto: T,
    publicKey: String,
    keySize: Int,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    return put(urlString, encryptDto(dto, publicKey, keySize), block)
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

suspend inline fun <reified T : DataTransferObject> HttpClient.patch(
    urlString: String,
    dto: T,
    publicKey: String,
    keySize: Int,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    return patch(urlString, encryptDto(dto, publicKey, keySize), block)
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