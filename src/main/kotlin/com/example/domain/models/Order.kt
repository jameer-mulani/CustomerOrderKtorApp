package com.example.domain.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Order(
    val id : String = UUID.randomUUID().toString(),
    val orderName : String = Date().toString(),
    var customerId : Int = 0,
    val contents : List<OrderItem>
)

@Serializable
data class OrderItem(
    val name : String,
    val amount : Int,
    val price : Double
)