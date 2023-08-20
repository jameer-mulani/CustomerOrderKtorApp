package com.example.routes

import com.example.di.customerRepositoryModule
import com.example.domain.models.Order
import com.example.repository.OrderInMemRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.orderRoutes(){


    //GET /orders/{customerId}/{orderId}/total

    val orderRepository by inject<OrderInMemRepository>()

//    route("/orders/total/{param...}"){
//        get {
//            val params = call.parameters.getAll("param")
//            if (params.isNullOrEmpty()){
//                return@get call.respond(HttpStatusCode.BadRequest, "Invalid parameters, please check Api Doc")
//            }
//            if (params!!.size != 2){
//                return@get call.respond(HttpStatusCode.BadRequest, "2 params must pass")
//            }
//            val customerId = params[0]
//            val orderId = params[1]
//            return@get call.respond("customer id : $customerId, order id : $orderId")
//        }
//    }


    route("/orders/{customerId}"){

        get {
            val customerId = call.parameters["customerId"] ?: call.respond(HttpStatusCode.BadRequest, "Missing customer id")
           return@get try {
               val id = (customerId as String).toInt()
               val results = orderRepository.getAllOrders(id)
               call.respond(HttpStatusCode.OK, results)
            }catch (e : Exception){
                call.respond(HttpStatusCode.BadRequest, "bad customer id")
            }

        }

        get("{id?}"){
            val pathId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing orderId")
            val order = orderRepository.getOrderById(pathId)
            return@get order?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respond(HttpStatusCode.NotFound, "No order exist")
        }

        post {
            try {
                val customerId = call.parameters["customerId"] ?: call.respond(HttpStatusCode.BadRequest, "Missing customer id")
                val order = call.receive<Order>()
                order.customerId = (customerId as String).toInt()
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

        orderUtilRoutes()

    }
}