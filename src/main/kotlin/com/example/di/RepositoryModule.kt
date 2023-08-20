package com.example.di

import com.example.repository.CustomerInMemRepository
import com.example.repository.OrderInMemRepository
import org.koin.dsl.module

val customerRepositoryModule = module {
    single<CustomerInMemRepository> {
        CustomerInMemRepository()
    }

    single {
        OrderInMemRepository()
    }
}