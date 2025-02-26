package com.emptyshop.kotlinrestapiclient

// Modelo de datos para la respuesta de la API en los métodos PUT y DELETE
data class APIResponse(
    var success: String?,
    var error: String?
)
