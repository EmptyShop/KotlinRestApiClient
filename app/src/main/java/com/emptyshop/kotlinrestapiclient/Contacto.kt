package com.emptyshop.kotlinrestapiclient

// El modelo de datos de la app
data class Contacto(
    var id: Int,
    var fullname: String,
    var email: String,
    var phone: String
)
