package io.gorillas.touringhelper.view

import io.gorillas.touringhelper.service.TourService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tour")
class TourController(private val tourService: TourService) {

    @PostMapping("/create")
    suspend fun createTourData(): ResponseEntity<String> {
        tourService.createFrichtiTourData()
        return ResponseEntity.accepted().build()
    }

    @PostMapping("/update")
    suspend fun updateTourData(): ResponseEntity<String> {
        tourService.createFrichtiTourData()
        return ResponseEntity.accepted().build()
    }
}
