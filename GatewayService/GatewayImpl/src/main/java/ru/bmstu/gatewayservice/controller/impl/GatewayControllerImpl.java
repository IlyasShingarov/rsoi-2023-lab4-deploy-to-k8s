package ru.bmstu.gatewayservice.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.bmstu.carservice.dto.CarResponseDto;
import ru.bmstu.gatewayservice.controller.GatewayConroller;
import ru.bmstu.gatewayservice.dto.CarRentDto;
import ru.bmstu.gatewayservice.dto.car.CarDto;
import ru.bmstu.gatewayservice.dto.rental.RentalCreateDto;
import ru.bmstu.gatewayservice.dto.rental.RentalDto;
import ru.bmstu.gatewayservice.dto.wrapper.PageableWrapperDto;
import ru.bmstu.gatewayservice.service.GatewayService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GatewayControllerImpl implements GatewayConroller {

    private final GatewayService gatewayService;

    @Override
    public PageableWrapperDto<CarDto> getAllCars(boolean showAll, int page, int size) {
        log.info("Request for reading cars. Request params: showAll {}, page {}, size {}", showAll, page, showAll);

        return gatewayService.getAllCars(showAll, page, size);
    }

    @Override
    public ResponseEntity<List<RentalDto>> getRental(String username) {
        log.info("Request for reading all rental info of user {}", username);

        return ResponseEntity.ok(gatewayService.getRental(username));
    }

    @Override
    public ResponseEntity<RentalDto> getRental(String username, UUID rentalUid) {
        log.info("Request for reading user's {} rental {}", username, rentalUid);

        return ResponseEntity.ok(gatewayService.getRental(username, rentalUid));
    }

    @Override
    public ResponseEntity<RentalCreateDto> bookCar(String userName, CarRentDto carBookDto) {
        log.info("Request for booking car {} by user {}", carBookDto, userName);

        RentalCreateDto rentalCreationOutDto = gatewayService.bookCar(userName, carBookDto);

        return ResponseEntity.ok(rentalCreationOutDto);
    }

    @Override
    public ResponseEntity<?> finishRental(String username, UUID rentalUid) {
        log.info("Request for finishing rental {} of user {}", rentalUid, username);

        gatewayService.finishRental(username, rentalUid);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> cancelRental(String username, UUID rentalUid) {
        log.info("Request for canceling rental {} of user {}", rentalUid, username);

        gatewayService.cancelRental(username, rentalUid);

        return ResponseEntity.noContent().build();
    }
}
