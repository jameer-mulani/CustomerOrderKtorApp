package routes

import com.example.plugins.configureRouting
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.math.log
import kotlin.test.Test


class CustomerRoutes{
    @Test
    fun testHomeRoute() = testApplication {

        application {
            configureRouting()
        }

        val response = client.get("/")
        println("bodyAsText : "+response.status)
        assert(response.status == HttpStatusCode.OK)
    }
}