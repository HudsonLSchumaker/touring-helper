package io.gorillas.touringhelper.model

data class FrichtiTour(
    val tourId: String,
    val riderId: String?,
    val vehicle: String?,
    val additionalRiders: List<String>?,
    val orders: List<FrichtiOrder>, // using FrichtiOrder
    val tourState: FrichtiTourState
)

data class FrichtiTourOrder(
    val orderId: String,
    val tourSequenceNumber: Int,
    val internalPromisedEta: String?
)

enum class FrichtiTourState {
    CREATED,
    PREPARING,
    PREPARED,
    READY,
    TOURING,
    DONE;
}
