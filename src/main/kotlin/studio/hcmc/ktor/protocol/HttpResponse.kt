package studio.hcmc.ktor.protocol

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*
import studio.hcmc.kotlin.protocol.ValueObject
import studio.hcmc.kotlin.protocol.io.Response

object HttpResponseUtil {
    var fallbackHandler: ((errorResponse: Response.Error) -> Throwable?)? = null
}

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
    if (status.isSuccess()) {
        return Result.success(body())
    } else {
        val errorResponse = try { body<Response.Error>() } catch (e: Throwable) { return Result.failure(e) }
        val fallbackHandler = HttpResponseUtil.fallbackHandler ?: return Result.failure(IOException("Uncaught error: $errorResponse"))
        val throwable = fallbackHandler(errorResponse) ?: return Result.failure(IOException("Unhandled error: $errorResponse"))

        return Result.failure(throwable)
    }
}