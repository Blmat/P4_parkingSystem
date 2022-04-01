package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null)
                || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        Duration duration = Duration.between(ticket.getInTime().toInstant(),
                ticket.getOutTime().toInstant());
        //Gratuit si moins de 30mins
        if (duration.toMinutes() < 30) {
            ticket.setPrice(0f);
        } else {
            // Duration en heure: 60f = conversion en double
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(duration.toMinutes() / 60f * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration.toMinutes() / 60f * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default: case DEFAULT:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        }
    }
    public void discountForRegularCustomer(Ticket ticket,double discount) {
        calculateFare(ticket);
        ticket.setPrice(ticket.getPrice()* Math.ceil(discount));

    }
}