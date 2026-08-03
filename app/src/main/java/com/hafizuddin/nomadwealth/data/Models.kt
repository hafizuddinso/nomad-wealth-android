package com.hafizuddin.nomadwealth.data

data class Account(
    val id: Long,
    val name: String,
    val currency: String,
    val balance: Double
)

data class Transaction(
    val id: Long,
    val title: String,
    val category: String,
    val amount: Double,
    val income: Boolean,
    val date: String
)

data class Loan(
    val id: Long,
    val name: String,
    val principal: Double,
    val remaining: Double,
    val monthlyPayment: Double,
    val currency: String
)

data class UserSession(
    val name: String,
    val email: String,
    val accessToken: String
)
