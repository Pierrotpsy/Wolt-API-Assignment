package com.wolt.assignment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*
import kotlin.math.ceil


//Easily modifiable constants to modify the behavior of the fee calculator and for clarity.
object Constants {
    const val MINIMAL_CART_VALUE_THRESHOLD = 1000
    const val STANDARD_DELIVERY_DISTANCE = 1000
    const val STANDARD_DELIVERY_DISTANCE_FEE = 200
    const val ADDITIONAL_DELIVERY_DISTANCE = 500
    const val ADDITIONAL_DELIVERY_DISTANCE_FEE = 100
    const val STANDARD_ITEM_NUMBER = 4
    const val ADDITIONAL_ITEM_FEE = 50
    const val BULK_AMOUNT = 12
    const val BULK_FEE = 120
    const val MAXIMAL_DELIVERY_FEE = 1500
    const val FREE_DELIVERY_THRESHOLD = 10000
    const val FREE_DELIVERY = 0
    const val RUSH_DAY = Calendar.FRIDAY
    const val RUSH_START = 15
    const val RUSH_END = 21
    const val RUSH_DELIVERY_MULTIPLIER = 1.2
    const val TIME_ZONE = "Europe/Paris"
}

@SpringBootApplication
class BackendAssignmentWoltApplication

fun main(args: Array<String>) {
    runApplication<BackendAssignmentWoltApplication>(*args)
}

//Controller for our API
@RestController
class Controller {

    @PostMapping("/")
    fun showDeliveryFee(@RequestBody information: OrderInformation): String {
        return """{"delivery_fee": ${calculateDeliveryFee(information)}}"""
    }
}

// Calculator function, which integrates all the conditions described
fun calculateDeliveryFee(information: OrderInformation): Int {
    var deliveryFee = 0

    // Condition 1: check if cart value is below a certain threshold
    if (information.cart_value < Constants.MINIMAL_CART_VALUE_THRESHOLD) {
        deliveryFee += Constants.MINIMAL_CART_VALUE_THRESHOLD - information.cart_value
    }

    // Condition 2: check the delivery distance
    deliveryFee += if (information.delivery_distance < Constants.STANDARD_DELIVERY_DISTANCE) {
        Constants.STANDARD_DELIVERY_DISTANCE_FEE
    } else {
        Constants.STANDARD_DELIVERY_DISTANCE_FEE + ((ceil((information.delivery_distance * 1.0 - Constants.STANDARD_DELIVERY_DISTANCE) / Constants.ADDITIONAL_DELIVERY_DISTANCE)) * Constants.ADDITIONAL_DELIVERY_DISTANCE_FEE).toInt()
    }

    // Condition 3: check the number of items
    if (information.number_of_items > Constants.STANDARD_ITEM_NUMBER) {
        deliveryFee += (information.number_of_items - Constants.STANDARD_ITEM_NUMBER) * Constants.ADDITIONAL_ITEM_FEE
    }
    if (information.number_of_items > Constants.BULK_AMOUNT) {
        deliveryFee += Constants.BULK_FEE
    }

    //Condition 4: check the day and hour to adapt fee
    val cal = Calendar.getInstance()
    cal.timeZone = TimeZone.getTimeZone(Constants.TIME_ZONE)
    cal.time = information.time
    val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

    if (dayOfWeek == Constants.RUSH_DAY && cal.get(Calendar.HOUR_OF_DAY) >= Constants.RUSH_START && cal.get(Calendar.HOUR_OF_DAY) <= Constants.RUSH_END) {
        deliveryFee = (deliveryFee * Constants.RUSH_DELIVERY_MULTIPLIER).toInt()
    }

    //Condition 5: check if cart is eligible for free delivery
    if (information.cart_value >= Constants.FREE_DELIVERY_THRESHOLD) deliveryFee = Constants.FREE_DELIVERY

    //Condition 6: check that fee is not above the maximum amount
    if (deliveryFee > Constants.MAXIMAL_DELIVERY_FEE) deliveryFee = Constants.MAXIMAL_DELIVERY_FEE

    return deliveryFee
}

//Object used to represent the input JSON information.
data class OrderInformation(val cart_value: Int, val delivery_distance: Int, val number_of_items: Int, val time: Date)
