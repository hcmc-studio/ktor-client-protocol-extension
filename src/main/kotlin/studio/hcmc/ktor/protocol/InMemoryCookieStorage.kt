package studio.hcmc.ktor.protocol

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
class InMemoryCookieStorage : HashMap<Url, ArrayList<Cookie>> {
    constructor(): super()

    constructor(initialCapacity: Int, loadFactor: Float): super(initialCapacity, loadFactor)

    constructor(initialCapacity: Int): super(initialCapacity)

    constructor(m: Map<out Url, ArrayList<Cookie>>): super(m)
}