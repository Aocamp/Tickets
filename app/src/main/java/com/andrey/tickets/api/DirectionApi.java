package com.andrey.tickets.api;

import com.andrey.tickets.model.Direction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DirectionApi {
    @GET("directions")
    Call<List<Direction>> getAll();
}
