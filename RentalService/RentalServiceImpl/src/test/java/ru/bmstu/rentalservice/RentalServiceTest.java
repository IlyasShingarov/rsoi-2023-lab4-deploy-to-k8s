package ru.bmstu.rentalservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.bmstu.rentalapi.dto.RentalRequestDto;
import ru.bmstu.rentalapi.dto.RentalResponseDto;
import ru.bmstu.rentalservice.mapper.RentalMapperImpl;
import ru.bmstu.rentalservice.model.Rental;
import ru.bmstu.rentalservice.repository.RentalRepository;
import ru.bmstu.rentalservice.service.RentalService;
import ru.bmstu.rentalservice.service.RentalServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(classes = { RentalServiceImpl.class, RentalMapperImpl.class })
public class RentalServiceTest {

    @Autowired
    private RentalService rentalService;

    @MockBean
    private RentalRepository rentalRepository;

    @BeforeEach
    public void setup() {
        Mockito.when(rentalRepository.save(Mockito.any(Rental.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        Mockito.when(rentalRepository.findByUsernameAndRentalUid(Mockito.any(String.class), Mockito.any(UUID.class)))
                .thenReturn(Optional.of(Rental.builder().build()));

        Mockito.when(rentalRepository.findAllByUsername(Mockito.any(String.class)))
                .thenReturn(List.of(
                        Rental.builder()
                                .id(1)
                                .username("TestUser")
                                .rentalUid(UUID.randomUUID())
                                .carUid(UUID.randomUUID())
                                .build(),
                        Rental.builder()
                                .id(2)
                                .username("TestUser")
                                .rentalUid(UUID.randomUUID())
                                .carUid(UUID.randomUUID())
                                .build(),
                        Rental.builder()
                                .id(3)
                                .username("TestUser")
                                .rentalUid(UUID.randomUUID())
                                .carUid(UUID.randomUUID())
                                .build()
                ));
    }

    @Test
    public void testCreateRental() {
        String testUsername = "TestUser";
        RentalRequestDto request = new RentalRequestDto(
                UUID.randomUUID(), UUID.randomUUID(), LocalDate.now(), LocalDate.now().plusDays(1)
        );

        RentalResponseDto result = rentalService.createRental(testUsername, request);

        Assertions.assertEquals(testUsername, result.getUsername());
        Assertions.assertEquals(request.carUid(), result.getCarUid());
        Assertions.assertEquals(request.paymentUid(), result.getPaymentUid());
        Assertions.assertEquals(request.dateFrom(), result.getDateFrom());
        Assertions.assertEquals(request.dateTo(), result.getDateTo());
    }

    @Test
    public void testGetRentals() {
        String testUsername = "TestUser";
        List<RentalResponseDto> rentals = rentalService.getRentals(testUsername);
        Assertions.assertNotNull(rentals);
        Assertions.assertEquals(3, rentals.size());
    }

    @Test
    public void testFinishRental() {
        UUID testUid = UUID.randomUUID();
        String testUsername = "TestUser";
        Assertions.assertDoesNotThrow(() -> rentalService.finishRental(testUid, testUsername));
    }

    @Test
    public void testCancelRental() {
        UUID testUid = UUID.randomUUID();
        String testUsername = "TestUser";
        Assertions.assertDoesNotThrow(() -> rentalService.cancelRental(testUid, testUsername));
    }
}
