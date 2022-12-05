package io.gorillas.touringhelper.model

import java.time.Instant

data class FrichtiOrder(
    val orderId: String,
    val tourSequenceNumber: Int,
    val sequence: String?,
    val createdOn: Instant,
    val address: Address,
    val customer: Customer,
    val dailySequence: Int,
    val orderItems: List<OrderItem>,
    val payment: Payment,
    val promiseDate: Instant,
    val state: String,
    val storeId: String,
    val tags: List<String>?,

    // add for the Apps
    val mainRiderContact: Contact,
    val bags: List<String>?,
    val tip: Double?,
    val requiresLegalCheck: Boolean? = false,
    val additionalInformation: String?,
    val totalWeight: TotalWeight,
    val currencyISO: String
)

data class GeoLocation(
    val latitude: Double,
    val longitude: Double
)

data class Address(
    val geoLocation: GeoLocation,
    val street: String,
    val city: String?,
    val countryISO: String,
    val note: String?,
    val alias: String?
)

data class Customer(
    val id: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val totalOrdersCount: Int?
)

data class Payment(
    val amount: Double,
    val currencyIso: String,
    val promoCode: String?
)

data class OrderItem(
    val quantity: Int,
    val product: Product
)

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val grossWeight: Double,
    val image: String? // new
)

data class Contact(
    val fulName: String?,
    val phoneNumber: String
)

data class OrderUpdate(
    val orderId: String,
    val state: FrichtiOrderState
)

data class TotalWeight(
    val unit: String,
    val value: Double
)

enum class FrichtiOrderState {
    CREATED,
    CANCELLED,
    READY,
    PREPARING,
    TOURING,
    DONE,
    FAILED,
    OUT_OF_STOCK,
}

enum class Tag(val value: String) {
    FIRST_ORDER("FIRSTORDER"),
    FROZEN("FROZEN"),
    CANTINE("CANTINE"),
    DELIVEROO("DELIVEROO"),
}
