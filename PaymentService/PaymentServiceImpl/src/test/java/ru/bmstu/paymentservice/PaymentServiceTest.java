package ru.bmstu.paymentservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.bmstu.paymentserivce.dto.PaymentResponseDto;
import ru.bmstu.paymentservice.mapper.PaymentMapperImpl;
import ru.bmstu.paymentservice.model.Payment;
import ru.bmstu.paymentservice.repository.PaymentRepository;
import ru.bmstu.paymentservice.service.PaymentService;
import ru.bmstu.paymentservice.service.impl.PaymentServiceImpl;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest(classes = { PaymentServiceImpl.class, PaymentMapperImpl.class })
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @MockBean
    private PaymentRepository paymentRepository;

    @BeforeEach
    public void setup() {
        Mockito.when(paymentRepository.save(Mockito.any(Payment.class)))
                .thenAnswer(i -> {
                    Payment payment = i.getArgument(0);
                    return Payment.builder().price(payment.getPrice()).build();
                });

        Mockito.when(paymentRepository.findByPaymentUid(Mockito.any(UUID.class)))
                .thenAnswer(i -> Optional.of(Payment.builder()
                        .paymentUid(i.getArgument(0))
                        .build()));
    }

    @Test
    public void createPaymentTest() {
        int price = 100;
        PaymentResponseDto result = paymentService.createPayment(price);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(price, result.price());
    }

    @Test
    public void cancelPaymentTest() {
        Assertions.assertDoesNotThrow(() -> paymentService.cancelPayment(UUID.randomUUID()));
    }

    @Test
    public void getDPaymentTest() {
        UUID testUid = UUID.randomUUID();
        Optional<PaymentResponseDto> result = paymentService.getPayment(testUid);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(testUid, result.get().paymentUid());
    }

}
