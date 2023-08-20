package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::main)
        .start(wait = true)
}

fun Application.main() {

    //koin must be called first
    configureKoin()

    install(CallLogging){
        level = Level.TRACE
        format {call->
            val status = call.response.status()
            val method = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            val path = call.request.path()
            "Method : $method, status : $status, userAgent : $userAgent, path : $path"
        }
    }


    log.info("inside application.module()")
    configureSerialization()
    configureRouting()
}
