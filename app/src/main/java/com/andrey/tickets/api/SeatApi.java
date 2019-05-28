package com.andrey.tickets.api;

import com.andrey.tickets.model.Seat;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SeatApi {
    @POST("seats")
    Call<Seat> save(@Body Seat seat);

    @PUT("seats")
    Call<Seat> update(@Body Seat seat);

    @GET("seats/train/{id}")
    Call<List<Seat>> getByTrainId(@Path("id") Long id);
}
