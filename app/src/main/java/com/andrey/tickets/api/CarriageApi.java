package com.andrey.tickets.api;

import com.andrey.tickets.model.Carriage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CarriageApi {
    @GET("carriages")
    Call<List<Carriage>> getAll();
}
