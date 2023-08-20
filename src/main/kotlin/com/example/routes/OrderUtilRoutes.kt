package com.example.routes

import com.example.repository.OrderInMemRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

//must be nest inside the /order routes
fun Route.orderUtilRoutes(){

    val orderRepository by inject<OrderInMemRepository>()

    get("{id?}/total"){
            val pathId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing id")
            try {
                val total = orderRepository.getOrderTotal(pathId)
                return@get call.respond(HttpStatusCode.OK, total)

            }catch (e : Exception){
                return@get call.respond(HttpStatusCode.InternalServerError)
            }
    }
}