package com.andrey.tickets.controller;

import com.andrey.tickets.api.CarriageApi;
import com.andrey.tickets.api.DirectionApi;
import com.andrey.tickets.api.ScheduleApi;
import com.andrey.tickets.api.SeatApi;
import com.andrey.tickets.api.TicketApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TicketsController {
    private static final String BASE_URL = "http://192.168.0.106:8080/com.api/rest/";

    private static TicketsController instance;

    private Retrofit retrofit;

    private TicketsController() {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static TicketsController getInstance() {
        if (instance == null) {
            instance = new TicketsController();
        }
        return instance;
    }

    public DirectionApi getDirectionApi(){
        return retrofit.create(DirectionApi.class);
    }

    public ScheduleApi getScheduleApi(){
        return retrofit.create(ScheduleApi.class);
    }

    public CarriageApi getCarriageApi(){
        return retrofit.create(CarriageApi.class);
    }

    public SeatApi getSeatApi(){
        return retrofit.create(SeatApi.class);
    }

    public TicketApi getTicketApi(){
        return retrofit.create(TicketApi.class);
    }
}
