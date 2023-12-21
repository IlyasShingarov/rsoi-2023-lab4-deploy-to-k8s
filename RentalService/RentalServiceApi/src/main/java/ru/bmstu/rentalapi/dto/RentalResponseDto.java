package ru.bmstu.rentalapi.dto;


import ru.bmstu.rentalapi.constants.RentalStatus;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class RentalResponseDto {
    private UUID rentalUid;
    private String username;
    private UUID paymentUid;
    private UUID carUid;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private RentalStatus status;

    public RentalResponseDto(
            UUID rentalUid,
            String username,
            UUID paymentUid,
            UUID carUid,
            LocalDate dateFrom,
            LocalDate dateTo,
            RentalStatus status
    ) {
        this.rentalUid = rentalUid;
        this.username = username;
        this.paymentUid = paymentUid;
        this.carUid = carUid;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.status = status;
    }

    public UUID rentalUid() {
        return rentalUid;
    }

    public String username() {
        return username;
    }

    public UUID paymentUid() {
        return paymentUid;
    }

    public UUID carUid() {
        return carUid;
    }

    public LocalDate dateFrom() {
        return dateFrom;
    }

    public LocalDate dateTo() {
        return dateTo;
    }

    public RentalStatus status() {
        return status;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (RentalResponseDto) obj;
        return Objects.equals(this.rentalUid, that.rentalUid) &&
                Objects.equals(this.username, that.username) &&
                Objects.equals(this.paymentUid, that.paymentUid) &&
                Objects.equals(this.carUid, that.carUid) &&
                Objects.equals(this.dateFrom, that.dateFrom) &&
                Objects.equals(this.dateTo, that.dateTo) &&
                Objects.equals(this.status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rentalUid, username, paymentUid, carUid, dateFrom, dateTo, status);
    }

    @Override
    public String toString() {
        return "RentalResponseDto[" +
                "rentalUid=" + rentalUid + ", " +
                "username=" + username + ", " +
                "paymentUid=" + paymentUid + ", " +
                "carUid=" + carUid + ", " +
                "dateFrom=" + dateFrom + ", " +
                "dateTo=" + dateTo + ", " +
                "status=" + status + ']';
    }

}
