package com.example.repository

import com.example.domain.models.Order

class OrderInMemRepository {

    companion object{
        private val orderStorage = mutableListOf<Order>()
    }

    fun getAllOrders(customerId : Int) : List<Order>{
        return orderStorage.filter { order ->
            order.customerId == customerId
        }
    }

    fun addOrder(vararg order: Order) : Boolean{
        return orderStorage.addAll(order)
    }

    fun removeOrder(order: Order) : Boolean{
        return orderStorage.remove(order)
    }

    fun getOrderById(orderId : String) : Order?{
        return orderStorage.find { order->
            order.id == orderId
        }
    }

    fun getOrderTotal(orderId : String) : Double{
        var total = 0.0
        val order = getOrderById(orderId) ?: return total
        return order.contents.sumOf {
            it.amount * it.price
        }
    }


    fun deleteOrder(orderId : String) : Boolean{
        return orderStorage.removeIf {
            it.id == orderId
        }
    }



}