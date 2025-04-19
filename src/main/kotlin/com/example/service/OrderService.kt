package com.example.service

import com.example.config.AuthenticatedUser
import com.example.domain.model.CafeOrder
import com.example.domain.repository.CafeOrderRepository
import com.example.shared.CafeException
import com.example.shared.CafeOrderStatus
import com.example.shared.CafeUserRole
import com.example.shared.ErrorCode
import com.example.shared.dto.OrderDto
import java.time.LocalDateTime
import java.util.*

class OrderService(
    private val menuService: MenuService,
    private val userService: UserService,
    private val cafeOrderRepository: CafeOrderRepository
) {
    fun createOrder(request: OrderDto.CreateRequest, authenticatedUser: AuthenticatedUser): String {
        val menu = menuService.getMenu(request.menuId)
        val order = CafeOrder(
            orderCode = "OC${UUID.randomUUID()}",
            cafeMenuId = menu.id!!,
            cafeUserId = authenticatedUser.userId,
            price = menu.price,
            status = CafeOrderStatus.CANCEL,
            orderedAt = LocalDateTime.now(),
        )

        cafeOrderRepository.create(order)
        return order.orderCode
    }

    fun getOrder(orderCode: String, authenticatedUser: AuthenticatedUser): OrderDto.DisplayResponse {
        val order = getOrderByCode(orderCode)
        checkOrderOwner(order, authenticatedUser)

        val menu = menuService.getMenu(order.cafeMenuId)
        val user = userService.getUser(order.cafeUserId)
        return OrderDto.DisplayResponse(
            orderCode = order.orderCode,
            menuName = menu.name,
            customerName = user.nickname,
            price = order.price,
            status = order.status,
            orderedAt = order.orderedAt,
            id = null // 명시적 Null

        )
    }

    private fun getOrderByCode(orderCode: String): CafeOrder {
        val order = cafeOrderRepository.findByCode(orderCode)
            ?: throw CafeException(ErrorCode.ORDER_NOT_FOUND)
        return order
    }

    fun updateOrderStatus(orderCode: String, status: CafeOrderStatus, authenticatedUser: AuthenticatedUser) {
        val order = getOrderByCode(orderCode)

        checkOrderOwner(order, authenticatedUser)

        checkCustomerAction(authenticatedUser, status)

        order.update(status)
        cafeOrderRepository.update(order)
    }

    private fun checkCustomerAction(
        authenticatedUser: AuthenticatedUser,
        status: CafeOrderStatus
    ) {
        if (authenticatedUser.userRoles == listOf(CafeUserRole.CUSTOMER)) {
            if (status != CafeOrderStatus.CANCEL) {
                throw CafeException(ErrorCode.FORBIDDEN)
            }
        }
    }

    private fun checkOrderOwner(
        order: CafeOrder,
        authenticatedUser: AuthenticatedUser
    ) {
        if (order.cafeUserId != authenticatedUser.userId) {
            throw CafeException(ErrorCode.FORBIDDEN)
        }
    }
}