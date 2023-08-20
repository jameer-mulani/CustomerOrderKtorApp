package com.example.domain.models

import kotlinx.serialization.Serializable

private var CustomerIdCounter = 0

@Serializable
data class Customer(
    val id : Int = CustomerIdCounter++,
    val firstName : String,
    val lastName : String? = null,
    val email : String
)
