package com.example.plugins

import com.example.routes.customerRoute
import com.example.routes.orderRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {

        get("/"){
            call.respond(HttpStatusCode.OK, "Hello World")
        }
        customerRoute()
        orderRoutes()
    }
}
