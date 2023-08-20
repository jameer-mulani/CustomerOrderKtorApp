package com.example.routes

import com.example.domain.models.Order
import com.example.repository.OrderInMemRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.orderRoutes(){

    val orderRepository by inject<OrderInMemRepository>()

    route("/orders"){

        get {
            call.respond(HttpStatusCode.OK, orderRepository.getAllOrders())
        }

        get("{id?}"){
            val pathId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing orderId")
            val order = orderRepository.getOrderById(pathId)
            return@get order?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respond(HttpStatusCode.NotFound, "No order exist")
        }

        get("{id?}/total"){
            val pathId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing id")
            try {
                val total = orderRepository.getOrderTotal(pathId)
                return@get call.respond(HttpStatusCode.OK, total)

            }catch (e : Exception){
                return@get call.respond(HttpStatusCode.InternalServerError)
            }
        }

        post {
            try {
                val order = call.receive<Order>()
                val result = orderRepository.addOrder(order)
                return@post if (result)
                    call.respond(HttpStatusCode.OK, order)
                else
                    call.respond(HttpStatusCode.InternalServerError)
            }catch (e : Exception){
                return@post call.respond(HttpStatusCode.BadRequest,)
            }
        }

        delete("{id?}"){
            val pathId = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing order id")
            try {
                val order = orderRepository.getOrderById(pathId)
                if (order == null)
                    call.respond(HttpStatusCode.NotFound, "No order exist")
                val result = orderRepository.removeOrder(order!!)
                return@delete if (result)
                    call.respond(HttpStatusCode.OK, order)
                else
                    call.respond(HttpStatusCode.BadRequest)
            }catch (e : Exception){
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

    }
}