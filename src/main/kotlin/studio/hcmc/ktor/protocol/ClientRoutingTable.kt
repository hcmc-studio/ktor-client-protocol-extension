package studio.hcmc.ktor.protocol

import io.ktor.client.*
import studio.hcmc.ktor.routing.RoutingTable

interface ClientRoutingTable : RoutingTable {
    val httpClient: HttpClient
}