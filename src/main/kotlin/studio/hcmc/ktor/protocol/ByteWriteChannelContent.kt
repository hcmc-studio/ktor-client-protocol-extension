package studio.hcmc.ktor.protocol

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*

class ByteWriteChannelContent(
    private val bytes: ByteArray,
    override val contentLength: Long? = bytes.size.toLong(),
    override val contentType: ContentType? = ContentType.Application.OctetStream
) : OutgoingContent.WriteChannelContent() {
    override suspend fun writeTo(channel: ByteWriteChannel) {
        channel.writeFully(bytes)
    }
}