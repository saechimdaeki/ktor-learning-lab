package com.example.route

import com.example.config.AuthenticatedUser
import com.example.config.authenticatedUser
import com.example.service.MenuService
import com.example.service.OrderService
import com.example.shared.dto.OrderDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.orderRoute() {

    val menuService by inject<MenuService>()
    val orderService by inject<OrderService>()
    authenticate(AuthenticatedUser.CUSTOMER_REQUIRED) {
        post("/orders") {
            val request = call.receive<OrderDto.CreateRequest>()

            val orderCode: String = orderService.createOrder(request, call.authenticatedUser())

            call.respond(orderCode)
        }
        get("/orders/{orderCode}") {
            val orderCode = call.parameters["orderCode"]!!
            val order : OrderDto.DisplayResponse = orderService.getOrder(orderCode, call.authenticatedUser())
            call.respond(order)
        }

        put("/orders/{orderCode}/status") {
            val orderCode = call.parameters["orderCode"]!!
            val status = call.receive<OrderDto.UpdateStatusRequest>().status
            orderService.updateOrderStatus(orderCode, status, call.authenticatedUser())
            call.respond(HttpStatusCode.OK)
        }
    }
}