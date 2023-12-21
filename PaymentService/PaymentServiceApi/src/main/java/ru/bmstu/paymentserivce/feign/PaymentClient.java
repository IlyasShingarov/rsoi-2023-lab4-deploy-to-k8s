package ru.bmstu.paymentserivce.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.paymentserivce.dto.PaymentResponseDto;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "payment", path = "/payment", url = "${feign.payment.url}")
public interface PaymentClient {
    @GetMapping(path = "/payment")
    List<PaymentResponseDto> getPayments(@RequestParam List<UUID> paymentsUids);

    @GetMapping(path = "/payment/{paymentUid}")
    PaymentResponseDto getPayment(@PathVariable UUID paymentUid);

    @PostMapping(path = "/payment")
    PaymentResponseDto createPayment(@RequestBody int price);

    @DeleteMapping(path = "/payment/{paymentUid}")
    void cancelPayment(@PathVariable UUID paymentUid);
}
