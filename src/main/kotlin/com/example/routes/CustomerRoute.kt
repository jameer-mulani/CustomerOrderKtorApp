package com.example.routes

import com.example.domain.models.Customer
import com.example.repository.CustomerInMemRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.customerRoute() {

    val customerRepo: CustomerInMemRepository by inject<CustomerInMemRepository>()

    route("/customers") {
        get {
            if (customerRepo.getAllCustomers().isEmpty()) {
                call.respond(status = HttpStatusCode.OK, emptyList<Customer>())
            } else {
                call.respond(status = HttpStatusCode.OK, customerRepo.getAllCustomers())
            }
        }

        get("{id?}") {
            val pathId =
                call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing Customer Id")
            val id: Int
            return@get try {
                id = pathId.toInt()
                val customer = customerRepo.getCustomerById(id)
                customer?.let {
                    call.respond(HttpStatusCode.OK, it)
                } ?: call.respond(HttpStatusCode.NotFound, "No customer exist for id : $id")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Bad customer id provided : $pathId")
            }
        }

        post {
            try {
                val customer = call.receive<Customer>()
                val result = customerRepo.addCustomer(customer)
                return@post if (result) call.respond(HttpStatusCode.Created, customer)
                else call.respond(HttpStatusCode.Conflict, "Failed to create a customer resource")

            } catch (e: Exception) {
                return@post call.respond(HttpStatusCode.InternalServerError, "Failed to serve request")
            }
        }

        delete("{id?}") {
            val pathId = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing id")
            try {
                val id = pathId.toInt()
                val customer = customerRepo.getCustomerById(id)
                customer?.run {
                    val result = customerRepo.removeCustomer(this)
                    return@delete if (result)
                        call.respond(HttpStatusCode.OK, "Deleted customer for Id : $id")
                    else
                        call.respond(HttpStatusCode.Conflict, "Failed to delete customer for id : $id")
                } ?: return@delete call.respond(HttpStatusCode.NotFound, "No customer exist for id : $id")
            } catch (e: Exception) {
                return@delete call.respond(HttpStatusCode.BadRequest, "Bad id provided : $pathId")
            }
        }
    }
}