package studio.hcmc.ktor.protocol

import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable
import java.util.LinkedList

@Serializable
class InMemoryCookieStorage : HashMap<Url, MutableList<Cookie>> {
    constructor(): super()

    constructor(initialCapacity: Int, loadFactor: Float): super(initialCapacity, loadFactor)

    constructor(initialCapacity: Int): super(initialCapacity)

    constructor(m: Map<out Url, ArrayList<Cookie>>): super(m)
}