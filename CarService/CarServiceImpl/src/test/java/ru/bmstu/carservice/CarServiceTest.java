package ru.bmstu.carservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.bmstu.carservice.dto.CarResponseDto;
import ru.bmstu.carservice.entity.Car;
import ru.bmstu.carservice.mapper.CarMapperImpl;
import ru.bmstu.carservice.repository.CarRepository;
import ru.bmstu.carservice.service.CarService;
import ru.bmstu.carservice.service.impl.CarServiceImpl;

import java.util.*;

@SpringBootTest(classes = { CarServiceImpl.class, CarMapperImpl.class })
public class CarServiceTest {

    @Autowired
    private CarService carService;

    @MockBean
    private CarRepository carRepository;

    @BeforeEach
    public void setup() {
        Mockito.when(carRepository.findByCarUid(Mockito.any(UUID.class)))
                .thenAnswer(i -> Optional.of(Car.builder()
                        .id(1)
                        .carUid(i.getArgument(0))
                        .build())
                );

        Mockito.when(carRepository.save(Mockito.any(Car.class)))
                .thenAnswer(i -> i.getArgument(0));

        Mockito.when(carRepository.findAllByCarUidIn(Mockito.anySet()))
                .thenAnswer(i -> {
                    Set<UUID> uids = i.getArgument(0);
                    return List.of(
                            Car.builder().carUid(uids.iterator().next()).build(),
                            Car.builder().carUid(uids.iterator().next()).build(),
                            Car.builder().carUid(uids.iterator().next()).build()
                    );
                });
    }

    @Test
    public void getCarTest() {
        UUID testUid = UUID.randomUUID();
        CarResponseDto car = carService.getCar(testUid);

        Assertions.assertNotNull(car);
        Assertions.assertEquals(testUid, car.carUid());
    }

    @Test
    public void changeAvailabilityCarTest() {
        UUID testUid = UUID.randomUUID();
        Assertions.assertDoesNotThrow(() -> carService.changeAvailability(testUid));
    }

    @Test
    public void getCarsTest() {
        UUID uid1 = UUID.randomUUID();
        UUID uid2 = UUID.randomUUID();
        UUID uid3 = UUID.randomUUID();
        Set<UUID> uidSet = Set.of(uid1, uid2, uid3);
        var cars = carService.getCars(uidSet);
        cars.forEach(car -> Assertions.assertTrue(uidSet.contains(car.carUid())));
    }

}
