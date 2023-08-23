package studio.hcmc.ktor.protocol

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*
import studio.hcmc.kotlin.protocol.io.Response

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
        val errorResponse = runCatching { body<Response.Error>() }.getOrElse {
            return Result.failure(IOException("Response is not instance of Response.Error", it))
        }
        val errorClass = runCatching { Class.forName(errorResponse.className) }.getOrElse {
            val tokens = errorResponse.className.split(".")
            val nestedClassName = tokens.subList(0, tokens.size - 1).joinToString(".") + "$" + tokens.last()
            runCatching { Class.forName(nestedClassName) }.getOrElse {
                return Result.failure(IOException("Exception class is not found for name: [${errorResponse.className}, $nestedClassName]"))
            }
        }
        val objectInstance = errorClass.kotlin.objectInstance
        if (objectInstance != null) {
            val throwable = objectInstance as? Throwable
                ?: return Result.failure(IOException("Companion INSTANCE is not throwable: class=$errorClass, response=$errorResponse"))

            return Result.failure(throwable)
        }

        val constructor = errorClass.kotlin.constructors.find { it.parameters.isEmpty() } ?:
            return Result.failure(IOException("Cannot find empty constructor: class=$errorClass, response=$errorResponse"))
        val instance = runCatching { constructor.call() }.getOrElse {
            return Result.failure(IOException("Cannot call constructor: class=$errorClass, response=$errorResponse", it))
        }
        val throwable = instance as? Throwable
            ?: return Result.failure(IOException("Created instance is not throwable: class=$errorClass, response=$errorResponse"))

        return Result.failure(throwable)

    }
}