package io.gorillas.touringhelper.service

import io.gorillas.touringhelper.model.Address
import io.gorillas.touringhelper.model.Contact
import io.gorillas.touringhelper.model.Customer
import io.gorillas.touringhelper.model.FrichtiOrder
import io.gorillas.touringhelper.model.FrichtiTour
import io.gorillas.touringhelper.model.FrichtiTourState
import io.gorillas.touringhelper.model.GeoLocation
import io.gorillas.touringhelper.model.OrderItem
import io.gorillas.touringhelper.model.Payment
import io.gorillas.touringhelper.model.Product
import io.gorillas.touringhelper.model.Tag
import io.gorillas.touringhelper.model.TotalWeight
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import kotlin.random.Random

@Service
class TourService(
    private val rabbitTemplate: RabbitTemplate,
    @Value("\${gorillas.tour.exchangeName}")
    private val exchangeName: String
) {

    suspend fun createFrichtiTourData() {
        val tour = createTour()
        rabbitTemplate.convertAndSend(
            exchangeName,
            FrichtiRoutingKey.TOUR_CREATED.routingKey,
            tour
        )
    }

    suspend fun updateFrichtiTourData() {
        rabbitTemplate.convertAndSend(
            exchangeName,
            FrichtiRoutingKey.TOUR_UPDATED.routingKey,
            createOrder()
        )
    }

    private fun createTour(): FrichtiTour {
        val tourId = UUID.randomUUID().toString()
        ValueStore.lastTourId = tourId

        val riderId = UUID.randomUUID().toString()
        ValueStore.lastRiderId = riderId

        return FrichtiTour(
            tourId = tourId,
            riderId = riderId,
            vehicle = "rocket",
            additionalRiders = null,
            orders = listOf(createOrder(), createOrder()),
            tourState = FrichtiTourState.CREATED
        )
    }

    private fun createOrder(): FrichtiOrder {
        return FrichtiOrder(
            orderId = UUID.randomUUID().toString(),
            tourSequenceNumber = Random.nextInt(0, 30),
            createdOn = Instant.now(),
            sequence = "sequence",
            address = Address(
                geoLocation = GeoLocation(
                    latitude = -22.9193482,
                    longitude = -43.1731624
                ),
                city = "Berlin",
                street = "Pariser Platz, 10117 Berlin",
                countryISO = "DE",
                note = "note",
                alias = "alias"
            ),
            storeId = UUID.randomUUID().toString(),
            state = "DISPATCH:DELIVERING",
            dailySequence = ++ValueStore.dailySequence,
            promiseDate = Instant.now(),
            payment = Payment(
                22.95,
                "EUR",
                "PROMO_CODE"
            ),
            orderItems = listOf(
                OrderItem(
                    quantity = 5,
                    product = Product(
                        id = UUID.randomUUID().toString(),
                        name = "Generic Beer",
                        category = "Drink",
                        grossWeight = 0.555,
                        image = "http://some-url"
                    )
                ),
                OrderItem(
                    quantity = 1,
                    product = Product(
                        id = UUID.randomUUID().toString(),
                        name = "Generic Salami",
                        category = "Food",
                        grossWeight = 0.100,
                        image = "http://some-url"
                    )
                )
            ),
            customer = Customer(
                id = UUID.randomUUID().toString(),
                firstName = "Gisele",
                lastName = "Bündchen",
                phoneNumber = "+554444444444",
                email = "gisele.bundchen@mail.com.br",
                totalOrdersCount = 5000
            ),
            tags = listOf(Tag.CANTINE.value),
            mainRiderContact = Contact(
                "Usain Bolt",
                "+496666666666"
            ),
            bags = emptyList(),
            tip = 2.0,
            requiresLegalCheck = true,
            additionalInformation = "bla bla bla",
            totalWeight = TotalWeight(
                unit = "KG",
                value = 0.655
            ),
            currencyISO = "EUR"
        )
    }

    object ValueStore {
        @JvmStatic
        var dailySequence = 0

        @JvmStatic
        var lastTourId = ""

        @JvmStatic
        var lastRiderId = ""
    }

    enum class FrichtiRoutingKey(val routingKey: String) {
        RIDER_UPSERTED("rider.upserted"),
        RIDER_DELETED("rider.deleted"),
        RIDER_ACTIVATED("rider.activated"),
        TOUR_CREATED("tour.created"),
        TOUR_UPDATED("tour.updated"),
        TOUR_REJECTED("tour.rejected"),
        ORDER_CREATED("order.created"),
        ORDER_STATE_UPDATED("order.state.updated"),
        TOUR_ROLLBACK("tour.update.rollback"),
        INVENTORY_UPDATED("inventory.updated");
    }
}
