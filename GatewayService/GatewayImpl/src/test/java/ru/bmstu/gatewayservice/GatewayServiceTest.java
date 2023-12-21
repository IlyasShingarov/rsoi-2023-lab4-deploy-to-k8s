package ru.bmstu.gatewayservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import ru.bmstu.carservice.dto.CarResponseDto;
import ru.bmstu.carservice.feign.CarClient;
import ru.bmstu.gatewayservice.config.mapper.ModelMapperConfig;
import ru.bmstu.gatewayservice.service.GatewayService;
import ru.bmstu.gatewayservice.service.GatewayServiceImpl;
import ru.bmstu.paymentserivce.constants.PaymentStatus;
import ru.bmstu.paymentserivce.dto.PaymentResponseDto;
import ru.bmstu.paymentserivce.feign.PaymentClient;
import ru.bmstu.rentalapi.dto.RentalResponseDto;
import ru.bmstu.rentalapi.feign.RentalClient;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest(classes = { GatewayServiceImpl.class, ModelMapperConfig.class })
@MockBeans({
        @MockBean(CarClient.class),
        @MockBean(RentalClient.class),
        @MockBean(PaymentClient.class)
})
public class GatewayServiceTest {

    @Autowired private GatewayService gatewayService;
    @Autowired private CarClient carClient;
    @Autowired private RentalClient rentalClient;
    @Autowired private PaymentClient paymentClient;

    @BeforeEach
    public void setup() {
        UUID carUid1 = UUID.randomUUID();
        UUID carUid2 = UUID.randomUUID();
        UUID carUid3 = UUID.randomUUID();

        UUID paymentUid1 = UUID.randomUUID();
        UUID paymentUid2 = UUID.randomUUID();
        UUID paymentUid3 = UUID.randomUUID();

        Mockito.when(carClient.getCars(Mockito.anyBoolean(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(new PageImpl<CarResponseDto>(List.of(
                        new CarResponseDto(carUid1,
                                null, null, null,
                                0, null,0, true),
                        new CarResponseDto(carUid1,
                                null, null, null,
                                0, null,0, true),
                        new CarResponseDto(carUid3,
                                null, null, null,
                                0, null,0, true)
                        )));
        Mockito.when(rentalClient.getRentals(Mockito.anyString()))
                .thenAnswer(i -> {
                    var r1 = new RentalResponseDto();
                    r1.setCarUid(carUid1);
                    r1.setPaymentUid(paymentUid1);
                    r1.setUsername(i.getArgument(0));
                    var r2 = new RentalResponseDto();
                    r2.setCarUid(carUid2);
                    r2.setPaymentUid(paymentUid2);
                    r2.setUsername(i.getArgument(0));
                    var r3 = new RentalResponseDto();
                    r3.setCarUid(carUid3);
                    r3.setPaymentUid(paymentUid3);
                    r3.setUsername(i.getArgument(0));
                    return ResponseEntity.ok(List.of(r1, r2, r3));
                });
        Mockito.when(carClient.getCars(Mockito.anyList()))
                .thenReturn(List.of(
                        new CarResponseDto(carUid1,
                                null, null, null,
                                0, null,0, true),
                        new CarResponseDto(carUid2,
                                null, null, null,
                                0, null,0, true),
                        new CarResponseDto(carUid3,
                                null, null, null,
                                0, null,0, true)
                ));
        Mockito.when(paymentClient.getPayments(Mockito.anyList()))
                .thenReturn(List.of(
                        new PaymentResponseDto(paymentUid1, PaymentStatus.PAID, ThreadLocalRandom.current().nextInt()),
                        new PaymentResponseDto(paymentUid2, PaymentStatus.PAID, ThreadLocalRandom.current().nextInt()),
                        new PaymentResponseDto(paymentUid3, PaymentStatus.PAID, ThreadLocalRandom.current().nextInt())
                ));
        Mockito.when(rentalClient.getRental(Mockito.any(UUID.class), Mockito.anyString()))
                .thenAnswer(i -> {
                    RentalResponseDto rental = new RentalResponseDto();
                    rental.setPaymentUid(UUID.randomUUID());
                    rental.setUsername(i.getArgument(1));
                    rental.setRentalUid(i.getArgument(0));
                    rental.setCarUid(UUID.randomUUID());
                    return ResponseEntity.ok(rental);
                });
    }

    @Test
    public void getAllCarsTest() {
        var page = gatewayService.getAllCars(true, 1, 3);
        Assertions.assertNotNull(page);
        Assertions.assertEquals(3, page.getPageSize());
        Assertions.assertEquals(1, page.getTotalElements());
        Assertions.assertEquals(0, page.getPage());
        Assertions.assertNotNull(page.getItems().get(0));
    }

    @Test
    public void testGetRental() {
        String testUser = "testUser";
        var rentals = gatewayService.getRental(testUser);
        Assertions.assertNotNull(rentals);
        Assertions.assertEquals(3, rentals.size());
        rentals.forEach(rentalDto -> {
            Assertions.assertNotNull(rentalDto.getPayment());
            Assertions.assertNotNull(rentalDto.getCar());
        });
    }

    @Test
    public void bigTest() {
        String testUser = "testUser";
        UUID testUid = UUID.randomUUID();
        Assertions.assertDoesNotThrow(() -> gatewayService.cancelRental(testUser, testUid));
        Assertions.assertDoesNotThrow(() -> gatewayService.finishRental(testUser, testUid));
    }

}
