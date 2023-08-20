package com.example.repository

import com.example.domain.models.Customer

class CustomerInMemRepository {
    companion object{
        private val customerStorage = mutableListOf<Customer>()
    }

   suspend fun addCustomer(vararg customer : Customer) : Boolean{
        return customerStorage.addAll(customer)
    }

    suspend fun removeCustomer(customer: Customer) : Boolean{
        return customerStorage.remove(customer)
    }

    suspend fun updateCustomer(vararg customer: Customer){
        val existedCustomers = mutableListOf<Customer>()
        for (c in customer){
            if (customerStorage.contains(c)){
                existedCustomers.add(c)
            }
        }
        customerStorage.removeAll(existedCustomers)
        customerStorage.addAll(customer)
    }

    suspend fun getAllCustomers() : List<Customer>{
        return customerStorage
    }

    fun getCustomerById(id : Int) : Customer?{
        return customerStorage.find {customer ->
            customer.id == id
        }
    }


}